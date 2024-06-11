import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DelegatingMethodTest {

    @Test
    public void testGetName() {
        Method method = new Method(DelegatingMethod.class, "getName", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String expected = "getName";
        String actual = delegatingMethod.getName();
        assertEquals(expected, actual);
    }

    // Edge case and exception handling: check if getName() returns the correct name for a method with a different name
    @Test(expected = AssertionError.class)
    public void testGetNameWithDifferentName() {
        Method method = new Method(DelegatingMethod.class, "isAbstract", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String actual = delegatingMethod.getName();
        String expected = "getName";
        assertEquals(expected, actual); // This test will fail as the method name is different
    }
}