package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(0, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegConstructorIntIntStringStringIntIntIntString() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "action", "exchange", 3, 4, "designatedLocation");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("action", comboLeg.m_action);
        assertEquals("exchange", comboLeg.m_exchange);
        assertEquals(3, comboLeg.m_openClose);
        assertEquals(4, comboLeg.m_shortSaleSlot);
        assertEquals("designatedLocation", comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegEqualsNull() {
        ComboLeg comboLeg = new ComboLeg();
        assertNotEquals(comboLeg, null);
    }

    @Test
    public void testComboLegEqualsSameInstance() {
        ComboLeg comboLeg = new ComboLeg();
        assertEquals(comboLeg, comboLeg);
    }

    @Test
    public void testComboLegEqualsDifferentInstances() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation1");
        assertEquals(comboLeg1, comboLeg2);

        ComboLeg comboLeg1_diff = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation2");
        assertNotEquals(comboLeg1, comboLeg1_diff);

        ComboLeg comboLeg1_ratioDiff = new ComboLeg(1, 3, "action1", "exchange1", 3, 4, "designatedLocation1");
        assertNotEquals(comboLeg1, comboLeg1_ratioDiff);

        ComboLeg comboLeg1_openCloseDiff = new ComboLeg(1, 2, "action1", "exchange1", 4, 4, "designatedLocation1");
        assertNotEquals(comboLeg1, comboLeg1_openCloseDiff);

        ComboLeg comboLeg1_shortSaleSlotDiff = new ComboLeg(1, 2, "action1", "exchange1", 3, 5, "designatedLocation1");
        assertNotEquals(comboLeg1, comboLeg1_shortSaleSlotDiff);

        ComboLeg comboLeg1_actionDiff = new ComboLeg(1, 2, "action2", "exchange1", 3, 4, "designatedLocation1");
        assertNotEquals(comboLeg1, comboLeg1_actionDiff);

        ComboLeg comboLeg1_exchangeDiff = new ComboLeg(1, 2, "action1", "exchange2", 3, 4, "designatedLocation1");
        assertNotEquals(comboLeg1, comboLeg1_exchangeDiff);

        ComboLeg comboLeg1_designatedLocationDiff = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation2");
        assertNotEquals(comboLeg1, comboLeg1_designatedLocationDiff);
    }

    @Test
    public void testComboLegHashcodeEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation1");
        assertEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
    }

    @Test
    public void testComboLegHashcodeDifferent() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "action1", "exchange1", 3, 4, "designatedLocation2");
        assertNotEquals(comboLeg1.hashCode(), comboLeg2.hashCode());
    }
}