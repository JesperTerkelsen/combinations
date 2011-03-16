/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator.algorithm;

import dk.deck.testdatagenerator.DataGenerationListener;
import dk.deck.testdatagenerator.reflect.ReflectBeanInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A small class for static algoritm methods, that are shared.
 * @author Jesper Terkelsen
 */
public class Algorithm {

    public static <T> void generateData(ReflectBeanInfo<T> beanInfo, Map<String, Set<?>> fieldValues, Set<String> supportedFields, DataGenerationListener listener) throws InstantiationException, IllegalAccessException{
                // Initialization of data
        Map<String, Integer> fieldIndexes = new HashMap<String, Integer>();
        Map<String, Integer> maxValues = new HashMap<String, Integer>();
        Map<String, List<?>> fieldValuesLocal = new HashMap();
        for (Entry<String, Set<?>> entry : fieldValues.entrySet()) {
            String key = entry.getKey();
            Set<?> value = entry.getValue();
            fieldValuesLocal.put(key, new ArrayList(value));
        }
        Set<String> supportedFieldsLocal = new LinkedHashSet<String>(supportedFields);

        // TODO make this logging configurable
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
            listener.onDataGenerated(instance);
            running = advance(fieldIndexes, maxValues, supportedFieldsLocal);
        }

    }
    
    /**
     * Modifies the fieldIndexes for the next position.
     * 
     * This is bacially a algoritm for matrix traversal
     * 
     * @param fieldIndexes the map to modify
     * @return false if we are at the last position, all positions are reset to 0 adfter this call
     */
    private static boolean advance(Map<String, Integer> fieldIndexes, Map<String, Integer> maxValues, Set<String> supportedFields) {
        // System.out.println("");
        for (String property : supportedFields) {
            int value = fieldIndexes.get(property);
            int max = maxValues.get(property);
            if (value < max) {
                int newValue = value + 1;
                // System.out.println("Advancing " + property + " to " + newValue + " max " + max);
                fieldIndexes.put(property, newValue);
                return true;
            }
            else { // value == max, should never be > max
                // System.out.println("Resetting " + property);
                fieldIndexes.put(property, 0); // Reset to zero, and go to next field
            }
        }
        return false;
    }
}
