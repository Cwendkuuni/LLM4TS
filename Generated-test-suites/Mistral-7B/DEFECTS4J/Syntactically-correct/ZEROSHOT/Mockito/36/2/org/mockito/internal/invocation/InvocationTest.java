package org.mockito.tests.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintingFriendlyInvocation;
import org.mockito.internal.util.PrintingFriendlyRealMethod;
import org.mockito.internal.util.MockitoMethod;
import org.mockito.internal.util.MockitoTypeUtils;
import org.mockito.internal.util.MockitoTypeUtils.$Type;
import org.mockito.internal.util.MockitoTypeUtils.$Void;
import org.mockito.internal.util.Objects;
import org.mockito.internal.util.StringUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link Invocation} class.
 *
 * @author <a href="mailto:kostyaby@gmail.com">Kostya Bychkov</a>
 * @since 1.8.5
 */
public class InvocationTest {

    @Test
    public void testConstructor() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public void test(int arg1, int[] arg2)");
        Object[] rawArguments = new Object[]{1, new Object[]{2, 3, 4}};
        RealMethod realMethod = new PrintingFriendlyRealMethod(MockitoTypeUtils.method("public void test(int, int[])"));

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{1, new Object[]{2, 3, 4}}, 0, realMethod);

        // then
        Assert.assertEquals(mock, invocation.getMock());
        Assert.assertEquals(mockitoMethod, invocation.getMethod());
        Assert.assertArrayEquals(new Object[]{1, new Object[]{2, 3, 4}}, invocation.getArguments());
        Assert.assertArrayEquals(new Object[]{1, new Object[]{2, 3, 4}}, invocation.getRawArguments());
        Assert.assertEquals(0, invocation.getSequenceNumber());
        Assert.assertFalse(invocation.isVerified());
        Assert.assertFalse(invocation.isVerifiedInOrder());
        Assert.assertNull(invocation.location);
        Assert.assertEquals(realMethod, invocation.realMethod);
    }

    @Test
    public void testToString() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public void test(int arg1, int[] arg2)");
        Object[] arguments = new Object[]{1, new Object[]{2, 3, 4}};
        PrintSettings printSettings = new PrintSettings();

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, null);

        // then
        String expectedToString = "test(int arg1, int[] arg2)";
        String toString = invocation.toString(printSettings);
        Assert.assertEquals(expectedToString, toString);
    }

    @Test
    public void testToStringWithMatchers() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public void test(int arg1, int arg2)", int.class, int.class);
        Object[] arguments = new Object[]{1, 2};
        PrintSettings printSettings = new PrintSettings();

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, null);

        // then
        List<Matcher> argumentsToMatchers = invocation.argumentsToMatchers();
        String expectedToString = String.format("test(%s, %s)", new Equals(1).toString(), new Equals(2).toString());
        String toString = invocation.toString(argumentsToMatchers, printSettings);
        Assert.assertEquals(expectedToString, toString);
    }

    @Test
    public void testToStringWithMultilinePrintSettings() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public void test(int arg1, int arg2, String arg3)", int.class, int.class, String.class);
        Object[] arguments = new Object[]{1, 2, "test"};
        PrintSettings printSettings = new PrintSettings(true);

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, null);

        // then
        List<Matcher> argumentsToMatchers = invocation.argumentsToMatchers();
        String expectedToString = String.format("test(%s, %s, %s)",
                new Equals(1).toString(), new Equals(2).toString(), new StringContains("test").toString());
        String toString = invocation.toString(argumentsToMatchers, printSettings);
        Assert.assertEquals(expectedToString, toString);
    }

    @Test
    public void testEquals() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod1 = MockitoTypeUtils.method("public void test(int arg1, int arg2)", int.class, int.class);
        MockitoMethod mockitoMethod2 = MockitoTypeUtils.method("public void test(int arg1, int arg2)", int.class, int.class);
        Object[] arguments1 = new Object[]{1, 2};
        Object[] arguments2 = new Object[]{1, 2};
        Invocation invocation1 = new Invocation(mock, mockitoMethod1, arguments1, 0, null);
        Invocation invocation2 = new Invocation(mock, mockitoMethod2, arguments2, 0, null);

        // when

        // then
        Assert.assertTrue(invocation1.equals(invocation1));
        Assert.assertTrue(invocation1.equals(invocation2));
        Assert.assertTrue(invocation2.equals(invocation1));
        Assert.assertFalse(invocation1.equals(null));
        Assert.assertFalse(invocation1.equals(new Object() {}));
        Assert.assertFalse(invocation1.equals(new Object() {}.getClass()));
        Assert.assertFalse(invocation1.equals("test"));
    }

    @Test
    public void testIsToString() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public String test()", String.class);

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 0, null);

        // then
        Assert.assertFalse(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public void test() throws Exception");

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 0, null);

        // then
        Assert.assertTrue(invocation.isValidException(new Exception("test")));
        Assert.assertFalse(invocation.isValidException(new RuntimeException("test")));
        Assert.assertFalse(invocation.isValidException(new NullPointerException()));
    }

    @Test
    public void testIsValidReturnType() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public int test()");

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 0, null);

        // then
        Assert.assertTrue(invocation.isValidReturnType(int.class));
        Assert.assertTrue(invocation.isValidReturnType(Integer.class));
        Assert.assertFalse(invocation.isValidReturnType(String.class));
        Assert.assertFalse(invocation.isValidReturnType(List.class));
    }

    @Test
    public void testIsVoid() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public void test()");

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 0, null);

        // then
        Assert.assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public String test()");

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 0, null);

        // then
        Assert.assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public void test()");

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 0, null);

        // then
        Assert.assertEquals("test", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public int test()");

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 0, null);

        // then
        Assert.assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public void test()");
        Location location = new Location();

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 0, null);
        invocation.setLocation(location);

        // then
        Assert.assertEquals(location, invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public void test(int arg1, int arg2)");

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 0, null);

        // then
        Assert.assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Exception {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public int test(int arg1) throws Exception");
        RealMethod realMethod = new PrintingFriendlyRealMethod(mockitoMethod);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{1}, 0, realMethod);

        // when
        int result = invocation.callRealMethod();

        // then
        Assert.assertEquals(1, result);
    }

    @Test(expected = MockitoAssertionError.class)
    public void testCallRealMethodWithException() throws Exception {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public int test(int arg1) throws Exception");
        RealMethod realMethod = new PrintingFriendlyRealMethod(mockitoMethod);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{1}, 0, realMethod);

        // when
        invocation.callRealMethod();

        // then
        // exception is thrown
    }

    @Test
    public void testMarkVerified() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public void test(int arg1, int arg2)");
        Object[] arguments = new Object[]{1, 2};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, null);

        // when
        invocation.markVerified();

        // then
        Assert.assertTrue(invocation.isVerified());
        Assert.assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        // given
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = MockitoTypeUtils.method("public void test(int arg1, int arg2)");
        Object[] arguments = new Object[]{1, 2};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, null);

        // when
        invocation.markVerified();
        invocation.markVerifiedInOrder();

        // then
        Assert.assertTrue(invocation.isVerified());
        Assert.assertTrue(invocation.isVerifiedInOrder());
    }
}