package src.test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {

    private Map<String, Fiefdoms> map;
    private VassalRebellion vassalRebellion;

    @Before
    public void setUp() {
        map = new HashMap<>();
        Fiefdoms fiefdom1 = new Fiefdoms("Fiefdom1", 10, 5, new ArrayList<>(), 5, true);
        Fiefdoms fiefdom2 = new Fiefdoms("Fiefdom2", 0, 0, new ArrayList<>(), 0, true);
        map.put("Fiefdom1", fiefdom1);
        map.put("Fiefdom2", fiefdom2);
        vassalRebellion = new VassalRebellion();
        vassalRebellion.setMap(map);
    }

    @Test
    public void testVassalRebellion_Loyal_Vassals_And_Castles() {
        Collection<String> collection = new ArrayList<>();
        collection.add("Fiefdom1");

        vassalRebellion.perform(collection);

        assertEquals(5, vassalRebellion.getFiefdomObj().getRebelliousVassals().size());
        assertEquals(false, vassalRebellion.getFiefdomObj().isKingdom());
        assertEquals(10, vassalRebellion.getFiefdomObj().getPersonalCastles());
        assertEquals(5, vassalRebellion.getFiefdomObj().getFiefdomCastles());
    }

    @Test
    public void testVassalRebellion_No_Loyal_Vassals() {
        Collection<String> collection = new ArrayList<>();
        collection.add("Fiefdom2");

        vassalRebellion.perform(collection);

        assertEquals(0, vassalRebellion.getFiefdomObj().getRebelliousVassals().size());
        assertEquals(true, vassalRebellion.getFiefdomObj().isKingdom());
    }

    @Test
    public void testVassalRebellion_Negative_Loyal_Vassals() {
        Collection<String> collection = new ArrayList<>();
        collection.add("Fiefdom1");

        Fiefdoms fiefdom1 = map.get("Fiefdom1");
        fiefdom1.setLoyalVassals(new ArrayList<>());

        vassalRebellion.perform(collection);

        assertEquals(0, vassalRebellion.getFiefdomObj().getRebelliousVassals().size());
        assertEquals(true, vassalRebellion.getFiefdomObj().isKingdom());
    }

    @Test
    public void testVassalRebellion_Zero_Personal_Castles() {
        Collection<String> collection = new ArrayList<>();
        collection.add("Fiefdom1");

        vassalRebellion.getFiefdomObj().setPersonalCastles(0);

        vassalRebellion.perform(collection);

        assertEquals(5, vassalRebellion.getFiefdomObj().getRebelliousVassals().size());
        assertEquals(false, vassalRebellion.getFiefdomObj().isKingdom());
        assertEquals(0, vassalRebellion.getFiefdomObj().getPersonalCastles());
        assertEquals(5, vassalRebellion.getFiefdomObj().getFiefdomCastles());
    }

    @Test
    public void testVassalRebellion_Zero_Fiefdom_Castles() {
        Collection<String> collection = new ArrayList<>();
        collection.add("Fiefdom1");

        vassalRebellion.getFiefdomObj().setPersonalCastles(10);
        vassalRebellion.getFiefdomObj().setFiefdomCastles(0);

        vassalRebellion.perform(collection);

        assertEquals(5, vassalRebellion.getFiefdomObj().getRebelliousVassals().size());
        assertEquals(false, vassalRebellion.getFiefdomObj().isKingdom());
        assertEquals(10, vassalRebellion.getFiefdomObj().getPersonalCastles());
        assertEquals(0, vassalRebellion.getFiefdomObj().getFiefdomCastles());
    }

    @Test
    public void testVassalRebellion_Empty_Collection() {
        Collection<String> collection = new ArrayList<>();

        vassalRebellion.perform(collection);

        assertEquals(0, vassalRebellion.getFiefdomObj().getRebelliousVassals().size());
        assertEquals(true, vassalRebellion.getFiefdomObj().isKingdom());
    }

    @Test
    public void testVassalRebellion_Invalid_Fiefdom_Name() {
        Collection<String> collection = new ArrayList<>();
        collection.add("InvalidFiefdomName");

        vassalRebellion.perform(collection);

        assertEquals(0, vassalRebellion.getFiefdomObj().getRebelliousVassals().size());
        assertEquals(true, vassalRebellion.getFiefdomObj().isKingdom());
        assertNull(vassalRebellion.getFiefdomObj());
    }

    @Test
    public void testVassalRebellion_Null_Collection() {
        Collection<String> collection = null;

        vassalRebellion.perform(collection);

        assertEquals(0, vassalRebellion.getFiefdomObj().getRebelliousVassals().size());
        assertEquals(true, vassalRebellion.getFiefdomObj().isKingdom());
        assertNull(vassalRebellion.getFiefdomObj());
    }
}