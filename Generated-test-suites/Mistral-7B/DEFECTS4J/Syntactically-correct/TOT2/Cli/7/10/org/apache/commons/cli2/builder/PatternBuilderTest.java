import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class PatternBuilderTest {
    @Test
    public void testPatternBuilderConstructor() {
        PatternBuilder patternBuilder = new PatternBuilder();
        assertNotNull(patternBuilder);
    }
}