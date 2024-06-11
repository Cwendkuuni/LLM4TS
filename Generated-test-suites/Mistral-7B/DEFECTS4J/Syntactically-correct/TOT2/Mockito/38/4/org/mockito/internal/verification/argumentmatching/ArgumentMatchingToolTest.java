import org.hamcrest.CoreMatchers.*;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.Arguments;
import static org.junit.Assert.*;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_Basic() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher m1 = argThat(isA(Integer.class));
        Matcher m2 = argThat(isA(String.class));
        Object[] arguments = new Object[]{1, "test"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[]{1}, result);
    }

    // Edge case: Different sizes of matchers and arguments
    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSizes() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(isA(Integer.class));
        Object[] arguments = new Object[]{1, "test"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    // Edge case: Matcher is null
    @Test(expected = NullPointerException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcher() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(null);
        Object[] arguments = new Object[]{1, "test"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    // Edge case: Argument is null
    @Test(expected = NullPointerException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArgument() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher m = argThat(isA(Integer.class));
        Object[] arguments = new Object[]{null};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    // Basic test case for safelyMatches method
    @Test
    public void testSafelyMatches_Basic() {
        Matcher m = argThat(isA(Integer.class));
        Object arg = 1;

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.safelyMatches(m, arg);

        assertTrue(result);
    }

    // Edge case: Matcher does not match argument
    @Test
    public void testSafelyMatches_MatcherDoesNotMatch() {
        Matcher m = argThat(isA(String.class));
        Object arg = 1;

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.safelyMatches(m, arg);

        assertFalse(result);
    }

    // Edge case: Throws an exception
    @Test(expected = Throwable.class)
    public void testSafelyMatches_ThrowsException() {
        Matcher m = argThat((Matcher) () -> { throw new RuntimeException("Test Exception"); });
        Object arg = 1;

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.safelyMatches(m, arg);
    }

    // Basic test case for toStringEquals method
    @Test
    public void testToStringEquals_Basic() {
        Matcher m = argThat(isA(String.class));
        Object arg = "test";

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(m, arg);

        assertTrue(result);
    }

    // Edge case: Different object types
    @Test
    public void testToStringEquals_DifferentObjectTypes() {
        Matcher m = argThat(isA(String.class));
        Object arg = 1;

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(m, arg);

        assertFalse(result);
    }

    private static Matcher<?> argThat(Matcher<?> matcher) {
        return Arguments.argThat(matcher);
    }

    private static void assertArrayEquals(Object[] expected, Object[] actual) {
        assertArrayEquals(expected, actual, "Arrays must be equal");
    }

}