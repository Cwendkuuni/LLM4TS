import static org.junit.Assert.*;
import src.VassalRebellion;
import src.Fiefdom;

public class VassalRebbellionTest {
    @Test
    public void testPerform() {
        Fiefdom fiefdom = new Fiefdom("TestFiefdom");
        VassalRebellion vassalRebellion = new VassalRebellion();
        List<String> collection = new ArrayList<>();
        collection.add(fiefdom.getName());
        vassalRebellion.perform(collection);
        assertFalse(fiefdom.isKingdom());
        assertTrue(fiefdom.getRebelliousVassals().size() > 0);
    }
}