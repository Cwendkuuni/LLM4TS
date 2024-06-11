package com.example.junit; //Replace with your package name

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float> tFloatDoubleHash;

 @Before
 public void setUp() {
 tFloatDoubleHash = new TFloatDoubleHash<>(5);
 }

 @Test
 public void testEmptyTFloatDoubleHash() {
 TFloatDoubleHash<Float> tfdh = new TFloatDoubleHash<>();
 assertTrue(tfdh.isEmpty());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TFloatDoubleHash<Float> tfdh = new TFloatDoubleHash<>(5);
 assertEquals(5, tfdh.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TFloatDoubleHash<Float> tfdh = new TFloatDoubleHash<>(5, 0.75f);
 assertEquals(5, tfdh.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyAndNoEntryValue() {
 TFloatDoubleHash<Float> tfdh = new TFloatDoubleHash<>(5, 0.75f, 0.0f, 0.0);
 assertEquals(5, tfdh.size());
 assertEquals(0.0f, tfdh.getNoEntryKey(), 0.0);
 assertEquals(0.0, tfdh.getNoEntryValue(), 0.0);
 }

 @Test
 public void testSetUp() {
 tFloatDoubleHash.setUp(5);
 assertNotNull(tFloatDoubleHash._set);
 }

 @Test
 public void testContains() {
 tFloatDoubleHash.put(0.0f);
 assertTrue(tFloatDoubleHash.contains(0.0f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = (value) -> {
 assertTrue(value > 0.0);
 };
 tFloatDoubleHash.put(1.0f);
 tFloatDoubleHash.put(2.0f);
 tFloatDoubleHash.put(3.0f);
 tFloatDoubleHash.forEach(procedure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAtWithEmptyList() {
 tFloatDoubleHash.removeAt(-1);
 }

 @Test
 public void testRemoveAt() {
 tFloatDoubleHash.put(0.0f);
 tFloatDoubleHash.put(1.0f);
 int index = tFloatDoubleHash.indexOf(0.0f);
 tFloatDoubleHash.removeAt(index);
 assertFalse(tFloatDoubleHash.contains(0.0f));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexWithEmptyList() {
 tFloatDoubleHash.index(1.0f);
 }

 @Test
 public void testIndex() {
 tFloatDoubleHash.put(0.0f);
 tFloatDoubleHash.put(1.0f);
 int index = tFloatDoubleHash.indexOf(0.0f);
 assertEquals(index, 0);
 }

 @Test
 public void testIndexRehashed() {
 TFloatDoubleHash<Float> tfdh = new TFloatDoubleHash<>(5, 0.75f);
 tfdh.put(0.0f);
 tfdh.put(1.0f);
 int index = tfdh.index(1.0f);
 assertEquals(index, 1);
 }

 @Test
 public void testInsertKey() {
 tFloatDoubleHash.put(0.0f);
 int index = tFloatDoubleHash.insertKey(1.0f);
 assertEquals(index, 1);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullList() {
 TFloatDoubleHash<Float> tfdh = new TFloatDoubleHash<>(5, 0.75f);
 for (int i = 0; i < 5; i++) {
 tfdh.put((float) i);
 }
 tFloatDoubleHash.insertKey(5.0f);
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 TFloatDoubleHash<Float> tfdh = new TFloatDoubleHash<>(5, 0.75f, 0.0f, 0.0);
 tfdh.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 TFloatDoubleHash<Float> tfdhFromBytes = new TFloatDoubleHash<>();
 tfdhFromBytes.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(tfdh, tfdhFromBytes);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 TFloatDoubleHash<Float> tfdh = new TFloatDoubleHash<>(5, 0.75f, 0.0f, 0.0);
 tfdh.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 TFloatDoubleHash<Float> tfdhFromBytes = new TFloatDoubleHash<>();
 tfdhFromBytes.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(tfdh, tfdhFromBytes);
 }
}