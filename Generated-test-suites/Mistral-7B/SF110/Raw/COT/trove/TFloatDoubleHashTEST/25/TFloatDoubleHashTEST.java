package com.example.junit.test; /* replace with your test package name */

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

import org.junit.Test;

public class TFloatDoubleHashTest {

	@Test
	public void testTFloatDoubleHashConstruction() {
		TFloatDoubleHash hash = new TFloatDoubleHash();
		assertNotNull(hash);

		TFloatDoubleHash hashWithInitialCapacity = new TFloatDoubleHash(10);
		assertNotNull(hashWithInitialCapacity);

		TFloatDoubleHash hashWithInitialCapacityAndLoadFactor = new TFloatDoubleHash(10, 0.75f);
		assertNotNull(hashWithInitialCapacityAndLoadFactor);

		TFloatDoubleHash hashWithInitialCapacityAndLoadFactorAndValues = new TFloatDoubleHash(10, 0.75f, 0.0f, 0.0);
		assertNotNull(hashWithInitialCapacityAndLoadFactorAndValues);
	}

	@Test
	public void testGetNoEntryKey() {
		TFloatDoubleHash hash = new TFloatDoubleHash();
		float noEntryKey = hash.getNoEntryKey();
		assertEquals(0.0f, noEntryKey, 0.0);
	}

	@Test
	public void testGetNoEntryValue() {
		TFloatDoubleHash hash = new TFloatDoubleHash();
		double noEntryValue = hash.getNoEntryValue();
		assertEquals(0.0, noEntryValue, 0.0);
	}

	@Test
	public void testSetUp() {
		int initialCapacity = 10;
		TFloatDoubleHash hash = new TFloatDoubleHash(initialCapacity);
		int capacity = hash.setUp(initialCapacity);
		assertEquals(initialCapacity, capacity);
	}

	@Test
	public void testContains() {
		TFloatDoubleHash hash = new TFloatDoubleHash();
		hash.insertKey(1.0f);
		assertTrue(hash.contains(1.0f));

		assertFalse(hash.contains(2.0f));
	}

	@Test
	public void testForEach() {
		TFloatDoubleHash hash = new TFloatDoubleHash();
		hash.insertKey(1.0f);
		hash.insertKey(2.0f);
		TFloatProcedure procedure = new TFloatProcedure() {
			@Override
			public boolean execute(float floatValue) {
				assertEquals(1.0f, floatValue, 0.0);
				assertEquals(2.0f, (float) (this.getIndex() + 1), 0.0);
				return true;
			}
		};
		assertTrue(hash.forEach(procedure));
	}

	@Test
	public void testRemoveAt() {
		TFloatDoubleHash hash = new TFloatDoubleHash();
		hash.insertKey(1.0f);
		hash.insertKey(2.0f);
		hash.removeAt(0);
		assertFalse(hash.contains(1.0f));
		assertTrue(hash.contains(2.0f));
	}

	@Test(expected = IllegalStateException.class)
	public void testIndexWithEmptyHash() {
		TFloatDoubleHash hash = new TFloatDoubleHash();
		int index = hash.index(1.0f);
		assertEquals(-1, index);
	}

	@Test
	public void testIndexWithExistingKey() {
		TFloatDoubleHash hash = new TFloatDoubleHash();
		hash.insertKey(1.0f);
		int index = hash.index(1.0f);
		assertEquals(0, index);
	}

	@Test
	public void testIndexWithRehashedKey() {
		TFloatDoubleHash hash = new TFloatDoubleHash();
		hash.insertKey(1.0f);
		hash.insertKey(2.0f);
		hash.insertKey(3.0f);
		int index = hash.index(2.0f);
		assertEquals(1, index);
	}

	@Test(expected = IllegalStateException.class)
	public void testInsertKeyWithFullHash() {
		TFloatDoubleHash hash = new TFloatDoubleHash(3);
		hash.insertKey(1.0f);
		hash.insertKey(2.0f);
		hash.insertKey(3.0f);
		hash.insertKey(4.0f); // should throw an exception
	}

	@Test
	public void testWriteExternal() throws IOException {
		TFloatDoubleHash hash = new TFloatDoubleHash();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
		hash.writeExternal(objStream);
		objStream.close();
		TFloatDoubleHash readHash = new TFloatDoubleHash();
		ObjectInputStream objInput = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
		readHash.readExternal(objInput);
		objInput.close();
		assertEquals(hash.getNoEntryKey(), readHash.getNoEntryKey(), 0.0);
		assertEquals(hash.getNoEntryValue(), readHash.getNoEntryValue(), 0.0);
	}

	@Test
	public void testReadExternal() throws IOException, ClassNotFoundException {
		TFloatDoubleHash hash = new TFloatDoubleHash();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
		hash.writeExternal(objStream);
		objStream.close();
		ObjectInputStream objInput = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
		TFloatDoubleHash readHash = new TFloatDoubleHash();
		readHash.readExternal(objInput);
		objInput.close();
		assertEquals(hash.size(), readHash.size());
	}

}