package nu.staldal.lagoon;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.xml.sax.SAXException;

import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;

class LagoonCLITest {

 @BeforeAll
 static void initMocks() {
Properties properties = mock(Properties.class);
File sitemapFile = mock(File.class);
Element sitemapTree = mock(Element.class);
LagoonProcessor processor = mock(LagoonProcessor.class);
 }

 @Test
 void testInvalidArgument() {
 assertThrows(IllegalArgumentException.class, () -> LagoonCLI.main(new String[0]));
 }

 @Test
 void testInvalidSyntax() {
 assertThrows(RuntimeException.class, () -> LagoonCLI.main(new String[]{"LagoonCLI", "invalid"}));
 }

 @Test
 void testInvalidPassword() {
 given(properties.getProperty("password")).willReturn("wrongpassword");
 given(processor.authenticate(anyString())).willThrow(new AuthenticationException("Error message"));

 assertThrows(RuntimeException.class, () -> LagoonCLI.main(new String[]{"LagoonCLI", "property_file.xml", "build"}));
 }

 @Test
 void testValidArgumentPropertyFile() throws IOException, SAXException {
 File propertyFile = new File("src/test/resources/valid.properties");
 Properties properties = new Properties();
 properties.load(new FileInputStream(propertyFile));
 given(processor.authenticate(anyString())).willReturn(true);

 when(sitemapFile.exists()).thenReturn(true);
 when(sitemapFile.isFile()).thenReturn(true);
 when(sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false)).thenReturn(mock(Element.class));

 LagoonCLI.main(new String[]{"LagoonCLI", propertyFile.getAbsolutePath(), "build"});

 verify(processor, times(1)).initialize(eq(properties), eq(sitemapTree), eq(new File(".")), eq(null));
 verify(processor, times(1)).build(eq(false));
 }

 @Test
 void testValidArgumentSitemapFile() throws IOException, SAXException {
 File sitemapFile = new File("src/test/resources/valid.xml");
 given(processor.authenticate(anyString())).willReturn(true);

 when(sitemapFile.exists()).thenReturn(true);
 when(sitemapFile.isFile()).thenReturn(true);
 when(sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false)).thenReturn(mock(Element.class));

 LagoonCLI.main(new String[]{"LagoonCLI", sitemapFile.getAbsolutePath(), "build"});

 verify(processor, times(1)).initialize(any(Properties.class), eq(sitemapTree), eq(new File(".")), eq(null));
 verify(processor, times(1)).build(eq(false));
 }

 @Test
 void testValidArgumentWithInterval() throws IOException, SAXException, InterruptedException {
 File sitemapFile = new File("src/test/resources/valid.xml");
 Properties properties = new Properties();
 properties.load(new FileInputStream(new File("src/test/resources/valid.properties")));
 long interval = 2000;

 when(sitemapFile.exists()).thenReturn(true);
 when(sitemapFile.isFile()).thenReturn(true);
 when(sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false)).thenReturn(mock(Element.class));

 LagoonCLI.main(new String[]{"LagoonCLI", sitemapFile.getAbsolutePath(), "5000"});

 verify(processor, times(1)).initialize(eq(properties), eq(sitemapTree), eq(new File(".")), eq(null));
 verify(processor, never()).build(anyBoolean());

 Thread.sleep(interval + 10);
 LagoonCLI.main(new String[]{"LagoonCLI", sitemapFile.getAbsolutePath(), "5000"});
 verify(processor, times(2)).initialize(eq(properties), eq(sitemapTree), eq(new File(".")), eq(null));
 verify(processor, times(1)).build(eq(false));
 }
}