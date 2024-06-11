import org.junit.Test;
import static org.mockito.ArgumentMatchingTool.*;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = Arrays.asList(
                equalTo("expectedString"),
                instanceOf(String.class)
        );
        Object[] arguments = new Object[]{new Integer(1), "expectedString"};

        Integer[] expectedIndexes = new Integer[]{1};
        Integer[] actualIndexes = getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expectedIndexes, actualIndexes);
    }
}