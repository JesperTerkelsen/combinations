/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator.model;

/**
 * This is a test bean that i want to generate data for.
 * 
 * @author g95490
 */
public class TestBean {
    private String stringValue;
    private boolean booleanValue;
    private int intValue;
    private Integer integerValue;

    public TestBean() {
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public void setIntegerValue(Integer integerValue) {
        this.integerValue = integerValue;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }
    

    @Override
    public String toString() {
        return "TestBean[" + 
                "booleanValue=" + booleanValue + 
                ",stringValue=" + stringValue + 
                ",intValue=" + intValue + 
                ",integerValue=" + integerValue + 
                ']';
    }
    
    
}
