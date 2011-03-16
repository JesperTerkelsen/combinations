/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator;

import dk.deck.testdatagenerator.generator.DataGeneratorImpl;

/**
 *
 * @author Jesper Terkelsen
 */
public class DataGeneratorFactory {
    public static DataGenerator getDataGenerator(Class type){
        return DataGeneratorImpl.getInstance(type);
    }
    
    public static DataGenerator getDataGenerator(Class type, FieldValues fieldValues){
        return DataGeneratorImpl.getInstance(type, fieldValues);
    }
}
