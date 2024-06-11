import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchersAndArgumentsMatch() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        Matcher m1 = Mockito.mock(Matcher.class);
        Matcher m2 = Mockito.mock(Matcher.class);
        Object[] arguments = new Object[]{1, 2};

        matchers.add(m1);
        matchers.add(m2);

        Integer[] indices = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[0], indices);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousArgument() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        Matcher m = Mockito.mock(ContainsExtraTypeInformation.class);
        Object arg = new Object(){};
        Object[] arguments = new Object[]{1, m, 2};

        matchers.add(m);

        Integer[] indices = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(new Integer[]{1}, indices);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_DifferentSizes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[]{1, 2, 3};

        matchers.add(Mockito.mock(Matcher.class));

        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }
}