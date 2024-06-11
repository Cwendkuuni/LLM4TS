import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 charHash = new TCharHash(5);
 }

 @Test
 public void testTCharHashConstructor() {
 TCharHash charHash = new TCharHash();
 assertNotNull(charHash.getNoEntryValue());
 TCharHash charHash1 = new TCharHash(10);
 assertNotNull(charHash1.getNoEntryValue());
 TCharHash charHash2 = new TCharHash(10, 0.75f);
 assertNotNull(charHash2.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 int capacity = charHash.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testGetNoEntryValue() {
 char defaultNoEntryValue = charHash.getNoEntryValue();
 assertEquals('\u0000', defaultNoEntryValue);
 char customNoEntryValue = '\u0001';
 charHash = new TCharHash(10, 0.75f, customNoEntryValue);
 assertEquals(customNoEntryValue, charHash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 char key = 'a';
 charHash.insertKey(key);
 assertTrue(charHash.contains(key));
 }

 @Test
 public void testContainsFalse() {
 char key = 'b';
 assertFalse(charHash.contains(key));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertKeyWithNull() {
 charHash.insertKey((char) null);
 }

 @Test
 public void testForEach() {
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char charValue) {
 assertEquals('a', charValue);
 return true;
 }
 };
 charHash.insertKey('a');
 charHash.insertKey('b');
 charHash.forEach(procedure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
 charHash.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 char key = 'a';
 charHash.insertKey(key);
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
 char key = 'a';
 charHash.insertKey(key);
 int index = charHash.indexOf(key);
 assertEquals(index, 0);
 }

 @Test
 public void testIndexRehashed() {
 char key = 'z';
 charHash.insertKey('a');
 charHash.insertKey('b');
 charHash.insertKey('c');
 charHash.insertKey(key);
 int index = charHash.indexOf(key);
 assertEquals(index, charHash._set.length - 1);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithNoFreeOrRemovedSlots() {
 for (int i = 0; i < charHash._set.length; i++) {
 charHash.insertKey((char) ('a' + i));
 }
 }
}