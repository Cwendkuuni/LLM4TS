package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructor() {
        ComboLeg test = new ComboLeg();
        assertEquals(0, test.m_conId);
        assertNull(test.m_action);
        assertNull(test.m_exchange);
        assertEquals(0, test.m_openClose);
        assertEquals(0, test.m_shortSaleSlot);
        assertNull(test.m_designatedLocation);

        ComboLeg test1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
        assertEquals(1, test1.m_conId);
        assertEquals(2, test1.m_ratio);
        assertEquals("Action1", test1.m_action);
        assertEquals("Exchange1", test1.m_exchange);
        assertEquals(1, test1.m_openClose);
        assertEquals(0, test1.m_shortSaleSlot);
        assertNull(test1.m_designatedLocation);

        ComboLeg test2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 5, "DesignatedLocation1");
        assertEquals(1, test2.m_conId);
        assertEquals(2, test2.m_ratio);
        assertEquals("Action1", test2.m_action);
        assertEquals("Exchange1", test2.m_exchange);
        assertEquals(1, test2.m_openClose);
        assertEquals(5, test2.m_shortSaleSlot);
        assertEquals("DesignatedLocation1", test2.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg obj1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 5, "DesignatedLocation1");
        ComboLeg obj2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 5, "DesignatedLocation1");

        assertTrue(obj1.equals(obj2));

        ComboLeg obj3 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 5, null);
        assertNotEquals(obj1, obj3);

        ComboLeg obj4 = new ComboLeg(1, 2, "Action1", "Exchange2", 1, 5, "DesignatedLocation1");
        assertNotEquals(obj1, obj4);

        ComboLeg obj5 = new ComboLeg(1, 3, "Action1", "Exchange1", 1, 5, "DesignatedLocation1");
        assertNotEquals(obj1, obj5);

        ComboLeg obj6 = new ComboLeg(1, 2, "Action1", "Exchange1", 2, 5, "DesignatedLocation1");
        assertNotEquals(obj1, obj6);
    }
}