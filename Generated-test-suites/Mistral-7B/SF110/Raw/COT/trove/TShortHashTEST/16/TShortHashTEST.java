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
 public void testConstructorWithDefaults() {
     TShortHash hash = new TShortHash();
     assertEquals(0, hash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
     TShortHash hash = new TShortHash(5);
     assertEquals(5, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
     TShortHash hash = new TShortHash(5, 0.75f);
     assertEquals(4, hash.size(), "Initial capacity minus one for load factor");
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
     TShortHash hash = new TShortHash(5, 0.75f, (short) 123);
     assertEquals(4, hash.size(), "Initial capacity minus one for load factor");
     assertEquals((short) 123, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
     TShortHash hash = new TShortHash();
     assertEquals(0, hash.getNoEntryValue());
     hash.setNoEntryValue((short) 123);
     assertEquals((short) 123, hash.getNoEntryValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetUpWithNegativeInitialCapacity() {
     TShortHash hash = new TShortHash(-5);
 }

 @Test
 public void testContains() {
     hash.put((short) 1);
     assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testContainsWhenKeyIsNotPresent() {
     assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testForEach() {
     hash.put((short) 1);
     hash.put((short) 2);
     final short[] expected = {1, 2};
     TShortProcedure procedure = new TShortProcedure() {
         short[] result = new short[1];
         @Override
         public boolean execute(short short1) {
             result[0] = short1;
             return true;
         }
     };
     hash.forEach(procedure);
     assertArrayEquals(expected, procedure.result);
 }

 @Test
 public void testRemoveAt() {
     hash.put((short) 1);
     hash.put((short) 2);
     int index = hash.indexOf((short) 1);
     hash.removeAt(index);
     assertFalse(hash.contains((short) 1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWithInvalidIndex() {
     hash.removeAt(-1);
 }

 @Test
 public void testIndex() {
     hash.put((short) 1);
     hash.put((short) 2);
     int index = hash.indexOf((short) 1);
     assertEquals(0, index);
     index = hash.indexOf((short) 3);
     assertEquals(-1, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyWithNegativeValue() {
     hash.put((short) 1);
     hash.put((short) 2);
     hash.insertKey((short) -1);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHashTable() {
     for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
         hash.put((short) i);
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyWithNullValue() {
     hash.put((short) 1);
     try {
         hash.insertKey((short) null);
     } catch (NullPointerException e) {
         fail("Expected IllegalStateException, not NullPointerException");
     }
 }

 @Test
 public void testInsertKeyRehash() {
     for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
         hash.put((short) i);
     }
     hash.insertKey((short) 0);
 }

 @Test
 public void testHashFunction() {
     assertEquals(11, HashFunctions.hash((short) 1));
 }
}