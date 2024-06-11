import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.BasicParser;
import org.junit.Assert;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WriteableCommandLineTest {

 private WriteableCommandLine commandLine;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 commandLine = new DefaultWriteableCommandLine();
 }

 @Test
 public void testAddOption() {
 // add option
 Option option = Option.builder("o").longOpt("option")
 .required(false)
 .desc("an option")
 .build();
 commandLine.addOption(option);

 // test option is added
 Assert.assertEquals(1, commandLine.getOptions().size());
 Assert.assertEquals(option, commandLine.getOptions().get(0));
 }

 @Test
 public void testAddValue() {
 // add option
 Option option = Option.builder("o").longOpt("option")
 .required(true)
 .desc("an option")
 .build();
 commandLine.addOption(option);

 // add value
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");

 // test values are added
 List<String> values = commandLine.getUndefaultedValues(option);
 Assert.assertEquals(Arrays.asList("value1", "value2"), values);
 }

 @Test
 public void testGetUndefaultedValues() {
 // add option with values
 Option option = Option.builder("o").longOpt("option")
 .required(false)
 .desc("an option")
 .build();
 commandLine.addOption(option);
 commandLine.addValues(option, Arrays.asList("value1", "value2"));

 // test get values
 List<String> values = commandLine.getUndefaultedValues(option);
 Assert.assertEquals(Arrays.asList("value1", "value2"), values);
 }

 @Test
 public void testSetDefaultValues() {
 // add option
 Option option = Option.builder("o").longOpt("option")
 .required(false)
 .desc("an option")
 .build();
 commandLine.addOption(option);

 // set defaults
 List<String> defaults = Arrays.asList("default1", "default2");
 commandLine.setDefaultValues(option, defaults);

 // test defaults
 List<String> values = commandLine.getUndefaultedValues(option);
 Assert.assertEquals(Collections.emptyList(), values);
 Assert.assertEquals(defaults, commandLine.getOptionValues(option));
 }

 @Test
 public void testAddSwitch() {
 // add option
 Option option = Option.builder("s").longOpt("switch")
 .required(false)
 .desc("a switch")
 .build();
 commandLine.addOption(option);

 // add switch value
 commandLine.addSwitch(option, true);

 // test switch is set
 Assert.assertTrue(option.getValue().isPresent());
 Assert.assertEquals(true, option.getValue().get());
 }

 @Test
 public void testSetDefaultSwitch() {
 // add option
 Option option = Option.builder("s").longOpt("switch")
 .required(false)
 .desc("a switch")
 .build();
 commandLine.addOption(option);

 // set default switch
 commandLine.setDefaultSwitch(option, true);

 // test switch is default
 Assert.assertTrue(option.getValue().isPresent());
 Assert.assertEquals(true, option.getValue().get());
 }

 @Test
 public void testAddProperty() {
 // add option
 Option option = Option.builder("p").longOpt("property")
 .required(false)
 .desc("a property")
 .build();
 commandLine.addOption(option);

 // add property
 commandLine.addProperty(option, "name", "value");

 // test property is set
 Assert.assertEquals("value", commandLine.getOptionProperty(option, "name"));
 }

 @Test
 public void testAddDefaultProperty() {
 // add property
 commandLine.addProperty("name", "defaultValue");

 // test property is set
 Assert.assertEquals("defaultValue", commandLine.getOptionProperty("name"));
 }

 @Test
 public void testLooksLikeOption() {
 // test argument looks like option "foo"
 String argument = "foo";
 Assert.assertTrue(commandLine.looksLikeOption(argument));

 // test argument does not look like option "bar"
 String argument2 = "bar";
 Assert.assertFalse(commandLine.looksLikeOption(argument2));
 }

 @Test
 public void testSetCurrentOption() {
 // add options
 Option option1 = Option.builder("o1").longOpt("option1")
 .required(false)
 .desc("option1")
 .build();
 commandLine.addOption(option1);
 Option option2 = Option.builder("o2").longOpt("option2")
 .required(false)
.desc("option2")
 .build();
 commandLine.addOption(option2);

 // set current option to option 1
 commandLine.setCurrentOption(option1);

 // test current option is option1
 Assert.assertEquals(option1, commandLine.getCurrentOption());
 }

 @Test
 public void testParseCommandLineWithValidOptions() {
 // add options
 Option option1 = Option.builder("o1").longOpt("option1")
 .required(false)
 .desc("option1")
 .build();
 commandLine.addOption(option1);
 Option option2 = Option.builder("o2").longOpt("option2")
 .required(false)
 .desc("option2")
 .build();
 commandLine.addOption(option2);

 // parse command line with valid options
 String[] args = new String[]{"myProgram", "--option1", "value1", "--option2"};
 CommandLineParser parser = new BasicParser();
 parser.parse(option1, option2, args, commandLine);

 // test options are parsed correctly
 Assert.assertEquals(option1, commandLine.getCurrentOption());
 Assert.assertEquals("value1", commandLine.getUndefaultedValues(option1).get(0));
 Assert.assertEquals(option2, commandLine.getCurrentOption());
 Assert.assertNull(commandLine.getUndefaultedValues(option1).get(1));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchTwice() {
 // add option
 Option option = Option.builder("s").longOpt("switch")
 .required(false)
 .desc("a switch")
 .build();
 commandLine.addOption(option);

 // add switch value
 commandLine.addSwitch(option, true);

 // try to add switch value again
 commandLine.addSwitch(option, true);
 }
}