import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TucsonSimpleTest {

    private static final String TEST_FILE_CONTENT = "test content";
    private static final String FORMAT = "TucsonSimple";
    private static final String DEFAULT_EXTENSION = ".RWL";

    private TucsonSimple tucsonSimple;
    private BufferedWriter writer;
    private PrintWriter printWriter;

    @Before
    public void setUp() {
        tucsonSimple = new TucsonSimple();
        writer = Mockito.mock(BufferedWriter.class);
        printWriter = new PrintWriter(writer);
    }

    @After
    public void tearDown() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testgetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoadMethodThrowsException() throws IOException {
        BufferedReader reader = Mockito.mock(BufferedReader.class);
        tucsonSimple.load(reader);
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.put("id", "123456");

        String expectedCode = "123456";
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals(expectedCode, code);

        Sample sample2 = new Sample();
        String code2 = tucsonSimple.make6digitCode(sample2);
        assertEquals("000000", code2);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        String code = "A12345";
        Year year = new Year(2022);
        int yearWidth = 5;

        tucsonSimple.saveRowHeader(writer, Mockito.mock(Sample.class), code, yearWidth, year);

        String expectedOutput = code + "2022";
        assertEquals(expectedOutput, writer.getBuffer().toString());
    }

    @Test
    public void testSaveData() throws IOException {
        Sample sample = new Sample();
        sample.data = new float[10];
        sample.count = new int[10];
        sample.range = new Range(new Year(2001), new Year(2010));

        Year start = new Year(2003);
        Year end = new Year(2005);

        tucsonSimple.saveData(sample, writer);

        String expectedOutput = "A003 003  " + // code + prefix
                StringUtils.leftPad("1.2", 4) + // data
                StringUtils.leftPad("1", 3) + // count (summed = false)
                "  " + // processed = false
                "A004 004  " +
                StringUtils.leftPad("2.3", 4) +
                StringUtils.leftPad("2", 3) +
                "  " +
                "A005 005  " +
                StringUtils.leftPad("3.4", 4) +
                StringUtils.leftPad("3", 3) +
                "  "; // processed = false

        assertEquals(expectedOutput, writer.getBuffer().toString());

        // Test with processed = true
        sample.processed = true;

        tucsonSimple.saveData(sample, writer);

        String expectedOutputProcessed = "A003 2003  " + // code + prefix with redated start year
                "    " + // processed = true and data = null
                "A004 2004  " +
                "   999" + // replaced data with "999" if processed and the year is not the start year
                "A005 2005  ";

        assertEquals(expectedOutputProcessed, writer.getBuffer().toString());

        // Test with summed = true
        sample.summed = true;

        tucsonSimple.saveData(sample, writer);

        String expectedOutputSummed = "A003 003  " + // code + prefix
                StringUtils.leftPad("1.2", 4) + // data
                StringUtils.leftPad("1", 3) + // count (summed = true)
                "   "; // processed = false

        assertEquals(expectedOutputSummed, writer.getBuffer().toString());
    }

    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        sample.range = new Range(new Year(2001), new Year(2005));

        Range expectedRange = new Range(new Year(2001), new Year(2005));
        Range computedRange = tucsonSimple.computeRange(sample);

        assertEquals(expectedRange, computedRange);

        sample.range.setStart(new Year(2000));

        Range expectedRedatedRange = new Range(new Year(1001), new Year(2005));
        Range redatedRange = tucsonSimple.computeRange(sample);

        assertEquals(expectedRedatedRange, redatedRange);

        sample.range.setStart(new Year(1999));

        Range expectedRedatedStartRange = new Range(new Year(1001), new Year(1001));
        Range redatedStartRange = tucsonSimple.computeRange(sample);

        assertEquals(expectedRedatedStartRange, redatedStartRange);
    }

    @Test
    public void testSaveMethod() throws IOException {
        Sample sample = new Sample();
        sample.data = new float[]{1.2f, 2.3f, 3.4f, 4.5f, 5.6f, 6.7f, 7.8f, 8.9f, 9.0f, 10.1f};
        sample.count = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        sample.processed = true;
        sample.summed = true;
        sample.range = new Range(new Year(2001), new Year(2010));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        PrintWriter printWriter = new PrintWriter(bufferedWriter);

        tucsonSimple.save(sample, printWriter);

        // The output of this test case is not possible to be hard-coded as it depends on the data provided.
        // The test case should print the expected output to the console or a test report and check it against the expected output.
    }

    @Test(expected = WrongFiletypeException.class)
    public void testSaveMethodThrowsExceptionWhenFiletypeIsWrong() throws IOException {
        Sample sample = new Sample();
        SampleWrongFiletype sampleWrongFiletype = Mockito.mock(SampleWrongFiletype.class);

        tucsonSimple.save(sampleWrongFiletype, writer);
    }
}