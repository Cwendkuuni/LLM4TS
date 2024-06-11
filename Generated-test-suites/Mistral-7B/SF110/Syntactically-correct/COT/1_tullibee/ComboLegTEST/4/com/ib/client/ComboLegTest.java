import org.junit.Assert;
import org.junit.Test;
import com.ib.client.ComboLeg;

public class ComboLegTest {

    @Test
    public void testConstructorZeroArgs() {
        ComboLeg comboLeg = new ComboLeg();
        Assert.assertEquals(0, comboLeg.m_conId);
        Assert.assertEquals(0, comboLeg.m_ratio);
        Assert.assertNull(comboLeg.m_action);
        Assert.assertNull(comboLeg.m_exchange);
        Assert.assertEquals(0, comboLeg.m_openClose);
        Assert.assertEquals(0, comboLeg.m_shortSaleSlot);
        Assert.assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructorWithArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN);
        Assert.assertEquals(1, comboLeg.m_conId);
        Assert.assertEquals(2, comboLeg.m_ratio);
        Assert.assertEquals("BUY", comboLeg.m_action);
        Assert.assertEquals("IBKR", comboLeg.m_exchange);
        Assert.assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
        Assert.assertEquals(0, comboLeg.m_shortSaleSlot);
        Assert.assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructorWithAllArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "DETROIT");
        Assert.assertEquals(1, comboLeg.m_conId);
        Assert.assertEquals(2, comboLeg.m_ratio);
        Assert.assertEquals("BUY", comboLeg.m_action);
        Assert.assertEquals("IBKR", comboLeg.m_exchange);
        Assert.assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
        Assert.assertEquals(3, comboLeg.m_shortSaleSlot);
        Assert.assertEquals("DETROIT", comboLeg.m_designatedLocation);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsNull() {
        ComboLeg comboLeg = new ComboLeg();
        Assert.assertNotEquals(null, comboLeg);
    }

    @Test
    public void testEqualsSameObject() {
        ComboLeg comboLeg1 = new ComboLeg();
        ComboLeg comboLeg2 = comboLeg1;
        Assert.assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentObjects() {
        ComboLeg comboLeg1 = new ComboLeg();
        ComboLeg comboLeg2 = new ComboLeg();
        Assert.assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsSameValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "DETROIT");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "DETROIT");
        Assert.assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "DETROIT");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.CLOSE, 3, "DETROIT");
        Assert.assertFalse(comboLeg1.equals(comboLeg2));

        ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "DETROIT");
        ComboLeg comboLeg4 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 4, "DETROIT");
        Assert.assertFalse(comboLeg3.equals(comboLeg4));

        ComboLeg comboLeg5 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 3, "DETROIT");
        ComboLeg comboLeg6 = new ComboLeg(1, 2, "SELL", "IBKR", ComboLeg.OPEN, 3, "DETROIT");
        Assert.assertFalse(comboLeg5.equals(comboLeg6));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDesignatedLocationNull() {
        ComboLeg comboLeg = new ComboLeg();
        comboLeg.setDesignatedLocation(null);
    }
}