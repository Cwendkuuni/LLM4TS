package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import gnu.trove.TShortHash;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.impl.hash.TShortHashConstants;

public class TShortHashTest {

 @Test
 public void testEmptyHash() {
 TShortHash hash = new TShortHash(0);
 assertEquals(0, hash.size());
 assertFalse(hash.contains((short) 5));
 }

 @Test
 public void testInitialCapacityHash() {
 TShortHash hash = new TShortHash(5);
 short[] values = { 1, 2, 3, 4, 5 };
 for (short value : values) {
 hash.put(value);
 }
 for (short value : values) {
 assertEquals(value, hash.get(value));
 }
 assertEquals(values.length, hash.size());
 }

 @Test
 public void testLoadFactorHash() {
 TShortHash hash = new TShortHash(5, 0.75f);
 short[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
 for (short value : values) {
 hash.put(value);
 }
 assertEquals(11, hash.size());
 }

 @Test
 public void testNoEntryValueHash() {
 TShortHash hash = new TShortHash(5, 0.75f, (short) -128);
 short[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
 for (short value : values) {
 hash.put(value);
 }
 for (short value : values) {
 assertEquals((short) -128, hash.get((short) -128));
 assertEquals(value, hash.get(value));
 }
 assertEquals(values.length, hash.size());
 }

 @Test
 public void testPutGetRemoveHash() {
 TShortHash hash = new TShortHash(5, 0.75f);
 short[] values = { 1, 2, 3, 4, 5 };
 for (short value : values) {
 hash.put(value);
 assertEquals(value, hash.get(value));
 }
 for (int i = 0; i < values.length; i++) {
 hash.remove(values[i]);
 assertEquals(values.length - i - 1, hash.size());
 assertFalse(hash.contains(values[i]));
 }
 }

 @Test
 public void testForEachHash() {
 TShortHash hash = new TShortHash(5, 0.75f);
 short[] values = { 1, 2, 3, 4, 5 };
 for (short value : values) {
 hash.put(value);
 }
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertEquals(value, hash.get(value));
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 new TShortHash(-5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 new TShortHash(5, -0.75f);
 }

 @Test(expected = NullPointerException.class)
 public void testNullValues() {
 short[] values = null;
 new TShortHash(5).put((short) 5, values);
 }
}