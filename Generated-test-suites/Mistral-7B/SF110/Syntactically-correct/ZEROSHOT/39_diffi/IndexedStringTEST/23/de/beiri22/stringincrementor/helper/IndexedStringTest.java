package de.beiri22.stringincrementor.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

 @Test
 public void testIndexedStringConstruction() {
 char[] values = new char[]{'a', 'b', 'a', 'c', 'a', 'a'};
 IndexedString indexedString = new IndexedString(new String(values));
 assertEquals(values.length, indexedString.values.length);
 }

 @Test
 public void testIndexedStringEmptyConstruction() {
 IndexedString indexedString = new IndexedString("");
 assertEquals(0, indexedString.values.length);
 }

 @Test
 public void testIndexedStringCount() {
 IndexedString indexedString = new IndexedString("abcdefghijk");
 assertEquals(5, indexedString.count('a'));
 assertEquals(1, indexedString.count('e'));
 assertEquals(0, indexedString.count('z'));
 }

 @Test
 public void testIndexedStringIndex() {
 IndexedString indexedString = new IndexedString("abcdefghijk");
 int[] indexA = indexedString.index['a'];
 int[] indexC = indexedString.index['c'];
 int[] indexE = indexedString.index['e'];
 int[] indexF = indexedString.index['f'];

 assertEquals(5, indexA.length);
 assertEquals(3, indexC.length);
 assertEquals(1, indexE.length);
 assertEquals(1, indexF.length);

 assertEquals(0, indexA[0]);
 assertEquals(2, indexA[1]);
 assertEquals(4, indexA[2]);
 assertEquals(5, indexA[3]);
 assertEquals(6, indexA[4]);

 assertEquals(3, indexC[0]);
 }

 @Test
 public void testIndexOf() {
 IndexedString indexedString = new IndexedString("abcabacaba");
 char[] targetA = new char[]{'a', 'b', 'a'};
 char[] targetC = new char[]{'c'};

 assertEquals(-1, indexedString.indexOf(new char[]{'z'}));
 assertEquals(0, indexedString.indexOf(new char[]{'a'}));
 assertEquals(5, indexedString.indexOf(targetA));
 assertEquals(-1, indexedString.indexOf(targetC));
 }
}