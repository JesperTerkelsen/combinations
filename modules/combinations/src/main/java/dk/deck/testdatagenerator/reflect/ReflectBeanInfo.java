/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.deck.testdatagenerator.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Jesper Terkelsen
 */
public class ReflectBeanInfo<T>  {

    private Class<T> type;

    public ReflectBeanInfo(Class<T> type) {
        this.type = type;
    }

    public List<String> getPropertyNames() {
        Set propertyNames = new LinkedHashSet();
        Method[] methods = type.getMethods();
        for (Method method : methods) {
            if (ReflectTypeUtil.isGetter(method)) {
                String name = ReflectTypeUtil.getPropertyName(method);
                propertyNames.add(name);
            } else if (ReflectTypeUtil.isSetter(method)) {
                String name = ReflectTypeUtil.getPropertyName(method);
                propertyNames.add(name);
            }
        }
        return new ArrayList<String>(propertyNames);
    }

    public Class getType(String property) {
        Method[] methods = type.getMethods();
        for (Method method : methods) {
            if (ReflectTypeUtil.isGetter(method)) {
                String name = ReflectTypeUtil.getPropertyName(method);
                if (name.equals(property)) {
                    return ReflectTypeUtil.getGetterPropertyType(method);
                }
            } else if (ReflectTypeUtil.isSetter(method)) {
                String name = ReflectTypeUtil.getPropertyName(method);
                if (name.equals(property)) {
                    return ReflectTypeUtil.getSetterPropertyType(method);
                }
            }
        }
        return null;
    }

    public boolean isReadOnly(String property) {
        Method[] methods = type.getMethods();
        boolean hasSetter = false;
        for (Method method : methods) {
            if (ReflectTypeUtil.isSetter(method)) {
                String name = ReflectTypeUtil.getPropertyName(method);
                if (name.equals(property)) {
                    hasSetter = true;
                }
            }
        }
        return !hasSetter;
    }

    public boolean isWriteOnly(String property) {
        Method[] methods = type.getMethods();
        boolean hasGetter = false;
        for (Method method : methods) {
            if (ReflectTypeUtil.isGetter(method)) {
                String name = ReflectTypeUtil.getPropertyName(method);
                if (name.equals(property)) {
                    hasGetter = true;
                }
            }
        }
        return !hasGetter;
    }

    public <METHOD_TYPE> METHOD_TYPE getProperty(T instance, String property) {
        Method[] methods = type.getMethods();
        Method getter = null;
        for (Method method : methods) {
            if (ReflectTypeUtil.isGetter(method)) {
                String name = ReflectTypeUtil.getPropertyName(method);
                if (name.equals(property)) {
                    getter = method;
                }
            }
        }
        if (getter != null) {
            try {
                Object result = getter.invoke(instance, new Object[0]);
                return (METHOD_TYPE) result;
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException(ex);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(ex);
            } catch (InvocationTargetException ex) {
                throw new IllegalArgumentException(ex);
            }
        } else {
            throw new IllegalArgumentException("Property " + property + " has no getter");
        }
    }

    public <METHOD_TYPE> void setProperty(T instance, String property, METHOD_TYPE value) {
        Method[] methods = type.getMethods();
        Method setter = null;
        for (Method method : methods) {
            if (ReflectTypeUtil.isSetter(method)) {
                String name = ReflectTypeUtil.getPropertyName(method);
                if (name.equals(property)) {
                    setter = method;
                }
            }
        }
        if (setter != null) {
            try {
                setter.invoke(instance, new Object[]{value});
                return;
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException(ex);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(ex);
            } catch (InvocationTargetException ex) {
                throw new IllegalArgumentException(ex);
            }
        } else {
            throw new IllegalArgumentException("Property " + property + " has no getter");
        }
    }

    public boolean isReadable(String property) {
        return !isWriteOnly(property);
    }

    public boolean isWriteable(String property) {
        return !isReadOnly(property);
    }

    public Class<T> getType() {
        return type;
    }
}
