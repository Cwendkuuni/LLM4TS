package gnu.trove.impl.hash.test;

import static org.junit.Assert.*;

import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

import org.junit.Test;

public class TShortHashTest {

	@Test
	public void testDefaultConstructor() {
		TShortHash hash = new TShortHash();
		assertEquals(0, hash.getNoEntryValue());
		assertTrue(hash.isEmpty());
	}

	@Test
	public void testInitialCapacityConstructor() {
		TShortHash hash = new TShortHash(10);
		assertEquals(0, hash.getNoEntryValue());
		assertFalse(hash.isEmpty());
		assertEquals(10, hash.size());
	}

	@Test
	public void testInitialCapacityAndLoadFactorConstructor() {
		TShortHash hash = new TShortHash(10, 0.75f);
		assertEquals(0, hash.getNoEntryValue());
		assertFalse(hash.isEmpty());
		assertEquals(7, hash.size());
	}

	@Test
	public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
		short noEntryValue = 123;
		TShortHash hash = new TShortHash(10, 0.75f, noEntryValue);
		assertEquals(noEntryValue, hash.getNoEntryValue());
		assertFalse(hash.isEmpty());
		assertEquals(7, hash.size());
	}

	@Test
	public void testGetNoEntryValue() {
		TShortHash hash = new TShortHash(10, 0.75f, (short) 456);
		assertEquals((short) 456, hash.getNoEntryValue());
	}

	@Test
	public void testSetUp() {
		TShortHash hash = new TShortHash(10, 0.75f, (short) 456);
		assertEquals(10, hash.size());
		hash.setUp(20);
		assertEquals(20, hash.size());
	}

	@Test
	public void testContains() {
		TShortHash hash = new TShortHash(10, 0.75f, (short) 456);
		hash.put((short) 1, (short) 1);
		assertTrue(hash.contains((short) 1));
		assertFalse(hash.contains((short) 2));
	}

	@Test
	public void testForEach() {
		TShortHash hash = new TShortHash(10, 0.75f, (short) 456);
		short count = 0;
		hash.put((short) 1, (short) 1);
		hash.put((short) 2, (short) 2);
		hash.put((short) 3, (short) 3);
		hash.forEach(new TShortProcedure() {
			public boolean execute(short value) {
				count++;
				return true;
			}
		});
		assertEquals(3, count);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeInitialCapacity() {
		new TShortHash(-5, 0.75f, (short) 456);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLoadFactorGreaterThanOne() {
		new TShortHash(10, 1.5f, (short) 456);
	}

	@Test(expected = NullPointerException.class)
	public void testNullInitialCapacityArray() {
		new TShortHash(null, 0.75f, (short) 456);
	}

	@Test(expected = NullPointerException.class)
	public void testNullNoEntryValue() {
		new TShortHash(10, 0.75f, null);
	}
}