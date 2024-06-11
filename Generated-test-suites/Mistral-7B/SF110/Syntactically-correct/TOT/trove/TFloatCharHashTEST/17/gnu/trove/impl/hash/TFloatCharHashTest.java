import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

 private TFloatCharHash<Float, Character> hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatCharHash<>(5, 0.75f);
 }

 @Test
 public void testDefaultConstructor() {
 TFloatCharHash<Float, Character> defaultHash = new TFloatCharHash<>();
 assertEquals(0, defaultHash.size());
 }

 @Test
 public void testInitialCapacityConstructor() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(10, 0.75f);
 assertEquals(10, hash.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndNoEntryValuesConstructor() {
 TFloatCharHash<Float, Character> hash = new TFloatCharHash<>(10, 0.75f, 0.0f, '\u0000');
 assertEquals(10, hash.size());
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatCharHash<Float, Character> hashMap = new TFloatCharHash<>(5, 0.75f);
 float noEntryKey = hashMap.getNoEntryKey();
 assertEquals(0.0f, noEntryKey);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatCharHash<Float, Character> hashMap = new TFloatCharHash<>(5, 0.75f);
 char noEntryValue = hashMap.getNoEntryValue();
 assertEquals('\u0000', noEntryValue);
 }

 @Test
 public void testSetUp() {
 TFloatCharHash<Float, Character> hashMap = new TFloatCharHash<>(5);
 int capacity = hashMap.setUp(5);
 assertEquals(5, capacity);
 }

 @Test
 public void testContains() {
 TFloatCharHash<Float, Character> hashMap = new TFloatCharHash<>(5);
 hashMap.put(1.0f, 'A');
 hashMap.put(2.0f, 'B');
 hashMap.put(3.0f, 'C');

 assertTrue(hashMap.contains(1.0f));
 assertTrue(hashMap.contains(2.0f));
 assertTrue(hashMap.contains(3.0f));

 assertFalse(hashMap.contains(4.0f));
 }

 @Test
 public void testForEach() {
 TFloatCharHash<Float, Character> hashMap = new TFloatCharHash<>(5);
 hashMap.put(1.0f, 'A');
 hashMap.put(2.0f, 'B');
 hashMap.put(3.0f, 'C');

 Character result = '\u0000';
 boolean passed = hashMap.forEach(new TFloatProcedure<Float, Character>() {
 @Override
 public boolean execute(Float key, Character value) {
 result = value;
 return true;
 }
 });

 assertEquals('\u0000', result);
 assertTrue(passed);
 }

 @Test
 public void testRemoveAt() {
 TFloatCharHash<Float, Character> hashMap = new TFloatCharHash<>(5);
 hashMap.put(1.0f, 'A');
 hashMap.put(2.0f, 'B');
 hashMap.put(3.0f, 'C');

 hashMap.removeAt(1);
 assertEquals(2, hashMap.size());
 assertFalse(hashMap.containsKey(1.0f));
 }

 @Test
 public void testIndex() {
 TFloatCharHash<Float, Character> hashMap = new TFloatCharHash<>(5);
 hashMap.put(1.0f, 'A');
 hashMap.put(2.0f, 'B');
 hashMap.put(3.0f, 'C');

 assertEquals(1, hashMap.index(1.0f));
 assertEquals(-1, hashMap.index(4.0f));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWhenFull() {
 TFloatCharHash<Float, Character> hashMap = new TFloatCharHash<>(1, 1.0f);
 hashMap.put(0.0f, '\u0000');
 hashMap.put(1.0f, 'A');
 hashMap.put(2.0f, 'B');
 hashMap.put(3.0f, 'C');
 hashMap.put(4.0f, 'D');
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 TFloatCharHash<Float, Character> hashMap = new TFloatCharHash<>(5);
 hashMap.put(1.0f, 'A');
 hashMap.put(2.0f, 'B');
 hashMap.put(3.0f, 'C');
 hashMap.writeExternal(objectOutput);
 byte[] byteArray = byteArrayOutputStream.toByteArray();

 TFloatCharHash<Float, Character> deserializedHashMap = new TFloatCharHash<>();
 deserializedHashMap.readExternal(new ObjectInput(new ByteArrayInputStream(byteArray)));

 assertEquals(3, deserializedHashMap.size());
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[]{(byte) 0});
 ObjectInput objectInput = new ObjectInput(byteArrayInputStream);
 TFloatCharHash<Float, Character> hashMap = new TFloatCharHash<>();
 hashMap.readExternal(objectInput);

 assertEquals(0, hashMap.size());
 }
}