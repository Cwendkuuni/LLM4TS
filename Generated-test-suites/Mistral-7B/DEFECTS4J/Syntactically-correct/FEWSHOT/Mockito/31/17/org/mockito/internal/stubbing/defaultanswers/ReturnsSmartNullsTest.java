package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;
    private Object target;

    @Before
    public void setup() {
        returnsSmartNulls = new ReturnsSmartNulls();
        target = mock(TestClass.class);
    }

    @Test
    public void testAnswerEmptyArray() {
        when(target.methodWithEmptyArrayReturnType()).thenAnswer(returnsSmartNulls);
        Object result = target.methodWithEmptyArrayReturnType();
        assertEquals("SmartNull returned by unstubbed methodWithEmptyArrayReturnType() method on mock", result);
    }

    @Test
    public void testAnswerNull() {
        when(target.methodWithNullReturnType()).thenAnswer(returnsSmartNulls);
        Object result = target.methodWithNullReturnType();
        assertNull(result);
    }

    @Test
    public void testAnswerSmartNullForString() {
        when(target.methodWithStringReturnType()).thenAnswer(returnsSmartNulls);
        String result = (String) target.methodWithStringReturnType();
        assertEquals("SmartNull returned by unstubbed methodWithStringReturnType() method on mock", result.getClass().getName());
    }

    @Test
    public void testAnswerSmartNullForInteger() {
        when(target.methodWithIntegerReturnType()).thenAnswer(returnsSmartNulls);
        Integer result = target.methodWithIntegerReturnType();
        assertEquals("SmartNull returned by unstubbed methodWithIntegerReturnType() method on mock", result.getClass().getName());
    }

    @Test
    public void testAnswerSmartNullForCustomClass() {
        when(target.methodWithCustomClassReturnType()).thenAnswer(returnsSmartNulls);
        CustomClass result = target.methodWithCustomClassReturnType();
        assertEquals("SmartNull returned by unstubbed methodWithCustomClassReturnType() method on mock", result.getClass().getName());
    }

    @Test
    public void testAnswerDelegateForNotMockableType() {
        when(target.methodWithNotMockableTypeReturnType()).thenAnswer(returnsSmartNulls);
        Object result = target.methodWithNotMockableTypeReturnType();
        assertNull(result);
    }

    @Test
    public void testAnswerDelegateForToStringMethod() {
        when(target.methodWithToStringReturnType()).thenAnswer(returnsSmartNulls);
        Object result = target.methodWithToStringReturnType();
        assertEquals("SmartNull returned by unstubbed methodWithToStringReturnType() method on mock", result.toString());
    }

    @Test
    public void testAnswerDelegateForMethodWithArgs() {
        when(target.methodWithArgsReturnType(1, "two")).thenAnswer(returnsSmartNulls);
        Object result = target.methodWithArgsReturnType(1, "two");
        assertEquals("SmartNull returned by unstubbed methodWithArgsReturnType(int, java.lang.String) method on mock", result.getClass().getName());
    }

    static class TestClass {
        Object methodWithEmptyArrayReturnType() {
            return new Object[0];
        }

        Object methodWithNullReturnType() {
            return null;
        }

        String methodWithStringReturnType() {
            return null;
        }

        Integer methodWithIntegerReturnType() {
            return null;
        }

        CustomClass methodWithCustomClassReturnType() {
            return null;
        }

        Object methodWithNotMockableTypeReturnType() {
            final int i = 0;
            return i;
        }

        String methodWithToStringReturnType() {
            return null;
        }

        Object methodWithArgsReturnType(int arg1, String arg2) {
            return null;
        }
    }
}

class CustomClass {
    // Custom class definition here
}