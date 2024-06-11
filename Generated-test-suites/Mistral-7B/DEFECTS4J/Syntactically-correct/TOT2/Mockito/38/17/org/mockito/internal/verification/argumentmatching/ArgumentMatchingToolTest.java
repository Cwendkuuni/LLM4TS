import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ArgumentMatchingToolTest {
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher m1 = new ContainsExtraTypeInformation("expectedString", String.class);
        Matcher m2 = new ContainsExtraTypeInformation("expectedInt", Integer.class);
        matchers.add(m1);
        matchers.add(m2);
        Object[] arguments = new Object[]{"expectedString1", 1, "expectedString2"};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        // Test when the number of matchers and arguments do not match
        matchers.clear();
        matchers.add(m1);
        arguments = new Object[]{"expectedString1", 1, "expectedString2", "expectedInt"};
        actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, actual.length);

        // Test when no suspicious arguments are found
        matchers = new ArrayList<>();
        matchers.add(m1);
        matchers.add(new ContainsRegex("expectedRegex", "regexPattern"));
        arguments = new Object[]{"expectedString1", 1, "expectedString2"};
        actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertEquals(0, actual.length);

        // Test when a suspicious argument is found
        matchers.clear();
        matchers.add(m1);
        matchers.add(m2);
        arguments = new Object[]{"expectedString1", 1, new Integer(1)};
        actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertThat(actual, is(new Integer[]{1}));
    }
}