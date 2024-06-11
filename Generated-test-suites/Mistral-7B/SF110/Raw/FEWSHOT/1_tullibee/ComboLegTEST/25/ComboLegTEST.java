package com.ib.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegWithDefaultConstructor() {
        ComboLeg defaultComboLeg = new ComboLeg();
        assertNull(defaultComboLeg.m_action);
        assertNull(defaultComboLeg.m_conId);
        assertNull(defaultComboLeg.m_designatedLocation);
        assertNull(defaultComboLeg.m_exchange);
        assertEquals(0, defaultComboLeg.m_openClose);
        assertEquals(0, defaultComboLeg.m_ratio);
        assertEquals(0, defaultComboLeg.m_shortSaleSlot);
    }

    @Test
    public void testComboLegWithIntConstructor() {
        ComboLeg intComboLeg = new ComboLeg(1);
        assertEquals(1, intComboLeg.m_conId);
        assertNull(intComboLeg.m_action);
        assertNull(intComboLeg.m_designatedLocation);
        assertNull(intComboLeg.m_exchange);
        assertEquals(0, intComboLeg.m_openClose);
        assertEquals(0, intComboLeg.m_ratio);
        assertEquals(0, intComboLeg.m_shortSaleSlot);
    }

    @Test
    public void testComboLegWithAllParametersConstructor() {
        String action = "BUY";
        String exchange = "SMART";
        int conId = 12345;
        int openClose = ComboLeg.OPEN;
        int ratio = 2;
        int shortSaleSlot = 5;
        String designatedLocation = "NY";

        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        assertEquals(conId, comboLeg.m_conId);
        assertEquals(ratio, comboLeg.m_ratio);
        assertEquals(action, comboLeg.m_action);
        assertEquals(exchange, comboLeg.m_exchange);
        assertEquals(openClose, comboLeg.m_openClose);
        assertEquals(shortSaleSlot, comboLeg.m_shortSaleSlot);
        assertEquals(designatedLocation, comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegEquals() {
        String action1 = "BUY";
        String action2 = "SELL";
        String exchange1 = "SMART";
        String exchange2 = "SMART";
        int conId1 = 12345;
        int conId2 = 12345;
        int ratio1 = 2;
        int ratio2 = 2;
        int openClose1 = ComboLeg.OPEN;
        int openClose2 = ComboLeg.OPEN;
        int shortSaleSlot1 = 5;
        int shortSaleSlot2 = 5;
        String designatedLocation1 = "NY";
        String designatedLocation2 = "NY";

        ComboLeg comboLeg1 = new ComboLeg(conId1, ratio1, action1, exchange1, openClose1, shortSaleSlot1, designatedLocation1);
        ComboLeg comboLeg2 = new ComboLeg(conId2, ratio2, action2, exchange2, openClose2, shortSaleSlot2, designatedLocation2);

        ComboLeg comboLeg3 = new ComboLeg(conId1, ratio1, action1, exchange1, openClose1, shortSaleSlot1, designatedLocation1);

        assertTrue(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg1.equals(null));
        assertFalse(comboLeg1.equals(new Object()));
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegConstructorWithInvalidParameters() {
        new ComboLeg(0, 0, null, null, -1, -1, null);
    }

    @Test(expected = NullPointerException.class)
    public void testComboLegEqualsWithNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "SMART", 0, 0, "NY");
        assertFalse(comboLeg.equals(null));
    }
}