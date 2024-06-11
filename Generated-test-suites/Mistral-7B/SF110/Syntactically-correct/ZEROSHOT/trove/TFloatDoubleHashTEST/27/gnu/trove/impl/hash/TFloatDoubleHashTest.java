import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Before
 public void setUp() {
 hash = new TFloatDoubleHash(10);
 }

 @Test
 public void testEmptyHash() {
 assertFalse(hash.contains(Float.NaN));
 assertFalse(hash.contains(0.0f));
 assertEquals(0, hash.size());
 }

 @Test
 public void testSetUp() {
 int capacity = hash.setUp(10);
 assertTrue(capacity > 0);
 assertNotNull(hash._set);
 }

 @Test
 public void testContains() {
 // test empty hash
 assertFalse(hash.contains(Float.NaN));
 assertFalse(hash.contains(0.0f));

 // test inserting and retrieving values
 hash.put(Float.NaN, Double.NaN);
 hash.put(0.0f, 0.0);
 assertTrue(hash.contains(Float.NaN));
 assertTrue(hash.contains(0.0f));

 // test non-existing value
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testForEach() {
 // test empty hash
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 fail("Should not be called when the hash is empty.");
 return false;
 }
 };
 assertFalse(hash.forEach(procedure));

 // test inserting and retrieving values
 hash.put(Float.NaN, Double.NaN);
 hash.put(0.0f, 0.0);
 TFloatProcedure procedure2 = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 0.0f);
 return true;
 }
 };
 assertTrue(hash.forEach(procedure2));
 }

 @Test
 public void testRemoveAt() {
 // test empty hash
 assertFalse(hash.contains(0.0f));
 assertFalse(hash.removeAt(0));
 assertEquals(0, hash.size());

 // test removing existing value
 hash.put(Float.NaN, Double.NaN);
 hash.put(0.0f, 0.0);
 int index = hash.index(0.0f);
 assertNotNull(index);
 hash.removeAt(index);
 assertFalse(hash.contains(0.0f));
 assertEquals(1, hash.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtOutOfBounds() {
 // test removing out of bounds
 hash.removeAt(-1);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehash() {
 // test inserting value when all slots are occupied
 hash.put(Float.NaN, Double.NaN);
 hash.put(0.0f, 0.0);
 hash.put(1.0f, 1.0);
 try {
 hash.put(2.0f, 2.0);
 fail("Should throw an IllegalStateException when all slots are occupied.");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyAfterFullRehash() {
 // test inserting value after full rehash
 hash.put(Float.NaN, Double.NaN);
 hash.put(0.0f, 0.0);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 try {
 hash.put(4.0f, 4.0);
 fail("Should throw an IllegalStateException when all slots are occupied.");
 } catch (IllegalStateException e) {
 // expected
 }
 }

 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 // test serialization
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(output);
 out.writeObject(hash);
 out.close();

 // read back the object
 ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
 ObjectInputStream in = new ObjectInputStream(input);
 TFloatDoubleHash hash2 = (TFloatDoubleHash) in.readObject();
 in.close();
 assertEquals(hash, hash2);

 // test serialization and deserialization of an empty hash
 ByteArrayOutputStream output2 = new ByteArrayOutputStream();
 ObjectOutputStream out2 = new ObjectOutputStream(output2);
 out2.writeObject(new TFloatDoubleHash(0));
 out2.close();

 // read back the object
 ByteArrayInputStream input2 = new ByteArrayInputStream(output2.toByteArray());
 ObjectInputStream in2 = new ObjectInputStream(input2);
 TFloatDoubleHash hash3 = (TFloatDoubleHash) in2.readObject();
 in2.close();
 assertEquals(new TFloatDoubleHash(0), hash3);
 }

 @After
 public void tearDown() {
 hash = null;
 }
}