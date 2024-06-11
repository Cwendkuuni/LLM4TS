package com.ib.client.test;

import static org.junit.Assert.*;

import com.ib.client.ComboLeg;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructorWithAllArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "SMART", 1, 3, "NYSE");
        assertEquals(1, comboLeg.getm_conId());
        assertEquals(2, comboLeg.getm_ratio());
        assertEquals("BUY", comboLeg.getm_action());
        assertEquals("SMART", comboLeg.getm_exchange());
        assertEquals(1, comboLeg.getm_openClose());
        assertEquals(3, comboLeg.getm_shortSaleSlot());
        assertEquals("NYSE", comboLeg.getm_designatedLocation());
    }

    @Test
    public void testConstructorWithNoArgs() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.getm_conId());
        assertEquals(0, comboLeg.getm_ratio());
        assertEquals(null, comboLeg.getm_action());
        assertEquals(null, comboLeg.getm_exchange());
        assertEquals(0, comboLeg.getm_openClose());
        assertEquals(0, comboLeg.getm_shortSaleSlot());
        assertEquals(null, comboLeg.getm_designatedLocation());
    }

    @Test
    public void testConstructorWithSomeArgs() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "BUY", "SMART", 1);
        assertEquals(1, comboLeg.getm_conId());
        assertEquals(2, comboLeg.getm_ratio());
        assertEquals("BUY", comboLeg.getm_action());
        assertEquals("SMART", comboLeg.getm_exchange());
        assertEquals(1, comboLeg.getm_openClose());
        assertEquals(0, comboLeg.getm_shortSaleSlot());
        assertEquals(null, comboLeg.getm_designatedLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidRatio() {
        new ComboLeg(1, -1, "BUY", "SMART", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullAction() {
        new ComboLeg(1, 2, null, "SMART", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullExchange() {
        new ComboLeg(1, 2, "BUY", null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDesignatedLocation() {
        new ComboLeg(1, 2, "BUY", "SMART", 1, 3, null);
    }

    @Test
    public void testGettersAndSetters() {
        ComboLeg comboLeg = new ComboLeg();
        comboLeg.setm_conId(1);
        comboLeg.setm_ratio(2);
        comboLeg.setm_action("BUY");
        comboLeg.setm_exchange("SMART");
        comboLeg.setm_openClose(1);
        comboLeg.setm_shortSaleSlot(3);
        comboLeg.setm_designatedLocation("NYSE");

        assertEquals(1, comboLeg.getm_conId());
        assertEquals(2, comboLeg.getm_ratio());
        assertEquals("BUY", comboLeg.getm_action());
        assertEquals("SMART", comboLeg.getm_exchange());
        assertEquals(1, comboLeg.getm_openClose());
        assertEquals(3, comboLeg.getm_shortSaleSlot());
        assertEquals("NYSE", comboLeg.getm_designatedLocation());
    }

    @Test
    public void testEqualsWithSameObject() {
        ComboLeg comboLeg1 = new ComboLeg();
        ComboLeg comboLeg2 = comboLeg1;
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        ComboLeg comboLeg1 = new ComboLeg();
        ComboLeg comboLeg2 = new ComboLeg();
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithDifferentValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "SMART", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 3, "BUY", "SMART", 1);
        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithSameValues() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "BUY", "SMART", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "BUY", "SMART", 1);
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsWithNullObject() {
        ComboLeg comboLeg = new ComboLeg();
        assertFalse(comboLeg.equals(null));
    }
}