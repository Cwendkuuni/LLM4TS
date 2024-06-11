import static org.junit.Assert.*;
import src.*;

public class VassalRebellionTest {
   //...

   @Test
   public void testPerform() {
      Fiefdoms fiefdom = new Fiefdoms("TestFiefdom");
      VassalRebellion vassalRebellion = new VassalRebellion();
      List<String> collection = new ArrayList<>();
      collection.add("TestFiefdom");
      vassalRebellion.perform(collection);
      // Add assertions here to check the state of fiefdomObj after the perform call
   }
}