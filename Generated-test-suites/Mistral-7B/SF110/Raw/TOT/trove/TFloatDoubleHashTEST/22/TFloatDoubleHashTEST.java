import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hashTable;

 @Before
 public void setUp() {
 hashTable = new TFloatDoubleHash(5, 0.75f);
 }

 @Test
 public void testContains() {
 // Test empty table
 assertFalse(hashTable.contains(1.0f));

 // Test adding elements and checking for existence
 hashTable.insertKey(1.0f, 1.0);
 hashTable.insertKey(2.0f, 2.0);
 hashTable.insertKey(3.0f, 3.0);
 assertTrue(hashTable.contains(1.0f));
 assertTrue(hashTable.contains(2.0f));
 assertTrue(hashTable.contains(3.0f));

 // Test non-existent key
 assertFalse(hashTable.contains(4.0f));
 }

 @Test
 public void testForEach() {
 // Test empty table
 assertFalse(hashTable.forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return false;
 }
 }));

 // Test adding elements and processing them
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, value, 0.0f);
 return true;
 }
 };
 hashTable.insertKey(1.0f, 1.0);
 hashTable.insertKey(2.0f, 2.0);
 hashTable.insertKey(3.0f, 3.0);
 assertTrue(hashTable.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
 hashTable.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 // Test removing elements
 hashTable.insertKey(1.0f, 1.0);
 hashTable.insertKey(2.0f, 2.0);
 hashTable.insertKey(3.0f, 3.0);
 int index = hashTable.indexOf(2.0f);
 hashTable.removeAt(index);
 assertFalse(hashTable.contains(2.0f));
 assertNotNull(hashTable._set[index]);
 assertEquals(0.0f, hashTable._set[index], 0.0f);
 }

 @Test
 public void testIndex() {
 // Test empty table
 int index = hashTable.index(1.0f);
 assertEquals(-1, index);

 // Test adding elements and checking indices
 hashTable.insertKey(1.0f, 1.0);
 hashTable.insertKey(2.0f, 2.0);
 hashTable.insertKey(3.0f, 3.0);
 index = hashTable.index(1.0f);
 assertEquals(0, index);
 index = hashTable.index(2.0f);
 assertEquals(1, index);
 index = hashTable.index(3.0f);
 assertEquals(2, index);
 index = hashTable.index(4.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 // Test inserting elements into empty table
 hashTable = new TFloatDoubleHash(5, 0.75f);
 index = hashTable.insertKey(1.0f);
 assertEquals(0, index);
 assertEquals(1.0f, hashTable.getNoEntryKey(), 0.0f);
 assertEquals(0.0, hashTable.getNoEntryValue(), 0.0);

 // Test inserting elements into full table and rehashing
 hashTable = new TFloatDoubleHash(5, 0.75f);
 for (float i = 0.0f; i < hashTable.getInitialCapacity() * 0.75f; i += 0.1f) {
 hashTable.insertKey(i);
 }
 index = hashTable.insertKey(1.0f);
 assertNotEquals(-1, index);
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 hashTable.writeExternal(objStream);
 objStream.close();
 byte[] byteArray = byteStream.toByteArray();
 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteArray);
 ObjectInput objInput = new ObjectInput(byteInput);
 TFloatDoubleHash newHashTable = (TFloatDoubleHash) objInput.readObject();
 objInput.close();
 // Test if new hash table contains the same elements as the original one
 for (float key : hashTable._set) {
 assertTrue(newHashTable.contains(key));
 }
 }

 @Test(expected = IOException.class)
 public void testWriteExternalWithNullOutputStream() throws IOException, ClassNotFoundException {
 TFloatDoubleHash hashTable = new TFloatDoubleHash(5, 0.75f);
 ObjectOutputStream outputStream = null;
 hashTable.writeExternal(outputStream);
 }
}