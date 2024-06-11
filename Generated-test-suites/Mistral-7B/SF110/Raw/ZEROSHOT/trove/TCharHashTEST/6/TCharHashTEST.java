import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

public class TCharHashTest {

 private TCharHash charHash;
 private char[] expectedSet;

 @Before
 public void setUp() {
 charHash = new TCharHash(10);
 expectedSet = new char[10];
 }

 @Test
 public void testTCharHashBasicConstruction() {
 TCharHash charHash = new TCharHash();
 assertNotNull(charHash);
 assertNull(charHash.getNoEntryValue());
 assertSame(TCharHash.class, charHash.getClass());
 }

 @Test
 public void testTCharHashWithInitialCapacity() {
 TCharHash charHash = new TCharHash(5);
 assertNotNull(charHash);
 assertSame(TCharHash.class, charHash.getClass());
 assertEquals(5, charHash.size());
 }

 @Test
 public void testTCharHashWithInitialCapacityAndLoadFactor() {
 TCharHash charHash = new TCharHash(11, 0.75f);
 assertNotNull(charHash);
 assertSame(TCharHash.class, charHash.getClass());
 assertEquals(15, charHash.capacity());
 }

 @Test
 public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
 TCharHash charHash = new TCharHash(15, 0.75f, 'X');
 assertNotNull(charHash);
 assertSame(TCharHash.class, charHash.getClass());
 assertEquals('X', charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 charHash.setNoEntryValue('Z');
 assertEquals('Z', charHash.getNoEntryValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TCharHash charHash = new TCharHash(-5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testZeroInitialCapacity() {
 TCharHash charHash = new TCharHash(0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 TCharHash charHash = new TCharHash(5, -0.5f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFactorGreaterThanOne() {
 TCharHash charHash = new TCharHash(5, 1.5f);
 }

 @Test
 public void testSizeAndIsEmpty() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 assertFalse(charHash.isEmpty());
 assertEqauls(5, charHash.size());
 }

 @Test
 public void testContains() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
assertTrue(charHash.contains('D'));
 assertTrue(charHash.contains('E'));
 }

 @Test
 public void testDoesNotContain() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 assertFalse(charHash.contains('F'));
 }

 @Test
 public void testForEachProcedure() {
 Set<Character> result = new HashSet<>();
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.forEach(new TCharProcedure() {
 @Override
 public boolean exec(char character) {
 result.add(character);
 return true;
 }
 });
 assertEquals(5, result.size());
 }

 @Test
 public void testRemoveChar() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.remove('D');
 assertFalse(charHash.contains('D'));
 assertEquals(4, charHash.size());
 }

 @Test
 public void testRemoveCharThatDoesNotExist() {
 charHash.put('A');
 charHash.put('B');
 charHash.put('C');
 charHash.put('D');
 charHash.put('E');
 charHash.remove('F');
 assertFalse(charHash.contains('F'));
 assertEquals(5, charHash.size());
 }

 @After
 public void tearDown() {
 for (int i = 0; i < expectedSet.length; i++) {
 charHash.remove((char) i);
 }
 }
}