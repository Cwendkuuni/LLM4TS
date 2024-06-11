
package com.example.junit; // replace with your package name

import org.junit.*;
import static org.junit.Assert.*;
import com.google.common.base.*;
import com.google.common.collect.*;

public class SmallCharMatcherTest {
   // Test cases for matches()
   @Test
   public void testMatches() {
       char[] table = new char[]{'a', 'b', 'c'};
       SmallCharMatcher matcher = new SmallCharMatcher(table, 0, false, "testMatcher");
       assertTrue(matcher.matches('a'));
       assertFalse(matcher.matches('d'));
   }

   @Test
   public void testMatches_NotInTable_False() {
       char[] table = new char[]{'a', 'b'};
       SmallCharMatcher matcher = new SmallCharMatcher(table, 0, false, "testMatcher");
       assertFalse(matcher.matches('d'));
   }

   @Test
   public void testMatches_InTable_True() {
       char[] table = new char[]{'a', 'b', 'a'};
       SmallCharMatcher matcher = new SmallCharMatcher(table, 0, false, "testMatcher");
       assertTrue(matcher.matches('a'));
   }

   // Test cases for setBits()
   @Test
   public void testSetBits_EmptyTable() {
       BitSet table = new BitSet();
       SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0, false, "testMatcher");
       matcher.setBits(table);
       assertTrue(table.get(0));
   }

   @Test
   public void testSetBits_NonEmptyTable() {
       char[] table = new char[]{'a', 'b'};
       SmallCharMatcher matcher = new SmallCharMatcher(table, 0, false, "testMatcher");
       BitSet expectedTable = new BitSet();
       expectedTable.set('a');
       expectedTable.set('b');
       matcher.setBits(expectedTable);
       assertTrue(expectedTable.equals(matcher.table));
   }

   // Test cases for from()
   @Test (expected = IllegalArgumentException.class)
   public void testFrom_EmptyBitSet() {
       BitSet chars = new BitSet();
       SmallCharMatcher matcher = SmallCharMatcher.from(chars, "testMatcher");
   }

   @Test
   public void testFrom_NonEmptyBitSet() {
       BitSet chars = new BitSet();
       chars.set('a');
       chars.set('b');
       SmallCharMatcher matcher = SmallCharMatcher.from(chars, "testMatcher");
       char[] expectedTable = new char[]{'a', 'b'};
       assertArrayEquals(expectedTable, matcher.table);
   }

   // Test case for chooseTableSize()
   @Test
   public void testChooseTableSize() {
       int setSize = 10;
       int expectedTableSize = 32;
       int calculatedTableSize = SmallCharMatcher.chooseTableSize(setSize);
       assertEquals(expectedTableSize, calculatedTableSize);
   }

   // Test case for smear()
   @Test
   public void testSmear() {
       int hashCode = 123;
       int expectedHashCode = 1964027481;
       int calculatedHashCode = SmallCharMatcher.smear(hashCode);
       assertEquals(expectedHashCode, calculatedHashCode);
   }
}