import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        Assert.assertNotNull(option);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.create();
        patternBuilder.reset();
        Assert.assertEquals(0, patternBuilder.options.size());
    }

    @Test
    public void testCreateMultipleOptions() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:b:c");
        Assert.assertEquals(3, patternBuilder.options.size());
    }

    @Test
    public void testCreateOptionWithValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%d");
        Option option = (Option) patternBuilder.options.iterator().next();
        Assert.assertTrue(option.getValidator() instanceof NumberValidator);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateOptionWithInvalidPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("x:");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidCharacter() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("@:");
    }

}
//