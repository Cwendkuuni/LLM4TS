import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(10);
 }

 @After
 public void tearDown() {
 hash = null;
 }

 @Test
 public void testConstructor() {
 TFloatCharHash h = new TFloatCharHash();
 assertTrue(h instanceof TFloatCharHash);
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TFloatCharHash h = new TFloatCharHash(10);
 assertTrue(h instanceof TFloatCharHash);
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TFloatCharHash h = new TFloatCharHash(10, 0.75f);
 assertTrue(h instanceof TFloatCharHash);
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyNoEntryValue() {
 TFloatCharHash h = new TFloatCharHash(10, 0.75f, 1.0f, '\u0000');
 assertTrue(h instanceof TFloatCharHash);
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatCharHash h = new TFloatCharHash();
 float noEntryKey = h.getNoEntryKey();
 assertEquals(0.0f, noEntryKey, 0.001f);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatCharHash h = new TFloatCharHash();
 char noEntryValue = h.getNoEntryValue();
 assertEquals('\u0000', noEntryValue);
 }

 @Test
 public void testSetUp() {
 TFloatCharHash h = new TFloatCharHash(10);
 int capacity = h.setUp(10);
 assertEquals(10, capacity);
 }

 @Test
 public void testContains() {
 TFloatCharHash h = new TFloatCharHash();
 h.insert(0.5f);
 assertTrue(h.contains(0.5f));
 }

 @Test
 public void testContainsFalse() {
 TFloatCharHash h = new TFloatCharHash();
 assertFalse(h.contains(1.5f));
 }

 @Test
 public void testForEach() {
 TFloatCharHash h = new TFloatCharHash();
 h.insert(0.5f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return value == 0.5f;
 }
 };
 assertTrue(h.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachWithEmptyHash() {
 TFloatCharHash h = new TFloatCharHash();
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return true;
 }
 };
 h.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 TFloatCharHash h = new TFloatCharHash();
 h.insert(0.5f);
 h.insert(1.5f);
 int index = h.indexOf(0.5f);
 h.removeAt(index);
 assertFalse(h.contains(0.5f));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtNegativeIndex() {
 TFloatCharHash h = new TFloatCharHash();
 h.insert(0.5f);
 h.removeAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtIndexOutOfBounds() {
 TFloatCharHash h = new TFloatCharHash();
 h.insert(0.5f);
 int capacity = h.setUp(10);
 h.removeAt(capacity);
 }

 @Test
 public void testIndex() {
 TFloatCharHash h = new TFloatCharHash();
 h.insert(0.5f);
 int index = h.indexOf(0.5f);
 assertEquals(0, index);
 }

 @Test(expected = java.util.NoSuchElementException.class)
 public void testIndexNotFound() {
 TFloatCharHash h = new TFloatCharHash();
 h.insert(0.5f);
 int index = h.indexOf(1.5f);
 }

 @Test
 public void testInsertKey() {
 TFloatCharHash h = new TFloatCharHash();
 int index = h.insertKey(0.5f);
 assertEquals(0, index);
 }

 @Test
 public void testInsertKeyWithExistingValue() {
 TFloatCharHash h = new TFloatCharHash();
 h.insert(0.5f);
 int index = h.insertKey(0.5f);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKeyRehash() {
 TFloatCharHash h = new TFloatCharHash();
 h.insert(0.5f);
 h.insert(1.5f);
 int index = h.insertKey(2.5f);
 assertEquals(-1, index);
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatCharHash h = new TFloatCharHash();
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(output);
 h.writeExternal(out);
 byte[] bytes = output.toByteArray();
 TFloatCharHash h2 = new TFloatCharHash();
 h2.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 float noEntryKey = h2.getNoEntryKey();
 assertEquals(0.0f, noEntryKey, 0.001f);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 TFloatCharHash h = new TFloatCharHash();
 ByteArrayOutputStream output = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(output);
 TFloatCharHash hWritten = new TFloatCharHash(5, 0.75f, 1.0f, '\u0000');
 hWritten.insert(1.0f);
 hWritten.insert(2.0f);
 hWritten.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(output.toByteArray()));
 TFloatCharHash hRead = new TFloatCharHash();
 hRead.readExternal(in);
 assertTrue(hRead.contains(1.0f));
 assertTrue(hRead.contains(2.0f));
 }

}