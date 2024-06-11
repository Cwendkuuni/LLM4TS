package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class HelpFormatterTest {

	@org.junit.Before
	public void setUp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(80);
		formatter.setLeftPadding(2);
		formatter.setDescPadding(4);
		formatter.setSyntaxPrefix("myapp ");
		formatter.setNewLine("\n");
		formatter.setOptPrefix("--");
		formatter.setLongOptPrefix("--");
		formatter.setArgName("arg");
		formatter.setOptionComparator(new OptionComparator());
	}

	@org.junit.Test
	public void testDefaultHelp() {
		Options options = new Options();
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("myapp", options);
	}

	@org.junit.Test
	public void testWidth() {
		Options options = new Options();
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("myapp", options, 50);
		formatter = new HelpFormatter();
		formatter.setWidth(100);
		formatter.printHelp("myapp", options);
	}

	@org.junit.Test
	public void testLeftPadding() {
		Options options = new Options();
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("myapp", options, 80, null, null, 5, 5);
		formatter = new HelpFormatter();
		formatter.setLeftPadding(10);
		formatter.printHelp("myapp", options, 80);
	}

	@org.junit.Test
	public void testDescPadding() {
		Options options = new Options();
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("myapp", options, 80, null, null, 5, 10);
		formatter = new HelpFormatter();
		formatter.setDescPadding(15);
		formatter.printHelp("myapp", options, 80);
	}

	@org.junit.Test
	public void testSyntaxPrefix() {
		Options options = new Options();
		HelpFormatter formatter = new HelpFormatter();
		formatter.setSyntaxPrefix("myapp usage: ");
		formatter.printHelp("myapp", options);
		formatter = new HelpFormatter();
		formatter.setSyntaxPrefix("myapp: ");
		formatter.printHelp("myapp", options);
	}

	@org.junit.Test
	public void testNewLine() {
		Options options = new Options();
		HelpFormatter formatter = new HelpFormatter();
		formatter.setNewLine("\r\n");
		formatter.printHelp("myapp", options);
		formatter = new HelpFormatter();
		formatter.setNewLine("---");
		formatter.printHelp("myapp", options);
	}

	@org.junit.Test
	public void testOptPrefix() {
		Options options = new Options();
		HelpFormatter formatter = new HelpFormatter();
		formatter.setOptPrefix("-o ");
		formatter.printHelp("myapp", options);
		formatter = new HelpFormatter();
		formatter.setOptPrefix("-p ");
		formatter.printHelp("myapp", options);
	}

	@org.junit.Test
	public void testLongOptPrefix() {
		Options options = new Options();
		HelpFormatter formatter = new HelpFormatter();
		formatter.setLongOptPrefix("--long ");
		formatter.printHelp("myapp", options);
		formatter = new HelpFormatter();
		formatter.setLongOptPrefix("--short ");
		formatter.printHelp("myapp", options);
	}

	@org.junit.Test
	public void testArgName() {
		Options options = new Options();
		HelpFormatter formatter = new HelpFormatter();
		formatter.setArgName("argName");
		formatter.printHelp("myapp", options);
		formatter = new HelpFormatter();
		formatter.setArgName("param");
		formatter.printHelp("myapp", options);
	}

	@org.junit.Test
	public void testSetOptionComparator() {
		Options options = new Options();
		HelpFormatter formatter = new HelpFormatter();
		formatter.setOptionComparator(null);
		formatter.printHelp("myapp", options);
		formatter = new HelpFormatter();
		Comparator comparator = new Comparator() {
			public int compare(Object o1, Object o2) {
				Option opt1 = (Option) o1;
				Option opt2 = (Option) o2;
				return opt1.getKey().compareToIgnoreCase(opt2.getKey());
			}
		};
		formatter.setOptionComparator(comparator);
		formatter.printHelp("myapp", options);
	}

	@org.junit.Test
	public void testPrintHelpWithOptions() {
		Options options = new Options();
		options.addOption(OptionBuilder.withLongOpt("longopt")
				.withDescription("This is a long option")
				.create('l'));
		options.addOption(OptionBuilder.withLongOpt("no-longopt")
				.withDescription("This is a no-long option")
				.create('n'));
		options.addOption(OptionBuilder.withLongOpt("opt-group")
				.withDescription("This is an option group")
				.create(new OptionGroup("group", "Options in the group")));
		options.addOption(OptionBuilder.withLongOpt("opt-group-required")
				.withDescription("This is a required option in the group")
				.create(new Option("req", "req", true, "This is an required option in the group")));
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("myapp", options);
	}

	@org.junit.Test
	public void testPrintHelpWithHeaders() {
		Options options = new Options();
		options.addOption(OptionBuilder.withLongOpt("longopt")
				.withDescription("This is a long option")
				.create('l'));
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("myapp", options, "My App", null, null);
		formatter = new HelpFormatter();
		formatter.printHelp("myapp", options, "My App", null, "My footer");
		formatter = new HelpFormatter();
		formatter.printHelp("myapp", options, null, "My header", null);
		formatter = new HelpFormatter();
		formatter.printHelp("myapp", options, null, "My header", "My footer");
	}

	@org.junit.Test
	public void testPrintHelpWithAutoUsage() {
		Options options = new Options();
		options.addOption(OptionBuilder.withLongOpt("longopt")
				.withDescription("This is a long option")
				.create('l'));
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("myapp", options, true);
		formatter = new HelpFormatter();
		formatter.printHelp("myapp", options, "My App", true, null);
		formatter = new HelpFormatter();
		formatter.printHelp("myapp", options, "My App", true, "My footer");
	}

	@org.junit.Test
	public void testPrintHelpWithPrintWriter() {
		Options options = new Options();
		options.addOption(OptionBuilder.withLongOpt("longopt")
				.withDescription("This is a long option")
				.create('l'));
		PrintWriter out = new PrintWriter(System.out);
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(out, 80, "myapp", options);
		formatter = new HelpFormatter();
		formatter.printHelp(out, 80, "myapp", options, "My footer");
	}

	private void testPrintHelpWithPrintWriterAndWidth(int width) {
		Options options = new Options();
		options.addOption(OptionBuilder.withLongOpt("longopt")
				.withDescription("This is a long option")
				.create('l'));
		PrintWriter out = new PrintWriter(System.out);
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(out, width, "myapp", options);
		formatter = new HelpFormatter();
		formatter.printHelp(out, width, "myapp", options, "My footer");
	}

	@org.junit.Test
	public void testPrintHelpWithPrintWriterAndWidths() {
		Options options = new Options();
		options.addOption(OptionBuilder.withLongOpt("longopt")
				.withDescription("This is a long option")
				.create('l'));
		PrintWriter out = new PrintWriter(System.out);
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(out, 80, "myapp", options);
		formatter.printHelp(out, 50, "myapp", options);
		formatter.printHelp(out, 120, "myapp", options);
	}

	@org.junit.Test
	public void testPrintUsage() {
		Options options = new Options();
		HelpFormatter formatter = new HelpFormatter();
		formatter.printUsage(System.out, 80, "myapp", options);
		formatter = new HelpFormatter();
		formatter.printUsage(System.out, "myapp: myapp [options]", options);
	}

	@org.junit.Test
	public void testPrintOptions() {
		Options options = new Options();
		HelpFormatter formatter = new HelpFormatter();
		List optionsList = Arrays.asList(new Option[] {
				new Option("d", "debug", false, "Print debug information"),
				new Option("h", "help", false, "Print this help message"),
				new Option("i", "input", true, "Input file"),
				new Option("o", "output", true, "Output file"),
				new Option("v", "verbose", false, "Verbose output"),
				new Option("a", "append", false, "Append to file instead of overwrite"),
				new Option("c", "checkonly", false, "Check files only, don't process them")
		});
		formatter.printOptions(System.out, 80, options, 2, 4);
		formatter = new HelpFormatter();
		options.addOptionGroup(new OptionGroup("group1", "Group 1") {
			protected List createOptions() {
				return Collections.unmodifiableList(Arrays.asList(new Option[] {
						new Option("d", "debug", false, "Print debug information"),
						new Option("h", "help", false, "Print this help message")
				}));
			}
		});
		formatter.printOptions(System.out, 80, options, 2, 4);
		formatter = new HelpFormatter();
		options.addOptionGroup(new OptionGroup("group2", "Group 2") {
			protected List createOptions() {
				return Collections.unmodifiableList(Arrays.asList(new Option[] {
						new Option("i", "input", true, "Input file"),
						new Option("o", "output", true, "Output file")
				}));
			}
		});
		formatter.printOptions(System.out, 80, options, 2, 4);
		formatter = new HelpFormatter();
		options.addOptionGroup(new OptionGroup("group3", "Group 3") {
			protected List createOptions() {
				return Collections.unmodifiableList(Arrays.asList(new Option[] {
						new Option("v", "verbose", false, "Verbose output"),
						new Option("a", "append", false, "Append to file instead of overwrite"),
						new Option("c", "checkonly", false, "Check files only, don't process them")
				}));
			}
		});
		formatter.printOptions(System.out, 80, options, 2, 4);
		formatter = new HelpFormatter();
		formatter.printOptions(System.out, 80, options, 0, 0);
	}

	@org.junit.Test
	public void testPrintWrapped() {
		HelpFormatter formatter = new HelpFormatter();
		String text = "This is a very very very very very very very very very very very long text that should be wrapped";
		String wrappedText = formatter.renderWrappedText(new StringBuffer(), 80, 0, text);
		assertEquals("This is a very very very very very very very very very very long text that should be wrapped", wrappedText);
		wrappedText = formatter.renderWrappedText(new StringBuffer(), 50, 0, text);
		assertEquals("This is a very very very very very very very text that should be wrapped", wrappedText);
		wrappedText = formatter.renderWrappedText(new StringBuffer(), 80, 10, text);
		assertEquals("         This is a very very very very very very very very very very long text that should be wrapped", wrappedText);
		wrappedText = formatter.renderWrappedText(new StringBuffer(), 80, 15, text);
		assertEquals("                 This is a very very very very very very very very very very long text that should be wrapped", wrappedText);
	}

	@org.junit.Test
	public void testFindWrapPos() {
		HelpFormatter formatter = new HelpFormatter();
		String text = "This is a very very very very very very very very very very long text that should be wrapped";
		int pos = formatter.findWrapPos(text, 80, 0);
		assertEquals(51, pos);
		pos = formatter.findWrapPos(text, 50, 0);
		assertEquals(-1, pos);
		pos = formatter.findWrapPos(text, 50, 20);
		assertEquals(28, pos);
		pos = formatter.findWrapPos(text, 80, 20);
		assertEquals(56, pos);
		pos = formatter.findWrapPos(text, 80, 30);
		assertEquals(53, pos);
	}

	private static class OptionComparator implements Comparator<Option> {
		public int compare(Option o1, Option o2) {
			return o1.getKey().compareToIgnoreCase(o2.getKey());
		}
	}

}