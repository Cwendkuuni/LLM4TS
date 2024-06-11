import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TCharHashTest {

 private TCharHash charHash;
 private char key;

 @Before
 public void setUp() {
 charHash = new TCharHash(11);
 key = 'a';
 }

 @Test
 public void testEmptyConstructor() {
 TCharHash emptyCharHash = new TCharHash();
 assertTrue(emptyCharHash.isEmpty());
 }

 @Test
 public void testInitialCapacityConstructor() {
 TCharHash charHash = new TCharHash(10);
 assertFalse(charHash.isEmpty());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TCharHash charHash = new TCharHash(10, 0.75f);
 assertFalse(charHash.isEmpty());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 char hashNoEntryValue = '\u0001';
 TCharHash charHash = new TCharHash(10, 0.75f, hashNoEntryValue);
 assertFalse(charHash.isEmpty());
 assertSame(hashNoEntryValue, charHash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 int capacity = charHash.setUp(11);
 assertEquals(11, capacity);
 }

 @Test
 public void testContains() {
 charHash.put(key);
 assertTrue(charHash.contains(key));
 }

 @Test
 public void testDoesNotContain() {
 assertFalse(charHash.contains('b'));
 }

 @Test(expected = IllegalStateException.class)
 public void testPutWhenFull() {
 for (int i = 0; i < charHash.size(); i++) {
 charHash.put(key);
 }
 }

 @Test
 public void testForEach() {
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char character) {
 assertEquals(key, character);
 return true;
 }
 };
 charHash.put(key);
 charHash.forEach(procedure);
 }

 @Test(expected = NullPointerException.class)
 public void testForEachWithNullProcedure() {
 charHash.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 int index = charHash.indexOf(key);
 charHash.removeAt(index);
 assertFalse(charHash.contains(key));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWithInvalidIndex() {
 charHash.removeAt(-1);
 }

 @Test
 public void testIndex() {
 charHash.put(key);
 int index = charHash.indexOf(key);
 assertEquals(0, index);
 }

 @Test
 public void testIndexRehashed() {
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 charHash.put('d');
 int index = charHash.indexOf('b');
 assertEquals(1, index);
 }

 @Test
 public void testInsertKey() {
 int index = charHash.insertKey(key);
 assertEquals(0, index);
 }

 @Test
 public void testInsertKeyRehash() {
 charHash.put('a');
 charHash.put('b');
 charHash.put('c');
 charHash.put('d');
 int index = charHash.insertKey(key);
 assertEquals(4, index);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWhenFull() {
 for (int i = 0; i < charHash.size(); i++) {
 charHash.put(key);
 }
 }

}