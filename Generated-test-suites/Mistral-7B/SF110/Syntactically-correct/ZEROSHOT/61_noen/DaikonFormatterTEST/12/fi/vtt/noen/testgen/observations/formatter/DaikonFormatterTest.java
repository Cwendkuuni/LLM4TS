package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.EventType;

public class DaikonFormatterTest {

 private static final String TEST_FILE = "test.dtrace";

 @Test
 public void testDaikonFormatterSimple() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, true);
 ProgramRun programRun = new ProgramRun();
 programRun.add(new Event("main", EventType.ENTER, Collections.emptyList(), Collections.emptyList()));
 programRun.add(new Event("main", EventType.EXIT, Collections.emptyList(), Collections.emptyList()));

 String expected = "decl-version 2.0\nvar-comparability implicit\n\nmain:::ENTER\n\nmain:::EXIT1\n";
 String result = formatter.observations(programRun);
 assertEquals(expected, result);
 }

 @Test
 public void testDaikonFormatterWithInputs() throws Exception {
 DaikonFormatter formatter = new DaikonFormatter(TEST_FILE, new ArrayList<>(Arrays.asList("method1", "method2")));
 ProgramRun programRun = new ProgramRun();
 programRun.add(new Event("method1", EventType.ENTER, Collections.emptyList(), Collections.emptyList()));
 programRun.add(new Event("method1", EventType.EXIT, Collections.emptyList(), Collections.emptyList()));
 programRun.add(new Event("method2", EventType.ENTER, Collections.emptyList(), Collections.emptyList()));
 programRun.add(new Event("method2", EventType.EXIT, Collections.emptyList(), Collections.emptyList()));
 programRun.add(new Event("method1", EventType.ENTER, Collections.emptyList(), Collections.singletonList(new EventAttribute("arg", "value"))));
 programRun.add(new Event("method1", EventType.EXIT, Collections.singletonList(new EventAttribute("result", "value")), Collections.emptyList()));
 programRun.add(new Event("method2", EventType.ENTER, Collections.singletonList(new EventAttribute("arg", "value")), Collections.emptyList()));
 programRun.add(new Event("method2", EventType.EXIT, Collections.emptyList(), Collections.singletonList(new EventAttribute("result", "value"))));

 String expected = "decl-version 2.0\nvar-comparability implicit\n\nmethod1:::ENTER\nmethod1:::ENTER variable arg comparability implicit dec-type java.lang.Object rep-type java.lang.Object\nmethod1:::EXIT1 ppt-type exit\nmethod1:::EXIT ppt-type exit\nmethod2:::ENTER\nmethod2:::ENTER variable arg comparability implicit dec-type java.lang.Object rep-type java.lang.Object\nmethod2:::EXIT1 ppt-type exit\n";
 String result = formatter.observations(programRun);
 assertEquals(expected, result);
 }

 @Test(expected = RuntimeException.class)
 public void testDaikonFormatterException() throws Exception {
 Properties properties = new Properties();
 properties.load(new FileInputStream("testgen.properties"));
 String className = properties.getProperty("InputInterface");
 Class<?> inputInterface = Class.forName(className);
 DaikonFormatter formatter = null;
 try {
 formatter = new DaikonFormatter(TEST_FILE, Arrays.asList("method1", "method2"));
 } catch (IOException e) {
 e.printStackTrace();
 fail("IOException shouldn't be thrown");
 }
 String exceptionMessage = "failed to load input and output interface definitions for daikon formatter";
 assertNotNull(formatter);
 try {
 throw new RuntimeException(exceptionMessage);
 } finally {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 InterfaceParser.parse(inputStream, inputInterface);
 }
 }
}