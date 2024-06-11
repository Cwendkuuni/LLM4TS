import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash(5, 0.75f);
 }

 @Test
 public void testEmptyHashMap() {
 assertFalse(hashMap.contains(0.0f));
 assertFalse(hashMap.contains(1.0f));
 assertFalse(hashMap.forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return false;
 }
 }));
 }

 @Test
 public void testPutAndGet() {
 hashMap.put(1.0f, 1.0);
 assertTrue(hashMap.contains(1.0f));
 assertEquals(1.0, hashMap.get(1.0f), 0.0);
 }

 @Test
 public void testPutAndGetWithLoadFactor() {
 hashMap = new TFloatDoubleHash(5, 0.75f);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.put(4.0f, 4.0);
 hashMap.put(5.0f, 5.0);
 hashMap.put(6.0f, 6.0); // this will cause the hash map to rehash

 assertTrue(hashMap.contains(1.0f));
 assertTrue(hashMap.contains(2.0f));
 assertTrue(hashMap.contains(3.0f));
 assertTrue(hashMap.contains(4.0f));
 assertTrue(hashMap.contains(5.0f));
 assertTrue(hashMap.contains(6.0f));

 assertEquals(1.0, hashMap.get(1.0f), 0.0);
 assertEquals(2.0, hashMap.get(2.0f), 0.0);
 assertEquals(3.0, hashMap.get(3.0f), 0.0);
 assertEquals(4.0, hashMap.get(4.0f), 0.0);
 assertEquals(5.0, hashMap.get(5.0f), 0.0);
 assertEquals(6.0, hashMap.get(6.0f), 0.0);
 }

 @Test
 public void testRemove() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.remove(2.0f);
 assertFalse(hashMap.contains(2.0f));
 }

 @Test(expected = IllegalStateException.class)
 public void testFullHashMap() {
 hashMap = new TFloatDoubleHash(5, 0.75f);
 for (float i = 0.0f; i < 6.0f; i++) {
 hashMap.put(i, i);
 }
 }

 @Test
 public void testForEachProcedure() {
 int count = 0;
 hashMap = new TFloatDoubleHash(5, 0.75f);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.put(4.0f, 4.0);
 hashMap.put(5.0f, 5.0);
 hashMap.forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 count++;
 return true;
 }
 });
 assertEquals(5, count);
 }

 @Test(expected = NullPointerException.class)
 public void testWriteExternalWithNullObjectOutput() {
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5, 0.75f);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 try {
 hashMap.writeExternal(null);
 } catch (IOException e) {
 fail("Expected NullPointerException, but got IOException: " + e.getMessage());
 }
 }

 @Test
 public void testWriteExternalAndReadBack() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 TFloatDoubleHash hashMap = new TFloatDoubleHash(5, 0.75f);
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TFloatDoubleHash hashMapReadBack = (TFloatDoubleHash) in.readObject();
 assertEquals(hashMap, hashMapReadBack);
 }
}