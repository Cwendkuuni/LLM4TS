import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 TFloatDoubleHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash(5);
 }

 @Test
 public void testConstructor() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash();
 assertNotNull(hashMap);

 hashMap = new TFloatDoubleHash(10);
 assertNotNull(hashMap);

 hashMap = new TFloatDoubleHash(10, 0.75f);
 assertNotNull(hashMap);
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash();
 float noEntryKey = hashMap.getNoEntryKey();
 assertEquals(0.0f, noEntryKey, 0.001f);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash();
 double noEntryValue = hashMap.getNoEntryValue();
 assertEquals(0.0, noEntryValue, 0.001);
 }

 @Test
 public void testSetUp() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 assertEquals(5, hashMap.setUp(5));
 }

 @Test
 public void testContains() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.put(1.0f, 1.0);
 assertTrue(hashMap.contains(1.0f));
 }

 @Test
 public void testContainsWithEmptyMap() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 assertFalse(hashMap.contains(1.0f));
 }

 @Test
 public void testForEach() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 1.0f, 0.001f);
 return true;
 }
 };
 hashMap.forEach(procedure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 int index = hashMap.indexOf(1.0f);
 hashMap.removeAt(index);
 assertFalse(hashMap.contains(1.0f));
 }

 @Test
 public void testIndex() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 int index = hashMap.index(1.0f);
 assertEquals(0, index);
 }

 @Test
 public void testIndexWithEmptyMap() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 int index = hashMap.index(1.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexRehashed() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 int index = hashMap.index(4.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 int index = hashMap.insertKey(1.0f);
 assertEquals(0, index);
 hashMap.put(2.0f, 2.0);
 index = hashMap.insertKey(1.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKeyRehash() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 int index = hashMap.insertKeyRehash(4.0f, 0, 1, (byte) 1);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKeyAt() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.insertKeyAt(0, 1.0f);
 assertEquals(1.0f, hashMap.get(0), 0.001f);
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 hashMap.writeExternal(objectOutput);
 byte[] bytes = byteArrayOutputStream.toByteArray();
 TFloatDoubleHash deserializedHashMap = new TFloatDoubleHash();
 deserializedHashMap.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(1.0f, deserializedHashMap.get(0), 0.001f);
 assertEquals(2.0f, deserializedHashMap.get(1), 0.001f);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 TFloatDoubleHash hashMap = new TFloatDoubleHash();
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
 ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
 hashMap.readExternal(objectInput);
 assertNull(hashMap.get(0.0f));
 }

}