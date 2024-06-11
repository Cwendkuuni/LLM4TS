package test;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Before
 public void setUp() {
 hash = new TFloatDoubleHash(5);
 }

 @Test
 public void testConstructor() {
 assertEquals(5, hash.capacity());
 assertNull(hash.getNoEntryKey());
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 assertTrue(hash.contains(0.0f));
 hash.put(0.0f, 0.0);
 assertTrue(hash.contains(0.0f));
 assertFalse(hash.contains(1.0f));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 new TFloatDoubleHash(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 new TFloatDoubleHash(5, -1.0f);
 }

 @Test
 public void testGetNoEntryKeyAndValue() {
 assertEquals(0.0f, hash.getNoEntryKey());
 assertEquals(0.0, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 assertEquals(5, hash.capacity());
 assertNotNull(hash._set);
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 0.0f);
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 hash.put(0.0f, 0.0);
 hash.put(1.0f, 1.0);
 hash.removeAt(0);
 assertFalse(hash.containsKey(0.0f));
 assertTrue(hash.containsKey(1.0f));
 }

 @Test
 public void testIndex() {
 hash.put(0.0f, 0.0);
 hash.put(1.0f, 1.0);
 assertEquals(0, hash.index(0.0f));
 assertEquals(-1, hash.index(1.1f));
 }

 @Test
 public void testInsertKey() {
 hash.put(0.0f, 0.0);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 assertEquals(3, hash.size());
 assertEquals(2, hash.index(0.0f));
 assertEquals(-1, hash.index(3.0f));
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 TFloatDoubleHash hashToWrite = new TFloatDoubleHash(5);
 hashToWrite.put(0.0f, 0.0);
 hashToWrite.put(1.0f, 1.0);
 hashToWrite.writeExternal(objectOutput);
 objectOutput.close();
 byteArrayOutputStream.close();
 byte[] byteArray = byteArrayOutputStream.toByteArray();
 ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArray));
 TFloatDoubleHash hashFromByteArray = (TFloatDoubleHash) objectInput.readObject();
 objectInput.close();
 assertTrue(hashFromByteArray.contains(0.0f));
 assertTrue(hashFromByteArray.contains(1.0f));
 }

}