import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Set;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilder() {
        PatternBuilder pb = new PatternBuilder();

        // Test creating an Option with no arguments
        pb.withPattern("b");
        Option option1 = pb.create();
        Assert.assertEquals("b", option1.getShortName());
        Assert.assertNull(option1.getArgument());
        Assert.assertFalse(option1.isRequired());

        // Test creating an Option with a required argument
        pb.reset();
        pb.withPattern("s:");
        Option option2 = pb.create();
        Assert.assertEquals("s", option2.getShortName());
        Assert.assertNotNull(option2.getArgument());
        Assert.assertTrue(option2.isRequired());

        // Test creating an Option with an optional argument
        pb.reset();
        pb.withPattern("s:");
        Option option3 = pb.create();
        Assert.assertEquals("s", option3.getShortName());
        Assert.assertNotNull(option3.getArgument());
        Assert.assertFalse(option3.isRequired());

        // Test creating an Option with a validator
        pb.reset();
        pb.withPattern("i:");
        Option option4 = pb.create();
        Assert.assertEquals("i", option4.getShortName());
        Assert.assertNotNull(option4.getArgument());
        Assert.assertFalse(option4.isRequired());
        OptionParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(option4, new String[]{"-i", "1"});
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(cmd);

        // Test creating an Option with a validator and argument
        pb.reset();
        pb.withPattern("i:d");
        Option option5 = pb.create();
        Assert.assertEquals("i", option5.getShortName());
        Assert.assertNotNull(option5.getArgument());
        Assert.assertFalse(option5.isRequired());
        LocalDate date = LocalDate.of(2022, 12, 31);
        CommandLine cmd2 = null;
        try {
            cmd2 = parser.parse(option5, new String[]{"-i", "2022-12-31"});
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(cmd2);

        // Test creating an Option with multiple validators
        pb.reset();
        pb.withPattern("g:f");
        Option option6 = pb.create();
        Assert.assertEquals("g", option6.getShortName());
        Assert.assertNotNull(option6.getArgument());
        Assert.assertFalse(option6.isRequired());
        OptionParser parser2 = new DefaultParser();
        CommandLine cmd3 = null;
        try {
            cmd3 = parser2.parse(option6, new String[]{"-g", "/path/to/file.txt"});
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(cmd3);

        // Test creating an Option with multiple arguments
        pb.reset();
        pb.withPattern("a:*");
        Option option7 = pb.create();
        Assert.assertEquals("a", option7.getShortName());
        Assert.assertNotNull(option7.getArgument());
        Assert.assertFalse(option7.isRequired());
        CommandLine cmd4 = null;
        try {
            cmd4 = parser.parse(option7, new String[]{"-a", "arg1", "arg2"});
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(cmd4);
        Set<String> args = cmd4.getArgs();
        Assert.assertEquals(2, args.size());
        Assert.assertEquals("arg1", args.iterator().next());
        Assert.assertEquals("arg2", args.iterator().next());

        // Test creating an Option group
        pb.reset();
        OptionGroup group = new OptionGroup();
        pb.withPattern("v");
        Option option8 = pb.create();
        group.addOption(option8);
        pb.withPattern("d:");
        Option option9 = pb.create();
        group.addOption(option9);
        Option option10 = pb.create();
        group.addOption(option10);
        Option option11 = pb.create();
        group.addOption(option11);
        Option option12 = pb.create();
        group.addOption(option12);
        OptionParser parser3 = new DefaultParser();
        CommandLine cmd5 = null;
        try {
            cmd5 = parser3.parse(group, new String[]{"-v", "--date", "2022-12-31", "--date2", "2023-01-01"});
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(cmd5);
        Assert.assertTrue(option1.getValues().isEmpty());
        Assert.assertEquals(2, option9.getValues().size());
        Assert.assertEquals("2022-12-31", option9.getValues().iterator().next());
        Assert.assertEquals("2023-01-01", option9.getValues().iterator().next());
        Assert.assertFalse(option10.getValues().isEmpty());
        Assert.assertFalse(option11.getValues().isEmpty());
        Assert.assertFalse(option12.getValues().isEmpty());
    }
}