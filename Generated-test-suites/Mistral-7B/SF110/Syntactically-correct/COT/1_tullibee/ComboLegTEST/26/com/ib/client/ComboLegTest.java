import org.junit.Assert; // for assertions
import org.junit.Test; // for Test annotation
import com.ib.client.ComboLeg;

public class ComboLegTest {

    @Test
    public void testComboLegConstructorWithAllArguments() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", 1, 3, "NY");
        Assert.assertEquals(1, comboLeg.getm_conId());
        Assert.assertEquals(2, comboLeg.getm_ratio());
        Assert.assertEquals("BUY", comboLeg.getm_action());
        Assert.assertEquals("IBKR", comboLeg.getm_exchange());
        Assert.assertEquals(1, comboLeg.getm_openClose());
        Assert.assertEquals(3, comboLeg.getm_shortSaleSlot());
        Assert.assertEquals("NY", comboLeg.getm_designatedLocation());
    }

    @Test
    public void testComboLegDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        Assert.assertEquals(0, comboLeg.getm_conId());
        Assert.assertEquals(0, comboLeg.getm_ratio());
        Assert.assertNull(comboLeg.getm_action());
        Assert.assertNull(comboLeg.getm_exchange());
        Assert.assertEquals(0, comboLeg.getm_openClose());
        Assert.assertNull(comboLeg.getm_shortSaleSlot());
        Assert.assertNull(comboLeg.getm_designatedLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegConstructorWithNegativeRatio() {
        new ComboLeg(1, -2, "BUY", "IBKR", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegConstructorWithNullExchange() {
        new ComboLeg(1, 2, "BUY", null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegConstructorWithNullDesignatedLocation() {
        new ComboLeg(1, 2, "BUY", "IBKR", 1, 3, null);
    }

    @Test
    public void testComboLegEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
        Assert.assertTrue(comboLeg1.equals(comboLeg2));

        ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", 2);
        Assert.assertFalse(comboLeg1.equals(comboLeg3));

        ComboLeg comboLeg4 = new ComboLeg(2, 2, "BUY", "IBKR", 1);
        Assert.assertFalse(comboLeg1.equals(comboLeg4));

        ComboLeg comboLeg5 = new ComboLeg(1, 2, "BUY", null, 1);
        Assert.assertFalse(comboLeg1.equals(comboLeg5));
    }

    @Test
    public void testComboLegHashCode() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
        Assert.assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());

        ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", 2);
        Assert.assertNotEquals(comboLeg1.hashCode(), comboLeg3.hashCode());

        ComboLeg comboLeg4 = new ComboLeg(2, 2, "BUY", "IBKR", 1);
        Assert.assertNotEquals(comboLeg1.hashCode(), comboLeg4.hashCode());
    }
}