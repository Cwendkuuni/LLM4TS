package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hashMap;
 private TFloatProcedure procedure;

 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash(5);
 procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return true;
 }
 };
 }

 @Test
 public void testConstructors() {
 TFloatDoubleHash hashMap1 = new TFloatDoubleHash();
 TFloatDoubleHash hashMap2 = new TFloatDoubleHash(10);
 TFloatDoubleHash hashMap3 = new TFloatDoubleHash(10, 0.5f);
 TFloatDoubleHash hashMap4 = new TFloatDoubleHash(10, 0.5f, 0.0f, 0.0d);
 assertEquals(0, hashMap1.size());
 assertEquals(10, hashMap2.size());
 assertEquals(0.5f, hashMap3.getLoadFactor(), 0.01f);
 assertEquals(0.0f, hashMap4.getNoEntryKey());
 assertEquals(0.0d, hashMap4.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash();
 assertEquals(0.0f, hashMap.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash();
 assertEquals(0.0d, hashMap.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 assertEquals(5, hashMap.size());
 }

 @Test
 public void testContains() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.insertKey(1.0f);
 assertTrue(hashMap.contains(1.0f));
 hashMap.remove(1.0f);
 assertFalse(hashMap.contains(1.0f));
 }

 @Test
 public void testForEach() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.forEach(procedure);
 assertEquals(3, hashMap.size());
 }

 @Test
 public void testRemoveAt() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.removeAt(1);
 assertEquals(2, hashMap.size());
 assertEquals(2.0f, hashMap.get(0), 0.01f);
 assertEquals(3.0f, hashMap.get(1), 0.01f);
 }

 @Test
 public void testIndex() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 assertEquals(1, hashMap.index(1.0f));
 assertEquals(-1, hashMap.index(4.0f));
 }

 @Test
 public void testIndexRehashed() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.insertKey(3.0f);
 hashMap.setUp(10);
 assertEquals(1, hashMap.index(1.0f));
 }

 @Test
 public void testInsertKey() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 int index = hashMap.insertKey(1.0f);
 assertEquals(1, hashMap.size());
 assertEquals(1.0f, hashMap.get(index), 0.01f);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(1, 1.0f);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 }

 @Test
 public void testInsertKeyRehash() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.setUp(10);
 int index = hashMap.insertKey(1.0f);
 assertEquals(1, hashMap.size());
 assertEquals(1.0f, hashMap.get(index), 0.01f);
 }

 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutput output = new ObjectOutputStream(outputStream);
 TFloatDoubleHash hashMap = new TFloatDoubleHash();
 hashMap.writeExternal(output);
 output.close();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ObjectInput input = new ObjectInput(inputStream);
 TFloatDoubleHash hashMapRead = (TFloatDoubleHash) input.readObject();
 input.close();
 assertEquals(0, hashMapRead.size());
 }

 @Test
 public void testWriteAndReadBackKeys() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutput output = new ObjectOutput(outputStream);
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.insertKey(1.0f);
 hashMap.insertKey(2.0f);
 hashMap.writeExternal(output);
 output.close();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ObjectInput input = new ObjectInput(inputStream);
 TFloatDoubleHash hashMapRead = (TFloatDoubleHash) input.readObject();
 input.close();
 assertEquals(2, hashMapRead.size());
 assertEquals(1.0f, hashMapRead.get(hashMapRead.index(1.0f)), 0.01f);
 assertEquals(2.0f, hashMapRead.get(hashMapRead.index(2.0f)), 0.01f);
 }
}