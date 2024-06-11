import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Before
 public void setUp() {
 hash = new TFloatDoubleHash(5);
 }

 @After
 public void tearDown() {
 hash = null;
 }

 @Test
 public void testConstructor() {
 assertNotNull(hash);
 assertEquals(5, hash.size());
 }

 @Test
 public void testGetNoEntryKey() {
 assertEquals(0.0f, hash.getNoEntryKey(), 0.01f);
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(0.0, hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 assertEquals(5, hash.capacity());
 }

 @Test
 public void testContains() {
 assertFalse(hash.contains(1.0f));
 hash.put(1.0f, 1.0);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 assertEquals(key, hash.get(key), 0.01f);
 return true;
 }
 };
 hash.putAll(new TFloatObjectHashMap<Float, Double>() {{
 put(1.0f, 1.0);
 put(2.0f, 2.0);
 put(3.0f, 3.0);
 }});
 hash.forEachValue(procedure);
 }

 @Test
 public void testRemoveAt() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 int index = hash.index(2.0f);
 hash.removeAt(index);
 assertEquals(2, hash.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexNegative() {
 hash.put(1.0f, 1.0);
 int index = hash.index(-1.0f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIndexKeyNull() {
 hash.put(1.0f, 1.0);
 int index = hash.index(null);
 }

 @Test
 public void testInsertKey() {
 hash.putAll(new TFloatObjectHashMap<Float, Double>() {{
 put(1.0f, 1.0);
 put(2.0f, 2.0);
 put(3.0f, 3.0);
 }});
 assertEquals(3, hash.size());
 assertEquals(1.0f, hash.keyAt(0), 0.01f);
 assertEquals(1.0, hash.valueAt(0), 0.01);
 hash.put(4.0f, 4.0);
 assertEquals(4, hash.size());
 assertEquals(4.0f, hash.keyAt(3), 0.01f);
 assertEquals(4.0, hash.valueAt(3), 0.01);
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
 hash.writeExternal(objectOutputStream);
 byte[] bytes = outputStream.toByteArray();
 TFloatDoubleHash clonedHash = new TFloatDoubleHash();
 clonedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
 assertEquals(hash.size(), clonedHash.size());
 for (float key : hash.keySet()) {
 assertEquals(hash.get(key), clonedHash.get(key), 0.01f);
 }
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
 TFloatDoubleHash hashToWrite = new TFloatDoubleHash(5, 0.75f);
 hashToWrite.put(1.0f, 1.0);
 hashToWrite.put(2.0f, 2.0);
 hashToWrite.put(3.0f, 3.0);
 hashToWrite.writeExternal(objectOutputStream);
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
 TFloatDoubleHash clonedHash = (TFloatDoubleHash) objectInputStream.readObject();
 assertEquals(hashToWrite.size(), clonedHash.size());
 for (float key : hashToWrite.keySet()) {
 assertEquals(hashToWrite.get(key), clonedHash.get(key), 0.01f);
 }
 }
}