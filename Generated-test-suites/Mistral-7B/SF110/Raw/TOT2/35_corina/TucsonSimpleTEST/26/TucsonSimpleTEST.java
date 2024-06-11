import org.junit.Test;
import static org.junit.Assert.*;
import corina.formats.TucsonSimple;
import corina.ui.*;
import corina.util.*;
import corina.*;
import java.io.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.toString();
        String expected = I18n.getText("format.tucsonsimple");
        assertEquals(expected, result);
    }

    @Test
    public void testDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String result = tucsonSimple.getDefaultExtension();
        String expected = ".RWL";
        assertEquals(expected, result);
    }

    @Test
    public void testMake6digitCode() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.setMeta("id", "123456");

        String result = tucsonSimple.make6digitCode(sample);
        String expected = "123456";
        assertEquals(expected, result);

        Sample sampleNoId = new Sample();
        String resultNoId = tucsonSimple.make6digitCode(sampleNoId);
        String expectedNoId = "000000";
        assertEquals(expectedNoId, resultNoId);
    }

    @Test
    public void testSaveRowHeader() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.setRange(new Range(new Year(2020), new Year(2021)));
        String code = "001234";
        int yearWidth = 6;
        Year year = new Year(2020);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("temp.txt"), StandardCharsets.UTF_8))) {
            tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
            String result = new String(Files.readAllBytes(Paths.get("temp.txt")));
            String expected = "0012342020";
            assertEquals(expected, result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        yearWidth = 3;
        year = new Year(2019);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("temp.txt"), StandardCharsets.UTF_8))) {
            tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
            String result = new String(Files.readAllBytes(Paths.get("temp.txt")));
            String expected = "001234 19";
            assertEquals(expected, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveData() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.setData(new ArrayList<Double>() {{ add(1.0); add(2.0); add(3.0); }});
        sample.setRange(new Range(new Year(2015), new Year(2020)));

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("temp.txt"), StandardCharsets.UTF_8))) {
            tucsonSimple.saveData(sample, writer);
            String expected = "1.0  1  \n2.0  2  \n3.0  3  ";
            String result = new String(Files.readAllBytes(Paths.get("temp.txt")));
            assertEquals(expected, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.setRange(new Range(new Year(1990), new Year(1995)));

        Range result = tucsonSimple.computeRange(sample);
        Range expected = sample.getRange();
        assertEquals(expected, result);

        sample.setRange(new Range(new Year(2025), new Year(2030)));
        expected = new Range(new Year(2025), new Year(2030)).redateStartTo(new Year(2001));
        result = tucsonSimple.computeRange(sample);
        assertEquals(expected, result);
    }

    @Test
    public void testSave() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.setData(new ArrayList<Double>() {{ add(1.0); add(2.0); add(3.0); }});
        sample.setRange(new Range(new Year(2015), new Year(2020)));

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("temp.txt"), StandardCharsets.UTF_8))) {
            tucsonSimple.save(sample, writer);
            String expected = "1.0  1  \n2.0  2  \n3.0  3  ";
            String result = new String(Files.readAllBytes(Paths.get("temp.txt")));
            assertEquals(expected, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}