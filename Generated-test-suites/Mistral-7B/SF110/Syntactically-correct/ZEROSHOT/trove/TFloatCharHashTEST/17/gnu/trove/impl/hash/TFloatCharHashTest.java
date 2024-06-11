import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertTrue(hash.isEmpty());
 assertFalse(hash.contains(0.0f));
 assertNull(hash.getNoEntryValue());
 assertEq(0.0f, hash.getNoEntryKey());
 }

 @Test
 public void testBasicOperations() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');

 assertEquals(3, hash.size());
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertEq('a', hash.get(1.0f));
 assertEq('b', hash.get(2.0f));
 assertEq('c', hash.get(3.0f));
 assertNull(hash.get(4.0f));
 assertEq(0.0f, hash.getNoEntryKey());
 assertEq('\0', hash.getNoEntryValue());
 }

 @Test
 public void testLoadFactor() {
 TFloatCharHash hashWithLoadFactor = new TFloatCharHash(5, 0.75f);
 hashWithLoadFactor.put(1.0f, 'a');
 hashWithLoadFactor.put(2.0f, 'b');
 hashWithLoadFactor.put(3.0f, 'c');
 hashWithLoadFactor.put(4.0f, 'd');
 hashWithLoadFactor.put(5.0f, 'e');

 assertEquals(5, hashWithLoadFactor.size());
 assertTrue(hashWithLoadFactor.contains(1.0f));
 assertTrue(hashWithLoadFactor.contains(2.0f));
 assertTrue(hashWithLoadFactor.contains(3.0f));
 assertTrue(hashWithLoadFactor.contains(4.0f));
 assertTrue(hashWithLoadFactor.contains(5.0f));
 assertEq('a', hashWithLoadFactor.get(1.0f));
 assertEq('b', hashWithLoadFactor.get(2.0f));
 assertEq('c', hashWithLoadFactor.get(3.0f));
 assertEq('d', hashWithLoadFactor.get(4.0f));
 assertEq('e', hashWithLoadFactor.get(5.0f));
 assertNull(hashWithLoadFactor.get(6.0f));
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 hash.writeExternal(oos);
 oos.close();

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput ois = new ObjectInputStream(bais);
 TFloatCharHash hashFromStream = (TFloatCharHash) ois.readObject();
 ois.close();

 assertEquals(3, hashFromStream.size());
 assertTrue(hashFromStream.contains(1.0f));
 assertTrue(hashFromStream.contains(2.0f));
 assertTrue(hashFromStream.contains(3.0f));
 assertEq('a', hashFromStream.get(1.0f));
 assertEq('b', hashFromStream.get(2.0f));
 assertEq('c', hashFromStream.get(3.0f));
 }

 @Test
 public void testForEach() {
 int count = 0;
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');

 hash.forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 count++;
 return true;
 }
 });

 assertEquals(3, count);
 }
}