package com.ib.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructorWithDefaultValues() {
        ComboLeg leg = new ComboLeg();
        assertEquals(0, leg.m_conId);
        assertEquals(0, leg.m_ratio);
        assertNull(leg.m_action);
        assertNull(leg.m_exchange);
        assertEquals(0, leg.m_openClose);
        assertEquals(0, leg.m_shortSaleSlot);
        assertNull(leg.m_designatedLocation);
    }

    @Test
    public void testConstructorWithAllParameters() {
        ComboLeg leg = new ComboLeg(1, 2, "Action", "Exchange", 1, 3, "DesignatedLocation");
        assertEquals(1, leg.m_conId);
        assertEquals(2, leg.m_ratio);
        assertEquals("Action", leg.m_action);
        assertEquals("Exchange", leg.m_exchange);
        assertEquals(1, leg.m_openClose);
        assertEquals(3, leg.m_shortSaleSlot);
        assertEquals("DesignatedLocation", leg.m_designatedLocation);
    }

    @Test
    public void testEqualsWhenObjectsAreEqual() {
        ComboLeg leg1 = new ComboLeg(1, 2, "Action", "Exchange", 1, 3, "DesignatedLocation");
        ComboLeg leg2 = new ComboLeg(1, 2, "Action", "Exchange", 1, 3, "DesignatedLocation");
        assertTrue(leg1.equals(leg2));
    }

    @Test
    public void testEqualsWhenObjectsAreDifferent() {
        ComboLeg leg1 = new ComboLeg(1, 2, "Action", "Exchange", 1, 3, "DesignatedLocation");
        ComboLeg leg2 = new ComboLeg(1, 2, "Action", "Exchange", 1, 3, null);
        assertFalse(leg1.equals(leg2));

        ComboLeg leg3 = new ComboLeg(1, 2, "Action", "Exchange", 2, 3, "DesignatedLocation");
        assertFalse(leg1.equals(leg3));

        ComboLeg leg4 = new ComboLeg(1, 2, "Action", null, 1, 3, "DesignatedLocation");
        assertFalse(leg1.equals(leg4));

        ComboLeg leg5 = new ComboLeg(1, 2, "Action1", "Exchange", 1, 3, "DesignatedLocation");
        assertFalse(leg1.equals(leg5));
    }

    @Test
    public void testHashCode() {
        ComboLeg leg1 = new ComboLeg(1, 2, "Action", "Exchange", 1, 3, "DesignatedLocation");
        int hashCode1 = leg1.hashCode();

        ComboLeg leg2 = new ComboLeg(1, 2, "Action", "Exchange", 1, 3, "DesignatedLocation");
        int hashCode2 = leg2.hashCode();

        assertEquals(hashCode1, hashCode2);

        ComboLeg leg3 = new ComboLeg(1, 2, "Action", "Exchange", 1, 4, "DesignatedLocation");
        int hashCode3 = leg3.hashCode();

        assertNotEquals(hashCode1, hashCode3);

        ComboLeg leg4 = new ComboLeg(1, 2, "Action", "Exchange", 1, 3, null);
        int hashCode4 = leg4.hashCode();

        assertNotEquals(hashCode1, hashCode4);
    }
}