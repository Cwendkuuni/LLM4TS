package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LagoonCLITest {

    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String result = lagoonCLI.getProperty("targetURL");
        assertEquals("http://example.com", result);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyException() {
        Properties properties = new Properties();
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.getProperty("nonExistentProperty");
    }

    @Test
    public void testShowTimeShort() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        long time = 5000L;
        lagoonCLI.showTime(time);
        String expected = "in 5 ms";
        assertTrue(lagoonCLI.getClass().getName() + ".showTime(\"" + time + "\") returns \"" + expected + "\"", expected.equals(lagoonCLI.showTime(time).trim()));
    }

    @Test
    public void testShowTimeLong() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        long time = 7000L;
        lagoonCLI.showTime(time);
        String expected = "in 7 s";
        assertTrue(lagoonCLI.getClass().getName() + ".showTime(\"" + time + "\") returns \"" + expected + "\"", expected.equals(lagoonCLI.showTime(time).trim()));
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFoundException() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        File file = new File("nonExistentFile.xml");
        lagoonCLI.parseXML(file, false);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        LagoonCLI lagoonCLI = new LagoonCLI();
        File file = new File("inputStreamException.txt");
        InputSource inputSource = new InputSource(new FileInputStream(file));
        lagoonCLI.parseXML(inputSource, false);
    }

    @Test(expected = SAXException.class)
    public void testSAXException() throws SAXException, IOException {
        LagoonCLI lagoonCLI = new LagoonCLI();
        File file = new File("saxException.xml");
        InputSource inputSource = new InputSource(new FileInputStream(file));
        lagoonCLI.parseXML(inputSource, false);
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testAuthenticationMissingException() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.initialize();
    }

    @Test(expected = AuthenticationException.class)
    public void testAuthenticationException() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.initialize();
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFoundExceptionInitialization() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(new String[]{"nonExistentPropertyFile.xml"});
    }

    @Test(expected = NumberFormatException.class)
    public void testNumberFormatException() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(new String[]{"propertyFile.xml", "-1"});
    }

    @Test(expected = LagoonException.class)
    public void testLagoonException() {
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(new String[]{"propertyFile.xml", "invalidInterval"});
    }
}