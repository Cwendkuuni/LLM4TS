package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AbstractLoaderTest {

 private AbstractLoader abstractLoader;
 private DefaultHandler mockDefaultHandler;

 @Before
 public void setUp() throws Exception {
 mockDefaultHandler = mock(DefaultHandler.class);
 abstractLoader = new AbstractLoader(Collections.singletonMap("testPublicId", "testDtdResourceName.dtd")) {
 @Override
 public void setContentHandler(DefaultHandler handler) {
 super.setContentHandler(handler);
 this.mockDefaultHandler = handler;
 }
 };
 }

 @Test
 public void testParseInputSourceWithValidInput() throws Exception {
 final InputStream testInput = new ByteArrayInputStream("Test Input".getBytes());
 final InputSource testInputSource = new InputSource(testInput);
 abstractLoader.parseInputSource(testInputSource);

 verify(mockDefaultHandler).startDocument();
 verify(mockDefaultHandler).characters(anyString());
 verify(mockDefaultHandler).endDocument();
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithUnmappedPublicIdThrowsSAXException() throws Exception {
 abstractLoader.resolveEntity("unmappedPublicId", null);
 }

 @Test
 public void testResolveEntityWithMappedPublicIdReturnsExpectedInputSource() throws Exception {
 when(mockDefaultHandler.getInputSource()).thenReturn(new InputSource(new ByteArrayInputStream("Test Input".getBytes())));
 final InputSource expectedInputSource = new InputSource(new ByteArrayInputStream("Expected Input".getBytes()));
 when(mockDefaultHandler.getPublicId()).thenReturn("testPublicId");

 final ArgumentCaptor<InputSource> inputSourceArgumentCaptor = ArgumentCaptor.forClass(InputSource.class);
 abstractLoader.parseInputSource(new InputSource(new ByteArrayInputStream("Test Input".getBytes())));

 verify(mockDefaultHandler).startDocument();
 verify(mockDefaultHandler).setInputSource(inputSourceArgumentCaptor.capture());
 assertNull(inputSourceArgumentCaptor.getValue().getSystemId());
 assertNull(inputSourceArgumentCaptor.getValue().getPublicId());
 verify(mockDefaultHandler).characters(anyString());
 verify(mockDefaultHandler).endDocument();

 final InputSource actualInputSource = inputSourceArgumentCaptor.getValue();
 assertEquals("Expected Input", actualInputSource.getByteStream());
 }
}