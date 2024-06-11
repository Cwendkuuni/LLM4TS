package weka.core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

	@Test
	public void testMemory() {
		Memory memory = new Memory();
		Memory memoryWithGUI = new Memory(true);

		// Test constructor without GUI
		assertEquals(false, memory.getUseGUI());
		assertEquals(Memory.m_Initial, memory.getInitial());
		assertEquals(Memory.m_Total, memory.getCurrent());
		assertEquals(Memory.m_Max, memory.getMax());
		assertEquals(false, memory.isEnabled());
		assertFalse(memory.isOutOfMemory());

		// Test constructor with GUI
		assertEquals(true, memoryWithGUI.getUseGUI());
		assertEquals(Memory.m_Initial, memoryWithGUI.getInitial());
		assertEquals(Memory.m_Total, memoryWithGUI.getCurrent());
		assertEquals(Memory.m_Max, memoryWithGUI.getMax());
		assertEquals(Memory.m_Enabled, memoryWithGUI.isEnabled());
		assertFalse(memoryWithGUI.isOutOfMemory());

		// Test setting enabled flag
		memory.setEnabled(true);
		memoryWithGUI.setEnabled(false);
		assertEquals(true, memory.isEnabled());
		assertEquals(false, memoryWithGUI.isEnabled());

		// Test isOutOfMemory method
		long currentMemory = Memory.m_Total - 5000000; // Decrease total memory by 5MB
		Memory.m_Total = currentMemory;
		assertTrue(new Memory().isOutOfMemory());
		assertFalse(new Memory(true).isOutOfMemory());

		// Test toMegaByte method
		assertEquals(1.5, Memory.toMegaByte(1572864), 0.0); // 1.5MB
		assertEquals(1.25, Memory.toMegaByte(1310720), 0.0); // 1.25MB

		// Test showOutOfMemory method
		Memory memoryWithOutOfMemory = new Memory();
		memoryWithOutOfMemory.setEnabled(true);
		memoryWithOutOfMemory.showOutOfMemory();
	}

	@Test
	public void testStaticInitialization() {
		Memory memory = new Memory();
		assertEquals(Memory.m_Enabled, true);
		assertEquals(Memory.m_Initial, Runtime.getRuntime().totalMemory());
	}

	@Test(expected = NullPointerException.class)
	public void testStopThreadsWithNullThreadGroup() {
		Memory memory = new Memory();
		Thread[] thGroup = null;
		memory.stopThreads();
	}
}