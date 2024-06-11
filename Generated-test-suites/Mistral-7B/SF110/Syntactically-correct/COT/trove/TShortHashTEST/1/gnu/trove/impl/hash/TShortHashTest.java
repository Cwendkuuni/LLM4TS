import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 @Test
 public void testConstructorWithDefaults() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TShortHash hash = new TShortHash(5, 0.75f);
 assertEquals(5, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash hash = new TShortHash(5, 0.75f, (short) 10);
 assertEquals(10, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash(5);
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals((short) 1, value);
 return true;
 }
 };
 hash.forEach(procedure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAtNegativeIndex() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.removeAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBoundsException() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.removeAt(5);
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(1, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 3));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWhenTableIsFull() {
 TShortHash hash = new TShortHash(5);
 for (short i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {
 hash.put(i);
 }
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 short key = (short) 6;
 int index = hash.insertKey(key);
 assertEquals(6, hash.get(index));
 }

 @Test
 public void testInsertKeyRehash() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 short key = (short) 6;
 int index = hash.insertKeyRehash(key, 0, 0, (byte) 0);
 assertEquals(6, hash.get(index));
 }
}