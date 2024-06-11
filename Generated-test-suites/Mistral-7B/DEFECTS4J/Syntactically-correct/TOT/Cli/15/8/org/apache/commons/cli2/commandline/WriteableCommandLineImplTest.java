import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option option1 = new Option("o", "output", true, "Output Directory");
        Option option2 = new Option("i", "input", true, "Input File");
        Option option3 = new Option("h", "help", false, "Print help message.");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, null);

        // When
        cmd.addOption(option2);
        cmd.addOption(option3);
        cmd.addValue(option1, "/output");
        cmd.addValue(option2, "input.txt");
        cmd.addSwitch(option3, true);

        // Then
        CommandLine parsed = null;
        try {
            parsed = cmd.parse();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(parsed);

        // Checking the values
        Assert.assertEquals("/output", parsed.getOptionValue("output"));
        Assert.assertEquals("input.txt", parsed.getOptionValues("input").get(0));
        Assert.assertTrue(parsed.hasOption("help"));

        // Checking the switches
        Assert.assertTrue(parsed.hasOption("output"));

        // Edge case: empty values and no switches
        cmd = new WriteableCommandLineImpl(option1, null);
        cmd.addOption(option1);
        cmd.addValue(option1, "");

        parsed = null;
        try {
            parsed = cmd.parse();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(parsed);
        Assert.assertNull(parsed.getOptionValue(option1.getLongOpt()));

        // Error case: incorrect option value type
        cmd = new WriteableCommandLineImpl(option1, null);
        cmd.addOption(option1);
        cmd.addValue(option1, new Object());

        try {
            cmd.parse();
        } catch (ParseException e) {
            Assert.assertEquals("Option 'output' requires a value of type java.lang.String but received java.lang.Object", e.getMessage());
        }
    }
}