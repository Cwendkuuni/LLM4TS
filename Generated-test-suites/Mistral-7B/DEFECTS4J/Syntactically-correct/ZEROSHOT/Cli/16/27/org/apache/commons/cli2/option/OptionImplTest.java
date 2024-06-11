import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionType;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private static final int OPTION_ID = 1;
 private static final boolean OPTION_REQUIRED = true;
 private static final String OPTION_PREFIX = "p";
 private static final String OPTION_TRIGGER = "opt";
 private static final String OPTION_LONG_NAME = "longOption";
 private static final String OPTION_DESCRIPTION = "This is an option";
 private static final String OPTION_LONG_DESCRIPTION = "This is a long description for the option";

 @Test
 public void testOptionImplConstruction() {
 OptionImpl option = new OptionImpl(OPTION_ID, OPTION_REQUIRED);
 assertNotNull(option);
 }

 @Test
 public void testOptionImplCanProcess() {
 WriteableCommandLine commandLine = new CommandLine();
 ListIterator<String> arguments = commandLine.getArguments().listIterator();
 CommandLine optionCommandLine = new CommandLine();
 optionCommandLine.addOption(new OptionBuilder(OPTION_TRIGGER)
 .longOpt(OPTION_LONG_NAME)
 .description(OPTION_DESCRIPTION)
 .build());

 OptionImpl option = new OptionImpl(OPTION_ID, OPTION_REQUIRED);
 assertTrue(option.canProcess(optionCommandLine, commandLine.iterator()));
 }

 @Test(expected = IllegalStateException.class)
 public void testOptionImplCanProcessWithoutMatchingOption() {
 WriteableCommandLine commandLine = new CommandLine();
 ListIterator<String> arguments = commandLine.getArguments().listIterator();
 CommandLine optionCommandLine = new CommandLine();
 optionCommandLine.addOption(new OptionBuilder(OPTION_TRIGGER + "2")
 .longOpt(OPTION_LONG_NAME)
 .description(OPTION_DESCRIPTION)
 .build());

 OptionImpl option = new OptionImpl(OPTION_ID, OPTION_REQUIRED);
 option.canProcess(optionCommandLine, commandLine.iterator());
 }

 @Test
 public void testOptionImplToString() {
 OptionImpl option = new OptionBuilder(OPTION_TRIGGER)
 .longOpt(OPTION_LONG_NAME)
 .description(OPTION_DESCRIPTION)
 .build();
 String optionString = option.toString();
 assertTrue(optionString.contains(OPTION_DESCRIPTION));
 }

 @Test
 public void testOptionImplEquals() {
 OptionImpl option1 = new OptionBuilder(OPTION_TRIGGER)
 .longOpt(OPTION_LONG_NAME)
 .description(OPTION_DESCRIPTION)
 .build();
 OptionImpl option2 = new OptionBuilder(OPTION_TRIGGER)
 .longOpt(OPTION_LONG_NAME)
 .description(OPTION_DESCRIPTION)
 .build();
 assertTrue(option1.equals(option2));
 }

 @Test
 public void testOptionImplHashCode() {
 OptionImpl option1 = new OptionBuilder(OPTION_TRIGGER)
 .longOpt(OPTION_LONG_NAME)
 .description(OPTION_DESCRIPTION)
 .build();
 OptionImpl option2 = new OptionBuilder(OPTION_TRIGGER)
 .longOpt(OPTION_LONG_NAME)
 .description(OPTION_DESCRIPTION)
 .build();
 assertEquals(option1.hashCode(), option2.hashCode());
 }

 @Test
 public void testOptionImplFindOption() {
 OptionImpl option1 = new OptionBuilder(OPTION_TRIGGER)
 .longOpt(OPTION_LONG_NAME)
 .description(OPTION_DESCRIPTION)
 .build();
 OptionImpl option2 = new OptionBuilder(OPTION_TRIGGER)
 .longOpt(OPTION_LONG_NAME)
 .description(OPTION_DESCRIPTION)
 .build();
 assertEquals(option1, option2.findOption(OPTION_TRIGGER));
 }

 @Test
 public void testOptionImplIsRequired() {
 OptionImpl option = new OptionBuilder(OPTION_TRIGGER)
 .longOpt(OPTION_LONG_NAME)
 .required(true)
 .description(OPTION_DESCRIPTION)
 .build();
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionImplIsNotRequired() {
 OptionImpl option = new OptionBuilder(OPTION_TRIGGER)
 .longOpt(OPTION_LONG_NAME)
 .description(OPTION_DESCRIPTION)
 .build();
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionImplGetId() {
 OptionImpl option = new OptionBuilder(OPTION_TRIGGER)
 .longOpt(OPTION_LONG_NAME)
 .description(OPTION_DESCRIPTION)
 .build();
 assertEquals(OPTION_ID, option.getId());
 }

 @Test
 public void testOptionImplCheckPrefixes() {
 OptionImpl option = new OptionBuilder(OPTION_TRIGGER)
 .longOpt(OPTION_LONG_NAME)
 .description(OPTION_DESCRIPTION)
 .build();
 Set<String> prefixes = new java.util.HashSet<>();
 prefixes.add(OPTION_PREFIX);
 option.checkPrefixes(prefixes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionImplCheckPrefixesWithInvalidPrefix() {
 OptionImpl option = new OptionBuilder(OPTION_TRIGGER)
 .longOpt(OPTION_LONG_NAME)
 .description(OPTION_DESCRIPTION)
 .build();
 Set<String> prefixes = new java.util.HashSet<>();
 prefixes.add("invalidPrefix");
 option.checkPrefixes(prefixes);
 }
}