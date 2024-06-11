import org.junit.Assert;
import org.junit.Test;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Test
 public void testConstructor() {
 hash = new TFloatDoubleHash();
 Assert.assertEquals(0, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 hash = new TFloatDoubleHash(10);
 Assert.assertEquals(0, hash.size());
 Assert.assertEquals(10, hash.capacity());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 hash = new TFloatDoubleHash(10, 0.75f);
 Assert.assertEquals(0, hash.size());
 Assert.assertEquals(10, hash.capacity());
 Assert.assertEquals(0.75f, hash.loadFactor(), 0.01f);
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyAndNoEntryValue() {
 hash = new TFloatDoubleHash(10, 0.75f, 0.5f, 1.0);
 Assert.assertEquals(0, hash.size());
 Assert.assertEquals(10, hash.capacity());
 Assert.assertEquals(0.75f, hash.loadFactor(), 0.01f);
 Assert.assertEquals(0.5f, hash.getNoEntryKey(), 0.01f);
 Assert.assertEquals(1.0, hash.getNoEntryValue(), 0.01);
 }

 @Test
 public void testSetUp() {
 hash = new TFloatDoubleHash(10);
 hash.setUp(10);
 Assert.assertArrayNotEquals(new float[0], hash._set);
 }

 @Test
 public void testContains() {
 hash = new TFloatDoubleHash(5);
 hash.insertKey(1.0f);
 Assert.assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testForEach() {
 hash = new TFloatDoubleHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float floatValue) {
 return Assert.assertEquals(floatValue, hash.get(floatValue), 0.01f) && true;
 }
 };
 Assert.assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 hash = new TFloatDoubleHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 hash.removeAt(1);
 Assert.assertEquals(2, hash.size());
 Assert.assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testIndex() {
 hash = new TFloatDoubleHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 Assert.assertEquals(1, hash.index(1.0f));
 Assert.assertEquals(-1, hash.index(4.0f));
 }

 @Test
 public void testInsertKey() {
 hash = new TFloatDoubleHash(5);
 Assert.assertEquals(-1, hash.insertKey(1.0f));
 Assert.assertEquals(1, hash.insertKey(1.0f));
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException {
 hash = new TFloatDoubleHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutput output = new ObjectOutputStream(outputStream);
 hash.writeExternal(output);
 output.close();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ObjectInput input = new ObjectInputStream(inputStream);
 TFloatDoubleHash newHash = new TFloatDoubleHash();
 newHash.readExternal(input);
 input.close();
 Assert.assertEquals(3, newHash.size());
 Assert.assertEquals(1.0f, newHash.get(1.0f), 0.01f);
 Assert.assertEquals(2.0f, newHash.get(2.0f), 0.01f);
 Assert.assertEquals(3.0f, newHash.get(3.0f), 0.01f);
 }

}