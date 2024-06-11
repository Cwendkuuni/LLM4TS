import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

 private TCharHash charHash;
 private char key;

 @Before
 public void setUp() {
 charHash = new TCharHash(5);
 key = 'a';
 }

 @Test
 public void testConstruction() {
 TCharHash charHash = new TCharHash();
 assertNotNull(charHash.getNoEntryValue());
 charHash = new TCharHash(10);
 charHash = new TCharHash(10, 0.75f);
 charHash = new TCharHash(10, 0.75f, 'z');
 }

 @Test
 public void testGetNoEntryValue() {
 char hashNoEntryValue = charHash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hashNoEntryValue);
 }

 @Test
 public void testSetUp() {
 charHash = new TCharHash(5);
 int capacity = charHash.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 charHash.put(key);
 assertTrue(charHash.contains(key));
 }

 @Test
 public void testForEach() {
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 return false;
 }
 };
 charHash.put(key);
 charHash.forEach(procedure);
 assertFalse(procedure.wasInvoked());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAtWithIndex() {
 charHash.put(key);
 int index = charHash.indexOf(key);
 charHash.removeAt(index);
 }

 @Test
 public void testIndex() {
 charHash.put(key);
 int index = charHash.indexOf(key);
 assertEquals(0, index);
 }

 @Test
 public void testIndexWithEmptyHash() {
 int index = charHash.indexOf(key);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 charHash.put(key);
 int index = charHash.insertKey(key);
 assertEquals(0, index);
 }

 @Test
 public void testInsertKeyWithAlreadyExistingKey() {
 charHash.put(key);
 int index = charHash.insertKey(key);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKeyRehash() {
 charHash.put(key);
 int index = charHash.insertKey(key);
 assertEquals(-1, index);
 charHash.insertKey('e');
 charHash.insertKey('i');
 charHash.insertKey('o');
 charHash.insertKey('u');
 charHash.insertKey('a');
 charHash.insertKey(key);
 int index2 = charHash.indexOf(key);
 assertEquals(3, index2);
 }
}