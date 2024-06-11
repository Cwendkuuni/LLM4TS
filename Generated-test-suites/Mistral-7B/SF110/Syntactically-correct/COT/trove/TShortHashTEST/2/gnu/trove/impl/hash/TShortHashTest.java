import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash shortHash;

 @Before
 public void setUp() {
 shortHash = new TShortHash(5);
 }

 @Test
 public void testConstructor() {
 TShortHash shortHash = new TShortHash();
 assertNotNull(shortHash);
 shortHash = new TShortHash(10);
 assertNotNull(shortHash);
 shortHash = new TShortHash(10, 0.75f);
 assertNotNull(shortHash);
 shortHash = new TShortHash(10, 0.75f, (short) 1);
 assertNotNull(shortHash);
 }

 @Test
 public void testGetNoEntryValue() {
 short expected = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
 short actual = shortHash.getNoEntryValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetUp() {
 int capacity = shortHash.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 short val = (short) 1;
 shortHash.put(val);
 assertTrue(shortHash.contains(val));
 }

 @Test
 public void testContainsFalse() {
 short val = (short) 2;
 assertFalse(shortHash.contains(val));
 }

 @Test
 public void testForEach() {
 int count = 0;
 shortHash.forEach(new TShortProcedure() {
 @Override
 public boolean execute(short short1) {
 count++;
 return true;
 }
 });
 assertEquals(0, count);
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 count = 0;
 shortHash.forEach(new TShortProcedure() {
 @Override
 public boolean execute(short short1) {
 count++;
 return true;
 }
 });
 assertEquals(2, count);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAtThrowsException() {
 shortHash.removeAt(-1);
 }

 @Test
 public void testIndex() {
 short val = (short) 1;
 shortHash.put(val);
 int index = shortHash.index(val);
 assertEquals(0, index);
 }

 @Test
 public void testIndexNegative() {
 short val = (short) 1;
 shortHash.put(val);
 int index = shortHash.index((short) -1);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexNonExistent() {
 short val = (short) 2;
 int index = shortHash.index(val);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKey() {
 short val = (short) 1;
 int index = shortHash.insertKey(val);
 assertEquals(0, index);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
 shortHash.put((short) i);
 }
 throw new IllegalStateException("No free or removed slots available. Key set full?!!");
 }

 @Test
 public void testInsertKeyRehash() {
 short val = (short) 1;
 int index = shortHash.insertKey(val);
 assertEquals(0, index);
 shortHash.put((short) 2);
 int index2 = shortHash.insertKey(val);
 assertNotEquals(0, index2);
 }

 @Test
 public void testInsertKeyAt() {
 short val = (short) 1;
 int index = shortHash.insertKey(val);
 assertEquals(0, index);
 shortHash.put((short) 2);
 shortHash.put((short) 3);
 shortHash.insertKeyAt(1, val);
 assertEquals(1, index);
 }

}