import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash shortHash;
 private TShortProcedure procedure;

 @Before
 public void setUp() {
 shortHash = new TShortHash(5);
 procedure = new TShortProcedure() {
 @Override
 public boolean execute(short shortValue) {
 return shortValue == testValue;
 }
 };
 }

 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(0, hash.getNoEntryValue());
 }

 @Test
 public void testInitialCapacityConstructor() {
 TShortHash hash = new TShortHash(3);
 assertEquals(3, hash.size());
 }

 @Test
 public void testLoadFactorConstructor() {
 TShortHash hash = new TShortHash(6, 0.75f);
 assertEquals(8, hash.size());
 }

 @Test
 public void testCustomNoEntryValueConstructor() {
 TShortHash hash = new TShortHash(6, 0.75f, (short) 123);
 assertEquals(123, hash.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryValue() {
 shortHash = new TShortHash(5);
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, shortHash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 shortHash = new TShortHash(3);
 assertEquals(3, shortHash.size());
 }

 @Test
 public void testContains() {
 shortHash.put((short) 1);
 assertTrue(shortHash.contains((short) 1));
 }

 @Test
 public void testDoesNotContain() {
 shortHash.put((short) 1);
 assertFalse(shortHash.contains((short) 2));
 }

 @Test
 public void testForEach() {
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 shortHash.put((short) 3);
 shortHash.forEach(procedure);
 assertTrue(shortHash.contains((short) 1));
 assertTrue(shortHash.contains((short) 2));
 assertTrue(shortHash.contains((short) 3));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 shortHash.put((short) 1);
 shortHash.removeAt(0);
 }

 @Test
 public void testIndex() {
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 assertEquals(1, shortHash.index((short) 1));
 assertEquals(-1, shortHash.index((short) 0));
 }

 @Test
 public void testIndexRehashed() {
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 shortHash.put((short) 3);
 assertEquals(-1, shortHash.index((short) 4));
 }

 @Test
 public void testInsertKey() {
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 shortHash.put((short) 3);
 shortHash.put((short) 4);
 assertEquals(4, shortHash.size());
 }

 @Test
 public void testInsertKeyAt() {
 shortHash.put((short) 1);
 shortHash.put((short) 2);
 shortHash.put((short) 3);
 shortHash.put((short) 4);
 shortHash.insertKeyAt(2, (short) 100);
 assertEquals(5, shortHash.size());
 }
}