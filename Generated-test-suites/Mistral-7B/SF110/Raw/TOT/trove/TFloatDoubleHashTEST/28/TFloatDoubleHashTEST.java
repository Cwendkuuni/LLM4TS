import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash floatHash;

 @Before
 public void setUp() {
 floatHash = new TFloatDoubleHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertFalse(floatHash.contains(0.0f));
 assertFalse(floatHash.contains(1.0f));
 }

 @Test
 public void testPutAndGet() {
 floatHash.put(0.0f, 1.0);
 assertTrue(floatHash.contains(0.0f));
 assertEquals(1.0, floatHash.get(0.0f), 0.0);
 }

 @Test
 public void testPutAndGetWithLoadFactor() {
 floatHash = new TFloatDoubleHash(5, 0.5f);
 for (int i = 0; i < 10; i++) {
 floatHash.put(i, i);
 }
 float value = 0.0f;
 for (int i = 0; i < 10; i++) {
 value = floatHash.get(i);
 assertEquals(i, value, 0.0);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testPutWhenHashIsFull() {
 for (int i = 0; i < floatHash.capacity(); i++) {
 floatHash.put(i, i);
 }
 floatHash.put(floatHash.capacity(), 0.0f);
 }

 @Test
 public void testRemove() {
 floatHash.put(0.0f, 1.0);
 floatHash.remove(0.0f);
 assertFalse(floatHash.containsKey(0.0f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 assertEquals(key, key, 0.0);
 return true;
 }
 };
 floatHash.put(1.0f, 1.0);
 floatHash.put(2.0f, 2.0);
 floatHash.put(3.0f, 3.0);
 floatHash.forEach(procedure);
 }

 @Test
 public void testWriteAndReadExternal() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 TFloatDoubleHash emptyHash = new TFloatDoubleHash();
 emptyHash.writeExternal(objectOutputStream);
 byte[] bytes = byteArrayOutputStream.toByteArray();
 TFloatDoubleHash readHash = new TFloatDoubleHash();
 ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(bytes));
 readHash.readExternal(objectInput);
 assertEquals(emptyHash, readHash);

 TFloatDoubleHash hashWithKeyValue = new TFloatDoubleHash(5, 0.5f);
 hashWithKeyValue.put(1.0f, 1.0);
 hashWithKeyValue.put(2.0f, 2.0);
 objectOutputStream = new ObjectOutputStream(new FileOutputStream("hash.dat"));
 hashWithKeyValue.writeExternal(objectOutputStream);
 objectInput = new ObjectInputStream(new FileInputStream("hash.dat"));
 TFloatDoubleHash readHashFromFile = new TFloatDoubleHash();
 readHashFromFile.readExternal(objectInput);
 assertEquals(hashWithKeyValue, readHashFromFile);
 }
}