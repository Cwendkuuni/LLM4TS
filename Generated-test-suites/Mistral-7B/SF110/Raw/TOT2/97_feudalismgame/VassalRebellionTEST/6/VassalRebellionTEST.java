import org.junit.Test;
import static org.junit.Assert.*;

public class VassalRebellionTest {
   @Test
   public void testPerform() {
      VassalRebellion vassalRebellion = new VassalRebellion();
      Collection<String> col = new ArrayList<>();
      col.add("TestFiefdom");
      vassalRebellion.perform(col);
      // Add assertions here based on the expected behavior of the method
   }
}