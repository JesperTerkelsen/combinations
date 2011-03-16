/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.deck.testdatagenerator;

import java.util.Set;
import dk.deck.testdatagenerator.concurrent.Waitable;
import dk.deck.testdatagenerator.model.TestBean;
import java.util.LinkedHashSet;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jesper Terkelsen
 */
public class DataGeneratorTest {

    public DataGeneratorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        count = 0;
    }

    @After
    public void tearDown() {
    }
    private int count = 0;

    /**
     * Test of generateData method, of class DataGenerator.
     */
    @Test
    public void testGenerateData_0args() throws Exception {
        DataGenerator generator = new DataGenerator(TestBean.class);
        Set<String> values = new LinkedHashSet<String>();
        values.add(null);
        values.add("");
        values.add("emustring");
        values.add("gnustring");
        generator.setFieldValues("stringValue", values);
        generator.disableField("intValue");
        generator.disableField("booleanValue");
        List<TestBean> list = generator.generateData();
        for (TestBean testBean : list) {
            System.out.println(testBean.toString());
        }
        int calculatedCount = generator.getNumberOfPermutations();
        System.out.println("Permutations: " + list.size());
        System.out.println("CalculatedCount: " + calculatedCount);
        assertEquals(calculatedCount, list.size());
    }

    @Test
    public void testGenerateData() throws Exception {
        DataGenerator generator = new DataGenerator(TestBean.class);
        Set<String> values = new LinkedHashSet<String>();
        values.add(null);
        values.add("");
        values.add("emustring");
        values.add("gnustring");
        generator.setFieldValues("stringValue", values);
        generator.disableField("intValue");
        generator.generateData(new DataGenerationListener<TestBean>() {

            public void onDataGenerated(TestBean value) {
                System.out.println(value.toString());
                count = count + 1;
            }
        });
        int calculatedCount = generator.getNumberOfPermutations();
        System.out.println("Permutations: " + count);
        System.out.println("CalculatedCount: " + calculatedCount);

        assertEquals(calculatedCount, count);
    }

    @Test
    public void generateDataConcurent() throws Exception {
        DataGenerator generator = new DataGenerator(TestBean.class);
        Set<String> values = new LinkedHashSet<String>();
        values.add(null);
        values.add("");
        values.add("emustring");
        values.add("gnustring");
        generator.setFieldValues("stringValue", values);
        Waitable waitable = generator.generateDataConcurrent(new DataGenerationConcurrentListener<TestBean>() {

            public void onDataGenerated(TestBean value, int threadNum) {
                System.out.println(threadNum + ": " + value.toString());
                count = count + 1;
            }
            
        }, 5, 10);
        waitable.await();
        int calculatedCount = generator.getNumberOfPermutations();
        System.out.println("Permutations: " + count);
        System.out.println("CalculatedCount: " + calculatedCount);

        assertEquals(calculatedCount, count);
    }

}
