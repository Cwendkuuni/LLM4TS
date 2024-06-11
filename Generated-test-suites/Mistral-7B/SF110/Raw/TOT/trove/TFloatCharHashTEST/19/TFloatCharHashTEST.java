import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5, 0.75f);
 }

 @Test
 public void testContainsEmptyHash() {
 assertFalse(hash.contains(Float.NaN));
 }

 @Test
 public void testContainsAddedKey() {
 hash.insertKey(1.0f);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testContainsDuplicateKey() {
 hash.insertKey(1.0f);
 hash.insertKey(1.0f);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testContainsNaNKey() {
 hash.insertKey(Float.NaN);
 assertTrue(hash.contains(Float.NaN));
 }

 @Test
 public void testContainsNegativeKey() {
 hash.insertKey(-1.0f);
 assertTrue(hash.contains(-1.0f));
 }

 @Test(expected = IllegalStateException.class)
 public void testContainsFullHash() {
 for (float i = 0.0f; i <= 5.0f; i += 0.1f) {
 hash.insertKey(i);
 }
 }

 @Test
 public void testForEachEmptyHash() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 fail("Procedure should not be called with empty hash");
 return false;
 }
 };
 assertFalse(hash.forEach(procedure));
 }

 @Test
 public void testForEachAddedKey() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(1.0f, value, 0.0f);
 return true;
 }
 };
 hash.insertKey(1.0f);
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testForEachMultipleKeys() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, value, 0.0f);
 return true;
 }
 };
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveKeyEmptyHash() {
 int index = hash.removeAt(0);
 assertEquals(-1, index);
 }

 @Test
 public void testRemoveKeyAddedKey() {
 int index = hash.insertKey(1.0f);
 hash.removeAt(index);
 assertEquals(-1, index);
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testRemoveKeyMultipleKeys() {
 int index1 = hash.insertKey(1.0f);
 int index2 = hash.insertKey(2.0f);
 hash.removeAt(index1);
 assertEquals(1.0f, hash.getNoEntryKey(), 0.0f);
 assertFalse(hash.contains(1.0f));
 int index3 = hash.insertKey(3.0f);
 hash.removeAt(index3);
 assertEquals(1.0f, hash.getNoEntryKey(), 0.0f);
 assertFalse(hash.contains(3.0f));
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 oos.writeObject(hash);
 byte[] bytes = baos.toByteArray();
 ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TFloatCharHash hash2 = (TFloatCharHash) ois.readObject();
 assertTrue(hash2.contains(1.0f));
 }

}