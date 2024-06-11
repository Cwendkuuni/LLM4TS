import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash(5, 0.75f);
 }

 @Test
 public void testEmptyHashMap() {
 assertTrue(hashMap.isEmpty());
 assertFalse(hashMap.containsKey(Float.NaN));
 }

 @Test
 public void testPutAndGet() {
 hashMap.put(Float.NAΝ, 1.0d);
 hashMap.put(0.5f, 2.0d);
 assertEquals(1.0d, hashMap.get(Float.NaN), 0.0);
 assertEquals(2.0d, hashMap.get(0.5f), 0.0);
 }

 @Test
 public void testPutAndGetMultipleValues() {
 hashMap.put(0.5f, 2.0d);
 hashMap.put(0.5f, 3.0d);
 assertEquals(3.0d, hashMap.get(0.5f), 0.0);
 }

 @Test
 public void testPutKeyAlreadyExists() {
 hashMap.put(0.5f, 2.0d);
 hashMap.put(0.5f, 3.0d);
 assertEquals(3.0d, hashMap.get(0.5f), 0.0);
 assertFalse(hashMap.containsValue(2.0d));
 }

 @Test
 public void testRemove() {
 hashMap.put(0.5f, 2.0d);
 hashMap.put(1.0f, 3.0d);
 hashMap.remove(0.5f);
 assertFalse(hashMap.containsKey(0.5f));
 assertEquals(3.0d, hashMap.get(1.0f), 0.0);
 }

 @Test
 public void testContains() {
 hashMap.put(0.5f, 2.0d);
 assertTrue(hashMap.containsKey(0.5f));
 assertFalse(hashMap.containsKey(1.0f));
 }

 @Test
 public void testForEachProcedure() {
 float sum = 0.0f;
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 sum += key;
 return true;
 }
 };
 hashMap.put(1.0f, 1.0d);
 hashMap.put(2.0f, 2.0d);
 hashMap.put(3.0f, 3.0d);
 hashMap.forEach(procedure);
 assertEquals(6.0f, sum, 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullKey() {
 hashMap.put(null, 1.0d);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNegativeCapacity() {
 hashMap.put(-1, 1.0d);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNegativeLoadFactor() {
 hashMap.put(5, -0.5f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWriteExternalNegativeLoadFactor() {
 byte[] buffer = new byte[1024];
 ObjectOutput out = new ByteArrayOutputStream(buffer);
 TFloatDoubleHash hashMap2 = new TFloatDoubleHash(5, -0.5f);
 try {
 hashMap2.writeExternal(out);
 } catch (IOException e) {
 fail("IOException should not be thrown");
 }
 String exceptionMessage = new String(buffer);
 assertTrue(exceptionMessage.contains("Load factor must be >= 0.0"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadExternalInvalidVersion() {
 byte[] buffer = new byte[1024];
 ObjectInput in = new ByteArrayInputStream(new byte[]{-1});
 TFloatDoubleHash hashMap2 = new TFloatDoubleHash();
 try {
 hashMap2.readExternal(in);
 } catch (IOException e) {
 fail("IOException should not be thrown");
 }
 String exceptionMessage = new String(buffer);
 assertTrue(exceptionMessage.contains("Invalid class version"));
}
}