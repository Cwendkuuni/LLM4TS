import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.TShortProcedure;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.impl.hash.TShortHash_Impl;

public class TShortHashTest {

 private TShortHash<Short> hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hash = new TShortHash<Short>(5, 0.75f);
 }

 @Test
 public void testDefaultConstructor() {
 TShortHash<Short> hash = new TShortHash<Short>();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TShortHash<Short> hash = new TShortHash<Short>(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TShortHash<Short> hash = new TShortHash<Short>(15, 0.8f);
 assertEquals(15, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash<Short> hash = new TShortHash<Short>(20, 0.9f, (short) 10);
 assertEquals(10, hash.getNoEntryValue());
 assertEquals(20, hash.size());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash<Short> hash = new TShortHash<Short>();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TShortHash<Short> hash = new TShortHash<Short>(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 TShortHash<Short> hash = new TShortHash<Short>();
 hash.put((short) 1);
 assertTrue(hash.contains((short) 1));
 hash.clear();
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testForEach() {
 TShortHash<Short> hash = new TShortHash<Short>();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 final short[] expected = {1, 2, 3};
 final TShortProcedure procedure = new TShortProcedure() {
 @SuppressWarnings("unchecked")
 @Override
 public boolean execute(final short short1) {
 assertEquals(expected[counter++], short1);
 return true;
 }
 };
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 TShortHash<Short> hash = new TShortHash<Short>();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.removeAt(1);
 assertEquals(2, hash.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtOutOfBounds() {
 TShortHash<Short> hash = new TShortHash<Short>();
 hash.removeAt(-1);
 }

 @Test
 public void testIndex() {
 TShortHash<Short> hash = new TShortHash<Short>();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(1, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 4));
 }

 @Test
 public void testInsertKey() {
 TShortHash<Short> hash = new TShortHash<Short>();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 assertEquals(4, hash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyNullKey() {
 TShortHash<Short> hash = new TShortHash<Short>();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put(null);
 }

 @Test
 public void testInsertKeyDuplicate() {
 TShortHash<Short> hash = new TShortHash<Short>();
 hash.put((short) 1);
 hash.put((short) 1);
 assertEquals(1, hash.size());
 }

 @Test
 public void testHashFunctions() {
 Short hash1 = Short.valueOf((short) 1);
 Short hash2 = Short.valueOf((short) 1);
 int hash = HashFunctions.hash(hash1);
 int hash2 = HashFunctions.hash(hash2);
 assertEquals(hash, hash2);
 }
}