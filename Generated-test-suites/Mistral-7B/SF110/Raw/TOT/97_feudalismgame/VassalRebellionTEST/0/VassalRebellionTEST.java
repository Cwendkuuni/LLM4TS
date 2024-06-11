import static org.junit.Assert.*;
import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {
    

    @Test
    public void testRebellionWithLoyalVassals() {
        Fiefdoms map = new Fiefdoms();
        Fiefdom fiefdom = new Fiefdom("TestFiefdom", 10, 5, 5, new ArrayList<>());
        map.put("TestFiefdom", fiefdom);
        VassalRebellion rebellion = new VassalRebellion();

        ArrayList<String> collection = new ArrayList<>();
        collection.add("TestFiefdom");

        rebellion.perform(collection);

        assertTrue(fiefdom.isRebellious());
        assertFalse(fiefdom.isKingdom());

        int loyalVassalsBeforeRebellion = fiefdom.getLoyalVassals().size();
        int loyalVassalsAfterRebellion = fiefdom.getRebelliousVassals().size();

        int personalCastles = fiefdom.getPersonalCastles();
        int fiefdomCastles = fiefdom.getFiefdomCastles();

        if (personalCastles > 0 || fiefdomCastles > 0) {
            int vassalsToBeRemoved = personalCastles * 10 + fiefdomCastles * 25;
            int loyalVassalsAfterCastleRemoval = fiefdom.getLoyalVassals().size() - vassalsToBeRemoved;
            assertEquals(loyalVassalsBeforeRebellion - loyalVassalsAfterCastleRemoval, loyalVassalsAfterRebellion);
        }

        assertTrue(fiefdom.getLoyalVassals().isEmpty());
        assertTrue(fiefdom.getRebelliousVassals().size() > 0);
    }

    
}