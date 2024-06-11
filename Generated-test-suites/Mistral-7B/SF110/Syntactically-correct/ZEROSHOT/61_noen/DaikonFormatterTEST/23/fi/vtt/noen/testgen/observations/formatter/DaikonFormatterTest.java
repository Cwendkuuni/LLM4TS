package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.observations.data.Const;
import fi.vtt.noen.testgen.parser.InterfaceParser;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("test.pdb", true);
        String expectedHeader = formatter.header();
        assertEquals(expectedHeader, "decl-version 2.0\nvar-comparability implicit\n");

        DaikonFormatter formatterWithInputs = new DaikonFormatter("test.pdb", InterfaceParser.methodNames(new File("inputs.txt").bufferedReader()));
        String fileName = "test_inputs.dtrace";
        formatterWithInputs.writeToFile(new ByteArrayOutputStream(), fileName);
        String expectedDaikonFile = readFile("test_inputs_expected.dtrace");
        assertEquals(expectedDaikonFile, readFile(fileName));

        ProgramRun programRun = new ProgramRun();
        Event event1 = new Event("event1", new ArrayList<EventAttribute>(), Const.INITIAL_STATE);
        Event event2 = new Event("event2", new ArrayList<EventAttribute>(), Const.INITIAL_STATE);
        Event event3 = new Event("event3", new ArrayList<EventAttribute>(), Const.INITIAL_STATE);
        Event event4 = new Event("event4", new ArrayList<EventAttribute>(), Const.EXIT_STATE);
        programRun.add(event1);
        programRun.add(event2);
        programRun.add(event3);
        programRun.add(event4);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DaikonFormatter formatterWithComplexLogic = new DaikonFormatter("test.pdb", programRun);
        String actualDaikonFile = formatterWithComplexLogic.generateDaikonFile(outputStream);
        String expectedDaikonFileComplex = readFile("test_complex_expected.dtrace");
        assertEquals(expectedDaikonFileComplex, actualDaikonFile);
    }

    private String readFile(String fileName) throws IOException {
        byte[] bytes = new byte[1024];
        int length;
        String text = "";
        try (FileInputStream file = new FileInputStream(fileName)) {
            while ((length = file.read(bytes)) > 0) {
                text += new String(bytes, 0, length);
            }
        }
        return text;
    }
}