import static org.junit.Assert.*;

public class ComboLegTest {
    @Test
    public void testComboLeg() {
        ComboLeg comboLeg = new ComboLeg();
        // test default constructor

        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 1);
        // test constructor with all arguments

        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 1, 1, "designatedLocation1");
        // test constructor with all arguments including shortSaleSlot and designatedLocation

        assertEquals(0, comboLeg.getConId());
        assertEquals(0, comboLeg.getRatio());
        assertNull(comboLeg.getAction());
        assertNull(comboLeg.getExchange());
        assertEquals(0, comboLeg.getOpenClose());
        assertEquals(0, comboLeg.getShortSaleSlot());
        assertNull(comboLeg.getDesignatedLocation());

        assertEquals(1, comboLeg1.getConId());
        assertEquals(2, comboLeg1.getRatio());
        assertEquals("action1", comboLeg1.getAction());
        assertEquals("exchange1", comboLeg1.getExchange());
        assertEquals(1, comboLeg1.getOpenClose());
        assertEquals(0, comboLeg1.getShortSaleSlot());
        assertNull(comboLeg1.getDesignatedLocation());

        assertEquals(1, comboLeg2.getConId());
        assertEquals(2, comboLeg2.getRatio());
        assertEquals("action1", comboLeg2.getAction());
        assertEquals("exchange1", comboLeg2.getExchange());
        assertEquals(1, comboLeg2.getOpenClose());
        assertEquals(1, comboLeg2.getShortSaleSlot());
        assertEquals("designatedLocation1", comboLeg2.getDesignatedLocation());
    }
}