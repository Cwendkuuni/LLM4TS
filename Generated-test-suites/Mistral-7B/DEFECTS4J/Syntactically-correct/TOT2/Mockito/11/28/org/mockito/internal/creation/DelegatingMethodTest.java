
import org.junit.Test;
import org.junit.Assert;
import static org.mockito.internal.creation.DelegatingMethod.DelegatingMethod;

public class DelegatingMethodTest {

    @Test
    public void testGettingJavaMethod() {
        Method mockMethod = new MockitoMethod(new Method(DelegatingMethod.class, "getJavaMethod", new Class[0]));
        DelegatingMethod delegatingMethod = new DelegatingMethod(mockMethod);

        Method actualMethod = delegatingMethod.getJavaMethod();
        Method expectedMethod = mockMethod;

        Assert.assertEquals(expectedMethod, actualMethod);
    }

    @Test(expected = NullPointerException.class)
    public void testGettingJavaMethodWithNullInput() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(null);
    }

    @Test
    public void testGettingJavaMethodWithNonDelegateMethodInput() {
        Object nonDelegateMethodInput = new Object();
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(DelegatingMethod.class, "getJavaMethod", new Class[0]));

        Assert.assertFalse(delegatingMethod.equals(nonDelegateMethodInput));
    }
}