import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Test
 public void testEmptyConstructor() {
 hash = new TFloatCharHash();
 assertEquals(0, hash.size());
 }

 @Test
 public void testInitialCapacityConstructor() {
 hash = new TFloatCharHash(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 hash = new TFloatCharHash(10, 0.75f);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndKeysConstructor() {
 hash = new TFloatCharHash(10, 0.75f, 1.0f, 'A');
 assertEquals(10, hash.size());
 }

 @Test
 public void testGetNoEntryKey() {
 hash = new TFloatCharHash();
 assertEquals(0.0f, hash.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 hash = new TFloatCharHash();
 assertEquals('\0', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TFloatCharHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testDoesNotContain() {
 hash = new TFloatCharHash(5);
 assertFalse(hash.contains(2.0f));
 }

 @Test
 public void testForEach() {
 hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 1.0f);
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 hash = new TFloatCharHash(-5);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 hash = new TFloatCharHash(5, -0.75f);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 // Write and read back an empty hash
 TFloatCharHash hash1 = new TFloatCharHash();
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(bos);
 hash1.writeExternal(out);
 out.close();
 byte[] bytes = bos.toByteArray();
 TFloatCharHash hash2 = new TFloatCharHash();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 hash2.readExternal(in);
 in.close();
 assertEquals(hash1, hash2);

 // Write and read back a hash with keys
 hash1 = new TFloatCharHash(3);
 hash1.insertKey(1.0f);
 hash1.insertKey(2.0f);
 hash1.insertKey(3.0f);
 bos = new ByteArrayOutputStream();
 out = new ObjectOutputStream(bos);
 hash1.writeExternal(out);
 out.close();
 bytes = bos.toByteArray();
 hash2 = new TFloatCharHash(3);
 in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 hash2.readExternal(in);
 in.close();
 assertEquals(hash1, hash2);
 }
}