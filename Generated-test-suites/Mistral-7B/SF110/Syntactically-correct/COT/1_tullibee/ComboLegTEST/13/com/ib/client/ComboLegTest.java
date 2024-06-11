package com.ib.client.test;

import static org.junit.Assert.*;

import com.ib.client.ComboLeg;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertNull(comboLeg.getM_action());
        assertNull(comboLeg.getM_exchange());
        assertEquals(0, comboLeg.getM_conId());
        assertEquals(0, comboLeg.getM_openClose());
        assertEquals(0, comboLeg.getM_ratio());
        assertEquals(0, comboLeg.getM_shortSaleSlot());
        assertNull(comboLeg.getM_designatedLocation());

        ComboLeg comboLegWithValues = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        assertEquals(1, comboLegWithValues.getM_conId());
        assertEquals(2, comboLegWithValues.getM_ratio());
        assertEquals("Action1", comboLegWithValues.getM_action());
        assertEquals("Exchange1", comboLegWithValues.getM_exchange());
        assertEquals(ComboLeg.OPEN, comboLegWithValues.getM_openClose());
        assertEquals(0, comboLegWithValues.getM_shortSaleSlot());
        assertNull(comboLegWithValues.getM_designatedLocation());
    }

    @Test
    public void testComboLegEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "Action2", "Exchange1", ComboLeg.OPEN);
        ComboLeg comboLeg4 = new ComboLeg(2, 2, "Action1", "Exchange1", ComboLeg.OPEN);

        assertTrue(comboLeg1.equals(comboLeg2));
        assertFalse(comboLeg1.equals(comboLeg3));
        assertFalse(comboLeg1.equals(comboLeg4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegInvalidConstructor() {
        new ComboLeg(0, 2, null, null, 1, -1, null);
    }

    @Test
    public void testComboLegGetters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);

        assertEquals(1, comboLeg.getM_conId());
        assertEquals(2, comboLeg.getM_ratio());
        assertEquals("Action1", comboLeg.getM_action());
        assertEquals("Exchange1", comboLeg.getM_exchange());
        assertEquals(ComboLeg.OPEN, comboLeg.getM_openClose());
        assertEquals(0, comboLeg.getM_shortSaleSlot());
        assertNull(comboLeg.getM_designatedLocation());
    }

    @Test
    public void testComboLegSetters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", ComboLeg.OPEN);

        comboLeg.setM_conId(3);
        comboLeg.setM_ratio(4);
        comboLeg.setM_action("Action2");
        comboLeg.setM_exchange("Exchange2");
        comboLeg.setM_openClose(ComboLeg.CLOSE);
        comboLeg.setM_shortSaleSlot(5);
        comboLeg.setM_designatedLocation("Location1");

        assertEquals(3, comboLeg.getM_conId());
        assertEquals(4, comboLeg.getM_ratio());
        assertEquals("Action2", comboLeg.getM_action());
        assertEquals("Exchange2", comboLeg.getM_exchange());
        assertEquals(ComboLeg.CLOSE, comboLeg.getM_openClose());
        assertEquals(5, comboLeg.getM_shortSaleSlot());
        assertEquals("Location1", comboLeg.getM_designatedLocation());
    }

    @Test(expected = NullPointerException.class)
    public void testComboLegSetM_actionNull() {
        ComboLeg comboLeg = new ComboLeg();
        comboLeg.setM_action(null);
    }

    @Test(expected = NullPointerException.class)
    public void testComboLegSetM_exchangeNull() {
        ComboLeg comboLeg = new ComboLeg();
        comboLeg.setM_exchange(null);
    }

    @Test(expected = NullPointerException.class)
    public void testComboLegSetM_designatedLocationNull() {
        ComboLeg comboLeg = new ComboLeg();
        comboLeg.setM_designatedLocation(null);
    }
}