import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreateOptionWithSingleCharacter() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals('a', option.getOpt());
    }

    @Test
    public void testCreateOptionWithMandatoryCharacter() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!a");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.isRequired());
        assertEquals('a', option.getOpt());
    }

    @Test
    public void testCreateOptionWithOptionalCharacter() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertFalse(option.isRequired());
        assertEquals('a', option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidPatternCharacter() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("@a");
        patternBuilder.create();
    }

    @Test
    public void testCreateOptionWithMultipleCharacterTypes() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%d:%s");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals('%', option.getOpt());
        assertTrue(option.getArgument().isRequired());
    }

    @Test
    public void testCreateMultipleOptions() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:b:c");
        Option option1 = patternBuilder.create();
        Option option2 = patternBuilder.create();
        Option option3 = patternBuilder.create();
        assertEquals('a', option1.getOpt());
        assertEquals('b', option2.getOpt());
        assertEquals('c', option3.getOpt());
    }

    @Test
    public void testCreateOptionWithMultipleValues() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("@,:files:*");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals('@', option.getOpt());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().isMultipleValuesAllowed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidOptionWithNoPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.create();
    }
}