import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest {

    /**
     * Test createValue method for String type.
     */
    @Test
    public void testCreateValueString() {
        String value = "test";
        Object result = TypeHandler.createValue(value, String.class);
        Assert.assertEquals(value, result);
    }

    /**
     * Test createValue method for Object type.
     */
    @Test
    public void testCreateValueObject() {
        String className = "java.lang.String";
        Object result = TypeHandler.createValue("TestString", Class.forName(className));
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof String);
    }

    /**
     * Test createValue method for Number type.
     */
    @Test
    public void testCreateValueNumber() {
        String value = "123";
        Number result = (Number) TypeHandler.createValue(value, Number.class);
        Assert.assertEquals(123, result.intValue());

        value = "456.78";
        result = (Number) TypeHandler.createValue(value, Double.class);
        Assert.assertEquals(456.78, result.doubleValue(), 0.01);
    }

    /**
     * Test createValue method for Date type.
     */
    @Test
    public void testCreateValueDate() {
        String value = "2022-12-31";
        Date result = (Date) TypeHandler.createValue(value, Date.class);
        Assert.assertNotNull(result);
        Assert.assertEquals("Fri Dec 31 00:00:00 CET 2022", result.toString());
    }

    /**
     * Test createValue method for Class type.
     */
    @Test
    public void testCreateValueClass() {
        String className = "java.lang.String";
        Class result = (Class) TypeHandler.createValue(className, Class.class);
        Assert.assertEquals(Class.forName(className), result);
    }

    /**
     * Test createValue method for File type.
     */
    @Test
    public void testCreateValueFile() {
        String path = "/test/TestFile.txt";
        File result = (File) TypeHandler.createValue(path, File.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(path, result.getAbsolutePath());
    }

    /**
     * Test createValue method for existing File type.
     */
    @Test
    public void testCreateValueExistingFile() {
        String path = "/test/TestFile.txt";
        File file = new File(path);
        Assert.assertTrue(file.exists());
        File result = (File) TypeHandler.createValue(path, File.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(file.getAbsolutePath(), result.getAbsolutePath());
    }

    /**
     * Test createValue method for URL type.
     */
    @Test
    public void testCreateValueURL() {
        String urlString = "http://www.example.com";
        URL result = (URL) TypeHandler.createValue(urlString, URL.class);
        Assert.assertNotNull(result);
        Assert.assertEquals("http://www.example.com", result.toString());
    }

    /**
     * Test createValue method for Array of Files type.
     */
    @Test
    public void testCreateValueFiles() {
        String paths = "/test/TestFile1.txt,/test/TestFile2.txt";
        String[] pathArray = paths.split(",");
        File[] result = (File[]) TypeHandler.createValue(paths, File[].class);
        Assert.assertNotNull(result);
        Assert.assertArrayEquals(pathArray.length, result.length);
        for (int i = 0; i < result.length; i++) {
            Assert.assertNotNull(result[i]);
            Assert.assertEquals(pathArray[i], result[i].getAbsolutePath());
        }
    }

    /**
     * Test createValue method for invalid types.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueInvalidTypes() {
        String value = "test";
        TypeHandler.createValue(value, int.class);
    }

    /**
     * Test createValue method with null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateValueNull() {
        String value = null;
        TypeHandler.createValue(value, String.class);
    }

    /**
     * Test parse command line options with valid values.
     * @throws ParseException
     */
    @Test
    public void testParseCommandLineOptionsValidValues() throws ParseException {
        Options options = new Options();
        Option testOption = Option.builder("t")
                .longOpt("test")
                .hasArg(true)
                .desc("Test option")
                .build();
        options.addOption(testOption);

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-t", "testValue"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertNotNull(cmd);
        Assert.assertEquals("testValue", cmd.getOptionValue("test"));
    }

    /**
     * Test parse command line options with invalid values.
     * @throws ParseException
     */
    @Test(expected = ParseException.class)
    public void testParseCommandLineOptionsInvalidValues() throws ParseException {
        Options options = new Options();
        Option testOption = Option.builder("t")
                .longOpt("test")
                .hasArg(true)
                .desc("Test option")
                .build();
        options.addOption(testOption);

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-t", "invalidValue"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(1, cmd.getParseErrors().size());
        Assert.assertEquals("Invalid value for option: 'test' - invalidValue",
                cmd.getParseErrors().get(0).getMessage());
    }
}