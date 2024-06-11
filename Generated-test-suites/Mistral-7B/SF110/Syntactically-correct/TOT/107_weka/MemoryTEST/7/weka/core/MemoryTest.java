package weka.core.test;

import static org.junit.Assert.*;

import weka.core.Memory;

import org.junit.Test;

public class MemoryTest {

	@Test
	public void testMemoryConstructor() {
		Memory memory = new Memory(true);
		assertEquals(true, memory.getUseGUI());
		assertEquals(Runtime.getRuntime().totalMemory(), memory.getCurrent());
	}

	@Test
	public void testMemoryIsOutOfMemory() {
		Memory memory = new Memory();
		memory.setEnabled(true);
		memory.m_Total = Runtime.getRuntime().totalMemory() - 1000000; // Reduce available memory
		assertTrue(memory.isOutOfMemory());
	}

	@Test
	public void testMemoryShowOutOfMemory() {
		Memory memory = new Memory();
		memory.setEnabled(true);
		try {
			memory.showOutOfMemory();
			fail("Expected an error message, but no error was thrown.");
		} catch (Exception e) {
			// Expected exception
		}
	}

	@Test
	public void testMemoryGetRevision() {
		Memory memory = new Memory();
		assertEquals("$Revision: 8034 $", memory.getRevision());
	}

	@Test
	public void testMemoryMain() {
		Memory memory = new Memory();
		Memory.main(new String[0]);
		assertTrue(memory.isEnabled());
		assertFalse(memory.isOutOfMemory());
	}
}