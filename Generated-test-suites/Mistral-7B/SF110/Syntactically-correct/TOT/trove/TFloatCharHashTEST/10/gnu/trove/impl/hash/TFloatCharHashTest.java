package gnu.trove.impl.hash.test;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 @Test
 public void testDefaultConstructor() {
 assertNotNull(hash);
 assertTrue(hash.isEmpty());
 }

 @Test
 public void testCapacityConstructor() {
 assertNotNull(new TFloatCharHash(10));
 }

 @Test
 public void testLoadFactorConstructor() {
 assertNotNull(new TFloatCharHash(5, 0.75f));
 }

 @Test
 public void testLoadFactorAndCapacityConstructor() {
 assertNotNull(new TFloatCharHash(5, 0.75f, 0.0f, '\0'));
 }

 @Test
 public void testGetNoEntryKey() {
 assertEquals(0.0f, hash.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals('\0', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TFloatCharHash(5);
 assertNotNull(hash._set);
 }

 @Test
 public void testContains() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 }

 @Test
 public void testContainsWithEmptyHash() {
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testContainsWithNullKey() {
 assertFalse(hash.contains(null));
 }

 @Test
 public void testContainsWithDuplicateKey() {
 hash.put(1.0f, 'a');
 hash.put(1.0f, 'b');
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testContainsWithKeyNotInHash() {
 assertFalse(hash.contains(4.0f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return key >= 0;
 }
 };
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testForEachWithEmptyHash() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
 hash.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 int index = hash.indexOf(2.0f);
 hash.removeAt(index);
 assertFalse(hash.contains(2.0f));
 }

 @Test
 public void testRemoveAtWithEmptyHash() {
 int index = hash.indexOf(1.0f);
 hash.removeAt(index);
 assertTrue(hash.isEmpty());
 }

 @Test
 public void testIndex() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 int index = hash.indexOf(1.0f);
 assertEquals(0, index);
 }

 @Test
 public void testIndexWithEmptyHash() {
 int index = hash.indexOf(1.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexWithNullKey() {
 int index = hash.indexOf(null);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexRehashed() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 hash.put(4.0f, 'd');
 hash.put(5.0f, 'e');
 hash.put(6.0f, 'f');
 int index = hash.indexOf(2.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 int index = hash.insertKey(4.0f);
 assertEquals(4, hash.size());
 assertEquals(4, index);
 }

 @Test
 public void testInsertKeyWithCapacityFull() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 hash.put(4.0f, 'd');
 hash.put(5.0f, 'e');
 hash.put(6.0f, 'f');
 assertThrows(IllegalStateException.class, () -> hash.insertKey(7.0f));
 }

 @Test
 public void testWriteExternal() {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
 hash.writeExternal(objectOutputStream);
 byte[] bytes = outputStream.toByteArray();
 TFloatCharHash hash2 = new TFloatCharHash();
 hash2.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(hash, hash2);
 }

 @Test
 public void testReadExternal() {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
 TFloatCharHash hash2 = new TFloatCharHash();
 hash2.writeExternal(objectOutputStream);
 byte[] bytes = outputStream.toByteArray();
 hash2.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(hash, hash2);
 }
}