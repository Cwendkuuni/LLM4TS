import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

 @Before
 public void setUp() {
 Options options = new Options();
 options.addOption("a", true, "option A");
 options.addOption("b", 1, "option B with an integer value");
 options.addOption("c", 3, "option C with a short value and an integer value");
 CommandLine commandLine = new CommandLine();
 }

 @Test
 public void testHasOption() {
 assertTrue(commandLine.hasOption("a"));
 assertTrue(commandLine.hasOption("b"));
 assertTrue(commandLine.hasOption("c"));
 assertFalse(commandLine.hasOption("d"));
 }

 @Test
 public void testHasOptionWithShortName() {
 assertTrue(commandLine.hasOption('a'));
 assertTrue(commandLine.hasOption('b'));
 assertTrue(commandLine.hasOption('c'));
 assertFalse(commandLine.hasOption('d'));
 }

 @Test
 public void testGetOptionObject() {
 Option optionA = commandLine.getOption("a");
 assertTrue(optionA.isSet());
 assertEquals(true, optionA.getValue());
 Option optionB = commandLine.getOption("b");
 assertNotNull(optionB.getValue());
 assertEquals(0, optionB.getValue().intValue());
 Option optionC = commandLine.getOption("c");
 assertNotNull(optionC.getValue());
 assertEquals(3, optionC.getValue().shortValue());
 }

 @Test
 public void testGetOptionValue() {
 String valueA = commandLine.getOptionValue("a");
 assertNull(valueA);
 String valueB = commandLine.getOptionValue("b");
 assertNull(valueB);
 String valueC = commandLine.getOptionValue("c");
 assertNull(valueC);
 }

 @Test
 public void testGetOptionValues() {
 String[] valuesB = commandLine.getOptionValues("b");
 assertArrayEquals(new String[]{""}, valuesB);
 Option optionC = commandLine.getOption("c");
 String[] valuesC = optionC.getValues();
 assertArrayEquals(new String[]{"3"}, valuesC);
 }

 @Test
 public void testGetOptionValuesWithShortName() {
 String[] valuesC = commandLine.getOptionValues('c');
 assertArrayEquals(new String[]{"3"}, valuesC);
 }

 @Test
 public void testGetArgs() {
 String[] args = commandLine.getArgs();
 assertArrayEquals(new String[]{}, args);
 }

 @Test
 public void testGetArgList() {
 List argList = commandLine.getArgList();
 assertTrue(argList.isEmpty());
 }

 @Test
 public void testAddArg() {
 commandLine.addArg("arg");
 List argList = commandLine.getArgList();
 assertEquals(1, argList.size());
 assertEquals("arg", argList.get(0));
 }

 @Test
 public void testAddOption() {
 Option optionD = new Option("d", true, "option D");
 commandLine.addOption(optionD);
 boolean hasOptionD = commandLine.hasOption("d");
 assertTrue(hasOptionD);
 Option[] options = commandLine.getOptions();
 assertEquals(4, options.length);
 }

 @Test
 public void testIterator() {
 Option[] options = commandLine.getOptions();
 Iterator iterator = commandLine.iterator();
 int i = 0;
 while (iterator.hasNext()) {
 Option option = (Option) iterator.next();
 assertEquals(options[i], option);
 i++;
 }
 }
}