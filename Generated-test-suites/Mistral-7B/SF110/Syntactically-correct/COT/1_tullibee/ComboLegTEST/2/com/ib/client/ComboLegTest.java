import org.junit.Assert;
import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructorWithAllArguments() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 3, "DesignatedLocation1");
        Assert.assertEquals(1, comboLeg.getm_conId());
        Assert.assertEquals(2, comboLeg.getm_ratio());
        Assert.assertEquals("Action1", comboLeg.getm_action());
        Assert.assertEquals("Exchange1", comboLeg.getm_exchange());
        Assert.assertEquals(1, comboLeg.getm_openClose());
        Assert.assertEquals(3, comboLeg.getm_shortSaleSlot());
        Assert.assertEquals("DesignatedLocation1", comboLeg.getm_designatedLocation());
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
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Action1", "Exchange1", 1, 1, "DesignatedLocation1");
        ComboLeg comboLeg2 = new ComboLeg(1, 1, "Action1", "Exchange1", 1, 1, "DesignatedLocation1");

        Assert.assertTrue(comboLeg1.equals(comboLeg2));

        ComboLeg comboLeg3 = new ComboLeg(1, 1, "Action1", "Exchange1", 1, 2, "DesignatedLocation1");
        Assert.assertFalse(comboLeg1.equals(comboLeg3));

        ComboLeg comboLeg4 = new ComboLeg(1, 1, "Action1", "Exchange2", 1, 1, "DesignatedLocation1");
        Assert.assertFalse(comboLeg1.equals(comboLeg4));

        ComboLeg comboLeg5 = new ComboLeg(1, 1, "Action1", "Exchange1", 1, 1, null);
        Assert.assertFalse(comboLeg1.equals(comboLeg5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidShortSaleSlot() {
        new ComboLeg(1, 1, "Action1", "Exchange1", 1, Integer.MIN_VALUE, "DesignatedLocation1");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullDesignatedLocation() {
        new ComboLeg(1, 1, "Action1", "Exchange1", 1, 1, null);
    }
}