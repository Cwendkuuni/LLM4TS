import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setup() {
 charHash = new TCharHash(5);
 }

 @Test
 public void testConstructor() {
 assertEquals(5, charHash.capacity());
 }

 @Test
 public void testGetNoEntryValue() {
 char noEntryValue = charHash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
 }

 @Test
 public void testSetUp() {
 int capacity = charHash.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 charHash.put('A', 1);
 assertTrue(charHash.contains('A'));
 }

 @Test
 public void testContainsFalse() {
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testForEach() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals('A', charValue);
 return true;
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachWithModifiedHash() {
 charHash.put('A', 1);
 charHash.forEach(new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 charHash.remove('A');
 return false;
 }
 });
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A', 1);
 int index = charHash.indexOf('A');
 charHash.removeAt(index);
 assertFalse(charHash.contains('A'));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtNegativeIndex() {
 charHash.put('A', 1);
 charHash.removeAt(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAtInvalidIndex() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.removeAt(5);
 }

 @Test
 public void testIndex() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 int indexA = charHash.indexOf('A');
 int indexB = charHash.indexOf('B');
 assertEquals(0, indexA);
 assertEquals(1, indexB);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexNegativeValue() {
 charHash.put('A', 1);
 int index = charHash.indexOf((char) -1);
 }

 @Test
 public void testIndexRehashed() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 int indexA = charHash.indexRehashed('A', 0, 1, (byte) 0);
 int indexB = charHash.indexRehashed('B', 0, 1, (byte) 0);
 int indexC = charHash.indexRehashed('C', 0, 1, (byte) 0);
 assertEquals(-1, indexA);
 assertEquals(1, indexB);
 assertEquals(2, indexC);
 }

 @Test
 public void testInsertKey() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 char newChar = 'D';
 int index = charHash.insertKey(newChar);
 assertEquals(4, charHash.size());
 assertEquals(3, index);
 assertTrue(charHash.contains(newChar));
 }

 @Test
 public void testInsertKeyRehash() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 char newChar = 'D';
 int index = charHash.insertKeyRehash(newChar, 0, 1, (byte) 0);
 assertEquals(4, charHash.size());
 assertEquals(-1, index);
 assertTrue(charHash.contains(newChar));
 }
}