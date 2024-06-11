package com.ib.client.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.ib.client.ComboLeg;

public class ComboLegTest {

    @Test
    public void testComboLegDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.getm_conId());
        assertEquals(0, comboLeg.getm_ratio());
        assertNull(comboLeg.getm_action());
        assertNull(comboLeg.getm_exchange());
        assertEquals(0, comboLeg.getm_openClose());
        assertEquals(0, comboLeg.getm_shortSaleSlot());
        assertNull(comboLeg.getm_designatedLocation());
    }

    @Test
    public void testComboLegConstructorWithAllParameters() {
        String exchange = "IBKR";
        int conId = 123;
        int ratio = 5;
        int openClose = ComboLeg.OPEN;
        int shortSaleSlot = 1;
        String action = "BUY";
        String designatedLocation = "LONDON";

        ComboLeg comboLeg = new ComboLeg(conId, ratio, action, exchange, openClose, shortSaleSlot, designatedLocation);

        assertEquals(conId, comboLeg.getm_conId());
        assertEquals(ratio, comboLeg.getm_ratio());
        assertEquals(action, comboLeg.getm_action());
        assertEquals(exchange, comboLeg.getm_exchange());
        assertEquals(openClose, comboLeg.getm_openClose());
        assertEquals(shortSaleSlot, comboLeg.getm_shortSaleSlot());
        assertEquals(designatedLocation, comboLeg.getm_designatedLocation());
    }

    @Test
    public void testComboLegConstructorWithSomeParameters() {
        String exchange = "IBKR";
        int conId = 123;
        int ratio = 5;

        ComboLeg comboLeg = new ComboLeg(conId, ratio, null, exchange, ComboLeg.OPEN, 1, null);

        assertEquals(conId, comboLeg.getm_conId());
        assertEquals(ratio, comboLeg.getm_ratio());
        assertNull(comboLeg.getm_action());
        assertEquals(exchange, comboLeg.getm_exchange());
        assertEquals(ComboLeg.OPEN, comboLeg.getm_openClose());
        assertEquals(1, comboLeg.getm_shortSaleSlot());
        assertNull(comboLeg.getm_designatedLocation());
    }

    @Test
    public void testComboLegConstructorWithInvalidParameters() {
        String action = null;
        String exchange = null;

        try {
            new ComboLeg(0, 0, action, exchange, ComboLeg.OPEN, 1, null);
            fail("Expected IllegalArgumentException with null action or exchange");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new ComboLeg(0, 0, "invalidExchange", null, ComboLeg.OPEN, 1, null);
            fail("Expected IllegalArgumentException with null exchange");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 1, "LONDON");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 1, "LONDON");

        ComboLeg comboLeg3 = new ComboLeg(1, 2, "BUY", "IBKR", ComboLeg.OPEN, 2, "LONDON");
        ComboLeg comboLeg4 = new ComboLeg(3, 2, "BUY", "IBKR", ComboLeg.OPEN, 1, "LONDON");

        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg1.equals(comboLeg4));

        ComboLeg comboLeg5 = new ComboLeg();
        assertFalse(comboLeg1.equals(comboLeg5));
    }

    private int getm_openClose(ComboLeg comboLeg) {
        return comboLeg.getm_openClose();
    }

    private String getm_action(ComboLeg comboLeg) {
        return comboLeg.getm_action();
    }

    private String getm_exchange(ComboLeg comboLeg) {
        return comboLeg.getm_exchange();
    }

    private String getm_designatedLocation(ComboLeg comboLeg) {
        return comboLeg.getm_designatedLocation();
    }
}