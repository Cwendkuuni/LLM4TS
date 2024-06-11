import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.creation.DelegatingMethod;

public class DelegatingMethodTest {

    @Test
    public void testGetName() {
        String name = "testMethod";
        Method method = new MockitoMethod(new MockitoMethod.StubMethod(name, new Class[0], new Class[0], new Class[0], new Class[0], new Class[0], new Class[0], false, false, new Class[0], new Class[0]));
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Assert.assertEquals(name, delegatingMethod.getName());
    }
}