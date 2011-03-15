/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator;

import java.util.Map;
import java.util.Set;

/**
 * A small class for static algoritm methods, that are shared.
 * @author Jesper Terkelsen
 */
public class Calculation {

    /**
     * Modifies the fieldIndexes for the next position
     * 
     * @param fieldIndexes the map to modify
     * @return false if we are at the last position, all positions are reset to 0 adfter this call
     */
    public static boolean advance(Map<String, Integer> fieldIndexes, Map<String, Integer> maxValues, Set<String> supportedFields) {
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
