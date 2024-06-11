import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hashMap;
 private TShortProcedure procedure;

 @Before
 public void setUp() {
 hashMap = new TShortHash(11);
 procedure = new TShortProcedure() {
 @Override
 public boolean execute(short val) {
 return hashMap.contains(val);
 }
 };
 }

 @Test
 public void testEmptyConstructor() {
 TShortHash emptyHashMap = new TShortHash();
 assertEquals(0, emptyHashMap.size());
 assertFalse(emptyHashMap.contains((short) 1));
 }

 @Test
 public void testInitialCapacityConstructor() {
 TShortHash hashMap = new TShortHash(5);
 assertEquals(5, hashMap.size());
 assertFalse(hashMap.contains((short) 1));
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash hashMap = new TShortHash(11, 0.75f);
 assertEquals(8, hashMap.size());
 assertFalse(hashMap.contains((short) 1));
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TShortHash hashMap = new TShortHash(11, 0.75f, (short) 127);
 assertEquals(8, hashMap.size());
 assertFalse(hashMap.contains((short) 1));
 assertEquals((short) 127, hashMap.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hashMap = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hashMap.getNoEntryValue());
 hashMap.setNoEntryValue((short) 0);
 assertEquals((short) 0, hashMap.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TShortHash hashMap = new TShortHash(11);
 assertEquals(11, hashMap.size());
 }

 @Test
 public void testContains() {
 TShortHash hashMap = new TShortHash(5);
 hashMap.put((short) 1, (short) 1);
 hashMap.put((short) 2, (short) 2);
 assertTrue(hashMap.contains((short) 1));
 assertTrue(hashMap.contains((short) 2));
 assertFalse(hashMap.contains((short) 3));
 }

 @Test
 public void testForEach() {
 TShortHash hashMap = new TShortHash(5);
 hashMap.put((short) 1, (short) 1);
 hashMap.put((short) 2, (short) 2);
 hashMap.put((short) 3, (short) 3);
 for (short i = 0; i < 5; i++) {
 procedure.value(i);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAt() {
 TShortHash hashMap = new TShortHash(5);
 hashMap.put((short) 1, (short) 1);
 hashMap.put((short) 2, (short) 2);
 hashMap.removeAt(-1);
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtWithNullIndex() {
 TShortHash hashMap = new TShortHash(5);
 hashMap.put((short) 1, (short) 1);
 hashMap.put((short) 2, (short) 2);
 hashMap.removeAt(null);
 }

 @Test
 public void testIndex() {
 TShortHash hashMap = new TShortHash(5);
 hashMap.put((short) 1, (short) 1);
 hashMap.put((short) 2, (short) 2);
 hashMap.put((short) 3, (short) 3);
 assertEquals(3, hashMap.index((short) 3));
 assertEquals(-1, hashMap.index((short) 4));
 }

 @Test
 public void testInsertKey() {
 TShortHash hashMap = new TShortHash(5);
 assertEquals(5, hashMap.size());
 hashMap.insertKey((short) 6);
 assertEquals(6, hashMap.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHashSet() {
 TShortHash hashMap = new TShortHash(5);
 for (short i = 0; i < 5; i++) {
 hashMap.put((short) i, (short) i);
 }
 hashMap.insertKey((short) 6);
 }
}