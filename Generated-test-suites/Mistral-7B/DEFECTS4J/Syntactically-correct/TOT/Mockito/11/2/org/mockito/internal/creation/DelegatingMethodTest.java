import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetName() {
        Method mockMethod = MockitoMethod.method(String.class, new Class[]{}, String.class, "someMethodName");
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        String expectedName = "someMethodName";
        String actualName = delegatingMethod.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetExceptionTypes() {
        Method mockMethod = MockitoMethod.method(String.class, new Class[]{}, String.class, "someMethodName", Exception1.class, Exception2.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        Class<?>[] expectedExceptionTypes = new Class[]{Exception1.class, Exception2.class};
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();

        assertArrayEquals(expectedExceptionTypes, actualExceptionTypes);
    }

    @Test
    public void testIsAbstract() {
        Method mockMethod = MockitoMethod.method(String.class, new Class[]{}, void.class, "someAbstractMethod", AbstractClass.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        boolean expectedResult = true;
        boolean actualResult = delegatingMethod.isAbstract();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsNotAbstract() {
        Method mockMethod = MockitoMethod.method(String.class, new Class[]{}, String.class, "someConcreteMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        boolean expectedResult = false;
        boolean actualResult = delegatingMethod.isAbstract();

        assertEquals(expectedResult, actualResult);
    }

    public static class AbstractClass {}

    class Exception1 {}
    class Exception2 {}
}