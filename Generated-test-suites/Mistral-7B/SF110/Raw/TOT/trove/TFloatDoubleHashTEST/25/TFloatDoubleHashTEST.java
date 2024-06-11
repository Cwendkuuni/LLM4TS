import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash(5, 0.75f);
 }

 @Test
 public void testContains() {
 float key = 0.5f;
 hashMap.insertKey(key);
 assertTrue(hashMap.contains(key));
 }

 @Test
 public void testDoesNotContain() {
 float key = 1.5f;
 assertFalse(hashMap.contains(key));
 }

 @Test
 public void testAddAndGet() {
 float key = 0.5f;
 float value = 5.5f;
 hashMap.insertKey(key);
 assertEquals(value, hashMap.get(key), 0.0);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddDuplicateKey() {
 float key = 0.5f;
 hashMap.insertKey(key);
 try {
 hashMap.insertKey(key);
 } catch (IllegalStateException e) {
 assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
 }
 }

 @Test
 public void testRemove() {
 float key = 0.5f;
 hashMap.insertKey(key);
 hashMap.remove(key);
 assertFalse(hashMap.contains(key));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKey() {
 try {
 hashMap.insertKey(null);
 } catch (IllegalArgumentException e) {
 assertEquals("Argument cannot be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeCapacity() {
 try {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(-5, 0.75f);
 } catch (IllegalArgumentException e) {
 assertEquals("Initial capacity cannot be negative", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 try {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5, -0.75f);
 } catch (IllegalArgumentException e) {
 assertEquals("Load factor must be between 0.0 and 1.0", e.getMessage());
 }
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 hashMap.writeExternal(oos);
 byte[] bytes = baos.toByteArray();
 ObjectInput ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TFloatDoubleHash deserializedHashMap = (TFloatDoubleHash) ois.readObject();
 assertEquals(hashMap.getNoEntryKey(), deserializedHashMap.getNoEntryKey());
 assertEquals(hashMap.getNoEntryValue(), deserializedHashMap.getNoEntryValue());
 }
}