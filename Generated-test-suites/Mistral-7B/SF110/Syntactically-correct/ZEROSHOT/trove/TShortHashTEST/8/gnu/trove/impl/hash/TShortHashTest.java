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
 public void testConstructorWithInitialCapacityAndLoadFactorAndNoEntryValue() {
 TShortHash hash = new TShortHash(5, 0.75f, (short) 10);
 assertEquals(5, hash.size());
 assertEquals((short) 10, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 hash.setNoEntryValue((short) 5);
 assertEquals((short) 5, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.capacity());
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 return value != ((short) 2);
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.removeAt(1);
 assertEquals(2, hash.size());
 assertFalse(hash.contains((short) 2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexWithNegativeHashCode() {
 TShortHash hash = new TShortHash(5);
 int hashCode = -1;
 int index = hash.index((short) 1, hashCode);
 fail();
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.put((short) 1, (short) 10);
 int index = hash.index((short) 1);
 assertEquals(1, index);
 }

 @Test
 public void testIndexRehashed() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1, (short) 1);
 hash.put((short) 2, (short) 2);
 hash.put((short) 3, (short) 3);
 hash.put((short) 1, (short) 10);
 int index = hash.indexRehashed((short) 1, 1, 1, (byte) 1);
 assertEquals(1, index);
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash(5);
 int index = hash.insertKey((short) 1);
 assertEquals(1, index);
 }

 @Test
 public void testInsertKeyAt() {
 TShortHash hash = new TShortHash(5);
 hash.insertKeyAt(0, (short) 1);
 assertEquals(1, hash.size());
 assertEquals((short) 1, hash.get(0));
 }
}