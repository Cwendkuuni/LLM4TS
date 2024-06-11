package com.ib.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertTrue(comboLeg.m_conId == 0);
        assertTrue(comboLeg.m_ratio == 0);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertTrue(comboLeg.m_openClose == ComboLeg.SAME);
        assertTrue(comboLeg.m_shortSaleSlot == 0);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegConstructorWithInts() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN);
        assertTrue(comboLeg.m_conId == 1);
        assertTrue(comboLeg.m_ratio == 2);
        assertTrue(Util.StringCompareIgnCase("action", comboLeg.m_action) == 0);
        assertTrue(Util.StringCompareIgnCase("exchange", comboLeg.m_exchange) == 0);
        assertTrue(comboLeg.m_openClose == ComboLeg.OPEN);
        assertTrue(comboLeg.m_shortSaleSlot == 0);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegConstructorWithAllArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN, 3, "designatedLocation");
        assertTrue(comboLeg.m_conId == 1);
        assertTrue(comboLeg.m_ratio == 2);
        assertTrue(Util.StringCompareIgnCase("action", comboLeg.m_action) == 0);
        assertTrue(Util.StringCompareIgnCase("exchange", comboLeg.m_exchange) == 0);
        assertTrue(comboLeg.m_openClose == ComboLeg.OPEN);
        assertTrue(comboLeg.m_shortSaleSlot == 3);
        assertTrue(Util.StringCompareIgnCase("designatedLocation", comboLeg.m_designatedLocation) == 0);
    }

    @Test
    public void testEqualsWithSameObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = comboLeg1;
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithNullObject() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN);
        assertFalse(comboLeg.equals(null));
    }

    @Test
    public void testEqualsWithDifferentObjectConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "action", "exchange", ComboLeg.OPEN);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentObjectRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "action", "exchange", ComboLeg.OPEN);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentObjectAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action2", "exchange", ComboLeg.OPEN);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentObjectExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange2", ComboLeg.OPEN);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentObjectOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.CLOSE);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentObjectShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN, 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN, 2);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentObjectDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN, 1, "designatedLocation1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN, 1, "designatedLocation2");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithEqualObjects() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN);
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testHashCodesAreEqualForEqualObjects() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action", "exchange", ComboLeg.OPEN);
        assertTrue(comboLeg1.hashCode() == comboLeg2.hashCode());
    }

    @Test
    public void testHashCodeForDifferentObjectsIsNotEqual() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action2", "exchange2", ComboLeg.OPEN);
        assertNotEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
    }
}