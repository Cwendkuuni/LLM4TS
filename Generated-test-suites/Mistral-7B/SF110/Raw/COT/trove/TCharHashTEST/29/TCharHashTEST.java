package com.example.junit4; // Replace with your test package name

import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

import org.junit.Test;

public class TCharHashTest {

	@Test
	public void testTCharHashWithDefaultConstructor() {
		TCharHash charHash = new TCharHash();
		assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, charHash.getNoEntryValue());
		int initialCapacity = charHash.size();
		charHash.put('testKey1', 'testValue1');
		charHash.put('testKey2', 'testValue2');
		assertEquals(2, charHash.size());
		assertTrue(charHash.contains('testKey1'));
		assertTrue(charHash.contains('testKey2'));
		assertFalse(charHash.contains('nonExistentKey'));
	}

	@Test
	public void testTCharHashWithInitialCapacity() {
		TCharHash charHash = new TCharHash(5);
		assertEquals(5, charHash.size());
		charHash.put('testKey1', 'testValue1');
		charHash.put('testKey2', 'testValue2');
		assertEquals(2, charHash.size());
		assertTrue(charHash.contains('testKey1'));
		assertTrue(charHash.contains('testKey2'));
		assertFalse(charHash.contains('nonExistentKey'));
	}

	@Test
	public void testTCharHashWithInitialCapacityAndLoadFactor() {
		TCharHash charHash = new TCharHash(5, 0.75f);
		assertEquals(5, charHash.size());
		charHash.put('testKey1', 'testValue1');
		charHash.put('testKey2', 'testValue2');
		assertEquals(2, charHash.size());
		assertTrue(charHash.contains('testKey1'));
		assertTrue(charHash.contains('testKey2'));
		assertFalse(charHash.contains('nonExistentKey'));
	}

	@Test
	public void testTCharHashWithInitialCapacityLoadFactorAndNoEntryValue() {
		char noEntryValue = 'CustomNoEntryValue';
		TCharHash charHash = new TCharHash(5, 0.75f, noEntryValue);
		assertEquals(5, charHash.size());
		charHash.put('testKey1', 'testValue1');
		charHash.put('testKey2', 'testValue2');
		assertEquals(2, charHash.size());
		assertTrue(charHash.contains('testKey1'));
		assertTrue(charHash.contains('testKey2'));
		assertFalse(charHash.contains('nonExistentKey'));
		assertEquals(noEntryValue, charHash.getNoEntryValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTCharHashWithNegativeInitialCapacity() {
		new TCharHash(-5);
	}

	@Test
	public void testTCharHashContains() {
		TCharHash charHash = new TCharHash(5);
		charHash.put('testKey1', 'testValue1');
		charHash.put('testKey2', 'testValue2');
		assertTrue(charHash.contains('testKey1'));
		assertTrue(charHash.contains('testKey2'));
		assertFalse(charHash.contains('nonExistentKey'));
	}

	@Test
	public void testTCharHashForEach() {
		TCharHash charHash = new TCharHash(5);
		charHash.put('testKey1', 'testValue1');
		charHash.put('testKey2', 'testValue2');
		assertTrue(charHash.forEach(new TCharProcedure() {
			@Override
			public boolean execute(char character) {
				boolean isTestKey = character == 'testKey1' || character == 'testKey2';
				assertTrue("Unexpected character found: " + character, isTestKey);
				return true;
			}
		}));
	}

	@Test
	public void testTCharHashRemoveAt() {
		TCharHash charHash = new TCharHash(5);
		charHash.put('testKey1', 'testValue1');
		charHash.put('testKey2', 'testValue2');
		assertTrue(charHash.contains('testKey1'));
		assertTrue(charHash.contains('testKey2'));
		charHash.removeAt(0);
		assertFalse(charHash.contains('testKey1'));
		assertTrue(charHash.contains('testKey2'));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testTCharHashIndexOutOfBounds() {
		TCharHash charHash = new TCharHash(5);
		charHash.put('testKey1', 'testValue1');
		charHash.put('testKey2', 'testValue2');
		charHash.removeAt(5);
	}

	@Test
	public void testTCharHashIndex() {
		TCharHash charHash = new TCharHash(5);
		charHash.put('testKey1', 'testValue1');
		charHash.put('testKey2', 'testValue2');
		assertEquals(0, charHash.index('testKey1'));
		assertEquals(1, charHash.index('testKey2'));
		assertEquals(-1, charHash.index('nonExistentKey'));
	}

	@Test(expected = IllegalStateException.class)
	public void testTCharHashNoFreeOrRemovedSlotsAvailable() {
		TCharHash charHash = new TCharHash(3);
		charHash.put('testKey1', 'testValue1');
		charHash.put('testKey2', 'testValue2');
		charHash.put('testKey3', 'testValue3');
		charHash.put('testKey4', 'testValue4');
	}

}