import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MemoryTest {
   @Test
   public void testIsEnabled() {
       Memory memory = new Memory();
       boolean enabled = memory.isEnabled();
       assertEquals(true, enabled);

       memory.setEnabled(false);
       enabled = memory.isEnabled();
       assertEquals(false, enabled);
   }
}