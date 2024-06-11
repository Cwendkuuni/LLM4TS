import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilderConstruction() {
        PatternBuilder patternBuilder = new PatternBuilder();
        assertNotNull(patternBuilder);
    }

    @Test
    public void testCreateOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        patternBuilder.reset();
        assertNull(patternBuilder.create());
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a");
        Option option = patternBuilder.create();
        assertEquals("-a", option.getLongOpt());
        assertEquals('a', option.getOpt());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());

        patternBuilder.reset();
        patternBuilder.withPattern("--foo <arg> @com.example.MyClass");
        Option option2 = patternBuilder.create();
        assertEquals("--foo", option2.getLongOpt());
        assertEquals('f', option2.getOpt());
        assertNotNull(option2.getValue());
        assertEquals("com.example.MyClass", option2.getValue().getClass().getName());
    }
}