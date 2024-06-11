package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertEquals(0, hash.size());
 assertFalse(hash.contains(Float.NaN));
 }

 @Test
 public void testBasicHashOperations() {
 final float key1 = 1.0f;
 final char value1 = 'A';
 final float key2 = 2.0f;
 final char value2 = 'B';

 hash.put(key1, value1);
 hash.put(key2, value2);

 assertEquals(2, hash.size());
 assertTrue(hash.contains(key1));
 assertTrue(hash.contains(key2));
 assertEquals(value1, hash.get(key1));
 assertEquals(value2, hash.get(key2));

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return hash.contains(key);
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRehashing() {
 final int initialCapacity = 5;
 final int newCapacity = 10;
 final float key = 1.5f;
 final char value = 'C';

 hash = new TFloatCharHash(initialCapacity);
 hash.put(key, value);
 assertEquals(1, hash.size());

 hash = new TFloatCharHash(newCapacity);
 hash.putAll(hash);
 assertEquals(1, hash.size());
 assertTrue(hash.contains(key));
 }

 @Test
 public void testSerialization() throws Exception {
 ObjectOutput out = new ByteArrayOutputStream().objOutputStream();
 hash.writeExternal(out);
 ObjectInput in = new ByteArrayInputStream(out.toByteArray());
 TFloatCharHash cloned = new TFloatCharHash();
 cloned.readExternal(in);

 assertEquals(hash.size(), cloned.size());
 for (float key : hash.keySet()) {
 assertEquals(hash.get(key), cloned.get(key));
 }
 }

 @After
 public void tearDown() {
 hash = null;
 }
}