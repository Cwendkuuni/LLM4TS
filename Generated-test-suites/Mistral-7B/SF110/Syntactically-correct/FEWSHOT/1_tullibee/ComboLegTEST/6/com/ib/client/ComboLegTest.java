package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructor() {
        ComboLeg comboLeg1 = new ComboLeg();
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN, 1, "DesignatedLocation1");

        assertNotNull(comboLeg1);
        assertNotNull(comboLeg2);
        assertNotNull(comboLeg3);

        assertEquals(0, comboLeg1.m_conId);
        assertEquals(0, comboLeg1.m_ratio);
        assertNull(comboLeg1.m_action);
        assertNull(comboLeg1.m_exchange);
        assertEquals(ComboLeg.SAME, comboLeg1.m_openClose);
        assertEquals(0, comboLeg1.m_shortSaleSlot);
        assertNull(comboLeg1.m_designatedLocation);

        assertEquals(1, comboLeg2.m_conId);
        assertEquals(2, comboLeg2.m_ratio);
        assertEquals("Action1", comboLeg2.m_action);
        assertEquals("Exchange1", comboLeg2.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg2.m_openClose);

        assertEquals(1, comboLeg3.m_conId);
        assertEquals(2, comboLeg3.m_ratio);
        assertEquals("Action1", comboLeg3.m_action);
        assertEquals("Exchange1", comboLeg3.m_exchange);
        assertEquals(ComboLeg.OPEN, comboLeg3.m_openClose);
        assertEquals(1, comboLeg3.m_shortSaleSlot);
        assertEquals("DesignatedLocation1", comboLeg3.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action2", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg4 = new ComboLeg(1, 2, "Action1", "Exchange2", ComboLeg.OPEN);

        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg1.equals(comboLeg4));
    }

    @Test
    public void testGettersAndSetters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);

        assertEquals(1, comboLeg.getm_conId());
        assertEquals(2, comboLeg.getm_ratio());
        assertEquals("Action1", comboLeg.getm_action());
        assertEquals("Exchange1", comboLeg.getm_exchange());
        assertEquals(ComboLeg.OPEN, comboLeg.getm_openClose());
        assertEquals(0, comboLeg.getm_shortSaleSlot());
        assertEquals(null, comboLeg.getm_designatedLocation());

        comboLeg.setm_conId(3);
        comboLeg.setm_ratio(4);
        comboLeg.setm_action("Action2");
        comboLeg.setm_exchange("Exchange2");
        comboLeg.setm_openClose(ComboLeg.CLOSE);
        comboLeg.setm_shortSaleSlot(5);
        comboLeg.setm_designatedLocation("DesignatedLocation2");

        assertEquals(3, comboLeg.getm_conId());
        assertEquals(4, comboLeg.getm_ratio());
        assertEquals("Action2", comboLeg.getm_action());
        assertEquals("Exchange2", comboLeg.getm_exchange());
        assertEquals(ComboLeg.CLOSE, comboLeg.getm_openClose());
        assertEquals(5, comboLeg.getm_shortSaleSlot());
        assertEquals("DesignatedLocation2", comboLeg.getm_designatedLocation());
    }
}