import org.junit.After;
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
 public void testConstructor() {
 TFloatDoubleHash emptyHashMap = new TFloatDoubleHash();
 TFloatDoubleHash initialCapacityHashMap = new TFloatDoubleHash(10);
 TFloatDoubleHash initialCapacityAndLoadFactorHashMap = new TFloatDoubleHash(10, 0.75f);
 TFloatDoubleHash initialCapacityAndLoadFactorAndNoEntryValueHashMap = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0);

 assertEquals(0, emptyHashMap.size());
 assertEquals(10, initialCapacityHashMap.size());
 assertEquals(10, initialCapacityAndLoadFactorHashMap.size());
 assertEquals(10, initialCapacityAndLoadFactorAndNoEntryValueHashMap.size());
 }

 @Test
 public void testContains() {
 hashMap.put(0.1f, 1.0);
 assertTrue(hashMap.contains(0.1f));
 assertFalse(hashMap.contains(1.0f));
 assertFalse(hashMap.contains(0.2f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(0.1f, value, 0.01f);
 return true;
 }
 };
 hashMap.put(0.1f, 1.0);
 hashMap.put(0.2f, 2.0);
 hashMap.put(0.3f, 3.0);
 assertTrue(hashMap.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveAtWithNegativeIndex() {
 hashMap.put(0.1f, 1.0);
 hashMap.removeAt(-1);
 }

 @Test
 public void testIndex() {
 hashMap.put(0.1f, 1.0);
 hashMap.put(0.2f, 2.0);
 hashMap.put(0.3f, 3.0);
 assertEquals(0, hashMap.index(0.1f));
 assertEquals(1, hashMap.index(0.2f));
 assertEquals(2, hashMap.index(0.3f));
 assertEquals(-1, hashMap.index(0.4f));
 }

 @Test
 public void testInsertKey() {
 hashMap.put(0.1f, 1.0);
 hashMap.put(0.2f, 2.0);
 hashMap.put(0.3f, 3.0);
 hashMap.put(0.4f, 4.0);
 assertEquals(4, hashMap.size());
 assertEquals(-1, hashMap.insertKey(0.1f)); // Key already exists
 assertEquals(4, hashMap.size());
 assertEquals(0, hashMap.insertKey(0.5f));
 assertEquals(5, hashMap.size());
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 hashMap.writeExternal(objStream);
 byte[] bytes = byteStream.toByteArray();
 TFloatDoubleHash deserializedHashMap = new TFloatDoubleHash();
 deserializedHashMap.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));

 assertEquals(4, deserializedHashMap.size());
 assertEquals(0.1f, deserializedHashMap.get(0.1f), 0.01f);
 assertEquals(1.0, deserializedHashMap.get(0.1f), 0.01f);
 assertEquals(0.2f, deserializedHashMap.get(0.2f), 0.01f);
 assertEquals(2.0, deserializedHashMap.get(0.2f), 0.01f);
 assertEquals(0.3f, deserializedHashMap.get(0.3f), 0.01f);
 assertEquals(3.0, deserializedHashMap.get(0.3f), 0.01f);
 assertEquals(0.4f, deserializedHashMap.get(0.4f), 0.01f);
 assertEquals(4.0, deserializedHashMap.get(0.4f), 0.01f);
 assertEquals(0.5f, deserializedHashMap.get(0.5f), 0.01f);
 assertEquals(null, deserializedHashMap.getNoEntryValue());
 assertEquals(0.0f, deserializedHashMap.getNoEntryKey());
 }

 @After
 public void tearDown() {
 hashMap = null;
 }
}