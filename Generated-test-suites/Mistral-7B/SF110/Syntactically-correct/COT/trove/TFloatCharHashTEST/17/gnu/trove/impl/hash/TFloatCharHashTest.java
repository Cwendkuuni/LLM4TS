package com.example.junit.test; // replace with your test package name

import static org.junit.Assert.*;
import static gnu.trove.HashFunctions.*;

import org.junit.Test;
import gnu.trove.TFloatProcedure;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.impl.hash.TPrimitiveHash;

public class TFloatCharHashTest {

 private TFloatCharHash<Float, Character> hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash<>(5, 0.75f);
 }

 @Test
 public void testConstructor() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>();
 TFloatCharHash<Float, Character> hash1 = new TFloatCharHash(5);
 TFloatCharHash<Float, Character> hash2 = new TFloatCharHash(5, 0.75f);
 TFloatCharHash<Float, Character> hash3 = new TFloatCharHash(5, 0.75f, 0.0f, '\0');
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 float noEntryKey = hash.getNoEntryKey();
 assertEquals(0.0f, noEntryKey, 0.001f);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 char noEntryValue = hash.getNoEntryValue();
 assertEquals('\0', noEntryValue);
 }

 @Test
 public void testSetUp() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 int capacity = hash.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 hash.insertKey(1.0f);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testContainsFalse() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 assertFalse(hash.contains(2.0f));
 }

 @Test
 public void testForEach() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return value > 1.0f;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachException() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 hash.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.removeAt(0);
 assertFalse(hash.contains(1.0f));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtIndexOutOfBounds() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 hash.removeAt(-1);
 }

 @Test
 public void testIndex() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 int index = hash.index(1.0f);
 assertEquals(0, index);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBounds() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 int index = hash.index(2.0f);
 fail("Expected IndexOutOfBoundsException");
 }

 @Test
 public void testIndexRehashed() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 int index = hash.indexRehashed(1.5f, 0, hash(1.5f) % hash.length(), (byte) 0);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 int index = hash.insertKey(1.0f);
 assertEquals(0, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyNullKey() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 hash.insertKey((Float) null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyNullValue() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 hash.insertKey(1.0f, '\u0000');
 }

 @Test
 public void testInsertKeyRehash() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 int index = hash.insertKey(1.0f);
 assertEquals(0, index);
 hash.insertKey(2.5f);
 int index1 = hash.insertKey(1.5f);
 assertEquals(-1, index1);
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 hash.insertKey(1.0f);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 hash.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 TFloatCharHash<Float, Character> hash1 = new TFloatCharHash<>(5, 0.75f);
 hash1.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertTrue(hash1.contains(1.0f));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(5, 0.75f);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutput(baos);
 out.writeByte(0);
 out.writeUTF("HelloWorld");
 out.writeFloat(1.0f);
 out.writeUTF("World");
 out.writeByte(1);
 hash.readExternal(new ObjectInput(new ByteArrayInputStream(baos.toByteArray())));
 assertEquals(1.0f, hash.getNoEntryKey(), 0.001f);
 assertEquals("World", hash.getNoEntryValue().toString());
 }

}