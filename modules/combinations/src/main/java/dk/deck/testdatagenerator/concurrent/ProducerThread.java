/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator.concurrent;

import dk.deck.testdatagenerator.algorithm.Algorithm;
import dk.deck.testdatagenerator.DataGenerator;
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
public class ProducerThread<T> extends Thread{

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
            Map<String, Integer> fieldIndexes = new HashMap<String, Integer>();
            Map<String, Integer> maxValues = new HashMap<String, Integer>();
            Map<String, List<?>> fieldValuesLocal = new HashMap();
            for (Entry<String, Set<?>> entry : fieldValues.entrySet()) {
                String key = entry.getKey();
                Set<?> value = entry.getValue();
                fieldValuesLocal.put(key, new ArrayList(value));
            }
            Set<String> supportedFieldsLocal = new LinkedHashSet<String>(supportedFields);

            System.out.println("Generating instances for supported fields");
            for (String property : supportedFieldsLocal) {
                System.out.println("Field: " + property + " values: " + fieldValuesLocal.get(property));
                fieldIndexes.put(property, 0);
                maxValues.put(property, fieldValuesLocal.get(property).size() - 1);
            }
            try {
                // Algoritm
                boolean running = true;
                while (running) {

                    T instance = beanInfo.getType().newInstance();
                    for (String property : supportedFieldsLocal) {
                        Object value = fieldValuesLocal.get(property).get(fieldIndexes.get(property));
                        beanInfo.setProperty(instance, property, value);
                    }
                    // listener.onDataGenerated(instance);
                    queue.put(instance);
                    running = Algorithm.advance(fieldIndexes, maxValues, supportedFieldsLocal);

                }
                queue.put(poisonObject);

                // TODO, fix so testcase fails after this.
            } catch (InterruptedException ex) {
                Logger.getLogger(DataGenerator.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(DataGenerator.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(DataGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
}
