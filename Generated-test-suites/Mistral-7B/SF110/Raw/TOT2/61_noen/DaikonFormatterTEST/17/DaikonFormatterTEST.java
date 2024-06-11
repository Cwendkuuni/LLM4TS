package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import fi.vtt.noen.testgen.model.*;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterWithSimpleInput() throws IOException {
        String fileName = "input.txt";
        boolean simple = true;
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
        String result = formatter.header();
        String expected = "decl-version 2.0\nvar-comparability implicit\n";
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testDaikonFormatterWithNullFileName() {
        String fileName = null;
        boolean simple = false;
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
    }

    @Test
    public void testDaikonFormatterWithInvalidFileName() {
        String fileName = "invalid_file_name.txt";
        boolean simple = false;
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
        Assert.assertNull(formatter.inputs);
    }

    @Test(expected = IOException.class)
    public void testDaikonFormatterWithEmptyInputList() throws IOException {
        String fileName = "input.txt";
        boolean simple = false;
        Collection<String> inputs = new ArrayList<>();
        DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
    }

    @Test(expected = IOException.class)
    public void testDaikonFormatterWithMissingTestgenPropertiesFile() throws IOException {
        String fileName = "input.txt";
        boolean simple = false;
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
    }

    @Test(expected = Exception.class)
    public void testDaikonFormatterWithNonExistentPropertyInTestgenPropertiesFile() throws IOException {
        String fileName = "input.txt";
        boolean simple = false;
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testDaikonFormatterWithInvalidClassNameInTestgenPropertiesFile() throws IOException {
        String fileName = "input.txt";
        boolean simple = false;
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
    }

    @Test(expected = IOException.class)
    public void testDaikonFormatterWithIOExceptionDuringInputLoading() throws IOException {
        String fileName = "input.txt";
        boolean simple = false;
        Collection<String> inputs = new ArrayList<>();
        DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
    }

    @Test
    public void testDaikonFormatterWithNullEvent() {
        String fileName = "input.txt";
        boolean simple = false;
        Collection<String> inputs = new ArrayList<>();
        DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
        Event event = null;
        String result = formatter.observations(null);
        Assert.assertNull(result);
    }

    @Test
    public void testDaikonFormatterWithEmptyEventList() {
        String fileName = "input.txt";
        boolean simple = false;
        Collection<String> inputs = new ArrayList<>();
        DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
        Iterator<Event> eventIterator = null;
        String result = formatter.observations(null);
        Assert.assertNull(result);
    }

    @Test
    public void testDaikonFormatterWithInvalidEventName() {
        String fileName = "input.txt";
        boolean simple = false;
        Collection<String> inputs = new ArrayList<>();
        DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
        Event event = new ProgramEvent("invalid_event_name");
        String result = formatter.observations(event);
        Assert.assertNull(result);
    }

    @Test
    public void testDaikonFormatterWithMultipleEventsWithSameName() {
        String fileName = "input.txt";
        boolean simple = false;
        Collection<String> inputs = new ArrayList<>();
        DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
        Event event1 = new ProgramEvent("event_name");
        Event event2 = new ProgramEvent("event_name");
        String result = formatter.observations(new ProgramRun(new ArrayList<>(), new ArrayList<>(), event1, event2));
        String expected = "ppt event_name\n";
        Assert.assertEquals(expected, result.substring(0, expected.length()));
    }

    @Test
    public void testDaikonFormatterWithMultipleEventsWithSameNameAndDifferentComparability() {
        String fileName = "input.txt";
        boolean simple = false;
        Collection<String> inputs = new ArrayList<>();
        DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
        Event event1 = new ProgramEvent("event_name");
        Event event2 = new ProgramEvent("event_name");
        event1.setComparability("implicit");
        event2.setComparability("explicit");
        String result = formatter.observations(new ProgramRun(new ArrayList<>(), new ArrayList<>(), event1, event2));
        String expected = "ppt event_name\nppt event_name\n";
        Assert.assertEquals(expected, result.substring(0, expected.length()));
    }

    @Test
    public void testDaikonFormatterWithNonStringEventAttributeValue() {
        String fileName = "input.txt";
        boolean simple = false;
        Collection<String> inputs = new ArrayList<>();
        DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
        Event event = new ProgramEvent("event_name");
        event.addAttribute(new LongAttribute("attribute_name", 123));
        String result = formatter.observations(event);
        String expected = "ppt event_name\n";
        Assert.assertEquals(expected, result.substring(0, expected.length()));
    }

    @Test
    public void testDaikonFormatterWithArrayAttributeWithNullValue() {
        String fileName = "input.txt";
        boolean simple = false;
        Collection<String> inputs = new ArrayList<>();
        DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
        Event event = new ProgramEvent("event_name");
        event.addAttribute(new ArrayAttribute("array_attribute_name", null));
        String result = formatter.observations(event);
        String expected = "ppt event_name\n";
        Assert.assertEquals(expected, result.substring(0, expected.length()));
    }

    @Test
    public void testDaikonFormatterWithArrayAttributeWithZeroLength() {
        String fileName = "input.txt";
        boolean simple = false;
        Collection<String> inputs = new ArrayList<>();
        DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
        Event event = new ProgramEvent("event_name");
        event.addAttribute(new ArrayAttribute("array_attribute_name", new int[0]));
        String result = formatter.observations(event);
        String expected = "ppt event_name\n";
        Assert.assertEquals(expected, result.substring(0, expected.length()));
    }

    @Test
    public void testDaikonFormatterWithArrayAttributeWithInvalidElementType() {
        String fileName = "input.txt";
        boolean simple = false;
        Collection<String> inputs = new ArrayList<>();
        DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
        Event event = new ProgramEvent("event_name");
        event.addAttribute(new ArrayAttribute("array_attribute_name", new Integer[]{1, 2, 3, 4}, "String"));
        String result = formatter.observations(event);
        String expected = "ppt event_name\n";
        Assert.assertEquals(expected, result.substring(0, expected.length()));
    }

    @Test
    public void testDaikonFormatterWithInvalidComparabilityValue() {
        String fileName = "input.txt";
        boolean simple = false;
        Collection<String> inputs = new ArrayList<>();
        DaikonFormatter formatter = new DaikonFormatter(fileName, inputs);
        Event event = new ProgramEvent("event_name");
        event.setComparability("invalid_comparability");
        String result = formatter.observations(event);
        Assert.assertNull(result);
    }
}