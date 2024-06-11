import org.junit.Before;
import org.junit.Test;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 @Test
 public void testDefaultConstructor() {
 assertNotNull(hash);
 assertTrue(hash.isEmpty());
 }

 @Test
 public void testInitialCapacityConstructor() {
 assertNotNull(hash);
 assertFalse(hash.isEmpty());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 assertNotNull(hash);
 assertFalse(hash.isEmpty());
 }

 @Test
 public void testInitialCapacityLoadFactorAndNoEntryKeyAndNoEntryValueConstructor() {
 assertNotNull(hash);
 assertFalse(hash.isEmpty());
 }

 @Test
 public void testGetNoEntryKey() {
 TFloatCharHash hash = new TFloatCharHash();
 assertEquals(0.0f, hash.getNoEntryKey(), 0.01f);
 }

 @Test
 public void testGetNoEntryValue() {
 TFloatCharHash hash = new TFloatCharHash();
 assertEquals('\0', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 TFloatCharHash hash = new TFloatCharHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f, 'A');
 hash.insert(2.0f, 'B');
 hash.insert(3.0f, 'C');
 hash.insert(4.0f, 'D');
 hash.insert(5.0f, 'E');

 assertEquals(5, hash.size());
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertTrue(hash.contains(4.0f));
 assertTrue(hash.contains(5.0f));
 }

 @Test
 public void testContainsWithEmptyHash() {
 TFloatCharHash hash = new TFloatCharHash();
 assertFalse(hash.contains(1.0f));
 }

 @Test
 public void testContainsWithNonExistentKey() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f, 'A');
 hash.insert(2.0f, 'B');
 hash.insert(3.0f, 'C');
 hash.insert(4.0f, 'D');
 hash.insert(5.0f, 'E');

 assertFalse(hash.contains(6.0f));
 }

 @Test
 public void testForEach() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f, 'A');
 hash.insert(2.0f, 'B');
 hash.insert(3.0f, 'C');
 hash.insert(4.0f, 'D');
 hash.insert(5.0f, 'E');

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return value > 3.0f;
 }
 };

 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testForEachWithEmptyHash() {
 TFloatCharHash hash = new TFloatCharHash();
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return value > 3.0f;
 }
 };

 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f, 'A');
 hash.insert(2.0f, 'B');
 hash.insert(3.0f, 'C');
 hash.insert(4.0f, 'D');
 hash.insert(5.0f, 'E');
 hash.removeAt(2);
 assertEquals(4, hash.size());
 assertFalse(hash.contains(2.0f));
 assertFalse(hash.contains('B'));
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(3.0f));
 assertTrue(hash.contains(4.0f));
 assertTrue(hash.contains(5.0f));
 }

 @Test
 public void testRemoveAtWithIndexOutOfBounds() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f, 'A');
 hash.insert(2.0f, 'B');
 hash.insert(3.0f, 'C');
 hash.insert(4.0f, 'D');
 hash.insert(5.0f, 'E');

 try {
 hash.removeAt(-1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testRemoveAtWithEmptyHash() {
 TFloatCharHash hash = new TFloatCharHash();
 try {
 hash.removeAt(-1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testIndex() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f, 'A');
 hash.insert(2.0f, 'B');
 hash.insert(3.0f, 'C');
 hash.insert(4.0f, 'D');
 hash.insert(5.0f, 'E');

 assertEquals(2, hash.indexOf(2.0f));
 assertEquals(-1, hash.indexOf(6.0f));
 }

 @Test
 public void testIndexWithEmptyHash() {
 TFloatCharHash hash = new TFloatCharHash();
 assertEquals(-1, hash.indexOf(1.0f));
 }

 @Test
 public void testIndexWithNonExistentKey() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f, 'A');
 hash.insert(2.0f, 'B');
 hash.insert(3.0f, 'C');
 hash.insert(4.0f, 'D');
 hash.insert(5.0f, 'E');

 assertEquals(-1, hash.indexOf(6.0f));
 }

 @Test
 public void testInsertKey() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f, 'A');
 hash.insert(2.0f, 'B');
 hash.insert(3.0f, 'C');
 hash.insert(4.0f, 'D');
 hash.insert(5.0f, 'E');

 assertEquals(5, hash.size());
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertTrue(hash.contains(4.0f));
 assertTrue(hash.contains(5.0f));

 assertEquals(2, hash.indexOf(2.0f));
 assertEquals(-1, hash.indexOf(6.0f));
 }

 @Test
 public void testInsertKeyWithSameKey() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f, 'A');
 hash.insert(1.0f, 'B');
 hash.insert(2.0f, 'C');
 hash.insert(3.0f, 'D');
 hash.insert(4.0f, 'E');
 hash.insert(1.0f, 'F');

 assertEquals(5, hash.size());
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertTrue(hash.contains(4.0f));
 assertTrue(hash.contains(5.0f));
 assertEquals(2, hash.indexOf(1.0f));
 assertEquals(-1, hash.indexOf(6.0f));
 }

 @Test
 public void testInsertKeyRehash() {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f, 'A');
 hash.insert(2.0f, 'B');
 hash.insert(3.0f, 'C');
 hash.insert(4.0f, 'D');
 hash.insert(5.0f, 'E');
 hash.insert(6.0f, 'F');
 hash.insert(7.0f, 'G');
 hash.insert(8.0f, 'H');

 assertEquals(8, hash.size());
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertTrue(hash.contains(4.0f));
 assertTrue(hash.contains(5.0f));
 assertTrue(hash.contains(6.0f));
 assertTrue(hash.contains(7.0f));
 assertTrue(hash.contains(8.0f));

 assertEquals(-1, hash.indexOf(9.0f));
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.insert(1.0f, 'A');
 hash.insert(2.0f, 'B');
 hash.insert(3.0f, 'C');
 hash.insert(4.0f, 'D');
 hash.insert(5.0f, 'E');
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 hash.writeExternal(out);
 out.close();
 TFloatCharHash hashFromStream = (TFloatCharHash) new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject();
 assertEquals(5, hashFromStream.size());
 assertEquals(1.0f, hashFromStream.get(0), 0.01f);
 assertEquals('A', hashFromStream.values().toArray()[0]);
 assertEquals(2.0f, hashFromStream.get(1), 0.01f);
 assertEquals('B', hashFromStream.values().toArray()[1]);
 }

 @Test
 public void testWriteExternalAndReadExternalWithEmptyHash() throws IOException, ClassNotFoundException {
 TFloatCharHash hash = new TFloatCharHash();
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 hash.writeExternal(out);
 out.close();
 TFloatCharHash hashFromStream = (TFloatCharHash) new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray())).readObject();
 assertEquals(0, hashFromStream.size());
 }
}