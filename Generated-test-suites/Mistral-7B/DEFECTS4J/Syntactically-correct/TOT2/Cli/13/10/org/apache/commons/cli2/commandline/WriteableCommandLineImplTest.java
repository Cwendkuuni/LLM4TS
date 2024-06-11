import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImplConstruction() {
        Option rootOption = new Option("optionName", "Option description");
        List arguments = new ArrayList();
        arguments.add("argument1");
        arguments.add("argument2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Check the root option is correctly set
        assertEquals(rootOption, commandLine.getOption(0));

        // Check the arguments are correctly set
        assertEquals(arguments, commandLine.getNormalised());
    }
}