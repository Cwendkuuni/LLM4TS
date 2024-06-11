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
 public void testConstructorWithDefaultArguments() {
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
     assertEquals(5, hash.size());
     assertEquals((short) 1, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
     TShortHash hash = new TShortHash();
     assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
     TShortHash hash = new TShortHash(5);
     assertEquals(5, hash.capacity());
 }

 @Test
 public void testContains() {
     hash.put((short) 1);
     assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testContainsWithEmptyHash() {
     assertFalse(hash.contains((short) 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
     hash.forEach(null);
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
 public void testForEachWithEmptyHash() {
     TShortProcedure procedure = new TShortProcedure() {
         @Override
         public boolean execute(short value) {
             fail();
             return false;
         }
     };
     assertTrue(hash.forEach(procedure));
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
     hash.removeAt(5);
 }

 @Test
 public void testIndex() {
     hash.put((short) 1);
     hash.put((short) 2);
     int index = hash.indexOf((short) 2);
     assertEquals(index, 1);
 }

 @Test
 public void testIndexWithEmptyHash() {
     int index = hash.indexOf((short) 1);
     assertEquals(index, -1);
 }

 @Test
 public void testInsertKey() {
     hash.put((short) 1);
     hash.put((short) 2);
     hash.put((short) 3);
     hash.put((short) 4);
     hash.put((short) 5);
     int index = hash.insertKey((short) 6);
     assertEquals(6, hash.get(index));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHash() {
     hash.put((short) 1);
     hash.put((short) 2);
     hash.put((short) 3);
     hash.put((short) 4);
     hash.put((short) 5);
     hash.put((short) 6);
     hash.put((short) 7);
 }

 @Test
 public void testInsertKeyRehash() {
     hash.put((short) 1);
     hash.put((short) 2);
     hash.put((short) 3);
     hash.put((short) 4);
     hash.put((short) 5);
     hash.put((short) 6);
     hash.put((short) 7);
     hash.put((short) 8);
     int index = hash.insertKey((short) 9);
     assertEquals(9, hash.get(index));
 }
}