package com.ib.client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ib.client.ComboLeg;

public class ComboLegTest {

    @Test
    public void testConstructorZeroArgs() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.m_conId);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructorOneArg() {
        ComboLeg comboLeg = new ComboLeg(1);
        assertEquals(1, comboLeg.m_conId);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(ComboLeg.UNKNOWN, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructorAllArgs() {
        String exchange = "IBM";
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", exchange, ComboLeg.OPEN, 3, "NYSE");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("BUY", comboLeg.m_action);
        assertEquals(exchange, comboLeg.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg.m_openClose);
        assertEquals(3, comboLeg.m_shortSaleSlot);
        assertEquals("NYSE", comboLeg.m_designatedLocation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidRatio() {
        new ComboLeg(-1, 0, null, null, 0, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullActionOrExchange() {
        new ComboLeg(1, 2, null, null, 0, 0, null);
        new ComboLeg(1, 2, null, null, 0, 0, " ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidOpenClose() {
        new ComboLeg(1, 2, "BUY", "IBM", 4, 0, null);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN, 3, "NYSE");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN, 3, "NYSE");
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN, 3, "NASDAQ");
        ComboLeg comboLeg4 = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN, 4, "NYSE");
        ComboLeg comboLeg5 = new ComboLeg(1, 3, "BUY", "IBM", ComboLeg.OPEN, 3, "NYSE");
        assertTrue(comboLeg1.equals(comboLeg2));
        assertNotEquals(comboLeg1, comboLeg3);
        assertNotEquals(comboLeg1, comboLeg4);
        assertNotEquals(comboLeg1, comboLeg5);
    }

}