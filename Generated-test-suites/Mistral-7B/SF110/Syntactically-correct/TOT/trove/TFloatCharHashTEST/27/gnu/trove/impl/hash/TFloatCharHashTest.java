import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 @Test
 public void testEmptyHash() {
 TFloatCharHash hash = new TFloatCharHash(10);
 assertNull(hash.getNoEntryKey());
 assertNull(hash.getNoEntryValue());
 assertTrue(hash.isEmpty());
 assertFalse(hash.contains(0.0f));
 }

 @Test
 public void testBasicFunctionality() {
 TFloatCharHash hash = new TFloatCharHash(5, 0.75f);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 hash.insertKey(4.0f);
 hash.insertKey(5.0f);

 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertTrue(hash.contains(4.0f));
 assertTrue(hash.contains(5.0f));

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return key > 3.0f;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testLoadFactor() {
 TFloatCharHash hash = new TFloatCharHash(5, 0.5f);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 hash.insertKey(4.0f);
 hash.insertKey(5.0f);
 hash.insertKey(6.0f);

 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertTrue(hash.contains(4.0f));
 assertTrue(hash.contains(5.0f));
 assertTrue(hash.contains(6.0f));
 assertTrue(hash.size() == 6);

 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 return key > 5.0f;
 }
 };
 assertFalse(hash.forEach(procedure));
 }

 @Test(expected = IllegalStateException.class)
 public void testFullHash() {
 TFloatCharHash hash = new TFloatCharHash(5, 0.5f);
 for (int i = 0; i < 11; i++) {
 hash.insertKey((float) i);
 }
 }

 @Test
 public void testWriteRead() throws IOException, ClassNotFoundException {
 TFloatCharHash hash = new TFloatCharHash(5, 0.5f);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);

 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput output = new ObjectOutputStream(byteArrayOutputStream);
 hash.writeExternal(output);
 output.close();

 byte[] bytes = byteArrayOutputStream.toByteArray();
 ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TFloatCharHash hashFromFile = (TFloatCharHash) input.readObject();
 input.close();

 assertTrue(hashFromFile.contains(1.0f));
 assertTrue(hashFromFile.contains(2.0f));
 assertTrue(hashFromFile.contains(3.0f));
 }

}