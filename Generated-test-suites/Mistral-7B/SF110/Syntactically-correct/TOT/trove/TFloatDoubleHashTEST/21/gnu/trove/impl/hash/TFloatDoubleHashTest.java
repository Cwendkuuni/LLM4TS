import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Before
 public void setUp() {
 hash = new TFloatDoubleHash(5);
 }

 @Test
 public void testConstructors() {
 // Test default constructor
 TFloatDoubleHash defaultHash = new TFloatDoubleHash();
 assertEquals(0, defaultHash.size());

 // Test constructor with initial capacity
 TFloatDoubleHash capacityHash = new TFloatDoubleHash(10);
 assertEquals(10, capacityHash.size());

 // Test constructor with initial capacity and load factor
 TFloatDoubleHash capacityAndLoadFactorHash = new TFloatDoubleHash(15, 0.75f);
 assertEquals(11, capacityAndLoadFactorHash.size());

 // Test constructor with initial capacity, load factor, no_entry_key, and no_entry_value
 TFloatDoubleHash customHash = new TFloatDoubleHash(15, 0.75f, 1.0f, 2.0);
 assertEquals(11, customHash.size());
 }

 @Test
 public void testGetters() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 assertEquals(0.0f, hash.getNoEntryKey());
 assertEquals(0.0, hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 // Test empty hash
 assertFalse(hash.contains(1.0f));

 // Test contains key after insertion
 hash.insertKey(1.0f);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testForEachProcedure() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 // Test empty hash
 assertFalse(hash.forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return false;
 }
 }));

 // Test forEachProcedure with one key
 hash.insertKey(1.0f);
 assertTrue(hash.forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(1.0f, value);
 return true;
 }
 }));

 // Test forEachProcedure with multiple keys
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 hash.insertKey(1.0f);
 assertTrue(hash.forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 if (value == 1.0f) {
 assertEquals(1.0f, value);
 } else if (value == 2.0f) {
 assertEquals(2.0f, value);
 } else if (value == 3.0f) {
 assertEquals(3.0f, value);
 }
 return true;
 }
 }));
 }

 @Test
 public void testRemoveAt() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);

 // Test removeAt with existing index
 int index = hash.index(1.0f);
 hash.removeAt(index);
 assertEquals(2, hash.size());
 assertFalse(hash.contains(1.0f));

 // Test removeAt with non-existing index
 index = hash.size();
 hash.removeAt(index);
 assertEquals(2, hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFullHash() {
 TFloatDoubleHash hash = new TFloatDoubleHash(3);
 for (float i = 0.0f; i < 4.0f; i++) {
 hash.insertKey(i);
 }
 }

 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 hash.writeExternal(objStream);
 objStream.close();
 byte[] byteArray = byteStream.toByteArray();
 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteArray);
 ObjectInputStream objInput = new ObjectInputStream(byteInput);
 TFloatDoubleHash readHash = (TFloatDoubleHash) objInput.readObject();
 objInput.close();
 assertEquals(2, readHash.size());
 assertEquals(1.0f, readHash.getNoEntryKey());
 assertEquals(0.0, readHash.getNoEntryValue());
 }
}