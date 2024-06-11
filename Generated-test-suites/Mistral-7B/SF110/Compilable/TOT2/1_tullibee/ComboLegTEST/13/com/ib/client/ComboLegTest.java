package com.ib.client;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testComboLegDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertNull(comboLeg.m_conId);
        assertNull(comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(ComboLeg.SAME, comboLeg.m_openClose);
        assertNull(comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegCustomConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Action", comboLeg.m_action);
        assertEquals("Exchange", comboLeg.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
        assertNull(comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN);
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testComboLegNotEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.CLOSE);
        assertFalse(comboLeg1.equals(comboLeg2));
    }
}