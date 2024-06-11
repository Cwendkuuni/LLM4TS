package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.internal.matchers.ArgumentsMatchers.*;

import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatchers;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.PrintingFriendlyInvocation;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.MatchersPrinter;
import org.mockito.invocation.MockitoMethod;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{}, null), new Object[0], 1, null);
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{}, null);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 1, null);
        assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object arg1 = "arg1";
        Object arg2 = new Object[]{"arg2"};
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{String.class, Object[].class}, null), new Object[]{arg1, arg2}, 1, null);
        Object[] expected = {arg1, arg2};
        assertArrayEquals(expected, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{String.class, Object[].class}, null), new Object[]{""}, 1, null);
        assertFalse(invocation.isVerified());

        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation1 = new Invocation(new Object(), new MockitoMethod(MockitoMethod.TestClass.class, "testMethod1", new Class[]{String.class}, null), new Object[]{"arg1"}, 1, null);
        Invocation invocation2 = new Invocation(new Object(), new MockitoMethod(MockitoMethod.TestClass.class, "testMethod2", new Class[]{String.class}, null), new Object[]{"arg2"}, 2, null);

        assertFalse(invocation1.isVerifiedInOrder());
        assertFalse(invocation2.isVerifiedInOrder());

        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();

        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerifiedInOrder());
    }

    @Test
    public void testToString() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{String.class, Object[].class}, null);
        Object mock = new Object();
        Object arg1 = "arg1";
        Object arg2 = new Object[]{"arg2"};
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{arg1, arg2}, 1, null);

        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        String expected = String.format("%s(%s)%n", new MockUtil().getMockName(mock), invocation.argumentsToMatchers().toString());
        assertEquals(expected, invocation.toString(printSettings));

        String expectedShort = String.format("%s.testMethod(%s, %s)", new MockUtil().getMockName(mock), matcher(arg1), matcher(arg2));
        assertEquals(expectedShort, invocation.toString());
    }

    @Test
    public void testToStringWithNoArguments() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[0], null);
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);
        String expected = String.format("%s.testMethod()", new MockUtil().getMockName(mock));
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testEquals() {
        MockitoMethod mockitoMethod1 = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod1", new Class[]{String.class}, null);
        MockitoMethod mockitoMethod2 = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod2", new Class[]{String.class}, null);
        Object mock = new Object();
        Object arg1 = "arg1";
        Object arg2 = "arg2";

        Invocation invocation1 = new Invocation(mock, mockitoMethod1, new Object[]{arg1}, 1, null);
        Invocation invocation2 = new Invocation(mock, mockitoMethod2, new Object[]{arg1}, 1, null);
        Invocation invocation3 = new Invocation(mock, mockitoMethod1, new Object[]{arg2}, 1, null);
        Invocation invocation4 = new Invocation(mock, mockitoMethod1, new Object[]{arg1}, 2, null);

        assertFalse(invocation1.equals(null));
        assertFalse(invocation1.equals(new Object()));
        assertFalse(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
        assertFalse(invocation1.equals(invocation4));

        assertTrue(invocation1.equals(invocation1));
    }

    @Test
    public void testGetSequenceNumber() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{String.class, Object[].class}, null), new Object[]{""}, 1, null);
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder_multipleCalls() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{String.class}, null);
        Invocation invocation1 = new Invocation(mock, method, new Object[]{"arg1"}, 1, null);
        Invocation invocation2 = new Invocation(mock, method, new Object[]{"arg2"}, 2, null);

        assertFalse(invocation1.isVerifiedInOrder());
        assertFalse(invocation2.isVerifiedInOrder());

        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();

        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerifiedInOrder());
    }

    @Test
    public void testIsVerifiedInOrder_multipleCalls_differentMethods() {
        Object mock = new Object();
        MockitoMethod method1 = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod1", new Class[]{String.class}, null);
        MockitoMethod method2 = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod2", new Class[]{String.class}, null);
        Invocation invocation1 = new Invocation(mock, method1, new Object[]{"arg1"}, 1, null);
        Invocation invocation2 = new Invocation(mock, method2, new Object[]{"arg2"}, 2, null);

        assertFalse(invocation1.isVerifiedInOrder());
        assertFalse(invocation2.isVerifiedInOrder());

        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();

        assertFalse(invocation1.isVerifiedInOrder());
        assertFalse(invocation2.isVerifiedInOrder());
    }

    @Test
    public void testToString_multiLine() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{String[].class}, null);
        Object mock = new Object();
        Object arg = new String[]{"arg1", "arg2"};
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{arg}, 1, null);

        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        String expected = String.format("%s.testMethod([%s])%n", new MockUtil().getMockName(mock), Arrays.toString(arg));
        assertEquals(expected, invocation.toString(printSettings));

        String expectedShort = String.format("%s.testMethod(%s)", new MockUtil().getMockName(mock), matcher(arg));
        assertEquals(expectedShort, invocation.toString());
    }

    @Test
    public void testToString_multilineWithArgumentsToMatchers() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{String[].class}, null);
        Object mock = new Object();
        Object arg = new String[]{"arg1", "arg2"};
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{arg}, 1, null);

        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        String expected = String.format("%s.testMethod([%s])%n", new MockUtil().getMockName(mock), new MatchersPrinter().getArgumentsBlock(invocation.argumentsToMatchers(), printSettings));
        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{String.class}, null), new Object[]{""}, 1, null);
        Location expected = new Location();
        assertEquals(expected, invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{String.class, Object[].class}, null), new Object[]{""}, 1, null);
        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoMethod.TestClass.class, "toString", new Class[]{}, null);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 1, null);
        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{String.class, Throwable.class}, null);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[]{"arg1"}, 1, null);
        Throwable throwable = new RuntimeException();

        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{Integer.TYPE}, null);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 1, null);
        assertTrue(invocation.isValidReturnType(int.class));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[0], null);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 1, null);
        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{Integer.TYPE}, null);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 1, null);
        assertEquals("Integer", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{String.class}, null);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 1, null);
        assertEquals("testMethod", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{boolean.class}, null);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 1, null);
        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testCallRealMethod() throws Exception {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoMethod.TestClass.class, "testMethod", new Class[]{String.class}, new Class[]{String.class}, new Object[]{"arg1"}, new Object[]{"result"});
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{"arg1"}, 1, new RealMethod() {
            @Override
            public Object invoke(Object obj, Object[] args) throws Throwable {
                return "result";
            }
        });
        assertEquals("result", invocation.callRealMethod());
    }
}

class MockitoMethod {
    public static class TestClass {
        public void testMethod(String arg1, String[] args) {
        }

        public void testMethod1(String arg1) {
        }

        public void testMethod2(String arg1) {
        }

        public void testMethod3() {
        }

        public String testMethod4() {
            return "";
        }

        public void testMethod5(String arg1, int arg2) {
        }

        public void testMethod6(String arg1, boolean arg2) {
        }

        public void testMethod7(String arg1, Throwable arg2) {
        }

        public Integer testMethod8() {
            return null;
        }

        public void testMethod9(Object[] args) {
        }

        public void testMethod10(String[] args) {
        }

        public void testMethod11() {
        }

        public void testMethod12() throws Exception {
        }

        public void testMethod13() {
            throw new Exception();
        }

        public String toString() {
            return "toString";
        }
    }

    private final Class<?> clazz;
    private final Class<?>[] exceptionTypes;
    private final MockitoMethod testClassMethod;

    public InvocationMethod(Class<?> clazz, String name, Class<?>[] parameterTypes, Class<?>[] exceptionTypes, MockitoMethod testClassMethod) {
        this.clazz = clazz;
        this.exceptionTypes = exceptionTypes;
        this.testClassMethod = testClassMethod;
        this.name = name;
        this.parameterTypes = parameterTypes;
    }

    public Class<?> getReturnType() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Class<?>[] getExceptionTypes() {
        return exceptionTypes;
    }
}

class RealMethod {
    public Object invoke(Object obj, Object[] args) throws Throwable {
        throw new UnsupportedOperationException();
    }
}