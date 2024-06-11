import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;
 private short[] expected;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 @Test
 public void testTShortHashConstruction() {
 TShortHash hash = new TShortHash();
 assertNotNull(hash);
 assertEquals(0, hash.size());

 hash = new TShortHash(10);
 assertNotNull(hash);
 assertEquals(0, hash.size());

 hash = new TShortHash(10, 0.75f);
 assertNotNull(hash);
 assertEquals(0, hash.size());

 hash = new TShortHash(10, 0.75f, (short) 123);
 assertNotNull(hash);
 assertEquals(0, hash.size());
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TShortHash(5);
 assertEquals(5, hash.size());
 assertNull(hash.getNoEntryValue());

 expected = new short[5];
 assertArrayEquals(expected, hash._set);
 }

 @Test
 public void testContains() {
 hash.put((short) 1);
 hash.put((short) 2);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertFalse(hash.contains((short) 3));
 }

 @Test
 public void testForEach() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 short[] result = new short[3];
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short short1) {
 result[hash.size()] = short1;
 return true;
 }
 };
 hash.forEach(procedure);
 assertArrayEquals(new short[]{1, 2, 3}, result);
 }

 @Test
 public void testRemoveAt() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 assertEquals(3, hash.size());
 hash.removeAt(1);
 assertEquals(2, hash.size());
 assertFalse(hash.contains((short) 2));
 }

 @Test
 public void testIndex() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 1);

 assertEquals(0, hash.index((short) 1));
 assertEquals(1, hash.index((short) 2));
 assertEquals(-1, hash.index((short) 3));
 }

 @Test
 public void testInsertKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);
 hash.put((short) 1);

 assertEquals(5, hash.size());
 assertEquals(1, hash.index((short) 1));
 }
}