import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setUp() {
 charHash = new TCharHash(11);
 }

 @Test
 public void testDefaultConstructor() {
 charHash = new TCharHash();
 assertNotNull(charHash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 assertEquals(11, charHash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 charHash = new TCharHash(11, 0.75f);
 assertEquals(15, charHash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 charHash = new TCharHash(11, 0.75f, 'Z');
 assertEquals(15, charHash.size());
 assertEquals('Z', charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 char defaultChar = charHash.getNoEntryValue();
 assertEquals(defaultChar, Constants.DEFAULT_CHAR_NO_ENTRY_VALUE);
 charHash.setNoEntryValue('X');
 assertEquals('X', charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 charHash = new TCharHash(11);
 assertEquals(11, charHash.size());
 charHash._set = new char[16];
 charHash.setUp(16);
 assertEquals(16, charHash.size());
 }

 @Test
 public void testContains() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 }

 @Test
 public void testForEach() {
 char count = 0;
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 count++;
 return true;
 }
 };
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.forEach(procedure);
 assertEquals(3, count);
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 int index = charHash.indexOf('B');
 charHash.removeAt(index);
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testIndex() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 int indexA = charHash.indexOf('A');
 int indexB = charHash.indexOf('B');
 int indexC = charHash.indexOf('C');
 assertEquals(indexA, 0);
 assertEquals(indexB, 1);
 assertEquals(indexC, 2);
 }

 @Test
 public void testInsertKey() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);
 assertEquals(5, charHash.size());
 }

 @Test
 public void testInsertKeyRehash() {
 charHash.put('A', 1);
 charHash.put('B', 2);
 charHash.put('C', 3);
 charHash.put('D', 4);
 charHash.put('E', 5);
 charHash.put('F', 6);
 charHash.put('G', 7);
 charHash.put('H', 8);
 charHash.put('I', 9);
 charHash.put('J', 10);
 charHash.put('K', 11);
 charHash.put('L', 12);
 charHash.put('M', 13);
 charHash.put('N', 14);
 charHash.put('O', 15);
 charHash.put('P', 16);
 charHash.put('Q', 17);
 charHash.put('R', 18);
 charHash.put('S', 19);
 charHash.put('T', 20);
 charHash.put('U', 21);
 charHash.put('V', 22);
 charHash.put('W', 23);
 charHash.put('X', 24);
 charHash.put('Y', 25);
 charHash.put('Z', 26);
 assertEquals(26, charHash.size());
 }
}