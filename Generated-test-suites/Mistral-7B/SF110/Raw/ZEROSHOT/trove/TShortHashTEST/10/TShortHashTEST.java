package test.gnu.trove.impl.hash;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;
 private short[] expectedSet;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 expectedSet = new short[5];
 Arrays.fill(expectedSet, (short) 1);
 }

 @Test
 public void testDefaultConstructor() {
 TShortHash hash = new TShortHash();
 short noEntryValue = hash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TShortHash hash = new TShortHash(5);
 short noEntryValue = hash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
 assertEquals(5, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TShortHash hash = new TShortHash(5, 0.75f);
 short noEntryValue = hash.getNoEntryValue();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
 assertEquals(4, hash.initialCapacity());
 assertEquals(3, (int) Math.ceil(5f * 0.75f));
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryValue() {
 TShortHash hash = new TShortHash(5, 0.75f, (short) 2);
 short noEntryValue = hash.getNoEntryValue();
 assertEquals((short) 2, noEntryValue);
 assertEquals(4, hash.initialCapacity());
 assertEquals(3, (int) Math.ceil(5f * 0.75f));
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(5);
 int capacity = hash.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);

 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));
 assertTrue(hash.contains((short) 4));
 assertTrue(hash.contains((short) 5));

 assertFalse(hash.contains((short) 6));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);

 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 expectedSet[hash.indexOf(value)] = value;
 return true;
 }
 };

 hash.forEach(procedure);

 assertArrayEquals(expectedSet, hash._set);
 }

 @Test
 public void testRemoveAt() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);

 int index = 2;
 hash.removeAt(index);

 assertEquals(4, hash.size());
 assertFalse(hash.contains((short) 3));
 assertEquals((short) 1, hash.get(0));
 assertEquals((short) 4, hash.get(1));
 assertEquals((short) 5, hash.get(3));
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash(5);
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 hash.put((short) 4);
 hash.put((short) 5);

 assertEquals(0, hash.indexOf((short) 1));
 assertEquals(1, hash.indexOf((short) 2));
 assertEquals(2, hash.indexOf((short) 3));
 assertEquals(-1, hash.indexOf((short) 6));
 }
}