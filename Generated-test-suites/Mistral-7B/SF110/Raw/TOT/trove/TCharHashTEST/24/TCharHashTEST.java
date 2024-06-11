import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash<Character> charHash;

 @Before
 public void setup() {
 charHash = new TCharHash<>(5, 0.75f, 'N');
 }

 @Test
 public void testConstructorWithNoArguments() {
 TCharHash<Character> charHash = new TCharHash<>();
 assertEquals(0, charHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TCharHash<Character> charHash = new TCharHash<>(10);
 assertEquals(0, charHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TCharHash<Character> charHash = new TCharHash<>(10, 0.75f);
 assertEquals(0, charHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TCharHash<Character> charHash = new TCharHash<>(10, 0.75f, 'X');
 assertEquals(0, charHash.size());
 }

 @Test
 public void testGetNoEntryValue() {
 TCharHash<Character> charHash = new TCharHash<>();
 char defaultValue = charHash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, defaultValue);
 }

 @Test
 public void testSetUp() {
 TCharHash<Character> charHash = new TCharHash<>(5);
 assertEquals(5, charHash.size());
 }

 @Test
 public void testContains() {
 TCharHash<Character> charHash = new TCharHash<>();
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 boolean containsA = charHash.contains('A');
 boolean containsD = charHash.contains('D');
 assertTrue(containsA);
 assertFalse(containsD);
 }

 @Test
 public void testForEach() {
 TCharHash<Character> charHash = new TCharHash<>();
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 TCharProcedure<Character> procedure = new TCharProcedure<Character>() {
 @Override
 public boolean execute(final Character character) {
 assertEquals(character, charHash.get(character));
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtIndexOutOfBounds() {
 TCharHash<Character> charHash = new TCharHash<>();
 charHash.put('A', 'A');
 charHash.removeAt(5);
 }

 @Test
 public void testIndex() {
 TCharHash<Character> charHash = new TCharHash<>();
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 int indexA = charHash.index('A');
 int indexB = charHash.index('B');
 int indexC = charHash.index('C');
 int indexD = charHash.index('D');
 assertEquals(0, indexA);
 assertEquals(1, indexB);
 assertEquals(2, indexC);
 assertEquals(-1, indexD);
 }
}