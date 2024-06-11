import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.apache.commons.cli2.commandline.WriteableCommandLineImpl.WriteableCommandLineImpl.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl writeableCommandLine;
    private Option rootOption;
    private List arguments;

    public WriteableCommandLineImplTest(Option rootOption, List arguments) {
        this.rootOption = rootOption;
        this.arguments = arguments;
        this.writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Parameterized.Parameters(name = "{index}: WriteableCommandLineImpl({0})")
    public static Iterable<Object[]> data() {
        Option option1 = mock(Option.class);
        when(option1.getPreferredName()).thenReturn("option1");
        when(option1.getLongOptName()).thenReturn("long-option1");
        when(option1.getValueSeparator()).thenReturn(":");
        Option argument1 = mock(Argument.class);
        when(argument1.getName()).thenReturn("arg1");
        when(argument1.getLongName()).thenReturn("arg-long");

        Option option2 = mock(Option.class);
        when(option2.getPreferredName()).thenReturn("option2");
        when(option2.getLongOptName()).thenReturn("long-option2");

        Option option3 = mock(Option.class);
        when(option3.getPreferredName()).thenReturn("option3");
        when(option3.getLongOptName()).thenReturn("long-option3");
        Argument argument2 = mock(Argument.class);
        when(argument2.getName()).thenReturn("arg2");
        when(argument2.getLongName()).thenReturn("arg-long2");

        return Arrays.asList(new Object[][]{
                {new Option[]{option1}, Arrays.asList("arg1")},
                {new Option[]{option1, option2}, Arrays.asList("arg1", "arg2")},
                {new Option[]{option1, option2, option3}, Arrays.asList("arg1", "arg2", "")},
                {new Option[]{option1, option2, option3}, Arrays.asList("arg1", "arg2:value", "")},
                {new Option[]{option1, option2, option3}, Arrays.asList("arg1", "arg2:", new String[]{"value"})}
        });
    }

    @Test
    public void testCreateWriteableCommandLineImplWithOneArgument() {
        when(rootOption.getPrefixes()).thenReturn(Collections.emptyList());

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals("arg1", writeableCommandLine.toString());
    }

    @Test
    public void testCreateWriteableCommandLineImplWithMultipleArguments() {
        List expectedArguments = Arrays.asList("arg1", "arg2");

        when(rootOption.getPrefixes()).thenReturn(Collections.emptyList());

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals("arg1 arg2", writeableCommandLine.toString());
        assertEquals(expectedArguments, writeableCommandLine.getNormalised());
    }

    @Test
    public void testCreateWriteableCommandLineImplWithOneOption() {
        when(rootOption.getPrefixes()).thenReturn(Collections.singletonList("-o"));

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals("-o option1", writeableCommandLine.toString());
    }

    @Test
    public void testCreateWriteableCommandLineImplWithMultipleOptions() {
        List expectedArguments = Arrays.asList("arg1", "arg2");

        when(rootOption.getPrefixes()).thenReturn(Arrays.asList("-o", "-a"));

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals("-o option1 -a option2", writeableCommandLine.toString());
        assertEquals(expectedArguments, writeableCommandLine.getNormalised());
    }

    @Test
    public void testCreateWriteableCommandLineImplWithOptionAndArgument() {
        List expectedArguments = Arrays.asList("arg1", "value");

        when(rootOption.getPrefixes()).thenReturn(Collections.singletonList("-o"));
        when(option1.getValueSeparator()).thenReturn("/");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = writeableCommandLine.getOption("option1");
        assertNotNull(option);

        assertEquals("option1", option.getPreferredName());
        assertEquals("long-option1", option.getLongOptName());
        assertEquals(Collections.singletonList("value"), writeableCommandLine.getValues(option, null));
        assertEquals(expectedArguments, writeableCommandLine.getNormalised());
    }

    @Test
    public void testCreateWriteableCommandLineImplWithMultipleOptionsAndArguments() {
        List expectedArguments = Arrays.asList("arg1", "value1", "arg2:value2");

        when(rootOption.getPrefixes()).thenReturn(Arrays.asList("-o", "-a"));
        when(option1.getValueSeparator()).thenReturn("/");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option1 = writeableCommandLine.getOption("option1");
        Argument argument1 = writeableCommandLine.getOption("arg1");

        assertEquals("option1", option1.getPreferredName());
        assertEquals("long-option1", option1.getLongOptName());
        assertEquals(Collections.singletonList("value1"), writeableCommandLine.getValues(option1, null));

        assertEquals("arg1", argument1.getName());
        assertEquals("arg-long", argument1.getLongName());
        assertEquals(Collections.emptyList(), writeableCommandLine.getValues(argument1, null));

        assertEquals(expectedArguments, writeableCommandLine.getNormalised());
    }

    @Test
    public void testCreateWriteableCommandLineImplWithOptionAndSwitch() {
        when(rootOption.getPrefixes()).thenReturn(Collections.singletonList("-o"));

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option = writeableCommandLine.getOption("option1");
        assertNotNull(option);

        assertEquals("option1", option.getPreferredName());
        assertEquals("long-option1", option.getLongOptName());
        assertFalse(writeableCommandLine.hasOption(option));
        assertEquals(Boolean.FALSE, writeableCommandLine.getSwitch(option, null));

        writeableCommandLine.addSwitch(option, true);

        assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(option, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWriteableCommandLineImplWithDuplicateOption() {
        when(rootOption.getPrefixes()).thenReturn(Collections.emptyList());

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option1 = mock(Option.class);
        Option option2 = mock(Option.class);
        when(option1.getPreferredName()).thenReturn("option1");
        when(option2.getPreferredName()).thenReturn("option1");

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
    }

    @Test
    public void testGetOption() {
        Option option = mock(Option.class);
        when(option.getPreferredName()).thenReturn("option1");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        writeableCommandLine.addOption(option);

        Option result = writeableCommandLine.getOption("option1");

        assertEquals("option1", result.getPreferredName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionWithNullOptionName() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        writeableCommandLine.getOption(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionWithNonExistentOptionName() {
        Option option = mock(Option.class);
        when(option.getPreferredName()).thenReturn("non-existent-option");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        writeableCommandLine.addOption(option);

        writeableCommandLine.getOption("non-existent-option");
    }

    @Test
    public void testGetProperties() {
        Properties properties = new Properties();
        properties.put("property1", "value1");
        properties.put("property2", "value2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        writeableCommandLine.addProperty("property1", "value1");
        writeableCommandLine.addProperty("property2", "value2");

        assertEquals(properties, writeableCommandLine.getProperties());
    }

    @Test
    public void testToString() {
        when(rootOption.getPrefixes()).thenReturn(Arrays.asList("-o", "-a"));

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals("-o option1 -a option2 arg1 arg2", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = mock(Option.class);
        Option option2 = mock(Option.class);
        Argument argument1 = mock(Argument.class);

        when(option1.getPreferredName()).thenReturn("option1");
        when(option1.getLongOptName()).thenReturn("long-option1");
        when(option2.getPreferredName()).thenReturn("option2");
        when(option2.getLongOptName()).thenReturn("long-option2");
        when(argument1.getName()).thenReturn("arg1");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addArgument(argument1);

        List result = writeableCommandLine.getOptions();

        assertEquals(2, result.size());
        assertTrue(result.contains(option1));
        assertTrue(result.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = mock(Option.class);
        Option option2 = mock(Option.class);

        when(option1.getPreferredName()).thenReturn("option1");
        when(option1.getLongOptName()).thenReturn("long-option1");
        when(option2.getPreferredName()).thenReturn("option2");
        when(option2.getLongOptName()).thenReturn("long-option2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        Set result = writeableCommandLine.getOptionTriggers();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("option1", "option2")));
    }
}