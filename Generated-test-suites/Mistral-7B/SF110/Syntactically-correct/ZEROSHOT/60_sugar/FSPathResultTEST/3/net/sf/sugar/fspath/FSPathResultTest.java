import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

import net.sf.sugar.fspath.FSPathResult;

/**
 * Test class for FSPathResult.
 *
 * @author YourName
 */
public class FSPathResultTest {

    /**
     * Test constructor with File argument.
     */
    @Test
    public void testConstructorWithFileArgument() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        File expectedResult = file;
        assertEquals(expectedResult, result.getFile());
    }

    /**
     * Test constructor with String argument.
     */
    @Test
    public void testConstructorWithStringArgument() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        String expectedResult = string;
        assertEquals(expectedResult, result.getString());
    }

    /**
     * Test constructor with Double argument.
     */
    @Test
    public void testConstructorWithDoubleArgument() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        Double expectedResult = doubleValue;
        assertEquals(expectedResult, result.getDouble());
    }

    /**
     * Test constructor with Date argument.
     */
    @Test
    public void testConstructorWithDateArgument() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Date expectedResult = date;
        assertEquals(expectedResult, result.getDate());
    }

    /**
     * Test constructor with URI argument.
     */
    @Test
    public void testConstructorWithURIArgument() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        URI expectedResult = uri;
        assertEquals(expectedResult, result.getURI());
    }

    /**
     * Test constructor with Boolean argument.
     */
    @Test
    public void testConstructorWithBooleanArgument() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        Boolean expectedResult = booleanValue;
        assertEquals(expectedResult, result.getBoolean());
    }

    /**
     * Test toString method with File argument.
     */
    @Test
    public void testToStringWithFileArgument() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        String expectedResult = file.getAbsolutePath();
        String resultString = result.toString();
        assertEquals(expectedResult, resultString);
    }

    /**
     * Test toString method with String argument.
     */
    @Test
    public void testToStringWithStringArgument() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        String expectedResult = string;
        String resultString = result.toString();
        assertEquals(expectedResult, resultString);
    }

    /**
     * Test toString method with Double argument.
     */
    @Test
    public void testToStringWithDoubleArgument() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        String expectedResult = doubleValue.toString();
        String resultString = result.toString();
        assertEquals(expectedResult, resultString);
    }

    /**
     * Test toString method with Date argument.
     */
    @Test
    public void testToStringWithDateArgument() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        String expectedResult = date.toString();
        String resultString = result.toString();
        assertEquals(expectedResult, resultString);
    }

    /**
     * Test toString method with URI argument.
     */
    @Test
    public void testToStringWithURIArgument() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        String expectedResult = uri.toString();
        String resultString = result.toString();
        assertEquals(expectedResult, resultString);
    }

    /**
     * Test toString method with Boolean argument.
     */
    @Test
    public void testToStringWithBooleanArgument() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        String expectedResult = booleanValue.toString();
        String resultString = result.toString();
        assertEquals(expectedResult, resultString);
    }

    /**
     * Test getFile method.
     */
    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        File expectedResult = file;
        File resultFile = result.getFile();
        assertEquals(expectedResult, resultFile);
    }

    /**
     * Test getString method.
     */
    @Test
    public void testGetString() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        String expectedResult = string;
        String resultString = result.getString();
        assertEquals(expectedResult, resultString);
    }

    /**
     * Test getDouble method.
     */
    @Test
    public void testGetDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        Double expectedResult = doubleValue;
        Double resultDouble = result.getDouble();
        assertEquals(expectedResult, resultDouble);
    }

    /**
     * Test getDate method.
     */
    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Date expectedResult = date;
        Date resultDate = result.getDate();
        assertEquals(expectedResult, resultDate);
    }

    /**
     * Test getURI method.
     */
    @Test
    public void testGetURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        URI expectedResult = uri;
        URI resultURI = result.getURI();
        assertEquals(expectedResult, resultURI);
    }

    /**
     * Test getBoolean method.
     */
    @Test
    public void testGetBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        Boolean expectedResult = booleanValue;
        Boolean resultBoolean = result.getBoolean();
        assertEquals(expectedResult, resultBoolean);
    }
}