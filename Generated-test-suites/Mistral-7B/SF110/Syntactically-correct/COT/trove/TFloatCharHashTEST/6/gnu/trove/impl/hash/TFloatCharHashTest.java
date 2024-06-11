package gnu.trove.impl.hash.test;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setup() {
 hash = new TFloatCharHash(5);
 }

 // Test constructor
 @Test
 public void testTFloatCharHashConstructor() {
 TFloatCharHash hash = new TFloatCharHash();
 assertEquals(0, hash.getNoEntryKey());
 assertEquals('\u0000', hash.getNoEntryValue());

 hash = new TFloatCharHash(5);
 assertEquals(5, hash.size());
 assertEquals(0.0f, hash.getNoEntryKey());
 assertEquals('\u0000', hash.getNoEntryValue());

 hash = new TFloatCharHash(5, 0.5f);
 assertEquals(5, hash.size());
 assertEquals(0.0f, hash.getNoEntryKey());
 assertEquals('\u0000', hash.getNoEntryValue());

 hash = new TFloatCharHash(5, 0.5f, 1.0f, '\u0001');
 assertEquals(5, hash.size());
 assertEquals(1.0f, hash.getNoEntryKey());
 assertEquals('\u0001', hash.getNoEntryValue());
 }

 // Test setUp method
 @Test
 public void testSetUp() {
 int capacity = hash.setUp(5);
 assertEquals(5, capacity);
 }

 // Test contains method
 @Test
 public void testContains() {
 hash.put(1.0f, 'a');
 assertTrue(hash.contains(1.0f));

 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 hash.put(4.0f, 'd');
 hash.put(5.0f, 'e');

 assertFalse(hash.contains(6.0f));
 }

 // Test forEach method
 @Test
 public void testForEach() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 hash.put(4.0f, 'd');
 hash.put(5.0f, 'e');

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return key > 2.0f;
 }
 };

 assertTrue(hash.forEach(procedure));
 assertEquals(2, countMatches(procedure));

 procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return key < 3.0f;
 }
 };

 assertFalse(hash.forEach(procedure));
 assertEquals(0, countMatches(procedure));
 }

 // Test removeAt method
 @Test
 public void testRemoveAt() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 hash.put(4.0f, 'd');
 hash.put(5.0f, 'e');

 int index = hash.indexOf(3.0f);
 hash.removeAt(index);

 assertEquals(4, hash.size());
 assertFalse(hash.containsKey(3.0f));
 }

 // Test index method
 @Test
 public void testIndex() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 hash.put(4.0f, 'd');
 hash.put(5.0f, 'e');

 assertEquals(-1, hash.index(-1.0f));
 assertEquals(3, hash.index(3.0f));
 }

 // Test indexRehashed method
 @Test
 public void testIndexRehashed() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 hash.put(4.0f, 'd');
 hash.put(5.0f, 'e');
 hash.put(6.0f, 'f');
 hash.put(7.0f, 'g');

 assertEquals(-1, hash.indexRehashed(-1.0f, 0, HashFunctions.hash(-1.0f) & Integer.MAX_VALUE, (byte) 0));
 assertEquals(3, hash.indexRehashed(3.0f, 0, HashFunctions.hash(3.0f) & Integer.MAX_VALUE, (byte) 1));
 }

 // Test insertKey method
 @Test
 public void testInsertKey() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 hash.put(4.0f, 'd');
 hash.put(5.0f, 'e');

 int index = hash.insertKey(6.0f);
 assertEquals(6, hash.size());
 assertEquals(6.0f, hash.get(index));
 }

 // Test insertKeyRehash method
 @Test
 public void testInsertKeyRehash() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 hash.put(4.0f, 'd');
 hash.put(5.0f, 'e');
 hash.put(6.0f, 'f');
 hash.put(7.0f, 'g');

 int index = hash.insertKeyRehash(8.0f, 0, HashFunctions.hash(8.0f) & Integer.MAX_VALUE, (byte) 1);
 assertEquals(8, hash.size());
 assertEquals(8.0f, hash.get(index));
 }

 // Test writeExternal and readExternal methods
 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 hash.writeExternal(out);
 out.close();

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInputStream(bais);
 TFloatCharHash hashCopy = (TFloatCharHash) in.readObject();
 in.close();

 assertEquals(5, hashCopy.size());
 for (float key : hash.keySet()) {
 assertTrue(hashCopy.contains(key));
 }
 }

 // Helper methods
 private int countMatches(TFloatProcedure procedure) {
 int count = 0;
 hash.forEach(procedure -> {
 if (procedure.execute(hash.get(hash.indexOf(hash.get(hash.size() - 1))))) {
 count++;
 }
 });
 return count;
 }
}