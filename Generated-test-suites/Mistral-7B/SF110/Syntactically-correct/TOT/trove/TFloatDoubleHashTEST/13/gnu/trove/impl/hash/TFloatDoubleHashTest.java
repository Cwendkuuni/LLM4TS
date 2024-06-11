import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash(5);
 }

 @Test
 public void testEmptyMap() {
 assertTrue(hashMap.isEmpty());
 assertFalse(hashMap.containsKey(0.0f));
 }

 @Test
 public void testPutAndGet() {
 hashMap.put(1.0f, 1.0);
 assertEquals(1.0, hashMap.get(1.0f), 0.0);
 }

 @Test
 public void testPutAndGetMultipleKeys() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 assertEquals(1.0, hashMap.get(1.0f), 0.0);
 assertEquals(2.0, hashMap.get(2.0f), 0.0);
 assertEquals(3.0, hashMap.get(3.0f), 0.0);
 }

 @Test
 public void testLoadFactor() {
 hashMap = new TFloatDoubleHash(5, 0.75f);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.put(4.0f, 4.0);
 hashMap.put(5.0f, 5.0);
 hashMap.put(6.0f, 6.0);
 assertTrue(hashMap.isFull());
 }

 @Test
 public void testRemove() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.remove(2.0f);
 assertFalse(hashMap.containsKey(2.0f));
 }

 @Test
 public void testContains() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 assertTrue(hashMap.containsKey(1.0f));
 assertTrue(hashMap.containsKey(2.0f));
 assertTrue(hashMap.containsKey(3.0f));
 assertFalse(hashMap.containsKey(4.0f));
 }

 @Test
 public void testForEach() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 assertEquals(key, hashMap.get(key), 0.0);
 return true;
 }
 };
 hashMap.forEach(procedure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 hashMap = new TFloatDoubleHash(5, -0.75f);
 }

 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
 hashMap.writeExternal(objectStream);
 objectStream.close();
 byte[] bytes = byteStream.toByteArray();
 ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TFloatDoubleHash hashMapClone = (TFloatDoubleHash) objectInput.readObject();
 assertEquals(hashMap, hashMapClone);
 }
}