package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegConstructor() {
        ComboLeg leg1 = new ComboLeg();
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN);
        ComboLeg leg3 = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN, 1, "NYSE");

        assertEquals(0, leg1.getConId());
        assertEquals(0, leg1.getRatio());
        assertNull(leg1.getAction());
        assertNull(leg1.getExchange());
        assertEquals(ComboLeg.SAME, leg1.getOpenClose());
        assertEquals(0, leg1.getShortSaleSlot());
        assertNull(leg1.getDesignatedLocation());

        assertEquals(1, leg2.getConId());
        assertEquals(2, leg2.getRatio());
        assertEquals("BUY", leg2.getAction());
        assertEquals("IBM", leg2.getExchange());
        assertEquals(ComboLeg.OPEN, leg2.getOpenClose());

        assertEquals(1, leg3.getConId());
        assertEquals(2, leg3.getRatio());
        assertEquals("BUY", leg3.getAction());
        assertEquals("IBM", leg3.getExchange());
        assertEquals(ComboLeg.OPEN, leg3.getOpenClose());
        assertEquals(1, leg3.getShortSaleSlot());
        assertEquals("NYSE", leg3.getDesignatedLocation());
    }

    @Test
    public void testComboLegEquals() {
        ComboLeg leg1 = new ComboLeg();
        ComboLeg leg2 = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN);
        ComboLeg leg3 = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN);
        ComboLeg leg4 = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN, 1, "NYSE");

        assertTrue(leg2.equals(leg3));
        assertNotEquals(leg2, leg1);
        assertNotEquals(leg2, leg4);

        ComboLeg leg5 = new ComboLeg();
        leg5.m_conId = 1;
        leg5.m_ratio = 2;
        leg5.m_action = "BUY";
        leg5.m_exchange = "IBM";
        leg5.m_openClose = ComboLeg.OPEN;
        leg5.m_shortSaleSlot = 1;
        leg5.m_designatedLocation = "NYSE";

        assertTrue(leg2.equals(leg5));
    }

    @Test
    public void testComboLegGetters() {
        ComboLeg leg = new ComboLeg(1, 2, "BUY", "IBM", ComboLeg.OPEN);

        assertEquals(1, leg.getConId());
        assertEquals(2, leg.getRatio());
        assertEquals("BUY", leg.getAction());
        assertEquals("IBM", leg.getExchange());
        assertEquals(ComboLeg.OPEN, leg.getOpenClose());
        assertEquals(0, leg.getShortSaleSlot());
        assertNull(leg.getDesignatedLocation());
    }
}