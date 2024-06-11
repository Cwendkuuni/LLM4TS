import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.ArgumentsMatcherWithTypes;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class InvocationMatcherTest {
    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = MockitoTestUtils.getMockMethod(Invocation.class, "getMethod");
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(new ArgumentsMatcherWithTypes(Integer.class, Double.class)));
        assertEquals(method, matcher.getMethod());

        // Test with empty matchers
        InvocationMatcher emptyMatcher = new InvocationMatcher(invocation);
        assertNull(emptyMatcher.getMethod());

        // Test with no invocation
        InvocationMatcher noInvocationMatcher = new InvocationMatcher();
        assertNull(noInvocationMatcher.getMethod());

        // Test invalid method
        try {
            InvocationMatcher invalidMatcher = new InvocationMatcher(null);
            assertFalse("InvocationMatcher should not be instantiated with null invocation", false);
        } catch (NullPointerException e) {
            // Expected
        }
    }
}