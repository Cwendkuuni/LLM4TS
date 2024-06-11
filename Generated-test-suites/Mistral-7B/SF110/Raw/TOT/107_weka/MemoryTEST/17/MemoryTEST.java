package weka.core.test;

import static org.junit.Assert.*;
import weka.core.Memory;

import org.junit.Test;

public class MemoryTest {

	@Test
	public void testMemoryInitialization() {
		Memory memory = new Memory();
		assertEquals(Memory.m_Enabled, true);
		assertEquals(memory.getInitial(), Runtime.getRuntime().totalMemory());
		assertEquals(memory.getMax(), Runtime.getRuntime().maxMemory());
		assertEquals(memory.getTotal(), Runtime.getRuntime().totalMemory());
		assertEquals(memory.isOutOfMemory(), false);
	}

	@Test
	public void testMemoryEnabledFalse() {
		Memory memory = new Memory(false);
		assertEquals(Memory.m_Enabled, false);
		assertEquals(memory.isEnabled(), false);
		assertFalse(memory.isOutOfMemory());
	}

	@Test
	public void testMemoryEnabledTrueWithGUIfalse() {
		Memory memory = new Memory(true);
		assertEquals(Memory.m_Enabled, true);
		assertTrue(memory.isEnabled());
		assertEquals(memory.getUseGUI(), true);
		assertFalse(memory.isOutOfMemory());
	}

	@Test
	public void testMemoryEnabledTrueWithGUIture() {
		Memory memory = new Memory(true);
		assertEquals(Memory.m_Enabled, true);
		assertTrue(memory.isEnabled());
		assertEquals(memory.getUseGUI(), true);
		long maxMemory = Runtime.getRuntime().maxMemory() / 2;
		Memory.setInitial(Runtime.getRuntime().totalMemory() - maxMemory);
		memory.setEnabled(true);
		assertTrue(memory.isOutOfMemory());
		memory.showOutOfMemory();
	}

	@Test(expected = NullPointerException.class)
	public void testNullThreadGroup() {
		Memory memory = new Memory();
		Thread[] thGroup = null;
		memory.stopThreads();
	}

	@Test
	public void testGetRevision() {
		Memory memory = new Memory();
		String expected = "$Revision: 8034 $";
		assertEquals(memory.getRevision(), expected);
	}

	@Test
	public void testMain() {
		Memory memory = new Memory();
		java.lang.reflect.Method mainMethod = Memory.class.getMethod("main", new Class[] {String[].class});
		mainMethod.invoke(memory, new Object[] {new String[] {}});
		assertEquals(Memory.m_Enabled, true);
		assertTrue(memory.isEnabled());
		assertFalse(memory.isOutOfMemory());
		assertEquals(memory.getTotal(), Runtime.getRuntime().totalMemory());
		assertEquals(memory.getMax(), Runtime.getRuntime().maxMemory());
		assertEquals(memory.getInitial(), Runtime.getRuntime().totalMemory());
	}
}