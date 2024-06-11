package corina.formats;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.*;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.toString();
        String expected = I18n.getText("format.tucsonsimple");
        assertEquals(expected, result);
    }

    @Test
    public void testgetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.getDefaultExtension();
        String expected = ".RWL";
        assertEquals(expected, result);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoadThrowsWrongFiletypeException() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("Invalid data");
        tucsonSimple.load(reader);
    }

    // Test cases for other methods will be added below

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta.put("id", "123456");
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.make6digitCode(sample);
        String expected = "123456";
        assertEquals(expected, result);

        Sample sample2 = new Sample();
        TucsonSimple tucsonSimple2 = new TucsonSimple();
        String result2 = tucsonSimple2.make6digitCode(sample2);
        String expected2 = "000000";
        assertEquals(expected2, result2);
    }

    // Test cases for other methods with edge cases and exceptions will be added below

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = new Sample();
        Year year = new Year(1995);
        String code = "123456";
        int yearWidth = 6;
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        String expected = "1234561995";
        verify(writer).write(expected);

        // Test cases for other scenarios will be added below
    }

    // Test cases for other methods with edge cases and exceptions will be added below

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = mock(BufferedWriter.class);
        Year start = new Year(1990);
        Year end = new Year(1995);
        String code = "123456";
        boolean isSummed = false;
        boolean isProcessed = false;
        Year y = start;
        tucsonSimple.saveData(sample, writer);

        String expected = "1234561990   ";
        verify(writer).write(expected);

        // Test cases for other scenarios will be added below
    }

    // Test cases for other methods with edge cases and exceptions will be added below

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Year start = new Year(2000);
        Range result = tucsonSimple.computeRange(sample);
        Range expected = sample.range;
        assertEquals(expected, result);

        Sample sample2 = new Sample();
        Year start2 = new Year(1899);
        Range result2 = tucsonSimple.computeRange(sample2);
        Range expected2 = sample2.range.redateStartTo(new Year(1001));
        assertEquals(expected2, result2);

        Sample sample3 = new Sample();
        Year start3 = new Year(8001);
        Range result3 = tucsonSimple.computeRange(sample3);
        Range expected3 = sample3.range.redateBy(8000);
        assertEquals(expected3, result3);
    }

    // Test cases for other methods with edge cases and exceptions will be added below

    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = mock(BufferedWriter.class);
        tucsonSimple.save(sample, writer);
        verify(writer, never()).write("9990  0");
        verify(writer, never()).write("9990   ");
        verify(writer, never()).write("999");
        verify(writer, never()).write("9990  999");
        verify(writer, never()).write("9990   ");
        verify(writer, never()).write("999");
        verify(writer, times(2)).write(StringUtils.LEFT_PAD);
        verify(writer, times(2)).newLine();
    }

    // Test cases for other methods with edge cases and exceptions will be added below

}