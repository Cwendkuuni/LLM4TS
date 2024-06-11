import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 hash = new TShortHash(11);
 }

 @Test
 public void testTShortHashConstructorWithEmptyArgument() {
 TShortHash hash = new TShortHash();
 assertEquals(0, hash.size());
 }

 @Test
 public void testTShortHashConstructorWithInitialCapacity() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testTShortHashConstructorWithInitialCapacityAndLoadFactor() {
 TShortHash hash = new TShortHash(11, 0.5f);
 assertEquals(11, hash.size());
 }

 @Test
 public void testTShortHashConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash hash = new TShortHash(11, 0.5f, (short) 127);
 assertEquals(11, hash.size());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 hash.setNoEntryValue((short) 123);
 assertEquals((short) 123, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(11);
 assertEquals(11, hash.size());
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertFalse(hash.contains((short) 0));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash(3);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 ShortProcedure procedure = new ShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 return shortValue % 2 == 0;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
 TShortHash hash = new TShortHash(3);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.forEach(null);
 }

 @Test
 public void testRemoveAt() {
 TShortHash hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 hash.removeAt(2);
 assertEquals(3, hash.size());
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 assertEquals(3, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 5));
 }

 @Test
 public void testIndexRehashed() {
 TShortHash hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 assertEquals(2, hash.indexRehashed((short) 1, 3, 1, (byte) 1));
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 hash.insert((short) 5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testInsertKeyRehash() {
 TShortHash hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 hash.insert((short) 5);
 hash.insert((short) 6);
 assertEquals(6, hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 TShortHash hash = new TShortHash(5);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 hash.insert((short) 5);
 hash.insert((short) 6);
 hash.insert((short) 7);
 }

 @Test
 public void testCustomNoEntryValue() {
 TShortHash hash = new TShortHash(5, (short) 123);
 hash.insert((short) 1);
 hash.insert((short) 2);
 hash.insert((short) 3);
 hash.insert((short) 4);
 hash.insert((short) 5);
 assertEquals(5, hash.size());
 assertEquals((short) 123, hash._set[5]);
 }

 class ShortProcedure implements TShortProcedure {
 @Override
 public boolean execute(short shortValue) {
 return true;
 }
 }
}