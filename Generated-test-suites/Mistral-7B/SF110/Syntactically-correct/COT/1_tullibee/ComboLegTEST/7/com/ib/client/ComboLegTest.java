package com.ib.client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ib.client.ComboLeg;

public class ComboLegTest {

    @Test
    public void testComboLegConstructorWithAllArguments() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "EXCHANGE1", 1, 3, "DesignatedLocation1");
        assertEquals(1, comboLeg.getm_conId());
        assertEquals(2, comboLeg.getm_ratio());
        assertEquals("Action1", comboLeg.getm_action());
        assertEquals("EXCHANGE1", comboLeg.getm_exchange());
        assertEquals(1, comboLeg.getm_openClose());
        assertEquals(3, comboLeg.getm_shortSaleSlot());
        assertEquals("DesignatedLocation1", comboLeg.getm_designatedLocation());
    }

    @Test
    public void testComboLegConstructorWithDefaultArguments() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.getm_conId());
        assertNull(comboLeg.getm_action());
        assertNull(comboLeg.getm_exchange());
        assertEquals(0, comboLeg.getm_openClose());
        assertEquals(0, comboLeg.getm_shortSaleSlot());
        assertNull(comboLeg.getm_designatedLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegConstructorWithNegativeRatio() {
        new ComboLeg(-1, 1, "Action1", "EXCHANGE1", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegConstructorWithNullExchange() {
        new ComboLeg(1, 2, "Action1", null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegConstructorWithNullDesignatedLocation() {
        new ComboLeg(1, 2, "Action1", "EXCHANGE1", 1, 3, null);
    }

    @Test
    public void testComboLegEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "EXCHANGE1", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "EXCHANGE1", 1);
        assertTrue(comboLeg1.equals(comboLeg2));

        ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action1", "EXCHANGE2", 1);
        assertNotEquals(comboLeg1, comboLeg3);

        ComboLeg comboLeg4 = new ComboLeg(1, 3, "Action1", "EXCHANGE1", 1);
        assertNotEquals(comboLeg1, comboLeg4);

        ComboLeg comboLeg5 = new ComboLeg(1, 2, null, "EXCHANGE1", 1);
        assertNotEquals(comboLeg1, comboLeg5);

        ComboLeg comboLeg6 = new ComboLeg(1, 2, "Action1", "EXCHANGE1", 2);
        assertNotEquals(comboLeg1, comboLeg6);
    }

    @Test
    public void testComboLegGetters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "EXCHANGE1", 1);
        assertEquals(1, comboLeg.getm_conId());
        assertEquals(2, comboLeg.getm_ratio());
        assertEquals("Action1", comboLeg.getm_action());
        assertEquals("EXCHANGE1", comboLeg.getm_exchange());
        assertEquals(1, comboLeg.getm_openClose());
        assertEquals(0, comboLeg.getm_shortSaleSlot());
        assertNull(comboLeg.getm_designatedLocation());
    }

    @Test
    public void testComboLegSetters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "EXCHANGE1", 1);
        comboLeg.setm_conId(3);
        comboLeg.setm_ratio(4);
        comboLeg.setm_action("Action2");
        comboLeg.setm_exchange("EXCHANGE2");
        comboLeg.setm_openClose(2);
        comboLeg.setm_shortSaleSlot(5);
        comboLeg.setm_designatedLocation("DesignatedLocation2");
        assertEquals(3, comboLeg.getm_conId());
        assertEquals(4, comboLeg.getm_ratio());
        assertEquals("Action2", comboLeg.getm_action());
        assertEquals("EXCHANGE2", comboLeg.getm_exchange());
        assertEquals(2, comboLeg.getm_openClose());
        assertEquals(5, comboLeg.getm_shortSaleSlot());
        assertEquals("DesignatedLocation2", comboLeg.getm_designatedLocation());
    }

    private int getm_conId(ComboLeg comboLeg) {
        return comboLeg.m_conId;
    }

    private int getm_ratio(ComboLeg comboLeg) {
        return comboLeg.m_ratio;
    }

    private String getm_action(ComboLeg comboLeg) {
        return comboLeg.m_action;
    }

    private String getm_exchange(ComboLeg comboLeg) {
        return comboLeg.m_exchange;
    }

    private int getm_openClose(ComboLeg comboLeg) {
        return comboLeg.m_openClose;
    }

    private int getm_shortSaleSlot(ComboLeg comboLeg) {
        return comboLeg.m_shortSaleSlot;
    }

    private String getm_designatedLocation(ComboLeg comboLeg) {
        return comboLeg.m_designatedLocation;
    }
}