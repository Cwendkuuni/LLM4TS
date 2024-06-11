package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

import java.util.Arrays;

public class TCharHashTest {

 private TCharHash charHash;

 @BeforeClass
 public static void setUpBeforeClass() {
 System.out.println("Setting up before class...");
 }

 @AfterClass
 public static void tearDownAfterClass() {
 System.out.println("Tearing down after class...");
 }

 @Before
 public void setUp() {
 System.out.println("Setting up test environment...");
 charHash = new TCharHash(11);
 }

 @After
 public void tearDown() {
 System.out.println("Tearing down test environment...");
 charHash = null;
 }

 @Test
 public void testTCharHashConstruction() {
 TCharHash charHash1 = new TCharHash();
 TCharHash charHash2 = new TCharHash(10);
 TCharHash charHash3 = new TCharHash(10, 0.75f);
 TCharHash charHash4 = new TCharHash(10, 0.75f, 'A');
 
 assertNotNull(charHash1);
 assertNotNull(charHash2);
 assertNotNull(charHash3);
 assertNotNull(charHash4);
 }

 @Test
 public void testGetNoEntryValue() {
 char noEntryValue = charHash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, noEntryValue);
 }

 @Test
 public void testSetUp() {
 int capacity = charHash.setUp(11);
 assertEquals(11, capacity);
 }

 @Test
 public void testContains() {
 charHash.put((char) 1, (Object) 1);
 assertTrue(charHash.contains((char) 1));
 assertFalse(charHash.contains((char) 2));
 }

 @Test
 public void testForEach() {
 charHash.put((char) 1, (Object) 1);
 charHash.put((char) 2, (Object) 2);
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 assertEquals((char) 1, character);
 return true;
 }
 };
 assertTrue(charHash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 charHash.put((char) 1, (Object) 1);
 int index = 0;
 charHash.removeAt(index);
 fail("RemoveAt operation should throw an UnsupportedOperationException");
 }

 @Test
 public void testIndex() {
 charHash.put((char) 1, (Object) 1);
 charHash.put((char) 2, (Object) 2);
 int index1 = charHash.index((char) 1);
 int index2 = charHash.index((char) 2);
 int index3 = charHash.index((char) 3);
 assertEquals(0, index1);
 assertEquals(-1, index2);
 assertEquals(-1, index3);
 }

 @Test
 public void testInsertKey() {
 charHash.put((char) 1, (Object) 1);
 charHash.put((char) 2, (Object) 2);
 charHash.put((char) 3, (Object) 3);
 char newChar = 'D';
 int index = charHash.insertKey(newChar);
 assertEquals(4, charHash.size());
 assertEquals(3, index);
 assertEquals(newChar, charHash.get((char) 3));
 }

 @Test
 public void testInsertKeyRehash() {
 charHash.put((char) 1, (Object) 1);
 charHash.put((char) 2, (Object) 2);
 charHash.put((char) 3, (Object) 3);
 charHash.put((char) 4, (Object) 4);
 charHash.put((char) 5, (Object) 5);
 charHash.put((char) 6, (Object) 6);
 charHash.put((char) 7, (Object) 7);
 charHash.put((char) 8, (Object) 8);
 charHash.put((char) 9, (Object) 9);
 charHash.put((char) 10, (Object) 10);
 char newChar = 'E';
 int index = charHash.insertKey(newChar);
 assertEquals(11, charHash.size());
 assertEquals(10, index);
 assertEquals(newChar, charHash.get((char) 10));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 for (int i = 0; i < charHash.size(); i++) {
 charHash.put((char) i, (Object) i);
 }
 char newChar = 'F';
 int index = charHash.insertKey(newChar);
 fail("NoFreeOrRemovedSlotsAvailable exception should be thrown");
 }

}