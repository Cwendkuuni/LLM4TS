import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() throws ParseException {
        // Given
        Option option = Option.builder("o")
                .longOpt("option")
                .hasArg(true)
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option", "value"));

        // When adding value to option
        writeableCommandLine.addValue(option, "testValue");

        // Then value should be added to the list of values
        List values = writeableCommandLine.getValues(option, Collections.emptyList());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("testValue", values.get(0));

        // Given
        Option switchOption = Option.builder("s")
                .longOpt("switch")
                .hasArg(false)
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLineWithSwitch = new WriteableCommandLineImpl(switchOption, Arrays.asList());

        // When setting switch
        writeableCommandLineWithSwitch.addSwitch(switchOption, true);

        // Then switch should be set to true
        Boolean switchValue = writeableCommandLineWithSwitch.getSwitch(switchOption, false);
        Assert.assertEquals(true, switchValue);

        // Given
        Option propertyOption = Option.builder("p")
                .longOpt("property")
                .hasArg(false)
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLineWithProperty = new WriteableCommandLineImpl(propertyOption, Arrays.asList());

        // When setting property
        writeableCommandLineWithProperty.addProperty("testProperty", "testValue");

        // Then property should be set
        Properties properties = writeableCommandLineWithProperty.getProperties();
        Assert.assertEquals("testValue", properties.getProperty("testProperty"));

        // Given
        Option groupOption = Option.builder("g")
                .longOpt("group")
                .hasArg(false)
                .required(false)
                .build();

        WriteableCommandLineImpl writeableCommandLineWithGroup = new WriteableCommandLineImpl(groupOption, Arrays.asList());

        // When adding option to the command line
        writeableCommandLineWithGroup.addOption(option);

        // Then option should be added to the list of options
        List options = writeableCommandLineWithGroup.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));

        // Given
        Option requiredOption = Option.builder("r")
                .longOpt("requiredOption")
                .hasArg(false)
                .required(true)
                .build();

        WriteableCommandLineImpl writeableCommandLineWithRequiredOption = new WriteableCommandLineImpl(requiredOption, Arrays.asList());

        // When trying to create command line without required option
        try {
            writeableCommandLineWithRequiredOption.parse();
            Assert.fail();
        } catch (ParseException e) {
            // Expected
        }

        // Given
        Option requiredOptionWithValue = Option.builder("r")
                .longOpt("requiredOption")
                .hasArg(true)
                .required(true)
                .build();

        WriteableCommandLineImpl writeableCommandLineWithRequiredOptionValue = new WriteableCommandLineImpl(requiredOptionWithValue, Arrays.asList("--requiredOption", "testValue"));

        // When parsing command line with required option
        CommandLine parsedCommandLine = writeableCommandLineWithRequiredOptionValue.parse();

        // Then command line should be parsed successfully
        Assert.assertNotNull(parsedCommandLine);
    }
}