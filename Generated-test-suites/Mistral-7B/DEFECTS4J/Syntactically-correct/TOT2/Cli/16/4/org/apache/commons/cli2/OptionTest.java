import org.apache.commons.cli2.*;
import org.junit.Assert;
import org.junit.Test;

public class OptionTest {
    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new BasicParseResult();
        Option option = new MockOption(); // replace with the actual Option implementation
        ListIterator<String> args = Arrays.asList("option", "value1", "value2").listIterator();

        option.process(commandLine, args);

        Assert.assertEquals("value1", commandLine.getOptionValues("option").get(0));
        Assert.assertEquals("value2", commandLine.getOptionValues("option").get(1));

        // Edge case: empty argument list
        args = Arrays.asList().listIterator();
        try {
            option.process(commandLine, args);
            Assert.fail();
        } catch (OptionException e) {
            // Expected exception
        }
    }
}