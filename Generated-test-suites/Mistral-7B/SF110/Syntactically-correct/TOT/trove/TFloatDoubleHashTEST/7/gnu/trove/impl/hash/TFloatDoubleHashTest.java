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
 public void testInitialization() {
 assertEquals(5, hashMap.size());
 assertNull(hashMap.getNoEntryValue());
 assertEquals(0.0f, hashMap.getNoEntryKey(), 0.0f);
 }

 @Test
 public void testContains() {
 // typical use case
 hashMap.insert(1.0f, 1.0);
 assertTrue(hashMap.contains(1.0f));

 // edge case: empty map
 assertFalse(new TFloatDoubleHash().contains(1.0f));

 // error scenario: null key
 assertFalse(hashMap.contains(null));
 }

 @Test
 public void testForEach() {
 // typical use case
 float[] result = new float[hashMap.size()];
 int index = 0;
 hashMap.forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float element) {
 result[index++] = element;
 return true;
 }
 });
 assertArrayEquals(new float[]{1.0f}, result);

 // edge case: empty map
 new TFloatDoubleHash().forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float element) {
 return true;
 }
 });

 // error scenario: map with no free slots
 hashMap.insert(Float.MAX_VALUE, Float.MAX_VALUE);
 assertThrows(IllegalStateException.class, () -> hashMap.insert(Float.MAX_VALUE, Float.MAX_VALUE));
 }

 @Test
 public void testRemoveAt() {
 // typical use case
 hashMap.insert(1.0f, 1.0);
 hashMap.insert(2.0f, 2.0);
 int index = hashMap.indexOf(1.0f);
 hashMap.removeAt(index);
 assertEquals(1, hashMap.size());
 assertFalse(hashMap.contains(1.0f));

 // edge case: empty map
 new TFloatDoubleHash().removeAt(-1);

 // error scenario: index out of bounds
 hashMap.removeAt(hashMap.size());
 }

 @Test
 public void testIndex() {
 // typical use case
 hashMap.insert(1.0f, 1.0);
 hashMap.insert(2.0f, 2.0);
 int index = hashMap.index(1.0f);
 assertEquals(0, index);
 index = hashMap.index(2.0f);
 assertEquals(1, index);

 // edge case: empty map
 new TFloatDoubleHash().index(1.0f);

 // error scenario: null key
 assertNull(hashMap.index(null));
 }

 @Test
 public void testInsertKey() {
 // typical use case
 hashMap.insert(1.0f, 1.0);
 hashMap.insert(2.0f, 2.0);
 assertEquals(2, hashMap.size());
 assertTrue(hashMap.containsKey(1.0f));
 assertTrue(hashMap.containsKey(2.0f));

 // edge case: full map
 TFloatDoubleHash fullMap = new TFloatDoubleHash(5, 0.75f);
 for (int i = 0; i < 5; i++) {
 fullMap.insert(i, i);
 }
 assertThrows(IllegalStateException.class, () -> fullMap.insert(5.0f, 5.0));

 // error scenario: insert duplicate key
 hashMap.insert(1.0f, 1.0);
 assertThrows(IllegalArgumentException.class, () -> hashMap.insert(1.0f, 2.0));
 }

 @Test
 public void testWriteAndReadExternal() throws IOException, ClassNotFoundException {
 // write and read back the same instance
 TFloatDoubleHash hashMap1 = new TFloatDoubleHash(5, 0.75f);
 hashMap1.insert(1.0f, 1.0);
 hashMap1.insert(2.0f, 2.0);
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
 objectStream.writeObject(hashMap1);
 byte[] bytes = byteStream.toByteArray();
 ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
 ObjectInputStream objectInput = new ObjectInputStream(byteInput);
 TFloatDoubleHash hashMap2 = (TFloatDoubleHash) objectInput.readObject();
 assertEquals(hashMap1, hashMap2);

 // write and read back different instances
 TFloatDoubleHash hashMap3 = new TFloatDoubleHash(10, 0.75f);
 hashMap3.insert(3.0f, 3.0);
 hashMap3.insert(4.0f, 4.0);
 ByteArrayOutputStream byteStream2 = new ByteArrayOutputStream();
 ObjectOutputStream objectStream2 = new ObjectOutputStream(byteStream2);
 objectStream2.writeObject(hashMap3);
 byte[] bytes2 = byteStream2.toByteArray();
 ByteArrayInputStream byteInput2 = new ByteArrayInputStream(bytes2);
 ObjectInputStream objectInput2 = new ObjectInputStream(byteInput2);
 TFloatDoubleHash hashMap4 = (TFloatDoubleHash) objectInput2.readObject();
 assertNotEquals(hashMap1, hashMap4);
 }
}