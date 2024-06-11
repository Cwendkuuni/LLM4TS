package test;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;
import org.junit.Assert;
import org.junit.Test;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Test
 public void testConstructors() {
     hash = new TFloatCharHash();
     Assert.assertNotNull(hash);
     hash = new TFloatCharHash(10);
     Assert.assertNotNull(hash);
     hash = new TFloatCharHash(10, 0.5f);
     Assert.assertNotNull(hash);
     hash = new TFloatCharHash(10, 0.5f, 0.0f, '\0');
     Assert.assertNotNull(hash);
 }

 @Test
 public void testGetNoEntryKey() {
     hash = new TFloatCharHash();
     Assert.assertEquals(0.0f, hash.getNoEntryKey(), 0.001f);
 }

 @Test
 public void testGetNoEntryValue() {
     hash = new TFloatCharHash();
     Assert.assertEquals('\0', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
     hash = new TFloatCharHash(5);
     Assert.assertEquals(5, hash.setUp(5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
     TFloatCharHash hash = new TFloatCharHash(-5);
 }

 @Test
 public void testContains() {
     hash = new TFloatCharHash(5);
     hash.insertKey(1.0f);
     Assert.assertTrue(hash.contains(1.0f));
     Assert.assertFalse(hash.contains(2.0f));
 }

 @Test
 public void testForEach() {
     hash = new TFloatCharHash(5);
     hash.insertKey(1.0f);
     hash.insertKey(2.0f);
     TFloatProcedure procedure = new TFloatProcedure() {
         @Override
         public boolean execute(float value) {
             return value != 1.0f;
         }
     };
     Assert.assertTrue(hash.forEach(procedure));
 }

 @Test
 public void testRemoveAt() {
     hash = new TFloatCharHash(5);
     hash.insertKey(1.0f);
     hash.insertKey(2.0f);
     hash.removeAt(0);
     Assert.assertFalse(hash.contains(1.0f));
     Assert.assertTrue(hash.contains(2.0f));
 }

 @Test
 public void testIndex() {
     hash = new TFloatCharHash(5);
     hash.insertKey(1.0f);
     hash.insertKey(2.0f);
     Assert.assertEquals(0, hash.index(1.0f));
     Assert.assertEquals(-1, hash.index(3.0f));
 }

 @Test
 public void testInsertKey() {
     hash = new TFloatCharHash(5);
     Assert.assertEquals(-1, hash.insertKey(-1.0f));
     Assert.assertEquals(0, hash.insertKey(0.0f));
     Assert.assertEquals(1, hash.insertKey(1.0f));
 }

 @Test(expected = IllegalStateException.class)
 public void testFullHash() {
     hash = new TFloatCharHash(5);
     for (int i = 0; i < 5; i++) {
         hash.insertKey(i + 0.1f);
     }
 }

 @Test
 public void testWriteExternal() throws IOException {
     hash = new TFloatCharHash(5);
     hash.insertKey(1.0f);
     TFloatCharHash clone = new TFloatCharHash();
     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(outputStream);
     hash.writeExternal(out);
     out.close();
     ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
     TFloatCharHash clonedHash = (TFloatCharHash) input.readObject();
     input.close();
     Assert.assertEquals(1.0f, clonedHash.getNoEntryKey(), 0.001f);
     Assert.assertEquals('\0', clonedHash.getNoEntryValue());
     Assert.assertTrue(clonedHash.contains(1.0f));
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
     hash = new TFloatCharHash();
     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     ObjectOutput out = new ObjectOutputStream(outputStream);
     out.writeByte(0);
     out.writeUTF("Test");
     out.writeFloat(0.0f);
     out.writeChar('\0');
     out.writeByte(1);
     out.writeInt(5);
     out.close();
     ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
     TFloatCharHash clonedHash = (TFloatCharHash) input.readObject();
     input.close();
     Assert.assertNull(clonedHash.getNoEntryKey());
     Assert.assertEquals('\0', clonedHash.getNoEntryValue());
 }

}