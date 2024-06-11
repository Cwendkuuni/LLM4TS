import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

public class WriteableCommandLineTest {

  @org.junit.Test
  public void testAddOption() {
    WriteableCommandLine commandLine = new CliCommandLine();
    Option option = new Option("o", "output", true, "Output file");
    commandLine.addOption(option);
    assertTrue(commandLine.getOptions().contains(option));
    assertFalse(commandLine.getOptions().contains(new Option("i", "input", true, "Input file")));
  }

  // ...
}