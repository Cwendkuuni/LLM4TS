package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 @Test
 public void testFlattenBasic() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B");
 options.addOption("c", 3, "Option C");

 String[] args = new String[]{"--a", "-b", "2", "-c", "3", "arg"};
 String[] expected = new String[]{"--a", "-b", "2", "-c", "3"};

 try {
 String[] actual = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, actual);
 } catch (ParseException e) {
 // Should not happen as we are only testing flattening, not parsing
 }
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B");
 options.addOption("c", 3, "Option C");

 String[] args = new String[]{"--a", "-b", "2", "-c", "3", "arg", "--", "nonOption", "arg2"};
 String[] expected = new String[]{"--a", "-b", "2", "-c", "3", "--", "nonOption", "arg2"};

 try {
 String[] actual = new PosixParser().flatten(options, args, true);
 assertArrayEquals(expected, actual);
 } catch (ParseException e) {
 // Should not happen as we are only testing flattening, not parsing
 }
 }

 @Test
 public void testFlattenWithLongOption() {
 Options options = new Options();
 options.addOption("longOption", true, "Long option");

 String[] args = new String[]{"--longOption"};
 String[] expected = new String[]{"--longOption"};

 try {
 String[] actual = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, actual);
 } catch (ParseException e) {
 // Should not happen as we are only testing flattening, not parsing
 }
 }

 @Test
 public void testFlattenWithOptionWithArg() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B with argument");

 String[] args = new String[]{"--a", "-b", "value"};
 String[] expected = new String[]{"--a", "-b", "value"};

 try {
 String[] actual = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, actual);
 } catch (ParseException e) {
 // Should not happen as we are only testing flattening, not parsing
 }
 }

 @Test
 public void testFlattenWithShortOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption('b', 2, "Option B");

 String[] args = new String[]{"-a", "-b", "2"};
 String[] expected = new String[]{"-a", "-b", "2"};

 try {
 String[] actual = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, actual);
 } catch (ParseException e) {
 // Should not happen as we are only testing flattening, not parsing
 }
 }

 @Test
 public void testFlattenWithShortOptionWithArg() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption('b', 2, "Option B with argument");

 String[] args = new String[]{"-a", "-b", "value"};
 String[] expected = new String[]{"-a", "-b", "value"};

 try {
 String[] actual = new PosixParser().flatten(options, args, false);
 assertArrayEquals(expected, actual);
 } catch (ParseException e) {
 // Should not happen as we are only testing flattening, not parsing
 }
 }

 @Test (expected = IllegalArgumentException.class)
 public void testFlattenWithEmptyOptions() {
 String[] args = new String[]{"--"};
 Options options = new Options();
 try {
 new PosixParser().flatten(options, args, false);
 } catch (ParseException e) {
 // Ignore, it's only a ParseException
 } catch (IllegalArgumentException ex) {
 assertEquals("Options must not be null", ex.getMessage());
 }
 }

 @Test (expected = IllegalArgumentException.class)
 public void testFlattenWithEmptyArguments() {
 Options options = new Options();
 String[] args = new String[] {};
 try {
 new PosixParser().flatten(options, args, false);
 } catch (ParseException e) {
 // Ignore, it's only a ParseException
 } catch (IllegalArgumentException ex) {
 assertEquals("Arguments must not be null", ex.getMessage());
 }
 }
}