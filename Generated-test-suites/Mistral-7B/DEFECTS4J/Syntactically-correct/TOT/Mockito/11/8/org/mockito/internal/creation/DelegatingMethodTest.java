import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetReturnType() {
        Method testMethod = new Method("testMethod", new Class[]{}, new Class[]{String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Class<?> expectedReturnType = String.class;
        Class<?> actualReturnType = delegatingMethod.getReturnType();

        Assert.assertEquals(expectedReturnType, actualReturnType);
    }
}