
package com.ib.client;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertThrows;

public class ComboLegTest {

    @Test
    public void testComboLegDefaultConstructor() {
        ComboLeg comboLeg = new ComboLeg();
        assertNull(comboLeg.m_conId);
        assertNull(comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertNull(comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegOneArgumentConstructor() {
        ComboLeg comboLeg = new ComboLeg(1);
        assertEquals(1, comboLeg.m_conId);
        assertNull(comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegTwoArgumentsConstructor() {
        ComboLeg comboLeg = new ComboLeg(1, 2);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertNull(comboLeg.m_action);
        assertNull(comboLeg.m_exchange);
        assertEquals(0, comboLeg.m_openClose);
        assertNull(comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegThreeArgumentsConstructor() {
        ComboLeg comboLeg = new ComboLeg("Action1", "Exchange1", 1);
        assertEquals("Action1", comboLeg.m_action);
        assertEquals("Exchange1", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertEquals(0, comboLeg.m_openClose);
        assertNull(comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testComboLegFourArgumentsConstructor() {
        ComboLeg comboLeg = new ComboLeg("Action1", "Exchange1", 1, "Location1");
        assertEquals("Action1", comboLeg.m_action);
        assertEquals("Exchange1", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(0, comboLeg.m_ratio);
        assertEquals(0, comboLeg.m_openClose);
        assertEquals("Location1", comboLeg.m_designatedLocation);
        assertNull(comboLeg.m_shortSaleSlot);
    }

    @Test
    public void testComboLegFiveArgumentsConstructorWithInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new ComboLeg("Action1", "Exchange1", 1, "Location1", -1));
    }

    @Test
    public void testComboLegEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);

        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testComboLegNotEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange2", 1);

        assertFalse(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testComboLegNull() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", 1);

        assertFalse(comboLeg.equals(null));
    }
}
