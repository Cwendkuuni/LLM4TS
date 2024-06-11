import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    private ComboLeg comboLeg1;
    private ComboLeg comboLeg2;
    private ComboLeg comboLeg3;

    @Before
    public void setUp() {
        comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
        comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", 1);
        comboLeg3 = new ComboLeg(2, 2, "SELL", "IBKR", 2);
    }

    @Test
    public void testComboLegConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        ComboLeg comboLegWithParams = new ComboLeg(1, 2, "BUY", "IBKR", 1);
        ComboLeg comboLegWithAllParams = new ComboLeg(1, 2, "BUY", "IBKR", 1, 0, "LONDON");

        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);

        assertEquals(1, comboLegWithParams.m_conId);
        assertEquals(2, comboLegWithParams.m_ratio);
        assertEquals("BUY", comboLegWithParams.m_action);
        assertEquals("IBKR", comboLegWithParams.m_exchange);
        assertEquals(1, comboLegWithParams.m_openClose);
        assertEquals(0, comboLegWithParams.m_shortSaleSlot);
        assertNull(comboLegWithParams.m_designatedLocation);

        assertEquals(1, comboLegWithAllParams.m_conId);
        assertEquals(2, comboLegWithAllParams.m_ratio);
        assertEquals("BUY", comboLegWithAllParams.m_action);
        assertEquals("IBKR", comboLegWithAllParams.m_exchange);
        assertEquals(1, comboLegWithAllParams.m_openClose);
        assertEquals(0, comboLegWithAllParams.m_shortSaleSlot);
        assertEquals("LONDON", comboLegWithAllParams.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
    }
}