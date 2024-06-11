package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class AbstractLoaderTest extends DefaultHandler {

 private static final String VALID_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root/>";
 private static final String INVALID_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root><tag></tag>";

 private Map<String, String> publicIdToResourceNameMap;
 private AbstractLoader abstractLoader;

 @Test
 public void testAbstractLoaderWithValidPublicIdAndResourceName() throws Exception {
 final String publicId = "test Public Id";
 final String resourceName = "test.dtd";
 final AbstractLoader abstractLoader = new AbstractLoader(publicId, resourceName);

 final ByteArrayInputStream expectedInputStream = new ByteArrayInputStream(VALID_XML.getBytes());
 final InputSource expectedInputSource = new InputSource(expectedInputStream);

 final InputSource actualInputSource = abstractLoader.parseInputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"test.dtd\">"));

 assertEquals(expectedInputSource.getByteStream(), actualInputSource.getByteStream());
 }

 @Test(expected = SAXParseException.class)
 public void testAbstractLoaderWithInvalidPublicIdAndResourceName() throws Exception {
 final String publicId = "test Public Id";
 final String resourceName = "test.dtd";
 final AbstractLoader abstractLoader = new AbstractLoader(publicId, resourceName);

 final InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"test.dtd\"></root>"));
 abstractLoader.parseInputSource(inputSource);
 }

 @Test(expected = SAXParseException.class)
 public void testAbstractLoaderWithNullPublicIdAndResourceName() throws Exception {
 final AbstractLoader abstractLoader = new AbstractLoader(null, null);

 final InputSource inputSource = new InputSource(new StringReader(VALID_XML));
 abstractLoader.parseInputSource(inputSource);
 }

 @Test
 public void testAbstractLoaderWithEmptyPublicIdToResourceNameMap() throws Exception {
 final AbstractLoader abstractLoader = new AbstractLoader(Collections.emptyMap());

 final InputSource inputSource = new InputSource(new StringReader(VALID_XML));
 abstractLoader.parseInputSource(inputSource);
 }

 @Test(expected = SAXParseException.class)
 public void testAbstractLoaderWithUnknownPublicId() throws Exception {
 final Map<String, String> publicIdToResourceNameMap = new HashMap<>();
 publicIdToResourceNameMap.put("testPublicId", "test.dtd");
 final AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);

 final InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"test.dtd\"></root>"));
 abstractLoader.parseInputSource(inputSource);
 }

 @Test(expected = IOException.class)
 public void testAbstractLoaderWithNullInputSource() throws Exception {
 final AbstractLoader abstractLoader = new AbstractLoader(Collections.emptyMap());

 assertNull(abstractLoader.parseInputSource(null));
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithIOException() throws Exception {
 final Map<String, String> publicIdToResourceNameMap = new HashMap<>();
 final AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);

 final InputSource inputSource = new InputSource(new FileInputStream("non-existent-file.dtd"));
 assertThrows(SAXException.class, () -> abstractLoader.parseInputSource(inputSource));
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithSAXParseException() throws Exception {
 final Map<String, String> publicIdToResourceNameMap = new HashMap<>();
 final AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);

 final InputSource inputSource = new InputSource(new StringReader(INVALID_XML));
 abstractLoader.parseInputSource(inputSource);
 }

 @Test
 public void testAbstractLoaderWithValidPublicIdAndEmptyResourceNameMap() throws Exception {
 final AbstractLoader abstractLoader = new AbstractLoader(Collections.emptyMap());

 final InputSource inputSource = new InputSource(new StringReader(VALID_XML));
 abstractLoader.parseInputSource(inputSource);
 }

 @Test
 public void testAbstractLoaderWithValidPublicIdAndNullResourceNameMap() throws Exception {
 final AbstractLoader abstractLoader = new AbstractLoader(null);

 final InputSource inputSource = new InputSource(new StringReader(VALID_XML));
 abstractLoader.parseInputSource(inputSource);
 }
}