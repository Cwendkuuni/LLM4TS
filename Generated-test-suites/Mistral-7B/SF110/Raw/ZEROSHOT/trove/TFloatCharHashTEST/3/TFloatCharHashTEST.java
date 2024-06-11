import org.junit.Assert;
import org.junit.Test;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 @Test
 public void testConstructor() {
 TFloatCharHash hash = new TFloatCharHash();
 Assert.assertNotNull(hash);
 }

 @Test
 public void testInitialCapacityConstructor() {
 TFloatCharHash hash = new TFloatCharHash(10);
 Assert.assertEquals(10, hash.size());
 }

 @Test
 public void testLoadFactorConstructor() {
 TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
 Assert.assertEquals(10, hash.capacity());
 Assert.assertEquals(7, hash.size());
 }

 @Test
 public void testAllArgumentsConstructor() {
 TFloatCharHash hash = new TFloatCharHash(10, 0.75f, Float.NaN, 'X');
 Assert.assertEquals(10, hash.capacity());
 Assert.assertEquals(0, hash.size());
 Assert.assertEquals(Float.NaN, hash.getNoEntryKey());
 Assert.assertEquals('X', hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatCharHash hash = new TFloatCharHash();
 Assert.assertEquals(0.0f, hash.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatCharHash hash = new TFloatCharHash();
 Assert.assertEquals('\u0000', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TFloatCharHash hash = new TFloatCharHash(10);
 Assert.assertEquals(10, hash.capacity());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TFloatCharHash hash = new TFloatCharHash(-5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 TFloatCharHash hash = new TFloatCharHash(5, -0.5f);
 }

 @Test
 public void testContains() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insert(2.5f);
 Assert.assertTrue(hash.contains(2.5f));
 }

 @Test
 public void testContainsKeyThatDoesntExist() {
 TFloatCharHash hash = new TFloatCharHash();
 Assert.assertFalse(hash.contains(3.14f));
 }

 @Test
 public void testForEach() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insert(1.0f);
 hash.insert(2.5f);
 hash.insert(3.14f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float floatValue) {
 Assert.assertTrue(hash.contains(floatValue));
 return true;
 }
 };
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insert(1.0f);
 hash.insert(2.5f);
 hash.insert(3.14f);
 hash.removeAt(1);
 Assert.assertEquals(2, hash.size());
 Assert.assertFalse(hash.contains(2.5f));
 }

 @Test
 public void testIndex() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insert(1.0f);
 hash.insert(2.5f);
 hash.insert(3.14f);
 Assert.assertEquals(1, hash.index(1.0f));
 Assert.assertEquals(-1, hash.index(4.0f));
 }

 @Test
 public void testIndexRehashed() {
 TFloatCharHash hash = new TFloatCharHash(3);
 hash.insert(1.0f);
 hash.insert(2.5f);
 hash.insert(3.14f);
 Assert.assertEquals(1, hash.indexRehashed(1.0f, 1, HashFunctions.hash(1.0f), (byte) 1));
 Assert.assertEquals(-1, hash.indexRehashed(4.0f, 1, HashFunctions.hash(1.0f), (byte) 1));
 }

 @Test
 public void testInsertKey() {
 TFloatCharHash hash = new TFloatCharHash();
 Assert.assertEquals(0, hash.size());
 Assert.assertEquals(-1, hash.insertKey(2.5f));
 Assert.assertEquals(1, hash.size());
 Assert.assertTrue(hash.contains(2.5f));
 }

 @Test
 public void testInsertKeyRehash() {
 TFloatCharHash hash = new TFloatCharHash(3);
 Assert.assertEquals(-1, hash.insertKey(1.0f));
 Assert.assertEquals(1, hash.size());
 Assert.assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testInsertKeyWithExistingKey() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insert(1.0f);
 Assert.assertEquals(1, hash.size());
 hash.insert(1.0f);
 Assert.assertEquals(1, hash.size());
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insert(1.0f);
 hash.insert(2.5f);
 hash.insert(3.14f);
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 hash.writeExternal(objectOutput);
 byte[] bytes = byteArrayOutputStream.toByteArray();
 TFloatCharHash deserializedHash = new TFloatCharHash();
 deserializedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 Assert.assertEquals(3, deserializedHash.size());
 Assert.assertTrue(deserializedHash.contains(1.0f));
 Assert.assertTrue(deserializedHash.contains(2.5f));
 Assert.assertTrue(deserializedHash.contains(3.14f));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 TFloatCharHash hash = new TFloatCharHash();
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 hash.writeExternal(objectOutput);
 byte[] bytes = byteArrayOutputStream.toByteArray();
 TFloatCharHash deserializedHash = new TFloatCharHash();
 deserializedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 Assert.assertEquals(0, hash.size());
 Assert.assertEquals(0, deserializedHash.size());
 hash.insert(1.0f);
 hash.insert(2.5f);
 hash.insert(3.14f);
 Assert.assertEquals(3, hash.size());
 Assert.assertEquals(3, deserializedHash.size());
 Assert.assertTrue(hash.contains(1.0f));
 Assert.assertTrue(hash.contains(2.5f));
 Assert.assertTrue(hash.contains(3.14f));
 Assert.assertTrue(deserializedHash.contains(1.0f));
 Assert.assertTrue(deserializedHash.contains(2.5f));
 Assert.assertTrue(deserializedHash.contains(3.14f));
 }
}