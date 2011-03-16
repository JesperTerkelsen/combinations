/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.deck.testdatagenerator;

import dk.deck.testdatagenerator.model.CreditCheckTestInput;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Jesper Terkelsen
 */
public class CreditCheckTest {

    @Test
    public void testCreditCheck() throws InstantiationException, IllegalAccessException {
        DataGenerator generator = new DataGenerator(CreditCheckTestInput.class);
        System.out.println("Permuations: " + generator.getNumberOfPermutations());
        generator.generateData(new DataGenerationListener<CreditCheckTestInput>() {

            public void onDataGenerated(CreditCheckTestInput value) {
                System.out.println(value.toString());
                
                String account = value.getAccountName();
                int currentBalance = value.getCurrentBalance();
                int newAmount = value.getNewAmount();
                boolean premium = value.isPremiumCustomer();
                // yourApi.performCreditCheck(account,currentBalance,newAmount,premium);
                // assertThatEverytingAreOk()
            }
        });
        
    }

    @Test
    public void testCreditCheckWidhParameters() throws InstantiationException, IllegalAccessException {
        DataGenerator generator = new DataGenerator(CreditCheckTestInput.class);
        List<String> accountNames = new ArrayList<String>();
        accountNames.add(null);
        accountNames.add("Big Customer");
        accountNames.add("Litte Customer");
        generator.setFieldValues("accountName", accountNames);
        List<Integer> currentBalance = new ArrayList<Integer>();
        currentBalance.add(0);
        currentBalance.add(1000);
        currentBalance.add(100000);
        generator.setFieldValues("currentBalance", currentBalance);
        List<Integer> newAmount = new ArrayList<Integer>();
        newAmount.add(1000);
        generator.setFieldValues("newAmount", newAmount);     
        System.out.println("Permuations: " + generator.getNumberOfPermutations());        
        generator.generateData(new DataGenerationListener<CreditCheckTestInput>() {

            public void onDataGenerated(CreditCheckTestInput value) {
                System.out.println(value.toString());
                
                String account = value.getAccountName();
                int currentBalance = value.getCurrentBalance();
                int newAmount = value.getNewAmount();
                boolean premium = value.isPremiumCustomer();
                // yourApi.performCreditCheck(account,currentBalance,newAmount,premium);
                // assertThatEverytingAreOk()          
            }
        });
    }    
    
}
