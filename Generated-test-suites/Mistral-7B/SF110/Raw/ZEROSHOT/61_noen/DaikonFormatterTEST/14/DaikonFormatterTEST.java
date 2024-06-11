package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.EventType;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.parser.ParseException;
import fi.vtt.noen.testgen.parser.TestgenParser;
import fi.vtt.noen.testgen.parser.TestgenParserFactory;
import fi.vtt.noen.testgen.testdata.TestDataFactory;
import fi.vtt.noen.testgen.testdata.TestDataProvider;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws Exception {
        // Test constructor with simple flag
        DaikonFormatter formatter = new DaikonFormatter("test.cfg", true);

        // Test header method
        String header = formatter.header();
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);

        // Test constructor without inputs
        try {
            formatter = new DaikonFormatter("test.cfg", Collections.emptyList());
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
        }

        // Test constructor with inputs
        Set<String> inputs = new HashSet<>(Arrays.asList("method1", "method2"));
        formatter = new DaikonFormatter("test.cfg", inputs);

        // Test inputs method
        Collection<String> actualInputs = formatter.inputs();
        assertEquals(inputs, actualInputs);

        // Test footer method
        String footer = formatter.footer();
        assertEquals("", footer);

        // Test fileNameExtension method
        String fileNameExtension = formatter.fileNameExtension();
        assertEquals("dtrace", fileNameExtension);

        // Test observations method with simple flag
        ProgramRun runSimple = TestDataFactory.createSimpleProgramRun();
        String observationsSimple = formatter.observations(runSimple);
        assertEquals("ppt method1:::ENTER\nppt method1:::EXIT1\n", observationsSimple);

        // Test observations method without simple flag
        ProgramRun runComplex = TestDataFactory.createComplexProgramRun();
        String observationsComplex = formatter.observations(runComplex);
        String expectedObservations = "ppt method1:::ENTER\n" +
                "ppt method1:::EXIT1\n" +
                "ppt method2:::ENTER\n" +
                "ppt method2:::EXIT1\n" +
                "ppt method1:::ENTER\n" +
                "ppt method2:::ENTER\n" +
                "ppt method1:::EXIT1\n" +
                "ppt method2:::EXIT1\n";
        assertEquals(expectedObservations, observationsComplex);

        // Test classForProperty and classesForMultipleProperties methods
        Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(TestDataProvider.TESTGEN_PROPERTIES.getBytes()));
        Class inputInterfaceClass = DaikonFormatter.classForProperty("InputInterface");
        Class outputInterfaceClass = DaikonFormatter.classForProperty("OutputInterface");
        Collection<Class> inputClasses = DaikonFormatter.classesForMultipleProperties("InputInterface");
        Collection<Class> outputClasses = DaikonFormatter.classesForMultipleProperties("OutputInterface");
        assertEquals(inputInterfaceClass, TestgenParserFactory.getParserClass(TestgenParser.class, "InputInterface"));
        assertEquals(outputInterfaceClass, TestgenParserFactory.getParserClass(TestgenParser.class, "OutputInterface"));
        assertEquals(2, inputClasses.size());
        assertEquals(outputClasses.size(), 3);

        // Test declarations method
        Event event = new Event("method1", EventType.ENTER, Arrays.asList(new EventAttribute("attr1", "java.lang.String", "value1")));
        String eventName = event.getName().replace(' ', '_');
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DaikonFormatter.declarations(event, outputStream, "implicit");
        String declarations = new String(outputStream.toByteArray());
        String expectedDeclarations = "ppt " + eventName + "\n" +
                "  variable attr1\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability implicit\n";
        assertEquals(expectedDeclarations, declarations);

        // Test attributes method
        outputStream = new ByteArrayOutputStream();
        DaikonFormatter.attributes(event, outputStream);
        String attributes = new String(outputStream.toByteArray());
        String expectedAttributes = "attr1\"value1\"1\n";
        assertEquals(expectedAttributes, attributes);
    }
}