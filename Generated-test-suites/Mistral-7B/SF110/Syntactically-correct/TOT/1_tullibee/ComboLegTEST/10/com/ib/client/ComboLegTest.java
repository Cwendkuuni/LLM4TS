
package com.ib.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertNotNull(comboLeg);
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructorWithValidArguments() {
        ComboLeg comboLeg = new ComboLeg(1234, 5, "ACTION", "EXCHANGE", 1);
        assertEquals(1234, comboLeg.m_conId);
        assertEquals(5, comboLeg.m_ratio);
        assertEquals("ACTION", comboLeg.m_action);
        assertEquals("EXCHANGE", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1234, 5, "ACTION", "EXCHANGE", 1);
        ComboLeg comboLeg2 = new ComboLeg(1234, 5, "ACTION", "EXCHANGE", 1);
        assertTrue(comboLeg1.equals(comboLeg2));

        ComboLeg comboLeg3 = new ComboLeg(1235, 5, "ACTION", "EXCHANGE", 1);
        assertFalse(comboLeg1.equals(comboLeg3));
    }
}
