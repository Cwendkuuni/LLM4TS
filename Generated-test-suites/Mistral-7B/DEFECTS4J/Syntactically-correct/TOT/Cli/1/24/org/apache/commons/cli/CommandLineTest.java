import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

 @Test
 public void testHasOption() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new String[]{"--optionA"});
 assertTrue(commandLine.hasOption("optionA"));
 commandLine.parse(new String[]{"--nonexistentOption"});
 assertFalse(commandLine.hasOption("nonexistentOption"));
 }

 @Test
 public void testGetOptionValue() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new String[]{"--optionA=valueA"});
 assertEquals("valueA", commandLine.getOptionValue("optionA"));
 commandLine.parse(new String[]{"--optionB=valueB"});
 assertNull(commandLine.getOptionValue("optionA"));
 }

 @Test
 public void testGetOptionValues() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new String[]{"--optionA=valueA1,valueA2"});
 String[] values = commandLine.getOptionValues("optionA");
 assertArrayEquals(new String[]{"valueA1", "valueA2"}, values);
 commandLine.parse(new String[]{"--optionC=valueC"});
 assertNull(commandLine.getOptionValues("optionA"));
 }

 @Test
 public void testGetArgs() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new String[]{"--optionA=valueA", "arg1", "arg2"});
 assertArrayEquals(new String[]{"arg1", "arg2"}, commandLine.getArgs());
 commandLine.parse(new String[]{"--optionA=valueA"});
 assertArrayEquals(new String[0], commandLine.getArgs());
 }
}