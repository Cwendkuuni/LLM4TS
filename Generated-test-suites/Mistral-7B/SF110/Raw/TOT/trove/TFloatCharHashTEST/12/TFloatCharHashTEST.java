import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 // Test constructor with default arguments
 @Test
 public void testDefaultConstructor() {
 TFloatCharHash hashDefault = new TFloatCharHash();
 assertEquals(0, hashDefault.getNoEntryKey());
 assertEquals('\u0000', hashDefault.getNoEntryValue());
 }

 // Test constructor with initial capacity
 @Test
 public void testConstructorWithInitialCapacity() {
 TFloatCharHash hashInitCap = new TFloatCharHash(10);
 assertEquals(10, hashInitCap.size());
 }

 // Test constructor with initial capacity and load factor
 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TFloatCharHash hashInitCapLoadFactor = new TFloatCharHash(15, 0.75f);
 assertEquals(11, hashInitCapLoadFactor.size());
 }

 // Test constructor with initial capacity, load factor, no_entry_key, and no_entry_value
 @Test
 public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyNoEntryValue() {
 TFloatCharHash hashInitCapLoadFactorNoEntryKeyNoEntryValue = new TFloatCharHash(20, 0.8f, 1.0f, 'X');
 assertEquals(1, hashInitCapLoadFactorNoEntryKeyNoEntryValue.getNoEntryKey());
 assertEquals('X', hashInitCapLoadFactorNoEntryKeyNoEntryValue.getNoEntryValue());
 }

 // Test getNoEntryKey method
 @Test
 public void testGetNoEntryKey() {
 assertEquals(0.0f, hash.getNoEntryKey());
 }

 // Test getNoEntryValue method
 @Test
 public void testGetNoEntryValue() {
 assertEquals('\u0000', hash.getNoEntryValue());
 }

 // Test contains method
 @Test
 public void testContains() {
 hash.insert(0.5f);
 assertTrue(hash.contains(0.5f));
 }

 // Test forEach method
 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 if (value == 0.5f) {
 return false;
 }
 assertTrue("Value should not be " + value, value != 0.0f);
 return true;
 }
 };
 hash.insert(0.5f);
 hash.insert(1.2f);
 hash.insert(3.1f);
 hash.forEach(procedure);
 }

 // Test removeAt method
 @Test
 public void testRemoveAt() {
 hash.insert(0.5f);
 hash.insert(1.2f);
 hash.insert(3.1f);
 int index = hash.indexOf(0.5f);
 hash.removeAt(index);
 assertEquals(2, hash.size());
 }

 // Test index method
 @Test
 public void testIndex() {
 hash.insert(0.5f);
 hash.insert(1.2f);
 hash.insert(3.1f);
 int index = hash.indexOf(1.2f);
 assertEquals(1, index);
 }

 // Test indexRehashed method
 @Test
 public void testIndexRehashed() {
 hash.insert(0.5f);
 hash.insert(1.2f);
 hash.insert(3.1f);
 hash.insert(4.3f);
 hash.insert(5.5f);
 int index = hash.indexRehashed(1.2f, 0, HashFunctions.hash(1.2f), 1);
 assertEquals(1, index);
 }

 // Test insertKey method
 @Test
 public void testInsertKey() {
 hash.insert(0.5f);
 hash.insert(1.2f);
 hash.insert(3.1f);
 hash.insert(4.3f);
 hash.insert(5.5f);
 assertEquals(5, hash.size());
 }

 // Test insertKeyRehash method
 @Test
 public void testInsertKeyRehash() {
 hash.insert(0.5f);
 hash.insert(1.2f);
 hash.insert(3.1f);
 hash.insert(4.3f);
 hash.insert(5.5f);
 hash.insert(6.7f);
 assertEquals(6, hash.size());
 }

 // Test writeExternal method
 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 hash.writeExternal(out);
 byte[] bytes = byteStream.toByteArray();
 TFloatCharHash hashRead = (TFloatCharHash) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
 assertEquals(5, hashRead.size());
 }

 // Test readExternal method
 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 TFloatCharHash hashWrite = new TFloatCharHash(5);
 hashWrite.writeExternal(out);
 byte[] bytes = byteStream.toByteArray();
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TFloatCharHash hashRead = (TFloatCharHash) in.readObject();
 assertEquals(5, hashRead.size());
 }
}