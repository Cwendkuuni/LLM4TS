import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float, Double> hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash<>(5, 0.75f);
 }

 @Test
 public void testEmptyMap() {
 assertTrue(hashMap.isEmpty());
 assertFalse(hashMap.containsKey(0.0f));
 assertNull(hashMap.get(0.0f));
 }

 @Test
 public void testPutGet() {
 hashMap.put(1.0f, 1.0);
 assertEquals(1.0, hashMap.get(1.0f), 0.0);
 }

 @Test
 public void testPutMultiple() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);

 assertEquals(1.0, hashMap.get(1.0f), 0.0);
 assertEquals(2.0, hashMap.get(2.0f), 0.0);
 assertEquals(3.0, hashMap.get(3.0f), 0.0);
 }

 @Test
 public void testLoadFactor() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.put(4.0f, 4.0);
 hashMap.put(5.0f, 5.0);
 hashMap.put(6.0f, 6.0);

 assertEquals(6.0, hashMap.size(), 0.0);
 assertTrue(hashMap.containsKey(1.0f));
 assertTrue(hashMap.containsKey(2.0f));
 assertTrue(hashMap.containsKey(3.0f));
 assertTrue(hashMap.containsKey(4.0f));
 assertTrue(hashMap.containsKey(5.0f));
 assertTrue(hashMap.containsKey(6.0f));
 }

 @Test
 public void testRemove() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);

 hashMap.remove(2.0f);
 assertFalse(hashMap.containsKey(2.0f));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCapacity() {
 hashMap = new TFloatDoubleHash<Float, Double>(-5, 0.75f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 hashMap = new TFloatDoubleHash<Float, Double>(5, -0.75f);
 }

 @Test
 public void testSerializable() {
 TFloatDoubleHash<Float, Double> clonedHash = null;
 try {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 out.writeObject(hashMap);
 out.close();
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 clonedHash = (TFloatDoubleHash<Float, Double>) in.readObject();
 } catch (IOException | ClassNotFoundException e) {
 e.printStackTrace();
 fail("Serialization failed");
 }
 assertTrue(clonedHash != null);
 assertEquals(hashMap.size(), clonedHash.size(), 0.0);
 }

 @Test
 public void testHashFunction() {
 Float key1 = 1.0f;
 Float key2 = 2.0f;
 int hashKey1 = HashFunctions.hash(key1);
 int hashKey2 = HashFunctions.hash(key2);

 int index1 = hashKey1 % hashMap._states.length;
 int index2 = hashKey2 % hashMap._states.length;

 byte state1 = hashMap._states[index1];
 byte state2 = hashMap._states[index2];

 assertEquals(0, state1);
 assertEquals(0, state2);
 }

 @Test
 public void testIndex() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);

 int index1 = hashMap.index(1.0f);
 int index2 = hashMap.index(2.0f);
 int index3 = hashMap.index(3.0f);
 int index4 = hashMap.index(4.0f);

 assertEquals(1, index1);
 assertEquals(2, index2);
 assertEquals(3, index3);
 assertEquals(-1, index4);
 }

 @Test
 public void testContains() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);

 assertTrue(hashMap.contains(1.0f));
 assertTrue(hashMap.contains(2.0f));
 assertTrue(hashMap.contains(3.0f));
 assertFalse(hashMap.contains(4.0f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override public boolean execute(float value) {
 int count = 0;
 while (value != 0.0f) {
 count += value % 10;
 value -= value % 10;
 value /= 10;
 }
 return count > 0;
 }
 };

 hashMap.put(10.0f, 10.0);
 hashMap.put(123.0f, 123.0);
 hashMap.put(357.0f, 357.0);

 assertTrue(hashMap.forEach(procedure));
 }
}