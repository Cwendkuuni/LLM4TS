package test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Random;

import org.junit.Test;

public class TFloatCharHashTest {

 private TFloatCharHash<Float, Character> hash;
 private TFloatProcedure<Float> procedure;

 @Before
 public void setUp() {
 hash = new TFloatCharHash<>(11, 0.75f);
 procedure = new TFloatProcedure<Float>() {
 @Override
 public boolean execute(Float floatingPoint) {
 return true;
 }
 };
 }

 @Test
 public void testConstructor() {
 TFloatCharHash<Float, Character> hash1 = new TFloatCharHash<>();
 TFloatCharHash<Float, Character> hash2 = new TFloatCharHash<>(11);
 TFloatCharHash<Float, Character> hash3 = new TFloatCharHash<>(11, 0.75f);
 TFloatCharHash<Float, Character> hash4 = new TFloatCharHash<>(11, 0.75f, 0.0f, '\u0000');
 }

 @Test
 public void testGetNoEntryKey() {
 assertEquals(0.0f, hash.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals('\u0000', hash.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 assertEquals(11, hash.setUp(11));
 }

 @Test
 public void testContains() {
 float key = 1.0f;
 hash.insertKey(key);
 assertTrue(hash.contains(key));
 }

 @Test
 public void testContainsFalse() {
 float key = 2.0f;
 assertFalse(hash.contains(key));
 }

 @Test
 public void testForEach() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.forEach(procedure);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachException() {
 hash.removeAt(0);
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveAt() {
 float key = 1.0f;
 hash.insertKey(key);
 int index = hash.index(key);
 hash.removeAt(index);
 assertFalse(hash.contains(key));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveAtIndexOutOfBounds() {
 hash.removeAt(-1);
 }

 @Test
 public void testIndex() {
 float key = 1.0f;
 hash.insertKey(key);
 int index = hash.index(key);
 assertEquals(0, index);
 }

 @Test
 public void testIndexRehashed() {
 float key = 1.0f;
 hash.insertKey(key);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 int index = hash.indexRehashed(key, 0, HashFunctions.hash(key), (byte) 1);
 assertEquals(2, index);
 }

 @Test
 public void testInsertKey() {
 float key = 1.0f;
 int index = hash.insertKey(key);
 assertEquals(0, index);
 }

 @Test
 public void testInsertKeySameValue() {
 float key = 1.0f;
 hash.insertKey(key);
 int index = hash.insertKey(key);
 assertEquals(-1, index);
 }

 @Test
 public void testInsertKeyRehash() {
 Random random = new Random();
 float key = random.nextFloat();
 int index = hash.insertKey(key);
 assertNotEquals(-1, index);
 }

 @Test
 public void testInsertKeyAt() {
 float key = 1.0f;
 int index = hash.XinsertKey(key);
 assertEquals(0, index);
 }

 @Test
 public void testWriteExternal() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 hash.writeExternal(objectOutput);
 byte[] bytes = byteArrayOutputStream.toByteArray();
 }

 @Test
 public void testReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
 ObjectInput objectInput = new ObjectInput(byteArrayInputStream);
 TFloatCharHash<Float, Character> hash1 = new TFloatCharHash<>(11, 0.75f);
 hash1.readExternal(objectInput);
 }

 @Test(expected = IOException.class)
 public void testReadExternalIOException() throws IOException, ClassNotFoundException {
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
 ObjectInput objectInput = new ObjectInput(byteArrayInputStream);
 TFloatCharHash<Float, Character> hash1 = new TFloatCharHash<>(11, 0.75f);
 hash1.readExternal(objectInput);
 byteArrayInputStream.close();
 }

 @Test(expected = ClassNotFoundException.class)
 public void testReadExternalClassNotFoundException() throws IOException, ClassNotFoundException {
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
 ObjectInput objectInput = new ObjectInput(byteArrayInputStream);
 }
}