package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import gnu.trove.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;
 private char[] testValues;
 private Random random;

 @Before
 public void setUp() {
 charHash = new TCharHash(11, 0.75f, '\u0000');
 testValues = new char[11];
 random = new Random();
 }

 @Test
 public void testEmptyHash() {
 assertEquals(0, charHash.size());
 assertNull(charHash.getNoEntryValue());
 }

 @Test
 public void testBasicOperations() {
 charHash.put('a', (char) 0);
 charHash.put('b', (char) 1);
 charHash.put('c', (char) 2);
 assertEquals(3, charHash.size());
 assertTrue(charHash.contains('a'));
 assertTrue(charHash.contains('b'));
 assertTrue(charHash.contains('c'));
 assertFalse(charHash.contains('d'));
 }

 @Test
 public void testPutAll() {
 charHash.clear();
 char hashValues[] = {'d', 'e', 'f'};
 charHash.putAll(hashValues);
 assertEquals(3, charHash.size());
 for (char value : hashValues) {
 assertTrue(charHash.contains(value));
 }
 }

 @Test
 public void testRemove() {
 charHash.put('a', (char) 0);
 charHash.put('b', (char) 1);
 charHash.put('c', (char) 2);
 charHash.remove('b');
 assertEquals(2, charHash.size());
 assertFalse(charHash.contains('b'));
 }

 @Test
 public void testClear() {
 charHash.put('a', (char) 0);
 charHash.put('b', (char) 1);
 charHash.put('c', (char) 2);
 charHash.clear();
 assertEquals(0, charHash.size());
 }

 @Test
 public void testNullKey() {
 try {
 charHash.put((char) 0, null);
 fail("Should not allow null keys");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testEmptyProcedure() {
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charToTest) {
 return true;
 }
 };
 charHash.clear();
 charHash.forEach(procedure);
 }

 @Test
 public void testProcedureWithValues() {
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charToTest) {
 testValues[indexOfChar(charToTest)] = charToTest + 1;
 return true;
 }
 };
 charHash.put('a', (char) 0);
 charHash.put('b', (char) 1);
 charHash.put('c', (char) 2);
 charHash.forEach(procedure);
 assertEquals((char) 1, testValues[0]);
 assertEquals((char) 2, testValues[1]);
 assertEquals((char) 3, testValues[2]);
 }

 private int indexOfChar(char c) {
 for (int i = 0; i < testValues.length; i++) {
 if (testValues[i] == c) {
 return i;
 }
 }
 return -1;
 }

 @Test(expected = IllegalStateException.class)
 public void testHashFull() {
 for (int i = 0; i < 11; i++) {
 charHash.put((char) i, (char) i);
 }
 }
}