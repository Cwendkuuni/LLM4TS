package fi.vtt.noen.testgen.observations.formatter.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.observations.data.ProgramRun;
import fi.vtt.noen.testgen.parser.InputInterface;
import fi.vtt.noen.testgen.parser.OutputInterface;

public class DaikonFormatterTest {

 private static class TestDaikonFormatter extends DaikonFormatter {
  TestDaikonFormatter(String fileName, boolean simple) throws IOException { super(fileName, simple); }

  @Override
  public String header() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String footer() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String observations(ProgramRun run) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void simplePoint(Event event, String comparability, OutputBuffer out) {
    // Implement this method for testing purposes
  }

  @Override
  public void point(String name, Event event, String comparability, OutputBuffer out) {
    // Implement this method for testing purposes
  }

  @Override
  public void declarations(Event event, String eventName, OutputBuffer out, String comparability) {
    // Implement this method for testing purposes
  }

  @Override
  public void attributes(Event event, OutputBuffer out) {
    // Implement this method for testing purposes
  }
}

@Test
public void testDaikonFormatter() throws IOException, InterruptedException {
  // Given
  String fileName = "test.dtrace";
  boolean simple = false;
  DaikonFormatter formatter = new TestDaikonFormatter(fileName, simple);

  // Method under test
  String result = formatter.observations(createTestProgramRun());

  // Then
  String expectedResult = FileUtils.readFileToString(new File("expected_output.dtrace"), "UTF-8");
  assertEquals(expectedResult, result);
}

private ProgramRun createTestProgramRun() {
  List<Event> events = new ArrayList<>();
  InputInterface inputInterface = InterfaceParser.parse("InputInterface.class");
  OutputInterface outputInterface = InterfaceParser.parse("OutputInterface.class");

  events.add(new Event("start", Collections.emptyList(), false));
  events.add(new Event("method1", Arrays.asList(new EventAttribute("a", "java.lang.String", "value1")), false));
  events.add(new Event("method1", Arrays.asList(new EventAttribute("a", "java.lang.String", "value2")), true));
  events.add(new Event("method2", Collections.emptyList(), true));

  return new ProgramRun("test_program", "Program.class", events, inputInterface, outputInterface);
}
}