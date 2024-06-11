
import static org.junit.Assert.*;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testMinimalConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals("1", comboLeg.m_exchange); // Assuming exchange is hardcoded as a string "1" for this test case
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBM", 1, 0, null);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBM", 1, 0, null);

        assertTrue(comboLeg1.equals(comboLeg2));

        ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBM", 1, 0, "london");
        assertFalse(comboLeg1.equals(comboLeg3));

        ComboLeg comboLeg4 = null;
        assertFalse(comboLeg1.equals(comboLeg4));
    }
}