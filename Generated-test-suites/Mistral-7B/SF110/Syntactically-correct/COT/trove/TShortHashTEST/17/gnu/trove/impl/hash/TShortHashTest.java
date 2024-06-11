import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hash = new TShortHash(11);
 }

 @Test
 public void testConstructorWithDefaultValues() {
 TShortHash hash = new TShortHash();
 assertEquals(0, hash.getNoEntryValue());
 assertNull(hash._set);
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TShortHash hash = new TShortHash(5);
 assertNotNull(hash._set);
 assertEquals(5, hash._set.length);
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TShortHash hash = new TShortHash(10, 0.75f);
 assertNotNull(hash._set);
 assertEquals(14, hash._set.length); // initial capacity * 1 + 1
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash hash = new TShortHash(15, 0.75f, (short) 42);
 assertNotNull(hash._set);
 assertEquals(21, hash._set.length); // initial capacity * 1 + 1
 assertEquals(42, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash(7);
 hash.setNoEntryValue((short) 123);
 assertEquals(123, hash.getNoEntryValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetUpWithNegativeCapacity() {
 TShortHash hash = new TShortHash(-3);
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 assertTrue(hash.contains((short) 4));
 assertFalse(hash.contains((short) 6));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 final int count = 0;
 hash.forEach(new TShortProcedure() {
 @Override
 public boolean execute(short short1) {
 count++;
 return true;
 }
 });
 assertEquals(5, count);
 }

 @Test
 public void testRemoveAt() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 int index = hash.indexOf((short) 4);
 hash.removeAt(index);
 assertFalse(hash.contains((short) 4));
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 assertEquals(4, hash.indexOf((short) 4));
 assertEquals(-1, hash.indexOf((short) 6));
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 int index = hash.insertKey((short) 6);
 assertEquals(6, hash._set[index]);
 assertFalse(hash.contains((short) 4));
 assertEquals(6, hash.indexOf((short) 6));
 }

 @Test
 public void testInsertKeyRehash() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 assertFalse(hash.contains((short) 7));
 int index = hash.insertKey((short) 7);
 assertEquals(7, hash._set[index]);
 assertTrue(hash.contains((short) 7));
 assertEquals(7, hash.indexOf((short) 7));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 6);
 hash.put((short) 7); // should throw an exception
 }

}