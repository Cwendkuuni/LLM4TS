import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;

import java.util.Arrays;

public class InvocationTest {

    @Test
    public void testInvocationWithNormalArguments() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[] { int.class, String.class }, new Object[] { 1, "test" });
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[] { 2, "test2" }, 1, null);

        Invocation expectedInvocation = new Invocation(mock, mockitoMethod, new Object[] { 2, "test2" }, 2, null);
        Mockito.verify(mock).testMethod(2, "test2");
        Mockito.verify(mock).testMethod(eq(2), eq("test2")); // Equals matcher
        Mockito.verify(mock).testMethod(anyInt(), eq("test2")); // AnyInt matcher
        Mockito.verify(mock).testMethod(eq(2), anyString()); // AnyString matcher

        Assert.assertEquals(expectedInvocation, invocation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvocationWithNullMethod() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mock, null, new Object[] {}, 1, null);
        Mockito.verify(mock, Mockito.never()).testMethod(anyInt(), anyString());
        Assert.assertNull(invocation.getMethod());
    }

    @Test
    public void testInvocationWithVarArgs() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[] { int.class }, new Object[] { 1 });
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[] { 1, new Object[] { 2, 3 } }, 1, null);

        Invocation expectedInvocation = new Invocation(mock, mockitoMethod, new Object[] { 1, new Object[] { 2, 3 } }, 2, null);
        Mockito.verify(mock).testMethod(1, (Object[]) ArrayUtils.toObject(new Object[] { 2, 3 }));
        Mockito.verify(mock).testMethod(eq(1), Arrays.asList(2, 3).toArray()); // List to Array conversion
        Mockito.verify(mock).testMethod(eq(1), any(Object[].class)); // Any matcher for Object[]

        Assert.assertEquals(expectedInvocation, invocation);
    }

    @Test
    public void testInvocationWithEmptyVarArgs() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[] { int.class }, new Object[] { 1 });
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[] { 1, (Object[]) new Object[0] }, 1, null);

        Mockito.verify(mock).testMethod(1, new Object[0]);
        Mockito.verify(mock).testMethod(eq(1), Arrays.asList().toArray()); // Empty List to Array conversion

        Assert.assertEquals(new Invocation(mock, mockitoMethod, new Object[] { 1, new Object[0] }, 2, null), invocation);
    }

    @Test
    public void testInvocationWithThrowableArgument() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[] { Throwable.class }, new Object[] { new Exception("Test Exception") });
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[] { new Exception("Test Exception") }, 1, null);

        Invocation expectedInvocation = new Invocation(mock, mockitoMethod, new Object[] { new Exception("Test Exception") }, 2, null);
        Mockito.verify(mock).testMethod(new Exception("Test Exception"));

        Assert.assertEquals(expectedInvocation, invocation);
    }

    @Test
    public void testInvocationWithVoidReturnType() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[] {}, new Object[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public Object invoke(Object obj, Object[] objects) throws Throwable {
                // Void method body
                return null;
            }
        });

        // Void methods cannot be verified directly using Mockito
        // Instead, we should check if the method was called or not
        Mockito.verify(mock, Mockito.times(1)).testMethod();

        Assert.assertFalse(invocation.isVerified());
        invocation.markVerified();
        Assert.assertTrue(invocation.isVerified());
    }

    @Test
    public void testInvocationWithPrimitiveReturnType() {
        Object mock = Mockito.mock(Object.class);
        MockitoMethod mockitoMethod = Mockito.method(Object.class, "testMethod", new Class[] {}, new Object[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod() {
            @Override
            public int invoke(Object obj, Object[] objects) throws Throwable {
                return 42;
            }
        });

        int expectedResult = 42;
        Mockito.verify(mock).testMethod();
        int actualResult = (int) invocation.callRealMethod();

        Assert.assertEquals(expectedResult, actualResult);
    }
}