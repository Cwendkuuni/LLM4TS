import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.TestUtils;
import org.junit.Assert;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testGroupImpl() throws Exception {
        // Given
        final List<Option> options = new ArrayList<>();
        options.add(TestUtils.createOption("o1", "opt1", false));
        options.add(TestUtils.createOption("o2", "opt2", true));
        options.add(TestUtils.createOption("o3", "opt3", false));
        options.add(TestUtils.createOption("o4", "opt4", false));
        options.add(TestUtils.createArgument("a1", "arg1"));
        options.add(TestUtils.createArgument("a2", "arg2"));

        final Group group = new GroupImpl(options, "group", "description", 1, 3, false);

        // When
        final CommandLine commandLine = new CommandLine();
        group.process(commandLine, commandLine.parser().parse(group.getOptions().toArray(new Option[0])));

        // Then
        Assert.assertEquals("opt1", commandLine.getOptionValue("o1"));
        Assert.assertEquals("opt2", commandLine.getOptionValue("o2"));
        Assert.assertNull(commandLine.getOptionValue("o3"));
        Assert.assertNull(commandLine.getOptionValue("o4"));
        Assert.assertEquals("arg1", commandLine.getOptionValue("a1"));
        Assert.assertEquals("arg2", commandLine.getOptionValue("a2"));

        Assert.assertEquals("group", group.getPreferredName());
        Assert.assertEquals("description", group.getDescription());
        Assert.assertEquals(1, group.getMinimum());
        Assert.assertEquals(3, group.getMaximum());
    }
}