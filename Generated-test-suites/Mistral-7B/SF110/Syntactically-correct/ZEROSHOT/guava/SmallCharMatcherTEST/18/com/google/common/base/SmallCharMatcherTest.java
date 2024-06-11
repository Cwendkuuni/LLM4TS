package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

 @Test
 public void testSmallCharMatcher_Empty() {
 char[] table = new char[0];
 long filter = 0L;
 boolean containsZero = false;
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newBitSet(), "Empty CharMatcher");

 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, filter, containsZero, "Empty CharMatcher");
 BitSet expectedTable = BitSets.newBitSet();
 BitSet actualTable = BitSet.create();

 smallCharMatcher.setBits(actualTable);

 assertEquals("Unexpected table size for empty CharMatcher", 0, actualTable.cardinality());
 assertEquals("Unexpected contents of table for empty CharMatcher", expectedTable, actualTable);
 }

 @Test
 public void testSmallCharMatcher_SingleCharacter() {
 char[] table = new char[2];
 long filter = 0b0001L;
 boolean containsZero = false;
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create(0), "SingleCharacter CharMatcher");

 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, filter, containsZero, "SingleCharacter CharMatcher");
 BitSet expectedTable = BitSets.create(0);
 BitSet actualTable = BitSet.create();

 smallCharMatcher.setBits(actualTable);

 assertTrue("Unexpected table size for single character CharMatcher", actualTable.cardinality() == 1);
 assertEquals("Unexpected contents of table for single character CharMatcher", expectedTable, actualTable);
 }

 @Test
 public void testSmallCharMatcher_MultipleCharacters() {
 char[] table = new char[11];
 long filter = 0b1111111111110111L;
 boolean containsZero = false;
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create(65, 66, 67), "MultipleCharacters CharMatcher");

 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, filter, containsZero, "MultipleCharacters CharMatcher");
 BitSet expectedTable = BitSets.create(65, 66, 67);
 BitSet actualTable = BitSet.create();

 smallCharMatcher.setBits(actualTable);

 assertTrue("Unexpected table size for multiple characters CharMatcher", actualTable.cardinality() >= expectedTable.cardinality());
 for (int i = 0; i < table.length; i++) {
 if (table[i] != 0) {
 assertEquals("Unexpected character at index " + i + " in table for multiple characters CharMatcher", table[i], BitSets.get(expectedTable, i));
 }
 }
 }

 @Test
 public void testSmallCharMatcher_MatchesExpectedCharacter() {
 char[] table = new char[11];
 long filter = 0b1111111111110111L;
 boolean containsZero = false;
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create(65, 66, 67), "MultipleCharacters CharMatcher");

 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, filter, containsZero, "MultipleCharacters CharMatcher");

 assertTrue("Unexpected match result for character 'A' in multiple characters CharMatcher", smallCharMatcher.matches('A'));
 assertTrue("Unexpected match result for character 'B' in multiple characters CharMatcher", smallCharMatcher.matches('B'));
 assertTrue("Unexpected match result for character 'C' in multiple characters CharMatcher", smallCharMatcher.matches('C'));
 assertFalse("Unexpected match result for character 'D' in multiple characters CharMatcher", smallCharMatcher.matches('D'));
 }

 @Test
 public void testSmallCharMatcher_MatchesContainsZero() {
 char[] table = new char[2];
 long filter = 0b0001L;
 boolean containsZero = true;
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create(), "ContainsZero CharMatcher");

 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, filter, containsZero, "ContainsZero CharMatcher");

 assertTrue("Unexpected match result for character '\0' in contains zero CharMatcher", smallCharMatcher.matches('\0'));
 assertFalse("Unexpected match result for character 'A' in contains zero CharMatcher", smallCharMatcher.matches('A'));
 }

 @Test
 public void testSmallCharMatcher_Filter() {
 char[] table = new char[11];
 long filter = 0b1111111111110111L;
 boolean containsZero = false;
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.create(65, 66, 67), "MultipleCharacters CharMatcher");

 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, filter, containsZero, "MultipleCharacters CharMatcher");

 assertFalse("Unexpected match result for character '!' in multiple characters CharMatcher", smallCharMatcher.matches((char)0x21));
 assertTrue("Unexpected match result for character 'A' in multiple characters CharMatcher", smallCharMatcher.matches('A'));
 assertTrue("Unexpected match result for character 'B' in multiple characters CharMatcher", smallCharMatcher.matches('B'));
 assertTrue("Unexpected match result for character 'C' in multiple characters CharMatcher", smallCharMatcher.matches('C'));
 assertFalse("Unexpected match result for character 'D' in multiple characters CharMatcher", smallCharMatcher.matches('D'));
 }

 @Test
 public void testSmallCharMatcher_TableSize() {
 char[] table = new char[1024];
 long filter = 0b11111111111111111111011111011111L;
 boolean containsZero = false;
 CharMatcher charMatcher = SmallCharMatcher.from(BitSets.range(0, 1023), "LargeCharMatcher");

 SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, filter, containsZero, "LargeCharMatcher");
 int expectedTableSize = (int)Math.ceil(1024.0 * SmallCharMatcher.DESIRED_LOAD_FACTOR);
 int actualTableSize = smallCharMatcher.table.length;

 assertEquals("Unexpected table size for large CharMatcher", expectedTableSize, actualTableSize);
 }
}