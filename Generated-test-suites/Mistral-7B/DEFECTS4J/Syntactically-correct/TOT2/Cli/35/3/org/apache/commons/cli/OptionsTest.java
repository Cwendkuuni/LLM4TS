import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 @Test
 public void testBasicUsage() {
 Options options = new Options();
 options.addOption("a", "alpha", false, "Specify alpha option");

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"-a"});
 Assert.assertTrue(cmd.hasOption("a"));
 } catch (ParseException e) {
 e.printStackTrace();
 FailureMessageFormat.printFailureMessage(System.out, e);
 }
 }

 @Test
 public void testShortOptionWithValue() {
 Options options = new Options();
 options.addOption("a", true, "Specify alpha option with value");

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"-a", "123"});
 Assert.assertEquals("123", cmd.getOptionValue("a"));
 } catch (ParseException e) {
 e.printStackTrace();
 FailureMessageFormat.printFailureMessage(System.out, e);
 }
 }

 @Test
 public void testLongOptionWithValue() {
 Options options = new Options();
 options.addOption("alpha", true, "Specify alpha option with value");

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"--alpha", "123"});
 Assert.assertEquals("123", cmd.getOptionValue("alpha"));
 } catch (ParseException e) {
 e.printStackTrace();
 FailureMessageFormat.printFailureMessage(System.out, e);
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() {
 Options options = new Options();
 options.addOption("a", true, "Specify alpha option with value");
 options.addOptionGroup(new OptionGroup("group", "Group", new Option[]{new Option("b", true, "Specify beta option")}));

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"-a"});
 } catch (ParseException e) {
 Assert.assertEquals("The following required options do not have values, are invalid, or inconsistent: [a]", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingLongOptionWithinGroup() {
 Options options = new Options();
 options.addOptionGroup(new OptionGroup("group", "Group", new Option[]{new Option("a", true, "Specify alpha option within group")}));

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"--group"});
 } catch (ParseException e) {
 Assert.assertEquals("Invalid option: 'group'. The option names must start with --", e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testMismatchedOptionGroup() {
 Options options = new Options();
 options.addOptionGroup(new OptionGroup("group1", "Group1", new Option[]{new Option("a", true, "Specify alpha option within group1")}));
 options.addOptionGroup(new OptionGroup("group2", "Group2", new Option[]{new Option("a", true, "Specify alpha option within group2")}));

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[]{"--group1", "--group2"});
 } catch (ParseException e) {
 Assert.assertEquals("The following conflicts were found: [a]", e.getMessage());
 }
 }
}