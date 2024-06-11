package com.example.junit.test;

import static org.junit.Assert.*;

import java.util.BitSet;

import org.junit.Test;

import com.google.common.base.SmallCharMatcher;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class SmallCharMatcherTest {

private char[] createRandomCharArray(int size) {
char[] charArray = new char[size];
for (int i = 0; i < size; i++) {
charArray[i] = (char) (Math.random() * 256);
}
return charArray;
}

@Test
public void testMatchesWithValidCharacter() {
char[] table = createRandomCharArray(10);
SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(table), "Test CharMatcher");
assertTrue(charMatcher.matches('a'));
}

@Test(expected = IllegalArgumentException.class)
public void testMatchesWithNullCharacter() {
SmallCharMatcher charMatcher = new SmallCharMatcher(new char[10], 0L, false, "Test CharMatcher");
assertFalse(charMatcher.matches((char) 0));
assertFalse(charMatcher.matches(null));
}

@Test
public void testMatchesWithCharacterOutsideTable() {
char[] table = new char[5];
table[0] = 'a';
table[1] = 'b';
SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(table), "Test CharMatcher");
assertFalse(charMatcher.matches('c'));
}

@Test
public void testMatchesWithCharacterInTable() {
char[] table = new char[5];
table[0] = 'a';
table[1] = 'b';
SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(table), "Test CharMatcher");
assertTrue(charMatcher.matches('a'));
assertTrue(charMatcher.matches('b'));
}

@Test(expected = IllegalArgumentException.class)
public void testFromWithEmptyBitSet() {
BitSet emptyBitSet = new BitSet();
assertFalse(emptyBitSet.get(0));
assertThrows(IllegalArgumentException.class, () -> SmallCharMatcher.from(emptyBitSet, "Test CharMatcher"));
}

@Test(expected = IllegalArgumentException.class)
public void testFromWithNullDescription() {
BitSet bitSet = new BitSet();
bitSet.add(1);
assertThrows(IllegalArgumentException.class, () -> SmallCharMatcher.from(bitSet, null));
}

@Test(expected = IllegalStateException.class)
public void testMatchesWithTooLargeTableSize() {
int maxSize = SmallCharMatcher.MAX_SIZE + 1;
char[] table = new char[maxSize];
SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0L, false, "Test CharMatcher");
assertFalse(charMatcher.matches('a'));
assertThrows(IllegalStateException.class, () -> charMatcher.matches('a'));
}

@Test
public void testSetBits() {
BitSet table = HashBasedTable.create();
SmallCharMatcher charMatcher = new SmallCharMatcher(new char[10], 0L, false, "Test CharMatcher");
charMatcher.setBits(table);
assertTrue(table.get(0));
for (char c : charMatcher.table) {
if (c != '\0') {
table.set(c);
}
}
}

@Test
public void testChooseTableSize() {
assertThat(SmallCharMatcher.chooseTableSize(0), equalTo(2));
assertThat(SmallCharMatcher.chooseTableSize(1), equalTo(2));
assertThat(SmallCharMatcher.chooseTableSize(3), equalTo(4));
assertThat(SmallCharMatcher.chooseTableSize(10), equalTo(16));
assertThat(SmallCharMatcher.chooseTableSize(32), equalTo(64));
}
}