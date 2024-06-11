import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

public class ExtendedBufferedReaderTest {
    @Test
    public void testReadMethod() throws IOException {
        String inputText = "Hello, World!";
        Reader reader = new StringReader(inputText);
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        // Test reading characters
        char[] expectedCharArray = {'H', 'e', 'l', 'l', 'o', ','};
        char[] actualCharArray = new char[expectedCharArray.length];
        int readChar;

        for (int i = 0; i < expectedCharArray.length; i++) {
            readChar = extendedReader.read();
            actualCharArray[i] = (char) readChar;
            Assert.assertEquals(expectedCharArray[i], actualCharArray[i]);
        }

        // Test end of stream
        readChar = extendedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, readChar);
    }

    // Other test cases for other methods can be added here
}