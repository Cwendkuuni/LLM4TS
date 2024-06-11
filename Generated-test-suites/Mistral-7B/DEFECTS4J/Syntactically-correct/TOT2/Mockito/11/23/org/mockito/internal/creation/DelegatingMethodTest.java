import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;

public class DelegatingMethodTest {

    @Test
    public void testGetName() {
        Method method = new Method("testMethod", new Class[]{}, new Class[0]); // any valid method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        String expectedName = "testMethod";
        Assert.assertEquals(expectedName, name);
    }

    @Test
    public void testGetExceptionTypes_emptyArray() {
        Method method = new Method("testMethod", new Class[]{}, new Class[0]); // method with no exceptions
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[0], exceptionTypes);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullMethod() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidParameterTypes() {
        Method method = new Method("testMethod", new Class[]{"int"}, new Class[0]); // method with invalid parameter types
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
    }

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method("testMethod", new Class[]{}, new Class[]{Exception.class, IOException.class}); // any method with exceptions
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Class<?> expectedExceptionType1 = Exception.class;
        Class<?> expectedExceptionType2 = IOException.class;
        Assert.assertArrayEquals(new Class<?>[]{expectedExceptionType1, expectedExceptionType2}, exceptionTypes);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method("testMethod", new Class[]{}, new Class[]{String[].class}); // method with varargs
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        boolean expectedValue = true;
        Assert.assertEquals(expectedValue, isVarArgs);

        method = new Method("testMethod", new Class[]{}, new Class[0]); // method without varargs
        delegatingMethod = new DelegatingMethod(method);
        isVarArgs = delegatingMethod.isVarArgs();
        expectedValue = false;
        Assert.assertEquals(expectedValue, isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method("testMethod", new Class[]{}, new Class[0]); // non-abstract method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();
        boolean expectedValue = false;
        Assert.assertEquals(expectedValue, isAbstract);

        method = new Method("testMethod", new Class[]{}, new Class[0]); // abstract method
        delegatingMethod = new DelegatingMethod(method);
        isAbstract = delegatingMethod.isAbstract();
        expectedValue = true;
        Assert.assertEquals(expectedValue, isAbstract);
    }

    @Test
    public void testEquals() {
        Method method1 = new Method("testMethod1", new Class[]{}, new Class[0]); // any valid method
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = new Method("testMethod2", new Class[]{}, new Class[0]); // any valid method
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        Method method3 = new Method("testMethod1", new Class[]{}, new Class[0]); // same method as method1
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        boolean equals1 = delegatingMethod1.equals(delegatingMethod1);
        boolean expectedValue1 = true;
        Assert.assertEquals(expectedValue1, equals1);

        boolean equals2 = delegatingMethod1.equals(delegatingMethod2);
        boolean expectedValue2 = false;
        Assert.assertEquals(expectedValue2, equals2);

        boolean equals3 = delegatingMethod1.equals(delegatingMethod3);
        boolean expectedValue3 = true;
        Assert.assertEquals(expectedValue3, equals3);
    }
}