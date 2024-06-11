import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;
 private short[] testData = {1, 2, 3, 4, 5};

 @Before
 public void setUp() {
 hash = new TShortHash(testData.length);
 for (short value : testData) {
 hash.put(value);
 }
 }

 @Test
 public void testDefaultConstructor() {
 TShortHash emptyHash = new TShortHash();
 assertEquals(0, emptyHash.size());
 short[] emptySet = emptyHash._set;
 assertNull(emptyHash.getNoEntryValue());
 assertArrayEquals(new short[0], emptySet);
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TShortHash hashWithInitialCapacity = new TShortHash(5);
 assertEquals(0, hashWithInitialCapacity.size());
 short[] set = hashWithInitialCapacity._set;
 assertNull(hashWithInitialCapacity.getNoEntryValue());
 assertArrayEquals(new short[5], set);
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TShortHash hashWithInitialCapacityAndLoadFactor = new TShortHash(5, 0.75f);
 assertEquals(0, hashWithInitialCapacityAndLoadFactor.size());
 short[] set = hashWithInitialCapacityAndLoadFactor._set;
 assertNull(hashWithInitialCapacityAndLoadFactor.getNoEntryValue());
 assertArrayEquals(new short[4], set);
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash hashWithInitialCapacityLoadFactorAndNoEntryValue = new TShortHash(5, 0.75f, (short) 10);
 assertEquals(0, hashWithInitialCapacityLoadFactorAndNoEntryValue.size());
 short[] set = hashWithInitialCapacityLoadFactorAndNoEntryValue._set;
 assertEquals((short) 10, hashWithInitialCapacityLoadFactorAndNoEntryValue.getNoEntryValue());
 assertArrayEquals(new short[4], set);
 Arrays.fill(set, (short) 10);
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 short[] expectedSet = new short[hash.capacity()];
 System.arraycopy(testData, 0, expectedSet, 0, testData.length);
 assertArrayEquals(expectedSet, hash._set);
 }

 @Test
 public void testContains() {
 for (short value : testData) {
 assertTrue(hash.contains(value));
 }
 }

 @Test
 public void testForEach() {
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals(value, hash.get(value));
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtWithNegativeIndex() {
 hash.removeAt(-1);
 }

 @Test
 public void testRemoveAt() {
 short valueToRemove = testData[3];
 int index = hash.indexOf(valueToRemove);
 hash.removeAt(index);
 assertEquals(4, hash.size());
 short[] set = hash._set;
 assertNotEquals(valueToRemove, set[index]);
 }

 @Test
 public void testIndex() {
 for (short value : testData) {
 int expectedIndex = hash.indexOf(value);
 int actualIndex = hash.index(value);
 assertEquals(expectedIndex, actualIndex);
 }
 }

 @Test
 public void testIndexRehashed() {
 short key = testData[2];
 int index = hash.index(key);
 int rehashedIndex = hash.indexRehashed(key, index, hash(key), (byte) 1);
 assertEquals(index, rehashedIndex);
 }

 @Test
 public void testInsertKey() {
 short newValue = (short) 6;
 int index = hash.insertKey(newValue);
 assertEquals(6, hash.get(newValue));
 assertEquals(6, hash._set[index]);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithKeyAlreadyPresent() {
 short keyToInsert = testData[0];
 hash.insertKey(keyToInsert);
 hash.insertKey(keyToInsert);
 }

 @Test
 public void testInsertKeyRehash() {
 short newValue = (short) 7;
 int index = hash.insertKeyRehash(newValue, 0, hash(newValue), (byte) 1);
 assertEquals(7, hash.get(newValue));
 assertEquals(7, hash._set[index]);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithNoFreeOrRemovedSlotsAvailable() {
 short newValue = (short) 11;
 hash.insertKey(newValue);
 hash.insertKey(newValue);
 }
}