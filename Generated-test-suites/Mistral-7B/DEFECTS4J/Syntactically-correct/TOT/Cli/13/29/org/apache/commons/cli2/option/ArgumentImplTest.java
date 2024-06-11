package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArgumentImplTest {

    private Argument arg;
    private WriteableCommandLine cmd;

    @Before
    public void setup() {
        arg = new ArgumentImpl("argName", "Description", 1, 1, Arguments.DEFAULT_INITIAL_SEPARATOR, Arguments.DEFAULT_SUBSEQUENT_SEPARATOR, new Validator() {
            @Override
            public void validate(List values) throws InvalidArgumentException {
                Assert.assertEquals("expected value", "testValue", values.get(0));
            }
        }, "--consumeRemaining", null, 0);

        cmd = new WriteableCommandLine();
    }

    @Test
    public void testMinimumOne() {
        cmd.parse(arg.parse(new String[]{"argName", "testValue"}));
        Assert.assertEquals(arg.getName(), cmd.getOptionNames().get(0));
        Assert.assertEquals("testValue", cmd.getOptionValue(arg.getName()));
    }

    @Test(expected = OptionException.class)
    public void testMaximumOne() {
        cmd.parse(arg.parse(new String[]{"argName", "testValue1", "testValue2"}));
        Assert.fail("expected OptionException");
    }

    @Test
    public void testConsumeRemaining() {
        cmd.parse(arg.parse(new String[]{"--consumeRemaining", "argName1", "argName2"}));
        Assert.assertEquals(arg.getName(), cmd.getOptionNames().get(1));
        Assert.assertEquals("argName2", cmd.getOptionValue(arg.getName()));
    }

    @Test
    public void testDefaultValue() {
        cmd.parse(arg.parse(new String[]{}));
        Assert.assertEquals("defaultValue", cmd.getOptionValue(arg.getName()));
    }
}