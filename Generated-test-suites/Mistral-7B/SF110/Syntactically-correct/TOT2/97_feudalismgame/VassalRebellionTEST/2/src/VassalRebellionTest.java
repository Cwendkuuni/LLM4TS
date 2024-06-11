import static org.junit.Assert.*;
import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {
    @Test
    public void testPerform() {
        Fiefdoms testFiefdom = new Fiefdoms("TestFiefdom");
        testFiefdom.setValue(10);
        VassalRebellion vassalRebellion = new VassalRebellion();
        Collection<String, Fiefdoms> testCollection = new ArrayList<>();
        testCollection.add(testFiefdom.getName());
        vassalRebellion.perform(testCollection);
        assertTrue(testFiefdom.isRebellious());
        testFiefdom.setLoyalVassals(new ArrayList<>());
        vassalRebellion.perform(testCollection);
        assertFalse(testFiefdom.isRebellious());
    }
}