import org.junit.Test;
import org.junit.experimental.methods.MethodInvoker;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() throws Exception {
        Class<?> clazz = MockitoMethod.class;
        Method method = clazz.getDeclaredMethod("getJavaMethod", (Class[]) null);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedExceptionTypes = new Class[] {IllegalArgumentException.class};
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();

        assertArrayEquals(expectedExceptionTypes, actualExceptionTypes);
    }

    @Test
    public void testgetName() throws Exception {
        Class<?> clazz = MockitoMethod.class;
        Method method = clazz.getDeclaredMethod("getJavaMethod", (Class[]) null);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String expectedName = "getJavaMethod";
        String actualName = delegatingMethod.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetParameterTypes() throws Exception {
        Class<?> clazz = MockitoMethod.class;
        Method method = clazz.getDeclaredMethod("getJavaMethod", String.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedParameterTypes = new Class[] {String.class, int.class};
        Class<?>[] actualParameterTypes = delegatingMethod.getParameterTypes();

        assertArrayEquals(expectedParameterTypes, actualParameterTypes);
    }
}