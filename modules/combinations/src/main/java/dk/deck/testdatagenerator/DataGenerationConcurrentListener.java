/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator;

/**
 *
 * @author Jesper Terkelsen
 */
public interface DataGenerationConcurrentListener<T> {
    public void onDataGenerated(T value, int threadNum);
}
