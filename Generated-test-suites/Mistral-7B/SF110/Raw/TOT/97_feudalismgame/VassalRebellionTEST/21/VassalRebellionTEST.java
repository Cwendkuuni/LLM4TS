package src.test; // make sure to import necessary packages
import static org.junit.Assert.*;
import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {
    
    @ org.junit.Before
    public void setUp() {
        // initialize objects and data structures
        Fiefdoms map = new Fiefdoms();
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdom fiefdom1 = new Fiefdom("Fiefdom1", 10, 5, 10, new ArrayList<>(), true, false);
        map.put("Fiefdom1", fiefdom1);

        // testing data for method under test
        List<String> input = new ArrayList<>();
        input.add("Fiefdom1");
        Collection<String> collection = input.iterator();
    }

    @ org.junit.Test
    public void testPerformWithLoyalVassalsAndCastles() {
        // given
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdom fiefdomObj = Map.map.get("Fiefdom1"); // get the fiefdom object from the map
        vassalRebellion.perform(collection); // invoke the method under test

        // then
        // check the state of the fiefdom object after the method call
        assertTrue(fiefdomObj.isRebellious());
        assertFalse(fiefdomObj.isKingdom());
        // check if the number of rebellious vassals is correct
        int expectedRebelliousVassals = fiefdomObj.getLoyalVassals().size() - 5; // calculating based on the condition in the method
        int actualRebelliousVassals = fiefdomObj.getRebelliousVassals().size();
        assertEquals(expectedRebelliousVassals, actualRebelliousVassals);

        // check if the castles are decreased
        int personalCastles = fiefdomObj.getPersonalCastles() - 1; // assuming there was 1 personal castle initially
        int fiefdomCastles = fiefdomObj.getFiefdomCastles() - 2; // assuming there were 2 fiefdom castles initially
        assertEquals(personalCastles, fiefdomObj.getPersonalCastles());
        assertEquals(fiefdomCastles, fiefdomObj.getFiefdomCastles());
    }

    @ org.junit.Test
    public void testPerformWithEmptyLoyalVassalsList() {
        // given
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdom fiefdomObj = Map.map.get("Fiefdom1"); // get the fiefdom object from the map
        vassalRebellion.perform(collection); // invoke the method under test

        // then
        // if there are no loyal vassals, a roll event should be triggered
        assertTrue(fiefdomObj.isRollEventTriggered());
    }
    
}