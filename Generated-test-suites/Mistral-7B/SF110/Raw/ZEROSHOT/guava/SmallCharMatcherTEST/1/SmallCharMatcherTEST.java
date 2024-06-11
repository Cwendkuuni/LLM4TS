package com.google.common.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.BitSet;

@RunWith(JUnit4.class)
public class SmallCharMatcherTest {

 private char[] createTable(int setSize) {
 char[] table = new char[SmallCharMatcher.chooseTableSize(setSize)];
 BitSet chars = new BitSet(setSize + 1);
 chars.set(0, true);
 CharMatcher charMatcher = SmallCharMatcher.from(chars, "Test CharMatcher");
 return charMatcher.table;
 }

 @Test
 public void testMatchesCharacterInTable() {
 char[] table = createTable(10);
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "Test CharMatcher");
 char testCharacter = 'a';
 assertTrue(charMatcher.matches(testCharacter));
 }

 @Test
 public void testMatchesCharacterNotInTable() {
 char[] table = createTable(10);
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "Test CharMatcher");
 char testCharacter = 'b';
 assertFalse(charMatcher.matches(testCharacter));
 }

 @Test
 public void testMatchesCharacterWithZeroInTable() {
 char[] table = createTable(1);
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, true, "Test CharMatcher");
 char testCharacter = '\0';
 assertTrue(charMatcher.matches(testCharacter));
 }

 @Test
 public void testMatchesCharacterWithZeroNotInTable() {
 char[] table = createTable(10);
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "Test CharMatcher");
 char testCharacter = '\0';
 assertFalse(charMatcher.matches(testCharacter));
 }

 @Test
 public void testMatchesCharacterWithFilter() {
 char[] table = createTable(5);
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0b11111L, false, "Test CharMatcher");
 char testCharacter1 = 'a';
 char testCharacter2 = 'e';
 assertTrue(charMatcher.matches(testCharacter1));
 assertFalse(charMatcher.matches(testCharacter2));
 }

 @Test
 public void testMatchesEmptyTable() {
 char[] table = new char[0];
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "Test CharMatcher");
 char testCharacter = 'a';
 assertFalse(charMatcher.matches(testCharacter));
 }

 @Test
 public void testMatchesTableWithRepeatedCharacter() {
 char[] table = createTable(3);
 SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "Test CharMatcher");
 char testCharacter = 'a';
 int index = 0;
 while (index < table.length) {
 if (table[index] == testCharacter) {
 assertTrue(charMatcher.matches(testCharacter));
 break;
 }
 index++;
 }
 }

 @Test
 public void testMatchesTableWithDuplicateFilter() {
 char[] table1 = createTable(5);
 SmallCharMatcher charMatcher1 = new SmallCharMatcher(table1, 0b11111L, false, "Test CharMatcher 1");
 char[] table2 = createTable(5);
 SmallCharMatcher charMatcher2 = new SmallCharMatcher(table2, 0b11111L, false, "Test CharMatcher 2");
 char testCharacter = 'a';
 assertTrue(charMatcher1.matches(testCharacter));
 assertTrue(charMatcher2.matches(testCharacter));
 assertTrue(charMatcher1 == charMatcher2);
 }

}