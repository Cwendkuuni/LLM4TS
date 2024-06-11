package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Before
 public void setUp() {
 hash = new TFloatDoubleHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertEquals(0, hash.size());
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testBasicOperations() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 assertEquals(3, hash.size());
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 }

 @Test
 public void testRemove() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.remove(2.0f);
 assertEquals(2, hash.size());
 assertFalse(hash.contains(2.0f));
 }

 @Test
 public void testPutAndGet() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 assertEquals(3, hash.size());
 assertEquals(1.0, hash.get(1.0f), 0.0);
 assertEquals(2.0, hash.get(2.0f), 0.0);
 assertEquals(3.0, hash.get(3.0f), 0.0);
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 assertEquals(key, hash.get(key), 0.0);
 return true;
 }
 };
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testSerialization() throws Exception {
 TFloatDoubleHash hash = new TFloatDoubleHash(5);
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 oos.writeObject(hash);
 oos.close();
 TFloatDoubleHash deserializedHash = (TFloatDoubleHash) new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject();
 assertEquals(3, deserializedHash.size());
 assertTrue(deserializedHash.contains(1.0f));
 assertTrue(deserializedHash.contains(2.0f));
 assertTrue(deserializedHash.contains(3.0f));
 }

}