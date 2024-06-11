package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.BitSet;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcherWithEmptyCharacterSet() {
 SmallCharMatcher emptyMatcher = SmallCharMatcher.from(new BitSet(), "Empty Character Set");
 BitSet expectedTable = new BitSet();
 BitSet actualTable = new BitSet();
 emptyMatcher.setBits(actualTable);
 assertEquals(expectedTable, actualTable);
 assertTrue(emptyMatcher.matches('\0'));
 }

 @Test
 public void testSmallCharMatcherWithSingleCharacterSet() {
 SmallCharMatcher singleMatcher = SmallCharMatcher.from(new BitSet().set(1), "Single Character Set");
 BitSet expectedTable = new BitSet().set(1);
 BitSet actualTable = new BitSet();
 singleMatcher.setBits(actualTable);
 assertEquals(expectedTable, actualTable);
 assertTrue(singleMatcher.matches((char)1));
 }

 @Test
 public void testSmallCharMatcherWithMultipleCharacterSet() {
 SmallCharMatcher multipleMatcher = SmallCharMatcher.from(new BitSet().set(1, 3, 5), "Multiple Character Set");
 int expectedTableSize = chooseTableSize(3);
 char[] expectedTable = new char[expectedTableSize];
 int index = 0;
 for (char c : new char[]{'1', '3', '5'}) {
 expectedTable[index++] = c;
 }
 expectedTable[index++] = '\0';
 BitSet actualTable = new BitSet();
 multipleMatcher.setBits(actualTable);
 assertEquals(expectedTableSize, actualTable.size());
 for (int i = 0; i < expectedTableSize; i++) {
 assertEquals(expectedTable[i], actualTable.get(i));
 }
 assertTrue(multipleMatcher.matches((char)1));
 assertTrue(multipleMatcher.matches((char)3));
 assertTrue(multipleMatcher.matches((char)5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSmallCharMatcherWithNegativeCharacterIndex() {
 SmallCharMatcher.from(new BitSet().set(-1), "Negative Character Index");
 }

 @Test
 public void testSmallCharMatcherWithLargeCharacterSet() {
 SmallCharMatcher largeMatcher = SmallCharMatcher.from(new BitSet().set(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), "Large Character Set");
 int expectedTableSize = chooseTableSize(10);
 char[] expectedTable = new char[expectedTableSize];
 int index = 0;
 for (int i = 0; i < 10; i++) {
 expectedTable[index++] = (char)i;
 }
 expectedTable[index++] = '\0';
 BitSet actualTable = new BitSet();
 largeMatcher.setBits(actualTable);
 assertEquals(expectedTableSize, actualTable.size());
 for (int i = 0; i < expectedTableSize; i++) {
 assertEquals(expectedTable[i], actualTable.get(i));
 }
 assertTrue(largeMatcher.matches((char)0));
 assertTrue(largeMatcher.matches((char)1));
 // ... test other characters up to 9
 }

 @Test
 public void testSmallCharMatcherWithFilter() {
 SmallCharMatcher filterMatcher = SmallCharMatcher.from(new BitSet(), "Filter Character Set");
 SmallCharMatcher expectedMatcher = SmallCharMatcher.from(new BitSet().set(1, 4, 7), "Filtered Character Set");
 BitSet actualTable = new BitSet();
 filterMatcher.setBits(actualTable);
 assertEquals(expectedMatcher.table.length, actualTable.size());
 for (int i = 0; i < expectedMatcher.table.length; i++) {
 if (expectedMatcher.table[i] != '\0') {
 assertEquals(expectedMatcher.table[i], actualTable.get(i));
 }
 }
 assertTrue(filterMatcher.matches((char)1));
 assertTrue(filterMatcher.matches((char)4));
 assertTrue(filterMatcher.matches((char)7));
 assertFalse(filterMatcher.matches((char)2));
 }
}