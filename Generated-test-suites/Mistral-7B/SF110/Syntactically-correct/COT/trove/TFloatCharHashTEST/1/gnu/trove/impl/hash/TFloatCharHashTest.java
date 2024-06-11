import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.*;

public class TFloatCharHashTest {

 private TFloatCharHash<Float, Character> hash;

 @Before
 public void setup() {
 hash = new TFloatCharHash<>(5);
 }

 @Test
 public void testConstructor() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>();
 TFloatCharHash<Float, Character> hash1 = new TFloatCharHash(5);
 TFloatCharHash<Float, Character> hash2 = new TFloatCharHash(5, 0.5f);
 TFloatCharHash<Float, Character> hash3 = new TFloatCharHash(5, 0.5f, 0.0f, '\0');
 }

 @Test
 public void testGetNoEntryKey() {
 Float noEntryKey = hash.getNoEntryKey();
 assertEquals(0.0f, noEntryKey);
 }

 @Test
 public void testGetNoEntryValue() {
 Character noEntryValue = hash.getNoEntryValue();
 assertEquals('\0', noEntryValue);
 }

 @Test
 public void testSetUp() {
 int capacity = hash.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertFalse(hash.contains(3.0f));
 }

 @Test
 public void testForEach() {
 final TFloatProcedure<Float, Character> procedure = (key, value) -> {
 assertEquals(key, hash.get(key));
 };
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.removeAt(0);
 assertEquals(1, hash.size());
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testIndex() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(1.0f);
 assertEquals(1, hash.index(1.0f));
 assertEquals(-1, hash.index(3.0f));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexNegativeIndex() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.index(-1);
 }

 @Test(expected = NullPointerException.class)
 public void testIndexNullKey() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.index(null);
 }

 @Test
 public void testInsertKey() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 assertEquals(3, hash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyNegativeKey() {
 hash.insertKey(-1.0f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyNullKey() {
 hash.insertKey(null);
 }

 @Test
 public void testInsertKeyRehash() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 hash.insertKey(4.0f);
 hash.insertKey(5.0f);
 assertEquals(5, hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 hash.insertKey(4.0f);
 hash.insertKey(5.0f);
 hash.insertKey(6.0f);
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 hash.writeExternal(out);
 byte[] byteArray = byteStream.toByteArray();
 TFloatCharHash<Float, Character> hash2 = new TFloatCharHash<>();
 hash2.readExternal(new ObjectInputStream(new ByteArrayInputStream(byteArray)));
 assertEquals(hash.getNoEntryKey(), hash2.getNoEntryKey());
 assertEquals(hash.size(), hash2.size());
 for (Float key : hash.keySet()) {
 assertEquals(hash.get(key), hash2.get(key));
 }
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 hash.writeExternal(out);
 byte[] byteArray = byteStream.toByteArray();
 TFloatCharHash<Float, Character> hash2 = new TFloatCharHash<>();
 hash2.readExternal(new ObjectInputStream(new ByteArrayInputStream(byteArray)));
 assertEquals(hash.getNoEntryKey(), hash2.getNoEntryKey());
 assertEquals(hash.size(), hash2.size());
 for (Float key : hash.keySet()) {
 assertEquals(hash.get(key), hash2.get(key));
 }
 }

}