import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setUp() {
 charHash = new TCharHash(5);
 }

 @Test
 public void testDefaultConstructor() {
 assertEquals(5, charHash.size());
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 charHash = new TCharHash(10);
 assertEquals(10, charHash.size());
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithLoadFactor() {
 charHash = new TCharHash(10, 0.5f);
 assertEquals(10, charHash.capacity());
 assertEquals(5, charHash.size());
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 charHash = new TCharHash(10, 0.5f, 'N');
 assertEquals(10, charHash.capacity());
 assertEquals(5, charHash.size());
 assertEquals('N', charHash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 charHash.put('A', 1);
 assertTrue(charHash.contains('A'));
 }

 @Test
 public void testDoesNotContain() {
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testForEach() {
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 assertEquals('A', character);
 return true;
 }
 };
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.forEach(procedure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachOnEmptyHash() {
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 return true;
 }
 };
 TCharHash emptyHash = new TCharHash();
 emptyHash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 int indexA = charHash.indexOf('A');
 int indexB = charHash.indexOf('B');
 charHash.removeAt(indexA);
 assertEquals(1, charHash.size());
 assertFalse(charHash.contains('A'));
 assertEquals('B', charHash.get(indexB));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtOutOfBounds() {
 charHash.put('A', 1);
 charHash.removeAt(-1);
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

 @Test
 public void testIndexRehashed() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 int indexC = charHash.indexOf('C');
 assertEquals(-1, indexC);
 }

 @Test
 public void testInsertKey() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);
 assertEquals(5, charHash.size());
 assertEquals(0, charHash.indexOf('A'));
 assertEquals(1, charHash.indexOf('B'));
 assertEquals(2, charHash.indexOf('C'));
 assertEquals(3, charHash.indexOf('D'));
 assertEquals(4, charHash.indexOf('E'));
 }
}