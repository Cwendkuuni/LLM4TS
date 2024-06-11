import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 @Test
 public void testConstructor() {
 TFloatCharHash hash = new TFloatCharHash();
 assertTrue(hash instanceof TFloatCharHash);
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TFloatCharHash hash = new TFloatCharHash(10);
 assertTrue(hash instanceof TFloatCharHash);
 assertEquals(10, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TFloatCharHash hash = new TFloatCharHash(10, 0.75f);
 assertTrue(hash instanceof TFloatCharHash);
 assertEquals(10, hash.capacity());
 assertEquals(7, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryKeyNoEntryValue() {
 TFloatCharHash hash = new TFloatCharHash(10, 0.75f, 1.0f, 'X');;
 assertTrue(hash instanceof TFloatCharHash);
 assertEquals(10, hash.capacity());
 assertEquals(7, hash.size());
 assertEquals(1.0f, hash.getNoEntryKey());
 assertEquals('X', hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatCharHash hash = new TFloatCharHash();
 assertEquals(0.0f, hash.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatCharHash hash = new TFloatCharHash();
 assertEquals('\u0000', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TFloatCharHash hash = new TFloatCharHash(5);
 int capacity = hash.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(1.0f);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testContainsNull() {
 TFloatCharHash hash = new TFloatCharHash();
 assertFalse(hash.contains(null));
 }

 @Test
 public void testContainsNonExistentKey() {
 TFloatCharHash hash = new TFloatCharHash();
 assertFalse(hash.contains(1.1f));
 }

 @Test
 public void testForEach() {
 TFloatCharHash hash = new TFloatCharHash();
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

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachEmptyCollection() {
 TFloatCharHash hash = new TFloatCharHash();
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return false;
 }
 };
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.removeAt(0);
 assertFalse(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtIndexOutOfBounds() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(1.0f);
 hash.removeAt(-1);
 }

 @Test
 public void testIndex() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 assertEquals(1, hash.index(1.0f));
 assertEquals(-1, hash.index(3.0f));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexNegativeIndex() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 assertEquals(-1, hash.index(-1));
 }

 @Test
 public void testIndexRehashed() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 assertEquals(-1, hash.indexRehashed(1.1f, 1, 1, (byte)1));
 }

 @Test
 public void testInsertKey() {
 TFloatCharHash hash = new TFloatCharHash();
 int index = hash.insertKey(1.0f);
 assertEquals(1, index);
 assertEquals(1.0f, hash.get(index));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyNullKey() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyKeyAlreadyPresent() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(1.0f);
 hash.insertKey(1.0f);
 }

 @Test
 public void testInsertKeyRehash() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 int index = hash.insertKeyRehash(4.0f, 1, 1, (byte)1);
 assertEquals(2, index);
 assertEquals(4.0f, hash.get(index));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyNoFreeOrRemovedSlotsAvailable() {
 TFloatCharHash hash = new TFloatCharHash();
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 hash.insertKey(4.0f);
 }

 @Test
 public void testWriteExternal() throws IOException {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 hash.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 TFloatCharHash hashFromSerialized = new TFloatCharHash();
 hashFromSerialized.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(2, hashFromSerialized.size());
 assertEquals(1.0f, hashFromSerialized.get(0), 0.0f);
 assertEquals(2.0f, hashFromSerialized.get(1), 0.0f);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 TFloatCharHash hash = new TFloatCharHash();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 out.writeByte(0);
 out.writeInt(10);
 out.writeFloat(0.0f);
 out.writeChar('\u0000');
 hash.readExternal(new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())));
 assertEquals(10, hash.capacity());
 assertEquals(0, hash.size());
 assertEquals(0.0f, hash.getNoEntryKey());
 assertEquals('\u0000', hash.getNoEntryValue());
 }

}