import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

 private TFloatCharHash hash;
 private TFloatProcedure procedure;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return true;
 }
 };
 }

 @Test
 public void testConstructor() {
 TFloatCharHash hash = new TFloatCharHash();
 assertNotNull(hash);
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TFloatCharHash hash = new TFloatCharHash(10);
 assertNotNull(hash);
 assertEquals(10, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TFloatCharHash hash = new TFloatCharHash(15, 0.75f);
 assertNotNull(hash);
 assertEquals(15, hash.capacity());
 assertEquals(11, hash.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyAndNoEntryValue() {
 TFloatCharHash hash = new TFloatCharHash(20, 0.75f, 1.0f, 'A');;
 assertNotNull(hash);
 assertEquals(20, hash.capacity());
 assertEquals(11, hash.size());
 assertEquals(1.0f, hash.getNoEntryKey());
 assertEquals('A', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hash = new TFloatCharHash(5);
 assertEquals(5, hash.capacity());
 }

 @Test
 public void testContains() {
 hash.insertKey(1.0f);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testContainsFalse() {
 hash.insertKey(1.0f);
 assertFalse(hash.contains(2.0f));
 }

 @Test
 public void testForEach() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testForEachWithNullProcedure() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 try {
 hash.forEach(null);
 } catch (IllegalArgumentException e) {
 assertEquals("procedure", e.getMessage());
 }
 }

 @Test
 public void testRemoveAt() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.removeAt(0);
 assertEquals(1, hash.size());
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testIndex() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 assertEquals(0, hash.index(1.0f));
 }

 @Test
 public void testIndexWithNoEntryKey() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 assertEquals(-1, hash.index(3.0f));
 }

 @Test
 public void testInsertKey() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 assertEquals(2, hash.size());
 }

 @Test
 public void testInsertKeyRehash() {
 hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 hash.insertKey(4.0f);
 hash.insertKey(5.0f);
 hash.insertKey(6.0f);
 assertEquals(6, hash.size());
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 hash = new TFloatCharHash(5);
 hash.insertKey(1.0f);
 hash.writeExternal(out);
 out.close();
 byte[] bytes = byteStream.toByteArray();
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TFloatCharHash hashCopy = (TFloatCharHash) in.readObject();
 in.close();
 assertTrue(hash.contains(1.0f));
 assertTrue(hashCopy.contains(1.0f));
 }

}