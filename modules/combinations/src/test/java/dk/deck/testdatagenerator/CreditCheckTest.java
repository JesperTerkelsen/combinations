/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.deck.testdatagenerator;

import dk.deck.testdatagenerator.generator.DataGeneratorImpl;
import dk.deck.testdatagenerator.model.CreditCheckTestInput;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author Jesper Terkelsen
 */
public class CreditCheckTest {

    @Test
    public void testCreditCheck() throws InstantiationException, IllegalAccessException {
        DataGenerator generator = DataGeneratorFactory.getDataGenerator(CreditCheckTestInput.class);
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
        DataGenerator generator = DataGeneratorFactory.getDataGenerator(CreditCheckTestInput.class);
        Set<String> accountNames = new LinkedHashSet<String>();
        accountNames.add(null);
        accountNames.add("Big Customer");
        accountNames.add("Litte Customer");
        generator.setFieldValues("accountName", accountNames);
        Set<Integer> currentBalance = new LinkedHashSet<Integer>();
        currentBalance.add(0);
        currentBalance.add(1000);
        currentBalance.add(100000);
        generator.setFieldValues("currentBalance", currentBalance);
        Set<Integer> newAmount = new LinkedHashSet<Integer>();
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
