import static org.junit.Assert.*;
import src.VassalRebellion;
import src.Fiefdoms;
import java.util.ArrayList;
import java.util.Collections;

public class VassalRebbellionTest {
    @Test
    public void testPerform() {
        Fiefdoms fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>());
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        assertFalse(fiefdomObj.isKingdom());
        assertTrue(fiefdomObj.isRebelliousVassals());
    }
}