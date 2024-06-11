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
 public void testConstructor() {
 assertEquals(5, charHash.capacity());
 assertNotNull(charHash._set);
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 char defaultCharNoEntryValue = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
 charHash.setNoEntryValue(defaultCharNoEntryValue);
 assertEquals(defaultCharNoEntryValue, charHash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 char key;
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);

 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 assertTrue(charHash.contains('D'));
 assertTrue(charHash.contains('E'));

 assertFalse(charHash.contains('F'));
 }

 @Test
 public void testForEach() {
 char key;
 int count = 0;
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char value) {
 count++;
 return true;
 }
 };
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);
 charHash.forEach(procedure);
 assertEquals(5, count);
 }

 @Test
 public void testRemoveAt() {
 char key;
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);

 int indexOfA = charHash.indexOf('A');
 int indexOfB = charHash.indexOf('B');
 int indexOfC = charHash.indexOf('C');
 int indexOfD = charHash.indexOf('D');
 int indexOfE = charHash.indexOf('E');

 assertEquals(0, indexOfA);
 assertEquals(1, indexOfB);
 assertEquals(2, indexOfC);
 assertEquals(3, indexOfD);
 assertEquals(4, indexOfE);

 charHash.removeAt(indexOfA);
 charHash.removeAt(indexOfB);
 charHash.removeAt(indexOfC);
 charHash.removeAt(indexOfD);
 charHash.removeAt(indexOfE);

 assertFalse(charHash.contains('A'));
 assertFalse(charHash.contains('B'));
 assertFalse(charHash.contains('C'));
 assertFalse(charHash.contains('D'));
 assertFalse(charHash.contains('E'));
 }

 @Test(expected = IllegalStateException.class)
 public void testPutWhenFull() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);
 charHash.put('F', 6);
 }

}