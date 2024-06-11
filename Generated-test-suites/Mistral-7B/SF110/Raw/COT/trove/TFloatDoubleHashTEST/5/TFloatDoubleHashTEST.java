package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float> hashMap;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash<>(5);
 }

 @Test
 public void testConstructor() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 TFloatDoubleHash<Float> hashMap1 = new TFloatDoubleHash<>(10);
 TFloatDoubleHash<Float> hashMap2 = new TFloatDoubleHash<>(10, 0.5f);
 TFloatDoubleHash<Float> hashMap3 = new TFloatDoubleHash<>(10, 0.5f, 0.0f, 0.0);
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 float noEntryKey = hashMap.getNoEntryKey();
 assertEquals(0.0f, noEntryKey);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 double noEntryValue = hashMap.getNoEntryValue();
 assertEquals(0.0, noEntryValue);
 }

 @Test
 public void testSetUp() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 int capacity = hashMap.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 assertTrue(hashMap.contains(1.0f));
 hashMap.removeAt(0);
 assertFalse(hashMap.contains(1.0f));
 }

 @Test
 public void testForEach() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float floatValue) {
 return (floatValue >= 1.5f && floatValue <= 1.8f);
 }
 };
 assertTrue(hashMap.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyNegativeCapacity() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 hashMap.insertKey(-1.0f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyNegativeValue() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(-1.0f);
 }

 @Test
 public void testInsertKeyEmptyHash() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 int index = hashMap.insertKey(1.0f);
 assertEquals(0, index);
 }

 @Test
 public void testInsertKeyHashCollision() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(1.0f);
 }

 @Test
 public void testInsertKeyRehash() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(1.1f);
 hashMap.insertKey(1.2f);
 hashMap.insertKey(1.3f);
 hashMap.insertKey(1.4f);
 hashMap.insertKey(1.5f);
 int index = hashMap.insertKey(1.6f);
 assertEquals(4, index);
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(1.1f);
 hashMap.insertKey(1.2f);
 hashMap.insertKey(1.3f);
 hashMap.insertKey(1.4f);
 hashMap.insertKey(1.5f);
 hashMap.insertKey(1.6f);
 hashMap.insertKey(1.7f);
 hashMap.insertKey(1.8f);
 hashMap.insertKey(1.9f);
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 hashMap.writeExternal(objectOutputStream);
 byte[] byteArray = byteArrayOutputStream.toByteArray();
 TFloatDoubleHash<Float> hashMapClone = null;
 ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArray));
 hashMapClone = (TFloatDoubleHash<Float>) objectInputStream.readObject();
 assertEquals(hashMap, hashMapClone);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(5);
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 hashMap.writeExternal(objectOutputStream);
 byte[] byteArray = byteArrayOutputStream.toByteArray();
 ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArray));
 hashMap = (TFloatDoubleHash<Float>) objectInputStream.readObject();
 assertEquals(hashMap, hashMap);
 }

}