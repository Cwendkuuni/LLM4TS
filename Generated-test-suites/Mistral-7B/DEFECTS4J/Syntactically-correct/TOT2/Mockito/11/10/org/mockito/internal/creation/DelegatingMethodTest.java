package org.mockito.internal.creation;
import org.junit.Test;
import org.junit.Assert;

public class DelegatingMethodTest {

    @Test
    public void testGetReturnType() {
        Method method = new Method(SomeClass.class, "someMethod", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> expectedReturnType = String.class;
        Class<?> actualReturnType = delegatingMethod.getReturnType();
        Assert.assertEquals(expectedReturnType, actualReturnType);
    }

    @Test(expected = NullPointerException.class)
    public void testGetReturnTypeWithNullMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(SomeClass.class, "someMethod", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expectedParameterTypes = new Class[] {String.class, int.class};
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(expectedParameterTypes, actualParameterTypes);
    }

    @Test
    public void testGetParameterTypesWithNoParameters() {
        Method method = new Method(SomeClass.class, "someMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expectedParameterTypes = new Class[] {};
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(expectedParameterTypes, actualParameterTypes);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(SomeAbstractClass.class, "someMethod", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expected = true;
        boolean actual = delegatingMethod.isAbstract();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsAbstractFalse() {
        Method method = new Method(SomeConcreteClass.class, "someMethod", new Class[] {String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expected = false;
        boolean actual = delegatingMethod.isAbstract();
        Assert.assertEquals(expected, actual);
    }
}