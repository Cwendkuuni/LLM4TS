import org.junit.After;
import org.junit.Before;
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
 public void testDefaultConstructor() {
 assertEquals(5, hash.size());
 assertNotNull(hash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 hash = new TShortHash(10);
 assertEquals(10, hash.size());
 assertNotNull(hash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 hash = new TShortHash(15, 0.75f);
 assertEquals(11, hash.size()); // initial capacity rounded up
 assertNotNull(hash.getNoEntryValue());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 hash = new TShortHash(20, 0.8f, (short) 123);
 assertEquals(21, hash.size()); // initial capacity rounded up
 assertSame((short) 123, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 assertEquals(hash.size(), hash._set.length);
 }

 @Test
 public void testContains() {
 hash.put((short) 1);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testContainsFalse() {
 hash.put((short) 1);
 assertFalse(hash.contains((short) 2));
 }

 @Test
 public void testForEach() {
 short count = 0;
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 count++;
 return true;
 }
 };
 hash.forEach(procedure);
 assertEquals(3, count);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachConcurrentModification() {
 short count = 0;
 hash.put((short) 1);
 hash.put((short) 2);
 hash.forEach(new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 count++;
 hash.put((short) 4);
 return true;
 }
 });
 }

 @Test
 public void testRemoveAt() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(2, hash.size());
 hash.removeAt(0);
 assertEquals(1, hash.size());
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(0, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 3));
 }

 @Test
 public void testInsertKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(3, hash.size());
 assertEquals(0, hash.index((short) 1));
 assertEquals(2, hash.index((short) 3));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHash() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5); // hash is full
 }

 @After
 public void tearDown() {
 hash = null;
 }
}