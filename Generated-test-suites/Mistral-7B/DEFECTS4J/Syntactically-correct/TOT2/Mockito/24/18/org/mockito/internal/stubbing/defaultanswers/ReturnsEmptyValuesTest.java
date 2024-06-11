
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.Mockito;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = mock(String.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(mock(Method.class));
        invocation.setTarget(mock);

        String result = (String) returnsEmptyValues.answer(invocation);
        assertNull(result);
        verify(mock).answer(invocation);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testAnswerMethodWithNullInvocation() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = null;

        try {
            String result = (String) returnsEmptyValues.answer(invocation);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Cannot answer invocation on a null target", e.getMessage());
        }
    }

    @Test
    public void testAnswerMethodWithNullReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> nullClass = null;

        try {
            returnsEmptyValues.returnValueFor(nullClass);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Null type parameter", e.getMessage());
        }
    }

    @Test
    public void testReturnValueForPrimitive() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        int result = (int) returnsEmptyValues.returnValueFor(int.class);
        assertEquals(0, result);
    }

    @Test
    public void testReturnValueForCustomClass() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<CustomClass> customClass = CustomClass.class;

        CustomClass result = (CustomClass) returnsEmptyValues.returnValueFor(customClass);
        assertNull(result);
        // Add more assertions based on the CustomClass behavior
    }
}
