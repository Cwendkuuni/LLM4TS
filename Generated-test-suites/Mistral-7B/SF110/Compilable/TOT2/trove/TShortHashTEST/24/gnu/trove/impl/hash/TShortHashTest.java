package com.example.junit;

import static org.junit.Assert.*;
import org.junit.Test;
import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

public class TShortHashTest {

    @Test
    public void testTShortHashEmptyConstructor() {
        // Expert 1: Method 1
    }

    @Test
    public void testTShortHashInitialCapacityConstructor() {
        // Expert 1: Method 2
    }

    @Test
    public void testTShortHashInitialCapacityAndLoadFactorConstructor() {
        // Expert 1: Method 3
    }

    @Test
    public void testTShortHashInitialCapacityLoadFactorAndNoEntryValueConstructor() {
        // Expert 1: Method 4
    }

    @Test
    public void testGetNoEntryValue() {
        // Expert 1: Method 5
    }

    @Test
    public void testContains() {
        // Expert 1: Method 6
    }

    @Test
    public void testForEach() {
        // Expert 1: Method 7
    }

    @Test
    public void testRemoveAt() {
        // Expert 1: Method 8
    }

    @Test
    public void testIndex() {
        // Expert 1: Method 9
    }

    @Test
    public void testInsertKey() {
        // Expert 2: Method 10
    }

    @Test
    public void testIndexRehashed() {
        // Expert 2: Method 11
    }

    @Test
    public void testInsertKeyAt() {
        // Expert 2: Method 12
    }

    @Test
    public void testInsertKeyRehash() {
        // Expert 2: Method 13
    }

    @Test
    public void testEdgeCasePrimeCapacity() {
        // Expert 3: Method 14
    }

    @Test
    public void testEdgeCaseLowLoadFactor() {
        // Expert 3: Method 15
    }

    @Test(expected = IllegalStateException.class)
    public void testErrorScenarioDuplicateKeys() {
        // Expert 3: Method 16
    }

    @Test(expected = IllegalArgumentException.class)
    public void testErrorScenarioNegativeCapacity() {
        // Expert 3: Method 17
    }

    @Test(expected = IllegalArgumentException.class)
    public void testErrorScenarioNegativeLoadFactor() {
        // Expert 3: Method 18
    }
}