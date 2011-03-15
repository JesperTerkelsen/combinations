/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator.reflect;

import java.lang.reflect.Method;

/**
 *
 * @author Jesper Terkelsen
 */
public class ReflectTypeUtil {

    public static  String getPropertyName(Method method) {
        String methodName = method.getName();
        if (methodName.startsWith("get")){
            return lowerCaseFirstChar(methodName.replaceFirst("get", ""));
        }
        if (methodName.startsWith("set")){
            return lowerCaseFirstChar(methodName.replaceFirst("set", ""));
        }
        if (methodName.startsWith("is")){
            return lowerCaseFirstChar(methodName.replaceFirst("is", ""));
        }
        if (methodName.startsWith("has")){
            return lowerCaseFirstChar(methodName.replaceFirst("has", ""));
        }
        else throw new IllegalArgumentException("Method " + method + " is not a property accessor");
    }

    public static String lowerCaseFirstChar(String propertyName){
        if (propertyName == null){
            throw new IllegalArgumentException("PropertyName is null");
        }
        if (propertyName.length() == 0){
            throw new IllegalArgumentException("PropertyName is empthy" + propertyName);
        }
        char firstChar = propertyName.charAt(0);
        String rest = propertyName.substring(1);
        char lowerCaseFirstChar = Character.toLowerCase(firstChar);
        String result = lowerCaseFirstChar + rest;
        return result;
    }

    public static boolean isGetter(Method method) {
        String methodName = method.getName();
        Class type = method.getReturnType();
        Class[] parameters = method.getParameterTypes();
        if (type == Void.TYPE){
            return false;
        }
        if (parameters.length != 0){
            return false;
        }
        if (methodName.startsWith("get")){
            return true;
        }
        if (methodName.startsWith("is") && type == Boolean.TYPE){
            return true;
        }
        if (methodName.startsWith("has") && type == Boolean.TYPE){
            return true;
        }
        return false;
    }

    public static boolean isSetter(Method method){
        String methodName = method.getName();
        Class type = method.getReturnType();
        Class[] parameters = method.getParameterTypes();
        if (type != Void.TYPE){
            return false;
        }
        if (parameters.length != 1){
            return false;
        }
        if (methodName.startsWith("set")){
            return true;
        }
        return false;
    }

    public static Class getGetterPropertyType(Method getter){
        Class result = getter.getReturnType();
        return result;
    }

    public static Class getSetterPropertyType(Method setter){
        Class result = setter.getParameterTypes()[0];
        return result;
    }
}
