package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.*;

import org.junit.Test;

public class TFloatDoubleHashTest {

 @Test
 public void testEmptyHash() {
 TFloatDoubleHash hash = new TFloatDoubleHash(10);
 assertTrue(hash.isEmpty());
 }

 @Test
 public void testInitialization() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
 assertEquals(5, hash.size());
 assertNull(hash.getNoEntryValue());
 assertNotNull(hash.getNoEntryKey());
 assertEquals(0.0f, hash.getNoEntryKey());
 }

 @Test
 public void testBasicPutAndGet() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.put(5.0f, 5.0);

 assertEquals(5.0f, hash.get(5.0f));
 assertEquals(1.0, hash.get(1.0f));
 assertNull(hash.get(6.0f));

 assertEquals(5, hash.size());
 }

 @Test
 public void testPutDuplicateKey() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
 hash.put(1.0f, 1.0);
 hash.put(1.0f, 2.0);

 assertEquals(1.0f, hash.get(1.0f));
 assertEquals(2.0, hash.get(1.0f));
 }

 @Test(expected = IllegalStateException.class)
 public void testPutFullHash() {
 TFloatDoubleHash hash = new TFloatDoubleHash(3, 0.75f);
 for (float i = 0.0f; i < 4.0f; i++) {
 hash.put(i, i);
 }
 }

 @Test
 public void testRemoveKey() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.put(5.0f, 5.0);

 hash.remove(2.0f);
 assertEquals(5, hash.size());
 assertNull(hash.get(2.0f));
 }

 @Test
 public void testProcedureWithEmptyHash() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return false;
 }
 };

 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testProcedureWithKeyPresent() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.put(5.0f, 5.0);

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 if (value == 1.0f) {
 return false;
 }
 return true;
 }
 };

 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testProcedureWithKeyAbsent() {
 TFloatDoubleHash hash = new TFloatDoubleHash(5, 0.75f);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.put(5.0f, 5.0);

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 if (value == 6.0f) {
 return false;
 }
 return true;
 }
 };

 assertTrue(hash.forEach(procedure));
 }
}