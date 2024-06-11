import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.addOption(option);
        Assert.assertTrue(cmd.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.addOption(option);
        cmd.addValue(option, "test");
        List values = cmd.getOptionValues("option");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test", values.get(0));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine cmd = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.addOption(option);
        cmd.addValue(option, "test1");
        List values = cmd.getOptionValues("option");
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test1", values.get(0));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        ArrayList<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        cmd.setDefaultValues(option, defaultValues);
        List values = cmd.getOptionValues("option");
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
        Assert.assertEquals("defaultValue2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchIllegalStateException() {
        WriteableCommandLine cmd = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.addSwitch(option, true);
        cmd.addSwitch(option, true);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDefaultSwitchIllegalStateException() {
        WriteableCommandLine cmd = new Cmd();
        Option option = Option.builder("o").longOpt("option").build();
        cmd.setDefaultSwitch(option, true);
        cmd.setDefaultSwitch(option, false);
    }

    @Test
    public void testLooksLikeOptionFalse() {
        WriteableCommandLine cmd = new Cmd();
        Assert.assertFalse(cmd.looksLikeOption("test"));
    }

    @Test
    public void testLooksLikeOptionTrue() {
        WriteableCommandLine cmd = new Cmd();
        cmd.addOption(Option.builder("t").longOpt("test").build());
        Assert.assertTrue(cmd.looksLikeOption("test"));
    }

    static class Cmd implements WriteableCommandLine {
    }
}