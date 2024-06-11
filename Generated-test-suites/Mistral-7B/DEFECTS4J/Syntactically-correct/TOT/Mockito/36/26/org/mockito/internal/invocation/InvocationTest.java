import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class InvocationTest {

    private Invocation invocation;
    private MockitoMock mock;

    @Before
    public void setup() {
        mock = mock(Object.class);
        invocation = new Invocation(mock, new MockitoMethod(Object.class, "method", new Class[]{}, new Class[]{}), new Object[0], 1, mock(RealMethod.class).getMockForConcrete(mock.getClass(), Object[].class));
    }

    @Test
    public void testInvokeNormalScenario() {
        when(mock.method((String) ArgumentMatchers.anyString())).thenReturn("test");

        String result = invocation.callRealMethod();

        Assert.assertEquals("test", result);
        Assert.assertTrue(invocation.isVerified());
        Assert.assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testInvokeWithVarArgs() {
        Object[] args = new Object[]{1, 2, 3};
        when(mock.method(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.arrayOf(ArgumentMatchers.anyInt()))).thenReturn(args[0]);

        Object[] result = (Object[]) invocation.callRealMethod(1, 2, new Object[]{3, 4});

        Assert.assertEquals(args[0], result[0]);
        Assert.assertTrue(invocation.isVerified());
        Assert.assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testInvokeThrowsException() {
        try {
            invocation.callRealMethod();
            Assert.fail("Expected exception");
        } catch (Exception e) {
            Assert.assertTrue(invocation.isValidException(e));
            Assert.assertFalse(invocation.isVerified());
            Assert.assertFalse(invocation.isVerifiedInOrder());
        }
    }

    @Test
    public void testInvokeReturnsWrongType() {
        when(mock.method()).thenReturn(1);

        Integer result = (Integer) invocation.callRealMethod();

        Assert.assertEquals(1, result);
        Assert.assertTrue(invocation.isValidReturnType(Integer.class));
        Assert.assertFalse(invocation.isVerified());
        Assert.assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testInvokeIsVoid() {
        mock.method();

        Assert.assertFalse(invocation.isVoid());
    }

    @Test
    public void testMarkVerified() {
        invocation.markVerified();

        Assert.assertTrue(invocation.isVerified());
        Assert.assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        invocation.markVerifiedInOrder();

        Assert.assertTrue(invocation.isVerified());
        Assert.assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testMarkVerifiedMultipleTimes() {
        invocation.markVerified();
        invocation.markVerified();

        Assert.assertTrue(invocation.isVerified());
        Assert.assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testMarkVerifiedInOrderMultipleTimes() {
        invocation.markVerified();
        invocation.markVerifiedInOrder();
        invocation.markVerifiedInOrder();

        Assert.assertTrue(invocation.isVerified());
        Assert.assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testMarkVerifiedWithSameArguments() {
        invocation.markVerified(1, 2, new Object[]{3, 4});
        invocation.markVerified(1, 2, new Object[]{3, 4});

        Assert.assertTrue(invocation.isVerified());
        Assert.assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testMarkVerifiedWithDifferentArguments() {
        invocation.markVerified(1, 2, new Object[]{3, 4});
        invocation.markVerified(1, 2, new Object[]{5, 6});

        Assert.assertTrue(invocation.isVerified());
        Assert.assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testPrintableInvocation() {
        String expected = "1 - test.mockito.internal.invocation.Invocation: test.mockito.internal.invocation.Invocation@478227e";
        Assert.assertEquals(expected, invocation.toString());
    }

    @Test
    public void testPrintableInvocationWithArguments() {
        Object[] arguments = new Object[]{"arg1", 1, "arg3"};
        invocation = new Invocation(mock, new MockitoMethod(Object.class, "method", new Class[]{}, new Class[]{}), arguments, 1, mock(RealMethod.class).getMockForConcrete(mock.getClass(), Object[].class));

        String expected = "1 - test.mockito.internal.invocation.Invocation: test.mockito.internal.invocation.Invocation@258966c"
                + "\narguments:\n[arg1, 1, arg3]";
        Assert.assertEquals(expected, invocation.toString());
    }

    @Test
    public void testPrintingFriendlyInvocation() {
        Object[] arguments = new Object[]{"arg1", 1, "arg3"};
        invocation = new Invocation(mock, new MockitoMethod(Object.class, "method", new Class[]{}, new Class[]{}), arguments, 1, mock(RealMethod.class).getMockForConcrete(mock.class, Object[].class));

        PrintingFriendlyInvocation printingFriendlyInvocation = new PrintingFriendlyInvocation(invocation);
        String expected = "Invocation on test.mockito.internal.invocation.Invocation:\n"
                + "\tmock: test.mockito.internal.invocation.Invocation@258966c\n"
                + "\tmethod: method()\n"
                + "\targuments: [arg1, 1, arg3]\n"
                + "\tsequence number: 1\n"
                + "\tlocation: unknown source";
        Assert.assertEquals(expected, printingFriendlyInvocation.toString());
    }

    @Test
    public void testPrintingFriendlyInvocationWithThrowable() {
        try {
            throw new RuntimeException("Test exception");
        } catch (RuntimeException e) {
            Invocation invocationWithThrowable = new Invocation(mock, new MockitoMethod(Object.class, "method", new Class[]{}, new Class[]{RuntimeException.class}), null, 1, mock(RealMethod.class).getMockForConcrete(mock.class, RuntimeException[].class));
            invocationWithThrowable.callRealMethod();

            PrintingFriendlyInvocation printingFriendlyInvocation = new PrintingFriendlyInvocation(invocationWithThrowable);
            String expected = "Invocation on test.mockito.internal.invocation.Invocation:\n"
                    + "\tmock: test.mockito.internal.invocation.Invocation@12664112\n"
                    + "\tmethod: method(RuntimeException)\n"
                    + "\targuments: []\n"
                    + "\tsequence number: 1\n"
                    + "\tlocation: test.mockito.internal.invocation.InvocationTest:\n"
                    + "\t\tInvocationTest.java:31: throw new RuntimeException(\"Test exception\")\n"
                    + "\t\tTestInvocationTest.java:0: method testInvokeThrowsException()\n"
                    + "\texception: Test exception\n";
            Assert.assertEquals(expected, printingFriendlyInvocation.toString());
        }
    }

    @Test
    public void testIsToString() {
        Assert.assertTrue(Invocation.isToString(new InvocationOnMock() {
            @Override
            public Object getMock() {
                return mock;
            }

            @Override
            public MockitoMethod getMethod() {
                return new MockitoMethod(Object.class, "toString", new Class[]{}, new Class[0]);
            }
        }));

        Assert.assertFalse(Invocation.isToString(new InvocationOnMock() {
            @Override
            public Object getMock() {
                return mock;
            }

            @Override
            public MockitoMethod getMethod() {
                return new MockitoMethod(Object.class, "equals", new Class[]{Object.class}, new Class[0]);
            }
        }));
    }
}