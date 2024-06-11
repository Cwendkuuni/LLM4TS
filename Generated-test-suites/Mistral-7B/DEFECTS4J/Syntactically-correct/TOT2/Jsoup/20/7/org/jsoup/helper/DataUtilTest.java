
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        // Given
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document document = DataUtil.load(inputFile, charsetName, baseUri);

        // Then
        Assertions.assertNotNull(document);

        // Given
        charsetName = null;

        // When & Then
        Assertions.assertThrows(IOException.class, () -> DataUtil.load(inputFile, charsetName, baseUri));

        // Given
        baseUri = null;

        // When & Then
        Assertions.assertThrows(NullPointerException.class, () -> DataUtil.load(inputFile, charsetName, baseUri));

        // Given
        inputFile = null;

        // When & Then
        Assertions.assertThrows(FileNotFoundException.class, () -> DataUtil.load(inputFile, charsetName, baseUri));

        // Given
        charsetName = "InvalidCharset";

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputFile, charsetName, baseUri));

        // Given
        baseUri = "http://example.com/";

        // Given
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());

        // When
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Then
        Assertions.assertNotNull(document);

        // Given
        charsetName = null;

        // When & Then
        Assertions.assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));

        // Given
        baseUri = null;

        // When & Then
        Assertions.assertThrows(NullPointerException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));

        // Given
        Document document = DataUtil.load(inputStream, null, baseUri);

        // Then
        Assertions.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";

        // When
        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Then
        Assertions.assertNotNull(document);

        // Given
        charsetName = null;

        // When & Then
        Assertions.assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));

        // Given
        baseUri = null;

        // When & Then
        Assertions.assertThrows(NullPointerException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));

        // Given
        Document document = DataUtil.load(inputStream, null, baseUri);

        // Then
        Assertions.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Parser parser = Parser.xmlParser();

        // When
        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);

        // Then
        Assertions.assertNotNull(document);

        // Given
        charsetName = null;

        // When & Then
        Assertions.assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri, parser));

        // Given
        baseUri = null;

        // When & Then
        Assertions.assertThrows(NullPointerException.class, () -> DataUtil.load(inputStream, charsetName, baseUri, parser));

        // Given
        Document document = DataUtil.load(inputStream, null, baseUri, parser);

        // Then
        Assertions.assertNotNull(document);
    }
}