import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.ArgumentMatchers.isA;
import org.mockito.Mockito;
import static org.junit.Assert.*;

public class ArgumentMatchingToolTest {
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher<String>> matchers = new ArrayList<>();
        matchers.add(argThat(isA(String.class)));
        matchers.add(argThat(isA(Integer.class)));
        Object[] arguments = new Object[]{"test arg", 1};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[]{1}, indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyMatchers() {
        List<Matcher<String>> matchers = new ArrayList<>();
        Object[] arguments = new Object[]{"test arg"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], indexes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_emptyArguments() {
        List<Matcher<String>> matchers = new ArrayList<>();
        matchers.add(argThat(isA(String.class)));

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, new Object[0]);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_differentSizes() {
        List<Matcher<String>> matchers = new ArrayList<>();
        matchers.add(argThat(isA(String.class)));
        matchers.add(argThat(isA(Integer.class)));
        Object[] arguments = new Object[]{"test arg", 1, "another arg"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nullMatcher() {
        List<Matcher<String>> matchers = new ArrayList<>();
        matchers.add(null);
        matchers.add(argThat(isA(String.class)));
        Object[] arguments = new Object[]{"test arg", "test arg"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] indexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(new Integer[0], indexes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArgument() {
        List<Matcher<String>> matchers = new ArrayList<>();
        matchers.add(argThat(isA(String.class)));
        Object[] arguments = new Object[]{null};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test(expected = RuntimeException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_matcherException() {
        List<Matcher<String>> matchers = new ArrayList<>();
        matchers.add(argThat(new MyMatcherThatThrowsException()));
        matchers.add(argThat(isA(String.class)));
        Object[] arguments = new Object[]{"test arg", "test arg"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    class MyMatcherThatThrowsException implements Matcher<String> {
        @Override
        public boolean matches(Object argument) {
            throw new RuntimeException("Matcher exception");
        }

        @Override
        public StringDescription describeTo(StringDescription description) {
            return description.appendText("Matcher that throws an exception");
        }
    }
}