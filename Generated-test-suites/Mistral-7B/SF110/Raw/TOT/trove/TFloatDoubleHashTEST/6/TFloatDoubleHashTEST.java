import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hashMap;

 @Before
 public void setup() {
 hashMap = new TFloatDoubleHash(5, 0.75f);
 }

 @Test
 public void testEmptyHashMap() {
 assertTrue(hashMap.isEmpty());
 assertFalse(hashMap.containsKey(0.0f));
 assertEquals(0, hashMap.size());
 }

 @Test
 public void testAddKeyValuePair() {
 hashMap.put(1.0f, 1.0);
 assertTrue(hashMap.containsKey(1.0f));
 assertEquals(1.0, hashMap.get(1.0f), 0.0);
 assertEquals(1, hashMap.size());
 }

 @Test
 public void testAddDuplicateKeyValuePair() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(1.0f, 2.0);
 assertEquals(1.0, hashMap.get(1.0f), 0.0);
 assertEquals(1, hashMap.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddKeyValuePairWithFullHashMap() {
 for (int i = 0; i < hashMap.capacity(); i++) {
 hashMap.put((float) i, (double) i);
 }
 hashMap.put(++i, 0.0);
 }

 @Test
 public void testRemoveKey() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.remove(1.0f);
 assertFalse(hashMap.containsKey(1.0f));
 assertEquals(1, hashMap.size());
 }

 @Test
 public void testContainsKey() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 assertTrue(hashMap.containsKey(1.0f));
 assertFalse(hashMap.containsKey(3.0f));
 }

 @Test
 public void testForEachProcedure() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 add(key);
 return true;
 }
 };
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.forEach(procedure);
 assertEquals(3, addCount);
 }

 @Test
 public void testWriteAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 hashMap.writeExternal(out);
 byte[] byteArray = byteStream.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteArray));
 TFloatDoubleHash hashMapCopy = (TFloatDoubleHash) in.readObject();
 assertEquals(hashMap.size(), hashMapCopy.size());
 for (float key : hashMap.keySet()) {
 assertEquals(hashMap.get(key), hashMapCopy.get(key), 0.0);
 }
 }

}