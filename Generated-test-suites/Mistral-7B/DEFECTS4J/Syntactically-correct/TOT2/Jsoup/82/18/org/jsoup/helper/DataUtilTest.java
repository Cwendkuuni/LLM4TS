import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {
    private Document doc;
    private InputStream input;

    @Before
    public void setup() {
        doc = new Document();
        input = mock(InputStream.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "/test";

        DataUtil.load(file, charsetName, baseUri); // typical use case

        // edge cases
        DataUtil.load(null, charsetName, baseUri); // null file
        DataUtil.load(file, null, baseUri); // null charsetName
        DataUtil.load(file, charsetName, null); // null baseUri
        DataUtil.load(file, charsetName, baseUri, null); // null parser

        // exception handling
        expect(input.read()).andThrow(new IOException("Mock IO error"));
        DataUtil.load(input, charsetName, baseUri); // IO error
        expect(input.read()).andThrow(new IllegalArgumentException("Mock IllegalArgument error"));
        DataUtil.load(input, charsetName, baseUri); // IllegalArgument error
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"></head><body></body></html>".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Parser parser = Parser.htmlParser();

        DataUtil.parseInputStream(input, charsetName, baseUri, parser); // typical use case

        // edge cases
        DataUtil.parseInputStream(null, charsetName, baseUri, parser); // null input
        DataUtil.parseInputStream(input, null, baseUri, parser); // null charsetName
        DataUtil.parseInputStream(input, charsetName, null, parser); // null baseUri
        DataUtil.parseInputStream(input, charsetName, baseUri, null); // null parser

        // exception handling
        input.mark(1024);
        input.read(); // read one byte, marking the position
        expect(input.read()).andThrow(new IOException("Mock IO error"));
        DataUtil.parseInputStream(input, charsetName, baseUri, parser); // IO error
        expect(input.read()).andThrow(new IllegalArgumentException("Mock IllegalArgument error"));
        DataUtil.parseInputStream(input, charsetName, baseUri, parser); // IllegalArgument error
    }

    // Add test cases for other methods in the DataUtil class

    

    // ... test code here

    
}