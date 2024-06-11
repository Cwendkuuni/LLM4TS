import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.impl.hash.TFloatCharHash;

public class TFloatCharHashTest {

 private TFloatCharHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatCharHash(5);
 }

 @Test
 public void testEmptyHashMap() {
 assertFalse(hashMap.contains(0.0f));
 assertFalse(hashMap.contains((float) 1.0));
 }

 @Test
 public void testPutAndGet() {
 hashMap.put(0.0f, 'a');
 hashMap.put(1.0f, 'b');
 assertEquals('a', hashMap.get(0.0f));
 assertEquals('b', hashMap.get(1.0f));
 }

 @Test
 public void testContains() {
 hashMap.put(0.0f, 'a');
 hashMap.put(1.0f, 'b');
 assertTrue(hashMap.contains(0.0f));
 assertTrue(hashMap.contains(1.0f));
 }

 @Test
 public void testRemove() {
 hashMap.put(0.0f, 'a');
 hashMap.put(1.0f, 'b');
 hashMap.remove(0.0f);
 assertFalse(hashMap.contains(0.0f));
 assertTrue(hashMap.contains(1.0f));
 }

 @Test
 public void testDuplicateEntry() {
 hashMap.put(0.0f, 'a');
 hashMap.put(0.0f, 'b');
 assertEquals('a', hashMap.get(0.0f));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCapacity() {
 new TFloatCharHash(-5);
 }

 @Test
 public void testLoadFactor() {
 hashMap.put(0.0f, 'a');
 hashMap.put(1.0f, 'b');
 hashMap.put(2.0f, 'c');
 assertEquals(0.5f, hashMap.getLoadFactor(), 0.001f);
 }

 @Test
 public void testProcedureIteration() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 if (key == 0.0f) {
 fail("Key should not be in the hashmap");
 }
 return true;
 }
 };
 hashMap.put(0.0f, 'a');
 hashMap.put(1.0f, 'b');
 hashMap.forEach(procedure);
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException {
 TFloatCharHash hashMapToWrite = new TFloatCharHash(5);
 hashMapToWrite.put(0.0f, 'a');
 hashMapToWrite.put(1.0f, 'b');

 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 hashMapToWrite.writeExternal(objStream);
 objStream.close();

 TFloatCharHash hashMapToRead = new TFloatCharHash(5);
 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
 ObjectInputStream objInput = new ObjectInputStream(byteInput);
 hashMapToRead.readExternal(objInput);
 objInput.close();

 assertEquals('a', hashMapToRead.get(0.0f));
 assertEquals('b', hashMapToRead.get(1.0f));
 }
}