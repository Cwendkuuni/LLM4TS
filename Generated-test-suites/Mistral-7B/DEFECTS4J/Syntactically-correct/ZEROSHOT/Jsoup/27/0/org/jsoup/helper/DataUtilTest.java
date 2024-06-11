package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class DataUtilTest {

 private static final String TEST_FILE = "test.html";
 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "http://example.com";

 @BeforeClass
 public static void setup() {
 File testFile = new File(DataUtilTest.class.getResource(TEST_FILE).getFile());
 DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
 }

 @Parameters(name = "{index}: testDocument{0}")
 public static Collection<Object[]> testDocuments() {
 return Arrays.asList(new Object[][]{
 // Add test cases here, each test case should be a pair of a byte array and a charset
 new Object[]{new byte[0], TEST_CHARSET},
 new Object[]{new ByteArrayInputStream(new byte[]{}), TEST_CHARSET},
 new Object[]{new ByteArrayInputStream(new byte[]{'<', 'h', 't', 'm', 'l'}), TEST_CHARSET},
 new Object[]{new ByteArrayInputStream(new byte[]{'<', 'h', 't', 'm', 'l', ' ', 'c', 'h', 'a', 'r', 's', 'e', 't', '=', 'U', 'T', 'F', '-', '8', ' ', 't', 'e', 'x', 't', '/', 'h', 't', 'm', 'l', '>', ''}), TEST_CHARSET},
 new Object[]{new File("test.html"), TEST_CHARSET},
 new Object[]{new File("test.html"), null},
 new Object[]{null, TEST_CHARSET}
 });
 }

 @Test
 public void testLoad(Object document, String charset) throws IOException {
 if (document instanceof File) {
 File file = (File) document;
 Document loadedDocument = DataUtil.load(file, charset, TEST_BASE_URI);
 assertNotNull(loadedDocument);
 } else if (document instanceof InputStream) {
 InputStream inputStream = (InputStream) document;
 Document loadedDocument = DataUtil.load(inputStream, charset, TEST_BASE_URI);
 assertNotNull(loadedDocument);
 } else if (document instanceof byte[]) {
 byte[] bytes = (byte[]) document;
 ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
 Document loadedDocument = DataUtil.load(inputStream, charset, TEST_BASE_URI);
 assertNotNull(loadedDocument);
 } else {
 throw new IllegalArgumentException("Invalid document type: " + document.getClass().getName());
 }
 // Add assertions here to test the Document loaded
 }
}