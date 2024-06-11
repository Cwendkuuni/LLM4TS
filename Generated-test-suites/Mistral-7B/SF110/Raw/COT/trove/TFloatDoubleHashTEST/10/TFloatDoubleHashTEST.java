package com.example.junit.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float> hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hash = new TFloatDoubleHash<>(5, 0.75f);
 }

 // Test constructor without arguments
 @Test
 public void testDefaultConstructor() {
 TFloatDoubleHash<Float> hashDefault = new TFloatDoubleHash<>();
 assertEquals(0, hashDefault.size());
 }

 // Test constructor with initial capacity
 @Test
 public void testConstructorWithInitialCapacity() {
 TFloatDoubleHash<Float> hashInitialCapacity = new TFloatDoubleHash<>(3);
 assertEquals(0, hashInitialCapacity.size());
 }

 // Test constructor with initial capacity and load factor
 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TFloatDoubleHash<Float> hashInitialCapacityAndLoadFactor = new TFloatDoubleHash<>(5, 0.75f);
 assertEquals(0, hashInitialCapacityAndLoadFactor.size());
 }

 // Test constructor with initial capacity, load factor, no entry key and no entry value
 @Test
 public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyNoEntryValue() {
 TFloatDoubleHash<Float> hashInitialCapacityLoadFactorNoEntryKeyNoEntryValue = new TFloatDoubleHash<>(5, 0.75f, 1.0f, 2.0);
 assertEquals(0, hashInitialCapacityLoadFactorNoEntryKeyNoEntryValue.size());
 assertEquals(1.0f, hashInitialCapacityLoadFactorNoEntryKeyNoEntryValue.getNoEntryKey(), 0.0);
 assertEquals(2.0, hashInitialCapacityLoadFactorNoEntryKeyNoEntryValue.getNoEntryValue(), 0.0);
 }

 // Test getNoEntryKey method
 @Test
 public void testGetNoEntryKey() {
 assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
 }

 // Test getNoEntryValue method
 @Test
 public void testGetNoEntryValue() {
 assertEquals(0.0, hash.getNoEntryValue(), 0.0);
 }

 // Test setUp method
 @Test
 public void testSetUp() {
 int capacity = hash.setUp(5);
 assertEquals(5, capacity);
 }

 // Test contains method
 @Test
 public void testContains() {
 float key = 1.0f;
 hash.put(key, 1.0);
 assertTrue(hash.contains(key));
 }

 // Test forEach method
 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(final Float value) {
 assertEquals(value, 1.0f);
 return true;
 }
 };
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.forEach(procedure);
 }

 // Test removeAt method
 @Test
 public void testRemoveAt() {
 float key = 1.0f;
 hash.put(key, 1.0);
 int index = hash.indexOfKey(key);
 hash.removeAt(index);
 assertEquals(0, hash.size());
 }

 // Test index method with key present
 @Test
 public void testIndexKeyPresent() {
 float key = 1.0f;
 hash.put(key, 1.0);
 int index = hash.indexOfKey(key);
 assertEquals(index, hash.index(key));
 }

 // Test index method with key not present
 @Test
 public void testIndexKeyNotPresent() {
 float key = 2.0f;
 assertEquals(-1, hash.index(key));
 }

 // Test insertKey method with key already present
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyKeyAlreadyPresent() {
 float key = 1.0f;
 hash.put(key, 1.0);
 hash.insertKey(key);
 }

 // Test insertKeyRehash method with key already present
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashKeyAlreadyPresent() {
 float key = 1.0f;
 hash.put(key, 1.0);
 hash.insertKeyRehash(key, 0, 0, (byte) 1);
 }

 // Test XinsertKey method with key already present
 @Test(expected = IllegalStateException.class)
 public void testXinsertKeyKeyAlreadyPresent() {
 float key = 1.0f;
 hash.put(key, 1.0);
 hash.XinsertKey(key);
 }

 // Test XinsertKey method with key not present and no free slots
 @Test(expected = IllegalStateException.class)
 public void testXinsertKeyKeyNotPresentNoFreeSlots() {
 float key = 1.0f;
 for (int i = 0; i < hash.capacity(); i++) {
 hash.put(i, i);
 }
 hash.XinsertKey(key);
 }

 // Test XinsertKey method with key not present and free slot available
 @Test
 public void testXinsertKeyKeyNotPresentFreeSlotAvailable() {
 float key = 1.0f;
 hash.XinsertKey(key);
 assertTrue(hash.containsKey(key));
 }

 // Test writeExternal method
 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 hash.writeExternal(oos);
 byte[] bytes = baos.toByteArray();
 TFloatDoubleHash<Float> hashRead = (TFloatDoubleHash<Float>) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
 assertEquals(hash.size(), hashRead.size());
 }

 // Test readExternal method
 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 hash.writeExternal(oos);
 byte[] bytes = baos.toByteArray();
 TFloatDoubleHash<Float> hashRead = new TFloatDoubleHash<>(5, 0.75f);
 ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
 hashRead.readExternal(ois);
 assertEquals(hash.size(), hashRead.size());
 }
}