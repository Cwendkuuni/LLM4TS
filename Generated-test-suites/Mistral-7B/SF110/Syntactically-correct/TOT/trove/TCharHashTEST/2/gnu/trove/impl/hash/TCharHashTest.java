import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;
 private char[] testData = new char[]{'a', 'b', 'c', 'd', 'e'};

 @Before
 public void setUp() {
 charHash = new TCharHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertEquals(0, charHash.size());
 assertFalse(charHash.contains('a'));
 }

 @Test
 public void testInitialCapacity() {
 charHash = new TCharHash(5, 0.75f);
 for (char c : testData) {
 charHash.put(c);
 }
 for (char c : testData) {
 assertTrue(charHash.contains(c));
 }
 }

 @Test
 public void testInitialCapacityLoadFactor() {
 charHash = new TCharHash(5, 0.75f, 'x');
 for (char c : testData) {
 charHash.put(c);
 }
 for (char c : testData) {
 assertTrue(charHash.contains(c));
 }
 }

 @Test
 public void testInvalidLoadFactor() {
 try {
 charHash = new TCharHash(5, -0.5f, 'x');
 fail();
 } catch (IllegalArgumentException e) {
 assertTrue(true);
 }
 }

 @Test
 public void testGetNoEntryValue() {
 charHash = new TCharHash(5);
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 charHash = new TCharHash(5);
 assertEquals(5, charHash.size());
 }

 @Test
 public void testContains() {
 charHash = new TCharHash(5);
 for (char c : testData) {
 charHash.put(c);
 }
 for (char c : testData) {
 assertTrue(charHash.contains(c));
 }
 }

 @Test
 public void testForEach() {
 charHash = new TCharHash(5);
 for (char c : testData) {
 charHash.put(c);
 }
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals(charValue, testData[indexOfChar(charValue)]);
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 charHash = new TCharHash(5);
 for (char c : testData) {
 charHash.put(c);
 }
 for (int i = 0; i < testData.length; i++) {
 charHash.removeAt(i);
 assertEquals(testData.length - i - 1, charHash.size());
 }
 }

 @Test
 public void testIndex() {
 charHash = new TCharHash(5);
 for (char c : testData) {
 charHash.put(c);
 }
 for (char c : testData) {
 assertEquals(indexOfChar(c), charHash.index(c));
 }
 }

 @Test
 public void testIndexRehashed() {
 charHash = new TCharHash(5);
 for (char c : testData) {
 charHash.put(c);
 }
 charHash.put('f');
 int index = charHash.index('f');
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 charHash = new TCharHash(5);
 charHash.put('a');
 int index = charHash.insertKey('b');
 assertEquals(1, index);
 }

 @Test
 public void testInsertKeyRehash() {
 charHash = new TCharHash(5);
 for (char c : testData) {
 charHash.put(c);
 }
 charHash.put('f');
 int index = charHash.insertKey('g');
 assertTrue(index < 0);
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 charHash = new TCharHash(5);
 for (char c : testData) {
 charHash.put(c);
 }
 charHash.put('g');
 }

 private int indexOfChar(char c) {
 for (int i = 0; i < testData.length; i++) {
 if (testData[i] == c) {
 return i;
 }
 }
 return -1;
 }
}