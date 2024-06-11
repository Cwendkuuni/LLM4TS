import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import java.util.ArrayList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new ContainsExtraTypeInformation(String.class, "test"));
        matchers.add(new ContainsExtraTypeInformation(Integer.class, 1));
        Object[] arguments = new Object[]{"test", 2};

        Integer[] expectedIndexes = new Integer[]{1};
        Integer[] actualIndexes = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        Assert.assertArrayEquals(expectedIndexes, actualIndexes);
    }
}