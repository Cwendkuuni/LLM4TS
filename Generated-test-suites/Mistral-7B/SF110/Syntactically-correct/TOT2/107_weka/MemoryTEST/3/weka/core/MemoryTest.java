import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static weka.core.Utils.doubleToString;

public class MemoryTest {

 @Test
 public void TestMemoryConstructor() {
 Memory memory = new Memory();
 }

 @Test
 public void TestMemoryConstructorWithUseGUI() {
 Memory memory = new Memory(true);
 }

 @Test
 public void TestIsEnabled() {
 Memory memory = new Memory();
 assertTrue(memory.isEnabled());
 Memory.setEnabled(false);
 assertFalse(memory.isEnabled());
 }

 @Test
 public void TestSetEnabled() {
 Memory memory = new Memory();
 memory.setEnabled(true);
 assertTrue(memory.isEnabled());
 }

 @Test
 public void TestGetUseGUI() {
 Memory memory = new Memory();
 assertFalse(memory.getUseGUI());
 memory.setUseGUI(true);
 assertTrue(memory.getUseGUI());
 }

 @Test
 public void TestGetInitial() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 long max = memory.getMax();
 long current = memory.getCurrent();
 long initialValue = Runtime.getRuntime().totalMemory();
 assertEquals(initial, initialValue);
 assertEquals(max, initial + Runtime.maxMemory());
 assertEquals(current, initialValue + Runtime.totalMemory() - initialValue);
 }

 @Test
 public void TestGetCurrent() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 long max = memory.getMax();
 long current = memory.getCurrent();
 long initialValue = Runtime.getRuntime().totalMemory();
 assertEquals(current, initialValue + Runtime.totalMemory() - initialValue);
 }

 @Test
 public void TestGetMax() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 long max = memory.getMax();
 long total = memory.getTotal();
 assertEquals(max, total);
 }

 @Test
 public void TestIsOutOfMemory() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 long max = memory.getMax();
 long current = initial + 10 * 1024 * 1024; // 10MB
 assertEquals(memory.isOutOfMemory(), false);
 memory.setInitial(initial - 5 * 1024 * 1024); // 5MB less than initial
 assertEquals(memory.isOutOfMemory(), true);
 memory.setInitial(initial); // reset initial
 }

 @Test
 public void TestToMegaByte() {
 long bytes = 1024 * 1024 * 1024; // 1GB
 assertEquals(Memory.toMegaByte(bytes), 1.0, 0.0);
 }

 @Test(expected = NullPointerException.class)
 public void TestShowOutOfMemoryWithNullMessage() {
 Memory memory = new Memory();
 memory.showOutOfMemory(null);
 }

 @Test
 public void TestShowOutOfMemory() {
 Memory memory = new Memory();
 memory.showOutOfMemory();
 }

 @Test
 public void TestStopThreads() {
 Memory memory = new Memory();
 memory.stopThreads();
 }

 @Test(expected = NullPointerException.class)
 public void TestStopThreadsWithNullThreads() {
 Memory memory = new Memory();
 memory.stopThreads(null);
 }

 @Test
 public void TestGetRevision() {
 Memory memory = new Memory();
 String revision = memory.getRevision();
 assertEquals(revision, "$Revision: 8034 $");
 }

 @Test
 public void TestMain() {
 Memory memory = new Memory();
 System.out.println("Initial memory: " + doubleToString(Memory.toMegaByte(memory.getInitial()), 1) + "MB" + " (" + memory.getInitial() + ")");
 System.out.println("Max memory: " + doubleToString(Memory.toMegaByte(memory.getMax()), 1) + "MB" + " (" + memory.getMax() + ")");
 }

 @Test
 public void TestMemoryInitialAndMaxEqualsCurrent() {
 Memory memory = new Memory();
 memory.setInitial(memory.getCurrent());
 memory.setMax(memory.getCurrent());
 assertEquals(memory.isOutOfMemory(), false);
 }

 @Test
 public void TestMemoryInitialLessThanCurrent() {
 Memory memory = new Memory();
 memory.setInitial(memory.getCurrent() - 1024 * 1024); // 1MB less than current
 assertEquals(memory.isOutOfMemory(), false);
 }

 @Test
 public void TestMemoryInitialGreaterThanCurrent() {
 Memory memory = new Memory();
 memory.setInitial(memory.getCurrent() + 1024 * 1024); // 1MB more than current
 assertEquals(memory.isOutOfMemory(), true);
 }

 @Test
 public void TestIsOutOfMemoryWithInitialLessThanCurrent() {
 Memory memory = new Memory();
 long initial = memory.getInitial() - 5 * 1024 * 1024; // 5MB less than current
 memory.setInitial(initial);
 assertEquals(memory.isOutOfMemory(), true);
 }

 @Test
 public void TestIsOutOfMemoryWithInitialEqualsCurrent() {
 Memory memory = new Memory();
 long initial = memory.getInitial();
 memory.setInitial(initial);
 assertEquals(memory.isOutOfMemory(), false);
 }

 @Test
 public void TestNegativeInitialMemory() {
 Memory memory = new Memory();
 try {
 memory.setInitial(-1);
 } catch (IllegalArgumentException e) {
 assertEquals(e.getMessage(), "Invalid memory size: -1");
 }
 }

 @Test
 public void TestNegativeMaxMemory() {
 Memory memory = new Memory();
 try {
 memory.setMax(-1);
 } catch (IllegalArgumentException e) {
 assertEquals(e.getMessage(), "Invalid memory size: -1");
 }
 }

 @Test
 public void TestNullUseGUI() {
 try {
 new Memory(null);
 } catch (NullPointerException e) {
 assertEquals(e.getMessage(), "useGUI is marked non-final");
 }
 }

 @Test
 public void TestNullThreads() {
 Memory memory = new Memory();
 memory.stopThreads(null);
 }

}