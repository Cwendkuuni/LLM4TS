package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

 @Test
 public void testDefaultHelp() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyApp -a arg1 -b arg2 -c arg3";
 Options options = new Options();
 options.addOption("a", "arg1", true, "Option A with an argument");
 options.addOption("b", "arg2", true, "Option B with an argument");
 options.addOption("c", "arg3", true, "Option C with an argument");
 formatter.printHelp(System.out, 80, cmdLineSyntax, options);
 }

 @Test
 public void testHelpWithHeader() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyApp [options]";
 Options options = new Options();
 options.addOption("h", "help", false, "Print this help message.");
 formatter.setSyntaxPrefix("java MyApp ");
 formatter.printHelp(System.out, 80, cmdLineSyntax, options, "MyApp Options", true);
 }

 @Test
 public void testHelpWithFooter() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyApp [options]";
 Options options = new Options();
 options.addOption("h", "help", false, "Print this help message.");
 formatter.setSyntaxPrefix("java MyApp ");
 formatter.setFooter("Copyright 2023 MyApp Team");
 formatter.printHelp(System.out, 80, cmdLineSyntax, options);
 }

 @Test
 public void testHelpWithCustomWidth() {
 HelpFormatter formatter = new HelpFormatter();
 formatter.setWidth(50);
 String cmdLineSyntax = "java MyApp [options]";
 Options options = new Options();
 options.addOption("h", "help", false, "Print this help message.");
 formatter.printHelp(System.out, 50, cmdLineSyntax, options);
 }

 @Test
 public void testHelpWithCustomLeftPadding() {
 HelpFormatter formatter = new HelpFormatter();
 HelpFormatter formatterWith5LeftPadding = new HelpFormatter();
 formatterWith5LeftPadding.setLeftPadding(5);
 String cmdLineSyntax = "java MyApp [options]";
 Options options = new Options();
 options.addOption("h", "help", false, "Print this help message.");
 String output = "";
 try {
 formatter.printHelp(new ByteArrayOutputStream(), 80, cmdLineSyntax, options);
 output = formatter.getHelpString();
 formatter = formatterWith5LeftPadding;
 } finally {
 formatter.printHelp(System.out, 80, cmdLineSyntax, options);
 }
 assertEquals(output, formatter.getHelpString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidWidth() {
 HelpFormatter formatter = new HelpFormatter();
 formatter.setWidth(-1);
 }

 @Test(expected = NullPointerException.class)
 public void testNullOptions() {
 HelpFormatter formatter = new HelpFormatter();
 formatter.printHelp(System.out, 80, null, null);
 }
}