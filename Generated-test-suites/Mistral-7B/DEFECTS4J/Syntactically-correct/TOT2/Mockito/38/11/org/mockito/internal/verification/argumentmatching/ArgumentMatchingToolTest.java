
import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.Arrays;
import java.util.Collections;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = Arrays.asList(
            ContainsExtraTypeInformation.not(new ContainsExtraTypeInformation(int.class)),
            ContainsExtraTypeInformation.not(new ContainsExtraTypeInformation(String.class))
        );
        Object[] arguments = new Object[]{1, "mockito"};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expectedIndexes = new Integer[]{0};
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(expectedIndexes, actualIndexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyMatchers() {
        Object[] arguments = new Object[]{1, "mockito"};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expectedIndexes = new Integer[0];
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(Collections.emptyList(), arguments);
        Assert.assertArrayEquals(expectedIndexes, actualIndexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyArguments() {
        List<Matcher> matchers = Arrays.asList(
                ContainsExtraTypeInformation.not(new ContainsExtraTypeInformation(int.class)),
                ContainsExtraTypeInformation.not(new ContainsExtraTypeInformation(String.class))
        );
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expectedIndexes = new Integer[0];
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, new Object[0]);
        Assert.assertArrayEquals(expectedIndexes, actualIndexes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SizeMismatch() {
        List<Matcher> matchers = Arrays.asList(
                ContainsExtraTypeInformation.not(new ContainsExtraTypeInformation(int.class)),
                ContainsExtraTypeInformation.not(new ContainsExtraTypeInformation(String.class))
        );
        Object[] arguments = new Object[]{1, "mockito", 3};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NullMatcher() {
        Object[] arguments = new Object[]{1, "mockito"};
        List<Matcher> matchers = Arrays.asList(null,
                ContainsExtraTypeInformation.not(new ContainsExtraTypeInformation(int.class)),
                ContainsExtraTypeInformation.not(new ContainsExtraTypeInformation(String.class))
        );
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expectedIndexes = new Integer[0];
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(expectedIndexes, actualIndexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArgument() {
        List<Matcher> matchers = Arrays.asList(
                ContainsExtraTypeInformation.not(new ContainsExtraTypeInformation(int.class)),
                ContainsExtraTypeInformation.not(new ContainsExtraTypeInformation(String.class))
        );
        Object[] arguments = new Object[]{1, null};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] expectedIndexes = new Integer[0];
        Integer[] actualIndexes = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(expectedIndexes, actualIndexes);
    }
}