package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import org.junit.Test;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash testHash;

 @Test
 public void testConstructor() {
 testHash = new TFloatCharHash();
 assertEquals(0, testHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 testHash = new TFloatCharHash(10);
 assertEquals(0, testHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 testHash = new TFloatCharHash(10, 0.75f);
 assertEquals(0, testHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryKeyAndNoEntryValue() {
 testHash = new TFloatCharHash(10, 0.75f, 1.0f, 'X');
 assertEquals(0, testHash.size());
 }

 @Test
 public void testGetNoEntryKey() {
 testHash = new TFloatCharHash(10, 0.75f, 1.0f, 'X');
 assertEquals(1.0f, testHash.getNoEntryKey(), 0.001f);
 }

 @Test
 public void testGetNoEntryValue() {
 testHash = new TFloatCharHash(10, 0.75f, 1.0f, 'X');
 assertEquals('X', testHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 testHash = new TFloatCharHash(10, 0.75f);
 assertEquals(10, testHash.setUp(10));
 }

 @Test
 public void testContains() {
 testHash = new TFloatCharHash(10, 0.75f);
 testHash.insertKey(1.0f);
 testHash.insertKey(2.0f);
 assertTrue(testHash.contains(1.0f));
 assertFalse(testHash.contains(2.1f));
 }

 @Test
 public void testForEach() {
 testHash = new TFloatCharHash(10, 0.75f);
 testHash.insertKey(1.0f);
 testHash.insertKey(2.0f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 if (value == 1.0f) {
 return false;
 }
 assertEquals(2.0f, value, 0.001f);
 return true;
 }
 };
 testHash.forEach(procedure);
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtWithEmptyList() {
 testHash = new TFloatCharHash(10, 0.75f);
 testHash.removeAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexWithEmptyList() {
 testHash = new TFloatCharHash(10, 0.75f);
 int index = testHash.index(-1.0f);
 assertEquals(-1, index);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexWithOneElement() {
 testHash = new TFloatCharHash(10, 0.75f);
 testHash.insertKey(1.0f);
 int index = testHash.index(2.0f);
 assertEquals(-1, index);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexRehashedWithOneElement() {
 testHash = new TFloatCharHash(10, 0.75f);
 testHash.insertKey(1.0f);
 int index = testHash.indexRehashed(1.2f, 0, 1, (byte) 0);
 assertEquals(-1, index);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInsertKeyWithFullList() {
 testHash = new TFloatCharHash(10, 0.75f);
 for (float i = 0; i < 11; i++) {
 testHash.insertKey(i);
 }
 assertThrows(IllegalStateException.class, () -> testHash.insertKey(12.0f));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithFullList() {
 testHash = new TFloatCharHash(10, 0.75f);
 for (float i = 0; i < 11; i++) {
 testHash.insertKey(i);
 }
 assertThrows(IllegalStateException.class, () -> testHash.insertKeyRehash(12.0f, -1, 1, (byte) 0));
 }

 @Test
 public void testWriteExternal() throws IOException {
 testHash = new TFloatCharHash(10, 0.75f);
 testHash.insertKey(1.0f);
 testHash.insertKey(2.0f);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
 testHash.writeExternal(objectOutputStream);
 byte[] bytes = outputStream.toByteArray();
 TFloatCharHash deserializedHash = new TFloatCharHash();
 deserializedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(2, deserializedHash.size());
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
 TFloatCharHash testHash = new TFloatCharHash();
 testHash.writeExternal(objectOutputStream);
 objectOutputStream.close();
 byte[] bytes = outputStream.toByteArray();
 TFloatCharHash deserializedHash = new TFloatCharHash();
 deserializedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(0, deserializedHash.size());
 }
}