import static org.junit.Assert.*;

public class MemoryTest {
   // ...

   @Test
   public void testIsEnabled() {
      Memory memory = new Memory();
      assertTrue(memory.isEnabled());
      Memory memoryFalse = new Memory(false);
      assertFalse(memoryFalse.isEnabled());
   }
}