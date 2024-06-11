import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hashMap;

 @Test
 public void testEmptyConstructor() {
 hashMap = new TFloatCharHash();
 assertTrue(hashMap.isEmpty());
 }

 @Test
 public void testInitialCapacityConstructor() {
 hashMap = new TFloatCharHash(10);
 assertEquals(10, hashMap.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorConstructor() {
 hashMap = new TFloatCharHash(10, 0.75f);
 assertEquals(10, hashMap.capacity());
 assertEquals(10, hashMap.size());
 }

 @Test
 public void testInitialCapacityAndLoadFactorAndKeysConstructor() {
 hashMap = new TFloatCharHash(10, 0.75f, 0.0f, '\u0000');
 assertEquals(10, hashMap.capacity());
 assertEquals(0, hashMap.size());
 }

 @Test
 public void testGetNoEntryKey() {
 hashMap = new TFloatCharHash();
 assertEquals(0.0f, hashMap.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 hashMap = new TFloatCharHash();
 assertEquals('\u0000', hashMap.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hashMap = new TFloatCharHash(10);
 assertEquals(10, hashMap.capacity());
 }

 @Test
 public void testContains() {
 hashMap = new TFloatCharHash(10);
 hashMap.insert(1.0f);
 assertTrue(hashMap.contains(1.0f));
 }

 @Test
 public void testContainsKeyThatIsNotPresent() {
 hashMap = new TFloatCharHash(10);
 assertFalse(hashMap.contains(2.0f));
 }

 @Test
 public void testForEach() {
 hashMap = new TFloatCharHash(10);
 hashMap.insert(1.0f);
 hashMap.insert(2.0f);
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return value > 1.0f;
 }
 };
 assertTrue(hashMap.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
 hashMap = new TFloatCharHash(10);
 hashMap.insert(1.0f);
 hashMap.insert(2.0f);
 hashMap.removeAt(0);
 assertEquals(1, hashMap.size());
 assertFalse(hashMap.contains(1.0f));
 }

 @Test
 public void testIndex() {
 hashMap = new TFloatCharHash(10);
 hashMap.insert(1.0f);
 hashMap.insert(2.0f);
 assertEquals(0, hashMap.index(1.0f));
 }

 @Test
 public void testIndexKeyNotPresent() {
 hashMap = new TFloatCharHash(10);
 hashMap.insert(1.0f);
 hashMap.insert(2.0f);
 assertEquals(-1, hashMap.index(3.0f));
 }

 @Test
 public void testInsertKey() {
 hashMap = new TFloatCharHash(10);
 hashMap.insert(1.0f);
 hashMap.insert(2.0f);
 hashMap.insert(3.0f);
 assertEquals(3, hashMap.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 hashMap = new TFloatCharHash(10);
 hashMap.insert(1.0f);
 hashMap.insert(2.0f);
 hashMap.insert(3.0f);
 hashMap.insert(4.0f);
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 hashMap = new TFloatCharHash(10);
 hashMap.insert(1.0f);
 hashMap.writeExternal(out);
 out.close();
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
 TFloatCharHash hashMapClone = (TFloatCharHash) in.readObject();
 in.close();
 assertEquals(1, hashMapClone.size());
 assertEquals(1.0f, hashMapClone.get(0), 0.001f);
 }
}