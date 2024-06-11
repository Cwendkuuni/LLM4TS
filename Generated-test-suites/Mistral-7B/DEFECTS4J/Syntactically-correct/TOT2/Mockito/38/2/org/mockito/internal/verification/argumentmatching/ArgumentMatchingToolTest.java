
import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.internal.matchers.ContainsString.containsString;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(containsString("expectedValue1"));
        Object[] arguments = new Object[]{"actualValue1", "actualValue2"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Integer[] expected = new Integer[0];
        assertArrayEquals(expected, result);
    }

    // Test cases for edge cases and exception handling scenarios
    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSizes() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(containsString("expectedValue1"));
        Object[] arguments = new Object[]{"actualValue1"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noMatchingMatcher() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(containsString("notExpectedValue"));
        Object[] arguments = new Object[]{"actualValue1", "actualValue2"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Integer[] expected = new Integer[0];
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_multipleMatchingMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(containsString("expectedValue1"));
        matchers.add(containsString("expectedValue2"));
        Object[] arguments = new Object[]{"actualValue1", "actualValue1"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Integer[] expected = new Integer[]{0};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matchingAndNotMatchingMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(containsString("expectedValue1"));
        matchers.add(containsString("notExpectedValue"));
        Object[] arguments = new Object[]{"actualValue1", "actualValue2"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Integer[] expected = new Integer[]{1};
        assertArrayEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void testSafelyMatches_exception() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(containsString("expectedValue"));
        Object[] arguments = new Object[]{new RuntimeException("Test exception")};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }
}