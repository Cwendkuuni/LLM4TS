import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Before
 public void setUp() {
 hash = new TFloatDoubleHash(5);
 }

 @Test
 public void testDefaultConstructor() {
 TFloatDoubleHash hash = new TFloatDoubleHash();
 assertEquals(0, hash.size());
 }

 @Test
 public void testInitialCapacityConstructor() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
 assertEquals(5, hash.size());
 assertEquals(0.75f, hash.getLoadFactor(), 0.01f);
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValuesConstructor() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f, 0.0f, 0.0);
 assertEquals(5, hash.size());
 assertEquals(0.75f, hash.getLoadFactor(), 0.01f);
 assertEquals(0.0f, hash.getNoEntryKey(), 0.01f);
 assertEquals(0.0, hash.getNoEntryValue(), 0.01);
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 assertEquals(0.0f, hash.getNoEntryKey(), 0.01f);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 assertEquals(0.0, hash.getNoEntryValue(), 0.01);
 }

 @Test
 public void testSetUp() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 assertEquals(5, hash.size());
 assertNotNull(hash._set);
 }

 @Test
 public void testContains() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 assertFalse(hash.contains(1.0f));
 hash.put(1.0f, 1.0);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testForEach() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float floatValue) {
 assertEquals(0.0f, floatValue, 0.01f);
 return true;
 }
 };
 hash.put(0.0f, 0.0);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.put(5.0f, 5.0);
 assertEquals(5, hash.size());
 hash.removeAt(1);
 assertEquals(4, hash.size());
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testIndex() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.put(5.0f, 5.0);
 assertEquals(1, hash.index(1.0f));
 assertEquals(-1, hash.index(6.0f));
 }

 @Test
 public void testInsertKey() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.put(5.0f, 5.0);
 hash.put(6.0f, 6.0);
 assertEquals(6, hash.size());
 assertEquals(1, hash.index(1.0f));
 assertEquals(6, hash.index(6.0f));
 assertFalse(hash.contains(7.0f));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.put(5.0f, 5.0);
 hash.put(6.0f, 6.0);
 hash.put(7.0f, 7.0);
 }
}