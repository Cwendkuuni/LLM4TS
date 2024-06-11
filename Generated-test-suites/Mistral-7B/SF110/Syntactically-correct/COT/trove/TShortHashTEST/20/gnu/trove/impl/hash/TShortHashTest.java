import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 // Test constructor with default initialCapacity and loadFactor
 @Test
 public void testDefaultConstructor() {
 final TShortHash hash = new TShortHash();
 assertNotNull(hash);
 assertEquals(Constants.DEFAULT_INTIAL_CAPACITY, hash.capacity());
 assertEquals(Constants.DEFAULT_LOAD_FACTOR, hash.loadFactor());
 }

 // Test constructor with given initialCapacity and loadFactor
 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 final TShortHash hash = new TShortHash(10, 0.75f);
 assertNotNull(hash);
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01);
 }

 // Test constructor with given initialCapacity, loadFactor and no_entry_value
 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 final short noEntryValue = 1;
 final TShortHash hash = new TShortHash(10, 0.75f, noEntryValue);
 assertNotNull(hash);
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01);
 assertSame(noEntryValue, hash.getNoEntryValue());
 }

 // Test getNoEntryValue method
 @Test
 public void testGetNoEntryValue() {
 final TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 // Test setUp method
 @Test
 public void testSetUp() {
 final TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.capacity());
 assertArrayEquals(new short[5], hash._set);
 }

 // Test contains method
 @Test
 public void testContains() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertFalse(hash.contains((short) 0));
 }

 // Test forEach method
 @Test
 public void testForEach() {
 final TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short short1) {
 assertEquals((short) 1, short1);
 return true;
 }
 };
 hash.put((short) 1);
 hash.put((short) 2);
 hash.forEach(procedure);
 }

 // Test removeAt method
 @Test
 public void testRemoveAt() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.removeAt(0);
 assertEquals(1, hash.size());
 assertFalse(hash.contains((short) 1));
 }

 // Test index method with present key
 @Test
 public void testIndexWithPresentKey() {
 hash.put((short) 1);
 assertEquals(0, hash.index((short) 1));
 }

 // Test index method with absent key
 @Test
 public void testIndexWithAbsentKey() {
 assertEquals(-1, hash.index((short) 1));
 }

 // Test insertKey method with non-full hash
 @Test
 public void testInsertKeyWithNonFullHash() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 assertEquals(4, hash.size());
 assertEquals(1, hash.index((short) 1));
 assertEquals(2, hash.index((short) 2));
 assertEquals(3, hash.index((short) 3));
 assertEquals(4, hash.index((short) 4));
 }

 // Test insertKey method with full hash
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHash() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6); // should throw an IllegalStateException
 }

 // Test insertKeyRehash method with non-full hash
 @Test
 public void testInsertKeyRehashWithNonFullHash() {
 final int hash = HashFunctions.hash((short) 7);
 final int index = hash % hash._states.length;
 final byte state = hash._states[index];
 hash.consumeFreeSlot = false;
 if (state == 0) {
 hash.insertKeyAt(index, (short) 7);
 assertEquals(1, hash.size());
 assertEquals(7, hash.index((short) 7));
 }
 }

 // Test insertKeyRehash method with full hash
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithFullHash() {
 final int hash = HashFunctions.hash((short) 8);
 final int index = hash % hash._states.length;
 final byte state = hash._states[index];
 hash.consumeFreeSlot = false;
 if (state == 0) {
 hash.insertKeyAt(index, (short) 8);
 hash.put((short) 9); // should throw an IllegalStateException
 }
 }
}