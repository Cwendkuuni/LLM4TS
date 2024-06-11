import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterConstructor() {
        HelpFormatter formatter = new HelpFormatter();
        // Check default values
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        // Check default prefixes and names
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertEquals("arg", formatter.getArgName());
    }

    @Test
    public void testSetterMethods() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(2);
        formatter.setDescPadding(4);
        assertEquals(50, formatter.getWidth());
        assertEquals(2, formatter.getLeftPadding());
        assertEquals(4, formatter.getDescPadding());
    }

    @Test
    public void testPrintHelp() {
        String cmdLineSyntax = "java MyClass -option1 value1 -option2 value2";
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addRequiredOption("option1", true, "o1", "Value for option1");
        options.addRequiredOption("option2", true, "o2", "Value for option2");
        formatter.printHelp(System.out, 80, cmdLineSyntax, null, options, 1, 4, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullPrintWriter() {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = null;
        formatter.printHelp(pw, 80, "usage: java MyClass", null, null, 1, 4, null, false);
    }

    @Test
    public void testPrintHelpWithEmptyCmdLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "";
        Options options = new Options();
        formatter.printHelp(System.out, 80, cmdLineSyntax, null, options, 1, 4, null, false);
    }

    @Test
    public void testPrintHelpWithEmptyOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass";
        Formatter formatterOut = new ConsoleFormatter();
        PrintWriter pw = new PrintWriter(new ByteArrayOutputStream());
        formatter.printHelp(pw, 80, cmdLineSyntax, null, new Options(), 1, 4, null, false, formatterOut);
        assertEquals("", pw.getBuffer().toString());
    }

    @Test
    public void testPrintHelpWithNullOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass";
        Formatter formatterOut = new ConsoleFormatter();
        PrintWriter pw = new PrintWriter(new ByteArrayOutputStream());
        try {
            formatter.printHelp(pw, 80, cmdLineSyntax, null, null, 1, 4, null, false, formatterOut);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPrintHelpWithEmptyOpt() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass";
        Options options = new Options();
        try {
            Option option = new Option("", true, "", "");
            options.addOption(option);
            formatter.printHelp(System.out, 80, cmdLineSyntax, null, options, 1, 4, null, false);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPrintHelpWithEmptyDescription() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass";
        Options options = new Options();
        Option option = new Option("o1", true, "", "");
        options.addOption(option);
        formatter.printHelp(System.out, 80, cmdLineSyntax, null, options, 1, 4, null, false);
    }

    @Test
    public void testPrintHelpWithInvalidComparator() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass";
        Options options = new Options();
        formatter.setOptionComparator(new InvalidComparator());
        try {
            formatter.printHelp(System.out, 80, cmdLineSyntax, null, options, 1, 4, null, false);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}

class InvalidComparator implements Comparator<Option> {
    @Override
    public int compare(Option o1, Option o2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}