/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.deck.testdatagenerator.generator;

import dk.deck.testdatagenerator.DataGenerationConcurrentListener;
import dk.deck.testdatagenerator.DataGenerationListener;
import dk.deck.testdatagenerator.DataGenerator;
import dk.deck.testdatagenerator.DefaultFieldValues;
import dk.deck.testdatagenerator.FieldValues;
import dk.deck.testdatagenerator.algorithm.Algorithm;
import dk.deck.testdatagenerator.concurrent.ConsumerThread;
import dk.deck.testdatagenerator.concurrent.CountDownLatchWaitable;
import dk.deck.testdatagenerator.concurrent.ProducerThread;
import dk.deck.testdatagenerator.concurrent.Waitable;
import dk.deck.testdatagenerator.reflect.ReflectBeanInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Generate combinatorics of primitives on the selected class
 * 
 * @author Jesper Terkelsen
 */
public class DataGeneratorImpl<T> implements DataGenerator<T> {

    // Default field values to use on any field
    private final Map<Class, Set<?>> defaultFieldValues;
    private final Map<String, Set<?>> fieldValues = new HashMap<String, Set<?>>();
    private final Set<String> supportedFields = new LinkedHashSet<String>();
    private final ReflectBeanInfo<T> beanInfo;

    private DataGeneratorImpl(Class type) {
        this(type, new DefaultFieldValues());
    }

    private DataGeneratorImpl(Class type, FieldValues fieldValues) {
        this.defaultFieldValues = fieldValues.getDefaultFieldValues();
        this.beanInfo = new ReflectBeanInfo<T>(type);
        analyze();
    }

    public static <T> DataGeneratorImpl getInstance(Class<T> type){
        return new DataGeneratorImpl<T>(type);
    }
    
    public static <T> DataGeneratorImpl getInstance(Class<T> type, FieldValues fieldValues){
        return new DataGeneratorImpl<T>(type, fieldValues);
    }    
    
    /**
     * Sets the field values for a specific field. 
     * Must be called before generatedata
     * 
     * @param propertyName the property name
     * @param fieldValues the possible values
     */
    @Override
    public <F> void setFieldValues(String propertyName, Set<F> fieldsValues) {
        // Validate that all the field values are the right tyoe
        // Validate that all the field values are different from each other.
        if (!beanInfo.isWriteable(propertyName)) {
            throw new IllegalArgumentException("Property " + propertyName + " has no setter method");
        }
        if (fieldsValues.isEmpty()) {
            throw new IllegalArgumentException("fieldValues must at least have one value in it.");
        }

        fieldValues.put(propertyName, fieldsValues);
        supportedFields.add(propertyName);
    }

    /**
     * Do not set values on this field at all
     * @param propertyName 
     */
    @Override
    public void disableField(String propertyName) {
        fieldValues.remove(propertyName);
        supportedFields.remove(propertyName);
    }

    /**
     * This is the non streaming way to call the API. 
     * Be aware that this can use a lot of memory
     * 
     * @return The list of instances created
     */
    @Override
    public List<T> generateData() throws InstantiationException, IllegalAccessException {
        final List<T> result = new ArrayList<T>();
        generateData(new DataGenerationListener<T>() {

            public void onDataGenerated(T value) {
                result.add(value);
            }
        });
        return result;
    }

    /**
     * This is the normal way to call the API, which streames out the results.
     * 
     * @param listener The listener will get a callback for ecah instance created.
     */
    @Override
    public void generateData(DataGenerationListener<T> listener) throws InstantiationException, IllegalAccessException {
        Algorithm.generateData(beanInfo, fieldValues, supportedFields, listener);
    }

    /**
     * Lige generateData() with the listner for streaming callback, 
     * but this one splits the task up in a numer of threads, 
     * and the callback will be called from those threads.
     * 
     * Also note that this method will return emidiatly.
     * 
     * @param listener
     * @param numthreads 
     * @return A handle to use if you wish to wait for the threads to finish
     */
    @Override
    public Waitable generateDataConcurrent(DataGenerationConcurrentListener<T> listener, int numthreads, int buffersize) throws InstantiationException, IllegalAccessException {
        // Split up the task based on the number of threads.
        // Start each thread
        // Create a countdownlatch, that waits for all threads to finish.
        T poisonObject = beanInfo.getType().newInstance();
        CountDownLatch latch = new CountDownLatch(numthreads);
        BlockingQueue<T> queue = new LinkedBlockingQueue<T>(buffersize);
        ProducerThread producer = new ProducerThread(queue, beanInfo, fieldValues, supportedFields, poisonObject);
        producer.start();        
        for (int i = 0; i < numthreads; i++) {
            ConsumerThread consumer = new ConsumerThread(i, queue, listener, latch, poisonObject);
            consumer.start();
        }
        return new CountDownLatchWaitable(latch);
    }



    /**
     * 
     * @return calculate the number of permutations of objects that can be generated on the type, based on the current fieldValues
     */
    @Override
    public int getNumberOfPermutations() {
        int count = 0;
        for (String property : supportedFields) {
            int size = fieldValues.get(property).size();
            if (count == 0) {
                count = size;
            } else {
                count *= size;
            }
        }
        return count;
    }

    private void analyze() {
        List<String> propertyNames = beanInfo.getPropertyNames();
        for (String property : propertyNames) {
            // System.out.println("Property " + property);
            Class propertyType = beanInfo.getType(property);
            if (defaultFieldValues.containsKey(propertyType) && beanInfo.isWriteable(property)) {
                setFieldValues(property, new LinkedHashSet(defaultFieldValues.get(propertyType)));
            }
        }
    }
}
