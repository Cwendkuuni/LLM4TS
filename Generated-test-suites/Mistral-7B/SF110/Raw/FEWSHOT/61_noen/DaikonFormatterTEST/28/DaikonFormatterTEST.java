package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterConstruction() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        assertNotNull(formatter);

        DaikonFormatter formatterWithInputs = new DaikonFormatter("test.dtrace", Arrays.asList("input1", "input2"));
        assertNotNull(formatterWithInputs);
    }

    @Test
    public void testClassForProperty() throws Exception {
        Properties configuration = new Properties();
        configuration.load(new FileInputStream("testgen.properties"));
        String className = configuration.getProperty("InputInterface1");
        Class<?> clazz = Class.forName(className);
        assertNotNull(clazz);
    }

    @Test(expected = Exception.class)
    public void testClassForPropertyFailure() throws Exception {
        Properties configuration = new Properties();
        configuration.load(new FileInputStream("testgen.properties"));
        String className = configuration.getProperty("NonExistentClass");
        Class<?> clazz = Class.forName(className);
        assertNull(clazz);
    }

    @Test
    public void testClassesForMultipleProperties() throws Exception {
        Properties configuration = new Properties();
        configuration.load(new FileInputStream("testgen.properties"));
        List<Class<?>> classes = DaikonFormatter.classesForMultipleProperties("InputInterface");
        assertEquals(2, classes.size());
        assertTrue(classes.contains(Class.forName(configuration.getProperty("InputInterface1"))));
        assertTrue(classes.contains(Class.forName(configuration.getProperty("InputInterface2"))));
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        String header = formatter.header();
        assertEquals("decl-version 2.0\nvar-comparability implicit\n", header);
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        String footer = formatter.footer();
        assertEquals("", footer);
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testObservationsSimple() throws Exception {
        Properties configuration = new Properties();
        configuration.load(new FileInputStream("testgen.properties"));
        Class<? extends TestEntity> inputInterface = Class.forName(configuration.getProperty("InputInterface1"));
        Class<? extends TestEntity> outputInterface = Class.forName(configuration.getProperty("OutputInterface1"));

        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ProgramRun run = new ProgramRun();
        Event event1 = new TestEvent("test1", inputInterface.newInstance(), outputInterface.newInstance(), true);
        Event event2 = new TestEvent("test2", inputInterface.newInstance(), outputInterface.newInstance(), false);
        run.addEvent(event1);
        run.addEvent(event2);

        formatter.observations(run, outputStream);
        String observations = new String(outputStream.toByteArray());
        assertEquals("ppt test1\n" +
                "ppt-type enter\n" +
                "ppt test1:::ENTER\n" +
                "variable test1\n" +
                "var-kind variable\n" +
                "dec-type java.lang.Object\n" +
                "rep-type java.lang.Object\n" +
                "comparability implicit\n" +
                "variable test1[]\n" +
                "var-kind array\n" +
                "dec-type [Ljava.lang.Object;\n" +
                "rep-type [Ljava.lang.Object;", observations.substring(0, 100));
    }

    @Test
    public void testObservationsComplex() throws Exception {
        Properties configuration = new Properties();
        configuration.load(new FileInputStream("testgen.properties"));
        Class<? extends TestEntity> inputInterface = Class.forName(configuration.getProperty("InputInterface1"));
        Class<? extends TestEntity> outputInterface = Class.forName(configuration.getProperty("OutputInterface1"));

        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", Arrays.asList("input1", "input2"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ProgramRun run = new ProgramRun();

        Event event1 = new TestEvent("input1:::ENTER", inputInterface.newInstance(), outputInterface.newInstance(), false);
        Event event2 = new TestEvent("input1:::EXIT1", inputInterface.newInstance(), outputInterface.newInstance(), true);
        Event event3 = new TestEvent("output1:::ENTER", inputInterface.newInstance(), outputInterface.newInstance(), false);
        Event event4 = new TestEvent("output1:::EXIT1", inputInterface.newInstance(), outputInterface.newInstance(), true);
        Event event5 = new TestEvent("input2:::ENTER", inputInterface.newInstance(), outputInterface.newInstance(), false);
        Event event6 = new TestEvent("input2:::EXIT1", inputInterface.newInstance(), outputInterface.newInstance(), true);

        run.addEvent(event1);
        run.addEvent(event2);
        run.addEvent(event3);
        run.addEvent(event4);
        run.addEvent(event5);
        run.addEvent(event6);

        formatter.observations(run, outputStream);
        String observations = new String(outputStream.toByteArray());

        List<String> expectedLines = Arrays.asList(
                "ppt input1\n" +
                        "ppt-type enter\n" +
                        "ppt input1:::ENTER\n" +
                        "variable input1\n" +
                        "var-kind variable\n" +
                        "dec-type java.lang.Object\n" +
                        "rep-type java.lang.Object\n" +
                        "comparability implicit\n" +
                        "variable input1[]\n" +
                        "var-kind array\n" +
                        "dec-type [Ljava.lang.Object;",
                "ppt input1:::EXIT1\n" +
                        "ppt-type exit\n" +
                        "ppt input1:::EXIT1\n" +
                        "variable input1\n" +
                        "var-kind variable\n" +
                        "dec-type java.lang.Object\n" +
                        "rep-type java.lang.Object\n" +
                        "comparability implicit\n" +
                        "variable input1[]\n" +
                        "var-kind array\n" +
                        "dec-type [Ljava.lang.Object;",
                "ppt output1\n" +
                        "ppt-type enter\n" +
                        "ppt output1:::ENTER\n" +
                        "variable output1\n" +
                        "var-kind variable\n" +
                        "dec-type java.lang.Object\n" +
                        "rep-type java.lang.Object\n" +
                        "comparability implicit\n" +
                        "variable output1[]\n" +
                        "var-kind array\n" +
                        "dec-type [Ljava.lang.Object;",
                "ppt output1:::EXIT1\n" +
                        "ppt-type exit\n" +
                        "ppt output1:::EXIT1\n" +
                        "variable output1\n" +
                        "var-kind variable\n" +
                        "dec-type java.lang.Object\n" +
                        "rep-type java.lang.Object\n" +
                        "comparability implicit\n" +
                        "variable output1[]\n" +
                        "var-kind array\n" +
                        "dec-type [Ljava.lang.Object;",
                "ppt input2\n" +
                        "ppt-type enter\n" +
                        "ppt input2:::ENTER\n" +
                        "variable input2\n" +
                        "var-kind variable\n" +
                        "dec-type java.lang.Object\n" +
                        "rep-type java.lang.Object\n" +
                        "comparability implicit\n" +
                        "variable input2[]\n" +
                        "var-kind array\n" +
                        "dec-type [Ljava.lang.Object;",
                "ppt input2:::EXIT1\n" +
                        "ppt-type exit\n" +
                        "ppt input2:::EXIT1\n" +
                        "variable input2\n" +
                        "var-kind variable\n" +
                        "dec-type java.lang.Object\n" +
                        "rep-type java.lang.Object\n" +
                        "comparability implicit\n" +
                        "variable input2[]\n" +
                        "var-kind array\n" +
                        "dec-type [Ljava.lang.Object;"
        );

        for (int i = 0; i < expectedLines.size(); i++) {
            String expectedLine = expectedLines.get(i);
            String actualLine = observations.substring(0, observations.indexOf("\n", observations.indexOf(expectedLine) + expectedLine.length())).trim();
            assertEquals(expectedLine, actualLine);
        }
    }

    private static class TestEntity {
        String name;

        public TestEntity() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class TestEvent extends Event {
        TestEntity entity;

        public TestEvent(String name, TestEntity entity, TestEntity output, boolean exit) {
            super(name, entity, output, exit);
            this.entity = entity;
        }

        @Override
        public EventAttribute[] getAttributes() {
            return new EventAttribute[0];
        }
    }

    private static class ProgramRun {
        Set<Event> events = new HashSet<>();

        void addEvent(Event event) {
            events.add(event);
        }

        Iterator<Event> iterator() {
            return events.iterator();
        }
    }

    private static class Event {
        String name;
        Event entity;
        Event output;
        boolean isExit;

        public Event(String name, Event entity, Event output, boolean exit) {
            this.name = name;
            this.entity = entity;
            this.output = output;
            this.isExit = exit;
        }

        @Override
        public String toString() {
            return "Event{" +
                    "name='" + name + '\'' +
                    ", entity=" + entity +
                    ", output=" + output +
                    ", isExit=" + isExit +
                    '}';
        }
    }

    private static class OutputBuffer {
        private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        void append(String str) throws IOException {
            buffer.append(str);
        }

        String toString() throws IOException {
            return buffer.toString();
        }
    }
}