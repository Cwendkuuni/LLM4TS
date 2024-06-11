import org.junit.Before;
import org.junit.Test;
import src.Fiefdoms;
import src.VassalRebellion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VassalRebellionTest {
    private Fiefdoms fiefdomObj;
    private VassalRebellion vassalRebellion;
    private List<String> fiefdomNames;

    @Before
    public void setup() {
        fiefdomObj = new Fiefdoms("Fiefdom1", 5, 3, 5, 10);
        fiefdomNames = new ArrayList<>();
        fiefdomNames.add("Fiefdom1");
        Map<String, Fiefdoms> map = new HashMap<>();
        map.put("Fiefdom1", fiefdomObj);

        vassalRebellion = new VassalRebellion();
        vassalRebellion.setMap(map);
    }

    @Test
    public void testPerform() {
        vassalRebellion.perform(fiefdomNames);
        assertTrue(fiefdomObj.isRebellious());
        assertTrue(fiefdomObj.isKingdom());
    }
}