import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

 private TFloatCharHash<Float, Character> hashMap;

 @Before
 public void setup() {
 hashMap = new TFloatCharHash<>(5, 0.75f);
 }

 @Test
 public void testEmptyHashMap() {
 assertTrue(hashMap.isEmpty());
 assertEquals(0, hashMap.size());
 assertNull(hashMap.getNoEntryKey());
 assertEquals('\0', hashMap.getNoEntryValue());
 }

 @Test
 public void testBasicOperations() {
 hashMap.put(1.0f, 'a');
 hashMap.put(2.0f, 'b');
 hashMap.put(3.0f, 'c');

 assertEquals(3, hashMap.size());
 assertTrue(hashMap.containsKey(1.0f));
 assertTrue(hashMap.containsKey(2.0f));
 assertTrue(hashMap.containsKey(3.0f));
 assertEquals('a', hashMap.get(1.0f));
 assertEquals('b', hashMap.get(2.0f));
 assertEquals('c', hashMap.get(3.0f));

 assertTrue(hashMap.remove(1.0f) > -1);
 assertEquals(2, hashMap.size());
 assertFalse(hashMap.containsKey(1.0f));
 assertEquals('b', hashMap.get(2.0f));
 assertEquals('c', hashMap.get(3.0f));

 hashMap.clear();
 assertEquals(0, hashMap.size());
 assertFalse(hashMap.containsKey(2.0f));
 assertFalse(hashMap.containsKey(3.0f));
 }

 @Test
 public void testLoadFactor() {
 for (int i = 0; i < 6; i++) {
 hashMap.put(i * 0.5f, (char) i);
 }

 assertEquals(6, hashMap.size());
 assertTrue(hashMap.containsKey(0.5f));
 assertTrue(hashMap.containsKey(1.5f));
 assertTrue(hashMap.containsKey(2.5f));
 assertTrue(hashMap.containsKey(3.5f));
 assertTrue(hashMap.containsKey(4.5f));
 assertTrue(hashMap.containsKey(5.5f));

 assertTrue(hashMap.containsValue('0'));
 assertTrue(hashMap.containsValue('1'));
 assertTrue(hashMap.containsValue('2'));
 assertTrue(hashMap.containsValue('3'));
 assertTrue(hashMap.containsValue('4'));
 assertTrue(hashMap.containsValue('5'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 hashMap = new TFloatCharHash<Float, Character>(-1, 0.75f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 hashMap = new TFloatCharHash<Float, Character>(5, -0.75f);
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 hashMap.writeExternal(oos);
 ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TFloatCharHash<Float, Character> deserializedHashMap = (TFloatCharHash<Float, Character>) ois.readObject();

 assertEquals(6, deserializedHashMap.size());
 assertTrue(deserializedHashMap.containsKey(0.5f));
 assertTrue(deserializedHashMap.containsKey(1.5f));
 assertTrue(deserializedHashMap.containsKey(2.5f));
 assertTrue(deserializedHashMap.containsKey(3.5f));
 assertTrue(deserializedHashMap.containsKey(4.5f));
 assertTrue(deserializedHashMap.containsKey(5.5f));
 }

 @Test
 public void testHashFunction() {
 float key = 1234567.0f;
 int expectedIndex = HashFunctions.hash(key) % hashMap._states.length;

 hashMap.put(key, (char) 1);
 assertEquals(1, hashMap._states[expectedIndex]);
 }
}