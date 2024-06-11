import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilderWithSingleCharacterOption() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option singleCharacterOption = optionBuilder.withLongOpt("optionA")
 .hasArg()
 .withArgName("argumentA")
 .create('A');

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(new String[]{"-A", "argumentValue"});

 assertEquals("argumentValue", cmd.getOptionValue("argumentA"));
 assertEquals("optionA", cmd.getOptionName("A"));
 assertTrue(cmd.hasOption("optionA"));
 }

 @Test
 public void testOptionBuilderWithLongOption() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option longOption = optionBuilder.withLongOpt("longOptionA")
 .hasArg()
 .withArgName("argumentA")
 .create('-', "longOptionA");

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(new String[]{"--longOptionA", "argumentValue"});

 assertEquals("argumentValue", cmd.getOptionValue("argumentA"));
 assertEquals("longOptionA", cmd.getOptionName("longOptionA"));
 assertTrue(cmd.hasOption("longOptionA"));
 }

 @Test
 public void testOptionBuilderWithNoArgumentOption() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option noArgumentOption = optionBuilder.withLongOpt("noArgumentOption")
 .create('N');

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(new String[]{"-N"});

 assertTrue(cmd.hasOption("noArgumentOption"));
 assertNull(cmd.getOptionValue("noArgumentOption"));
 }

 @Test
 public void testOptionBuilderWithOptionalArgument() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option optionalArgumentOption = optionBuilder.withLongOpt("optionalArgumentOption")
 .hasArg(true)
 .withArgName("argumentA")
 .create('O');

 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(new String[]{"-O", "argumentValue"});

 assertEquals("argumentValue", cmd.getOptionValue("argumentA"));
 assertTrue(cmd.hasOption("optionalArgumentOption"));

 CommandLine cmd2 = parser.parse(new String[]{"-O"});
 assertNull(cmd2.getOptionValue("argumentA"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderWithMissingLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create('X');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderWithInvalidNumberOfArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("invalidOption")
 .hasArgs(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderWithInvalidCharOption() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create((char) 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionBuilderWithMissingDescription() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("missingDescription")
 .create('M');
 }
}