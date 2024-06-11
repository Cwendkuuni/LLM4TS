import static org.junit.Assert.assertEquals;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.annotations.Test;

public class InvocationMatcherTest {
    @Test
    public void testGetMethod() {
        Invocation invocation = MockitoTestUtils.createMockInvocation("testMethod(int, String)", new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method expectedMethod = MockitoTestUtils.createMethodMock("testMethod", new Class[]{int.class, String.class});
        Method actualMethod = invocationMatcher.getMethod();

        assertEquals(expectedMethod.getName(), actualMethod.getName());
        assertEquals(expectedMethod.getParameterTypes(), actualMethod.getParameterTypes());
    }
}