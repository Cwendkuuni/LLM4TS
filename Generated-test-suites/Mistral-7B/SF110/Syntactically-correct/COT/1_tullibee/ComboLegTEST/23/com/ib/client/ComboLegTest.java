import org.junit.Assert; // for Assert.assertEquals, Assert.assertNull, etc.
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {

    @Test
    public void testConstructorNoArgs() {
        ComboLeg comboLeg = new ComboLeg();
        Assert.assertEquals(0, comboLeg.getm_conId());
        Assert.assertEquals(0, comboLeg.getm_ratio());
        Assert.assertNull(comboLeg.getm_action());
        Assert.assertNull(comboLeg.getm_exchange());
        Assert.assertEquals(0, comboLeg.getm_openClose());
        Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
        Assert.assertNull(comboLeg.getm_designatedLocation());
    }

    @Test
    public void testConstructorWithArgs() {
        int conId = 1;
        int ratio = 2;
        String action = "BUY";
        String exchange = "IBKR";
        int openClose = ComboLeg.OPEN;
        int shortSaleSlot = 3;
        String designatedLocation = "London";
        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
        Assert.assertEquals(conId, comboLeg.getm_conId());
        Assert.assertEquals(ratio, comboLeg.getm_ratio());
        Assert.assertEquals(action, comboLeg.getm_action());
        Assert.assertEquals(exchange, comboLeg.getm_exchange());
        Assert.assertEquals(openClose, comboLeg.getm_openClose());
        Assert.assertEquals(shortSaleSlot, comboLeg.getm_shortSaleSlot());
        Assert.assertEquals(designatedLocation, comboLeg.getm_designatedLocation());
    }

    @Test
    public void testConstructorWithAllArgs() {
        int conId = 1;
        int ratio = 2;
        String action = "SELL";
        String exchange = "NASDAQ";
        int openClose = ComboLeg.CLOSE;
        int shortSaleSlot = 3;
        String designatedLocation = "New York";
        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);
        Assert.assertEquals(conId, comboLeg.getm_conId());
        Assert.assertEquals(ratio, comboLeg.getm_ratio());
        Assert.assertEquals(action, comboLeg.getm_action());
        Assert.assertEquals(exchange, comboLeg.getm_exchange());
        Assert.assertEquals(openClose, comboLeg.getm_openClose());
        Assert.assertEquals(shortSaleSlot, comboLeg.getm_shortSaleSlot());
        Assert.assertEquals(designatedLocation, comboLeg.getm_designatedLocation());
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "London");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "London");
        ComboLeg comboLeg3 = new ComboLeg(1, 3, "BUY", "IBKR", ComboLeg.OPEN, 3, "London");
        Assert.assertTrue(comboLeg1.equals(comboLeg2));
        Assert.assertFalse(comboLeg1.equals(comboLeg3));
        Assert.assertFalse(comboLeg1.equals(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullExchange() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", null, ComboLeg.OPEN, 3, "London");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDesignatedLocation() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, null);
    }

}