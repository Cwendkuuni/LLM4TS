import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithOptAndDescription() {
        Option option = new Option("a", "option-a", false, "Description for option-a");
        assertEquals("a", option.getOpt());
        assertEquals("option-a", option.getLongOpt());
        assertFalse(option.hasArg());
        assertNotNull(option.getDescription());
        assertEquals("Description for option-a", option.getDescription());
    }

    @Test
    public void testOptionConstructorWithOptAndHasArgAndDescription() {
        Option option = new Option("b", "option-b", true, "Description for option-b");
        assertEquals("b", option.getOpt());
        assertEquals("option-b", option.getLongOpt());
        assertTrue(option.hasArg());
        assertNotNull(option.getDescription());
        assertEquals("Description for option-b", option.getDescription());
    }

    @Test
    public void testOptionConstructorWithOptOptHasArgAndDescription() {
        Option option = new Option("c", "long-option-c", true, "Description for long-option-c");
        assertEquals("c", option.getOpt());
        assertEquals("long-option-c", option.getLongOpt());
        assertTrue(option.hasArg());
        assertNotNull(option.getDescription());
        assertEquals("Description for long-option-c", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new Option("d", "option-d", false, "Description for option-d");
        assertEquals('d', option.getId());
    }

    @Test
    public void testGetKey() {
        Option option = new Option("e", null, false, "Description for option-e");
        assertEquals("e", option.getKey());
        Option optionWithLongOpt = new Option("e", "long-option-e", false, "Description for option-e");
        assertEquals("long-option-e", optionWithLongOpt.getKey());
    }

    @Test
    public void testGetOpt() {
        Option option = new Option("f", null, false, "Description for option-f");
        assertEquals("f", option.getOpt());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("g", null, false, "Description for option-g");
        assertNull(option.getLongOpt());
        Option optionWithLongOpt = new Option("g", "long-option-g", false, "Description for option-g");
        assertEquals("long-option-g", optionWithLongOpt.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("h", null, false, "Description for option-h");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArg() {
        Option option = new Option("i", null, false, "Description for option-i");
        assertFalse(option.hasOptionalArg());
        Option optionWithOptionalArg = new Option("i", null, true, "Description for option-i");
        assertTrue(optionWithOptionalArg.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("j", null, false, "Description for option-j");
        assertFalse(option.hasLongOpt());
        Option optionWithLongOpt = new Option("j", "long-option-j", false, "Description for option-j");
        assertTrue(optionWithLongOpt.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("k", null, false, "Description for option-k");
        assertFalse(option.hasArg());
        Option optionWithArg = new Option("k", null, true, "Description for option-k");
        assertTrue(optionWithArg.hasArg());
    }

    @Test
    public void testSetDescription() {
        Option option = new Option("l", null, false, "Description for option-l");
        assertNotNull(option.getDescription());
        assertEquals("Description for option-l", option.getDescription());
        option.setDescription("New description for option-l");
        assertEquals("New description for option-l", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("m", null, false, "Description for option-m");
        assertFalse(option.isRequired());
        Option optionRequired = new Option("m", null, false, "Description for option-m");
        optionRequired.setRequired(true);
        assertTrue(optionRequired.isRequired());
    }

    @Test
    public void testSetRequired() {
        Option option = new Option("n", null, false, "Description for option-n");
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("o", null, false, "Description for option-o");
        assertNull(option.getArgName());
        option.setArgName("arg-name");
        assertEquals("arg-name", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("p", null, false, "Description for option-p");
        assertFalse(option.hasArgName());
        Option optionWithArgName = new Option("p", null, false, "Description for option-p");
        optionWithArgName.setArgName("arg-name");
        assertTrue(optionWithArgName.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("q", null, false, "Description for option-q");
        assertFalse(option.hasArgs());
        Option optionWithArgs = new Option("q", null, true, "Description for option-q");
        assertTrue(optionWithArgs.hasArgs());
    }

    @Test
    public void testSetArgs() {
        Option option = new Option("r", null, false, "Description for option-r");
        assertEquals(-1, option.getArgs());
        option.setArgs(5);
        assertEquals(5, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("s", null, false, "Description for option-s");
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(';');
        assertTrue(option.hasValueSeparator());
        assertEquals(';', option.getValueSeparator());
    }

    @Test
    public void testAddValueForProcessingWithNoArgs() {
        Option option = new Option("t", null, false, "Description for option-t");
        assertThrows(RuntimeException.class, () -> option.addValueForProcessing("value"));
    }

    @Test
    public void testAddValueForProcessingWithMoreThanExpectedArgs() {
        Option option = new Option("u", null, 2, "Description for option-u");
        List<String> values = new ArrayList<>();
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetValue() {
        Option option = new Option("v", null, false, "Description for option-v");
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testGetValueWithDefaultValue() {
        Option option = new Option("w", null, false, "Description for option-w");
        assertEquals(null, option.getValue());
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue("default-value"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("x", null, false, "Description for option-x");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        Option option = new Option("y", null, false, "Description for option-y");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        List<String> list = option.getValuesList();
        assertEquals(2, list.size());
        assertEquals("value1", list.get(0));
        assertEquals("value2", list.get(1));
    }

    @Test
    public void testToString() {
        Option option = new Option("z", null, false, "Description for option-z");
        assertEquals("[ option: z :: Description for option-z ]", option.toString());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWithMoreThanExpectedValuesWhenHasNoArgs() {
        Option option = new Option("A", null, false, "Description for option A");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithIndexOutOfBounds() {
        Option option = new Option("B", null, false, "Description for option B");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
        option.getValue(2);
    }
}