package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import nu.staldal.lagoon.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;
import org.junit.*;
import org.mockito.*;

public class LagoonCLITest {

 private static final String syntaxMsg = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";

 @Before
 public void initializeMocks() {
 Properties properties = mock(Properties.class);
 FileInputStream fis = mock(FileInputStream.class);
 LagoonCLI.properties = properties;
 when(properties.load(fis)).thenReturn(null);
 when(properties.getProperty("targetURL")).thenReturn("http://example.com");
 when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
 when(properties.getProperty("sourceDir")).thenReturn(new File("sourceDir"));
 when(properties.getProperty("password")).thenReturn("password");
 File sitemapFile = mock(File.class);
 when(sitemapFile.exists()).thenReturn(true);
 InputSource inputSource = mock(InputSource.class);
 when(TreeBuilder.fileToInputSource(sitemapFile)).thenReturn(inputSource);
 Element sitemapTree = mock(Element.class);
 SAXParser parser = mock(SAXParser.class);
 when(parser.parse(inputSource)).thenReturn(sitemapTree);
 LagoonProcessor processor = mock(LagoonProcessor.class);
 PrintWriter out = mock(PrintWriter.class);
 PrintWriter err = mock(PrintWriter.class);
 when(processor.build(false)).thenReturn(true);
 when(processor.build(true)).thenReturn(false);
 when(processor.destroy()).thenReturn(true);
 LagoonCLI.main(new String[]{"property.xml"});
 }

 @Test
 public void testInvalidSyntax() {
 String[] args = new String[0];
 LagoonCLI.main(args);
 verify(System.out, times(1)).println(syntaxMsg);
 }

 @Test
 public void testInvalidArgumentType() {
 String[] args = new String[]{"property.xml", "abc"};
 LagoonCLI.main(args);
 verify(System.out, times(1)).println(syntaxMsg);
 }

 @Test
 public void testInvalidIntervalValue() {
 String[] args = new String[]{"property.xml", "-1"};
 LagoonCLI.main(args);
 verify(System.out, times(1)).println(syntaxMsg);
 }

 @Test
 public void testInvalidPasswordMissing() {
 String[] args = new String[]{"sitemap.xml", "build"};
 LagoonCLI.main(args);
 verify(System.err, times(1)).println("Error while initializing Lagoon:\nPassword is required but not specified");
 }

 @Test
 public void testInvalidPasswordIncorrect() {
 String[] args = new String[]{"property.xml", "sitemap.xml", "wrongPassword"};
 LagoonCLI.main(args);
 verify(System.err, times(1)).println("Error while initializing Lagoon:\nIncorrect password");
 }

 @Test
 public void testFileNotFound() {
 String[] args = new String[]{"property.xml", "sitemap.xml", "build"};
 when(sitemapFile.exists()).thenReturn(false);
 LagoonCLI.main(args);
 verify(System.err, times(1)).println("Error while initializing Lagoon:\nFile not found: sitemap.xml");
 }

 @Test
 public void testIOException() {
 String[] args = new String[]{"property.xml", "sitemap.xml", "build"};
 when(sitemapFile.exists()).thenReturn(true);
 when(TreeBuilder.parseXML(inputSource, false)).thenThrow(new IOException());
 LagoonCLI.main(args);
 verify(System.err, times(1)).println("Error while initializing Lagoon:\nI/O error: ");
 }

 @Test
 public void testLagoonException() {
 String[] args = new String[]{"property.xml", "sitemap.xml", "build"};
 when(sitemapFile.exists()).thenReturn(true);
 when(TreeBuilder.parseXML(inputSource, false)).thenReturn(sitemapTree);
 when(processor.build(false)).thenThrow(new LagoonException("Exception message"));
 LagoonCLI.main(args);
 verify(System.err, times(1)).println("Error while initializing Lagoon:\nException message");
 }

 @Test
 public void testBuildSuccess() {
 String[] args = new String[]{"property.xml", "sitemap.xml", "build"};
 LagoonCLI.main(args);
 verify(processor, times(1)).build(false);
 }

 @Test
 public void testForceBuildSuccess() {
 String[] args = new String[]{"property.xml", "sitemap.xml", "force"};
 LagoonCLI.main(args);
 verify(processor, times(1)).build(true);
 }

 @Test
 public void testBuildWithInterval() {
 String[] args = new String[]{"property.xml", "1000"};
 LagoonCLI.main(args);
 verify(processor, times(1)).build(false);
 }

 @Test
 public void testInputFromConsole() {
 String[] args = new String[]{"property.xml"};
 InputStreamSystemInMock inputStreamMock = mock(InputStreamSystemInMock.class);
 when(System.in).thenReturn(inputStreamMock);
 when(inputStreamMock.readLine()).thenReturn("b");
 LagoonCLI.main(args);
 verify(processor, times(1)).build(false);
 }

 @Test
 public void testInputFromConsoleForce() {
 String[] args = new String[]{"property.xml"};
 InputStreamSystemInMock inputStreamMock = mock(InputStreamSystemInMock.class);
 when(System.in).thenReturn(inputStreamMock);
 when(inputStreamMock.readLine()).thenReturn("f");
 LagoonCLI.main(args);
 verify(processor, times(1)).build(true);
 }

 @Test
 public void testInputFromConsoleQuit() {
 String[] args = new String[]{"property.xml"};
 InputStreamSystemInMock inputStreamMock = mock(InputStreamSystemInMock.class);
 when(System.in).thenReturn(inputStreamMock);
 when(inputStreamMock.readLine()).thenReturn("q");
 LagoonCLI.main(args);
 }
}