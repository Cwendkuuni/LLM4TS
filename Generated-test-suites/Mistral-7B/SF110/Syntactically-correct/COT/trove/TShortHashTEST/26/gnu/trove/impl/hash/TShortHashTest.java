package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

import org.junit.Test;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(10);
 }

 @Test
 public void testConstructor() {
 TShortHash shortHash = new TShortHash();
 assertNotNull(shortHash);

 shortHash = new TShortHash(5);
 assertNotNull(shortHash);

 shortHash = new TShortHash(5, 0.75f);
 assertNotNull(shortHash);

 shortHash = new TShortHash(5, 0.75f, (short) 1);
 assertNotNull(shortHash);
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash shortHash = new TShortHash();
 short noEntryValue = shortHash.getNoEntryValue();
 assertEquals(Short.MIN_VALUE, noEntryValue);
 }

 @Test
 public void testSetUp() {
 TShortHash shortHash = new TShortHash(10);
 int capacity = shortHash.setUp(10);
 assertEquals(10, capacity);
 }

 @Test
 public void testContains() {
 TShortHash shortHash = new TShortHash();
 short val = (short) 1;
 assertFalse(shortHash.contains(val));

 shortHash.put(val);
 assertTrue(shortHash.contains(val));
 }

 @Test
 public void testForEach() {
 TShortHash shortHash = new TShortHash();
 short val1 = (short) 1;
 short val2 = (short) 2;
 short val3 = (short) 3;
 shortHash.put(val1);
 shortHash.put(val2);
 shortHash.put(val3);

 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short key) {
 if (key == val2) {
 return false;
 }
 return true;
 }
 };

 assertTrue(shortHash.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAtWithNegativeIndex() {
 hash.removeAt(-1);
 }

 @Test
 public void testRemoveAt() {
 short val = (short) 1;
 hash.put(val);
 int index = hash.indexOf(val);
 assertNotNull(index);
 hash.removeAt(index);
 assertFalse(hash.contains(val));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexWithNegativeIndex() {
 short val = (short) 1;
 hash.put(val);
 int index = hash.indexOf(val);
 assertNotNull(index);
 hash.removeAt(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyWithNegativeVal() {
 hash.put((short) Short.MIN_VALUE);
 }

 @Test
 public void testInsertKey() {
 short val = (short) 1;
 int index = hash.indexOf(val);
 assertFalse(hash.contains(val));
 hash.put(val);
 index = hash.indexOf(val);
 assertEquals(index, 0);
 assertTrue(hash.contains(val));
 }

 @Test
 public void testIndex() {
 short val = (short) 1;
 hash.put(val);
 int index = hash.indexOf(val);
 assertEquals(index, 0);
 }

 @Test
 public void testIndexRehashed() {
 short val = (short) 1;
 hash.put(val);
 int index = hash.indexOf(val);
 assertEquals(index, 0);

 short val2 = (short) 2;
 hash.put(val2);
 index = hash.indexOf(val);
 assertFalse(hash.contains(val));
 index = hash.indexOf(val); // hash collision
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKeyRehash() {
 short val = (short) 1;
 int index = hash.indexOf(val);
 assertFalse(hash.contains(val));
 hash.put(val);
 index = hash.indexOf(val);
 assertEquals(index, 0);
 assertTrue(hash.contains(val));

 short val2 = (short) 2;
 hash.put(val2);
 index = hash.indexOf(val); // hash collision
 assertFalse(hash.contains(val));
 index = hash.indexOf(val); // rehashing
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKeyAt() {
 short val = (short) 1;
 hash.insertKeyAt(0, val);
 assertTrue(hash.contains(val));
 }
}