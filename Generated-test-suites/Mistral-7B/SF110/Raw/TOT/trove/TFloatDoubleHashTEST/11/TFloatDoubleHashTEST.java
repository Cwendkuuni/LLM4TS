import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float, Double> floatDoubleHash;

 @Before
 public void setUp() {
 floatDoubleHash = new TFloatDoubleHash<>(5, 0.75f);
 }

 @Test
 public void testEmptyHash() {
 assertTrue(floatDoubleHash.isEmpty());
 assertFalse(floatDoubleHash.contains(0.0f));
 }

 @Test
 public void testAddAndRemove() {
 floatDoubleHash.put(0.5f, 1.0);
 assertTrue(floatDoubleHash.containsKey(0.5f));
 assertTrue(floatDoubleHash.containsValue(1.0));
 floatDoubleHash.remove(0.5f);
 assertFalse(floatDoubleHash.containsKey(0.5f));
 assertFalse(floatDoubleHash.containsValue(1.0));
 }

 @Test
 public void testLoadFactor() {
 floatDoubleHash.put(0.1f, 2.0);
 floatDoubleHash.put(0.2f, 3.0);
 floatDoubleHash.put(0.3f, 4.0);
 floatDoubleHash.put(0.4f, 5.0);
 floatDoubleHash.put(0.5f, 6.0);

 assertEquals(5.0, floatDoubleHash.size(), 0.0);
 floatDoubleHash.put(0.6f, 7.0);

 assertTrue(floatDoubleHash.size() > 5.0);
 }

 @Test
 public void testDuplicateKey() {
 floatDoubleHash.put(0.1f, 2.0);
 floatDoubleHash.put(0.1f, 3.0);

 assertEquals(1.0, floatDoubleHash.get(0.1f), 0.0);
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeSlot() {
 floatDoubleHash.put(0.1f, 2.0);
 floatDoubleHash.put(0.2f, 3.0);
 floatDoubleHash.put(0.3f, 4.0);
 floatDoubleHash.put(0.4f, 5.0);
 floatDoubleHash.put(0.5f, 6.0);
 floatDoubleHash.put(0.6f, 7.0);
 floatDoubleHash.put(0.7f, 8.0);
 }

 @Test
 public void testNoEntryValue() {
 floatDoubleHash = new TFloatDoubleHash<>(5, 0.75f, Float.NaN, Double.NaN);
 assertFalse(floatDoubleHash.containsKey(Float.NaN));
 assertFalse(floatDoubleHash.containsValue(Double.NaN));
 }
}