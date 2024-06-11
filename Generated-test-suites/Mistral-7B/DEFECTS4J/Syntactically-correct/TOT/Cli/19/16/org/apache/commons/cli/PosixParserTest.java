package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

 @Test
 public void testFlattenBasic() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");

 String[] arguments = {"-a", "-b=1", "--c=3", "-d", "e", "f", "--"};

 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, false);

 Assert.assertArrayEquals(new String[]{"-a", "--", "-b", "1", "--", "-c", "3", "-d", "e", "f"}, parsedArguments);
 }

 @Test(expected = ParseException.class)
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");

 String[] arguments = {"-a", "--", "-d"};

 PosixParser parser = new PosixParser();
 parser.flatten(options, arguments, true);
 }

 @Test
 public void testFlattenLongOption() {
 Options options = new Options();
 options.addOption("longOption", true, "Long Option");

 String[] arguments = {"--longOption", "value"};

 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, false);

 Assert.assertArrayEquals(new String[]{"--longOption", "value"}, parsedArguments);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");

 String[] arguments = {"--invalidOption", "-d"};

 PosixParser parser = new PosixParser();
 parser.flatten(options, arguments, false);
 }

 @Test
 public void testFlattenMultipleInvalidOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");

 String[] arguments = {"--invalidOption1", "--invalidOption2", "-d"};

 PosixParser parser = new PosixParser();
 try {
 parser.flatten(options, arguments, false);
 Assert.fail();
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testFlattenEmpty() {
 Options options = new Options();

 String[] arguments = {};

 PosixParser parser = new PosixParser();
 String[] parsedArguments = parser.flatten(options, arguments, false);

 Assert.assertArrayEquals(new String[]{}, parsedArguments);
 }
}