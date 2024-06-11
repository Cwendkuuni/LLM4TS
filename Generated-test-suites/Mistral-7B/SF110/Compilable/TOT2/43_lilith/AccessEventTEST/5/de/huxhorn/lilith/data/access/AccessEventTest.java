package de.huxhorn.lilith.data.access.test;

import org.junit.Test;
import static org.junit.Assert.*;
import de.huxhorn.lilith.data.access.AccessEvent;

public class AccessEventTest {

    @Test
    public void testGetTimeStamp() {
        AccessEvent accessEvent = new AccessEvent();
        Long expectedTimeStamp = 1L;
        accessEvent.setTimeStamp(expectedTimeStamp);
        Long actualTimeStamp = accessEvent.getTimeStamp();
        assertEquals(expectedTimeStamp, actualTimeStamp);
    }

    @Test(expected = NullPointerException.class)
    public void testGetTimeStampWithNullValue() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(null);
        accessEvent.getTimeStamp();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTimeStampWithInvalidInput() {
        AccessEvent accessEvent = new AccessEvent();
        accessEvent.setTimeStamp(-1L);
    }

    @Test
    public void testEqualsWithSameObject() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = accessEvent1;
        assertTrue(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    @Test
    public void testEqualsWithDifferentFields() {
        AccessEvent accessEvent1 = new AccessEvent();
        AccessEvent accessEvent2 = new AccessEvent();
        accessEvent1.setTimeStamp(1L);
        accessEvent2.setTimeStamp(2L);
        assertFalse(accessEvent1.equals(accessEvent2));
    }

    // Add test cases for other methods
}