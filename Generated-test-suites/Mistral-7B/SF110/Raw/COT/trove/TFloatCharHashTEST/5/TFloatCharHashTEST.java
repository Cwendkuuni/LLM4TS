package com.example.junit4.test; // replace with your test package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 @Test
 public void testConstructorWithDefaultArguments() {
 TFloatCharHash hash = new TFloatCharHash();
 assertEquals(0, hash.getNoEntryKey());
 assertEquals('\u0000', hash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TFloatCharHash hash = new TFloatCharHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TFloatCharHash hash = new TFloatCharHash(5, 0.75f);
 assertEquals(5, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyNoEntryValue() {
 TFloatCharHash hash = new TFloatCharHash(5, 0.75f, 1.0f, 'X');
 assertEquals(1.0f, hash.getNoEntryKey());
 assertEquals('X', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TFloatCharHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 hash.put(1.0f);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testContainsNonExistingKey() {
 assertFalse(hash.contains(2.0f));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TFloatCharHash hash = new TFloatCharHash(-1);
 fail("IllegalArgumentException expected");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 TFloatCharHash hash = new TFloatCharHash(5, -0.5f);
 fail("IllegalArgumentException expected");
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, value, 0.0f);
 return true;
 }
 };
 hash.put(1.0f);
 hash.put(2.0f);
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 hash.put(1.0f);
 hash.put(2.0f);
 hash.removeAt(0);
 assertEquals(1, hash.size());
 assertFalse(hash.contains(1.0f));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtOutOfBounds() {
 hash.removeAt(-1);
 fail("IndexOutOfBoundsException expected");
 }

 @Test
 public void testIndex() {
 hash.put(1.0f);
 hash.put(2.0f);
 assertEquals(0, hash.index(1.0f));
 assertEquals(-1, hash.index(3.0f));
 }

 @Test
 public void testIndexRehashed() {
 hash.put(1.0f);
 hash.put(2.0f);
 hash.put(3.0f);
 assertEquals(2, hash.indexRehashed(3.0f, 2, 1, (byte) 1));
 }

 @Test
 public void testInsertKey() {
 hash.put(1.0f);
 hash.put(2.0f);
 hash.insertKey(3.0f);
 assertEquals(3, hash.size());
 assertEquals(3.0f, hash.get(3.0f), 0.0f);
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 hash.put(1.0f);
 hash.put(2.0f);
 hash.put(3.0f);
 hash.put(4.0f);
 hash.put(5.0f);
 hash.insertKey(6.0f);
 fail("IllegalStateException expected");
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f);
 hash.writeExternal(out);
 byte[] byteArray = byteStream.toByteArray();
 TFloatCharHash hashFromByteArray = new TFloatCharHash(new ByteArrayInputStream(byteArray));
 assertEquals(1.0f, hashFromByteArray.get(0.0f), 0.0f);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutput(byteStream);
 TFloatCharHash hashToWrite = new TFloatCharHash(5);
 hashToWrite.put(1.0f);
 hashToWrite.writeExternal(out);
 ByteArrayInputStream byteArrayInput = new ByteArrayInputStream(byteStream.toByteArray());
 TFloatCharHash hashFromByteArray = new TFloatCharHash(new ByteArrayInputStream(byteArrayInput));
 assertEquals(1.0f, hashFromByteArray.get(0.0f), 0.0f);
 }

}