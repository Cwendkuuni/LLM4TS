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
 public void testEmptyHash() {
 assertTrue(hash.isEmpty());
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testAddAndGet() {
 hash.put(1.0f, 'A');
 assertEquals(1.0f, hash.keyAt(0), 0.0f);
 assertEquals('A', hash.valueAt(0));
 }

 @Test
 public void testAddAndGetWithLoadFactor() {
 hash = new TFloatCharHash(5, 0.75f);
 for (int i = 0; i < 6; i++) {
 hash.put(i, (char) i);
 }
 assertEquals(5.5f, hash.size(), 0.0f);
 assertEquals(0.0f, hash.getNoEntryKey());
 assertEquals('\0', hash.getNoEntryValue());
 assertTrue(hash.contains(1.0f));
 assertEquals(1, hash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 hash = new TFloatCharHash(5, -0.75f);
 }

 @Test
 public void testContains() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertFalse(hash.contains(3.0f));
 }

 @Test
 public void testRemove() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.remove(1.0f);
 assertFalse(hash.containsKey(1.0f));
 assertTrue(hash.containsValue('A'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 hash = new TFloatCharHash(-5, 0.75f);
 }

 @Test
 public void testForEachProcedure() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 if (key == 1.0f) {
 fail("Unexpected key in the procedure.");
 }
 return true;
 }
 };
 hash.forEach(procedure);
 }

 @Test
 public void testIndex() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 assertEquals(1, hash.index(1.0f));
 assertEquals(-1, hash.index(3.0f));
 }

 @Test
 public void testInsertKey() {
 hash = new TFloatCharHash(5, 0.75f);
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 assertEquals(3, hash.size());
 hash.insertKey(4.0f);
 assertEquals(4, hash.size());
 assertEquals(4.0f, hash.keyAt(3), 0.0f);
 assertEquals('C', hash.valueAt(3));
 assertEquals(0.0f, hash.getNoEntryKey());
 assertEquals('\0', hash.getNoEntryValue());
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 hash.writeExternal(objectOutputStream);
 ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
 TFloatCharHash hash2 = (TFloatCharHash) objectInput.readObject();
 assertEquals(hash, hash2);
 }
}