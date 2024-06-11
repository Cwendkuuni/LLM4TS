import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;
 private char key;

 @Before
 public void setUp() {
 charHash = new TCharHash(10);
 key = 'A';
 }

 @Test
 public void testEmptyConstructor() {
 TCharHash emptyCharHash = new TCharHash();
 assertEquals(0, emptyCharHash.size());
 }

 @Test
 public void testInitialCapacityConstructor() {
 assertEquals(10, charHash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TCharHash charHashWithLoadFactor = new TCharHash(10, 0.75f);
 assertEquals(13, charHashWithLoadFactor.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TCharHash charHashWithLoadFactorAndNoEntryValue = new TCharHash(10, 0.75f, 'X');
 assertEquals(13, charHashWithLoadFactorAndNoEntryValue.size());
 assertEquals('X', charHashWithLoadFactorAndNoEntryValue.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 assertEquals(charHash.size(), charHash._set.length);
 }

 @Test
 public void testContains() {
 assertFalse(charHash.contains(key));
 charHash.put(key);
 assertTrue(charHash.contains(key));
 }

 @Test
 public void testForEach() {
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 if (character == key) {
 return false;
 }
 return true;
 }
 };
 charHash.put(key);
 charHash.put('B');
 charHash.put('C');
 charHash.forEach(procedure);
 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TCharHash charHashWithNegativeCapacity = new TCharHash(-5);
 fail("IllegalArgumentException expected but not thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 TCharHash charHashWithNegativeLoadFactor = new TCharHash(10, -0.5f);
 fail("IllegalArgumentException expected but not thrown.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKey() {
 TCharHash charHashWithNullKey = new TCharHash();
 try {
 charHashWithNullKey.put((char) 0);
 fail("NullPointerException expected but not thrown.");
 } catch (NullPointerException e) {
 // anticipated
 }
 }

 @Test
 public void testPutAndGet() {
 charHash.put(key);
 assertEquals(key, charHash.get(key));
 }

 @Test
 public void testPutAndContains() {
 charHash.put(key);
 assertTrue(charHash.contains(key));
 }

 @Test
 public void testPutAndSize() {
 charHash.put(key);
 assertEquals(1, charHash.size());
 }

 @Test
 public void testRemove() {
 charHash.put(key);
 charHash.remove(key);
 assertFalse(charHash.contains(key));
 }

 @Test
 public void testRemoveAt() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.removeAt(1);
 assertEquals(3, charHash.size());
 assertFalse(charHash.contains('B'));
 }

 @Test
 public void testClear() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.clear();
 assertEquals(0, charHash.size());
 }

 @Test
 public void testHashFunction() {
 assertEquals(123, HashFunctions.hash('A'));
 assertEquals(234, HashFunctions.hash('B'));
 }

}