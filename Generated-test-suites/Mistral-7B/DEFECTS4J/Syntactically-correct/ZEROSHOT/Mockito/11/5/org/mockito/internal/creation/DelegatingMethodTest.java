import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new MockMethod("testMethod", new Class[] { String.class, int.class }, new Class[] { String.class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertArrayEquals(new Class[]{}, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new MockMethod("testMethod", new Class[] { String.class, int.class }, new Class[] { String.class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new MockMethod("testMethod", new Class[] { String.class, int.class }, new Class[] { String.class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();
        assertEquals("testMethod", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new MockMethod("testMethod", new Class[] { String.class, int.class }, new Class[] { String.class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertArrayEquals(new Class[] { String.class, int.class }, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new MockMethod("testMethod", new Class[] { String.class, int.class }, new Class[] { String.class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(String.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new MockMethod("testMethod", new Class[] { String.class, int.class, String[].class }, new Class[] { String.class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isVarArgs = delegatingMethod.isVarArgs();
        assertTrue(isVarArgs);

        method = new MockMethod("testMethod", new Class[] { String.class, int.class }, new Class[] { String.class });
        delegatingMethod = new DelegatingMethod(method);

        isVarArgs = delegatingMethod.isVarArgs();
        assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new MockMethod("testMethod", new Class[] { String.class, int.class }, new Class[] { String.class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean isAbstract = delegatingMethod.isAbstract();
        assertFalse(isAbstract);

        method = new MockitoMethod() {
            @Override
            public String getName() {
                return "abstractMethod";
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] {};
            }

            @Override
            public Class<?> getReturnType() {
                return void.class;
            }

            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {};
            }

            @Override
            public boolean isVarArgs() {
                return false;
            }

            @Override
            public boolean isAbstract() {
                return true;
            }
        };
        delegatingMethod = new DelegatingMethod(method);

        isAbstract = delegatingMethod.isAbstract();
        assertTrue(isAbstract);
    }

    @Test
    public void testEquals() {
        Method method1 = new MockMethod("testMethod1", new Class[] { String.class, int.class }, new Class[] { String.class });
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = new MockMethod("testMethod2", new Class[] { String.class, int.class }, new Class[] { String.class });
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        Method method3 = method1;
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        Object notMethod = new Object();
        DelegatingMethod delegatingMethod4 = new DelegatingMethod(method1);

        assertTrue(delegatingMethod1.equals(delegatingMethod1));
        assertTrue(delegatingMethod1.equals(delegatingMethod3));
        assertTrue(delegatingMethod1.equals(method1));
        assertFalse(delegatingMethod1.equals(notMethod));
        assertFalse(delegatingMethod1.equals(method2));
        assertFalse(delegatingMethod1.equals(delegatingMethod4));
        assertFalse(delegatingMethod1.equals(null));
    }

    @Test
    public void testHashCode() {
        Method method = new MockMethod("testMethod", new Class[] { String.class, int.class }, new Class[] { String.class });
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode1 = delegatingMethod.hashCode();
        int hashCode2 = delegatingMethod.hashCode();

        assertEquals(hashCode1, hashCode2);

        Method method2 = new MockitoMethod() {
            @Override
            public String getName() {
                return "testMethod";
            }

            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int.class };
            }

            @Override
            public Class<?> getReturnType() {
                return String.class;
            }

            @Override
            public Class<?>[] getExceptionTypes() {
                return new Class[] {};
            }

            @Override
            public boolean isVarArgs() {
                return false;
            }

            @Override
            public boolean isAbstract() {
                return false;
            }
        };
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        int hashCode3 = delegatingMethod.hashCode();
        int hashCode4 = delegatingMethod2.hashCode();

        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }
}