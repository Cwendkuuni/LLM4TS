import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 // Test constructor with default arguments
 @Test
 public void testDefaultConstructor() {
 TFloatCharHash hash = new TFloatCharHash();
 assertEquals(0, hash.size());
 }

 // Test constructor with initial capacity and load factor
 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
 assertEquals(10, hash.capacity());
 }

 // Test getNoEntryKey and getNoEntryValue methods
 @Test
 public void testGetNoEntryKeyAndValue() {
 TFloatCharHash hash = new TFloatCharHash();
 assertEquals(0.0f, hash.getNoEntryKey());
 assertEquals('\0', hash.getNoEntryValue());
 }

 // Test contains method with empty hash
 @Test
 public void testContainsWithEmptyHash() {
 TFloatCharHash hash = new TFloatCharHash();
 assertFalse(hash.contains(0.5f));
 }

 // Test contains method with key present
 @Test
 public void testContainsWithKeyPresent() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(0.5f);
 assertTrue(hash.contains(0.5f));
 }

 // Test forEach method with empty hash
 @Test
 public void testForEachWithEmptyHash() {
 TFloatCharHash hash = new TFloatCharHash();
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return true;
 }
 };
 hash.forEach(procedure);
 }

 // Test forEach method with key present
 @Test
 public void testForEachWithKeyPresent() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(0.5f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(0.5f, value);
 return true;
 }
 };
 hash.forEach(procedure);
 }

 // Test removeAt method with empty hash
 @Test
 public void testRemoveAtWithEmptyHash() {
 TFloatCharHash hash = new TFloatCharHash();
 int index = hash.removeAt(0);
 assertEquals(-1, index);
 }

 // Test removeAt method with key not present
 @Test
 public void testRemoveAtWithKeyNotPresent() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(0.5f);
 int index = hash.removeAt(1);
 assertEquals(-1, index);
 }

 // Test removeAt method with key present
 @Test
 public void testRemoveAtWithKeyPresent() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(0.5f);
 hash.insertKey(1.0f);
 int index = hash.removeAt(0);
 assertEquals(0, index);
 assertEquals(1, hash.size());
 }

 // Test index method with empty hash
 @Test
 public void testIndexWithEmptyHash() {
 TFloatCharHash hash = new TFloatCharHash();
 int index = hash.index(0.5f);
 assertEquals(-1, index);
 }

 // Test index method with key present
 @Test
 public void testIndexWithKeyPresent() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(0.5f);
 int index = hash.index(0.5f);
 assertEquals(0, index);
 }

 // Test index method with key not present and rehash
 @Test
 public void testIndexWithKeyNotPresentAndRehash() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(0.5f);
 hash.insertKey(1.0f);
 int index = hash.index(1.5f);
 assertEquals(-1, index);
 }

 // Test insertKey method with key already present
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithKeyAlreadyPresent() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(0.5f);
 hash.insertKey(0.5f);
 }

 // Test writeExternal and readExternal methods
 @Test
 public void testWriteReadExternalMethods() throws IOException {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(0.5f);
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 hash.writeExternal(objStream);
 objStream.close();
 byte[] bytes = byteStream.toByteArray();
 TFloatCharHash hashFromBytes = new TFloatCharHash();
 ObjectInputStream objInput = new ObjectInputStream(new ByteArrayInputStream(bytes));
 hashFromBytes.readExternal(objInput);
 objInput.close();
 assertTrue(hash.contains(0.5f));
 assertTrue(hashFromBytes.contains(0.5f));
 }
}