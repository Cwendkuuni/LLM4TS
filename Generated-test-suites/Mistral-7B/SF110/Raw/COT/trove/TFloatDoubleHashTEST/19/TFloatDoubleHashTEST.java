package tests; import gnu.trove.impl.hash.*; import org.junit.Assert; import org.junit.Test;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float> testHash;

 @Before
 public void setUp() {
 testHash = new TFloatDoubleHash<>(5);
 }

 @Test
 public void testConstructor() {
 // Test default constructor
 TFloatDoubleHash<Float> hash = new TFloatDoubleHash<>();
 // Test constructors with initialCapacity and loadFactor
 TFloatDoubleHash<Float> hash1 = new TFloatDoubleHash<>(10, 0.75f);
 // Test constructors with initialCapacity, loadFactor, no_entry_key, and no_entry_value
 TFloatDoubleHash<Float> hash2 = new TFloatDoubleHash<>(15, 0.9f, 0.1f, 1.0d);
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatDoubleHash<Float> hash = testHash;
 float noEntryKey = hash.getNoEntryKey();
 Assert.assertEquals(0.0f, noEntryKey, 0);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatDoubleHash<Float> hash = testHash;
 double noEntryValue = hash.getNoEntryValue();
 Assert.assertEquals(0.0d, noEntryValue, 0);
 }

 @Test
 public void testSetUp() {
 int capacity = testHash.setUp(5);
 Assert.assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 TFloatDoubleHash<Float> hash = testHash;
 // Test case when the hash contains the given key
 hash.insertKey(1.0f);
 Assert.assertTrue(hash.contains(1.0f));
 // Test case when the hash does not contain the given key
 Assert.assertFalse(hash.contains(2.0f));
 }

 @Test(expected = java.lang.IndexOutOfBoundsException.class)
 public void testIndexNegativeIndex() {
 TFloatDoubleHash<Float> hash = testHash;
 int index = hash.index(-1);
 }

 @Test
 public void testInsertKey() {
 TFloatDoubleHash<Float> hash = testHash;
 // Test case when the hash does not contain the given key
 int index = hash.insertKey(1.0f);
 Assert.assertEquals(0, index);
 // Test case when the hash already contains the given key
 hash.insertKey(1.0f);
 index = hash.insertKey(1.0f);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testForEach() {
 TFloatDoubleHash<Float> hash = testHash;
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 TFloatProcedure<Float> procedure = new TFloatProcedure<Float>() {
 @Override
 public boolean execute(Float key) {
 return key > 1.0f;
 }
 };
 boolean result = hash.forEach(procedure);
 Assert.assertTrue(result);
 }

 @Test(expected = java.lang.NullPointerException.class)
 public void testForEachNullProcedure() {
 TFloatDoubleHash<Float> hash = testHash;
 hash.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 TFloatDoubleHash<Float> hash = testHash;
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 int index = hash.indexOf(1.0f);
 hash.removeAt(index);
 Assert.assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testIndex() {
 TFloatDoubleHash<Float> hash = testHash;
 hash.insertKey(1.0f);
 int index = hash.indexOf(1.0f);
 Assert.assertEquals(0, index);
 hash.insertKey(2.0f);
 index = hash.indexOf(1.0f);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testIndexRehashed() {
 TFloatDoubleHash<Float> hash = testHash;
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 int index = hash.indexRehashed(1.0f, 1, 1, (byte) 0);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatDoubleHash<Float> hash = testHash;
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 hash.writeExternal(out);
 byte[] bytes = byteStream.toByteArray();
 // Code to test writing and reading the hash back from the byte array goes here
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 TFloatDoubleHash<Float> hash = testHash;
 ByteArrayInputStream byteStream = new ByteArrayInputStream(testHash.writeExternal(new ByteArrayOutputStream().toByteArray()));
 ObjectInput in = new ObjectInputStream(byteStream);
 TFloatDoubleHash<Float> readHash = (TFloatDoubleHash<Float>) in.readObject();
 Assert.assertEquals(hash, readHash);
 }

}