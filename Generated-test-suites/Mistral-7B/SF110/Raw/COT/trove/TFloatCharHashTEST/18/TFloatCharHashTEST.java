package tests;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 // Test constructors
 @Test
 public void testConstructor() {
 TFloatCharHash h1 = new TFloatCharHash();
 TFloatCharHash h2 = new TFloatCharHash(10);
 TFloatCharHash h3 = new TFloatCharHash(10, 0.5f);
 TFloatCharHash h4 = new TFloatCharHash(10, 0.5f, 1.0f, 'A');
 assertEquals(1, h1.getNoEntryKey());
 assertEquals('\u0000', h1.getNoEntryValue());
 assertEquals(10, h2.size());
 assertEquals(0.5f, h3.getNoEntryKey());
 assertEquals('A', h4.getNoEntryValue());
 }

 // Test getters
 @Test
 public void testGetters() {
 assertEquals(1, hash.getNoEntryKey());
 assertEquals('\u0000', hash.getNoEntryValue());
 }

 // Test setUp method
 @Test
 public void testSetUp() {
 hash = new TFloatCharHash(5);
 assertEquals(5, hash.size());
 }

 // Test contains method
 @Test
 public void testContains() {
 assertFalse(hash.contains(1.1f));
 hash.put(1.1f, 'A');
 assertTrue(hash.contains(1.1f));
 }

 // Test forEach method
 @Test
 public void testForEach() {
 int count = 0;
 hash.put(1.1f, 'A');
 hash.put(2.2f, 'B');
 hash.put(3.3f, 'C');
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 count++;
 return true;
 }
 };
 hash.forEach(procedure);
 assertEquals(3, count);
 }

 // Test removeAt method
 @Test
 public void testRemoveAt() {
 hash.put(1.1f, 'A');
 hash.put(2.2f, 'B');
 hash.put(3.3f, 'C');
 hash.removeAt(1);
 assertFalse(hash.containsKey(2.2f));
 }

 // Test index method
 @Test
 public void testIndex() {
 hash.put(1.1f, 'A');
 hash.put(2.2f, 'B');
 hash.put(3.3f, 'C');
 assertEquals(1, hash.index(1.1f));
 assertEquals(-1, hash.index(4.4f));
 }

 // Test insertKey method
 @Test
 public void testInsertKey() {
 hash.put(1.1f, 'A');
 hash.put(2.2f, 'B');
 hash.put(3.3f, 'C');
 hash.put(4.4f, 'D');
 assertEquals(4, hash.size());
 assertEquals(-2, hash.insertKey(1.1f));
 assertEquals(5, hash.size());
 }

 // Test edge cases and exceptions
 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlotsAvailable() {
 hash.put(1.1f, 'A');
 hash.put(2.2f, 'B');
 hash.put(3.3f, 'C');
 for (int i = 0; i < hash.size(); i++) {
 hash.removeAt(0);
 }
 hash.insertKey(4.4f);
 }

 // Test writeExternal and readExternal methods
 @Test
 public void testWriteReadMethods() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutput objOut = new ObjectOutputStream(out);
 TFloatCharHash hash = new TFloatCharHash(5);
 hash.put(1.1f, 'A');
 hash.put(2.2f, 'B');
 hash.writeExternal(objOut);
 objOut.close();
 ByteArrayInputSigma stream = new ByteArrayInputSigma(out.toByteArray());
 ObjectInput objIn = new ObjectInputStream(stream);
 TFloatCharHash hash2 = (TFloatCharHash) objIn.readObject();
 objIn.close();
 assertEquals(2, hash2.size());
 assertEquals(1.1f, hash2.get(0), 0.001f);
 assertEquals('A', hash2.get(1));
 }

}