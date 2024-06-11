package weka.core.test;

import static org.junit.Assert.*;
import static weka.core.Memory.toMegaByte;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.BeforeClass;
import org.junit.Test;

import weka.core.Memory;
import weka.core.RevisionUtils;
import weka.core.Utils;

public class MemoryTest {

	private static Memory memory;
	private static long initialMemory;
	private static long maxMemory;

	@BeforeClass
	public static void setup() {
		memory = new Memory();
		initialMemory = memory.getInitial();
		maxMemory = memory.getMax();
	}

	@Test
	public void testMemoryInitialization() {
		Memory memory = new Memory();
		assertEquals(false, memory.isEnabled());
		assertEquals(false, memory.getUseGUI());
		assertEquals(Runtime.getRuntime().totalMemory(), memory.getTotal());
		assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
		assertEquals(initialMemory, memory.getInitial());
	}

	@Test
	public void testMemoryEnabled() {
		Memory memory = new Memory(true);
		assertEquals(true, memory.isEnabled());
		assertEquals(true, memory.getUseGUI());
		assertEquals(Runtime.getRuntime().totalMemory(), memory.getTotal());
		assertEquals(Runtime.getRuntime().maxMemory(), memory.getMax());
		assertEquals(initialMemory, memory.getInitial());
	}

	@Test
	public void testIsOutOfMemory() {
		Memory memory = new Memory();
		long currentMemory = memory.getTotal();
		Memory.m_Enabled = true;
		memory.setEnabled(true);
		long maxMemoryUsed = currentMemory + 200001L;
		Field maxMemoryField = Memory.class.getDeclaredField("m_Max");
		maxMemoryField.setAccessible(true);
		maxMemoryField.set(null, maxMemoryUsed);
		assertTrue(memory.isOutOfMemory());
	}

	@Test
	public void testShowOutOfMemory() {
		Memory memory = new Memory();
		Memory.m_Enabled = true;
		memory.setEnabled(true);
		try {
			Field showOutOfMemoryField = Memory.class.getDeclaredField("m_UseGUI");
			showOutOfMemoryField.setAccessible(true);
			showOutOfMemoryField.set(memory, true);
			Runtime.getRuntime().gc();
			memory.showOutOfMemory();
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
			fail("Error occurred during test execution");
		} finally {
			Memory.m_Enabled = false;
			try {
				Field m_UseGUIField = Memory.class.getDeclaredField("m_UseGUI");
				m_UseGUIField.setAccessible(true);
				m_UseGUIField.set(memory, false);
			} catch (IllegalAccessException | NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		String expectedMsg = "Not enough memory. Please load a smaller dataset or use larger heap size.\n- initial JVM size:   ?";
		String actualMsg = Utils.getDelimitedString(System.err.getErrorStream(), "\n");
		assertTrue(actualMsg.contains(expectedMsg));
	}

	@Test(expected = SecurityException.class)
	public void testStopThreads() {
		Memory memory = new Memory();
		memory.stopThreads();
	}

	@Test
	public void testGetRevision() {
		assertEquals("$Revision: 8034 $", memory.getRevision());
	}

	@Test
	public void testMain() {
		Memory.main(new String[0]);
		String expectedOutput = "Initial memory: " + toMegaByte(initialMemory) + "MB" + " (" + initialMemory + ")\n"
				+ "Max memory: " + toMegaByte(maxMemory) + "MB" + " (" + maxMemory + ")\n";
		String actualOutput = System.out.getHistory(0).toString();
		assertEquals(expectedOutput, actualOutput);
	}
}