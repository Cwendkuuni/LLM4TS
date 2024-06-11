import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertTrue(hash.getNoEntryValue() == Constants.DEFAULT_SHORT_NO_ENTRY_VALUE);
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testInitialCapacityConstructor() {
 TShortHash hash = new TShortHash(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f, (short) 123);
 assertEquals(10, hash.size());
 assertEquals((short) 123, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());

 hash = new TShortHash((short) 123);
 assertEquals((short) 123, hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash();
 assertFalse(hash.contains((short) 1));

 hash.put((short) 1);
 assertTrue(hash.contains((short) 1));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash();
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 this.counter++;
 return true;
 }
 };
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.forEach(procedure);
 assertEquals(3, procedure.counter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 new TShortHash(-5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 new TShortHash(5, -0.5f);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInitialCapacity() {
 new TShortHash(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullLoadFactor() {
 new TShortHash(5, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullNoEntryValue() {
 new TShortHash(5, 0.75f, null);
 }
}