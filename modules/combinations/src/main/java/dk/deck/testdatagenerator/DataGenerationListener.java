/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator;

/**
 * This listener is used for callbacks, when data is generated normally.
 * 
 * @author Jesper Terkelsen
 */
public interface DataGenerationListener<T> {
    /**
     * Called when a combination has been created.
     * 
     * @param value The data generated.
     */
    public void onDataGenerated(T value);
}
