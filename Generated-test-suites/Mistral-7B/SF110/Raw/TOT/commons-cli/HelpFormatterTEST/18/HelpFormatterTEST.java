package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

 @Test
 public void testDefaultHelpFormatter() {
  Options options = new Options();
  options.addOption("a", "arg1", true, "Argument option 1");
  options.addOption("b", "arg2", false, "Argument option 2");
  options.addOptionGroup(new OptionGroup("group1", "Group 1 options") {{
    this.setRequired(false);
    this.addOption("g1a", "group1-arg1", true, "Group 1 argument option 1");
    this.addOption("g1b", "group1-arg2", false, "Group 1 argument option 2");
  }});
  options.addOptionGroup(new OptionGroup("group2", "Group 2 options") {{
    this.setRequired(false);
    this.addOption("g2a", "group2-arg1", true, "Group 2 argument option 1");
  }});

  HelpFormatter formatter = new HelpFormatter();

  StringBuilder expectedHelp = new StringBuilder();
  expectedHelp.append("usage: org.apache.commons.cli.HelpFormatterTest [-arg1 <arg>] [--arg2]\n")
      .append("[--group1]\n")
      .append("   group1 options:\n")
      .append("      --group1-arg1 <arg>\n")
      .append("      --group1-arg2\n")
      .append("[--group2]\n")
      .append("   group2 options:\n")
      .append("      --group2-arg1 <arg>\n");

  StringHelpParser parser = new StringHelpParser(formatter);
  parser.parse(expectedHelp.toString(), options);
  String help = formatter.printHelp("org.apache.commons.cli.HelpFormatterTest", options, true);
  assertEquals(expectedHelp.toString(), help);
 }

 @Test
 public void testCustomizedHelpFormatter() {
  Options options = new Options();
  options.addOption("a", "arg1", true, "Argument option 1");
  options.addOption("b", "arg2", false, "Argument option 2");
  options.addOptionGroup(new OptionGroup("group1", "Group 1 options") {{
    this.setRequired(false);
    this.addOption("g1a", "group1-arg1", true, "Group 1 argument option 1");
    this.addOption("g1b", "group1-arg2", false, "Group 1 argument option 2");
  }});
  options.addOptionGroup(new OptionGroup("group2", "Group 2 options") {{
    this.setRequired(false);
    this.addOption("g2a", "group2-arg1", true, "Group 2 argument option 1");
  }});

  HelpFormatter formatter = new HelpFormatter();
  formatter.setWidth(100);
  formatter.setLeftPadding(5);
  formatter.setDescPadding(5);
  formatter.setSyntaxPrefix("Usage: ");
  formatter.setNewLine("\n  ");
  formatter.setOptPrefix("--");
  formatter.setLongOptPrefix("--");
  formatter.setArgName("ARG");

  StringBuilder expectedHelp = new StringBuilder();
  expectedHelp.append("Usage: org.apache.commons.cli.HelpFormatterTest\n")
      .append("  Arguments:\n")
      .append("      ARG_A:<ARG>\n")
      .append("      --arg2\n")
      .append("\n  Group 1 options:\n")
      .append("      --group1-arg1:<ARG>\n")
      .append("      --group1-arg2\n")
      .append("\n  Group 2 options:\n")
      .append("      --group2-arg1:<ARG>\n");

  StringHelpParser parser = new StringHelpParser(formatter);
  parser.parse(expectedHelp.toString(), options);
  String help = formatter.printHelp("org.apache.commons.cli.HelpFormatterTest", options, true);
  assertEquals(expectedHelp.toString(), help);
 }

 @Test
 public void testEmptyOptions() {
  Options options = new Options();

  HelpFormatter formatter = new HelpFormatter();

  StringBuilder expectedHelp = new StringBuilder();
  expectedHelp.append("usage: org.apache.commons.cli.HelpFormatterTest\n");

  StringHelpParser parser = new StringHelpParser(formatter);
  parser.parse(expectedHelp.toString(), options);
  String help = formatter.printHelp("org.apache.commons.cli.HelpFormatterTest", options, true);
  assertEquals(expectedHelp.toString(), help);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullOptions() {
  Options options = null;

  HelpFormatter formatter = new HelpFormatter();

  StringHelpParser parser = new StringHelpParser(formatter);
  parser.parse("", options);
 }

 @Test
 public void testCustomOptionComparator() {
  Options options = new Options();
  options.addOption("z", "zOption", true, "Z option");
  options.addOption("y", "yOption", true, "Y option");
  options.addOption("x", "xOption", false, "X option");

  HelpFormatter formatter = new HelpFormatter();
  formatter.setOptionComparator(new OptionComparator() {
    public int compare(Option o1, Option o2) {
      return o2.getKey().compareToIgnoreCase(o1.getKey());
    }
  });

  StringBuilder expectedHelp = new StringBuilder();
  expectedHelp.append("usage: org.apache.commons.cli.HelpFormatterTest\n")
      .append("options:\n")
      .append("      Y_OPTION:<ARG>\n")
      .append("      X_OPTION\n")
      .append("      Z_OPTION:<ARG>\n");

  StringHelpParser parser = new StringHelpParser(formatter);
  parser.parse(expectedHelp.toString(), options);
  String help = formatter.printHelp("org.apache.commons.cli.HelpFormatterTest", options, true);
  assertEquals(expectedHelp.toString(), help);
 }

}