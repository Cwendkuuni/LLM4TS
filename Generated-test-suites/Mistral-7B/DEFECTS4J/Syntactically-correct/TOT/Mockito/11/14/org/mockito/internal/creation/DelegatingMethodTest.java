package org.mockito.internal.creation;

import org.junit.*;
import static org.junit.Assert.*;;

public class DelegatingMethodTest {

    @Test
    public void testGetJavaMethod() {
        Method testMethod = new MockObjectMethod(); // assuming this is a valid method object
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        Method returnedMethod = delegatingMethod.getJavaMethod();

        assertTrue(testMethod.equals(returnedMethod));
    }

    @Test
    public void testIsAbstract() {
        Method testMethod = new MockObjectMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertFalse(delegatingMethod.isAbstract());

        Method abstractMethod = new AbstractMockObjectMethod();
        DelegatingMethod abstractDelegatingMethod = new DelegatingMethod(abstractMethod);
        assertTrue(abstractDelegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method testMethod1 = new MockObjectMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(testMethod1);

        Method testMethod2 = new MockObjectMethod();
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(testMethod2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        Method sameMethod = new MockObjectMethod();
        DelegatingMethod sameDelegatingMethod = new DelegatingMethod(sameMethod);

        assertTrue(delegatingMethod1.equals(sameDelegatingMethod));
    }

    @Test
    public void testGetExceptionTypes() {
        Method testMethod = new MockObjectMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetName() {
        Method testMethod = new MockObjectMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertEquals("testMethodName", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method testMethod = new MockObjectMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(1, parameterTypes.length);
        assertEquals(String.class, parameterTypes[0]);
    }

    @Test
    public void testGetReturnType() {
        Method testMethod = new MockObjectMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertEquals(String.class, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method testMethod = new MockObjectMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);
        assertFalse(delegatingMethod.isVarArgs());

        Method varArgsMethod = new VarargsMockObjectMethod();
        DelegatingMethod varArgsDelegatingMethod = new DelegatingMethod(varArgsMethod);
        assertTrue(varArgsDelegatingMethod.isVarArgs());
    }

    static class MockObjectMethod extends Method {
        public MockObjectMethod() {
            super("testMethodName", null, null, null, null);
        }
    }

    static class AbstractMockObjectMethod extends Method implements Cloneable {
        public AbstractMockObjectMethod() {
            super("abstractMethodName", null, null, Modifier.ABSTRACT, null);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    static class VarargsMockObjectMethod extends Method {
        public VarargsMockObjectMethod() {
            super("varargsMethodName", null, new Class[]{String[].class}, null, null);
        }
    }
}