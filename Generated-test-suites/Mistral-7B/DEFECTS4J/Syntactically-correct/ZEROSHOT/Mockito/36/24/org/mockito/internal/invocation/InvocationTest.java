import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintSettings;
import org.mockito.internal.util.PrintingFriendlyInvocation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class InvocationTest {

    private Invocation invocation;
    private Object mock;

    @Before
    public void setup() {
        mock = Mockito.mock(String.class);
    }

    @Test
    public void testInvokeConstructor() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(new Object[0], invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertEquals(realMethod, invocation.realMethod);
    }

    @Test
    public void testInvokeConstructorWithArguments() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "concat", new Class[]{String.class});
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, new Object[]{"test"}, 1, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(new Object[]{"test"}, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertEquals(realMethod, invocation.realMethod);
    }

    @Test
    public void testInvokeConstructorWithVarArgs() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "format", new Class[]{String.class, Object[].class});
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, new Object[]{"%s %d"}, 1, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(new Object[]{"%s %d"}, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertEquals(realMethod, invocation.realMethod);
    }

    @Test
    public void testGetMock() {
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "concat", new Class[]{String.class});
        Object[] arguments = new Object[]{"test1", "test2"};
        invocation = new Invocation(mock, mockitoMethod, arguments, 1, Mockito.mock(RealMethod.class));

        assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedFalse() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testToString() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        String expected = "1" + MockUtil.getMockName(mock) + ".length()";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithArguments() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "concat", new Class[]{String.class});
        Object[] arguments = new Object[]{"test1", "test2"};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        String expected = "1" + MockUtil.getMockName(mock) + ".concat(\"test1\", \"test2\")";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithMatchers() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();

        String expected = "1" + MockUtil.getMockName(mock) + ".length()" +
                " != (null as int)";
        assertEquals(expected, invocation.toString(matchers, printSettings));
    }

    @Test
    public void testEquals() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation1 = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        assertTrue(invocation1.equals(invocation2));
    }

    @Test
    public void testHashCode() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation1 = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        assertEquals(invocation1.hashCode(), invocation2.hashCode());
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "toString", new Class[0]);
        assertTrue(Invocation.isToString(Mockito.mock(InvocationOnMock.class)));
        assertFalse(Invocation.isToString(new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class))));
    }

    @Test
    public void testIsValidException() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        assertFalse(invocation.isValidException(new RuntimeException()));
        assertFalse(invocation.isValidException(new Exception()));
        assertTrue(invocation.isValidException(new IllegalArgumentException()));
    }

    @Test
    public void testIsValidReturnType() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        assertTrue(invocation.isValidReturnType(int.class));
        assertTrue(invocation.isValidReturnType(Integer.class));
        assertTrue(invocation.isValidReturnType(int[].class));
        assertTrue(invocation.isValidReturnType(int[].class));
        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod mockitoMethod = Mockito.method(void.class, "voidMethod", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        assertEquals("int", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        assertEquals("length", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod mockitoMethod = Mockito.method(boolean.class, "booleanMethod", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Location location = new Location();
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));

        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "concat", new Class[]{String.class});
        Object[] arguments = new Object[]{"test1", "test2"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, Mockito.mock(RealMethod.class));

        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "concat", new Class[]{String.class});
        Object[] arguments = new Object[]{"test1", "test2"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, Mockito.mock(RealMethod.class));

        assertArrayEquals(arguments, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        assertEquals(5, invocation.callRealMethod().intValue());
    }

    @Test
    public void testToStringWithEmptyArguments() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        String expected = "1" + MockUtil.getMockName(mock) + ".length()";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithArrayArguments() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "concat", new Class[]{String[].class});
        Object[] arguments = new Object[]{"test1", "test2"};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        String expected = "1" + MockUtil.getMockName(mock) + ".concat([\"test1\", \"test2\"])" +
                " != (null as java.lang.String[])";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithNullArguments() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "concat", new Class[]{String.class});
        Object[] arguments = new Object[]{null};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        String expected = "1" + MockUtil.getMockName(mock) + ".concat(null)";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithComplexArguments() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "format", new Class[]{String.class, String[].class});
        Object[] arguments = new Object[]{"%s %d", new String[]{"%s", "%d"}};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        String expected = "1" + MockUtil.getMockName(mock) + ".format(\"%s %d\", [\"%s\", \"%d\"])" +
                " != (null as java.lang.String[])";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithMatchersAndMultilinePrintSettings() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        String expected = "1" + MockUtil.getMockName(mock) + ".length()" +
                " != (null as int)" +
                " and" +
                " 1" + MockUtil.getMockName(mock) + ".length()" +
                " == 5";
        assertEquals(expected, invocation.toString(matchers, printSettings));
    }

    @Test
    public void testToStringWithMatchersAndSingleLinePrintSettings() {
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", new Class[0]);
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();

        String expected = "1" + MockUtil.getMockName(mock) + ".length()" +
                " != (null as int) and" +
                " 1" + MockUtil.getMockName(mock) + ".length()" +
                " == 5";
        assertEquals(expected, invocation.toString(matchers, printSettings));
    }
}