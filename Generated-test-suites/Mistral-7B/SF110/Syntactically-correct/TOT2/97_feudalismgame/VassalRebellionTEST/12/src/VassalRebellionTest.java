import org.junit.Before;
import org.junit.Test;
import src.VassalRebellion;
import src.Fiefdoms;
import java.util.*;

public class DawbinTest1 {
    private VassalRebellion vassalRebellion;
    private Fiefdoms fiefdomObj;

    @Before
    public void setup() {
        this.vassalRebellion = new VassalRebellion();
        this.fiefdomObj = new Fiefdoms("TestFiefdom");
        Map.map.put("TestFiefdom", this.fiefdomObj);
    }

    @Test
    public void testPerformMethod() {
        Collection<String> collection = new ArrayList<>();
        collection.add("TestFiefdom");
        this.vassalRebellion.perform(collection);
    }

    @Test(expected = NullPointerException.class)
    public void testPerformWithNullCollection() {
        Collection<String> collection = null;
        this.vassalRebellion.perform(collection);
    }

    @Test
    public void testPerformWithEmptyCollection() {
        Collection<String> collection = new ArrayList<>();
        this.vassalRebellion.perform(collection);
    }

    @Test
    public void testPerformWithMultipleFiefdoms() {
        Collection<String> collection = new ArrayList<>();
        collection.add("TestFiefdom1");
        collection.add("TestFiefdom2");
        this.vassalRebellion.perform(collection);
    }

    @Test
    public void testPerformWithNoFiefdomObject() {
        Collection<String> collection = new ArrayList<>();
        collection.add("TestNoFiefdom");
        this.vassalRebellion.perform(collection);
    }

    @Test
    public void testPerformWithNoLoyalVassals() {
        Fiefdoms fiefdomObjNoVassals = new Fiefdoms("TestFiefdomNoVassals");
        Map.map.put("TestFiefdomNoVassals", fiefdomObjNoVassals);
        Collection<String> collection = new ArrayList<>();
        collection.add("TestFiefdomNoVassals");
        this.vassalRebellion.perform(collection);
    }

    @Test
    public void testPerformWithNoPersonalCastlesAndNoFiefdomCastles() {
        Fiefdoms fiefdomObjNoCastles = new Fiefdoms("TestFiefdomNoCastles");
        fiefdomObjNoCastles.setPersonalCastles(0);
        fiefdomObjNoCastles.setFiefdomCastles(0);
        Map.map.put("TestFiefdomNoCastles", fiefdomObjNoCastles);
        Collection<String> collection = new ArrayList<>();
        collection.add("TestFiefdomNoCastles");
        this.vassalRebellion.perform(collection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPerformWithTooManyRebelliousVassals() {
        this.fiefdomObj.setLoyalVassals(new ArrayList<>());
        this.fiefdomObj.setLoyalVassals(Collections.nCopies(100, new Vassal()));
        this.vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
    }

    @Test
    public void testPerformWithRebelliousVassals() {
        this.fiefdomObj.setLoyalVassals(new ArrayList<>());
        this.fiefdomObj.setLoyalVassals(Collections.nCopies(15, new Vassal()));
        this.vassalRebellion.perform(Collections.singletonList("TestFiefdom"));
    }

    @Test
    public void testRollEventMethod() {
        Collection<String> collection = new ArrayList<>();
        collection.add("TestFiefdom");
        this.vassalRebellion.perform(collection);
        assert(Map.map.get("TestFiefdom").isKingdom() == false);
    }
}