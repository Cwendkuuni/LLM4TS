import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float, Double> hashMap;

 @Test
 public void testEmptyMap() {
 hashMap = new TFloatDoubleHash<>(10);
 assertTrue(hashMap.isEmpty());
 assertFalse(hashMap.containsKey((float) 0.0));
 }

 @Test
 public void testBasicOperations() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.put((float) 1.0, (double) 1.0);
 hashMap.put((float) 2.0, (double) 2.0);
 hashMap.put((float) 3.0, (double) 3.0);

 assertEquals(3, hashMap.size());
 assertTrue(hashMap.containsKey((float) 1.0));
 assertTrue(hashMap.containsKey((float) 2.0));
 assertTrue(hashMap.containsKey((float) 3.0));

 assertEquals((double) 1.0, hashMap.get((float) 1.0), 0.001);
 assertEquals((double) 2.0, hashMap.get((float) 2.0), 0.001);
 assertEquals((double) 3.0, hashMap.get((float) 3.0), 0.001);

 hashMap.remove((float) 2.0);
 assertEquals(2, hashMap.size());
 assertFalse(hashMap.containsKey((float) 2.0));
 }

 @Test
 public void testPutNullKey() {
 hashMap = new TFloatDoubleHash<>(5);
 try {
 hashMap.put(null, (double) 1.0);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetNullKey() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.put((float) 1.0, (double) 1.0);
 try {
 hashMap.get((float) null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testForEachProcedure() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.put((float) 1.0, (double) 1.0);
 hashMap.put((float) 2.0, (double) 2.0);
 hashMap.put((float) 3.0, (double) 3.0);

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float floatValue) {
 assertEquals((float) 1.0, floatValue, 0.001);
 return true;
 }
 };

 assertTrue(hashMap.forEach(procedure));
 }

 @Test(expected = IllegalStateException.class)
 public void testFullMap() {
 hashMap = new TFloatDoubleHash<>(3, 0.5f);
 for (float i = 0.0f; i <= 3.0f; i += 0.1f) {
 hashMap.put((float) i, (double) i);
 }
 }
}