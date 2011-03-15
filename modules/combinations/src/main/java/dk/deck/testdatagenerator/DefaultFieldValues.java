/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jesper Terkelsen
 */
public class DefaultFieldValues implements FieldValues {
    public final Map<Class, List<?>> getDefaultFieldValues(){
        Map<Class, List<?>> defaultValues = new HashMap<Class, List<?>>();
        defaultValues.put(boolean.class, getBooleans());
        defaultValues.put(Boolean.class, getWrapperBooleans());
        defaultValues.put(int.class, getIntegers());
        defaultValues.put(Integer.class, getWrapperIntegers());
        defaultValues.put(String.class, getStrings());
        
        return defaultValues;
    }
    
    protected List<Float> getFloats(){
        List<Float> result = new ArrayList<Float>();
        result.add(-0.5f);
        result.add(0.0f);
        result.add(0.5f);
        return result;        
    }

    protected List<Float> getDoubles(){
        List<Float> result = new ArrayList<Float>();
        result.add(-0.5f);
        result.add(0.0f);
        result.add(0.5f);
        return result;        
    }
    
    
    protected List<String> getStrings(){
        List<String> result = new ArrayList<String>();
        result.add(null);
        result.add("");
        result.add("non empthy string");
        return result;        
    }
    
    
    protected  List<Integer> getWrapperIntegers(){
        List<Integer> result = new ArrayList<Integer>();
        result.add(null);
        result.addAll(getIntegers());
        return result;
    }
    
    protected List<Integer> getIntegers(){
        List<Integer> result = new ArrayList<Integer>();
        result.add(-1);
        result.add(0);
        result.add(1);
        return result;
    }
    
    protected  List<Boolean> getWrapperBooleans(){
        List<Boolean> result = new ArrayList<Boolean>();
        result.add(null);
        result.addAll(getBooleans());
        return result;
    }
    
    
    protected  List<Boolean> getBooleans(){
        List<Boolean> result = new ArrayList<Boolean>();
        result.add(Boolean.TRUE);
        result.add(Boolean.FALSE);
        return result;
    }
}
