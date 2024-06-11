package weka.core.test;

import static org.junit.Assert.*;

import weka.core.Memory;

import org.junit.Test;

public class MemoryTest {

	@Test
	public void testMemoryInitialization() {
		Memory memory = new Memory();
		Memory memoryWithGUI = new Memory(true);

		assertEquals(false, memory.getUseGUI());
		assertEquals(false, memoryWithGUI.getUseGUI());

		long initialMemory = memory.getInitial();
		long initialMemoryWithGUI = memoryWithGUI.getInitial();

		assertTrue(initialMemory > 0);
		assertTrue(initialMemoryWithGUI > 0);
	}

	@Test
	public void testIsEnabled() {
		Memory memory = new Memory();
		memory.setEnabled(true);

		assertTrue(memory.isEnabled());
		memory.setEnabled(false);
		assertFalse(memory.isEnabled());
	}

	@Test
	public void testGetMax() {
		Memory memory = new Memory();
		long maxMemory = memory.getMax();

		assertNotNull(maxMemory);
		assertGreaterThan(maxMemory, 0);
	}

	@Test
	public void testGetCurrent() {
		Memory memory = new Memory();
		long currentMemory = memory.getCurrent();

		assertNotNull(currentMemory);
		assertGreaterThan(currentMemory, 0);
	}

	@Test
	public void testIsOutOfMemory() {
		Memory memory = new Memory();
		memory.setEnabled(true);

		long maxMemory = memory.getMax();
		long initialMemory = memory.getInitial();

		assertFalse(memory.isOutOfMemory());

		// Simulate memory usage by setting initial memory to a lower value
		memory.setInitial(initialMemory - 1000000);
		assertTrue(memory.isOutOfMemory());
	}

	@Test(expected = NullPointerException.class)
	public void testShowOutOfMemoryNullGui() {
		Memory memory = new Memory();
		memory.setEnabled(true);
		memory.showOutOfMemory();
		memory = null;
	}
}