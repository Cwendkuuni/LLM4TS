package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hash = new TFloatCharHash(11, 0.5f);
 }

 @Test
 public void testConstructor() {
 TFloatCharHash hash = new TFloatCharHash();
 assertEquals(0, hash.size());
 hash = new TFloatCharHash(11);
 assertEquals(11, hash.capacity());
 assertEquals(0.5f, hash.loadFactor(), 0.01f);
 assertEquals(0, hash.size());
 hash = new TFloatCharHash(11, 0.5f, 0.0f, '\u0000');
 assertEquals(11, hash.capacity());
 assertEquals(0.5f, hash.loadFactor(), 0.01f);
 assertEquals(0.0f, hash.getNoEntryKey());
 assertEquals('\u0000', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 int capacity = hash.setUp(11);
 assertEquals(11, capacity);
 }

 @Test
 public void testContains() {
 hash.insertKey(1.0f);
 assertTrue(hash.contains(1.0f));
 hash.removeAt(0);
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(1.0f, value, 0.001f);
 return true;
 }
 };
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.forEach(procedure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAtNegativeIndex() {
 hash.removeAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBounds() {
 hash.insertKey(1.0f);
 hash.removeAt(0);
 try {
 hash.index(1.0f);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testIndexRehashed() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 hash.insertKey(4.0f);
 hash.insertKey(5.0f);
 hash.insertKey(6.0f);
 hash.insertKey(7.0f);
 hash.insertKey(8.0f);
 hash.insertKey(9.0f);
 int index = hash.index(10.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 float key = 1.0f;
 int index = hash.insertKey(key);
 assertEquals(1, hash.size());
 assertEquals(0, index);
 key = 2.0f;
 index = hash.insertKey(key);
 assertEquals(2, hash.size());
 assertEquals(1, index);
 key = 3.0f;
 index = hash.insertKey(key);
 assertEquals(3, hash.size());
 assertEquals(2, index);
 }

 @Test
 public void testInsertKeyRehash() {
 float key = 1.0f;
 int index = hash.insertKey(key);
 assertEquals(1, hash.size());
 assertEquals(0, index);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 hash.insertKey(4.0f);
 hash.insertKey(5.0f);
 hash.insertKey(6.0f);
 hash.insertKey(7.0f);
 hash.insertKey(8.0f);
 hash.insertKey(9.0f);
 hash.insertKey(10.0f);
 index = hash.insertKey(11.0f);
 assertEquals(11, hash.size());
 assertEquals(10, index);
 }

 @Test
 public void testWriteRead() throws IOException {
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(output);
 hash.writeExternal(out);
 out.close();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(output.toByteArray()));
 TFloatCharHash hash2 = (TFloatCharHash) in.readObject();
 in.close();
 assertEquals(hash.size(), hash2.size());
 for (int i = 0; i < hash.size(); i++) {
 assertEquals(hash.getKey(i), hash2.getKey(i), 0.001f);
 }
 }

}