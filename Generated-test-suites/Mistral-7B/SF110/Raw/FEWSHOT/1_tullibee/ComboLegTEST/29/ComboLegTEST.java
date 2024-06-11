package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructor0() {
        ComboLeg comboLeg = new ComboLeg();
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_conId);
        assertNull(comboLeg.m_designatedLocation);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_shortSaleSlot);
    }

    @Test
    public void testConstructor1() {
        ComboLeg comboLeg = new ComboLeg(1);
        assertEquals(1, comboLeg.m_conId);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_designatedLocation);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_shortSaleSlot);
    }

    @Test
    public void testConstructor2() {
        ComboLeg comboLeg = new ComboLeg(1, "Action1", "Exchange1", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals("Action1", comboLeg.m_action);
        assertEquals("Exchange1", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructor3() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 1, "DesignatedLocation1");
        assertEquals(1, comboLeg.m_conId);
        assertEquals("Action1", comboLeg.m_action);
        assertEquals("Exchange1", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals(1, comboLeg.m_shortSaleSlot);
        assertEquals("DesignatedLocation1", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, "Action1", "Exchange1", 1, 1, 1, "DesignatedLocation1");
        ComboLeg comboLeg2 = new ComboLeg(1, "Action1", "Exchange1", 1, 1, 1, "DesignatedLocation1");
        ComboLeg comboLeg3 = new ComboLeg(2, "Action1", "Exchange1", 1, 1, 1, "DesignatedLocation1");
        ComboLeg comboLeg4 = new ComboLeg(1, "Action2", "Exchange1", 1, 1, 1, "DesignatedLocation1");

        assertEquals(comboLeg1, comboLeg2);
        assertNotSame(comboLeg1, comboLeg3);
        assertNotSame(comboLeg1, comboLeg4);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsWithNullObject() {
        new ComboLeg().equals(null);
    }

    @Test
    public void testHashCode() {
        ComboLeg comboLeg1 = new ComboLeg(1, "Action1", "Exchange1", 1, 1, 1, "DesignatedLocation1");
        ComboLeg comboLeg2 = new ComboLeg(1, "Action1", "Exchange1", 1, 1, 1, "DesignatedLocation1");
        ComboLeg comboLeg3 = new ComboLeg(2, "Action1", "Exchange1", 1, 1, 1, "DesignatedLocation1");
        ComboLeg comboLeg4 = new ComboLeg(1, "Action2", "Exchange1", 1, 1, 1, "DesignatedLocation1");

        assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
        assertNotEquals(comboLeg1.hashCode(), comboLeg3.hashCode());
        assertNotEquals(comboLeg1.hashCode(), comboLeg4.hashCode());
    }
}