import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(11);
 }

 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertNotNull(hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 TShortHash hash = new TShortHash(11);
 assertEquals(11, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash hash = new TShortHash(11, 0.75f);
 assertEquals(11, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01);
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TShortHash hash = new TShortHash(11, 0.75f, (short) 1234);
 assertEquals(11, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01);
 assertEquals((short) 1234, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash(11);
 short noEntryValue = hash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(11);
 assertEquals(11, hash.capacity());
 hash.setUp(11);
 assertNotNull(hash._set);
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(11);
 hash.put((short) 1);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash(11);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 if (value == 2) {
 return false;
 }
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 TShortHash hash = new TShortHash(11);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 int index = 1;
 hash.removeAt(index);
 assertEquals(2, hash.size());
 assertNotEquals((short) 2, hash.get(index));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtInvalidIndex() {
 TShortHash hash = new TShortHash(11);
 hash.removeAt(12);
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash(11);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 int index = hash.index((short) 2);
 assertEquals(1, index);
 }

 @Test
 public void testIndexRehashed() {
 TShortHash hash = new TShortHash(11);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 int index = hash.index((short) 5);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash(11);
 int index = hash.insertKey((short) 1);
 assertEquals(1, index);
 assertTrue(hash.contains((short) 1));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 TShortHash hash = new TShortHash(3);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 }
}