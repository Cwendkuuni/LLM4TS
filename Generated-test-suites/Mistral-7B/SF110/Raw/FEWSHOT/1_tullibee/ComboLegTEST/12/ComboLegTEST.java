package com.ib.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testConstructorNoArgs() {
        ComboLeg cls = new ComboLeg();
        assertEquals(0, cls.m_conId);
        assertEquals(0, cls.m_ratio);
        assertNull(cls.m_action);
        assertNull(cls.m_exchange);
        assertEquals(0, cls.m_openClose);
        assertEquals(0, cls.m_shortSaleSlot);
        assertNull(cls.m_designatedLocation);
    }

    @Test
    public void testConstructorOneArg() {
        ComboLeg cls = new ComboLeg(1);
        assertEquals(1, cls.m_conId);
        assertEquals(0, cls.m_ratio);
        assertNull(cls.m_action);
        assertNull(cls.m_exchange);
        assertEquals(ComboLeg.UNKNOWN, cls.m_openClose);
        assertEquals(0, cls.m_shortSaleSlot);
        assertNull(cls.m_designatedLocation);
    }

    @Test
    public void testConstructorTwoArgs() {
        ComboLeg cls = new ComboLeg(1, 2);
        assertEquals(1, cls.m_conId);
        assertEquals(2, cls.m_ratio);
        assertNull(cls.m_action);
        assertNull(cls.m_exchange);
        assertEquals(ComboLeg.UNKNOWN, cls.m_openClose);
        assertEquals(0, cls.m_shortSaleSlot);
        assertNull(cls.m_designatedLocation);
    }

    @Test
    public void testConstructorThreeArgs() {
        ComboLeg cls = new ComboLeg(1, "ACTION", "EXCHANGE");
        assertEquals(1, cls.m_conId);
        assertEquals(0, cls.m_ratio);
        assertEquals("ACTION", cls.m_action);
        assertEquals("EXCHANGE", cls.m_exchange);
        assertEquals(ComboLeg.UNKNOWN, cls.m_openClose);
        assertEquals(0, cls.m_shortSaleSlot);
        assertNull(cls.m_designatedLocation);
    }

    @Test
    public void testConstructorFourArgs() {
        ComboLeg cls = new ComboLeg(1, 2, "ACTION", "EXCHANGE");
        assertEquals(1, cls.m_conId);
        assertEquals(2, cls.m_ratio);
        assertEquals("ACTION", cls.m_action);
        assertEquals("EXCHANGE", cls.m_exchange);
        assertEquals(ComboLeg.UNKNOWN, cls.m_openClose);
        assertEquals(0, cls.m_shortSaleSlot);
        assertNull(cls.m_designatedLocation);
    }

    @Test
    public void testConstructorFiveArgs() {
        ComboLeg cls = new ComboLeg(1, 2, "ACTION", "EXCHANGE", 3);
        assertEquals(1, cls.m_conId);
        assertEquals(2, cls.m_ratio);
        assertEquals("ACTION", cls.m_action);
        assertEquals("EXCHANGE", cls.m_exchange);
        assertEquals(ComboLeg.UNKNOWN, cls.m_openClose);
        assertEquals(3, cls.m_shortSaleSlot);
        assertNull(cls.m_designatedLocation);
    }

    @Test
    public void testConstructorSixArgs() {
        ComboLeg cls = new ComboLeg(1, 2, "ACTION", "EXCHANGE", 3, "LOCATION");
        assertEquals(1, cls.m_conId);
        assertEquals(2, cls.m_ratio);
        assertEquals("ACTION", cls.m_action);
        assertEquals("EXCHANGE", cls.m_exchange);
        assertEquals(ComboLeg.UNKNOWN, cls.m_openClose);
        assertEquals(3, cls.m_shortSaleSlot);
        assertEquals("LOCATION", cls.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg obj1 = new ComboLeg(1, 2, "ACTION", "EXCHANGE", ComboLeg.OPEN, 3, "LOCATION");
        ComboLeg obj2 = new ComboLeg(1, 2, "ACTION", "EXCHANGE", ComboLeg.OPEN, 3, "LOCATION");
        assertTrue(obj1.equals(obj2));

        ComboLeg obj3 = new ComboLeg(1, 2, "ACTION", "EXCHANGE", ComboLeg.OPEN, 4, "LOCATION");
        assertNotEquals(obj1, obj3);

        ComboLeg obj4 = new ComboLeg();
        assertNotEquals(obj1, obj4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualsNull() {
        new ComboLeg().equals(null);
    }

    @Test
    public void testHashCode() {
        ComboLeg obj1 = new ComboLeg(1, 2, "ACTION", "EXCHANGE", ComboLeg.OPEN, 3, "LOCATION");
        int hashCode1 = obj1.hashCode();

        ComboLeg obj2 = new ComboLeg(1, 2, "ACTION", "EXCHANGE", ComboLeg.OPEN, 3, "LOCATION");
        int hashCode2 = obj2.hashCode();

        assertEquals(hashCode1, hashCode2);

        ComboLeg obj3 = new ComboLeg(1, 2, "ACTION", "EXCHANGE", ComboLeg.OPEN, 4, "LOCATION");
        int hashCode3 = obj3.hashCode();

        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        ComboLeg obj = new ComboLeg(1, 2, "ACTION", "EXCHANGE", ComboLeg.OPEN, 3, "LOCATION");
        String toString = obj.toString();
        assertTrue(toString.contains("ComboLeg [conId=1, ratio=2, action=ACTION, exchange=EXCHANGE, openClose=" + ComboLeg.OPEN + ", shortSaleSlot=3, designatedLocation=LOCATION]"));
    }
}