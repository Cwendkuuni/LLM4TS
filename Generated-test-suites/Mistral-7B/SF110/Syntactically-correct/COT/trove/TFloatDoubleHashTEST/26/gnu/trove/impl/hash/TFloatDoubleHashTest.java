package com.yourpackage.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float> hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash<>(11, 0.75f);
 }

 @Test
 public void testConstructor() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 assertEquals(0, hashMap.size());

 hashMap = new TFloatDoubleHash<>(10);
 assertEquals(0, hashMap.size());

 hashMap = new TFloatDoubleHash<>(10, 0.75f);
 assertEquals(0, hashMap.size());

 hashMap = new TFloatDoubleHash<>(10, 0.75f, 0.0f, 0.0);
 assertEquals(0, hashMap.size());
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 assertEquals(0.0f, hashMap.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 assertEquals(0.0, hashMap.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(11);
 assertEquals(11, hashMap.capacity());

 hashMap.setUp(11);
 assertArrayEquals(new float[11], hashMap._set);
 }

 @Test
 public void testContains() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 assertFalse(hashMap.contains(1.0f));

 hashMap.put(1.0f, 1.0);
 assertTrue(hashMap.contains(1.0f));
 }

 @Test
 public void testForEach() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(Float key) {
 return key > 1.5f;
 }
 };

 assertTrue(hashMap.forEach(procedure));

 assertEquals(2, hashMap.size());
 }

 @Test
 public void testRemoveAt() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);

 hashMap.removeAt(1);
 assertEquals(2, hashMap.size());
 assertFalse(hashMap.containsKey(2.0f));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBoundsExceptionWhenRemovingAtNegativeIndex() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 hashMap.put(1.0f, 1.0);
 hashMap.removeAt(-1);
 }

 @Test
 public void testIndex() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);

 assertEquals(2, hashMap.index(2.0f));
 assertEquals(-1, hashMap.index(4.0f));
 }

 @Test
 public void testIndexRehashed() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(3);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.put(4.0f, 4.0);

 assertEquals(3, hashMap.index(3.0f));
 assertEquals(-1, hashMap.index(5.0f));
 }

 @Test
 public void testInsertKey() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 assertEquals(-1, hashMap.insertKey(1.0f));
 hashMap.put(1.0f, 1.0);
 assertEquals(0, hashMap.insertKey(1.0f));
 }

 @Test
 public void testInsertKeyAt() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 hashMap.insertKeyAt(0, 1.0f);
 assertEquals(1, hashMap.size());
 assertEquals(1.0f, hashMap.get(0));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>(3);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.put(4.0f, 4.0);
 hashMap.put(5.0f, 5.0);
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 hashMap.writeExternal(out);

 TFloatDoubleHash<Float> hashMapFromExternal = new TFloatDoubleHash<>();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 hashMapFromExternal.readExternal(in);

 assertEquals(3, hashMapFromExternal.size());
 assertArrayEquals(new float[] {1.0f, 2.0f, 3.0f}, hashMapFromExternal._set);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 TFloatDoubleHash<Float> hashMap = new TFloatDoubleHash<>();
 hashMap.writeExternal(out);

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 hashMap.readExternal(in);

 assertEquals(0, hashMap.size());
 assertEquals(0.0f, hashMap.getNoEntryKey());
 assertEquals(0.0, hashMap.getNoEntryValue());
 }
}