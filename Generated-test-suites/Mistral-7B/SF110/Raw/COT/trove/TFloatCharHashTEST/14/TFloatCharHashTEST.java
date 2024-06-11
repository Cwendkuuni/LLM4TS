package com.example.junit4; // replace with your package name

import static org.junit.Assert.*;
import static gnu.trove.HashFunctions.hash;

import org.junit.Test;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

	@Test
	public void testConstructor() {
		TFloatCharHash hash = new TFloatCharHash();
		assertNotNull(hash);
	}

	@Test
	public void testConstructorsWithInitialCapacity() {
		TFloatCharHash hash = new TFloatCharHash(10);
		assertNotNull(hash);
		assertEquals(10, hash.size());
	}

	@Test
	public void testConstructorsWithInitialCapacityAndLoadFactor() {
		TFloatCharHash hash = new TFloatCharHash(10, 0.5f);
		assertNotNull(hash);
		assertEquals(10, hash.capacity());
		assertEquals(0.5f, hash.loadFactor(), 0.01);
	}

	@Test
	public void testConstructorsWithInitialCapacityLoadFactorAndNoEntryValues() {
		TFloatCharHash hash = new TFloatCharHash(10, 0.5f, 0.0f, '\0'); ;
		assertNotNull(hash);
		assertEquals(10, hash.capacity());
		assertEquals(0.5f, hash.loadFactor(), 0.01);
		assertEquals(0.0f, hash.getNoEntryKey());
		assertEquals('\0', hash.getNoEntryValue());
	}

	@Test
	public void testSetUp() {
		final int initialCapacity = 10;
		TFloatCharHash hash = new TFloatCharHash() {
			@Override
			protected int setUp(int initialCapacity) {
				return super.setUp(initialCapacity);
			}
		};
		assertEquals(initialCapacity, hash.capacity());
		assertNull(hash._set);
	}

	@Test
	public void testContains() {
		TFloatCharHash hash = new TFloatCharHash();
		assertTrue(hash.contains(Float.NaN));
		assertTrue(hash.contains(0.0f));
		assertFalse(hash.contains(1.0f));
	}

	@Test
	public void testForEach() {
		TFloatCharHash hash = new TFloatCharHash();
		TFloatProcedure procedure = arg -> {};
		hash.put(0.0f, '\u0001');
		hash.put(1.0f, '\u0002');
		hash.put(2.0f, '\u0003');
		assertTrue(hash.forEach(procedure));
		assertEquals('\u0001', procedure.value[0]);
		assertEquals('\u0002', procedure.value[1]);
		assertEquals('\u0003', procedure.value[2]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testForEachWithNullProcedure() {
		TFloatCharHash hash = new TFloatCharHash();
		hash.forEach(null);
	}

	@Test
	public void testRemoveAt() {
		TFloatCharHash hash = new TFloatCharHash();
		hash.put(0.0f, '\u0001');
		hash.put(1.0f, '\u0002');
		hash.put(2.0f, '\u0003');
		assertEquals(3, hash.size());
		assertEquals(1, hash.removeAt(1));
		assertEquals(2, hash.size());
		assertEquals(0.0f, hash.get(1.0f));
		assertEquals('\u0003', hash.get(2.0f));
		assertEquals(-1, hash.removeAt(3));
	}

	@Test
	public void testIndex() {
		TFloatCharHash hash = new TFloatCharHash();
		hash.put(0.0f, '\u0001');
		hash.put(1.0f, '\u0002');
		hash.put(2.0f, '\u0003');
		assertEquals(1, hash.index(1.0f));
		assertEquals(-1, hash.index(3.0f));
		assertEquals(-1, hash.index(Float.NaN));
	}

	@Test
	public void testIndexRehashed() {
		TFloatCharHash hash = new TFloatCharHash();
		hash.put(0.0f, '\u0001');
		hash.put(1.0f, '\u0002');
		hash.put(2.0f, '\u0003');
		hash.put(3.0f, '\u0004');
		assertEquals(3, hash.size());
		assertEquals(1, hash.indexRehashed(1.0f, 1, hash(1.0f) & Integer.MAX_VALUE, (byte) 1));
		assertEquals(-1, hash.indexRehashed(Float.NaN, 1, hash(Float.NaN) & Integer.MAX_VALUE, (byte) 1));
	}

	@Test
	public void testInsertKey() {
		TFloatCharHash hash = new TFloatCharHash();
		assertEquals(-1, hash.insertKey(0.0f));
		assertEquals(0, hash.size());
		assertEquals(0.0f, hash.getNoEntryKey());
		assertEquals('\u0000', hash.getNoEntryValue());
		assertEquals(-1, hash.insertKey(1.0f));
		assertEquals(0, hash.size());
		assertEquals(0.0f, hash.getNoEntryKey());
		assertEquals('\u0000', hash.getNoEntryValue());
		assertEquals(1, hash.insertKey(2.0f));
		assertEquals(1, hash.size());
		assertEquals(2.0f, hash.get(1));
		assertEquals(0.0f, hash.getNoEntryKey());
		assertEquals('\u0000', hash.getNoEntryValue());
		assertEquals(2, hash.insertKey(2.0f));
		assertEquals(1, hash.size());
		assertEquals(2.0f, hash.get(1));
		assertEquals(-3, hash.insertKey(Float.NaN));
		assertEquals(1, hash.size());
		assertNull(hash.get(Float.NaN));
	}

	@Test
	public void testInsertKeyRehash() {
		TFloatCharHash hash = new TFloatCharHash();
		hash.put(0.0f, '\u0001');
		hash.put(1.0f, '\u0002');
		hash.put(2.0f, '\u0003');
		hash.put(3.0f, '\u0004');
		assertEquals(4, hash.size());
		assertEquals(1, hash.insertKey(1.0f));
		assertEquals(5, hash.size());
		assertEquals(1.0f, hash.get(1));
		assertEquals(1, hash.indexRehashed(1.0f, 1, hash(1.0f) & Integer.MAX_VALUE, (byte) 1));
		assertEquals(1, hash.insertKey(2.0f));
		assertEquals(5, hash.size());
		assertEquals(2.0f, hash.get(1));
		assertEquals(2, hash.indexRehashed(2.0f, 1, hash(2.0f) & Integer.MAX_VALUE, (byte) 1));
		assertEquals(-6, hash.insertKey(Float.NaN));
		assertEquals(5, hash.size());
		assertNull(hash.get(Float.NaN));
	}

	@Test(expected = IllegalStateException.class)
	public void testInsertKeyWithFullHash() {
		TFloatCharHash hash = new TFloatCharHash();
		final int initialCapacity = 1;
		for (int i = 0; i < initialCapacity; i++) {
			hash.put(i, (float) i);
		}
		assertEquals(initialCapacity, hash.size());
		assertEquals(initialCapacity - 1, hash.capacity());
		assertFalse(hash.isEmpty());
		assertTrue(hash.isFull());
		try {
			hash.insertKey(Float.NaN);
		} catch (Exception e) {
			assertEquals("No free or removed slots available. Key set full?!!", e.getMessage());
		}
	}

	@Test
	public void testWriteExternal() throws IOException {
		TFloatCharHash hash = new TFloatCharHash() {
			@Override
			protected void writeExternal(ObjectOutput out) throws IOException {
				super.writeExternal(out);
				out.writeFloat(0.5f);
				out.writeChar('A');
			}
		};
		hash.put(0.0f, '\u0001');
		hash.put(1.0f, '\u0002');
		hash.put(2.0f, '\u0003');
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objectOut = new ObjectOutputStream(out);
		hash.writeExternal(objectOut);
		byte[] bytes = out.toByteArray();
		TFloatCharHash deserializedHash = new TFloatCharHash();
		deserializedHash.readExternal(new ObjectInputStream(new ByteArrayInputStream(bytes)));
		assertEquals(3, deserializedHash.size());
		assertEquals(0.5f, deserializedHash.getNoEntryKey());
		assertEquals('A', deserializedHash.getNoEntryValue());
		assertEquals(0.0f, deserializedHash.get(0));
		assertEquals(1.0f, deserializedHash.get(1));
		assertEquals(2.0f, deserializedHash.get(2));
	}

	@Test
	public void testReadExternal() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream objectOut = new ObjectOutputStream(out);
		TFloatCharHash hash = new TFloatCharHash();
		hash.writeExternal(objectOut);
		ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());
		ObjectInputStream objectIn = new ObjectInputStream(input);
		TFloatCharHash deserializedHash = (TFloatCharHash) objectIn.readObject();
		assertEquals(0, deserializedHash.size());
		assertEquals(0.0f, deserializedHash.getNoEntryKey());
		assertEquals('\u0000', deserializedHash.getNoEntryValue());
		hash.put(0.0f, '\u0001');
		hash.put(1.0f, '\u0002');
		hash.put(2.0f, '\u0003');
		objectOut.writeFloat(0.5f);
		objectOut.writeChar('A');
		objectOut.writeFloat(hash.getNoEntryKey());
		objectOut.writeChar(hash.getNoEntryValue());
		objectOut.flush();
		input = new ByteArrayInputStream(out.toByteArray());
		deserializedHash = (TFloatCharHash) objectIn.readObject();
		assertEquals(3, deserializedHash.size());
		assertEquals(0.5f, deserializedHash.getNoEntryKey());
		assertEquals('A', deserializedHash.getNoEntryValue());
		assertEquals(0.0f, deserializedHash.get(0));
		assertEquals(1.0f, deserializedHash.get(1));
		assertEquals(2.0f, deserializedHash.get(2));
	}

}