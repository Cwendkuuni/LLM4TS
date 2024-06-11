package test;

import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Test
 public void testDefaultConstructor() {
 charHash = new TCharHash();
 assertNotNull(charHash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 charHash = new TCharHash(10);
 assertEquals(10, charHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 charHash = new TCharHash(10, 0.75f);
 assertEquals(10, charHash.capacity());
 assertEquals(7, charHash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 charHash = new TCharHash(10, 0.75f, 'X');
 assertEquals(10, charHash.capacity());
 assertEquals(7, charHash.size());
 assertEquals('X', charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash = new TCharHash('A');
 assertEquals('A', charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 charHash = new TCharHash(10);
 assertEquals(10, charHash.size());
 }

 @Test
 public void testContains() {
 charHash = new TCharHash('A');
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 assertTrue(charHash.contains('A'));
 assertFalse(charHash.contains('D'));
 }

 @Test
 public void testForEach() {
 charHash = new TCharHash('A');
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char value) {
 assertEquals('A', value);
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtWhenKeyIsNotPresent() {
 charHash = new TCharHash('A');
 charHash.removeAt(-1);
 }

 @Test
 public void testIndex() {
 charHash = new TCharHash('A');
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 assertEquals(0, charHash.index('A'));
 assertEquals(-1, charHash.index('D'));
 }

 @Test
 public void testInsertKey() {
 charHash = new TCharHash('A');
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 assertEquals(4, charHash.size());
 assertEquals(-1, charHash.index('D'));
 charHash.put('D');
 assertEquals(5, charHash.size());
 assertEquals(3, charHash.index('C'));
 }

 @Test
 public void testInsertKeyWithInitialCapacityLoadFactorAndNoEntryValue() {
 charHash = new TCharHash(5, 0.5f, 'X');
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 assertEquals(5, charHash.capacity());
 assertEquals(5, charHash.size());
 assertEquals('X', charHash.getNoEntryValue());
 }

}