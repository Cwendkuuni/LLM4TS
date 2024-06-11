package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputstream;
import java.io.ObjectOutput;
import java.io.ObjectInput;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;
 private float key;
 private double value;
 private TFloatProcedure procedure;

 @Before
 public void setUp() {
 hash = new TFloatDoubleHash(5);
 key = 0.1f;
 value = 1.1d;
 procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float arg) {
 return arg == key;
 }
 };
 }

 @Test
 public void testDefaultConstructor() {
 TFloatDoubleHash h = new TFloatDoubleHash();
 assertEquals(0, h.size());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 TFloatDoubleHash h = new TFloatDoubleHash(5);
 assertEquals(5, h.size());
 }

 @Test
 public void testConstructorWithInitialCapacityAndLoadFactor() {
 TFloatDoubleHash h = new TFloatDoubleHash(5, 0.75f);
 assertEquals(5, h.size());
 }

 @Test
 public void testConstructorWithInitialCapacityLoadFactorAndNoEntryKeyValue() {
 TFloatDoubleHash h = new TFloatDoubleHash(5, 0.75f, 1.0f, 1.0d);
 assertEquals(5, h.size());
 assertEquals(1.0f, h.getNoEntryKey(), 0.0f);
 assertEquals(1.0d, h.getNoEntryValue(), 0.0d);
 }

 @Test
 public void testSetUp() {
 TFloatDoubleHash h = new TFloatDoubleHash(5);
 assertEquals(5, h.size());
 }

 @Test
 public void testContains() {
 TFloatDoubleHash h = new TFloatDoubleHash(5);
 h.insert(key, value);
 assertTrue(h.contains(key));
 }

 @Test
 public void testContainsKeyNotPresent() {
 TFloatDoubleHash h = new TFloatDoubleHash(5);
 assertFalse(h.contains(key));
 }

 @Test
 public void testForEach() {
 TFloatDoubleHash h = new TFloatDoubleHash(5);
 h.insert(key, value);
 assertTrue(h.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachWhenEmpty() {
 TFloatDoubleHash h = new TFloatDoubleHash(5);
 h.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 TFloatDoubleHash h = new TFloatDoubleHash(5);
 h.insert(key, value);
 h.removeAt(0);
 assertFalse(h.contains(key));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtWhenEmpty() {
 TFloatDoubleHash h = new TFloatDoubleHash(5);
 h.removeAt(0);
 }

 @Test
 public void testIndex() {
 TFloatDoubleHash h = new TFloatDoubleHash(5);
 h.insert(key, value);
 assertEquals(0, h.index(key));
 }

 @Test
 public void testIndexNotPresent() {
 TFloatDoubleHash h = new TFloatDoubleHash(5);
 assertLessThan(0, h.index(key));
 }

 @Test
 public void testInsertKey() {
 TFloatDoubleHash h = new TFloatDoubleHash(5);
 h.insert(key, value);
 assertEquals(1, h.size());
 assertTrue(h.contains(key));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWhenFull() {
 TFloatDoubleHash h = new TFloatDoubleHash(5, 1.0f);
 h.insert(key, value);
 }

 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 TFloatDoubleHash h = new TFloatDoubleHash(5);
 h.insert(key, value);

 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = null;
 try {
 out = new ObjectOutputStream(baos);
 out.writeObject(h);
 } finally {
 if (out != null) {
 out.close();
 }
 }
 ByteArrayInputstream bais = new ByteArrayInputstream(baos.toByteArray());
 ObjectInput in = null;
 try {
 in = new ObjectInput(bais);
 TFloatDoubleHash h2 = (TFloatDoubleHash) in.readObject();
 assertEquals(h.size(), h2.size());
 assertTrue(h.contains(key) == h2.contains(key));
 } finally {
 if (in != null) {
 in.close();
 }
 }
 }

 @After
 public void tearDown() {
 hash = null;
 }

}