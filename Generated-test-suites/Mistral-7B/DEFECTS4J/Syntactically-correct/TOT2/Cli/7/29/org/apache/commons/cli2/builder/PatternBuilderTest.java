package org.apache.commons.cli2.builder.test;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilderEmptyConstructor() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Assert.assertNotNull(patternBuilder);
    }

    @Test
    public void testPatternBuilderAllBuilders() {
        PatternBuilder patternBuilder = new PatternBuilder(
            new GroupBuilder(),
            new DefaultOptionBuilder(),
            new ArgumentBuilder());
        Assert.assertNotNull(patternBuilder);
    }

    @Test
    public void testOptionCreate() {
        final PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("d:i:@java.lang.Integer");
        Option option = patternBuilder.create();
        Assert.assertNotNull(option);
    }

    @Test
    public void testOptionCreateMultiple() {
        final PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("d:i:@java.lang.Integer,b:");
        Iterator<Option> options = patternBuilder.create().getOptions().iterator();
        Assert.assertEquals(2, patternBuilder.create().getOptions().size());
        Assert.assertEquals("d", options.next().getLongOpt());
        Assert.assertEquals("b", options.next().getLongOpt());
    }

    @Test
    public void testReset() {
        final PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("d:i:@java.lang.Integer");
        patternBuilder.reset();
        Assert.assertEquals(0, patternBuilder.create().getOptions().size());
    }

    @Test
    public void testWithPatternSimple() {
        final PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("d:i:@java.lang.Integer");
        Option option = patternBuilder.create();
        Assert.assertEquals("d", option.getLongOpt());
        Assert.assertEquals(NumberValidator.class, option.getValidator().getClass());
    }

    @Test
    public void testWithPatternComplex() {
        final PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("b:s,i::,d::i:@java.util.Date,f::,j::,@org.apache.commons.cli.ParseException:,{0},<file>,<files>,:<file>,/url,/url,s:s,s:s,s:s,s:s,s:s,s:s,s:s,s:s,s:s,s:s,s:s,s:s,s:s,s:s,s:s,s:s,s:s,s:s");
        Option option = patternBuilder.create();
        Assert.assertEquals(27, option.getOptions().size());
        // Test individual options here
    }

    @Test
    public void testWithPatternEdgeCases() {
        final PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern(":<file>,:<file>,:<file>,:<file>,:<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>,<file>");
        Iterator<Option> options = patternBuilder.create().getOptions().iterator();
        Assert.assertEquals(25, patternBuilder.create().getOptions().size());
        for (int i = 0; i < 25; i++) {
            Assert.assertEquals("file" + (i + 1), options.next().getLongOpt());
        }
        Assert.assertNull(options.next());
    }

}