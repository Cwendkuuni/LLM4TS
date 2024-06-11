import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testGetConId() {
        ComboLeg comboLeg = new ComboLeg(1, 1, "Action1", "Exchange1", 1, 1, "DesignatedLocation1");
        assertEquals(1, comboLeg.getConId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRatio() {
        ComboLeg comboLeg = new ComboLeg(1, -1, "Action1", "Exchange1", 1, 1, "DesignatedLocation1");
    }

    @Test
    public void testEmptyStrings() {
        ComboLeg comboLeg = new ComboLeg(1, 1, "", "", 1, 1, "");
        assertEquals("", comboLeg.getAction());
        assertEquals("", comboLeg.getExchange());
        assertEquals("", comboLeg.getDesignatedLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOpenClose() {
        ComboLeg comboLeg = new ComboLeg(1, 1, "Action1", "Exchange1", 5, 1, "DesignatedLocation1");
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsNull() {
        ComboLeg comboLeg = new ComboLeg(1, 1, "Action1", "Exchange1", 1, 1, "DesignatedLocation1");
        assertNotEquals(null, comboLeg);
    }

    @Test
    public void testEqualsDifferentObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Action1", "Exchange1", 1, 1, "DesignatedLocation1");
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "Action2", "Exchange2", 2, 2, "DesignatedLocation2");
        assertNotEquals(comboLeg1, comboLeg2);
    }

    @Test
    public void testEqualsSameObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 1, "Action1", "Exchange1", 1, 1, "DesignatedLocation1");
        ComboLeg comboLeg2 = comboLeg1;
        assertEquals(comboLeg1, comboLeg2);
    }

    @Test
    public void testEmptyConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.getConId());
        assertEquals(0, comboLeg.getRatio());
        assertEquals(null, comboLeg.getAction());
        assertEquals(null, comboLeg.getExchange());
        assertEquals(0, comboLeg.getOpenClose());
        assertEquals(0, comboLeg.getShortSaleSlot());
        assertEquals(null, comboLeg.getDesignatedLocation());
    }

    @Test
    public void testConstructorWithAllArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 1, "Action1", "Exchange1", 1, 1, "DesignatedLocation1");
        assertEquals(1, comboLeg.getConId());
        assertEquals(1, comboLeg.getRatio());
        assertEquals("Action1", comboLeg.getAction());
        assertEquals("Exchange1", comboLeg.getExchange());
        assertEquals(1, comboLeg.getOpenClose());
        assertEquals(1, comboLeg.getShortSaleSlot());
        assertEquals("DesignatedLocation1", comboLeg.getDesignatedLocation());
    }
}