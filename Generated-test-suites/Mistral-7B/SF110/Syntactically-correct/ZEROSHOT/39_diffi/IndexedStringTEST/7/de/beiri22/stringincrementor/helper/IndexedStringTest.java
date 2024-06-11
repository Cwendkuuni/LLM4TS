package de.beiri22.stringincrementor.helper;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class IndexedStringTest {

 private IndexedString indexedString;
 private char[] target1;
 private char[] target2;
 private char[] target3;

 @Before
 public void setUp() {
 char[] values = "teststring123".toCharArray();
 indexedString = new IndexedString(new String(values));
 target1 = new char[]{'t', 'e', 's', 't'};
 target2 = new char[]{'s', 't', 'r', 'i', 'n', 'g'};
 target3 = new char[]{'i', 'n', 'g'};
 }

 @Test
 public void testIndexedStringConstruction() {
 assertNotNull(indexedString);
 assertEquals(12, indexedString.values.length);
 }

 @Test
 public void testCharCount() {
 assertEquals(3, indexedString.count('t'));
 assertEquals(3, indexedString.count('s'));
 assertEquals(3, indexedString.count('i'));
 assertEquals(2, indexedString.count('r'));
 assertEquals(1, indexedString.count('g'));
 assertEquals(1, indexedString.count('1'));
 assertEquals(1, indexedString.count('3'));
 assertEquals(0, indexedString.count('z'));
 }

 @Test
 public void testIndexOfNullTarget() {
 assertNull(indexedString.indexOf(null));
 }

 @Test
 public void testIndexOfEmptyTarget() {
 assertFalse(indexedString.indexOf(new char[0]) >= 0);
 }

 @Test
 public void testIndexOfSingleCharacter() {
 assertEquals(0, indexedString.indexOf(new char[]{'t'}));
 }

 @Test
 public void testIndexOfSubstringWithMultipleMatches() {
 assertEquals(0, indexedString.indexOf(target1));
 assertEquals(4, indexedString.indexOf(target2));
 }

 @Test
 public void testIndexOfSubstringWithNoMatches() {
 assertEquals(-1, indexedString.indexOf(new char[]{'a', 'b'}));
 }

 @Test
 public void testIndexOfSubstringWithPartialMatches() {
 assertEquals(0, indexedString.indexOf(new char[]{'t', 's'}));
 assertEquals(6, indexedString.indexOf(new char[]{'i', 'n', 'g'}));
 }

 @Test
 public void testIndexOfSubstringWithMismatchedLength() {
 assertEquals(-1, indexedString.indexOf(new char[]{'s', 't', 'r', 'i', 'n', 'g', 'g'}));
 }
}