import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
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
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 hash = new TShortHash(10);
 assertEquals(10, hash.size());
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 hash = new TShortHash(15, 0.75f);
 assertEquals(15, hash.size());
 assertNull(hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 hash = new TShortHash(20, 0.75f, (short) 42);
 assertEquals(20, hash.size());
 assertEquals((short) 42, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 assertEquals((short) 0, hash.getNoEntryValue());
 hash.put((short) 0, (short) 0);
 assertEquals((short) 0, hash.getNoEntryValue());
 hash.put((short) 42, (short) 42);
 assertEquals((short) 42, hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertFalse(hash.contains((short) 3));
 }

 @Test
 public void testForEach() {
 int count = 0;
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 count++;
 return true;
 }
 };
 hash.forEach(procedure);
 assertEquals(3, count);
 }

 @Test
 public void testRemoveAt() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 int index = hash.indexOf((short) 2);
 hash.removeAt(index);
 assertEquals(2, hash.size());
 assertFalse(hash.contains((short) 2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 hash = new TShortHash(-5);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 hash = new TShortHash(5, -0.5f);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testPut() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 assertEquals(3, hash.size());
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 }

 @Test
 public void testPutAndGet() {
 hash.put((short) 1, (short) 1);
 assertEquals((short) 1, hash.get((short) 1));
 }

 @Test
 public void testPutAndContains() {
 hash.put((short) 1, (short) 1);
 assertTrue(hash.contains((short) 1));
 hash.put((short) 2, (short) 2);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 }

 @Test
 public void testPutAndSize() {
 hash.put((short) 1, (short) 1);
 assertEquals(1, hash.size());
 hash.put((short) 2, (short) 2);
 assertEquals(2, hash.size());
 }

 @Test
 public void testPutAndContainsAll() {
 Short[] values = {(short) 1, (short) 2, (short) 3};
 hash.putAll(Arrays.asList(values));
 assertEquals(3, hash.size());
 for (Short value : values) {
 assertTrue(hash.contains(value));
 }
 }

 @Test
 public void testClear() {
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.clear();
 assertEquals(0, hash.size());
 assertFalse(hash.contains((short) 1));
 assertFalse(hash.contains((short) 2));
 }

 @After
 public void tearDown() {
 hash = null;
 }

}