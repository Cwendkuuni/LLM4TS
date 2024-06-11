import org.junit.After;
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

 @After
 public void tearDown() {
 hash = null;
 }

 @Test
 public void testConstructor() {
 TFloatCharHash hash = new TFloatCharHash();
 assertNotNull(hash);

 TFloatCharHash hash2 = new TFloatCharHash(10);
 assertNotNull(hash2);

 TFloatCharHash hash3 = new TFloatCharHash(10, 0.5f);
 assertNotNull(hash3);

 TFloatCharHash hash4 = new TFloatCharHash(10, 0.5f, 0.0f, '\0');
 assertNotNull(hash4);
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatCharHash hash = new TFloatCharHash();
 float noEntryKey = hash.getNoEntryKey();
 assertEquals(0.0f, noEntryKey);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatCharHash hash = new TFloatCharHash();
 char noEntryValue = hash.getNoEntryValue();
 assertEquals('\0', noEntryValue);
 }

 @Test
 public void testSetUp() {
 TFloatCharHash hash = new TFloatCharHash(5);
 int capacity = hash.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f);
 assertTrue(hash.contains(1.0f));

 hash.insert(2.0f);
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));

 hash.remove(1.0f);
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testForEach() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f);
 hash.insert(2.0f);
 hash.insert(3.0f);

 final int count = 3;
 int counter = 0;
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 counter++;
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 assertEquals(count, counter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyWithNegativeCapacity() {
 TFloatCharHash hash = new TFloatCharHash(-5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyWithNegativeLoadFactor() {
 TFloatCharHash hash = new TFloatCharHash(5, -0.5f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyWithNegativeNoEntryKey() {
 TFloatCharHash hash = new TFloatCharHash(5, 0.5f, -0.0f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyWithNegativeNoEntryValue() {
 TFloatCharHash hash = new TFloatCharHash(5, 0.5f, 0.0f, (char) -1);
 }

 @Test
 public void testIndex() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f);
 hash.insert(2.0f);
 hash.insert(3.0f);
 hash.insert(3.0f);
 hash.insert(1.0f);

 int index1 = hash.indexOf(1.0f);
 assertEquals(-1, index1);

 int index2 = hash.indexOf(2.0f);
 assertEquals(1, index2);

 int index3 = hash.indexOf(3.0f);
 assertEquals(2, index3);
 }

 @Test
 public void testRemoveAt() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f);
 hash.insert(2.0f);
 hash.insert(3.0f);
 hash.insert(3.0f);
 hash.insert(1.0f);

 hash.removeAt(2);
 assertEquals(4, hash.size());
 float key = hash.get(1);
 assertEquals(1.0f, key, 0.0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtOutOfBounds() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f);
 hash.removeAt(5);
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f);
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(output);
 hash.writeExternal(out);
 byte[] bytes = output.toByteArray();
 TFloatCharHash hash2 = new TFloatCharHash();
 hash2.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(1.0f, hash2.get(0), 0.0);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 TFloatCharHash hash = new TFloatCharHash();
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(output);
 hash.writeExternal(out);
 byte[] bytes = output.toByteArray();
 TFloatCharHash hash2 = new TFloatCharHash();
 hash2.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(hash, hash2);
 }
}