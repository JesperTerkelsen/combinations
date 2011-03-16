/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.deck.testdatagenerator.concurrent;

import dk.deck.testdatagenerator.DataGenerationConcurrentListener;
import dk.deck.testdatagenerator.DataGenerationListener;
import dk.deck.testdatagenerator.generator.DataGeneratorImpl;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jesper Terkelsen
 */
public class ConsumerThread<T> extends Thread {

    private final int num;
    private final BlockingQueue<T> queue;
    private final DataGenerationConcurrentListener<T> listener;
    private final CountDownLatch latch;
    private final T poisonObject;

    public ConsumerThread(int num, BlockingQueue<T> queue, DataGenerationConcurrentListener<T> listener, CountDownLatch latch, T poisonObject) {
        this.queue = queue;
        this.listener = listener;
        this.latch = latch;
        this.num = num;
        this.poisonObject = poisonObject;
    }

    @Override
    public void run() {
        boolean running = true;
        try {
            while (running) {
                T instance = queue.take();
                if (instance != poisonObject) {
                    listener.onDataGenerated(instance, num);
                } else {
                    running = false;
                    queue.put(poisonObject);
                    latch.countDown();
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(DataGeneratorImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
