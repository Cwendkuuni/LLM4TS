package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.MockitoMethod;
import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetName() {
        Method mockMethod = Mockito.mock(Method.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        String expectedName = "getName";
        String actualName = delegatingMethod.getName();
        assertEquals(expectedName, actualName);

        Method spyMethod = Mockito.spy(mockMethod);
        DelegatingMethod spyDelegatingMethod = new DelegatingMethod(spyMethod);
        Mockito.verify(spyMethod).getName();
        assertEquals(expectedName, spyDelegatingMethod.getName());
    }

    @Test
    public void testIsAbstract() {
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod1.isAbstract());
        assertFalse(delegatingMethod2.isAbstract());

        method1.setAccessible(true);
        method1.setModifiers(method1.getModifiers() | Modifier.ABSTRACT);
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method1);
        assertTrue(delegatingMethod3.isAbstract());

        method2.setAccessible(true);
        method2.setModifiers(method2.getModifiers() | Modifier.ABSTRACT);
        DelegatingMethod delegatingMethod4 = new DelegatingMethod(method2);
        assertTrue(delegatingMethod4.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        Method method3 = method1;
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);
        assertTrue(delegatingMethod1.equals(delegatingMethod3));

        Method method4 = method1;
        DelegatingMethod delegatingMethod4 = new DelegatingMethod(method4);
        assertTrue(delegatingMethod1.equals(delegatingMethod4));

        DelegatingMethod delegatingMethod5 = new DelegatingMethod(method1);
        assertTrue(delegatingMethod1.equals(delegatingMethod5));
    }
}