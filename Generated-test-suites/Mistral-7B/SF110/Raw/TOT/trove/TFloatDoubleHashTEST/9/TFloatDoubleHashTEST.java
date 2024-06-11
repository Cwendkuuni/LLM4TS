import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float> hashMap;

 @Test
 public void testEmptyHashMap() {
 hashMap = new TFloatDoubleHash<>(5);
 assertTrue(hashMap.isEmpty());
 assertFalse(hashMap.containsKey((float) 0.0));
 }

 @Test
 public void testAddKeyValuePair() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.put((float) 1.0, (double) 1.0);
 hashMap.put((float) 2.0, (double) 2.0);
 assertEquals(2, hashMap.size());
 assertTrue(hashMap.containsKey((float) 1.0));
 assertTrue(hashMap.containsKey((float) 2.0));
 }

 @Test
 public void testAddKeyValuePairWithSameKey() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.put((float) 1.0, (double) 1.0);
 hashMap.put((float) 1.0, (double) 2.0);
 assertEquals(2, hashMap.size());
 assertTrue(hashMap.containsKey((float) 1.0));
 assertFalse(hashMap.containsValue((double) 1.0));
 assertTrue(hashMap.containsValue((double) 2.0));
 }

 @Test
 public void testRemoveKeyValuePair() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.put((float) 1.0, (double) 1.0);
 hashMap.put((float) 2.0, (double) 2.0);
 hashMap.remove((float) 1.0);
 assertEquals(1, hashMap.size());
 assertFalse(hashMap.containsKey((float) 1.0));
 }

 @Test
 public void testForEachProcedure() {
 hashMap = new TFloatDoubleHash<>(5);
 hashMap.put((float) 1.0, (double) 1.0);
 hashMap.put((float) 2.0, (double) 2.0);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return hashMap.remove(key);
 }
 };
 hashMap.forEachProcedure(procedure);
 assertEquals(0, hashMap.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 hashMap = new TFloatDoubleHash<Float>(-5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 hashMap = new TFloatDoubleHash<Float>(5, -0.5f);
 }

 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 // Write test
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutputStream objectOut = new ObjectOutputStream(out);
 TFloatDoubleHash<Float> hashMap1 = new TFloatDoubleHash<Float>(5);
 hashMap1.put((float) 1.0, (double) 1.0);
 hashMap1.put((float) 2.0, (double) 2.0);
 objectOut.writeObject(hashMap1);
 objectOut.close();
 byte[] bytes = out.toByteArray();

 // Read test
 ByteArrayInputStream in = new ByteArrayInputStream(bytes);
 ObjectInputStream objectIn = new ObjectInputStream(in);
 TFloatDoubleHash<Float> hashMap2 = (TFloatDoubleHash<Float>) objectIn.readObject();
 objectIn.close();

 assertEquals(2, hashMap2.size());
 assertTrue(hashMap2.containsKey((float) 1.0));
 assertTrue(hashMap2.containsKey((float) 2.0));
 }
}