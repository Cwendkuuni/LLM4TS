package com.example.junit4; // Replace with the actual package name

import static org.junit.Assert.*;

import gnu.trove.impl.hash.*;
import gnu.trove.procedure.*;

import org.junit.Test;

public class TFloatCharHashTest {

 private TFloatCharHash<Float, Character> hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash<>(3);
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
 assertEquals(0.0f, hash.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals('\0', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 assertEquals(3, hash.setUp(3));
 }

 @Test
 public void testContains() {
 assertTrue(hash.contains(Float.NaN));
 assertFalse(hash.contains(Float.NEGATIVE_INFINITY));
 assertFalse(hash.contains(Float.POSITIVE_INFINITY));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(Float value) {
 return value >= 0.0f;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachWithEmptyHash() {
 TFloatCharHash<Float, Character> emptyHash = new TFloatCharHash<>();
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(Float value) {
 return true;
 }
 };
 emptyHash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 hash.put(Float.NaN, '\u0000');
 hash.removeAt(0);
 assertTrue(hash.isEmpty());
 }

 @Test
 public void testIndex() {
 hash.put(0.0f, 'A');
 hash.put(1.0f, 'B');
 hash.put(Float.NaN, 'C');
 hash.put(2.0f, 'D');

 assertEquals(0, hash.index(0.0f));
 assertEquals(-1, hash.index(1.1f));
 assertEquals(3, hash.index(Float.NaN));
 }

 @Test
 public void testIndexRehashed() {
 hash.put(0.0f, 'A');
 hash.put(1.0f, 'B');
 hash.put(Float.NaN, 'C');
 hash.put(2.0f, 'D');
 hash.put(3.0f, 'E');

 assertEquals(0, hash.indexRehashed(0.0f, 0, 0, (byte)0));
 assertEquals(-1, hash.indexRehashed(1.1f, 0, 0, (byte)0));
 assertEquals(3, hash.indexRehashed(Float.NaN, 0, 0, (byte)0));
 }

 @Test
 public void testInsertKey() {
 hash.put(Float.NaN, '\u0000');
 hash.put(0.1f, 'A');
 hash.put(0.2f, 'B');
 hash.put(0.3f, 'C');
 hash.put(0.4f, 'D');
 hash.put(0.5f, 'E');

 assertEquals(4, hash.size());
 assertEquals(-5, hash.insertKey(0.0f));
 assertEquals(5, hash.size());
 }

 @Test
 public void testInsertKeyRehash() {
 hash.put(Float.NaN, '\u0000');
 hash.put(0.1f, 'A');
 hash.put(0.2f, 'B');
 hash.put(0.3f, 'C');
 hash.put(0.4f, 'D');
 hash.put(0.5f, 'E');
 hash.put(0.6f, 'F');
 hash.put(0.7f, 'G');

 assertEquals(7, hash.size());
 assertEquals(-1, hash.insertKey(0.0f));
 assertEquals(8, hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHash() {
 hash.put(Float.NaN, '\u0000');
 hash.put(0.1f, 'A');
 hash.put(0.2f, 'B');
 hash.put(0.3f, 'C');
 hash.put(0.4f, 'D');
 hash.put(0.5f, 'E');
 hash.put(0.6f, 'F');
 hash.put(0.7f, 'G');
 hash.put(0.8f, 'H');
 }

 @Test
 public void testWriteExternal() {
 try {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
 hash.writeExternal(objectStream);
 byte[] bytes = byteStream.toByteArray();
 TFloatCharHash<Float, Character> newHash = new TFloatCharHash<>();
 newHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(hash, newHash);
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testReadExternal() {
 try {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
 hash.writeExternal(objectStream);
 byte[] bytes = byteStream.toByteArray();
 TFloatCharHash<Float, Character> newHash = new TFloatCharHash<>();
 newHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(hash, newHash);
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

}