/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Jesper Terkelsen
 */
public interface FieldValues {

    Map<Class, List<?>> getDefaultFieldValues();

}
