package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

 @Test
 public void testDefaultHelpFormatter() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyApp --optionA valueA --optionB valueB arg1 arg2";
 Options options = new Options();
 options.addOption("optionA", true, "Option A description");
 options.addOption("optionB", false, "Option B description");
 options.addOption("help", false, "Print this help message");

 formatter.printHelp(System.out, cmdLineSyntax, options);
 }

 @Test
 public void testCustomHelpFormatterWidth() {
 HelpFormatter formatter = new HelpFormatter();
 formatter.setWidth(50);
 String cmdLineSyntax = "java MyApp --optionA valueA --optionB valueB arg1 arg2";
 Options options = new Options();
 options.addOption("optionA", true, "Option A description");
 options.addOption("optionB", false, "Option B description");
 options.addOption("help", false, "Print this help message");

 formatter.printHelp(System.out, cmdLineSyntax, options);
 }

 @Test
 public void testCustomHelpFormatterOptPrefix() {
 HelpFormatter formatter = new HelpFormatter();
 formatter.setOptPrefix("--");
 String cmdLineSyntax = "java MyApp --optionA valueA --optionB valueB arg1 arg2";
 Options options = new Options();
 options.addOption("optionA", true, "Option A description");
 options.addOption("optionB", false, "Option B description");
 options.addOption("help", false, "Print this help message");

 formatter.printHelp(System.out, cmdLineSyntax, options);
 }

 @Test
 public void testCustomHelpFormatterArgName() {
 HelpFormatter formatter = new HelpFormatter();
 formatter.setArgName("param");
 String cmdLineSyntax = "java MyApp param1 param2";
 Options options = new Options();
 options.addOption("option", false, "Option description");

 formatter.printHelp(System.out, cmdLineSyntax, options);
 }

 @Test
 public void testHelpFormatterWithRequiredOption() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyApp arg1 arg2";
 Options options = new Options();
 options.addOption("option", true, "Option description");
 options.addOption("help", false, "Print this help message");

 try {
 formatter.printHelp(System.out, cmdLineSyntax, options);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testHelpFormatterWithInvalidArgName() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyApp arg1 arg2";
 Options options = new Options();
 options.addOption("option", true, "Option description");
 options.addOption("arg", true, "Invalid argument name");

 try {
 formatter.printHelp(System.out, cmdLineSyntax, options);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testHelpFormatterWithMissingArgument() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyApp arg1";
 Options options = new Options();
 options.addOption("option", true, "Option description");
 options.addOption("arg", true, "Argument description");

 try {
 formatter.printHelp(System.out, cmdLineSyntax, options);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testHelpFormatterWithGroupOptions() {
 HelpFormatter formatter = new HelpFormatter();
 String cmdLineSyntax = "java MyApp arg1 arg2";
 Options options = new Options();
 OptionGroup group = new OptionGroup("MyGroup");
 group.setDescription("Group description");
 group.addOption("groupOption1", true, "Group option 1");
 group.addOption("groupOption2", true, "Group option 2");
 options.addOptionGroup(group);
 options.addOption("help", false, "Print this help message");

 formatter.printHelp(System.out, cmdLineSyntax, options);
 }
}