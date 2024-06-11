package weka.core.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Test;

import weka.core.Memory;

public class MemoryTest {

	Memory memory;
	long initialMemory, maxMemory;

	@Test
	public void testMemoryConstructor() {
		memory = new Memory(true);
		initialMemory = Memory.m_Initial;
		maxMemory = memory.getMax();
		assertTrue(memory.isEnabled());
	}

	@Test
	public void testMemoryNoGUIConstructor() {
		memory = new Memory();
		initialMemory = Memory.m_Initial;
		maxMemory = memory.getMax();
		assertFalse(memory.getUseGUI());
	}

	@Test
	public void testIsEnabled() {
		memory = new Memory();
		memory.setEnabled(true);
		assertTrue(memory.isEnabled());
		memory.setEnabled(false);
		assertFalse(memory.isEnabled());
	}

	@Test
	public void testGetUseGUI() {
		memory = new Memory(true);
		assertTrue(memory.getUseGUI());
		memory = new Memory();
		assertFalse(memory.getUseGUI());
	}

	@Test
	public void testGetInitial() {
		assertEquals(initialMemory, Memory.m_Initial);
		memory = new Memory();
		assertEquals(Memory.m_Initial, memory.getInitial());
	}

	@Test
	public void testGetCurrent() {
		long currentMemory = memory.getCurrent();
		long maxCurrentMemory = memory.getMax();
		assertTrue(currentMemory <= maxCurrentMemory);
		memory.setEnabled(true);
		memory.showOutOfMemory();
		currentMemory = memory.getCurrent();
		assertTrue(currentMemory > maxCurrentMemory);
		memory.setEnabled(false);
		currentMemory = memory.getCurrent();
		assertTrue(currentMemory <= maxCurrentMemory);
	}

	@Test
	public void testGetMax() {
		assertEquals(maxMemory, memory.getMax());
		memory = new Memory();
		assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
	}

	@Test(expected = RuntimeException.class)
	public void testIsOutOfMemory() {
		memory.setEnabled(true);
		memory.setMax(memory.getInitial() - 100000L);
		memory.isOutOfMemory();
	}

	@Test
	public void testToMegaByte() {
		assertEquals(4.473214943, Memory.toMegaByte(4682734), 0.001);
		assertEquals(0, Memory.toMegaByte(0));
	}

	@Test
	public void testShowOutOfMemory() {
		memory.setEnabled(true);
		Memory.m_Initial = 100L;
		memory.m_Total = 200L;
		memory.m_Max = 500L;
		memory.showOutOfMemory();
	}

	@Test
	public void testStopThreads() {
		memory = new Memory();
		Thread[] threads = new Thread[10];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(() -> {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			threads[i].start();
		}
		memory.stopThreads();
		for (int i = 0; i < threads.length; i++) {
			if (threads[i].isAlive()) {
				fail("Thread " + i + " is still alive.");
			}
		}
	}

	@Test
	public void testGetRevision() {
		Method getRevisionMethod = ReflectionUtils.findMethod(Memory.class, "getRevision");
		assertEquals("$Revision: 8034 $", Memory.class.getDeclaredMethod("getRevision").invoke(memory));
	}

	@Test
	public void testMainMethod() {
		Memory memory = new Memory();
		assertEquals("Initial memory: 1.9MB (2056016)", memory.getClass().getDeclaredMethod("main", (Class[]) null).invoke(memory));
	}
}