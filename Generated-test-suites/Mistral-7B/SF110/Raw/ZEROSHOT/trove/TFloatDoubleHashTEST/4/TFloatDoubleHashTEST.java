package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Before
 public void setUp() {
 hash = new TFloatDoubleHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertEquals(0, hash.size());
 assertNull(hash.getNoEntryValue());
 assertSame(0.0f, hash.getNoEntryKey());
 }

 @Test
 public void testInitialCapacity() {
 hash = new TFloatDoubleHash(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testLoadFactor() {
 hash = new TFloatDoubleHash(5, 0.75f);
 assertEquals(3, hash.size());
 }

 @Test
 public void testNoEntryValueAndKey() {
 hash = new TFloatDoubleHash(5, 0.75f, 1.0f, 2.0);
 assertEquals(1.0f, hash.getNoEntryKey(), 0.0);
 assertEquals(2.0, hash.getNoEntryValue(), 0.0);
 }

 @Test
 public void testSetUp() {
 hash = new TFloatDoubleHash(5);
 assertEquals(5, hash.size());
 }

 @Test
 public void testContains() {
 hash.put(1.0f, 1.0);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testForEach() {
 int count = 0;
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 count++;
 return true;
 }
 };
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.forEach(procedure);
 assertEquals(3, count);
 }

 @Test
 public void testRemoveAt() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 int index = hash.index(2.0f);
 hash.removeAt(index);
 assertEquals(2, hash.size());
 }

 @Test
 public void testIndex() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 int index = hash.index(2.0f);
 assertEquals(1, index);
 }

 @Test(expected = IllegalStateException.class)
 public void testIndexNegative() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 int index = hash.index(-1.0f);
 }

 @Test(expected = IllegalStateException.class)
 public void testIndexNaN() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 float nan = Float.NaN;
 int index = hash.index(nan);
 }

 @Test
 public void testInsertKey() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 assertEquals(4, hash.size());
 }

 @Test
 public void testInsertKeyRehash() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.put(5.0f, 5.0);
 assertEquals(5, hash.size());
 }

 @Test
 public void testWriteReadExternal() throws IOException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutput objOut = new ObjectOutputStream(out);
 hash.writeExternal(objOut);
 ObjectInput objIn = new ObjectInput(new ByteArrayInputStream(out.toByteArray()));
 TFloatDoubleHash hash2 = new TFloatDoubleHash();
 hash2.readExternal(objIn);
 assertEquals(4, hash2.size());
 }

 @After
 public void tearDown() {
 hash = null;
 }
}