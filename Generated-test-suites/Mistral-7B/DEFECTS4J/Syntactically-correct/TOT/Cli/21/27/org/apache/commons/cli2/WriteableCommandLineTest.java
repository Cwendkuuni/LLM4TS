import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.BasicParser;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

 @Test
 public void testAddOptionAndValue() {
 WriteableCommandLine cmdLine = new BasicParser().parse(getTestOptions());
 Option option = new Option("i", "inputFile", true, "input file");
 cmdLine.addOption(option);
 cmdLine.addValue(option, "input.txt");
 List<String> values = cmdLine.getUndefaultedValues(option);
 assertEquals(Arrays.asList("input.txt"), values);
 }

 @Test
 public void testGetUndefaultedValues() {
 WriteableCommandLine cmdLine = new BasicParser().parse(getTestOptions());
 Option option = new Option("o", "outputFile", true, "output file");
 cmdLine.addOption(option);
 cmdLine.addValue(option, "output.txt");
 cmdLine.addValue(option, "output2.txt");
 List<String> values = cmdLine.getUndefaultedValues(option);
 assertEquals(Arrays.asList("output.txt", "output2.txt"), values);
 }

 @Test
 public void testAddSwitch() {
 WriteableCommandLine cmdLine = new BasicParser().parse(getTestOptions());
 Option option = new Option("d", "debug", false, "debug mode");
 cmdLine.addOption(option);
 try {
 cmdLine.addSwitch(option, true);
 fail("Should not be able to add switch value to an Option that doesn't have a value");
 } catch (IllegalStateException e) {
 // expected
 }
 option.setValues(Arrays.asList(new String[]{"debug1", "debug2"}));
 cmdLine.addSwitch(option, true);
 cmdLine.addSwitch(option, false);
 Boolean switchValue = cmdLine.getDefaultSwitch(option);
 assertFalse(switchValue);
 }

 @Test
 public void testSetDefaultValues() {
 WriteableCommandLine cmdLine = new BasicParser().parse(getTestOptions());
 Option option = new Option("f", "file", true, "file");
 List<String> defaultValues = Arrays.asList(new String[]{"default1.txt", "default2.txt"});
 cmdLine.setDefaultValues(option, defaultValues);
 List<String> values = cmdLine.getUndefaultedValues(option);
 assertTrue(values.isEmpty());
 List<String> defaultValuesList = cmdLine.getDefaultValues(option);
 assertEquals(defaultValues, defaultValuesList);
 }

 @Test
 public void testLooksLikeOption() {
 WriteableCommandLine cmdLine = new BasicParser().parse(getTestOptions());
 assertTrue(cmdLine.looksLikeOption("--inputFile"));
 assertTrue(cmdLine.looksLikeOption("-i"));
 assertFalse(cmdLine.looksLikeOption("--inputFile="));
 assertFalse(cmdLine.looksLikeOption("-"));
 }

 @Test
 public void testParseCommandLineWithOptionAndValue() {
 String[] args = {"--inputFile", "input.txt"};
 WriteableCommandLine cmdLine = new BasicParser().parse(args);
 Option option = cmdLine.getOption("inputFile");
 List<String> values = cmdLine.getUndefaultedValues(option);
 assertEquals(Arrays.asList("input.txt"), values);
 }

 @Test
 public void testParseCommandLineWithOptionSwitch() {
 String[] args = {"-d"};
 WriteableCommandLine cmdLine = new BasicParser().parse(args);
 Option option = cmdLine.getOption("debug");
 Boolean switchValue = cmdLine.getDefaultSwitch(option);
 assertTrue(switchValue);
 }

 @Test(expected = IllegalStateException.class)
 public void testParseCommandLineWithDuplicateOption() {
 String[] args = {"--inputFile", "--inputFile"};
 WriteableCommandLine cmdLine = new BasicParser().parse(args);
 fail("Should not be able to parse command line with a duplicate Option");
 }

 @Test
 public void testParseCommandLineWithMissingOption() {
 String[] args = {"--unknownOption"};
 WriteableCommandLine cmdLine = new BasicParser().parse(args);
 assertNull(cmdLine.getOption("unknownOption"));
 }

 private List<Option> getTestOptions() {
 List<Option> options = Arrays.asList(
 new Option("i", "inputFile", true, "input file"),
 new Option("o", "outputFile", true, "output file"),
 new Option("d", "debug", false, "debug mode"),
 new Option("f", "file", true, "file"),
 new Option("h", "help", false, "display help")
 );
 return options;
 }
}