package tests; import gnu.trove.impl.hash.*; import org.junit.Assert.*; import org.junit.Test;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Test
 public void testInitialization() {
 hash = new TFloatDoubleHash();
 Assert.assertEquals(0, hash.size());
 Assert.assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
 Assert.assertEquals(0.0, hash.getNoEntryValue(), 0.0);
 }

 @Test
 public void testInitialCapacity() {
 hash = new TFloatDoubleHash(10);
 Assert.assertEquals(10, hash.size());
 }

 @Test
 public void testLoadFactor() {
 hash = new TFloatDoubleHash(10, 0.75f);
 Assert.assertEquals(10, hash.capacity());
 Assert.assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
 Assert.assertEquals(0.0, hash.getNoEntryValue(), 0.0);
 Assert.assertEquals(7, hash.size());
 }

 @Test
 public void testNoEntryKeyAndValue() {
 hash = new TFloatDoubleHash(10, 0.75f, 0.1f, 1.0d);
 Assert.assertEquals(10, hash.capacity());
 Assert.assertEquals(0.1f, hash.getNoEntryKey(), 0.1f);
 Assert.assertEquals(1.0d, hash.getNoEntryValue(), 1.0d);
 }

 @Test
 public void testSetUp() {
 int capacity = hash.setUp(15);
 Assert.assertEquals(15, capacity);
 }

 @Test
 public void testContains() {
 hash.put(1.0f, 1.0d);
 Assert.assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testForEach() {
 float result = 0.0f;
 hash.put(1.0f, 1.0d);
 hash.put(2.0f, 2.0d);
 hash.put(3.0f, 3.0d);
 HashProcedure procedure = new HashProcedure<Float, Double>() {
 @Override
 public boolean execute(Float key, Double value) {
 result += value.doubleValue();
 return true;
 }
 };
 hash.forEach(procedure);
 Assert.assertEquals(6.0d, result, 0.01d);
 }

 @Test
 public void testRemoveAt() {
 hash.put(1.0f, 1.0d);
 hash.put(2.0f, 2.0d);
 hash.put(3.0f, 3.0d);
 int index = hash.index(2.0f);
 hash.removeAt(index);
 Assert.assertEquals(2, hash.size());
 }

 @Test
 public void testIndex() {
 hash.put(1.0f, 1.0d);
 hash.put(2.0f, 2.0d);
 hash.put(3.0f, 3.0d);
 float key = 2.0f;
 int index = hash.index(key);
 Assert.assertEquals(1, index);
 }

 @Test
 public void testIndexRehashed() {
 hash.put(1.0f, 1.0d);
 hash.put(2.0f, 2.0d);
 hash.put(3.0f, 3.0d);
 int index = hash.indexRehashed(2.0f, 1, HashFunctions.hash(2.0f), (byte) 1);
 Assert.assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 hash.put(1.0f, 1.0d);
 hash.put(2.0f, 2.0d);
 hash.put(3.0f, 3.0d);
 int index = hash.insertKey(4.0f);
 Assert.assertEquals(4, hash.size());
 Assert.assertEquals(4.0f, hash.get(4.0f), 0.0f);
 Assert.assertEquals(1, index);
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 hash.writeExternal(out);
 byte[] byteArray = byteStream.toByteArray();
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayInputStream byteStream = new ByteArrayInputStream(new byte[0]);
 ObjectInput in = new ObjectInputStream(byteStream);
 TFloatDoubleHash hashCopy = new TFloatDoubleHash();
 hashCopy.readExternal(in);
 }
}