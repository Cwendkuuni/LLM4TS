package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructorZeroArg() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(ComboLeg.SAME, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructorSingleArg() {
        ComboLeg comboLeg = new ComboLeg(1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(ComboLeg.SAME, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructorMultipleArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Action", comboLeg.m_action);
        assertEquals("Exchange", comboLeg.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testEqualsWithSameObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = comboLeg1;
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentObject() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.CLOSE);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentConId() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(2, 2, "Action", "Exchange", ComboLeg.OPEN);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentRatio() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "Action", "Exchange", ComboLeg.OPEN);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentAction() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action2", "Exchange", ComboLeg.OPEN);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentExchange() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action", "Exchange2", ComboLeg.OPEN);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentOpenClose() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.CLOSE);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentShortSaleSlot() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN, 1);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentDesignatedLocation() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN, null, "DesignatedLocation1");
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithNull() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action", "Exchange", ComboLeg.OPEN);
        assertFalse(comboLeg.equals(null));
    }
}