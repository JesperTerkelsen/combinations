/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator.model;

/**
 *
 * @author Jesper Terkelsen
 */
public class CreditCheckTestInput {
    private String accountName;
    private boolean premiumCustomer;
    private int newAmount;
    private int currentBalance;

    public CreditCheckTestInput() {
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(int currentBalance) {
        this.currentBalance = currentBalance;
    }

    public int getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(int newAmount) {
        this.newAmount = newAmount;
    }

    public boolean isPremiumCustomer() {
        return premiumCustomer;
    }

    public void setPremiumCustomer(boolean premiumCustomer) {
        this.premiumCustomer = premiumCustomer;
    }

    @Override
    public String toString() {
        return "CreditCheck[" + 
                "a=" + accountName + 
                ",cb=" + currentBalance + 
                ",na=" + newAmount + 
                ",p=" + premiumCustomer +                 
                ']';
    }
    
}
