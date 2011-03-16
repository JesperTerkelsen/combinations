/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.deck.testdatagenerator.concurrent;

import dk.deck.testdatagenerator.DataGenerationListener;
import dk.deck.testdatagenerator.algorithm.Algorithm;
import dk.deck.testdatagenerator.generator.DataGeneratorImpl;
import dk.deck.testdatagenerator.reflect.ReflectBeanInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jesper Terkelsen
 */
public class ProducerThread<T> extends Thread {

    private final BlockingQueue<T> queue;
    private final ReflectBeanInfo<T> beanInfo;
    private final Map<String, Set<?>> fieldValues;
    private final Set<String> supportedFields;
    private final T poisonObject;

    public ProducerThread(BlockingQueue<T> queue, ReflectBeanInfo<T> beanInfo, Map<String, Set<?>> fieldValues, Set<String> supportedFields, T poisonObject) {
        this.queue = queue;
        this.beanInfo = beanInfo;
        this.fieldValues = fieldValues;
        this.supportedFields = supportedFields;
        this.poisonObject = poisonObject;
    }

    @Override
    public void run() {
        try {
            Algorithm.generateData(beanInfo, fieldValues, supportedFields, new DataGenerationListener<T>() {

                public void onDataGenerated(T value) {
                    try {
                        queue.put(value);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ProducerThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            // TODO, fix so testcase fails after this.
        } catch (InstantiationException ex) {
            Logger.getLogger(DataGeneratorImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DataGeneratorImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                queue.put(poisonObject);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProducerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
