/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.deck.testdatagenerator;

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
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Generate combinatorics of primitives on the selected class
 * 
 * @author Jesper Terkelsen
 */
public class DataGenerator<T> {

    // Default field values to use on any field
    private final Map<Class, List<?>> defaultFieldValues;
    private final Map<String, List<?>> fieldValues = new HashMap<String, List<?>>();
    private final Set<String> supportedFields = new LinkedHashSet<String>();
    private final ReflectBeanInfo<T> beanInfo;

    public DataGenerator(Class type) {
        this(type, new DefaultFieldValues());
    }

    public DataGenerator(Class type, FieldValues fieldValues) {
        this.defaultFieldValues = fieldValues.getDefaultFieldValues();
        this.beanInfo = new ReflectBeanInfo<T>(type);
        analyze();
    }

    /**
     * Sets the field values for a specific field. 
     * Must be called before generatedata
     * 
     * @param propertyName the property name
     * @param fieldValues the possible values
     */
    public <F> void setFieldValues(String propertyName, List<F> fieldsValues) {
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
    public List<T> generateData() throws InstantiationException, IllegalAccessException {
        final List<T> result = new ArrayList<T>();
        generateData(new DataGenerationListener<T>() {

            public void onDataGenerated(T value, int threadNum) {
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
    public void generateData(DataGenerationListener<T> listener) throws InstantiationException, IllegalAccessException {
        // Initialization of data
        Map<String, Integer> fieldIndexes = new HashMap<String, Integer>();
        Map<String, Integer> maxValues = new HashMap<String, Integer>();
        Map<String, List<?>> fieldValuesLocal = new HashMap();
        for (Entry<String, List<?>> entry : fieldValues.entrySet()) {
            String key = entry.getKey();
            List<?> value = entry.getValue();
            fieldValuesLocal.put(key, new ArrayList(value));
        }
        Set<String> supportedFieldsLocal = new LinkedHashSet<String>(supportedFields);

        System.out.println("Generating instances for supported fields");
        for (String property : supportedFieldsLocal) {
            System.out.println("Field: " + property + " values: " + fieldValuesLocal.get(property));
            fieldIndexes.put(property, 0);
            maxValues.put(property, fieldValuesLocal.get(property).size() - 1);
        }

        // Algoritm
        boolean running = true;
        while (running) {
            T instance = beanInfo.getType().newInstance();
            for (String property : supportedFieldsLocal) {
                Object value = fieldValuesLocal.get(property).get(fieldIndexes.get(property));
                beanInfo.setProperty(instance, property, value);
            }
            listener.onDataGenerated(instance, 0);
            running = Calculation.advance(fieldIndexes, maxValues, supportedFieldsLocal);
        }

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
    public Waitable generateDataConcurent(DataGenerationListener<T> listener, int numthreads, int buffersize) throws InstantiationException, IllegalAccessException {
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
                setFieldValues(property, new ArrayList(defaultFieldValues.get(propertyType)));
            }
        }
    }
}
