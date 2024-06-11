import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.*;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hashMap;

 @Before
 public void setup() {
 hashMap = new TFloatDoubleHash(5, 0.75f);
 }

 @Test
 public void testConstructors() {
 TFloatDoubleHash emptyHashMap = new TFloatDoubleHash();
 TFloatDoubleHash initialCapacityHashMap = new TFloatDoubleHash(10);
 TFloatDoubleHash initialCapacityAndLoadFactorHashMap = new TFloatDoubleHash(10, 0.75f);
 TFloatDoubleHash initialCapacityAndLoadFactorAndKeysValuesHashMap = new TFloatDoubleHash(10, 0.75f, 0.1f, 0.2);

 assertEquals(0, emptyHashMap.size());
 assertEquals(10, initialCapacityHashMap.capacity());
 assertEquals(0.75f, initialCapacityAndLoadFactorHashMap.loadFactor(), 0.01f);
 }

 @Test
 public void testGetNoEntryKeyAndValue() {
 assertEquals(0.0f, hashMap.getNoEntryKey());
 assertEquals(0.0, hashMap.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hashMap = new TFloatDoubleHash(5);
 assertEquals(5, hashMap.capacity());
 }

 @Test
 public void testContains() {
 hashMap.put(1.0f, 1.0);
 assertTrue(hashMap.contains(1.0f));
 hashMap.remove(1.0f);
 assertFalse(hashMap.contains(1.0f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, value, 0.0f);
 return true;
 }
 };

 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.forEach(procedure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 hashMap.put(1.0f, 1.0);
 int index = hashMap.indexOf(1.0f);
 hashMap.removeAt(index);
 }

 @Test
 public void testIndex() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 int index = hashMap.indexOf(1.0f);
 assertEquals(index, 0);
 index = hashMap.indexOf(4.0f);
 assertEquals(index, -1);
 }

 @Test
 public void testInsertKey() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.put(4.0f, 4.0);
 hashMap.put(5.0f, 5.0);

 assertEquals(5, hashMap.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyRehashWithFullKeySet() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 hashMap.put(4.0f, 4.0);
 hashMap.put(5.0f, 5.0);
 hashMap.put(6.0f, 6.0);
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 hashMap.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TFloatDoubleHash clonedHashMap = (TFloatDoubleHash) in.readObject();

 assertEquals(hashMap.size(), clonedHashMap.size());
 }
}