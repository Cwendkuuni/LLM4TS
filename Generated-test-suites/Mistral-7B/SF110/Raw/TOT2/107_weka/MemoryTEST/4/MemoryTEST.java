
import static org.junit.Assert.*;
import weka.core.Memory;
import static weka.core.Utils.doubleToString;

public class MemoryTest {

    @Test
    public void MemoryTest_ConstructorNoArgs() {
        Memory memory = new Memory();
        assertEquals(false, memory.m_UseGUI);
        assertEquals(false, Memory.m_Enabled);
        assertEquals(0, Memory.m_Initial);
        assertEquals(Runtime.getRuntime().totalMemory(), memory.m_Total);
        assertEquals(Runtime.getRuntime().maxMemory(), memory.m_Max);
    }

    // Add the remaining test cases here...

    @Test
    public void MemoryTest_ConstructorWithBooleanArg(boolean useGUI) {
        Memory memory = new Memory(useGUI);
        assertEquals(useGUI, memory.m_UseGUI);
        assertEquals(false, Memory.m_Enabled);
        assertEquals(Runtime.getRuntime().totalMemory(), memory.m_Total);
        assertEquals(Runtime.getRuntime().maxMemory(), memory.m_Max);
    }

    // Add the remaining test cases here...

    @Test
    public void MemoryTest_isEnabled() {
        Memory memory = new Memory();
        assertTrue(Memory.m_Enabled);
        memory.setEnabled(false);
        assertFalse(memory.isEnabled());
    }

    // Add the remaining test cases here...

    @Test
    public void MemoryTest_getUseGUI() {
        Memory memory = new Memory(true);
        assertTrue(memory.getUseGUI());
        memory = new Memory(false);
        assertFalse(memory.getUseGUI());
    }

    // Add the remaining test cases here...

    @Test
    public void MemoryTest_getInitial() {
        Memory memory = new Memory();
        assertEquals(Runtime.getRuntime().totalMemory(), memory.getInitial());
        memory = new Memory();
        Memory.m_Initial = 100;
        assertEquals(100, memory.getInitial());
    }

    // Add the remaining test cases here...

    // Repeat the same pattern for the remaining methods...

    @Test
    public void MemoryTest_isOutOfMemory() {
        Memory memory = new Memory();
        assertFalse(memory.isOutOfMemory());
        memory.setEnabled(false);
        memory.setMax(memory.getTotal() - 10000L);
        memory.setInitial(memory.getTotal() - 20001L);
        assertTrue(memory.isOutOfMemory());
    }

    // Add edge case tests here...

    // Repeat the same pattern for the remaining methods...
}