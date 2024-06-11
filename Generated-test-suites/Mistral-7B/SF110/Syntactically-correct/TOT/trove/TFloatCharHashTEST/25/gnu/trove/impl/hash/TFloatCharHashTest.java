import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatCharHash(5);
 }

 @Test
 public void testEmptyMap() {
 assertFalse(hashMap.contains(0.0f));
 assertFalse(hashMap.forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return false;
 }
 }));
 }

 @Test
 public void testPutAndGet() {
 hashMap.put(1.0f, 'A');
 assertEquals(1.0f, hashMap.get(1.0f), 0.0f);
 }

 @Test
 public void testPutAndGetWithLoadFactor() {
 hashMap = new TFloatCharHash(5, 0.75f);
 hashMap.put(1.0f, 'A');
 hashMap.put(2.0f, 'B');
 assertEquals(1.0f, hashMap.get(1.0f), 0.0f);
 assertEquals(2.0f, hashMap.get(2.0f), 0.0f);
 }

 @Test
 public void testPutAndGetWithInitialCapacityAndLoadFactor() {
 hashMap = new TFloatCharHash(10, 0.75f);
 hashMap.put(1.0f, 'A');
 hashMap.put(2.0f, 'B');
 assertEquals(1.0f, hashMap.get(1.0f), 0.0f);
 assertEquals(2.0f, hashMap.get(2.0f), 0.0f);
 }

 @Test
 public void testPutWithNoEntryKeyAndValue() {
 hashMap = new TFloatCharHash(5, 0.0f, '\0', '\0');
 hashMap.put(1.0f, 'A');
 assertEquals(1.0f, hashMap.get(1.0f), 0.0f);
 }

 @Test
 public void testPutWithNoEntryKeyAndValueWithLoadFactor() {
 hashMap = new TFloatCharHash(5, 0.0f, '\0', '\0', 0.75f);
 hashMap.put(1.0f, 'A');
 assertEquals(1.0f, hashMap.get(1.0f), 0.0f);
 }

 @Test
 public void testPutWithNoEntryKeyAndValueWithInitialCapacityAndLoadFactor() {
 hashMap = new TFloatCharHash(10, 0.0f, '\0', '\0', 10, 0.75f);
 hashMap.put(1.0f, 'A');
 assertEquals(1.0f, hashMap.get(1.0f), 0.0f);
 }

 @Test
 public void testContains() {
 hashMap.put(1.0f, 'A');
 assertTrue(hashMap.contains(1.0f));
 }

 @Test
 public void testContainsWithNoEntryValue() {
 hashMap = new TFloatCharHash(5, '\u0001');
 hashMap.put(1.0f, 'A');
 assertFalse(hashMap.contains(1.0f));
 }

 @Test
 public void testForEach() {
 byte[] data = new byte[hashMap.size()];
 int index = 0;
 hashMap.forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 data[index++] = (byte) ((int) value % 256);
 return true;
 }
 });
 for (int i = 0; i < data.length; i++) {
 assertEquals((int) hashMap.keyAt(i), data[i] & 0xFF);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testFullMap() {
 hashMap = new TFloatCharHash(5, 0.75f);
 for (float i = 0.0f; i < 6.0f; i += 0.1f) {
 hashMap.put(i, (char) i);
 }
 }

 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(outputStream);
 TFloatCharHash mapToWrite = new TFloatCharHash(5, 0.75f, 1.0f, 'A');
 out.writeObject(mapToWrite);
 out.close();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
 TFloatCharHash mapToRead = (TFloatCharHash) in.readObject();
 in.close();
 assertEquals(mapToWrite, mapToRead);
 }

}