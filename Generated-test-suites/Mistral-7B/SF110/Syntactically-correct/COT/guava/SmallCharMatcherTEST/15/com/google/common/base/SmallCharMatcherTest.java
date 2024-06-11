package com.example.junit; // replace with your test package name

import static org.junit.Assert.*;
import com.google.common.base.SmallCharMatcher;
import com.google.common.collect.BitSet;

public class SmallCharMatcherTest {

 private char[] createTable(int setSize) {
 char[] table = new char[SmallCharMatcher.chooseTableSize(setSize)];
 return table;
 }

 @Test
 public void testMatches_SingleCharacter() {
 char[] table = createTable(5);
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Single Character Matcher");
 charMatcher.setBits(new BitSet(5));
 charMatcher.setBits(BitSet.create(48)); // set bit for the ASCII value of '0'
 assertTrue(charMatcher.matches('0'));
 }

 @Test
 public void testMatches_NonExistentCharacter() {
 char[] table = createTable(5);
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Single Character Matcher");
 charMatcher.setBits(new BitSet(5));
 charMatcher.setBits(BitSet.create(48)); // set bit for the ASCII value of '0'
 assertFalse(charMatcher.matches('1'));
 }

 @Test
 public void testMatches_ExistentCharacter() {
 char[] table = createTable(5);
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Single Character Matcher");
 charMatcher.setBits(new BitSet(5));
 charMatcher.setBits(BitSet.create(49, 50)); // set bits for the ASCII values of '1' and '2'
 assertTrue(charMatcher.matches('1'));
 assertTrue(charMatcher.matches('2'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFrom_NullDescription() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(null, "Null Description");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFrom_EmptyBitSet() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Empty Bit Set");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFrom_NegativeTableSize() {
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Negative Table Size", -1);
 }

 @Test
 public void testSetBits_NullBitSet() {
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[5], 0L, false, "Null BitSet");
 assertFalse(charMatcher.matches('0'));
 charMatcher.setBits(null);
 assertFalse(charMatcher.matches('0'));
 }

 @Test
 public void testSetBits_ExistentCharacter() {
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[5], 0L, false, "Existent Character");
 assertFalse(charMatcher.matches('0'));
 charMatcher.setBits(BitSet.create(48)); // set bit for the ASCII value of '0'
 assertTrue(charMatcher.matches('0'));
 }

 @Test
 public void testSetBits_MultipleExistentCharacters() {
 SmallCharMatcher charMatcher = new SmallCharMatcher(new char[5], 0L, false, "Multiple Existent Characters");
 assertFalse(charMatcher.matches('0'));
 charMatcher.setBits(BitSet.create(48, 49, 50)); // set bits for the ASCII values of '0', '1', and '2'
 assertTrue(charMatcher.matches('0'));
 assertTrue(charMatcher.matches('1'));
 assertTrue(charMatcher.matches('2'));
 }

 @Test
 public void testFrom_LargeSetSize() {
 char[] table = createTable(Integer.MAX_VALUE);
 SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Large Set Size");
 charMatcher.setBits(new BitSet(Integer.MAX_VALUE));
 }

 }