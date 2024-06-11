import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 charHash = new TCharHash(5);
 }

 @Test
 public void testTCharHashConstructor() {
 TCharHash charHash = new TCharHash();
 assertEquals(1, charHash.size());
 }

 @Test
 public void testTCharHashConstructorWithInitialCapacity() {
 TCharHash charHash = new TCharHash(5);
 assertEquals(5, charHash.size());
 }

 @Test
 public void testTCharHashConstructorWithInitialCapacityAndLoadFactor() {
 TCharHash charHash = new TCharHash(5, 0.75f);
 assertEquals(5, charHash.size());
 }

 @Test
 public void testTCharHashConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TCharHash charHash = new TCharHash(5, 0.75f, 'E');
 assertEquals(5, charHash.size());
 assertEquals('E', charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash = new TCharHash();
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 charHash = new TCharHash(5);
 assertEquals(5, charHash.capacity());
 }

 @Test
 public void testContains() {
 charHash.put('A');
 assertTrue(charHash.contains('A'));
 }

 @Test
 public void testContainsWithEmptyHash() {
 assertFalse(charHash.contains('B'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testContainsWithNullValue() {
 charHash.put('A');
 assertFalse(charHash.contains(null));
 }

 @Test
 public void testForEach() {
 charHash.put('A');
 charHash.put('B');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 if (character == 'A') {
 return false;
 }
 return true;
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
 charHash.put('A');
 charHash.put('B');
 TCharProcedure procedure = null;
 assertFalse(charHash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = 1;
 charHash.removeAt(index);
 assertEquals(2, charHash.size());
 assertEquals('B', charHash.get(index));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWithNegativeIndex() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = -1;
 charHash.removeAt(index);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWithIndexGreaterThanCapacity() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = 5;
 charHash.removeAt(index);
 }

 @Test
 public void testIndex() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = indexOf('B');
 assertEquals(1, index);
 }

 @Test
 public void testIndexWithNullValue() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = indexOf(null);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexWithNonExistentValue() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = indexOf('D');
 assertEquals(-1, index);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexWithNullValueNullReturnType() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int[] indices = indexes(null);
 assertNull(indices);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexWithNullValueArrayReturnType() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int[] indices = indexes(new int[0]);
 assertNull(indices);
 }

 @Test
 public void testInsertKey() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 int index = charHash.insertKey('E');
 assertEquals(6, charHash.size());
 assertEquals('E', charHash.get(index));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyWithNullValue() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHash() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.put('F');
 }
}