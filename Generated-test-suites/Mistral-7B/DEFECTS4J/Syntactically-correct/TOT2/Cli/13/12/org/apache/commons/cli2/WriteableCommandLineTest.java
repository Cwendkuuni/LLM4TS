import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        assert(commandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        commandLine.addOption(null);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("v").longOpt("value").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<Object> values = commandLine.getValuesForOption(option);
        assert(values.size() == 1);
        assert(values.get(0).equals("value1"));
        commandLine.addValue(option, "value2");
        values = commandLine.getValuesForOption(option);
        assert(values.size() == 2);
        assert(values.get(0).equals("value1"));
        assert(values.get(1).equals("value2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("v").longOpt("value").build();
        commandLine.addOption(option);
        commandLine.addValue(option, null);
    }

    @Test
    public void testGetEmptyValuesForOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("e").longOpt("empty").build();
        commandLine.addOption(option);
        List<Object> values = commandLine.getValuesForOption(option);
        assert(values.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSameSwitchValue() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = Option.builder("d").longOpt("default").build();
        commandLine.setDefaultValues(option, null);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLine();
        commandLine.addProperty("property1", "value1");
        assert(commandLine.getProperties().containsKey("property1"));
        assert(commandLine.getProperties().get("property1").equals("value1"));
        commandLine.addProperty("property1", "value2");
        assert(commandLine.getProperties().get("property1").equals("value2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSamePropertyName() {
        WriteableCommandLine commandLine = new CliCommandLine();
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property1", "value2");
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        assert(commandLine.looksLikeOption("--option"));
        assert(!commandLine.looksLikeOption("--value"));
        assert(commandLine.looksLikeOption("-o"));
        assert(commandLine.looksLikeOption("-option"));
        assert(!commandLine.looksLikeOption("-v"));
    }

}