import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 private static final String[] ARGUMENTS_1 = {"--help", "-a", "-b", "--optionC=valueC", "-d", "-e=valueE"};
 private static final String[] ARGUMENTS_2 = {"--help", "-a", "-b", "-c", "--optionD=valueD"};
 private static final String[] ARGUMENTS_3 = {"--help", "-a", "--"};
 private static final String[] ARGUMENTS_4 = {"--help", "-a", "-b", "--invalidOption"};
 private static final String[] ARGUMENTS_5 = {"--help", "-a", "--", "value"};
 private static final String[] ARGUMENTS_6 = {"--help", "-a", "-b", "--optionC=valueC", "--optionD=valueD"};

 @Test(expected = ParseException.class)
 public void testParsingWithInvalidOptionThrowsParseException() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = ARGUMENTS_4;
 String[] expectedOptions = {"optionC", "optionD"};
 String[] expectedArguments = {};
 try {
 parser.parse(options, arguments, true);
 fail("ParseException expected");
 } catch (ParseException e) {
 assertArrayEquals(expectedOptions, options.getOptions());
 assertArrayEquals(expectedArguments, options.getArgList());
 }
 }

 @Test
 public void testParsingWithValidOptionsAndArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = ARGUMENTS_1;
 String[] expectedOptions = {"a", "optionC", "d", "e"};
 String[] expectedArguments = {"valueC", "valueE"};
 assertArrayEquals(expectedOptions, options.getOptions());
 assertArrayEquals(expectedArguments, options.getArgList());
 }

 @Test
 public void testParsingWithValidOptions() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = ARGUMENTS_3;
 String[] expectedOptions = {"help"};
 String[] expectedArguments = {};
 assertArrayEquals(expectedOptions, options.getOptions());
 assertArrayEquals(expectedArguments, options.getArgList());
 }

 @Test
 public void testParsingWithNoOptions() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = ARGUMENTS_5;
 String[] expectedOptions = {};
 String[] expectedArguments = {"value"};
 assertArrayEquals(expectedOptions, options.getOptions());
 assertArrayEquals(expectedArguments, options.getArgList());
 }

 @Test
 public void testParsingWithMultipleOptions() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = ARGUMENTS_2;
 String[] expectedOptions = {"a", "optionC"};
 String[] expectedArguments = {};
 assertArrayEquals(expectedOptions, options.getOptions());
 assertArrayEquals(expectedArguments, options.getArgList());
 }

 @Test
 public void testParsingWithMultipleOptionsAndArguments() {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = ARGUMENTS_6;
 String[] expectedOptions = {"a", "optionC", "optionD"};
 String[] expectedArguments = {};
 assertArrayEquals(expectedOptions, options.getOptions());
 assertArrayEquals(expectedArguments, options.getArgList());
 }
}