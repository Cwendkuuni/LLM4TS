import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash floatCharHash;

 @Before
 public void setUp() {
 floatCharHash = new TFloatCharHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertTrue(floatCharHash.isEmpty());
 assertEquals(0, floatCharHash.size());
 }

 @Test
 public void testAddAndGet() {
 float key = 0.1f;
 char value = 'A';
 floatCharHash.put(key, value);
 assertTrue(floatCharHash.containsKey(key));
 assertEquals(value, floatCharHash.get(key));
 }

 @Test
 public void testAddAndGetWithLoadFactor() {
 floatCharHash = new TFloatCharHash(3, 0.75f);
 float key = 0.1f;
 char value = 'A';
 floatCharHash.put(key, value);
 assertTrue(floatCharHash.containsKey(key));
 assertEquals(value, floatCharHash.get(key));
 }

 @Test
 public void testAddAndGetWithInitialCapacityAndLoadFactor() {
 floatCharHash = new TFloatCharHash(4, 0.8f);
 float key = 0.1f;
 char value = 'A';
 floatCharHash.put(key, value);
 assertTrue(floatCharHash.containsKey(key));
 assertEquals(value, floatCharHash.get(key));
 }

 @Test
 public void testAddAndGetWithInitialCapacityAndLoadFactorAndKey() {
 floatCharHash = new TFloatCharHash(5, 0.8f, 0.2f, 'B');
 float key = 0.1f;
 char value = 'A';
 floatCharHash.put(key, value);
 assertTrue(floatCharHash.containsKey(key));
 assertEquals(value, floatCharHash.get(key));
 }

 @Test
 public void testContains() {
 float key = 0.1f;
 char value = 'A';
 floatCharHash.put(key, value);
 assertTrue(floatCharHash.contains(key));
 }

 @Test
 public void testContainsWithEmptyHash() {
 floatCharHash = new TFloatCharHash();
 float key = 0.1f;
 assertFalse(floatCharHash.contains(key));
 }

 @Test
 public void testRemove() {
 float key = 0.1f;
 char value = 'A';
 floatCharHash.put(key, value);
 floatCharHash.remove(key);
 assertFalse(floatCharHash.containsKey(key));
 }

 @Test
 public void testRemoveWithNullKey() {
 floatCharHash.remove(null);
 assertTrue(floatCharHash.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithInvalidKey() {
 floatCharHash.remove(-1.5f);
 }

 @Test
 public void testForEach() {
 float key1 = 0.1f;
 char value1 = 'A';
 floatCharHash.put(key1, value1);

 float key2 = 0.2f;
 char value2 = 'B';
 floatCharHash.put(key2, value2);

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key, char value) {
 if (key == key1 && value == value1) {
 System.out.println("Found key: " + key + " value: " + value);
 return false;
 }
 return true;
 }
 };

 floatCharHash.forEach(procedure);
 }

 @Test
 public void testWriteReadExternal() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

 TFloatCharHash floatCharHashToWrite = new TFloatCharHash(5, 0.8f, 0.1f, 'C');
 objectOutputStream.writeObject(floatCharHashToWrite);
 objectOutputStream.close();

 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
 TFloatCharHash floatCharHashRead = (TFloatCharHash) objectInputStream.readObject();
 objectInputStream.close();

 assertTrue(floatCharHashRead.containsKey(0.1f));
 assertEquals('C', floatCharHashRead.getNoEntryValue());
 }

 @Test
 public void testWriteReadExternalWithEmptyHash() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

 TFloatCharHash emptyFloatCharHash = new TFloatCharHash();
 objectOutputStream.writeObject(emptyFloatCharHash);
 objectOutputStream.close();

 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
 TFloatCharHash emptyFloatCharHashRead = (TFloatCharHash) objectInputStream.readObject();
 objectInputStream.close();

 assertTrue(emptyFloatCharHashRead.isEmpty());
 }
}