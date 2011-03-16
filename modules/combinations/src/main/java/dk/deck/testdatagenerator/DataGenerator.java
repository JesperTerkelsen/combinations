/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.deck.testdatagenerator;

import dk.deck.testdatagenerator.concurrent.Waitable;
import java.util.List;
import java.util.Set;

/**
 * Generate combinatorics of primitives on the selected class
 * 
 * @author Jesper Terkelsen
 */
public interface DataGenerator<T> {

    
    /**
     * Do not set values on this field at all
     * @param propertyName
     */
    void disableField(String propertyName);

    /**
     * This is the non streaming way to call the API.
     * Be aware that this can use a lot of memory
     *
     * @return The list of instances created
     */
    List<T> generateData() throws InstantiationException, IllegalAccessException;

    /**
     * This is the normal way to call the API, which streames out the results.
     *
     * @param listener The listener will get a callback for ecah instance created.
     */
    void generateData(DataGenerationListener<T> listener) throws InstantiationException, IllegalAccessException;

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
    Waitable generateDataConcurrent(DataGenerationConcurrentListener<T> listener, int numthreads, int buffersize) throws InstantiationException, IllegalAccessException;

    /**
     *
     * @return calculate the number of permutations of objects that can be generated on the type, based on the current fieldValues
     */
    int getNumberOfPermutations();

    /**
     * Sets the field values for a specific field.
     * Must be called before generatedata
     *
     * @param propertyName the property name
     * @param fieldValues the possible values
     */
    <F> void setFieldValues(String propertyName, Set<F> fieldsValues);
}
