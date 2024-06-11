import org.junit.After;
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
 assertEquals(5, charHash.capacity());
 assertNotNull(charHash._set);
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 charHash = new TCharHash(10);
 assertEquals(10, charHash.capacity());
 assertNotNull(charHash._set);
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 charHash = new TCharHash(15, 0.75f);
 assertEquals(15, charHash.capacity());
 assertNotNull(charHash._set);
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 assertEquals(0.75f, charHash.loadFactor(), 0.01f);
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 charHash = new TCharHash(20, 0.8f, 'X');
 assertEquals(20, charHash.capacity());
 assertNotNull(charHash._set);
 assertEquals('X', charHash.getNoEntryValue());
 assertEquals(0.8f, charHash.loadFactor(), 0.01f);
 }

 @Test
 public void testContains() {
 charHash.put('A', 'A');
 assertTrue(charHash.contains('A'));
 }

 @Test
 public void testContainsWithNonPresentKey() {
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testForEach() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 CharArrayChar iterator = new CharArrayChar(new char[charHash.size()]);
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 iterator[0] = character;
 return true;
 }
 };
 charHash.forEach(procedure);
 assertEquals('A', iterator[0]);
 assertEquals('B', iterator[1]);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachWithConcurrentModification() {
 charHash.put('A', 'A');
 final char[] iterator = new char[charHash.size()];
 charHash.forEach(new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 iterator[0] = character;
 charHash.remove('A');
 return true;
 }
 });
 fail("ForEach should not allow concurrent modification");
 }

 @Test
 public void testInsertKey() {
 charHash.put('C', 'C');
 assertEquals(3, charHash.size());
 charHash.put('D', 'D');
 assertEquals(4, charHash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyWithNullCharacter() {
 charHash.put(null, 'N');
 fail("Should not allow inserting null character as key");
 }

 @Test
 public void testRemoveAt() {
 charHash.put('E', 'E');
 charHash.put('F', 'F');
 charHash.put('G', 'G');
 assertEquals(3, charHash.size());
 int index = charHash.indexOf('F');
 charHash.removeAt(index);
 assertEquals(2, charHash.size());
 }

 @Test
 public void testIndex() {
 charHash.put('H', 'H');
 charHash.put('I', 'I');
 charHash.put('J', 'J');
 int index = charHash.indexOf('I');
 assertEquals(1, index);
 }

 @Test
 public void testIndexWithNonPresentKey() {
 charHash.put('K', 'K');
 charHash.put('L', 'L');
 charHash.put('M', 'M');
 int index = charHash.indexOf('N');
 assertEquals(-1, index);
 }

 @After
 public void tearDown() {
 charHash = null;
 }
}