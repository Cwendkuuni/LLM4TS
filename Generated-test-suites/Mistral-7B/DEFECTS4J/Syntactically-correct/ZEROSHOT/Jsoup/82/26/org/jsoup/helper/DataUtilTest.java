package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private InputStream inputStream;
    private OutputStream outputStream;
    private Random random;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        inputStream = mock(InputStream.class);
        outputStream = mock(OutputStream.class);
        random = new Random();
    }

    @After
    public void tearDown() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a file content.";
        File file = new File("file.html");
        when(file.exists()).thenReturn(true);
        when(file.length()).thenReturn((long) fileContent.length());
        when(file.getAbsolutePath()).thenReturn("/path/to/file.html");
        when(file.isFile()).thenReturn(true);
        when(file.getCanonicalPath()).thenReturn("/path/to/file.html");

        // Test with default charset
        Document document = DataUtil.load(file, null);
        assertThat(document, notNullValue());
        assertThat(document.text(), is(fileContent));

        // Test with specified charset
        Charset charset = Charset.forName("UTF-16");
        Document documentWithCharset = DataUtil.load(file, charset.name(), "baseUri");
        assertThat(documentWithCharset, notNullValue());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is an input stream content.";
        when(inputStream.available()).thenReturn(inputStreamContent.length());
        when(inputStream.read(anyByteArray())).thenAnswer(i -> inputStreamContent.getBytes());

        Document document = DataUtil.load(inputStream, null, "baseUri");
        assertThat(document, notNullValue());
        assertThat(document.text(), is(inputStreamContent));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "<html><body><p>This is an input stream content.</p></body></html>";
        byte[] inputStreamBytes = inputStreamContent.getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputStreamBytes);

        Document document = DataUtil.parseInputStream(byteArrayInputStream, null, "baseUri", Parser.htmlParser());
        assertThat(document, notNullValue());
        assertThat(document.text(), is(inputStreamContent));

        // Test with specified charset
        Charset charset = Charset.forName("UTF-16");
        Document documentWithCharset = DataUtil.parseInputStream(byteArrayInputStream, charset.name(), "baseUri", Parser.htmlParser());
        assertThat(documentWithCharset, notNullValue());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputStreamContent = "This is an input stream content.";
        String outputStreamContent = "This is an output stream content.";

        when(inputStream.available()).thenReturn(inputStreamContent.length());
        when(inputStream.read(anyByteArray())).thenAnswer(i -> inputStreamContent.getBytes());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(outputStream.write(anyInt())).thenAnswer(i -> byteArrayOutputStream.write(i));

        DataUtil.crossStreams(inputStream, outputStream);
        assertThat(byteArrayOutputStream.toString(), is(outputStreamContent));
    }

    @Test
    public void testParseInputStreamWithContentType() throws IOException {
        String inputStreamContent = "Content-Type: text/html; charset=UTF-8\n" +
                "<html><body><p>This is an input stream content.</p></body></html>";
        byte[] inputStreamBytes = inputStreamContent.getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputStreamBytes);

        Document document = DataUtil.parseInputStream(byteArrayInputStream, "UTF-8", "baseUri", Parser.htmlParser());
        assertThat(document, notNullValue());
        assertThat(document.text(), is(inputStreamContent.substring(inputStreamContent.indexOf("\n<html>\n"))));
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithEmptyInputStream() throws IOException {
        InputStream emptyInputStream = DataUtil.emptyByteBuffer();
        DataUtil.parseInputStream(emptyInputStream, null, "baseUri", Parser.htmlParser());
    }

    @Test
    public void testReadToByteBuffer() {
        String inputStreamContent = "This is an input stream content.";
        byte[] inputStreamBytes = inputStreamContent.getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputStreamBytes);

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(byteArrayInputStream, 0);
        byteBuffer.flip();
        byte[] actualBytes = new byte[inputStreamContent.length()];
        byteBuffer.get(actualBytes);

        assertThat(actualBytes, is(inputStreamBytes));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertThat(charset, is("UTF-8"));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-16";
        String validatedCharset = DataUtil.validateCharset(charset);

        assertThat(validatedCharset, is("UTF-16"));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        assertThat(boundary, is(notNullValue()));
        assertThat(boundary.length(), is(32));
    }
}