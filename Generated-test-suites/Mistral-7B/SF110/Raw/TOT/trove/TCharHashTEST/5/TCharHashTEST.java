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
 char defaultValue = TCharHash.Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
 assertArrayEquals(new char[5], charHash._set);
 assertEquals(defaultValue, charHash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 charHash = new TCharHash(10);
 assertEquals(10, charHash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 charHash = new TCharHash(15, 0.75f);
 assertEquals(15, charHash.size());
 }

 @Test
 public void testInitialCapacityLoadFactorAndNoEntryValueConstructor() {
 charHash = new TCharHash(20, 0.8f, 'X');
 assertEquals(20, charHash.size());
 assertEquals('X', charHash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 charHash.put((char) 10);
 assertTrue(charHash.contains((char) 10));
 }

 @Test
 public void testContainsEmptyHash() {
 assertFalse(charHash.contains('A'));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testContainsNegativeKey() {
 charHash.put((char) -1);
 try {
 charHash.contains((char) -1);
 } catch (IllegalArgumentException e) {
 assertEquals("Negative key", e.getMessage());
 }
 }

 @Test
 public void testForEach() {
 charHash.put((char) 1);
 charHash.put((char) 2);
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals(charValue, (char) (charValue + 1));
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testGetNoEntryValue() {
 char defaultValue = TCharHash.Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
 assertEquals(defaultValue, charHash.getNoEntryValue());
 }

 @Test
 public void testRemoveAt() {
 charHash.put((char) 1);
 charHash.put((char) 2);
 charHash.put((char) 3);
 int index = 1;
 charHash.removeAt(index);
 assertEquals(3, charHash.size());
 assertNotEquals((char) 2, charHash._set[index]);
 }

 @Test (expected = IndexOutOfBoundsException.class)
 public void testRemoveAtOutOfBounds() {
 charHash.put((char) 1);
 charHash.put((char) 2);
 int index = 5;
 try {
 charHash.removeAt(index);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testIndex() {
 charHash.put((char) 1);
 charHash.put((char) 2);
 charHash.put((char) 1);
 int index1 = charHash.index((char) 1);
 int index2 = charHash.index((char) 2);
 assertEquals(0, index1);
 assertEquals(1, index2);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testIndexNegativeKey() {
 charHash.put((char) 1);
 try {
 charHash.index((char) -1);
 } catch (IllegalArgumentException e) {
 assertEquals("Negative key", e.getMessage());
 }
 }
}