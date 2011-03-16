/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator;

/**
 * This listener is used for callbacks, in a concurrent data generation setup.
 * 
 * @author Jesper Terkelsen
 */
public interface DataGenerationConcurrentListener<T> {
    /**
     * Callback when a instance has been generated.
     * 
     * @param value The value of the data
     * @param threadNum The thread number that is calling you.
     */
    public void onDataGenerated(T value, int threadNum);
}
