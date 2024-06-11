
package org.mockito.internal.creation;

import org.junit.*;
import static org.junit.Assert.*;;

public class DelegatingMethodTest {

    @Test
    public void testGetName() {
        Method testMethod = new MockitoMethod(String[].class, "testMethod", new Class[] {int.class}) {
        };
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        String expectedName = "testMethod";
        String actualName = delegatingMethod.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testIsAbstract() {
        Method abstractMethod = new MockitoMethod(void.class, "abstractMethod", new Class[] {}) {
        }.setAccessible(true);
        abstractMethod.setModifiers( abstractMethod.getModifiers() | Modifier.ABSTRACT);
        DelegatingMethod delegatingMethod = new DelegatingMethod(abstractMethod);
        boolean expectedResult = true;
        boolean actualResult = delegatingMethod.isAbstract();
        assertEquals(expectedResult, actualResult);

        Method concreteMethod = new MockitoMethod(int.class, "concreteMethod", new Class[] {}) {
        };
        DelegatingMethod delegatingConcreteMethod = new DelegatingMethod(concreteMethod);
        boolean expectedResultConcrete = false;
        boolean actualResultConcrete = delegatingConcreteMethod.isAbstract();
        assertEquals(expectedResultConcrete, actualResultConcrete);
    }

    @Test
    public void testEquals() {
        Method method1 = new MockitoMethod(String[].class, "testMethod1", new Class[] {int.class}) {
        };
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = new MockitoMethod(String[].class, "testMethod1", new Class[] {int.class}) {
        };
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        Method method3 = new MockitoMethod(String[].class, "testMethod2", new Class[] {int.class}) {
        };
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        assertTrue(delegatingMethod1.equals(delegatingMethod1));
        assertTrue(delegatingMethod1.equals(delegatingMethod2)); // same method object
        assertFalse(delegatingMethod1.equals(delegatingMethod3)); // different method object
    }
}