import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
 assertNull(hash.getNoEntryKey());
 assertNull(hash.getNoEntryValue());
 assertTrue(hash.isEmpty());
 assertFalse(hash.contains(0.0f));
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

 assertEquals('a', hash.get(1.0f));
 assertEquals('b', hash.get(2.0f));
 assertEquals('c', hash.get(3.0f));

 assertFalse(hash.contains(4.0f));

 hash.remove(1.0f);
 assertEquals(2, hash.size());
 assertFalse(hash.contains(1.0f));
 assertNull(hash.get(1.0f));
 }

 @Test
 public void testLoadFactor() {
 hash = new TFloatCharHash(3, 0.5f);
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');
 hash.put(4.0f, 'd');

 assertEquals(4, hash.size());
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertTrue(hash.contains(4.0f));
 }

 @Test(expected = IllegalStateException.class)
 public void testFullHash() {
 hash = new TFloatCharHash(3, 0.5f);
 for (float i = 0.0f; i <= 3.0f; i += 0.1f) {
 hash.put(i, (char) i);
 }
 }

 @Test
 public void testNoEntryKeyAndValue() {
 hash = new TFloatCharHash(3, 0.5f, 5.0f, 'e');
 assertEquals(5.0f, hash.getNoEntryKey());
 assertEquals('e', hash.getNoEntryValue());
 }

 @Test
 public void testForEach() {
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.put(3.0f, 'c');

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 1.0f, 0.0f);
 return true;
 }
 };

 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 hash.writeExternal(oos);
 byte[] bytes = baos.toByteArray();
 ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
 ObjectInput oin = new ObjectInput(bis);
 TFloatCharHash hashCopy = (TFloatCharHash) oin.readObject();

 assertEquals(3, hashCopy.size());
 assertTrue(hashCopy.contains(1.0f));
 assertTrue(hashCopy.contains(2.0f));
 assertTrue(hashCopy.contains(3.0f));
 }
}