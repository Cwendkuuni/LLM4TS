
package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.junit.Test;
import org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SameTest {

    @Test
    public void testSameObjects() {
        Same same = new Same("testString");
        assertTrue(same.matches("testString"));
    }

    @Test
    public void testDescribeTo() {
        Description description = mock(Description.class);
        Same same = new Same("testString");
        same.describeTo(description);
        verify(description).appendText("same(");
        verify(description).appendText("\"testString\"");
        verify(description).appendText(")");
    }

    @Test
    public void testAppendQuotingString() {
        Same same = new Same("testString");
        assertEquals("\"testString\"", same.toString());
    }

    @Test
    public void testAppendQuotingCharacter() {
        Same same = new Same('t');
        assertEquals("'t'", same.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        new Same(null);
    }

    @Test
    public void testDifferentTypes() {
        Same same = new Same(1);
        assertFalse(same.matches("testString"));
    }

    @Test
    public void testEqualPrimitives() {
        Same same = new Same((byte) 1);
        Same same2 = new Same((byte) 1);
        assertTrue(same.matches(same2));
    }

    @Test
    public void testNotEqualPrimitives() {
        Same same = new Same((byte) 1);
        Same same2 = new Same((short) 1);
        assertFalse(same.matches(same2));
    }

    @Test
    public void testArrays() {
        Object[] array1 = new Object[]{"testString1", "testString2"};
        Object[] array2 = new Object[]{"testString1", "testString2"};
        Same same = new Same(array1);
        assertTrue(same.matches(array2));
    }

    @Test
    public void testNonEqualArrays() {
        Object[] array1 = new Object[]{"testString1", "testString2"};
        Object[] array2 = new Object[]{"testString1", "testString3"};
        Same same = new Same(array1);
        assertFalse(same.matches(array2));
    }

    @Test
    public void testDifferentTypesDifferentClass() {
        Same same = new Same(1);
        String testString = "testString";
        assertFalse(same.matches(testString));
    }

    @Test
    public void testNullWanted() {
        assertFalse(new Same(null).matches(new Object()));
    }

    @Test
    public void testNullActual() {
        Same same = new Same("testString");
        assertFalse(same.matches(null));
    }

    @Test
    public void testNullWantedAndActual() {
        assertFalse(new Same(null).matches(null));
    }
}
