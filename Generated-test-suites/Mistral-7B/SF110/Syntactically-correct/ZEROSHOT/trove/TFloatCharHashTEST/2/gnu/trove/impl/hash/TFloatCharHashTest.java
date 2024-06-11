import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 @Test
 public void testNoEntryKeyAndValue() {
 float noEntryKey = hash.getNoEntryKey();
 char noEntryValue = hash.getNoEntryValue();
 assertEquals(0.0f, noEntryKey, 0.001f);
 assertEquals('\0', noEntryValue);
 }

 @Test
 public void testInitialization() {
 assertNull(hash._set);
 assertEquals(0, hash.size());
 }

 @Test
 public void testInitialCapacityConstructor() {
 hash = new TFloatCharHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testInitialCapacityLoadFactorConstructor() {
 hash = new TFloatCharHash(5, 0.75f);
 assertEquals(5, hash.size());
 }

 @Test
 public void testInitialCapacityLoadFactorNoEntryValuesConstructor() {
 float noEntryKey = 0.0f;
 char noEntryValue = '\0';
 hash = new TFloatCharHash(5, 0.75f, noEntryKey, noEntryValue);
 assertEquals(5, hash.size());
 assertEquals(noEntryKey, hash.getNoEntryKey(), 0.001f);
 assertEquals(noEntryValue, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TFloatCharHash(5);
 assertEquals(5, hash.size());
 assertNull(hash._set);
 hash.setUp(5);
 assertNotNull(hash._set);
 }

 @Test
 public void testContains() {
 hash.put(1.0f, 'a');
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testContainsFalse() {
 hash.put(1.0f, 'a');
 assertFalse(hash.contains(2.0f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 1.0f, 0.001f);
 return true;
 }
 };
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.forEach(procedure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 hash.put(1.0f, 'a');
 hash.removeAt(-1);
 }

 @Test
 public void testIndex() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 int index = hash.index(1.0f);
 assertEquals(0, index);
 index = hash.index(2.0f);
 assertEquals(1, index);
 index = hash.index(3.0f);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexRehashed() {
 float key = 1.0f;
 float newKey = 1.5f;
 int length = 10;
 TFloatCharHash hash = new TFloatCharHash(length);
 hash.insertKey(key);
 hash.insertKey(newKey);
 int index = hash.indexRehashed(key, 0, (int) HashFunctions.hash(key) & Integer.MAX_VALUE, (byte) 0);
 assertEquals(0, index);
 index = hash.indexRehashed(newKey, 0, (int) HashFunctions.hash(newKey) & Integer.MAX_VALUE, (byte) 0);
 assertEquals(1, index);
 }

 @Test
 public void testInsertKey() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 int index = hash.insertKey(3.0f);
 assertEquals(3, hash.size());
 assertEquals(3.0f, hash._set[index], 0.001f);
 assertEquals('\0', hash._set[index - 1]);
 assertEquals(1, hash._states[index - 1]);
 assertEquals(0, hash._states[index]);
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 hash.writeExternal(out);
 byte[] bytes = baos.toByteArray();
 TFloatCharHash hashFromBytes = new TFloatCharHash();
 hashFromBytes.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(hash.getNoEntryKey(), hashFromBytes.getNoEntryKey(), 0.001f);
 assertEquals(hash.getNoEntryValue(), hashFromBytes.getNoEntryValue());
 assertEquals(hash.size(), hashFromBytes.size());
 for (int i = 0; i < hash.size(); i++) {
 assertEquals(hash.keyAt(i), hashFromBytes.keyAt(i), 0.001f);
 }
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 TFloatCharHash hashToWrite = new TFloatCharHash();
 hashToWrite.put(1.0f, 'a');
 hashToWrite.put(2.0f, 'b');
 out.writeObject(hashToWrite);
 baos.writeTo(System.out);
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TFloatCharHash hashFromBytes = (TFloatCharHash) in.readObject();
 assertEquals(hashToWrite.getNoEntryKey(), hashFromBytes.getNoEntryKey(), 0.001f);
 assertEquals(hashToWrite.getNoEntryValue(), hashFromBytes.getNoEntryValue());
 assertEquals(hashToWrite.size(), hashFromBytes.size());
 for (int i = 0; i < hashToWrite.size(); i++) {
 assertEquals(hashToWrite.keyAt(i), hashFromBytes.keyAt(i), 0.001f);
 }
 }

}