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
 public void testInitialCapacityConstructor() {
 TShortHash hash = new TShortHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash hash = new TShortHash(5, 0.75f);
 assertEquals(5, hash.size());
 assertEquals(0.75f, hash.loadFactor(), 0.01f);
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TShortHash hash = new TShortHash(5, 0.75f, (short) 42);
 assertEquals(5, hash.size());
 assertEquals(0.75f, hash.loadFactor(), 0.01f);
 assertEquals((short) 42, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 assertTrue(hash.contains((short) 1));
 assertFalse(hash.contains((short) 2));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 fail("Should not be called when containing keys 1 and 2");
 return true;
 }
 };
 hash.forEach(procedure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachWithUnsupportedOperationException() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.forEach(new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 throw new UnsupportedOperationException();
 }
 });
 }

 @Test
 public void testRemoveAt() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.removeAt(0);
 assertFalse(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(0, hash.index((short) 1));
 assertEquals(-1, hash.index((short) 3));
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(3, hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHashTable() {
 TShortHash hash = new TShortHash(5);
 for (short i = 0; i < hash.size() * 2; i++) {
 hash.put((short) i);
 }
 }
}