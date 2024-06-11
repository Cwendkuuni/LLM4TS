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
 assertEquals(5, hashMap.size());
 }

 @Test
 public void testGetNoEntryKey() {
 assertEquals(0.0f, hashMap.getNoEntryKey(), 0.01f);
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(0.0, hashMap.getNoEntryValue(), 0.01);
 }

 @Test
 public void testContainsEmptyMap() {
 assertFalse(hashMap.contains(0.5f));
 }

 @Test
 public void testContainsEntryPresent() {
 hashMap.put(1.0f, 1.0);
 assertTrue(hashMap.contains(1.0f));
 }

 @Test
 public void testContainsNullKey() {
 assertFalse(hashMap.contains((float) null));
 }

 @Test(expected = NullPointerException.class)
 public void testContainsNullValueProcedure() {
 hashMap.forEach(null);
 }

 @Test
 public void testForEachEmptyMap() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return false;
 }
 };
 assertFalse(hashMap.forEach(procedure));
 }

 @Test
 public void testForEachEntryPresent() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(1.0f, value, 0.01f);
 return true;
 }
 };
 hashMap.put(1.0f, 1.0);
 assertTrue(hashMap.forEach(procedure));
 }

 @Test
 public void testRemoveAtIndexOutOfBounds() {
 int index = hashMap.size() + 1;
 try {
 hashMap.removeAt(index);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testRemoveAtIndexPresent() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.removeAt(1);
 assertEquals(1, hashMap.size());
 assertFalse(hashMap.contains(1.0f));
 }

 @Test
 public void testIndexEmptyMap() {
 assertEquals(-1, hashMap.index(1.0f));
 }

 @Test
 public void testIndexEntryPresent() {
 hashMap.put(1.0f, 1.0);
 assertEquals(1, hashMap.index(1.0f));
 }

 @Test
 public void testIndexNullKey() {
 assertEquals(-1, hashMap.index((float) null));
 }

 @Test
 public void testIndexKeyNotPresent() {
 assertEquals(-1, hashMap.index(2.0f));
 }

 @Test
 public void testInsertKeyEmptyMap() {
 assertEquals(0, hashMap.insertKey(1.0f));
 assertEquals(1, hashMap.size());
 assertEquals(1.0f, hashMap.get(1.0f), 0.01f);
 }

 @Test
 public void testInsertKeyEntryAlreadyPresent() {
 hashMap.put(1.0f, 1.0);
 assertEquals(-1, hashMap.insertKey(1.0f));
 assertEquals(1, hashMap.size());
 assertFalse(hashMap.contains(1.0f));
 }

 @Test
 public void testInsertKeyIndexOutOfBounds() {
 int index = hashMap.size() + 1;
 try {
 hashMap.insertKeyAt(index, 1.0f);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testInsertKeyNullKey() {
 try {
 hashMap.insertKey((float) null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 hashMap.writeExternal(objStream);
 ObjectInputStream objStreamRead = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
 TFloatDoubleHash readHashMap = (TFloatDoubleHash) objStreamRead.readObject();
 assertEquals(hashMap.size(), readHashMap.size());
 for (float key : hashMap.keySet()) {
 assertEquals(hashMap.get(key), readHashMap.get(key), 0.01f);
 }
 }
}