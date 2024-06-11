package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

import java.util.Arrays;

public class TCharHashTest {

 private TCharHash charHash;

 @BeforeClass
 public static void setupBeforeClass() {
 System.out.println("Setting up before class");
 }

 @AfterClass
 public static void tearDownAfterClass() {
 System.out.println("Tearing down after class");
 }

 @Before
 public void setup() {
 System.out.println("Setting up test");
 charHash = new TCharHash(11);
 }

 @After
 public void tearDown() {
 System.out.println("Tearing down test");
 charHash = null;
 }

 // Testing constructor with no arguments
 @Test
 public void testTCharHash() {
 charHash = new TCharHash();
 assertNotNull(charHash);
 }

 // Testing constructor with initial capacity
 @Test
 public void testTCharHashWithInitialCapacity() {
 charHash = new TCharHash(10);
 assertNotNull(charHash);
 assertEquals(10, charHash.size());
 }

 // Testing constructor with initial capacity and load factor
 @Test
 public void testTCharHashWithInitialCapacityAndLoadFactor() {
 charHash = new TCharHash(10, 0.75f);
 assertNotNull(charHash);
 assertEquals(10, charHash.capacity());
 assertEquals(7, charHash.size());
 }

 // Testing constructor with initial capacity, load factor, and no entry value
 @Test
 public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
 charHash = new TCharHash(10, 0.75f, 'E');
 assertNotNull(charHash);
 assertEquals(10, charHash.capacity());
 assertEquals(7, charHash.size());
 assertEquals('E', charHash.getNoEntryValue());
 }

 // Testing getNoEntryValue method
 @Test
 public void testGetNoEntryValue() {
 charHash = new TCharHash(11);
 charHash.setNoEntryValue('A');
 assertEquals('A', charHash.getNoEntryValue());
 }

 // Testing setUp method
 @Test
 public void testSetUp() {
 charHash = new TCharHash(11);
 assertEquals(11, charHash.size());
 }

 // Testing contains method
 @Test
 public void testContains() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 assertTrue(charHash.contains('A'));
 assertTrue(charHash.contains('B'));
 assertTrue(charHash.contains('C'));
 assertTrue(charHash.contains('D'));
 }

 // Testing forEach method
 @Test
 public void testForEach() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals('A', charValue);
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 // Testing removeAt method
 @Test
 public void testRemoveAt() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 charHash.removeAt(0);
 assertEquals(3, charHash.size());
 }

 // Testing index method with present key
 @Test
 public void testIndexWithPresentKey() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 int index = charHash.index('B');
 assertEquals(1, index);
 }

 // Testing index method with absent key
 @Test
 public void testIndexWithAbsentKey() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 int index = charHash.index('E');
 assertEquals(-1, index);
 }

 // Testing insertKey method with present key
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithPresentKey() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 charHash.insertKey('B');
 fail("Expected IllegalStateException");
 }

 // Testing insertKey method with absent key
 @Test
 public void testInsertKeyWithAbsentKey() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 charHash.insertKey('E');
 assertEquals(5, charHash.size());
 }

 // Testing indexRehashed method with absent key
 @Test
 public void testIndexRehashedWithAbsentKey() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 int index = charHash.indexRehashed('E', 0, 1, (byte) 0);
 assertEquals(-1, index);
 }

 // Testing insertKeyRehash method with absent key
 @Test
 public void testInsertKeyRehashWithAbsentKey() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 charHash.insertKeyRehash('E', 0, 1, (byte) 0);
 assertEquals(5, charHash.size());
 }

 // Testing insertKeyRehash method with present key
 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithPresentKey() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 charHash.put('E', 'E');
 charHash.insertKeyRehash('E', 0, 1, (byte) 1);
 fail("Expected IllegalStateException");
 }

 // Testing consumeFreeSlot flag
 @Test
 public void testConsumeFreeSlotFlag() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 charHash.setConsumeFreeSlot(false);
 charHash.put('E', 'E');
 charHash.setConsumeFreeSlot(true);
 charHash.put('F', 'F');
 assertEquals(7, charHash.size());
 }

}