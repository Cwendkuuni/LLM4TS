import org.junit.Assert;
import org.junit.Test;
import static weka.core.Memory.*;

public class MemoryTest {

 private Memory memory;

 @Test
 public void testMemoryInitialization() {
 memory = new Memory();
 Assert.assertEquals(Memory.m_Enabled, true);
 Assert.assertEquals(toMegaByte(getInitial()), Memory.m_Initial / 1024.0 / 1024.0, 0.01);
 }

 @Test
 public void testMemoryGetters() {
 memory = new Memory(true);
 Assert.assertEquals(memory.getUseGUI(), true);
 Assert.assertEquals(memory.isEnabled(), Memory.m_Enabled);
 Assert.assertEquals(toMegaByte(memory.getInitial()), Memory.m_Initial / 1024.0 / 1024.0, 0.01);
 Assert.assertEquals(toMegaByte(memory.getCurrent()), toMegaByte(Runtime.getRuntime().totalMemory()) / 1024.0 / 1024.0, 0.01);
 Assert.assertEquals(toMegaByte(memory.getMax()), Memory.m_Max / 1024.0 / 1024.0, 0.01);
 Assert.assertFalse(memory.isOutOfMemory());
 }

 @Test
 public void testMemoryOutOfMemory() {
 memory = new Memory();
 Runtime.getRuntime().gc();
 Runtime.getRuntime().runFinalization();
 Runtime.getRuntime().freeMemory(); // free some memory
 Memory.m_Enabled = true;
 memory.setEnabled(true);
 Assert.assertTrue(memory.isOutOfMemory());
 }

 @Test
 public void testMemoryShowOutOfMemory() {
 memory = new Memory();
 memory.showOutOfMemory();
 }

 @Test(expected = NullPointerException.class)
 public void testThreadsNullArray() {
 memory = new Memory();
 Thread[] thGroup = null;
 memory.stopThreads();
 }

 @Test
 public void testMemoryToString() {
 memory = new Memory();
 String expected = "Initial memory: X.X MB (X)\nMax memory: Y.Y MB (X)\n";
 String actual = Memory.main(null).split("\\n")[0];
 Assert.assertEquals(expected.replace("X", String.valueOf((int) (toMegaByte(getInitial()))))
 .replace("Y", String.valueOf((int) (toMegaByte(getMax())))), actual);
 }
}