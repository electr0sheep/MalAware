package com.group6.malaware;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by michaeldegraw on 11/11/15.
 */
public class GameManagerTest {
    private static final double DELTA = 1e-15;
    GameManager testManager;

    @Before
    public void initialize(){
        testManager = new GameManager();
    }


    @Test
    public void testSubtractResourcesOkay(){
        testManager.addResources(8d);
        testManager.subtractResources(4d);
        assertEquals(4d, testManager.getTotalResources(), DELTA);
    }

    @Test(expected = RuntimeException.class)
    public void testSubtractResourcesNegative(){
        testManager.subtractResources(100d);
    }

    @Test
    public void testCalcResourcesPerSec(){
        testManager.addGenerator(GameManager.ADWARE, 1);
        testManager.addGenerator(GameManager.MALWARE, 1);
        testManager.addGenerator(GameManager.WORM, 1);
        testManager.addGenerator(GameManager.TROJAN, 1);
        testManager.addGenerator(GameManager.ROOTKIT, 1);
        testManager.addGenerator(GameManager.HIJACKER, 1);
        assertEquals(6d, testManager.getResourcesPerSec(), DELTA);
    }

    @Test
    public void testAddResources(){
        testManager.addResources(10d);
        assertEquals(10d, testManager.getTotalResources(), DELTA);
    }

    @Test
    public void testAddGeneratorSingle(){
        testManager.addGenerator(GameManager.ADWARE, 1);
        assertEquals(1, testManager.getNumOfGenerators(GameManager.ADWARE));
    }

    @Test
    public void testAddGeneratorMultiple(){
        testManager.addGenerator(GameManager.ADWARE, 10);
        assertEquals(10, testManager.getNumOfGenerators(GameManager.ADWARE));
    }

    @Test(expected = RuntimeException.class)
    public void testAddGeneratorNegative(){
        testManager.addGenerator(GameManager.ADWARE, -1);
    }

    @Test(expected = RuntimeException.class)
    public void testAddGeneratorsUnknown(){
        testManager.addGenerator(12435614, 1);
    }

    @Test
    public void testAttemptBuyValid(){
        testManager.addResources(10d);
        assertEquals(true, testManager.attemptBuy(GameManager.ADWARE, 1));
    }

    @Test
    public void testAttemptBuyNotEnoughResources(){
        assertEquals(false, testManager.attemptBuy(GameManager.ADWARE, 1));
    }

    @Test
    public void testConvertNumToStringDecimal(){
        assertEquals(".125", testManager.convertNumToString(0.125d));
    }

    @Test
    public void testConvertNumToStringOnes(){
        assertEquals("500", testManager.convertNumToString(500d));
    }

    @Test
    public void testConvertNumToStringThousand(){
        assertEquals("1 K", testManager.convertNumToString(1000d));
    }

    @Test
    public void testConvertNumToStringMillion(){
        assertEquals("10 M", testManager.convertNumToString(10000000d));
    }

    @Test
    public void testConvertNumToStringBillion(){
        assertEquals("100 B", testManager.convertNumToString(100000000000d));
    }

    @Test
    public void testConvertNumToStringTrillion(){
        assertEquals("1 T", testManager.convertNumToString(1000000000000d));
    }

    @Test
    public void testGetResourcesString(){
        testManager.addResources(50000000000000d);
        assertEquals("50 T", testManager.getResourcesString());
    }

    /*
    @Test
    public void testGetStoredTime(){
        // how can I do this?
    }

    @Test
    public void testLoadData(){
        // how can I do this?
    }

    @Test
    public void testStoreData(){
        // how can I do this?
    }
    */
}
