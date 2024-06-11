import org.junit.Assert;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructorWithAllArguments() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, "NY");
        Assert.assertEquals(1, comboLeg.getm_conId());
        Assert.assertEquals(2, comboLeg.getm_ratio());
        Assert.assertEquals("BUY", comboLeg.getm_action());
        Assert.assertEquals("IBKR", comboLeg.getm_exchange());
        Assert.assertEquals(1, comboLeg.getm_openClose());
        Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
        Assert.assertEquals("NY", comboLeg.getm_designatedLocation());
    }

    @Test
    public void testConstructorWithDefaultArguments() {
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
    public void testConstructorWithPartialArguments() {
        ComboLeg comboLeg = new ComboLeg(1, "SELL", "IBKR", 2);
        Assert.assertEquals(1, comboLeg.getm_conId());
        Assert.assertNull(comboLeg.getm_ratio());
        Assert.assertEquals("SELL", comboLeg.getm_action());
        Assert.assertEquals("IBKR", comboLeg.getm_exchange());
        Assert.assertEquals(2, comboLeg.getm_openClose());
        Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
        Assert.assertNull(comboLeg.getm_designatedLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidRatio() {
        new ComboLeg(1, -2, "BUY", "IBKR", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullAction() {
        new ComboLeg(1, 2, null, "IBKR", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullExchange() {
        new ComboLeg(1, 2, "BUY", null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDesignatedLocation() {
        new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, null);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
        ComboLeg comboLeg3 = new ComboLeg(2, 2, "BUY", "IBKR", 1);
        Assert.assertTrue(comboLeg1.equals(comboLeg2));
        Assert.assertFalse(comboLeg1.equals(comboLeg3));
        Assert.assertFalse(comboLeg1.equals(null));
    }

    @Test
    public void testGetters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 1);
        Assert.assertEquals(1, comboLeg.getm_conId());
        Assert.assertEquals(2, comboLeg.getm_ratio());
        Assert.assertEquals("BUY", comboLeg.getm_action());
        Assert.assertEquals("IBKR", comboLeg.getm_exchange());
        Assert.assertEquals(1, comboLeg.getm_openClose());
        Assert.assertEquals(0, comboLeg.getm_shortSaleSlot());
        Assert.assertEquals("NY", comboLeg.getm_designatedLocation());
    }

    @Test
    public void testHashCode() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, "NY");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, "NY");
        Assert.assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
    }
}