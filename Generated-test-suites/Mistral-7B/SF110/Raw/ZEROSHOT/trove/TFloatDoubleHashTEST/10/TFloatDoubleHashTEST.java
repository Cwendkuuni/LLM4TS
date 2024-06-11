package com.example.junit; // Replace with your package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hashTable;

 @Before
 public void setup() {
 hashTable = new TFloatDoubleHash(3);
 }

 @Test
 public void testConstructors() {
 // Test default constructor
 TFloatDoubleHash defaultConstructor = new TFloatDoubleHash();
 // Test constructor with initialCapacity
 TFloatDoubleHash initialCapacityConstructor = new TFloatDoubleHash(5);
 // Test constructor with initialCapacity and loadFactor
 TFloatDoubleHash initialCapacityAndLoadFactorConstructor = new TFloatDoubleHash(7, 0.75f);
 // Test constructor with initialCapacity, loadFactor, no_entry_key, and no_entry_value
 TFloatDoubleHash initialCapacityLoadFactorNoEntryKeyAndValueConstructor = new TFloatDoubleHash(11, 0.85f, Float.NaN, Double.NaN);
 }

 @Test
 public void testGetNoEntryKeyAndValue() {
 TFloatDoubleHash hashTable = new TFloatDoubleHash(5);
 float expectedNoEntryKey = 0.0f;
 double expectedNoEntryValue = 0.0;
 float actualNoEntryKey = hashTable.getNoEntryKey();
 double actualNoEntryValue = hashTable.getNoEntryValue();
 assertEquals(expectedNoEntryKey, actualNoEntryKey);
 assertEquals(expectedNoEntryValue, actualNoEntryValue);
 }

 @Test
 public void testSetUp() {
 int initialCapacity = 10;
 TFloatDoubleHash hashTable = new TFloatDoubleHash() {
 @Override
 protected int setUp(int initialCapacity) {
 return super.setUp(initialCapacity) + 5;
 }
 };
 assertEquals(15, hashTable.size());
 }

 @Test
 public void testContains() {
 float key = 1.0f;
 TFloatDoubleHash hashTable = new TFloatDoubleHash(3);
 assertTrue(hashTable.contains(key));
 hashTable.insert(key, 2.0);
 assertFalse(hashTable.contains(key));
 }

 @Test
 public void testForEach() {
 TFloatDoubleHash hashTable = new TFloatDoubleHash(5);
 float[] expectedValues = {2.5f, 3.6f, 1.2f, 4.8f, 0.9f};
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 int index = hashTable.indexOf(value);
 assertEquals(expectedValues[hashTable.size() - index - 1], value);
 return true;
 }
 };
 hashTable.insert(1.2f, 1.2);
 hashTable.insert(2.5f, 2.5);
 hashTable.insert(3.6f, 3.6);
 hashTable.insert(4.8f, 4.8);
 hashTable.insert(0.9f, 0.9);
 hashTable.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 TFloatDoubleHash hashTable = new TFloatDoubleHash(5);
 hashTable.insert(1.0f, 1.0);
 hashTable.insert(2.0f, 2.0);
 hashTable.insert(3.0f, 3.0);
 hashTable.insert(4.0f, 4.0);
 hashTable.insert(5.0f, 5.0);
 int expectedSize = 4;
 hashTable.removeAt(3);
 assertEquals(expectedSize, hashTable.size());
 }

 @Test
 public void testIndex() {
 TFloatDoubleHash hashTable = new TFloatDoubleHash(5);
 hashTable.insert(1.0f, 1.0);
 hashTable.insert(2.0f, 2.0);
 hashTable.insert(3.0f, 3.0);
 hashTable.insert(4.0f, 4.0);
 hashTable.insert(5.0f, 5.0);
 int index1 = hashTable.index(1.0f);
 int index2 = hashTable.index(2.0f);
 int index3 = hashTable.index(3.0f);
 int index4 = hashTable.index(4.0f);
 int index5 = hashTable.index(5.0f);
 int index6 = hashTable.index(6.0f);
 assertEquals(index1, 1);
 assertEquals(index2, 2);
 assertEquals(index3, 3);
 assertEquals(index4, 4);
 assertEquals(index5, 5);
 assertEquals(index6, -1);
 }

 @Test
 public void testInsertKey() {
 TFloatDoubleHash hashTable = new TFloatDoubleHash(5);
 float key = 1.0f;
 int expectedIndex = 1;
 hashTable.insert(key, key);
 assertEquals(expectedIndex, hashTable.indexOf(key));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 TFloatDoubleHash hashTable = new TFloatDoubleHash(2);
 hashTable.insert(1.0f, 1.0);
 hashTable.insert(2.0f, 2.0);
 try {
 hashTable.insert(3.0f, 3.0);
 } catch (IllegalStateException e) {
 assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
 }
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 TFloatDoubleHash hashTable = new TFloatDoubleHash(5);
 hashTable.insert(1.0f, 1.0);
 hashTable.insert(2.0f, 2.0);
 hashTable.insert(3.0f, 3.0);
 hashTable.insert(4.0f, 4.0);
 hashTable.insert(5.0f, 5.0);
 objectOutputStream.writeObject(hashTable);
 byte[] bytes = byteArrayOutputStream.toByteArray();
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
 TFloatDoubleHash deserializedHashTable = (TFloatDoubleHash) objectInputStream.readObject();
 assertEquals(5, deserializedHashTable.size());
 for (int i = 0; i < 5; i++) {
 assertEquals(Float.intBitsToFloat(hashTable.hashCode(i)), Float.intBitsToFloat(deserializedHashTable.hashCode(i)));
 assertEquals(hashTable.get(i), deserializedHashTable.get(i));
 }
 }
}