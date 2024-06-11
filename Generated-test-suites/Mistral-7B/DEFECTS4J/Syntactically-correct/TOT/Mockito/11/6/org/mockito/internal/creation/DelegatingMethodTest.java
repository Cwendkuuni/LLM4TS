import org.junit.Test;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;;

public class DelegatingMethodTest {

    @Test
    public void testGetJavaMethod() {
        Method mockMethod = mock(Method.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        Method actualMethod = delegatingMethod.getJavaMethod();

        assertSame(mockMethod, actualMethod);
    }

    @Test
    public void testGetName() {
        Method mockMethod = mock(Method.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        String actualName = delegatingMethod.getName();

        assertEquals(mockMethod.getName(), actualName);
    }

    @Test
    public void testGetParameterTypes() {
        Method mockMethod = mock(Method.class);
        Class<?>[] parameterTypes = new Class[] { String.class, int.class };
        when(mockMethod.getParameterTypes()).thenReturn(parameterTypes);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();

        assertArrayEquals(parameterTypes, actualParameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method mockMethod = mock(Method.class);
        Class<?> returnType = String.class;
        when(mockMethod.getReturnType()).thenReturn(returnType);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        Class<?> actualReturnType = delegatingMethod.getReturnType();

        assertEquals(returnType, actualReturnType);
    }

    @Test
    public void testIsAbstract() {
        Method mockMethod = mock(Method.class);
        when(mockMethod.isAbstract()).thenReturn(false);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        boolean actualIsAbstract = delegatingMethod.isAbstract();

        assertFalse(actualIsAbstract);
    }

    @Test
    public void testIsVarArgs() {
        Method mockMethod = mock(Method.class);
        when(mockMethod.isVarArgs()).thenReturn(true);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);
        boolean actualIsVarArgs = delegatingMethod.isVarArgs();

        assertTrue(actualIsVarArgs);
    }

    @Test
    public void testEquals() {
        Method mockMethod1 = mock(Method.class);
        Method mockMethod2 = mock(Method.class);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(mockMethod1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(mockMethod2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));

        when(mockMethod1.equals(mockMethod2)).thenReturn(true);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method mockMethod = mock(Method.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        int expectedHashCode = mockMethod.hashCode();
        int actualHashCode = delegatingMethod.hashCode();

        assertEquals(expectedHashCode, actualHashCode);
    }
}