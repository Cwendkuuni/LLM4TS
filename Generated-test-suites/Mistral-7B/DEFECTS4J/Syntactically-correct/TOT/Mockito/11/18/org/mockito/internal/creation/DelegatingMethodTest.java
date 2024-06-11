import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = getClass().getDeclaredMethod("exampleMethod", String.class, int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] expectedExceptionTypes = {IllegalArgumentException.class};
        Class<?>[] actualExceptionTypes = delegatingMethod.getExceptionTypes();

        assertArrayEquals(expectedExceptionTypes, actualExceptionTypes);
    }

    @Test
    public void testGetName() {
        Method method = getClass().getDeclaredMethod("exampleMethod", int.class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String expectedName = "exampleMethod";
        String actualName = delegatingMethod.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    public void testIsAbstract() {
        Method abstractMethod = getClass().getDeclaredMethod("exampleAbstractMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(abstractMethod);

        boolean expectedAbstract = true;
        boolean actualAbstract = delegatingMethod.isAbstract();

        assertTrue(expectedAbstract == actualAbstract);
    }

    // Example method with an exception
    private static void exampleMethod(String argument, int argument2) throws IllegalArgumentException {
        throw new IllegalArgumentException("Invalid argument");
    }

    // Example abstract method
    public abstract void exampleAbstractMethod();
}