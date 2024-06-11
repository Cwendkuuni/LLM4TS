package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import gnu.trove.procedure.TCharProcedure;
import gnu.trove.impl.hash.TCharHash;

public class TCharHashTest {

 private TCharHash charHash;
 private char[] testData;
 private TCharProcedure procedure;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 charHash = new TCharHash(11);
 testData = new char[11];
 procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charToTest) {
 return charToTest > 64 && charToTest < 91; // check if character is an alphabetic uppercase
 }
 };
 for (int i = 0; i < testData.length; i++) {
 testData[i] = (char) (new Random().nextInt(26) + 'A');
 charHash.insert(testData[i]);
 }
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 for (char charToTest : testData) {
 assertTrue(charHash.contains(charToTest));
 }
 }

 @Test
 public void testForEach() {
 int countAlphabeticalUpperCase = 0;
 charHash.forEach(procedure);
 for (char charToTest : testData) {
 if (procedure.wasInvokedWith(charToTest)) {
 countAlphabeticalUpperCase++;
 }
 }
 assertEquals(testData.length, countAlphabeticalUpperCase);
 }

 @Test
 public void testRemoveAt() {
 char charToRemove = testData[0];
 charHash.removeAt(0);
 assertFalse(charHash.contains(charToRemove));
 }

 @Test
 public void testIndex() {
 for (int i = 0; i < testData.length; i++) {
 assertEquals(i, charHash.index(testData[i]));
 }
 assertEquals(-1, charHash.index((char) 0));
 }

 @Test
 public void testInsertKey() {
 char newKey = (char) (new Random().nextInt(26) + 'A');
 int index = charHash.insertKey(newKey);
 assertEquals(newKey, testData[index]);
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 for (char charToTest : testData) {
 charHash.insertKey(charToTest);
 }
 }
}