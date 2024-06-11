import static org.junit.Assert.*;
import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        Fiefdoms fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>(), true, false, true);
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String, Fiefdoms> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        // Add assertions here based on the expected behavior of the method
    }
}