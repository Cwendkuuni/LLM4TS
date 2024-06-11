package corina.formats;
import org.junit.Test;
import static org.junit.Assert.*;
import corina.formats.TucsonSimple;
import corina.ui.*;
import java.io.*;
import java.util.*;
import corina.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = "format.tucsonsimple";
        String result = tucsonSimple.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expected = ".RWL";
        String result = tucsonSimple.getDefaultExtension();
        assertEquals(expected, result);
    }

    // Add test cases for other methods with similar structure
}