package com.ib.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegConstructor() {
        ComboLeg comboLeg1 = new ComboLeg();
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 1, "NYSE");

        assertTrue(comboLeg1.m_conId == 0);
        assertTrue(comboLeg1.m_ratio == 0);
        assertNull(comboLeg1.m_action);
        assertNull(comboLeg1.m_exchange);
        assertTrue(comboLeg1.m_openClose == ComboLeg.UNKNOWN);
        assertNull(comboLeg1.m_designatedLocation);

        assertTrue(comboLeg2.m_conId == 1);
        assertTrue(comboLeg2.m_ratio == 2);
        assertTrue(Util.StringCompareIgnCase(comboLeg2.m_action, "BUY") == 0);
        assertTrue(Util.StringCompareIgnCase(comboLeg2.m_exchange, "IBKR") == 0);
        assertTrue(comboLeg2.m_openClose == ComboLeg.OPEN);
        assertTrue(comboLeg2.m_shortSaleSlot == 0);
        assertNull(comboLeg2.m_designatedLocation);

        assertTrue(comboLeg3.m_conId == 1);
        assertTrue(comboLeg3.m_ratio == 2);
        assertTrue(Util.StringCompareIgnCase(comboLeg3.m_action, "BUY") == 0);
        assertTrue(Util.StringCompareIgnCase(comboLeg3.m_exchange, "IBKR") == 0);
        assertTrue(comboLeg3.m_openClose == ComboLeg.OPEN);
        assertTrue(comboLeg3.m_shortSaleSlot == 1);
        assertTrue(Util.StringCompareIgnCase(comboLeg3.m_designatedLocation, "NYSE") == 0);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "SELL", "IBKR", ComboLeg.OPEN);

        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));

        ComboLeg comboLeg4 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 1, "NYSE");
        assertFalse(comboLeg1.equals(comboLeg4));

        ComboLeg comboLeg5 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN);
        comboLeg5.m_designatedLocation = "NYSE";
        assertTrue(comboLeg1.equals(comboLeg5));
    }

    @Test
    public void testStringCompareIgnCase() {
        assertTrue(Util.StringCompareIgnCase("BUY", "buy") == 0);
        assertTrue(Util.StringCompareIgnCase("IBKR", "IBkr") != 0);
        assertTrue(Util.StringCompareIgnCase("", "") == 0);
    }
}