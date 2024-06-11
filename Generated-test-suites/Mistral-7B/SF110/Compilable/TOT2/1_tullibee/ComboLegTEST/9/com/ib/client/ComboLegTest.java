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
        assertEquals(0, comboLeg.m_openClose);
        assertNull(comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }
}