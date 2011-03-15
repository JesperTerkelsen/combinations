/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.deck.testdatagenerator.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 *
 * @author jt
 */
public class CountDownLatchWaitable implements Waitable {

    private CountDownLatch countDownLatch;

    public CountDownLatchWaitable(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void await() throws InterruptedException {
        countDownLatch.await();
    }



}
