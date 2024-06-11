import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.TShortProcedure;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.impl.hash.Constants;

public class TShortHashTest {

 private TShortHash hash;
 private short key;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hash = new TShortHash(5);
 key = (short) 5;
 }

 @Test
 public void testTShortHashConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());

 hash = new TShortHash(10);
 assertEquals(10, hash.capacity());
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());

 hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01);
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());

 hash = new TShortHash(10, 0.75f, (short) 100);
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.01);
 assertEquals((short) 100, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 hash = new TShortHash(5);
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());

 hash = new TShortHash(5, (short) 111);
 assertEquals((short) 111, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 byte[] states = hash._states;
 short[] set = hash._set;
 assertEquals(5, states.length);
 assertEquals(5, set.length);
 Arrays.fill(set, Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(5);
 assertFalse(hash.contains((short) 5));

 hash.put((short) 5, (short) 5);
 assertTrue(hash.contains((short) 5));

 hash.remove((short) 5);
 assertFalse(hash.contains((short) 5));
 }

 @Test
 public void testForEach() {
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals(value, key);
 return true;
 }
 };
 TShortHash hash = new TShortHash(5);
 hash.put((short) 5, (short) 5);
 hash.put((short) 6, (short) 6);
 hash.put((short) 7, (short) 7);
 hash.put((short) 8, (short) 8);
 hash.put((short) 9, (short) 9);
 hash.forEach(procedure);
 hash.remove((short) 5);
 hash.forEach(procedure);
 hash.clear();
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 5, (short) 5);
 hash.put((short) 6, (short) 6);
 hash.put((short) 7, (short) 7);
 hash.put((short) 8, (short) 8);
 hash.put((short) 9, (short) 9);

 assertEquals(5, hash.size());
 hash.removeAt(0);
 assertEquals(4, hash.size());

 hash.clear();
 hash.put((short) 5, (short) 5);
 hash.put((short) 6, (short) 6);
 hash.put((short) 7, (short) 7);
 hash.put((short) 8, (short) 8);
 hash.put((short) 9, (short) 9);

 assertEquals(5, hash.size());
 hash.removeAt(2);
 assertEquals(4, hash.size());
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 5, (short) 5);
 hash.put((short) 6, (short) 6);
 hash.put((short) 7, (short) 7);
 hash.put((short) 8, (short) 8);
 hash.put((short) 9, (short) 9);

 assertEquals(5, hash.index((short) 5));
 assertEquals(-1, hash.index((short) 10));
 }

 @Test
 public void testIndexRehashed() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 5, (short) 5);
 hash.put((short) 6, (short) 6);
 hash.put((short) 7, (short) 7);
 hash.put((short) 8, (short) 8);
 hash.put((short) 9, (short) 9);
 hash.put((short) 10, (short) 10);

 assertEquals(5, hash.index((short) 5));
 assertEquals(-1, hash.index((short) 11));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHashTable() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 5, (short) 5);
 hash.put((short) 6, (short) 6);
 hash.put((short) 7, (short) 7);
 hash.put((short) 8, (short) 8);
 hash.put((short) 9, (short) 9);
 hash.put((short) 10, (short) 10);

 try {
 hash.put((short) 11, (short) 11);
 fail();
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testInsertKeyRehash() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 5, (short) 5);
 hash.put((short) 6, (short) 6);
 hash.put((short) 7, (short) 7);
 hash.put((short) 8, (short) 8);
 hash.put((short) 9, (short) 9);
 hash.put((short) 10, (short) 10);

 int index = hash.insertKey((short) 11);
 assertEquals(index, -6);
 }

 @Test
 public void testInsertKeyAt() {
 TShortHash hash = new TShortHash(5);
 hash._set = new short[10];
 hash._states = new byte[10];
 hash.setUp(10);
 hash.insertKeyAt(5, (short) 5);
 assertEquals(5, hash.index((short) 5));
 }
}