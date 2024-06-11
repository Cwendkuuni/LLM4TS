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
 public void testConstructor() {
 assertNotNull(hashMap);
 assertEquals(5, hashMap.size());
 }

 @Test
 public void testGetNoEntryKeyAndValue() {
 assertEquals(0.0f, hashMap.getNoEntryKey(), 0.001f);
 assertEquals(0.0, hashMap.getNoEntryValue(), 0.001);
 }

 @Test
 public void testSetUp() {
 assertEquals(5, hashMap.capacity());
 hashMap.setUp(10);
 assertEquals(10, hashMap.capacity());
 }

 @Test
 public void testContainsNullKey() {
 assertFalse(hashMap.contains(null));
 }

 @Test
 public void testContainsSameKeyMultipleTimes() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(1.0f, 2.0);
 assertTrue(hashMap.contains(1.0f));
 }

 @Test
 public void testContainsDifferentKeys() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 assertFalse(hashMap.contains(3.0f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, value, 0.001f);
 return true;
 }
 };
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.forEach(procedure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 int index = hashMap.indexOfKey(2.0f);
 hashMap.removeAt(index);
 assertEquals(2, hashMap.size());
 assertFalse(hashMap.containsKey(2.0f));
 }

 @Test
 public void testIndex() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 int index = hashMap.indexOfKey(2.0f);
 assertEquals(1, index);
 }

 @Test
 public void testIndexRehashed() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.put(4.0f, 4.0);
 int index = hashMap.indexRehashed(4.0f, 3, 13, (byte) 1);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.clear();
 float key = 4.0f;
 int index = hashMap.insertKey(key);
 assertEquals(4, hashMap.size());
 assertTrue(hashMap.containsKey(key));
 assertEquals(index, hashMap.indexOfKey(key));
 }

 @Test
 public void testInsertKeyRehash() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.clear();
 float key = 4.0f;
 int index = hashMap.insertKeyRehash(key, 3, 13, (byte) 1);
 assertEquals(4, hashMap.size());
 assertTrue(hashMap.containsKey(key));
 assertEquals(index, hashMap.indexOfKey(key));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.put(4.0f, 4.0);
 hashMap.put(5.0f, 5.0);
 try {
 hashMap.insertKey(5.0f);
 } catch (IllegalStateException e) {
 assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
 }
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 hashMap.writeExternal(objectOutputStream);
 objectOutputStream.close();
 byte[] bytes = byteArrayOutputStream.toByteArray();
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
 ObjectInput objectInput = new ObjectInput(byteArrayInputStream);
 TFloatDoubleHash<Float, Double> hashMapClone = (TFloatDoubleHash<Float, Double>) objectInput.readObject();
 assertEquals(hashMap, hashMapClone);
 }
}