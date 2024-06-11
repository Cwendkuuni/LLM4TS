import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testBasicUsage() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, Collections.emptyList());

        cli.addValue(option, "test-output.txt");

        CommandLine parsed = new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-o", "test-output.txt"});
        Assert.assertEquals(cli, parsed);
    }

    @Test
    public void testMandatoryArgument() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, Collections.emptyList());

        try {
            new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-o"});
            Assert.fail("Parsing with mandatory argument missing should fail");
        } catch (ParseException e) {
            // expected
        }

        cli.addValue(option, "test-output.txt");

        CommandLine parsed = new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-o", "test-output.txt"});
        Assert.assertEquals(cli, parsed);
    }

    @Test
    public void testOptionalArgument() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, Collections.emptyList());

        CommandLine parsed1 = new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-o"});
        Assert.assertEquals(cli, parsed1);
        Assert.assertEquals(Collections.emptyList(), parsed1.getValues(option, null));

        cli.addValue(option, "test-output.txt");

        CommandLine parsed2 = new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-o", "test-output.txt"});
        Assert.assertEquals(cli, parsed2);
        Assert.assertEquals(Collections.singletonList("test-output.txt"), parsed2.getValues(option, null));

        cli.addValue(option, "another-output.txt");

        CommandLine parsed3 = new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-o", "test-output.txt", "another-output.txt"});
        Assert.assertEquals(cli, parsed3);
        Assert.assertEquals(Collections.singletonList("test-output.txt"), parsed3.getValues(option, null));
        Assert.assertEquals(Collections.singletonList("another-output.txt"), parsed3.getValues(option, Collections.singletonList("another-output.txt")));
    }

    @Test
    public void testSwitch() {
        Option option = new Option("d", "debug", false, "Debug mode");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, Collections.emptyList());

        cli.addSwitch(option, true);

        CommandLine parsed1 = new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-d"});
        Assert.assertEquals(cli, parsed1);
        Assert.assertTrue(parsed1.getSwitch(option, false));

        cli.addSwitch(option, false);

        CommandLine parsed2 = new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-d"});
        Assert.assertEquals(cli, parsed2);
        Assert.assertFalse(parsed2.getSwitch(option, false));
    }

    @Test
    public void testProperty() {
        Option propertyOption = new Option("p", "property", true, "Property name");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(propertyOption, Collections.emptyList());

        cli.addProperty(propertyOption, "property.name", "value");

        CommandLine parsed = new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-p", "property.name"});
        Assert.assertEquals(cli, parsed);
        Assert.assertEquals("value", parsed.getProperty("property.name"));
    }

    @Test
    public void testMultipleProperties() {
        Option propertyOption = new Option("p", "property", true, "Property name");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(propertyOption, Collections.emptyList());

        cli.addProperty(propertyOption, "property.name1", "value1");
        cli.addProperty(propertyOption, "property.name2", "value2");

        CommandLine parsed = new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-p", "property.name1", "-p", "property.name2"});
        Assert.assertEquals(cli, parsed);
        Assert.assertEquals("value1", parsed.getProperty("property.name1"));
        Assert.assertEquals("value2", parsed.getProperty("property.name2"));
    }

    @Test
    public void testMandatoryBooleanSwitch() {
        Option option = new Option("d", "debug", true, "Debug mode");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, Collections.emptyList());

        try {
            new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-d"});
            Assert.fail("Parsing with mandatory boolean switch missing should fail");
        } catch (ParseException e) {
            // expected
        }

        cli.addSwitch(option, true);

        CommandLine parsed = new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-d"});
        Assert.assertEquals(cli, parsed);
        Assert.assertTrue(parsed.getSwitch(option, false));
    }

    @Test
    public void testParseOptionsWithInvalidTrigger() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl cli = new WriteableCommandLineImpl(option, Collections.emptyList());

        try {
            new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-x"});
            Assert.fail("Parsing with invalid trigger should fail");
        } catch (ParseException e) {
            // expected
        }

        cli.addValue(option, "test-output.txt");

        try {
            new DefaultParser().parse(option.getCommandLineSpec(), new String[]{"-x"});
            Assert.fail("Parsing with invalid trigger after adding values should fail");
        } catch (ParseException e) {
            // expected
        }
    }
}