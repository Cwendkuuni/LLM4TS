import org.junit.Before;
import org.junit.Test;
import src.VassalRebellion;
import src.Fiefdoms;
import java.util.*;

public class VassalRebellionTest {
    VassalRebellion vassalRebellion;
    Fiefdoms fiefdoms;

    @Before
    public void setUp() {
        fiefdoms = new Fiefdoms();
        vassalRebellion = new VassalRebellion();
        Map.map.put("testFiefdom", new Fiefdom("testFiefdom", 5, 2, new ArrayList<>()));
        fiefdoms.setMap(Map.map);
        vassalRebellion.setFiefdoms(fiefdoms);
    }

    @Test
    public void testPerform() {
        Collection<String> col = new ArrayList<>();
        col.add("testFiefdom");
        vassalRebellion.perform(col);
        // Add assertions here
    }
}