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
 public void testConstructor() {
 // Test default constructor
 TFloatDoubleHash defaultHash = new TFloatDoubleHash();
 assertEquals(0, defaultHash.getNoEntryKey());
 assertEquals(0.0, defaultHash.getNoEntryValue(), 0.0);
 // Test constructor with initialCapacity
 TFloatDoubleHash capacityHash = new TFloatDoubleHash(10);
 assertEquals(10, capacityHash.size());
 // Test constructor with initialCapacity and loadFactor
 TFloatDoubleHash loadFactorHash = new TFloatDoubleHash(15, 0.75f);
 assertEquals(15, loadFactorHash.size(), 1);
 // Test constructor with initialCapacity, loadFactor, no_entry_key, and no_entry_value
 TFloatDoubleHash paramsHash = new TFloatDoubleHash(20, 0.85f, 1.0f, 2.0);
 assertEquals(1.0f, paramsHash.getNoEntryKey());
 assertEquals(2.0, paramsHash.getNoEntryValue(), 0.0);
 }

 @Test
 public void testGetNoEntryKey() {
 // Test default constructor
 TFloatDoubleHash defaultHash = new TFloatDoubleHash();
 assertEquals(0.0f, defaultHash.getNoEntryKey());
 // Test constructor with no_entry_key
 TFloatDoubleHash keyHash = new TFloatDoubleHash(3, 0.6f, 1.1f, 3.3);
 assertEquals(1.1f, keyHash.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 // Test default constructor
 TFloatDoubleHash defaultHash = new TFloatDoubleHash();
 assertEquals(0.0, defaultHash.getNoEntryValue(), 0.0);
 // Test constructor with no_entry_value
 TFloatDoubleHash valueHash = new TFloatDoubleHash(4, 0.75f, 1.2f, 4.5);
 assertEquals(4.5, valueHash.getNoEntryValue(), 0.0);
 }

 @Test
 public void testSetUp() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 // Test empty hash
 TFloatDoubleHash emptyHash = new TFloatDoubleHash();
 assertFalse(emptyHash.contains(1.0f));
 // Test contains with key in hash
 TFloatDoubleHash hash = new TFloatDoubleHash();
 hash.insert(1.0f, 1.0);
 assertTrue(hash.contains(1.0f));
 // Test contains with key not in hash
 TFloatDoubleHash hash2 = new TFloatDoubleHash();
 assertFalse(hash2.contains(1.5f));
 }

 @Test
 public void testForEach() {
 // Test empty hash
 TFloatDoubleHash emptyHash = new TFloatDoubleHash();
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return false;
 }
 };
 assertFalse(emptyHash.forEach(procedure));
 // Test forEach with hash containing keys
 TFloatDoubleHash hash = new TFloatDoubleHash();
 hash.insert(1.0f, 1.0);
 hash.insert(2.0f, 2.0);
 TFloatProcedure procedure2 = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 1.0f);
 return true;
 }
 };
 assertTrue(hash.forEach(procedure2));
 }

 @Test
 public void testRemoveAt() {
 // Test removeAt with empty hash
 TFloatDoubleHash emptyHash = new TFloatDoubleHash();
 assertFalse(emptyHash.removeAt(0) >= 0);
 // Test removeAt with key in hash
 TFloatDoubleHash hash = new TFloatDoubleHash();
 hash.insert(1.0f, 1);
 hash.insert(2.0f, 2);
 int index = hash.indexOf(1.0f);
 assertEquals(1, hash.removeAt(index));
 // Test removeAt with key not in hash
 TFloatDoubleHash hash2 = new TFloatDoubleHash();
 assertFalse(hash2.removeAt(0) >= 0);
 }

 @Test
 public void testIndex() {
 // Test index with empty hash
 TFloatDoubleHash emptyHash = new TFloatDoubleHash();
 assertEquals(-1, emptyHash.index(1.0f));
 // Test index with key in hash
 TFloatDoubleHash hash = new TFloatDoubleHash();
 hash.insert(1.0f, 1);
 assertEquals(0, hash.index(1.0f));
 // Test index with key not in hash
 TFloatDoubleHash hash2 = new TFloatDoubleHash();
 assertEquals(-1, hash2.index(1.5f));
 }

 @Test
 public void testInsertKey() {
 // Test insertKey with empty hash
 TFloatDoubleHash emptyHash = new TFloatDoubleHash();
 assertEquals(-1, emptyHash.insertKey(1.0f));
 // Test insertKey with key already in hash
 TFloatDoubleHash hash = new TFloatDoubleHash();
 hash.insert(1.0f, 1);
 assertEquals(-1, hash.insertKey(1.0f));
 // Test insertKey with key not in hash
 TFloatDoubleHash hash2 = new TFloatDoubleHash();
 assertEquals(0, hash2.insertKey(2.0f));
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.insert(1.0f, 1);
 hash.insert(2.0f, 2);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 hash.writeExternal(out);
 TFloatDoubleHash readHash = new TFloatDoubleHash();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 readHash.readExternal(in);
 assertEquals(2, readHash.size());
 assertEquals(1.0f, readHash.get(0), 0.0);
 assertEquals(2.0f, readHash.get(1), 0.0);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 TFloatDoubleHash hash = new TFloatDoubleHash();
 out.writeObject(hash);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TFloatDoubleHash readHash = (TFloatDoubleHash) in.readObject();
 assertEquals(0, readHash.size());
 hash.insert(1.0f, 1);
 hash.insert(2.0f, 2);
 assertEquals(2, readHash.size());
 assertEquals(1.0f, readHash.get(0), 0.0);
 assertEquals(2.0f, readHash.get(1), 0.0);
 }

}