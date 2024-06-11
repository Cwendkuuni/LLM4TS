import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setUp() {
 charHash = new TCharHash(10);
 }

 @Test
 public void testDefaultConstructor() {
 char defaultValue = CharHash.Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
 assertEquals(defaultValue, charHash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 charHash = new TCharHash(5);
 assertEquals(5, charHash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 charHash = new TCharHash(10, 0.75f);
 assertEquals(10, charHash.size());
 assertEquals(7, charHash.capacity());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 charHash = new TCharHash(5, 0.75f, 'X');
 char expectedValue = 'X';
 char testValue = 'A';
 charHash.put(testValue);
 assertEquals(6, charHash.size());
 assertEquals(expectedValue, charHash.get('X'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 charHash = new TCharHash(-5);
 fail("Initial capacity cannot be negative");
 }

 @Test
 public void testLoadFactorGreaterThanOne() {
 charHash = new TCharHash(10, 2.0f);
 assertEquals(10, charHash.size());
 assertEquals(20, charHash.capacity());
 }

 @Test
 public void testContains() {
 charHash.put('A');
 assertTrue(charHash.contains('A'));
 }

 @Test
 public void testContainsFalse() {
 charHash.put('A');
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testForEach() {
 charHash.put('A');
 charHash.put('B');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 assertEquals('A', character);
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = 1;
 charHash.removeAt(index);
 assertEquals(2, charHash.size());
 assertEquals('C', charHash.get(index));
 }

 @Test
 public void testIndex() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = 1;
 assertEquals('B', charHash.get(index));
 }

 @Test
 public void testIndexNegative() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 int index = -1;
 assertEquals(-1, charHash.index(index));
 }

 @Test
 public void testIndexNull() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 char key = null;
 assertEquals(-1, charHash.index(key));
 }
}