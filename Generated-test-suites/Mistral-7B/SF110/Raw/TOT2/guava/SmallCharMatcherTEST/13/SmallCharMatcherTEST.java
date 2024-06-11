package com.example.junit; // Replace with your package name

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import com.google.common.base.*;
import java.util.BitSet;

class SmallCharMatcherTest {

    @Test
    public void testMatches_whenCharacterMatches_returnsTrue() {
        // Test case for matches method from Expert 1
    }

    @Test
    public void testMatches_whenCharacterDoesNotMatch_returnsFalse() {
        // Test case for matches method from Expert 1
    }

    @Test
    public void testMatches_whenCharacterIsZero_returnsContainsZero() {
        // Test case for matches method from Expert 1
    }

    @Test
    public void testMatches_whenFilterDoesNotAllowCharacter_returnsFalse() {
        // Test case for matches method from Expert 1
    }

    @Test
    public void testSetBits_whenNullBitSet_throwsNullPointerException() {
        // Test case for setBits method from Expert 1
    }

    @Test
    public void testSetBits_whenEmptyBitSet_setsZero() {
        // Test case for setBits method from Expert 1
    }

    @Test
    public void testSetBits_whenBitSetContainingCharacter_setsCorrectly() {
        // Test case for setBits method from Expert 1
    }

    @Test
    public void testFrom_whenEmptyBitSet_returnsEmptyCharacterMatcher() {
        // Test case for from method from Expert 2
    }

    @Test
    public void testFrom_whenSingleCharacterBitSet_returnsCharacterMatcherWithSingleCharacter() {
        // Test case for from method from Expert 2
    }

    @Test
    public void testFrom_whenMultipleCharactersBitSet_returnsCharacterMatcherWithMultipleCharacters() {
        // Test case for from method from Expert 2
    }
}