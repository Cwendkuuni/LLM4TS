import org.junit.Before;
import org.junit.Test;
import src.Fiefdoms;
import src.VassalRebellion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class VassalRebellionTest1 {
    private Fiefdoms map;
    private VassalRebellion vassalRebellion;

    @Before
    public void setup() {
        map = new Fiefdoms();
        map.put("Fiefdom1", new Fiefdoms("Fiefdom1", 10, 5, 15, new ArrayList<>()));
        vassalRebellion = new VassalRebellion();
    }

    @Test
    public void testPerform_LoyalVassals_NoRebellion() {
        vassalRebellion.perform(map.get("Fiefdom1").getVassals());
        assertFalse(map.get("Fiefdom1").isRebellious());
        assertTrue(map.get("Fiefdom1").isKingdom());
        assertEquals(15, map.get("Fiefdom1").getLoyalVassals().size());
    }

    @Test
    public void testPerform_LoyalVassals_Rebellion() {
        map.get("Fiefdom1").setValue(14);
        vassalRebellion.perform(map.get("Fiefdom1").getVassals());
        assertTrue(map.get("Fiefdom1").isRebellious());
        assertFalse(map.get("Fiefdom1").isKingdom());
        assertEquals(1, map.get("Fiefdom1").getRebelliousVassals().size());
    }

    @Test(expected = NullPointerException.class)
    public void testPerform_EmptyFiefdom() {
        vassalRebellion.perform(null);
    }

    @Test
    public void testPerform_PersonalCastlesAndFiefdomCastles() {
        final int personalCastles = 3;
        final int fiefdomCastles = 2;
        map.put("Fiefdom2", new Fiefdoms("Fiefdom2", 15, personalCastles, fiefdomCastles, new ArrayList<>()));
        vassalRebellion.perform(map.get("Fiefdom2").getVassals());

        final int vassalsBeforeRebellion = map.get("Fiefdom2").getLoyalVassals().size() - (personalCastles * 10 + fiefdomCastles * 25);
        vassalRebellion.perform(map.get("Fiefdom2").getVassals());

        assertTrue(map.get("Fiefdom2").isRebellious());
        assertFalse(map.get("Fiefdom2").isKingdom());
        assertEquals(vassalsBeforeRebellion, map.get("Fiefdom2").getRebelliousVassals().size());
    }
}