/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default field values, this class will be used by DataGenerator if no other is supplied.
 * 
 * @author Jesper Terkelsen
 */
public class DefaultFieldValues implements FieldValues {
    public Map<Class, Set<?>> getDefaultFieldValues(){
        Map<Class, Set<?>> defaultValues = new HashMap<Class, Set<?>>();
        defaultValues.put(boolean.class, getBooleans());
        defaultValues.put(Boolean.class, getWrapperBooleans());
        defaultValues.put(int.class, getIntegers());
        defaultValues.put(Integer.class, getWrapperIntegers());
        defaultValues.put(String.class, getStrings());
        
        return defaultValues;
    }
    
    protected Set<Float> getFloats(){
        Set<Float> result = new LinkedHashSet<Float>();
        result.add(-0.5f);
        result.add(0.0f);
        result.add(0.5f);
        return result;        
    }

    protected Set<Float> getDoubles(){
        Set<Float> result = new LinkedHashSet<Float>();
        result.add(-0.5f);
        result.add(0.0f);
        result.add(0.5f);
        return result;        
    }
    
    
    protected Set<String> getStrings(){
        Set<String> result = new LinkedHashSet<String>();
        result.add(null);
        result.add("");
        result.add("non empthy string");
        return result;        
    }
    
    
    protected  Set<Integer> getWrapperIntegers(){
        Set<Integer> result = new LinkedHashSet<Integer>();
        result.add(null);
        result.addAll(getIntegers());
        return result;
    }
    
    protected Set<Integer> getIntegers(){
        Set<Integer> result = new LinkedHashSet<Integer>();
        result.add(-1);
        result.add(0);
        result.add(1);
        return result;
    }
    
    protected  Set<Boolean> getWrapperBooleans(){
        Set<Boolean> result = new LinkedHashSet<Boolean>();
        result.add(null);
        result.addAll(getBooleans());
        return result;
    }
    
    
    protected  Set<Boolean> getBooleans(){
        Set<Boolean> result = new LinkedHashSet<Boolean>();
        result.add(Boolean.TRUE);
        result.add(Boolean.FALSE);
        return result;
    }
}
