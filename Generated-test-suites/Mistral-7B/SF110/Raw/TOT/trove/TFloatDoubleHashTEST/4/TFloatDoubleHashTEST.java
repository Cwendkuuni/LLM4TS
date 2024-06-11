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
 public void testEmptyMap() {
 assertEquals(0, hashMap.size());
 assertFalse(hashMap.contains(0.5f));
 }

 @Test
 public void testPutAndGet() {
 hashMap.put(0.5f, 1.0);
 assertEquals(1, hashMap.size());
 assertTrue(hashMap.containsKey(0.5f));
 assertEquals(1.0, hashMap.get(0.5f));
 }

 @Test
 public void testPutMultipleKeys() {
 hashMap.put(0.5f, 1.0);
 hashMap.put(1.5f, 2.0);
 hashMap.put(2.5f, 3.0);
 assertEquals(3, hashMap.size());
 assertTrue(hashMap.containsKey(0.5f));
 assertTrue(hashMap.containsKey(1.5f));
 assertTrue(hashMap.containsKey(2.5f));
 assertEquals(1.0, hashMap.get(0.5f));
 assertEquals(2.0, hashMap.get(1.5f));
 assertEquals(3.0, hashMap.get(2.5f));
 }

 @Test
 public void testPutAndGetNullValues() {
 hashMap.put(0.5f, null);
 hashMap.put(1.5f, "Hello World");
 assertEquals(2, hashMap.size());
 assertTrue(hashMap.containsKey(0.5f));
 assertNull(hashMap.get(0.5f));
 assertTrue(hashMap.containsKey(1.5f));
 assertEquals("Hello World", hashMap.get(1.5f));
 }

 @Test
 public void testPutLoadFactor() {
 for (int i = 0; i < 10; i++) {
 hashMap.put((float) i * 0.1, (float) i * 0.1);
 }
 assertEquals(10, hashMap.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testPutFullMap() {
 for (int i = 0; i < 11; i++) {
 hashMap.put((float) i * 0.1, (float) i * 0.1);
 }
 }

 @Test
 public void testRemoveKey() {
 hashMap.put(0.5f, 1.0);
 hashMap.put(1.5f, 2.0);
 hashMap.put(2.5f, 3.0);
 hashMap.remove(1.5f);
 assertEquals(2, hashMap.size());
 assertFalse(hashMap.containsKey(1.5f));
 }

 @Test
 public void testForEachProcedure() {
 float sum = 0.0f;
 hashMap.put(0.5f, 1.0);
 hashMap.put(1.5f, 2.0);
 hashMap.put(2.5f, 3.0);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 sum += value;
 return true;
 }
 };
 hashMap.forEach(procedure);
 assertEquals(6.0f, sum, 0.01);
 }
}