package tests; // Add your test package here

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

private TFloatDoubleHash<Float> createHash(int initialCapacity) {
 return new TFloatDoubleHash<Float>(initialCapacity);
}

@Test
public void testConstructorWithoutArguments() {
 TFloatDoubleHash<Float> hash = new TFloatDoubleHash<Float>();
 assertEquals(0, hash.size());
 assertNull(hash.getNoEntryKey());
 assertNotNull(hash._states);
 assertNotNull(hash._set);
}

@Test
public void testConstructorWithInitialCapacity() {
 TFloatDoubleHash<Float> hash = new TFloatDoubleHash<Float>(10);
 assertEquals(0, hash.size());
 assertNull(hash.getNoEntryKey());
 assertNotNull(hash._states);
 assertNotNull(hash._set);
 assertEquals(10, hash.capacity());
}

@Test
public void testConstructorWithInitialCapacityAndLoadFactor() {
 TFloatDoubleHash<Float> hash = new TFloatDoubleHash<Float>(10, 0.75f);
 assertEquals(0, hash.size());
 assertNull(hash.getNoEntryKey());
 assertNotNull(hash._states);
 assertNotNull(hash._set);
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.001);
}

@Test
public void testConstructorWithInitialCapacityLoadFactorAndNoEntryKeyNoEntryValue() {
 TFloatDoubleHash<Float> hash = new TFloatDoubleHash<Float>(10, 0.75f, 0.0f, 0.0d);
 assertEquals(0, hash.size());
 assertNotNull(hash._states);
 assertNotNull(hash._set);
 assertEquals(10, hash.capacity());
 assertEquals(0.75f, hash.loadFactor(), 0.001);
 assertEquals(0.0f, hash.getNoEntryKey(), 0.001);
 assertEquals(0.0d, hash.getNoEntryValue(), 0.001);
}

@Test
public void testSetUp() {
 int capacity = 10;
 TFloatDoubleHash<Float> hash = createHash(capacity);
 int actualCapacity = hash.setUp(capacity);
 assertEquals(capacity, actualCapacity);
 assertNotNull(hash._set);
}

@Test(expected = IllegalArgumentException.class)
public void testSetUpWithNegativeInitialCapacity() {
 TFloatDoubleHash<Float> hash = new TFloatDoubleHash<Float>();
 int actualCapacity = hash.setUp(-5);
 fail("IllegalArgumentException expected");
}

@Test
public void testContains() {
 TFloatDoubleHash<Float> hash = createHash(3);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertFalse(hash.contains(4.0f));
}

@Test
public void testForEach() {
 TFloatDoubleHash<Float> hash = createHash(3);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 float count = 0;
 hash.forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 count++;
 return true;
 }
 });
 assertEquals(3, count);
}

@Test
public void testRemoveAt() {
 TFloatDoubleHash<Float> hash = createHash(3);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 int index = 1;
 hash.removeAt(index);
 assertEquals(2, hash.size());
 assertFalse(hash.contains(2.0f));
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(3.0f));
}

@Test(expected = IndexOutOfBoundsException.class)
public void testRemoveAtWithInvalidIndex() {
 TFloatDoubleHash<Float> hash = createHash(3);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 int index = 4;
 hash.removeAt(index);
 fail("IndexOutOfBoundsException expected");
}

@Test
public void testIndex() {
 TFloatDoubleHash<Float> hash = createHash(3);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 int index = hash.index(1.0f);
 assertEquals(0, index);
 index = hash.index(2.0f);
 assertEquals(1, index);
 index = hash.index(3.0f);
 assertEquals(2, index);
 index = hash.index(4.0f);
 assertEquals(-1, index);
}

@Test
public void testInsertKey() {
 TFloatDoubleHash<Float> hash = createHash(3);
 int index = hash.insertKey(1.0f);
 assertEquals(1, hash.size());
 assertEquals(0, index);
 index = hash.insertKey(2.0f);
 assertEquals(2, hash.size());
 assertEquals(1, index);
 index = hash.insertKey(3.0f);
 assertEquals(3, hash.size());
 assertEquals(2, index);
}

@Test(expected = IllegalArgumentException.class)
public void testInsertKeyWithInvalidValue() {
 TFloatDoubleHash<Float> hash = createHash(3);
 hash.insertKey((float) Double.NaN);
 fail("IllegalArgumentException expected");
}

@Test(expected = IllegalStateException.class)
public void testInsertKeyWithFullHashTable() {
 TFloatDoubleHash<Float> hash = createHash(3);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 hash.insertKey(4.0f);
 fail("IllegalStateException expected");
}

@Test
public void testWriteExternal() throws IOException {
 TFloatDoubleHash<Float> hash = createHash(3);
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.insertKey(3.0f);
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 hash.writeExternal(out);
 TFloatDoubleHash<Float> hashLoad = new TFloatDoubleHash<Float>();
 hashLoad.readExternal(new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray())));
 assertEquals(3, hashLoad.size());
 assertEquals(1.0f, hashLoad.get(0), 0.001);
 assertEquals(2.0f, hashLoad.get(1), 0.001);
 assertEquals(3.0f, hashLoad.get(2), 0.001);
}

@Test
public void testReadExternal() throws IOException, ClassNotFoundException {
 TFloatDoubleHash<Float> hash = createHash(3);
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(byteStream);
 hash.writeExternal(out);
 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteStream.toByteArray());
 ObjectInput in = new ObjectInputStream(byteInput);
 TFloatDoubleHash<Float> hashLoad = new TFloatDoubleHash<Float>();
 hashLoad.readExternal(in);
 assertEquals(3, hashLoad.size());
 assertEquals(1.0f, hashLoad.get(0), 0.001);
 assertEquals(2.0f, hashLoad.get(1), 0.001);
 assertEquals(3.0f, hashLoad.get(2), 0.001);
}

}