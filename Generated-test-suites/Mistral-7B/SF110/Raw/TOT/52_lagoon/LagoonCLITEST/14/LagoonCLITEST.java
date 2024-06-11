package nu.staldal.lagoon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.xml.sax.SAXException;

import nu.staldal.xtree.Element;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LagoonCLITest {

 @Mock
 private PrintWriter outMock;
 @Mock
 private PrintWriter errMock;
 @Mock
 private LagoonProcessor processorMock;
 @InjectMocks
 private LagoonCLI lagoonCLI;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 lagoonCLI.setOut(outMock);
 lagoonCLI.setErr(errMock);
 }

 @After
 public void tearDown() {
 reset(outMock, errMock, processorMock);
 }

 @Test
 public void testMainWithInvalidArguments() {
 String[] args = new String[0];
 lagoonCLI.main(args);
 verify(errMock, times(1)).println(syntaxMsg);
 }

 @Test
 public void testMainWithInvalidArgumentType() {
 String[] args = new String[]{"property_file", "invalid"};
 lagoonCLI.main(args);
 verify(errMock, times(1)).println(syntaxMsg);
 }

 @Test
 public void testMainWithInvalidInterval() {
 String[] args = new String[]{"property_file", "-1"};
 lagoonCLI.main(args);
 verify(errMock, times(1)).println(syntaxMsg);
 }

 @Test
 public void testMainWithValidArguments() {
 String[] args = new String[]{"property_file", "5000"};
 lagoonCLI.main(args);
 verify(processorMock, times(1)).__constructor__(eq("http://localhost"), argThat(isA(Element.class)), eq(new File("source_dir")), eq(null), eq(outMock), eq(errMock));
 }

 @Test
 public void testPropertyFileParsing() {
 Properties properties = new Properties();
 File propertyFile = new File("test_property_file.xml");
 FileInputStream fis = new FileInputStream(propertyFile);
 properties.load(fis);
 fis.close();
 when(processorMock.parseXML(any(InputSource.class), anyBoolean())).thenReturn(newElementMock());
 ArgumentCaptor<Element> sitemapTreeCaptor = ArgumentCaptor.forClass(Element.class);
 lagoonCLI.main(new String[]{"property_file"});
 verify(processorMock, times(1)).__constructor__(eq("http://localhost"), sitemapTreeCaptor.capture(), eq(new File("source_dir")), eq(null), eq(outMock), eq(errMock));
 assertEquals("expected_sitemap_tree", sitemapTreeCaptor.getValue().toString());
 }

 @Test
 public void testBuildingWebsiteWithSuccess() {
 when(processorMock.build(anyBoolean())).thenReturn(true);
 lagoonCLI.main(new String[]{"sitemap_file", "build"});
 verify(outMock, times(1)).println("Building website...");
 verify(outMock, times(1)).println("...successfully");
 }

 @Test
 public void testBuildingWebsiteWithErrors() {
 when(processorMock.build(anyBoolean())).thenReturn(false);
 lagoonCLI.main(new String[]{"sitemap_file", "build"});
 verify(outMock, times(1)).println("Building website...");
 verify(errMock, times(1)).println("...with errors...");
 }

 @Test
 public void testForceBuildingWebsiteWithSuccess() {
 when(processorMock.build(anyBoolean())).thenReturn(true);
 lagoonCLI.main(new String[]{"sitemap_file", "force"});
 verify(outMock, times(1)).println("Force building website...");
 verify(outMock, times(1)).println("...successfully");
 }

 @Test
 public void testForceBuildingWebsiteWithErrors() {
 when(processorMock.build(anyBoolean())).thenReturn(false);
 lagoonCLI.main(new String[]{"sitemap_file", "force"});
 verify(outMock, times(1)).println("Force building website...");
 verify(errMock, times(1)).println("...with errors...");
 }

 @Test
 public void testPeriodicBuildingWebsiteWithSuccess() {
 when(processorMock.build(anyBoolean())).thenReturn(true);
 lagoonCLI.main(new String[]{"sitemap_file", "10000"});
 verify(outMock, times(1)).println("Building website...");
 verify(outMock, times(1)).println("...successfully");
 }

 @Test
 public void testPeriodicBuildingWebsiteWithErrors() {
 when(processorMock.build(anyBoolean())).thenReturn(false);
 lagoonCLI.main(new String[]{"sitemap_file", "10000"});
 verify(outMock, times(1)).println("Building website...");
 verify(errMock, times(1)).println("...with errors...");
 }

 @Test
 public void testMainWithInvalidURL() {
 String[] args = new String[]{"property_file", "http://invalid_url"};
 lagoonCLI.main(args);
 verify(errMock, times(1)).println("Error while initializing Lagoon:");
 verify(errMock, times(1)).println("File not found: http://invalid_url");
 }

 @Test
 public void testMainWithMissingPropertyFile() {
 String[] args = new String[]{"missing_file.xml", "build"};
 lagoonCLI.main(args);
 verify(errMock, times(1)).println("Error while initializing Lagoon:");
 verify(errMock, times(1)).println("File not found: missing_file.xml");
 }

 @Test
 public void testMainWithMissingProperty() {
 Properties properties = new Properties();
 File propertyFile = new File("test_property_file.xml");
 FileInputStream fis = new FileInputStream(propertyFile);
 properties.load(fis);
 fis.close();
 when(processorMock.parseXML(any(InputSource.class), anyBoolean())).thenReturn(newElementMock());
 properties.remove("targetURL");
 lagoonCLI.main(new String[]{"property_file"});
 verify(errMock, times(1)).println("Error while initializing Lagoon:");
 verify(errMock, times(1)).println("Property targetURL not specified");
 }

 @Test
 public void testMainWithInvalidPassword() {
 Properties properties = new Properties();
 File propertyFile = new File("test_property_file.xml");
 FileInputStream fis = new FileInputStream(propertyFile);
 properties.load(fis);
 fis.close();
 when(processorMock.parseXML(any(InputSource.class), anyBoolean())).thenReturn(newElementMock());
 properties.setProperty("password", "invalid_password");
 lagoonCLI.main(new String[]{"property_file"});
 verify(errMock, times(1)).println("Error while initializing Lagoon:");
 verify(errMock, times(1)).println("Incorrect password");
 }

 @Test
 public void testMainWithSyntaxErrorInPropertyFile() {
 Properties properties = new Properties();
 File propertyFile = new File("test_property_file.xml");
 FileInputStream fis = new FileInputStream(propertyFile);
 properties.load(fis);
 fis.close();
 when(processorMock.parseXML(any(InputSource.class), anyBoolean())).thenThrow(new SAXException());
 lagoonCLI.main(new String[]{"property_file"});
 verify(errMock, times(1)).println("Error while initializing Lagoon:");
 verify(errMock, times(1)).println("Syntax error in XML sitemap file: ");
 }

 @Test
 public void testMainWithMissingSitemapFile() {
 Properties properties = new Properties();
 File propertyFile = new File("test_property_file.xml");
 FileInputStream fis = new FileInputStream(propertyFile);
 properties.load(fis);
 fis.close();
 properties.setProperty("sitemapFile", "missing_file.xml");
 lagoonCLI.main(new String[]{"property_file"});
 verify(errMock, times(1)).println("Error while initializing Lagoon:");
 verify(errMock, times(1)).println("File not found: missing_file.xml");
 }

 private Element newElementMock() {
 return mock(Element.class);
 }
}