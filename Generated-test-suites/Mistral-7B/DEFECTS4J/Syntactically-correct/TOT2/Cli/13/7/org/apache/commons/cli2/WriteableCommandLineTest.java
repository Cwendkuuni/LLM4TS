// AddOptionTest.java
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Test;

public class AddOptionTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new CliCommandLine(); // Assuming CliCommandLine is an implementation of WriteableCommandLine
        Option option = Option.builder("o").longOpt("option").build();
        cmd.addOption(option);
        assert(cmd.getOptions().contains(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        WriteableCommandLine cmd = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.addOption(option);
        cmd.addOption(option);
    }
}