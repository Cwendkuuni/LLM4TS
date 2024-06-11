import static org.junit.Assert.*;

import org.junit.Test;

public class ComboLegTest {

    @Test
    public void testDefaultConstructor() {
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
    public void testConstructorWithParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Action1", comboLeg.m_action);
        assertEquals("Exchange1", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testConstructorWithAllParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 3, "DesignatedLocation1");
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals("Action1", comboLeg.m_action);
        assertEquals("Exchange1", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(3, comboLeg.m_shortSaleSlot);
        assertEquals("DesignatedLocation1", comboLeg.m_designatedLocation);
    }

    @Test
    public void testEquals() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 3, "DesignatedLocation1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 3, "DesignatedLocation1");
        assertTrue(comboLeg1.equals(comboLeg2));
    }

    @Test
    public void testEqualsDifferentObjects() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 3, "DesignatedLocation1");
        Object otherObject = new Object();
        assertFalse(comboLeg1.equals(otherObject));
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorParameters() {
        new ComboLeg(null, 2, "Action1", "Exchange1", 1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorParameters2() {
        new ComboLeg(1, null, "Action1", "Exchange1", 1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorParameters3() {
        new ComboLeg(1, 2, null, "Exchange1", 1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorParameters4() {
        new ComboLeg(1, 2, "Action1", null, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorParameters5() {
        new ComboLeg(1, 2, "Action1", "Exchange1", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeConstructorParameters() {
        new ComboLeg(-1, 2, "Action1", "Exchange1", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeConstructorParameters2() {
        new ComboLeg(1, -2, "Action1", "Exchange1", 1);
    }

    @Test
    public void testEmptyStringConstructorParameters() {
        ComboLeg comboLeg = new ComboLeg(1, 2, "", "Exchange1", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEmpty(comboLeg.m_action);
        assertEquals("Exchange1", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testLongStringConstructorParameters() {
        String longString = "Long long string for testing constructor parameters.";
        ComboLeg comboLeg = new ComboLeg(1, 2, longString, "Exchange1", 1);
        assertEquals(1, comboLeg.m_conId);
        assertEquals(2, comboLeg.m_ratio);
        assertEquals(longString, comboLeg.m_action);
        assertEquals("Exchange1", comboLeg.m_exchange);
        assertEquals(1, comboLeg.m_openClose);
        assertEquals(0, comboLeg.m_shortSaleSlot);
        assertNull(comboLeg.m_designatedLocation);
    }

    @Test
    public void testStringComparisonNullValue() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 3, "DesignatedLocation1");
        String nullValue = null;
        assertFalse(comboLeg1.equals(nullValue));
    }

    @Test
    public void testStringComparisonDifferentCases() {
        ComboLeg comboLeg1 = new ComboLeg(1, 2, "ACTION1", "Exchange1", 1, 3, "DesignatedLocation1");
        ComboLeg comboLeg2 = new ComboLeg(1, 2, "Action1", "Exchange1", 1, 3, "DesignatedLocation1");
        assertFalse(comboLeg1.equals(comboLeg2));
        assertTrue(Util.StringCompareIgnCase(comboLeg1.m_action, comboLeg2.m_action));
    }
}