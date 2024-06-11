import static org.junit.Assert.*;
import src.VassalRebellion;
import src.Fiefdoms;
import java.util.*;

public class VassalRebellionTest {
    @Test
    public void testPerform() {
        Fiefdoms fiefdom = new Fiefdoms();
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("TestFiefdom");
        vassalRebellion.perform(col);
        assertFalse(fiefdom.isKingdom());
    }
}