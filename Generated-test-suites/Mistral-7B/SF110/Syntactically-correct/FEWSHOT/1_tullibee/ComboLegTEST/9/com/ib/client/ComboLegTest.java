package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testComboLegConstructor() {
        ComboLeg cl1 = new ComboLeg();
        ComboLeg cl2 = new ComboLeg(1, 2, "ACTION1", "EXCHANGE1", ComboLeg.OPEN);
        ComboLeg cl3 = new ComboLeg(1, 2, "ACTION1", "EXCHANGE1", ComboLeg.OPEN, 4, "LOCATION1");

        assertEquals(0, cl1.m_conId);
        assertEquals(0, cl1.m_ratio);
        assertNull(cl1.m_action);
        assertNull(cl1.m_exchange);
        assertEquals(ComboLeg.SAME, cl1.m_openClose);
        assertEquals(0, cl1.m_shortSaleSlot);
        assertNull(cl1.m_designatedLocation);

        assertEquals(1, cl2.m_conId);
        assertEquals(2, cl2.m_ratio);
        assertEquals("ACTION1", cl2.m_action);
        assertEquals("EXCHANGE1", cl2.m_exchange);
        assertEquals(ComboLeg.OPEN, cl2.m_openClose);

        assertEquals(1, cl3.m_conId);
        assertEquals(2, cl3.m_ratio);
        assertEquals("ACTION1", cl3.m_action);
        assertEquals("EXCHANGE1", cl3.m_exchange);
        assertEquals(ComboLeg.OPEN, cl3.m_openClose);
        assertEquals(4, cl3.m_shortSaleSlot);
        assertEquals("LOCATION1", cl3.m_designatedLocation);
    }

    @Test
    public void testComboLegEquals() {
        ComboLeg cl1 = new ComboLeg(1, 2, "ACTION1", "EXCHANGE1", ComboLeg.OPEN);
        ComboLeg cl2 = new ComboLeg(1, 2, "ACTION1", "EXCHANGE1", ComboLeg.OPEN);
        ComboLeg cl3 = new ComboLeg(1, 2, "ACTION2", "EXCHANGE1", ComboLeg.OPEN);
        ComboLeg cl4 = new ComboLeg(1, 2, "ACTION1", "EXCHANGE2", ComboLeg.OPEN);

        assertTrue(cl1.equals(cl2));
        assertNotSame(cl1, cl3);
        assertNotSame(cl1, cl4);
    }

    @Test
    public void testComboLegHashCode() {
        ComboLeg cl1 = new ComboLeg(1, 2, "ACTION1", "EXCHANGE1", ComboLeg.OPEN);
        ComboLeg cl2 = new ComboLeg(1, 2, "ACTION1", "EXCHANGE1", ComboLeg.OPEN);

        assertEquals(cl1.hashCode(), cl2.hashCode());
    }

    @Test(expected = NullPointerException.class)
    public void testComboLegNullConstructor() {
        new ComboLeg(null, 2, "ACTION1", "EXCHANGE1", ComboLeg.OPEN);
    }

    @Test(expected = NullPointerException.class)
    public void testComboLegNullAction() {
        new ComboLeg(1, 2, null, "EXCHANGE1", ComboLeg.OPEN);
    }

    @Test(expected = NullPointerException.class)
    public void testComboLegNullExchange() {
        new ComboLeg(1, 2, "ACTION1", null, ComboLeg.OPEN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComboLegNegativeOpenClose() {
        new ComboLeg(1, 2, "ACTION1", "EXCHANGE1", -1);
    }
}