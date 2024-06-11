package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.EventAttribute;
import fi.vtt.noen.testgen.observations.data.EventType;

public class DaikonFormatterTest {

	private DaikonFormatter formatter;
	private ProgramRun programRun;
	private InterfaceParser interfaceParser;

	@Before
	public void setup() throws IOException {
		interfaceParser = mock(InterfaceParser.class);
		when(interfaceParser.methodNames(Collections.emptyList())).thenReturn(Arrays.asList("method1", "method2"));
		formatter = new DaikonFormatter("test.txt", interfaceParser);
		programRun = new ProgramRun();
		programRun.add(new Event("method1", EventType.ENTER, new EventAttribute("attr1", "value1"), new EventAttribute("attr2", "value2")));
		programRun.add(new Event("method1", EventType.EXIT, new EventAttribute("attr1", "value1"), new EventAttribute("attr2", "value3")));
		programRun.add(new Event("method2", EventType.ENTER, new EventAttribute("attr3", "value4"), new EventAttribute("attr4", "value5")));
		programRun.add(new Event("method2", EventType.EXIT, new EventAttribute("attr3", "value4"), new EventAttribute("attr4", "value6")));
	}

	@Test
	public void testDaikonFormatterHeader() {
		String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
		assertEquals(expectedHeader, formatter.header());
	}

	@Test
	public void testDaikonFormatterFooter() {
		assertEquals("", formatter.footer());
	}

	@Test
	public void testDaikonFormatterFileNameExtension() {
		assertEquals("dtrace", formatter.fileNameExtension());
	}

	@Test
	public void testDaikonFormatterObservationsSimple() {
		formatter = new DaikonFormatter("test.txt", true);
		String expectedObservations = "method1:::ENTER\nmethod1:::EXIT1\nmethod2:::ENTER\nmethod2:::EXIT1";
		assertEquals(expectedObservations, formatter.observations(programRun));
	}

	@Test
	public void testDaikonFormatterObservationsComplex() {
		String expectedObservations = "method1:::ENTER ppt method1 ppt-type enter ppt-kind enter var-kind variable dec-type java.lang.String rep-type java.lang.String comparability implicit\nmethod1:::EXIT1 ppt method1 ppt-type exit ppt-kind exit var-kind variable dec-type java.lang.String rep-type java.lang.String comparability implicit\nmethod1:::ENTER ppt method1 ppt-type enter ppt-kind enter var-kind variable dec-type java.lang.String rep-type java.lang.String comparability implicit\nmethod2:::ENTER ppt method2 ppt-type enter ppt-kind enter var-kind variable dec-type java.lang.String rep-type java.lang.String comparability implicit\nmethod2:::EXIT1 ppt method2 ppt-type exit ppt-kind exit var-kind variable dec-type java.lang.String rep-type java.lang.String comparability implicit";
		assertEquals(expectedObservations, formatter.observations(programRun));
	}

	@Test
	public void testDaikonFormatterDeclarationsSimple() {
		Event event = new Event("method1", EventType.ENTER, null, null);
		String expectedDeclarations = "ppt method1 ppt-type enter ppt-kind enter var-kind variable dec-type java.lang.String rep-type java.lang.String comparability implicit";
		formatter.simplePoint(event, "implicit", new OutputBuffer());
		assertEquals(expectedDeclarations, formatter.getDeclarations().toString());
		formatter.getDeclarations().clear();
	}

	@Test
	public void testDaikonFormatterDeclarationsComplex() {
		Event event = new Event("method1", EventType.ENTER, new EventAttribute("attr1", "value1"), new EventAttribute("attr2", "value2"));
		String expectedDeclarations = "ppt method1 ppt-type enter ppt-kind enter var-kind variable dec-type java.lang.String rep-type java.lang.String comparability implicit\nvariable attr1 comparability implicit dec-type java.lang.String rep-type java.lang.String\nvariable attr2 comparability implicit dec-type java.lang.String rep-type java.lang.String";
		formatter.point(event.getName(), event, "implicit", new OutputBuffer());
		assertEquals(expectedDeclarations, formatter.getDeclarations().toString());
		formatter.getDeclarations().clear();
	}

	@Test
	public void testDaikonFormatterAttributesSimple() {
		Event event = new Event("method1", EventType.ENTER, null, null);
		String expectedAttributes = "method1\"";
		formatter.simplePoint(event, "implicit", new OutputBuffer());
		String observations = formatter.getObservations();
		Iterator<String> observableLines = Arrays.asList(observations.split("\n")).iterator();
		String currentLine = observableLines.next();
		assertEquals(expectedAttributes, currentLine.substring(currentLine.lastIndexOf(":") + 1).trim());
	}

	@Test
	public void testDaikonFormatterAttributesComplex() {
		Event event = new Event("method1", EventType.ENTER, new EventAttribute("attr1", "value1"), new EventAttribute("attr2", "value2"));
		String expectedAttributes = "method1\"\"value1value21";
		formatter.point(event.getName(), event, "implicit", new OutputBuffer());
		String observations = formatter.getObservations();
		Iterator<String> observableLines = Arrays.asList(observations.split("\n")).iterator();
		String currentLine = observableLines.next();
		assertEquals(expectedAttributes, currentLine.substring(currentLine.lastIndexOf(":") + 1).trim());
	}

}