package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.parser.ParserException;
import fi.vtt.noen.testgen.observations.data.Constants;

public class DaikonFormatterTest {

	private File testInputFile;
	private File testOutputFile;
	private DaikonFormatter formatter;
	private ProgramRun run;

	@Before
	public void setUp() throws IOException, ParserException {
		testInputFile = new File("testInput.txt");
		testOutputFile = new File("testOutput.dtrace");
		run = new ProgramRun("TestProgram");

		// Create test input data
		Event event1 = new Event("Event1");
		EventAttribute ea1 = new EventAttribute("StringAttribute1", "TestValue1");
		event1.addAttribute(ea1);
		Event event2 = new Event("Event2");
		EventAttribute ea2 = new EventAttribute("ArrayAttribute1", new int[] { 1, 2, 3 });
		event2.addAttribute(ea2);
		Event event3 = new Event("Event3");
		run.addEvent(event1);
		run.addEvent(event2);
		run.addEvent(event3);
	}

	@Test
	public void testDaikonFormatterConstructionWithFileNameAndSimpleMode() throws IOException {
		formatter = new DaikonFormatter(testInputFile.getPath(), true);
		assertNotNull(formatter);
	}

	@Test(expected = RuntimeException.class)
	public void testDaikonFormatterConstructionWithFileNameAndInputThrowsException() throws IOException {
		formatter = new DaikonFormatter(testInputFile.getPath(), null);
	}

	@Test(expected = RuntimeException.class)
	public void testDaikonFormatterConstructionWithFileNameAndInvalidInputsThrowsException() throws IOException {
		formatter = new DaikonFormatter(testInputFile.getPath(), Collections.singletonList("InvalidInput"));
	}

	@Test
	public void testDaikonFormatterConstructionWithFileNameAndInputs() throws IOException {
		HashSet<String> inputs = new HashSet<>(Arrays.asList("Event1", "Event2"));
		formatter = new DaikonFormatter(testInputFile.getPath(), inputs);
		assertNotNull(formatter);
		assertEquals(inputs, formatter.getInputs());
	}

	@Test
	public void testDaikonFormatterHeader() {
		formatter = new DaikonFormatter(testInputFile.getPath(), Arrays.asList("Event1", "Event2"));
		String header = formatter.header();
		assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
	}

	@Test
	public void testDaikonFormatterFooter() {
		formatter = new DaikonFormatter(testInputFile.getPath(), Arrays.asList("Event1", "Event2"));
		String footer = formatter.footer();
		assertEquals("", footer);
	}

	@Test
	public void testDaikonFormatterFileNameExtension() {
		formatter = new DaikonFormatter(testInputFile.getPath(), Arrays.asList("Event1", "Event2"));
		String fileNameExtension = formatter.fileNameExtension();
		assertEquals("dtrace", fileNameExtension);
	}

	@Test
	public void testDaikonFormatterObservationsSimplePoint() throws IOException {
		formatter = new DaikonFormatter(testInputFile.getPath(), true);
		String observations = formatter.observations(run);
		assertNotNull(observations);
		assertEquals("ppt Event1:::ENTER\n", observations.substring(0, observations.indexOf("\n") + 1));
		assertEquals("ppt Event3:::EXIT1\n", observations.substring(observations.lastIndexOf("\n") + 1));
	}

	@Test
	public void testDaikonFormatterObservationsSimplePointWithoutExitEvent() throws IOException {
		run.removeEvent(run.getEvent(1));
		formatter = new DaikonFormatter(testInputFile.getPath(), true);
		String observations = formatter.observations(run);
		assertNotNull(observations);
		assertEquals("ppt Event1:::ENTER\n", observations.substring(0, observations.indexOf("\n") + 1));
	}

	@Test
	public void testDaikonFormatterObservationsPoint() throws IOException {
		formatter = new DaikonFormatter(testInputFile.getPath(), Arrays.asList("Event1", "Event2"));
		String observations = formatter.observations(run);
		assertNotNull(observations);
		assertEquals("ppt Event1:::ENTER\n", observations.substring(0, observations.indexOf("\n") + 1));
		assertEquals("ppt Event1:::ENTER\n", observations.substring(observations.indexOf("ppt Event1:::ENTER\n") + 1, observations.indexOf("ppt Event2:::ENTER\n")).trim());
		assertEquals("ppt Event2:::ENTER\n", observations.substring(observations.indexOf("ppt Event2:::ENTER\n")).trim());
		assertEquals("ppt Event3:::EXIT1\n", observations.substring(observations.lastIndexOf("\n") + 1));
	}

	@Test
	public void testDaikonFormatterObservationsPointWithMultipleExits() throws IOException {
		Event event4 = new Event("Event4");
		run.addEvent(event4);
		formatter = new DaikonFormatter(testInputFile.getPath(), Arrays.asList("Event1", "Event2"));
		String observations = formatter.observations(run);
		assertNotNull(observations);
		assertEquals("ppt Event1:::ENTER\n", observations.substring(0, observations.indexOf("\n") + 1));
		assertEquals("ppt Event1:::ENTER\n", observations.substring(observations.indexOf("ppt Event1:::ENTER\n") + 1, observations.indexOf("ppt Event2:::ENTER\n")).trim());
		assertEquals("ppt Event2:::ENTER\n", observations.substring(observations.indexOf("ppt Event2:::ENTER\n")).trim());
		assertEquals("ppt Event3:::EXIT1\n", observations.substring(observations.lastIndexOf("\n") + 1));
		assertEquals("ppt Event4:::EXIT1\n", observations.substring(observations.lastIndexOf("\n") + 1));
	}

	@Test
	public void testDaikonFormatterDeclarations() throws IOException {
		formatter = new DaikonFormatter(testInputFile.getPath(), Arrays.asList("Event1", "Event2"));
		String observations = formatter.observations(run);
		assertNotNull(observations);
		assertEquals("ppt Event1:::ENTER\n", observations.substring(0, observations.indexOf("\n") + 1));
		assertEquals("ppt Event1:::ENTER\n", observations.substring(observations.indexOf("ppt Event1:::ENTER\n") + 1, observations.indexOf("ppt Event2:::ENTER\n")).trim());
		assertEquals("ppt Event2:::ENTER\n", observations.substring(observations.indexOf("ppt Event2:::ENTER\n")).trim());
		assertEquals("ppt Event3:::EXIT1\n", observations.substring(observations.lastIndexOf("\n") + 1));

		String expectedDeclarations = "ppt Event1\n" +
				"ppt Event2\n" +
				"ppt Event3\n";

		String actualDeclarations = observations.substring(0, observations.indexOf("ppt Event1:::ENTER\n")).trim();
		assertEquals(expectedDeclarations, actualDeclarations);
	}

	@Test
	public void testDaikonFormatterAttributesSimplePoint() throws IOException {
		formatter = new DaikonFormatter(testInputFile.getPath(), true);
		String observations = formatter.observations(run);
		assertNotNull(observations);
		assertEquals("", observations.substring(observations.indexOf("ppt Event1:::ENTER\n") + 1, observations.indexOf("ppt Event1:::ENTER\n") + 2).trim());
		assertEquals("TestValue11", observations.substring(observations.indexOf("ppt Event1:::ENTER\n") + 1, observations.indexOf("ppt Event3:::EXIT1\n")).trim());
		assertEquals("", observations.substring(observations.lastIndexOf("\n") + 1).trim());
	}

	@Test
	public void testDaikonFormatterAttributesPoint() throws IOException {
		formatter = new DaikonFormatter(testInputFile.getPath(), Arrays.asList("Event1", "Event2"));
		String observations = formatter.observations(run);
		assertNotNull(observations);
		assertEquals("", observations.substring(observations.indexOf("ppt Event1:::ENTER\n") + 1, observations.indexOf("ppt Event1:::ENTER\n") + 2).trim());
		assertEquals("TestValue11\nTestValue11\n1\n", observations.substring(observations.indexOf("ppt Event1:::ENTER\n") + 1, observations.indexOf("ppt Event2:::ENTER\n")).trim());
		assertEquals("1\n3\n1\n", observations.substring(observations.indexOf("ppt Event2:::ENTER\n")).trim());
		assertEquals("", observations.substring(observations.lastIndexOf("\n") + 1).trim());
	}

	@Test
	public void testDaikonFormatterAttributesPointWithArrayAttribute() throws IOException {
		formatter = new DaikonFormatter(testInputFile.getPath(), Arrays.asList("Event1", "Event2"));
		String observations = formatter.observations(run);
		assertNotNull(observations);
		assertEquals("", observations.substring(observations.indexOf("ppt Event1:::ENTER\n") + 1, observations.indexOf("ppt Event1:::ENTER\n") + 2).trim());
		assertEquals("TestValue11\nTestValue11\n1\n", observations.substring(observations.indexOf("ppt Event1:::ENTER\n") + 1, observations.indexOf("ppt Event2:::ENTER\n")).trim());
		assertEquals("1\n[1, 2, 3]\n1\n", observations.substring(observations.indexOf("ppt Event2:::ENTER\n")).trim());
		assertEquals("", observations.substring(observations.lastIndexOf("\n") + 1).trim());
	}

	@Test
	public void testDaikonFormatterClassesForMultipleProperties() throws IOException, ParserException {
		List<Class> classes = DaikonFormatter.outputs();
		assertEquals(2, classes.size());
		assertEquals("fi.vtt.noen.testgen.model.OutputInterface1", classes.get(0).getName());
		assertEquals("fi.vtt.noen.testgen.model.OutputInterface2", classes.get(1).getName());
	}
}