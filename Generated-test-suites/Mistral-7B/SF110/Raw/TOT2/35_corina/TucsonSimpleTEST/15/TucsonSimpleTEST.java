import org.junit.Test;
import static org.junit.Assert.*;
import corina.formats.TucsonSimple;
import corina.ui.*;
import java.io.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {
    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = I18n.getText("format.tucsonsimple");
        String actual = tucsonSimple.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String actual = tucsonSimple.getDefaultExtension();
        assertEquals(expected, actual);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoadNull() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader nullReader = null;
        tucsonSimple.load(nullReader);
    }

    @Test(expected = IOException.class)
    public void testMake6digitCodeNull() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample nullSample = null;
        String actual = tucsonSimple.make6digitCode(nullSample);
    }

    @Test
    public void testMake6digitCodeSampleWithID() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "sampleId");
        String expected = "sampleId";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testMake6digitCodeSampleWithoutID() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    // Continue writing test cases for other methods...
}