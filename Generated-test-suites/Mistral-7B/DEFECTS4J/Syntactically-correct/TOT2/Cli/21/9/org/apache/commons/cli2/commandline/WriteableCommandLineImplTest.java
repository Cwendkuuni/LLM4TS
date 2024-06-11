import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "anOption"), null);
        writeableCommandLine.addOption(new Option("optionB", "anotherOption"));

        Option optionB = writeableCommandLine.getOption("optionB");
        Assert.assertNotNull(optionB);
        Assert.assertEquals("anotherOption", optionB.getLongOpt());
        Assert.assertEquals("anotherOption", optionB.getOpt());
        Assert.assertEquals(Collections.emptyList(), writeableCommandLine.getValues(optionB, null));
        Assert.assertFalse(writeableCommandLine.hasOption(new Option("nonexistentOption", "nonexistentOption")));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "anOption"), null);
        writeableCommandLine.addOption(new Option("optionB", "anOption", true, "value"));
        writeableCommandLine.addValue(writeableCommandLine.getOption("optionB"), "testValue");

        Assert.assertTrue(writeableCommandLine.hasOption(writeableCommandLine.getOption("optionB")));
        Assert.assertEquals(1, writeableCommandLine.getValues(writeableCommandLine.getOption("optionB"), null).size());
        Assert.assertEquals("testValue", writeableCommandLine.getValues(writeableCommandLine.getOption("optionB"), null).get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "anOption"), null);
        writeableCommandLine.addOption(new Option("optionB", "anOption", false, null));
        writeableCommandLine.addSwitch(writeableCommandLine.getOption("optionB"), true);

        Assert.assertTrue(writeableCommandLine.hasOption(writeableCommandLine.getOption("optionB")));
        Assert.assertTrue(writeableCommandLine.getSwitch(writeableCommandLine.getOption("optionB"), false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "anOption"), null);
        writeableCommandLine.addOption(new Option("optionB", "anOption", false, null));
        writeableCommandLine.addSwitch(writeableCommandLine.getOption("optionB"), true);
        writeableCommandLine.addSwitch(writeableCommandLine.getOption("optionB"), true);
    }

    @Test
    public void testPropertyOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "anOption"), null);
        writeableCommandLine.addOption(new PropertyOption("property", "PropertyValue", "defaultValue", "A property."));
        writeableCommandLine.addProperty(writeableCommandLine.getOption("property"), "testPropertyValue");

        Map<String, String> properties = writeableCommandLine.getProperties(writeableCommandLine.getOption("property"));
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("testPropertyValue", properties.get("property"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "anOption"), null);
        List arguments = new ArrayList();
        arguments.add("argument1");
        arguments.add("argument2");
        writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "anOption"), arguments);

        String commandLineString = writeableCommandLine.toString();
        Assert.assertEquals("argument1 argument2", commandLineString);
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "anOption"), null);
        writeableCommandLine.addOption(new Option("optionB", "anotherOption"));

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("optionA", "optionB")));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "anOption"), null);
        writeableCommandLine.addOption(new Option("optionB", "anOption", true, "value"));
        writeableCommandLine.addValue(writeableCommandLine.getOption("optionB"), "testValue1");
        writeableCommandLine.addValue(writeableCommandLine.getOption("optionB"), "testValue2");

        List values = writeableCommandLine.getValues(writeableCommandLine.getOption("optionB"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("testValue1", values.get(0));
        Assert.assertEquals("testValue2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "anOption"), null);
        writeableCommandLine.addOption(new Option("optionB", "anOption", true, "value"));
        writeableCommandLine.addValue(writeableCommandLine.getOption("optionB"), "testValue1");
        writeableCommandLine.addValue(writeableCommandLine.getOption("optionB"), "testValue2");

        List undefaultedValues = writeableCommandLine.getUndefaultedValues(writeableCommandLine.getOption("optionB"));
        Assert.assertEquals(1, undefaultedValues.size());
        Assert.assertEquals("testValue1", undefaultedValues.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "anOption"), null);
        writeableCommandLine.addOption(new Option("optionB", "anOption", false, null));
        writeableCommandLine.addSwitch(writeableCommandLine.getOption("optionB"), true);

        Boolean switchValue = writeableCommandLine.getSwitch(writeableCommandLine.getOption("optionB"), false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "anOption"), null);
        writeableCommandLine.addOption(new Option("optionB", "anOption", true, "value"));

        List defaultValues = (List) writeableCommandLine.getDefaultValues(writeableCommandLine.getOption("optionB"));
        Assert.assertNull(defaultValues);

        writeableCommandLine = new WriteableCommandLineImpl(new Option("optionA", "anOption"), null);
        writeableCommandLine.addOption(new Option("optionB", "anOption", true, "value1", "value2"));
        defaultValues = (List) writeableCommandLine.getDefaultValues(writeableCommandLine.getOption("optionB"));
        Assert.assertEquals(2, defaultValues.size());
        Assert.assertEquals("value1", defaultValues.get(0));
        Assert.assertEquals("value2", defaultValues.get(1));
    }
}