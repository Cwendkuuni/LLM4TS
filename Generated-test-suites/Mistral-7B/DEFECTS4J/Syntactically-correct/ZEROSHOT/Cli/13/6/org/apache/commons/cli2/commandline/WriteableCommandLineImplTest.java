import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;
 private Option option;

 @Before
 public void setUp() {
 option = new Option("o", "output", true, "output file path");
 }

 @Test
 public void testAddOption() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addOption(option);
 assertEquals(1, commandLine.getOptions().size());
 }

 @Test
 public void testAddValue() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addOption(option);
 commandLine.addValue(option, "test.txt");
 List values = commandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("test.txt", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test(expected = IllegalStateException.class)
 public void testSwitchAlreadySet() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 commandLine.addSwitch(option, false);
 }

 @Test
 public void testHasOption() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addOption(option);
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testGetOption() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addOption(option);
 assertEquals(option, commandLine.getOption("o"));
 }

 @Test
 public void testGetValues() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addOption(option);
 commandLine.addValue(option, "test.txt");
 List values = commandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("test.txt", values.get(0));

 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addOption(option);
 commandLine.setDefaultValues(option, Collections.singletonList("default.txt"));
 values = commandLine.getValues(option, null);
 assertEquals(1, values.size());
 assertEquals("default.txt", values.get(0));
 }

 @Test
 public void testGetSwitch() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addOption(option);
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));

 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addOption(option);
 commandLine.setDefaultSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testAddProperty() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addOption(option);
 commandLine.addProperty("outputFile", "test.txt");
 assertEquals("test.txt", commandLine.getProperty("outputFile", ""));
 }

 @Test
 public void testGetProperty() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addOption(option);
 commandLine.addProperty("outputFile", "test.txt");
 assertEquals("test.txt", commandLine.getProperty("outputFile", ""));
 assertEquals(Collections.emptySet(), commandLine.getProperties());
 }

 @Test
 public void testToString() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
 commandLine.addOption(option);
 commandLine.addValue(option, "test.txt");
 assertEquals("test.txt", commandLine.toString());
 }

 @Test
 public void testGetNormalised() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());commandLine.addOption(option); commandLine.addValue(option, "test.txt"); List normalised = commandLine.getNormalised(); assertEquals(1, normalised.size()); assertEquals("test.txt", normalised.get(0)); }

 @Test
 public void testSetDefaultValues() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList()); commandLine.addOption(option); List defaultValues = new ArrayList(); defaultValues.add("default1.txt"); commandLine.setDefaultValues(option, defaultValues); List values = commandLine.getValues(option, null); assertEquals(1, values.size()); assertEquals("default1.txt", values.get(0)); }

 @Test
 public void testSetDefaultSwitch() {
 commandLine = new WriteableCommandLineImpl(option, Collections.emptyList()); commandLine.addOption(option); commandLine.setDefaultSwitch(option, true); assertTrue(commandLine.getSwitch(option, false)); }
}