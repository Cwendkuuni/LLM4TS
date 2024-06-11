package com.example.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.SmallCharMatcher;

public class SmallCharMatcherTest {

    @Test
    public void testSmallCharMatcher_StaticMethods() {
        // Test chooseTableSize method
        int size = 16;
        int expectedTableSize = 32;
        int actualTableSize = SmallCharMatcher.chooseTableSize(size);
        assertEquals(expectedTableSize, actualTableSize);

        // Test smear method
        int hashCode = 123;
        int expectedSmearValue = 1686931315;
        int actualSmearValue = SmallCharMatcher.smear(hashCode);
        assertEquals(expectedSmearValue, actualSmearValue);
    }

    @Test
    public void testSmallCharMatcher_FromBitSet() {
        // Given
        BitSet givenChars = new BitSet();
        givenChars.set('a');
        givenChars.set('b');
        givenChars.set('c');
        givenChars.set(128); // random value

        CharMatcher expectedMatcher = CharMatcher.is('a', 'b', 'c');

        // When
        CharMatcher actualMatcher = SmallCharMatcher.from(givenChars, "Matches characters 'a', 'b', and 'c'.");

        // Then
        assertEquals(expectedMatcher.description(), actualMatcher.description());
        assertEquals(expectedMatcher, actualMatcher);

        for (char testChar : new HashSet<Character>(Collections.singletonList('a'))) {
            assertTrue(actualMatcher.matches(testChar));
        }

        for (char testChar : new HashSet<Character>(Collections.singletonList('b'))) {
            assertTrue(actualMatcher.matches(testChar));
        }

        for (char testChar : new HashSet<Character>(Collections.singletonList('c'))) {
            assertTrue(actualMatcher.matches(testChar));
        }

        for (char testChar : new HashSet<Character>(Collections.singletonList((char) 128))) {
            assertFalse(actualMatcher.matches(testChar));
        }

        for (char testChar : new HashSet<Character>()) {
            if (testChar != '\0') {
                assertFalse(actualMatcher.matches(testChar));
            }
        }

        assertNotNull(actualMatcher.description());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSmallCharMatcher_SetBits_ThrowsUOE() {
        // Given
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Matches all characters.");

        // When & Then
        charMatcher.setBits(null);
    }

    @Test
    public void testSmallCharMatcher_Matches() {
        // Given
        CharMatcher charMatcher = SmallCharMatcher.from(new BitSet('a', 'b', 'c'), "Matches characters 'a', 'b', and 'c'.");

        // When & Then
        for (char testChar : new HashSet<Character>(Collections.singletonList('a'))) {
            assertTrue(charMatcher.matches(testChar));
        }

        for (char testChar : new HashSet<Character>(Collections.singletonList('b'))) {
            assertTrue(charMatcher.matches(testChar));
        }

        for (char testChar : new HashSet<Character>(Collections.singletonList('c'))) {
            assertTrue(charMatcher.matches(testChar));
        }

        for (char testChar : new HashSet<Character>()) {
            if (testChar != '\0') {
                assertFalse(charMatcher.matches(testChar));
            }
        }
    }
}