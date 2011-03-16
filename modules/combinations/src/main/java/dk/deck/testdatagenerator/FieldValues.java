/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator;

import java.util.Map;
import java.util.Set;

/**
 * An interface for supplying default field values, for varous types.
 * 
 * Usefull to implement if you have some predefined data for your own types
 * 
 * @author Jesper Terkelsen
 */
public interface FieldValues {

    /**
     * 
     * @return A map that contains a list of possible values for each 
     */
    Map<Class, Set<?>> getDefaultFieldValues();

}
