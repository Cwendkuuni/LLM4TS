import org.junit.Assert;
import org.junit.Test;
import src.Actions;
import src.Fiefdoms;
import src.VassalRebellion;

public class VassalRebellionTest {
    @Test
    public void testPerform() {
        Fiefdoms fiefdomObj = new Fiefdoms("TestFiefdom", 10, 5, new ArrayList<>(), new ArrayList<>(), true);
        Actions actions = new Actions();
        VassalRebellion vassalRebellion = new VassalRebellion();
        vassalRebellion.setFiefdomObj(fiefdomObj);

        List<String> col = new ArrayList<>();
        col.add(fiefdomObj.getName());

        vassalRebellion.perform(col);

        Assert.assertFalse(fiefdomObj.isKingdom());
        Assert.assertTrue(fiefdomObj.isRebellious());
    }

    // Add edge cases and exception handling tests here
}