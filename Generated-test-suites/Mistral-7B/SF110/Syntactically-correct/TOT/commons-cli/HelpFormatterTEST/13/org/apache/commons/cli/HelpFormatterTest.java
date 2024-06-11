package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

 @Test
 public void testDefaultHelp() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java org.apache.commons.cli.Test -a 1 -b arg";
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B with argument");

 formatter.printHelp(cmdLineSyntax, options);
 }

 @Test
 public void testCustomHelp() {
 HelpFormatter formatter = new HelpFormatter();
 int width = 100;
 formatter.setWidth(width);
 String cmdLineSyntax = "java org.apache.commons.cli.Test [<arg1>] [options]";
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B with argument");

 formatter.printHelp(cmdLineSyntax, options, "Custom Help Message");
 }

 @Test
 public void testHelpWithArguments() {
 HelpFormatter formatter = new HelpFormatter();
 int width = 100;
 formatter.setWidth(width);
 String cmdLineSyntax = "java org.apache.commons.cli.Test [<arg1>] [options]";
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B with argument");

 String output = "";
 try {
 PrintWriter out = new PrintWriter(new ByteArrayOutputStream());
 formatter.printHelp(out, width, cmdLineSyntax, options);
 output = out.getBuffer().toString();
 }
 catch (IOException e) {
 e.printStackTrace();
 fail("Failed to print help");
 }

 assertTrue(output.contains("-a"));
 assertTrue(output.contains("Option A"));
 assertTrue(output.contains("--b"));
 assertTrue(output.contains("Option B with argument"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCmdLineSyntax() {
 HelpFormatter formatter = new HelpFormatter();
 int width = 100;
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B with argument");

 formatter.printHelp(null, width, "", options);
 }

 @Test
 public void testEmptyCmdLineSyntax() {
 HelpFormatter formatter = new HelpFormatter();
 int width = 100;
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B with argument");

 formatter.printHelp("", width, "", options);
 }
}