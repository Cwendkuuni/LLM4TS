package org.mockito.tests.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.VerificationException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.PrintSettings;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockitoMethod;
import org.mockito.internal.realmethod.RealMethod;
import org.mockito.internal.reporting.PrintSettings.Printable;

import java.lang.reflect.Method;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationTest {

    @Test(expected = VerificationException.class)
    public void test_isVerified_notVerified() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(null, null, new Class[0]), new Object[0], 0, null);

        assertFalse(invocation.isVerified());
    }

    @Test
    public void test_isVerified_verified() {
        Object mock = Mockito.mock(Object.class);
        Mockito.verify(mock).methodCall();

        Invocation invocation = new Invocation(mock, new MockitoMethod(ObjectMethodsGuru.class, "isToString", new Class[]{InvocationOnMock.class}), new Object[]{Mockito.any(InvocationOnMock.class)}, 0, new RealMethod() {
            @Override
            public Object invoke(Object receiver, Object[] arguments) throws Throwable {
                return true;
            }
        });

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void test_getMock() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[0]), new Object[0], 0, null);

        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void test_getMethod() {
        Object mock = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "toString");
        Invocation invocation = new Invocation(mock, new MockitoMethod(method), new Object[0], 0, null);

        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void test_getArguments() {
        Object mock = Mockito.mock(Object.class);
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{Object[].class}), arguments, 0, null);

        assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void test_isVerifiedInOrder() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation1 = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{Object[].class}), new Object[]{new Object[]{1}}, 0, null);
        Invocation invocation2 = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{Object[].class}), new Object[]{new Object[]{2}}, 1, null);

        assertFalse(invocation1.isVerifiedInOrder());
        assertFalse(invocation2.isVerifiedInOrder());

        Mockito.verify(mock).toString((Object[]) Arrays.asList(new Object[]{1}).toArray());
        Mockito.verify(mock).toString((Object[]) Arrays.asList(new Object[]{2}).toArray());

        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();

        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerifiedInOrder());

        assertFalse(invocation1.isVerified());
        assertFalse(invocation2.isVerified());

        Mockito.verifyNoMoreInteractions(mock);
    }

    @Test
    public void test_toString_noMatchers() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[0]), new Object[0], 0, null);

        String result = invocation.toString();

        String expected = "org.mockito.internal.invocation.Invocation@" + Integer.toHexString(invocation.hashCode()) + "";
        assertEquals(expected, result);
    }

    @Test
    public void test_toString_withMatchers() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{Object[].class}), new Object[]{new Object[]{1, 2, 3}}, 0, null);

        String result = invocation.toString(new PrintSettings(true));

        String expected = "org.mockito.internal.invocation.Invocation@" + Integer.toHexString(invocation.hashCode()) + " [" +
                "toString(Object[]) -> " +
                "args: [1, 2, 3]" +
                "]";
        assertEquals(expected, result);
    }

    @Test
    public void test_toString_withPrintable() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{Object[].class}), new Object[]{new Object[]{1, 2, 3}}, 0, null);

        String result = invocation.toString(new PrintSettings(PrintSettings.Printable.VERBOSE));

        String expected = "Invocation: Invocation on mock:\n" +
                "\t+ mock: org.mockito.examples.stubbing.example1.BarMock\n" +
                "\t+ method: toString(Object[])\n" +
                "\t+ arguments: [1, 2, 3]\n" +
                "\t+ sequence number: 1\n" +
                "\t+ location:\n" +
                "\t+--- Invocation on this.bar.foo(int, int, int)\n" +
                "\t+--- at TestClass.testMethod(TestClass.java:13)\n";
        assertEquals(expected, result);
    }

    @Test
    public void test_toString_withPrintableAndArgumentsMatchers() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{Object[].class}), new Object[]{new Object[]{1, 2, 3}}, 0, null);

        String result = invocation.toString(new PrintSettings(PrintSettings.Printable.VERBOSE, new ArgumentsMatcher[]{new Equals(1), new Equals(2), new ArrayEquals(new Object[]{3})}));

        String expected = "Invocation: Invocation on mock:\n" +
                "\t+ mock: org.mockito.examples.stubbing.example1.BarMock\n" +
                "\t+ method: toString(Object[])\n" +
                "\t+ arguments: [ArgumentMatchers.argThat(equalTo(1)), ArgumentMatchers.argThat(equalTo(2)), ArgumentMatchers.arrayContaining((ArgumentMatchers.argThat(equalTo(3)))))]\n" +
                "\t+ sequence number: 1\n" +
                "\t+ location:\n" +
                "\t+--- Invocation on this.bar.foo(int, int, int)\n" +
                "\t+--- at TestClass.testMethod(TestClass.java:13)\n";
        assertEquals(expected, result);
    }

    @Test
    public void test_isToString() {
        MockitoMethod toStringMethod = new MockitoMethod(Object.class, "toString", new Class[]{InvocationOnMock.class});

        assertTrue(Invocation.isToString(Mockito.mock(InvocationOnMock.class)));
        assertFalse(Invocation.isToString(Mockito.mock(Object.class)));
        assertTrue(Invocation.isToString(new Invocation(Mockito.mock(Object.class), toStringMethod, new Object[]{Mockito.any(InvocationOnMock.class)}, 0, null)));
    }

    @Test(expected = RuntimeException.class)
    public void test_callRealMethod_nonVoid() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{Object[].class}), new Object[]{new Object[]{1, 2, 3}}, 0, null);

        invocation.callRealMethod();
    }

    @Test
    public void test_callRealMethod_void() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(void.class, "voidMethod", new Class[0]), new Object[0], 0, new RealMethod() {
            @Override
            public void invoke(Object receiver, Object[] arguments) throws Throwable {
                // do nothing
            }
        });

        invocation.callRealMethod();

        Mockito.verifyNoMoreInteractions(mock);
    }

    @Test
    public void test_isValidException_withException() {
        Object mock = Mockito.mock(Object.class);
        Throwable throwable = new RuntimeException();
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[0]), new Object[0], 0, null);

        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void test_isValidException_withoutException() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[0]), new Object[0], 0, null);

        assertFalse(invocation.isValidException(null));
    }

    @Test
    public void test_isValidReturnType_primitive() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{Boolean.TYPE}), new Object[]{true}, 0, null);

        assertTrue(invocation.isValidReturnType(boolean.class));
    }

    @Test
    public void test_isValidReturnType_object() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{String.class}), new Object[]{null}, 0, null);

        assertTrue(invocation.isValidReturnType(String.class));
    }

    @Test
    public void test_isVoid() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(void.class, "voidMethod", new Class[0]), new Object[0], 0, null);

        assertTrue(invocation.isVoid());
    }

    @Test
    public void test_printMethodReturnType() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{Object.class}), new Object[]{null}, 0, null);

        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void test_getMethodName() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[0]), new Object[0], 0, null);

        assertEquals("toString", invocation.getMethodName());
    }

    @Test
    public void test_returnsPrimitive() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{Boolean.TYPE}), new Object[]{true}, 0, null);

        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void test_getLocation() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{Object[].class}), new Object[]{new Object[]{1, 2, 3}}, 0, null);

        Location expected = new Location();
        Location actual = invocation.getLocation();

        assertEquals(expected, actual);
    }

    @Test
    public void test_getArgumentsCount() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{Object[].class}), new Object[]{new Object[]{1, 2, 3}}, 0, null);

        int expected = 3;
        int actual = invocation.getArgumentsCount();

        assertEquals(expected, actual);
    }

    @Test
    public void test_getRawArguments() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Object.class, "toString", new Class[]{Object[].class}), new Object[]{new Object[]{1, 2, 3}}, 0, null);

        Object[] expected = new Object[]{1, 2, 3};
        Object[] actual = invocation.getRawArguments();

        assertArrayEquals(expected, actual);
    }
}