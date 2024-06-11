import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_validMatcherAndArguments() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(contains("expectedValue1"));
        Object[] arguments = new Object[]{new Integer(1), "expectedValue2"};
        Integer[] expectedIndexes = new Integer[]{0, 1};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(expectedIndexes, actualIndexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_noSuspiciousMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(contains("expectedValue1"));
        Object[] arguments = new Object[]{new Integer(1), new Integer(1)};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(new Integer[0], actualIndexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_differentTypes_toStringEquals() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation("expectedString") {
            @Override
            public boolean matches(Object argument) {
                return argument instanceof String;
            }

            @Override
            public String toString() {
                return "matchesString";
            }
        });
        Object[] arguments = new Object[]{new Integer(1), "expectedString"};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(new Integer[]{1}, actualIndexes);
    }

    @Test(expected = AssertionError.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_invalidNumberOfMatchers() {
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{new Integer(1)};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test(expected = AssertionError.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_invalidNumberOfArguments() {
        List<Matcher> matchers = Arrays.asList(contains("expectedValue"));
        Object[] arguments = new Object[0];
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nullMatcherList() {
        Object[] arguments = new Object[]{new Integer(1), "expectedString"};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(null, arguments);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_nullArgumentArray() {
        List<Matcher> matchers = Arrays.asList(contains("expectedValue"));
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, null);
    }
}