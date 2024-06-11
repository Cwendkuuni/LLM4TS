package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;

public class HelpFormatterTest {

 private static final int DEFAULT_WIDTH = 74;

 @org.junit.Before
 public void setUp() {
 HelpFormatter formatter = new HelpFormatter();
 formatter.setWidth(DEFAULT_WIDTH);
 }

 @org.junit.Test
 public void testPrintHelp() {
 String cmdLineSyntax = "HelpFormatter [-width <width>] [options]";
 Options options = new Options();
 options.addOption("w", "width", true, "Width of output");
 options.addOption("h", "help", false, "Print this help message");

 HelpFormatter formatter = new HelpFormatter();
 formatter.printHelp(cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testPrintHelpWithHeaderAndFooter() {
 String cmdLineSyntax = "HelpFormatter [-width <width>] [options]";
 Options options = new Options();
 options.addOption("w", "width", true, "Width of output");
 options.addOption("h", "help", false, "Print this help message");

 HelpFormatter formatter = new HelpFormatter();
 formatter.setWidth(DEFAULT_WIDTH);
 formatter.setSyntaxPrefix("usage: ");
 formatter.setLeftPadding(5);
 formatter.setDescPadding(10);
 formatter.printHelp("Test HelpFormatter", cmdLineSyntax, options, "This is a test help message.");
 }

 @org.junit.Test
 public void testPrintHelpWithAutoUsage() {
 String cmdLineSyntax = "HelpFormatter [-width <width>] [options]";
 Options options = new Options();
 options.addOption("w", "width", true, "Width of output");
 options.addOption("h", "help", false, "Print this help message");

 HelpFormatter formatter = new HelpFormatter();
 formatter.printHelp("Test HelpFormatter", cmdLineSyntax, options, null, true);
 }

 @org.junit.Test
 public void testPrintHelpWithNegativeWidth() {
 String cmdLineSyntax = "HelpFormatter [-width <width>] [options]";
 Options options = new Options();
 options.addOption("w", "width", true, "Width of output");
 options.addOption("h", "help", false, "Print this help message");

 HelpFormatter formatter = new HelpFormatter();
 formatter.setWidth(-5);
 formatter.printHelp(cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testPrintHelpWithInvalidOption() {
 String cmdLineSyntax = "HelpFormatter [-width <width>] [options]";
 Options options = new Options();
 options.addOption("w", "width", true, "Width of output");
 options.addOption("h", "help", false, "Print this help message");
 options.addOption("x", "x", false, "Test option x");

 HelpFormatter formatter = new HelpFormatter();
 formatter.printHelp(cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testPrintHelpWithMissingHelpOption() {
 String cmdLineSyntax = "HelpFormatter [-width <width>] [options]";
 Options options = new Options();
 options.addOption("w", "width", true, "Width of output");

 HelpFormatter formatter = new HelpFormatter();
 formatter.printHelp(cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testPrintHelpWithEmptyOptions() {
 String cmdLineSyntax = "HelpFormatter [-width <width>] [options]";
 Options options = new Options();

 HelpFormatter formatter = new HelpFormatter();
 formatter.printHelp(cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testPrintHelpWithEmptyCommandLineSyntax() {
 Options options = new Options();
 options.addOption("w", "width", true, "Width of output");
 options.addOption("h", "help", false, "Print this help message");

 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "";
 formatter.printHelp(cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testPrintHelpWithNullCommandLineSyntax() {
 Options options = new Options();
 options.addOption("w", "width", true, "Width of output");
 options.addOption("h", "help", false, "Print this help message");

 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = null;
 formatter.printHelp(cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testPrintHelpWithNullOptions() {
 String cmdLineSyntax = "HelpFormatter [-width <width>] [options]";

 HelpFormatter formatter = new HelpFormatter();
 Options options = null;
 formatter.printHelp(cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testPrintUsage() {
 PrintWriter pw = new PrintWriter(System.out);
 String cmdLineSyntax = "HelpFormatter [-width <width>] [options]";
 Options options = new Options();
 options.addOption("w", "width", true, "Width of output");
 options.addOption("h", "help", false, "Print this help message");

 HelpFormatter formatter = new HelpFormatter();
 formatter.printUsage(pw, DEFAULT_WIDTH, cmdLineSyntax, options);
 }

 @org.junit.Test
 public void testPrintUsageWithAutoUsage() {
 PrintWriter pw = new PrintWriter(System.out);
 String cmdLineSyntax = "HelpFormatter [-width <width>] [options]";
 Options options = new Options();
 options.addOption("w", "width", true, "Width of output");
 options.addOption("h", "help", false, "Print this help message");

 HelpFormatter formatter = new HelpFormatter();
 formatter.printUsage(pw, DEFAULT_WIDTH, cmdLineSyntax, options, true);
 }

 @org.junit.Test
 public void testPrintUsageWithEmptyCommandLineSyntax() {
 PrintWriter pw = new PrintWriter(System.out);
 Options options = new Options();
 options.addOption("w", "width", true, "Width of output");
 options.addOption("h", "help", false, "Print this help message");

 HelpFormatter formatter = new HelpFormatter();
 formatter.printUsage(pw, DEFAULT_WIDTH, "", options);
 }

 @org.junit.Test
 public void testPrintUsageWithNullOptions() {
 PrintWriter pw = new PrintWriter(System.out);
 String cmdLineSyntax = "HelpFormatter [-width <width>] [options]";

 HelpFormatter formatter = new HelpFormatter();
 formatter.printUsage(pw, DEFAULT_WIDTH, cmdLineSyntax, null);
 }

 @org.junit.Test
 public void testPrintUsageWithNullCommandLineSyntax() {
 PrintWriter pw = new PrintWriter(System.out);
 Options options = new Options();
 options.addOption("w", "width", true, "Width of output");
 options.addOption("h", "help", false, "Print this help message");

 HelpFormatter formatter = new HelpFormatter();
 formatter.printUsage(pw, DEFAULT_WIDTH, null, options);
 }
}