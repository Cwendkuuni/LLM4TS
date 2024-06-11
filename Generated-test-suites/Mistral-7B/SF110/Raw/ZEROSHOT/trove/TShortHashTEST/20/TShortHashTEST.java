import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash shortHash;
 private TShortProcedure procedure;

 @Before
 public void setUp() {
 shortHash = new TShortHash(5);
 procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 return true;
 }
 };
 }

 @Test
 public void testEmptyConstructor() {
 TShortHash emptyHash = new TShortHash();
 assertEquals(0, emptyHash.size());
 short emptyValue = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
 assertTrue(emptyHash.contains(emptyValue));
 }

 @Test
 public void testInitialCapacityConstructor() {
 assertEquals(5, shortHash.size());
 short hashValue = (short) 123;
 shortHash.put(hashValue);
 assertTrue(shortHash.contains(hashValue));
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash hashWithLoadFactor = new TShortHash(7, 0.75f);
 assertEquals(5, hashWithLoadFactor.size());
 shortHash.put(hashValue);
 assertTrue(shortHash.contains(hashValue));
 }

 @Test
 public void testInitialCapacityLoadFactorAndNoEntryValueConstructor() {
 TShortHash hashWithNoEntryValue = new TShortHash(7, 0.75f, (short) 111);
 assertEquals(5, hashWithNoEntryValue.size());
 shortHash.put(hashValue);
 assertFalse(hashWithNoEntryValue.contains((short) 111));
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, shortHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(11);
 assertEquals(11, hash.size());
 }

 @Test
 public void testContains() {
 shortHash.put(hashValue);
 assertTrue(shortHash.contains(hashValue));
 }

 @Test
 public void testForEach() {
 shortHash.put(hashValue);
 shortHash.put((short) 456);
 shortHash.forEach(procedure);
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TShortHash negativeHash = new TShortHash(-5);
 fail("Should throw IllegalArgumentException with negative initial capacity");
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testLoadFactorLessThanZero() {
 TShortHash negativeLoadFactorHash = new TShortHash(5, -0.25f);
 fail("Should throw IllegalArgumentException with negative load factor");
 }

 @Test
 public void testRemoveAt() {
 shortHash.put(hashValue);
 shortHash.put((short) 789);
 shortHash.removeAt(0);
 assertFalse(shortHash.contains(hashValue));
 }

 @Test
 public void testIndex() {
 shortHash.put(hashValue);
 int index = shortHash.index(hashValue);
 assertEquals(0, index);
 }

 @Test
 public void testIndexRehashed() {
 shortHash.put(hashValue);
 shortHash.put((short) 789);
 int index = shortHash.indexRehashed(hashValue, 0, 1, (byte) 0);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 shortHash.put((short) 123);
 shortHash.put((short) 456);
 int index = shortHash.insertKey(hashValue);
 assertEquals(3, shortHash.size());
 assertEquals(2, index);
 assertTrue(shortHash.contains(hashValue));
 }

 @Test
 public void testInsertKeyRehash() {
 shortHash.put((short) 123);
 shortHash.put((short) 456);
 int index = shortHash.insertKeyRehash(hashValue, 0, 1, (byte) 1);
 assertEquals(4, shortHash.size());
 assertEquals(3, index);
 assertTrue(shortHash.contains(hashValue));
 }

 @Test
 public void testInsertKeyAt() {
 shortHash.put((short) 123);
 shortHash.put((short) 456);
 shortHash.removeAt(1);
 shortHash.insertKeyAt(1, hashValue);
 assertEquals(3, shortHash.size());
 assertEquals(1, shortHash.index(hashValue));
 assertTrue(shortHash.contains(hashValue));
 }

}