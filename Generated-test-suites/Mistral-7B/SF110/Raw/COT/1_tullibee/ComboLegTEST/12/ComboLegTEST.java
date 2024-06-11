package com.ib.client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.ib.client.ComboLeg;

public class ComboLegTest {

    @Test
    public void testComboLegConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.getm_conId());
        assertNull(comboLeg.getm_action());
        assertNull(comboLeg.getm_exchange());
        assertEquals(0, comboLeg.getm_openClose());
        assertNull(comboLeg.getm_designatedLocation());

        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 3);
        assertEquals(1, comboLeg1.getm_conId());
        assertEquals("action1", comboLeg1.getm_action());
        assertEquals("exchange1", comboLeg1.getm_exchange());
        assertEquals(3, comboLeg1.getm_openClose());
        assertNull(comboLeg1.getm_designatedLocation());

        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation1");
        assertEquals(1, comboLeg2.getm_conId());
        assertEquals("action1", comboLeg2.getm_action());
        assertEquals("exchange1", comboLeg2.getm_exchange());
        assertEquals(3, comboLeg2.getm_openClose());
        assertEquals("designatedLocation1", comboLeg2.getm_designatedLocation());
    }

    @Test
    public void testEqualsMethod() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation1");
        ComboLeg comboLeg3 = new ComboLeg(1, 2, "action1", "exchange1", 4, 4, "designatedLocation1");
        ComboLeg comboLeg4 = new ComboLeg(2, 2, "action1", "exchange1", 3, 4, "designatedLocation1");

        assertEquals(comboLeg1, comboLeg2);
        assertNotEquals(comboLeg1, comboLeg3);
        assertNotEquals(comboLeg1, comboLeg4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidRatio() {
        new ComboLeg(-1, 2, "action1", "exchange1", 3, 4, "designatedLocation1");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullActionOrExchange() {
        new ComboLeg(1, 2, null, "exchange1", 3, 4, "designatedLocation1");
        new ComboLeg(1, 2, "action1", null, 3, 4, "designatedLocation1");
    }
}