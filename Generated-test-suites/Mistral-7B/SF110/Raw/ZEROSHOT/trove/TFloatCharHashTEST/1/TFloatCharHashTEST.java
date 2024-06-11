import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hash;
 private TFloatProcedure procedure;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float floatValue) {
 return true;
 }
 };
 }

 @After
 public void tearDown() {
 hash = null;
 procedure = null;
 }

 @Test
 public void testEmptyHash() {
 assertEquals(0, hash.size());
 assertFalse(hash.contains(0.0f));
 }

 @Test
 public void testInitializationWithInitialCapacity() {
 hash = new TFloatCharHash(3, 0.5f);
 assertEquals(3, hash.size());
 assertFalse(hash.contains(0.0f));
 }

 @Test
 public void testInitializationWithInitialCapacityAndLoadFactor() {
 hash = new TFloatCharHash(3, 0.5f, 1.0f, 'A');
 assertEquals(3, hash.size());
 assertFalse(hash.contains(0.0f));
 assertEquals(1.0f, hash.getNoEntryKey());
 assertEquals('A', hash.getNoEntryValue());
 }

 @Test
 public void testForEach() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');
 hash.forEach(procedure);
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullHash() {
 for (int i = 0; i < hash.size(); i++) {
 hash.put((float) i, (char) i);
 }
 try {
 hash.put(Float.NaN, (char) 0);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWriteExternalWithNullOutput() {
 try {
 hash.writeExternal(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 DataOutputStream dataStream = new DataOutputStream(byteStream);
 hash.writeExternal(dataStream);
 dataStream.close();
 TFloatCharHash hashClone = new TFloatCharHash(5);
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteStream.toByteArray());
 DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
 hashClone.readExternal(dataInputStream);
 dataInputStream.close();
 byteStream.close();
 assertEquals(3, hash.size());
 assertEquals(3, hashClone.size());
 assertArrayEquals(hash._set, hashClone._set);
 assertEquals(hash.getNoEntryKey(), hashClone.getNoEntryKey());
 assertEquals(hash.getNoEntryValue(), hashClone.getNoEntryValue());
 }
}