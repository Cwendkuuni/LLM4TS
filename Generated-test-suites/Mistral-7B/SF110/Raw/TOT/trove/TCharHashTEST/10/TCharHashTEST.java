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
 public void testEmptyHash() {
 assertEquals(0, charHash.size());
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testAddChar() {
 charHash.put('a', 'a');
 assertEquals(1, charHash.size());
 assertTrue(charHash.contains('a'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddCharAlreadyPresent() {
 charHash.put('a', 'a');
 charHash.put('a', 'b');
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testForEach() {
 charHash.put('a', 'a');
 charHash.put('b', 'b');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char value) {
 assertEquals('a', value);
 return true;
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 @Test
 public void testRemoveChar() {
 charHash.put('a', 'a');
 charHash.remove('a');
 assertEquals(0, charHash.size());
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testContainsNull() {
 assertFalse(charHash.contains(null));
 }

 @Test
 public void testHashFunction() {
 assertEquals(191, HashFunctions.hash('a'));
 }
}