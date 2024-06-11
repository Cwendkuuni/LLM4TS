import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TShortHash hash = new TShortHash(5, 0.75f);
 assertEquals(5, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash hash = new TShortHash(5, 0.75f, (short) 1);
 assertEquals(1, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash();
 assertFalse(hash.contains((short) 1));
 hash.put((short) 1, (short) 1);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 return value == 1;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.removeAt(0);
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 assertEquals(0, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 3));
 }

 @Test
 public void testIndexRehashed() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 assertEquals(2, hash.index((short) 3));
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.put((short) 4, (short) 4);
 assertTrue(hash.contains((short) 3));
 assertTrue(hash.containsKey((short) 3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyWithNegativeValue() {
 TShortHash hash = new TShortHash();
 hash.put((short) -1, (short) 1);
 }

 @Test
 public void testInsertKeyRehash() {
 TShortHash hash = new TShortHash();
 for (short i = 0; i < 11; i++) {
 hash.put((short) i, (short) i);
 }
 assertTrue(hash.contains((short) 10));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 TShortHash hash = new TShortHash();
 for (short i = 0; i < 11; i++) {
 hash.put((short) i, (short) i);
 }
 }
}