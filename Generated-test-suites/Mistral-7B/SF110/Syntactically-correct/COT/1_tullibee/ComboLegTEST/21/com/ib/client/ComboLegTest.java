import org.junit.Assert;
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {

    @Test
    public void testComboLegConstructorWithAllArguments() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN, 1, "DesignatedLocation1");
        Assert.assertEquals(1, comboLeg.getm_conId());
        Assert.assertEquals(2, comboLeg.getm_ratio());
        Assert.assertEquals("Action1", comboLeg.getm_action());
        Assert.assertEquals("Exchange1", comboLeg.getm_exchange());
        Assert.assertEquals(ComboLeg.OPEN, comboLeg.getm_openClose());
        Assert.assertEquals(1, comboLeg.getm_shortSaleSlot());
        Assert.assertEquals("DesignatedLocation1", comboLeg.getm_designatedLocation());
    }

    @Test
    public void testComboLegConstructorWithNoArguments() {
        ComboLeg comboLeg = new ComboLeg();
        Assert.assertEquals(0, comboLeg.getm_conId());
        Assert.assertEquals(0, comboLeg.getm_ratio());
        Assert.assertNull(comboLeg.getm_action());
        Assert.assertNull(comboLeg.getm_exchange());
        Assert.assertEquals(ComboLeg.SAME, comboLeg.getm_openClose());
        Assert.assertNull(comboLeg.getm_designatedLocation());
    }

    @Test
    public void testComboLegConstructorWithSomeArguments() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1");
        Assert.assertEquals(1, comboLeg.getm_conId());
        Assert.assertEquals(2, comboLeg.getm_ratio());
        Assert.assertEquals("Action1", comboLeg.getm_action());
        Assert.assertEquals("Exchange1", comboLeg.getm_exchange());
        Assert.assertEquals(ComboLeg.SAME, comboLeg.getm_openClose());
        Assert.assertNull(comboLeg.getm_shortSaleSlot());
        Assert.assertNull(comboLeg.getm_designatedLocation());
    }

    @Test
    public void testComboLegEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN, 1, "DesignatedLocation1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN, 1, "DesignatedLocation1");
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange2", ComboLeg.OPEN, 1, "DesignatedLocation1");
        ComboLeg comboLeg4 = new ComboLeg(2, 2, "Action1", "Exchange1", ComboLeg.OPEN, 1, "DesignatedLocation1");

        Assert.assertTrue(comboLeg1.equals(comboLeg2));
        Assert.assertFalse(comboLeg1.equals(comboLeg3));
        Assert.assertFalse(comboLeg1.equals(comboLeg4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegConstructorWithNegativeRatio() {
        new ComboLeg(-1, 2, "Action1", "Exchange1", ComboLeg.OPEN, 1, "DesignatedLocation1");
    }

    @Test(expected = NullPointerException.class)
    public void testComboLegConstructorWithNullAction() {
        new ComboLeg(1, 2, null, "Exchange1", ComboLeg.OPEN, 1, "DesignatedLocation1");
    }

    @Test(expected = NullPointerException.class)
    public void testComboLegConstructorWithNullExchange() {
        new ComboLeg(1, 2, "Action1", null, ComboLeg.OPEN, 1, "DesignatedLocation1");
    }

    @Test(expected = NullPointerException.class)
    public void testComboLegConstructorWithNullDesignatedLocation() {
        new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN, 1, null);
    }
}