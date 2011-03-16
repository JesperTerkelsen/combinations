/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator.concurrent;

/**
 * Wrapper class for that supports waiting for a task to complete
 * @author jt
 */
public interface Waitable {
    /**
     * Causes the current thread to sleep until a certain event has happened
     */
    public void await() throws InterruptedException;
}
