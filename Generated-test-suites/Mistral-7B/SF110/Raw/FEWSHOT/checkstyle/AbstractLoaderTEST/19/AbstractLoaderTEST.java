package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AbstractLoaderTest {

 private AbstractLoader loader;
 private XMLReader parser;

 @Before
 public void setup() throws Exception {
 loader = new AbstractLoader(Collections.emptyMap());
 parser = mock(XMLReader.class);
 when(parser.parse(InputSource.class.getConstructor(InputStream.class).newInstance(new ByteArrayInputStream(new byte[0]))))
 .thenReturn(null);
 loader.setParser(parser);
 }

 @Test
 public void testParseInputSourceWithEmptyInputSource() throws Exception {
 InputSource inputSource = new InputSource(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
 loader.parseInputSource(inputSource);
 assertNull(loader.getErrors());
 }

 @Test
 public void testParseInputSourceWithValidInputSource() throws Exception {
 InputSource inputSource = new InputSource(new InputStreamReader(new FileInputStream("test.xml")));
 loader.parseInputSource(inputSource);
 assertNull(loader.getErrors());
 }

 @Test
 public void testResolveEntityWithPublicIdMapped() throws Exception {
 final String publicId = "testPublicId";
 final String dtdResourceName = "test.dtd";
 loader = new AbstractLoader(new HashMap<String, String>() {{
 put(publicId, dtdResourceName);
 }});
 InputSource inputSource = new InputSource(new ByteArrayInputStream("<?xml version='1.0'?>\n".getBytes()));
 InputStream expectedDTDStream = new FileInputStream("test.dtd");
 InputSource resolvedInputSource = loader.resolveEntity(publicId, null);
 assertEquals(expectedDTDStream, resolvedInputSource.getInputStream());
 }

 @Test
 public void testResolveEntityWithUnknownPublicId() throws Exception {
 InputSource resolvedInputSource = loader.resolveEntity("unknownPublicId", null);
 assertNull(resolvedInputSource);
 }

 @Test
 public void testParseInputSourceWithError() throws Exception {
 InputSource inputSource = new InputSource(new ByteArrayInputStream("<?xml version='1.0'?>\n<root><error/></root>"
 .getBytes()));
 loader.parseInputSource(inputSource);
 assertNotNull(loader.getErrors());
 assertEquals(1, loader.getErrors().size());
 }

 @Test
 public void testParseInputSourceWithFatalError() throws Exception {
 InputSource inputSource = new InputSource(new ByteArrayInputStream("<?xml version='1.0'?>\n<root><fatalError/></root>"
 .getBytes()));
 loader.parseInputSource(inputSource);
 assertNull(loader.getErrors()); // Fatal errors are not added to the errors list
 }
}