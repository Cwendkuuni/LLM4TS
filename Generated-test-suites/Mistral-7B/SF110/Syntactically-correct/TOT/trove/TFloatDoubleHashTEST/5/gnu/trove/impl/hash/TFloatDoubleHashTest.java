import org.junit.Assert;
import org.junit.Test;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash floatHash;

 @Test
 public void testEmptyHash() {
 floatHash = new TFloatDoubleHash(5);
 Assert.assertFalse(floatHash.contains(0.1f));
 }

 @Test
 public void testInitialCapacityAndLoadFactor() {
 floatHash = new TFloatDoubleHash(10, 0.75f);
 floatHash.put(0.1f, 1.0d);
 floatHash.put(0.2f, 2.0d);
 floatHash.put(0.3f, 3.0d);

 Assert.assertTrue(floatHash.contains(0.1f));
 Assert.assertTrue(floatHash.contains(0.2f));
 Assert.assertTrue(floatHash.contains(0.3f));

 Assert.assertEquals(3, floatHash.size());
 }

 @Test
 public void testNoEntryKeyAndNoEntryValue() {
 floatHash = new TFloatDoubleHash(10, 0.75f, 0.5f, 0.0d);
 floatHash.put(0.1f, 1.0d);
 floatHash.put(0.2f, 2.0d);
 floatHash.put(0.3f, 3.0d);

 Assert.assertEquals(0.5f, floatHash.getNoEntryKey(), 0.0f);
 Assert.assertEquals(0.0d, floatHash.getNoEntryValue(), 0.0d);
 }

 @Test
 public void testContains() {
 floatHash = new TFloatDoubleHash(5);
 floatHash.put(0.1f, 1.0d);

 Assert.assertTrue(floatHash.contains(0.1f));
 Assert.assertFalse(floatHash.contains(0.2f));
 }

 @Test
 public void testRemove() {
 floatHash = new TFloatDoubleHash(5);
 floatHash.put(0.1f, 1.0d);
 floatHash.put(0.2f, 2.0d);

 Assert.assertTrue(floatHash.contains(0.1f));
 Assert.assertTrue(floatHash.contains(0.2f));

 floatHash.remove(0.1f);
 Assert.assertFalse(floatHash.contains(0.1f));
 Assert.assertTrue(floatHash.contains(0.2f));
 }

 @Test
 public void testRehash() {
 floatHash = new TFloatDoubleHash(3);
 floatHash.put(0.1f, 1.0d);
 floatHash.put(0.2f, 2.0d);
 floatHash.put(0.3f, 3.0d);

 Assert.assertEquals(-1, floatHash.index(0.4f));

 floatHash.put(0.4f, 4.0d);

 Assert.assertEquals(1, floatHash.size());
 }

 @Test
 public void testProcedure() {
 floatHash = new TFloatDoubleHash(5);
 floatHash.put(0.1f, 1.0d);
 floatHash.put(0.2f, 2.0d);
 floatHash.put(0.3f, 3.0d);

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 if (value == 0.2f) {
 System.out.println("Found 0.2f");
 return false;
 }
 return true;
 }
 };

 Assert.assertTrue(floatHash.forEach(procedure));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertFullHash() {
 floatHash = new TFloatDoubleHash(3);
 for (float i = 0.0f; i < 4.0f; i++) {
 floatHash.put(i, i);
 }
 }
}