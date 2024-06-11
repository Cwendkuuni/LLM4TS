import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new MockMethod("testMethod", new Class[]{Integer.class, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new MockMethod("testMethod", new Class[]{Integer.class, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new MockMethod("testMethod", new Class[]{Integer.class, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        assertEquals("testMethod", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new MockMethod("testMethod", new Class[]{Integer.class, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(2, parameterTypes.length);
        assertEquals(Integer.class, parameterTypes[0]);
        assertEquals(String.class, parameterTypes[1]);
    }

    @Test
    public void testGetReturnType() {
        Method method = new MockMethod("testMethod", new Class[]{Integer.class, String.class}, new Class[]{Integer.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(Integer.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new MockMethod("testMethod", new Class[]{Integer.class}, new Class[]{Integer[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());

        method = new MockMethod("testMethod", new Class[]{String.class});
        delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new MockMethod("testMethod", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());

        method = new MockitoMethod(Mockito.class, "verify", new Class[]{Mockito.class, Method.class});
        delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = new MockMethod("testMethod1", new Class[]{Integer.class, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = new MockMethod("testMethod2", new Class[]{Integer.class, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        Method method3 = new MockMethod("testMethod1", new Class[]{Integer.class, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(method3);

        assertEquals(delegatingMethod1, delegatingMethod1);
        assertEquals(delegatingMethod1, delegatingMethod3);
        assertNotEquals(delegatingMethod1, delegatingMethod2);

        Object notMethodObject = new Object();
        assertNotEquals(delegatingMethod1, notMethodObject);
    }

    @Test
    public void testHashCode() {
        Method method = new MockMethod("testMethod", new Class[]{Integer.class, String.class}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);

        int hashCode1 = delegatingMethod1.hashCode();
        int hashCode2 = delegatingMethod2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}