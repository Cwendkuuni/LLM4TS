import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

 private TFloatCharHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatCharHash(5);
 }

 @Test
 public void testConstructor() {
 assertEquals(5, hashMap.size());
 assertNull(hashMap.getNoEntryKey());
 assertNull(hashMap.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryKey() {
 hashMap.put(0.0f, '\u0000');
 assertEquals(0.0f, hashMap.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 hashMap.put(0.0f, '\u0000');
 assertEquals('\u0000', hashMap.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hashMap = new TFloatCharHash(10);
 assertEquals(10, hashMap.capacity());
 assertNull(hashMap.getNoEntryKey());
 assertNull(hashMap.getNoEntryValue());
 }

 @Test
 public void testContains() {
 hashMap.put(0.0f, '\u0000');
 assertTrue(hashMap.contains(0.0f));
 }

 @Test
 public void testContainsNullKey() {
 hashMap.put(0.0f, '\u0000');
 assertFalse(hashMap.contains(null));
 }

 @Test
 public void testContainsNonFloatKey() {
 hashMap.put(0.0f, '\u0000');
 assertFalse(hashMap.contains('A'));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 0.0f, 0.0f);
 return true;
 }
 };
 hashMap.put(0.0f, '\u0000');
 hashMap.put(1.1f, 'A');
 hashMap.forEach(procedure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachConcurrentModification() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 hashMap.remove(value);
 return true;
 }
 };
 hashMap.put(0.0f, '\u0000');
 hashMap.put(1.1f, 'A');
 hashMap.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 hashMap.put(0.0f, '\u0000');
 hashMap.put(1.1f, 'A');
 int index = hashMap.index(0.0f);
 hashMap.removeAt(index);
 assertEquals(1, hashMap.size());
 assertNull(hashMap.get(0.0f));
 }

 @Test
 public void testIndex() {
 hashMap.put(0.0f, '\u0000');
 hashMap.put(1.1f, 'A');
 int index = hashMap.index(1.1f);
 assertEquals(1, index);
 }

 @Test
 public void testIndexWithCollision() {
 hashMap.put(0.0f, '\u0000');
 hashMap.put(1.1f, 'A');
 hashMap.put(1.1f, 'B');
 int index = hashMap.index(1.1f);
 assertEquals(1, index);
 }

 @Test(expected = IllegalStateException.class)
 public void testIndexWithEmptyMap() {
 hashMap.index(0.0f);
 }

 @Test
 public void testInsertKey() {
 hashMap.put(0.0f, '\u0000');
 assertFalse(hashMap.containsKey(1.1f));
 hashMap.put(1.1f, 'B');
 assertTrue(hashMap.containsKey(1.1f));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullMap() {
 hashMap = new TFloatCharHash(5);
 hashMap.put(0.0f, '\u0000');
 hashMap.put(1.1f, 'A');
 hashMap.put(2.2f, 'B');
 hashMap.put(3.3f, 'C');
 hashMap.put(4.4f, 'D');
 hashMap.put(5.5f, 'E');
 try {
 hashMap.put(6.6f, 'F');
 } catch (IllegalStateException e) {
 assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
 }
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 hashMap.writeExternal(objectOutput);
 byte[] bytes = byteArrayOutputStream.toByteArray();
 TFloatCharHash hashMap2 = new TFloatCharHash(10);
 hashMap2.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(hashMap, hashMap2);
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 TFloatCharHash hashMap2 = new TFloatCharHash(10);
 hashMap2.writeExternal(objectOutput);
 byte[] bytes = byteArrayOutputStream.toByteArray();
 ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(bytes));
 hashMap2.readExternal(objectInput);
 assertEquals(hashMap, hashMap2);
 }
}