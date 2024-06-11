package corina.formats.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import corina.formats.*;
import corina.ui.*;
import java.io.*;
import corina.util.*;
import corina.*;

@RunWith(MockitoJUnitRunner.class)
public class TucsonSimpleTest {

    @Mock
    BufferedWriter writer;

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "format.tucsonsimple";
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }

    @Test
    public void testMake6DigitCodeWithId() {
        Sample sample = new Sample();
        sample.meta.put("id", "123456");
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testMake6DigitCodeWithoutId() {
        Sample sample = new Sample();
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testSaveDataIOException() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Range range = new Range(new Year(2001), new Year(2001));
        sample.range = range;
        BufferedWriter writer = mock(BufferedWriter.class);
        doThrow(IOException.class).when(writer).write(anyString());
        tucsonSimple.saveData(sample, writer);
    }

    @Test
    public void testSaveDataCorrectData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Range range = new Range(new Year(2001), new Year(2001));
        sample.range = range;
        String expectedCode = "123456";
        Year start = new Year(2001);
        Year end = new Year(2001);
        tucsonSimple.saveData(sample, writer);
        verify(writer).write(expectedCode + "2001");
    }

    @Test
    public void testSaveDataEdgeCases() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Range range = new Range(new Year(1900), new Year(1900));
        sample.range = range;
        String expectedCode = "000000";
        Year start = new Year(1900);
        Year end = new Year(1900);
        tucsonSimple.saveData(sample, writer);
        verify(writer).write(expectedCode + "1900");

        sample = new Sample();
        range = new Range(new Year(2001), new Year(2002));
        sample.range = range;
        expectedCode = "123456";
        start = new Year(2001);
        end = new Year(2002);
        tucsonSimple.saveData(sample, writer);
        verify(writer, times(2)).write(expectedCode + anyString());
    }
}