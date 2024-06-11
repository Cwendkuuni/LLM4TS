import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
     hash = new TShortHash(5);
 }

 @Test
 public void testTShortHashConstructor() {
     assertEquals(5, hash.capacity());
     assertNull(hash.getNoEntryValue());
     assertTrue(hash.isEmpty());
 }

 @Test
 public void testTShortHashInitialCapacityAndLoadFactorConstructor() {
     TShortHash hash1 = new TShortHash(5, 0.75f);
     assertEquals(5, hash1.capacity());
     assertEquals(0.75f, hash1.loadFactor(), 0.01f);
     assertNull(hash1.getNoEntryValue());
     assertTrue(hash1.isEmpty());
 }

 @Test
 public void testTShortHashInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
     short noEntryValue = 1;
     TShortHash hash2 = new TShortHash(5, 0.75f, noEntryValue);
     assertEquals(5, hash2.capacity());
     assertEquals(0.75f, hash2.loadFactor(), 0.01f);
     assertEquals(noEntryValue, hash2.getNoEntryValue());
     assertTrue(hash2.isEmpty());
 }

 @Test
 public void testGetNoEntryValue() {
     TShortHash hash3 = new TShortHash(5);
     hash3.setNoEntryValue((short) 2);
     assertEquals((short) 2, hash3.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
     TShortHash hash4 = new TShortHash(5);
     assertEquals(5, hash4.capacity());
     assertArrayEquals(new short[5], hash4._set);
     assertArrayEquals(new byte[5], hash4._states);
 }

 @Test
 public void testContains() {
     hash.put((short) 1);
     assertTrue(hash.contains((short) 1));
     assertFalse(hash.contains((short) 2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsNegativeValue() {
     hash.put((short) 1);
     hash.contains((short) -1);
 }

 @Test
 public void testForEach() {
     hash.put((short) 1);
     hash.put((short) 2);
     TShortProcedure procedure = new TShortProcedure() {
         @Override
         public boolean execute(short value) {
             assertEquals((short) 1, value);
             return true;
         }
     };
     assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
     hash.put((short) 1);
     hash.put((short) 2);
     int index = hash.index((short) 1);
     assertEquals(1, index);
     hash.removeAt(index);
     assertFalse(hash.contains((short) 1));
     assertNull(hash.get((short) 1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtNegativeIndex() {
     hash.removeAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtBeyondCapacity() {
     hash.put((short) 1);
     hash.put((short) 2);
     hash.removeAt(hash.capacity());
 }

 @Test
 public void testIndex() {
     hash.put((short) 1);
     hash.put((short) 2);
     assertEquals(1, hash.index((short) 1));
     assertEquals(-1, hash.index((short) 3));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexNegativeValue() {
     hash.put((short) 1);
     hash.put((short) 2);
     hash.index((short) -1);
 }

 @Test
 public void testIndexRehashed() {
     hash.put((short) 1);
     hash.put((short) 2);
     int index = hash.index((short) 1);
     assertEquals(1, index);
     hash.put((short) 3);
     assertEquals(2, hash.index((short) 3));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFullHash() {
     for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
         hash.put(i);
     }
     hash.put((short) 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyNegativeValue() {
     hash.put((short) 1);
     hash.put((short) -1);
 }

 @Test
 public void testInsertKeyRehash() {
     hash.put((short) 1);
     hash.put((short) 2);
     int index = hash.index((short) 3);
     hash.put((short) 3);
     assertEquals(2, index);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyNoFreeOrRemovedSlotsAvailable() {
     for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
         hash.put(i);
     }
     hash.put((short) 1);
 }

}