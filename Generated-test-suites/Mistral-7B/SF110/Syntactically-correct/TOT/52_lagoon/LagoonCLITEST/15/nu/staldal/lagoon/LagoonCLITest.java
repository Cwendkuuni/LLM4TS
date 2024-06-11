package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import static nu.staldal.lagoon.LagoonCLI.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.xml.sax.SAXParseException;

import java.io.*;
import java.util.*;

public class LagoonCLITest {

 private static final String SYNTAX_MSG = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 private static final String PROPERTY_FILE = "testPropertyFile.xml";
 private static final String SITEMAP_FILE = "testSitemapFile.xml";
 private static final String INVALID_PROPERTY_FILE = "testInvalidPropertyFile.xml";
 private static final String INVALID_SITEMAP_FILE = "testInvalidSitemapFile.xml";
 private static final String INVALID_ARGUMENT = "invalidArgument";
 private static final String VALID_ARGUMENT = "build";
 private static final long VALID_INTERVAL = 5000L;
 private static final String VALID_TARGET_URL = "http://test.com";
 private static final String VALID_SOURCE_DIR = "testSourceDir";
 private static final String VALID_PASSWORD = "testPassword";
 private LagoonProcessor mockProcessor;

 @Before
 public void setup() {
 mockProcessor = Mockito.mock(LagoonProcessor.class);
 }

 @Test
 public void testParseArgs_ValidArguments() throws Exception {
 String[] args = {PROPERTY_FILE, VALID_ARGUMENT};
 runLagoonCLI(args);
 Mockito.verify(mockProcessor).init(eq(VALID_TARGET_URL), any(Element.class), eq(new File(VALID_SOURCE_DIR)), eq(VALID_PASSWORD), any(PrintWriter.class), any(PrintWriter.class));
 }

 @Test
 public void testParseArgs_InvalidArguments() throws Exception {
 String[] args = {INVALID_ARGUMENT};
 runLagoonCLI(args);
 verifyNoLagoonExceptionThrown();
 System.out.println(SYNTAX_MSG);
 }

 @Test
 public void testParseArgs_InvalidPropertyFile() throws Exception {
 String[] args = {INVALID_PROPERTY_FILE};
 runLagoonCLI(args);
 verifyNoLagoonExceptionThrown();
 System.out.println("Error while parsing property file: " + INVALID_PROPERTY_FILE);
 }

 @Test
 public void testParseArgs_InvalidSitemapFile() throws Exception {
 String[] args = {SITEMAP_FILE};
 runLagoonCLI(args);
 verifyNoLagoonExceptionThrown();
 System.out.println("Error while parsing sitemap file: " + SITEMAP_FILE);
 }

 @Test
 public void testParseArgs_InvalidInterval() throws Exception {
 String[] args = {PROPERTY_FILE, "-1"};
 runLagoonCLI(args);
 verifyNoLagoonExceptionThrown();
 System.out.println("Invalid interval: -1");
 }

 @Test
 public void testParseArgs_InvalidBuildFlag() throws Exception {
 String[] args = {PROPERTY_FILE, "invalidBuildFlag"};
 runLagoonCLI(args);
 verifyNoLagoonExceptionThrown();
 System.out.println("Invalid build flag: invalidBuildFlag");
 }

 @Test
 public void testBuild_Success() throws Exception {
 Mockito.when(mockProcessor.build(false)).thenReturn(true);
 executeLagoonCLI( args(PROPERTY_FILE, VALID_ARGUMENT) );
 Mockito.verify(mockProcessor).build(eq(false));
 }

 @Test
 public void testBuild_Failure() throws Exception {
 Mockito.when(mockProcessor.build(false)).thenThrow(new LagoonException("Build error"));
 executeLagoonCLI( args(PROPERTY_FILE, VALID_ARGUMENT) );
 System.out.println("Build error");
 }

 @Test
 public void testForceBuild_Success() throws Exception {
 Mockito.when(mockProcessor.build(true)).thenReturn(true);
 executeLagoonCLI( args(SITEMAP_FILE, VALID_ARGUMENT, VALID_INTERVAL) );
 Mockito.verify(mockProcessor).build(eq(true));
 }

 @Test
 public void testForceBuild_Failure() throws Exception {
 Mockito.when(mockProcessor.build(true)).thenThrow(new LagoonException("Force build error"));
 executeLagoonCLI( args(SITEMAP_FILE, VALID_ARGUMENT, VALID_INTERVAL) );
 System.out.println("Force build error");
 }

 @Test
 public void testInterval_Success() throws Exception {
 Mockito.when(mockProcessor.build(false)).thenReturn(true);
 executeLagoonCLI( args(PROPERTY_FILE, VALID_ARGUMENT, VALID_INTERVAL) );
 Mockito.verify(mockProcessor, Mockito.atLeastOnce()).build(eq(false));
 Thread.sleep(VALID_INTERVAL + 100);
 }

 @Test
 public void testInterval_Failure() throws Exception {
 Mockito.when(mockProcessor.build(false)).thenThrow(new LagoonException("Build error"));
 executeLagoonCLI( args(PROPERTY_FILE, VALID_ARGUMENT, VALID_INTERVAL) );
 System.out.println("Build error");
 Thread.sleep(VALID_INTERVAL + 100);
 }

 private void runLagoonCLI(String[] args) throws Exception {
 try {
 main(args);
 } catch (Exception e) {
 throw new Exception("Unexpected exception", e);
 }
 }

 private void executeLagoonCLI(String[] args) throws Exception {
 runLagoonCLI(args);
 }

 private ArgumentCaptor<PrintWriter> printWriterArgumentCaptor() {
 return ArgumentCaptor.forClass(PrintWriter.class);
 }

 private ArgumentCaptor<PrintWriter> errorPrintWriterArgumentCaptor() {
 return ArgumentCaptor.forClass(PrintWriter.class);
 }

 private void verifyNoLagoonExceptionThrown() {
 try {
 main(new String[0]);
 } catch (LagoonException e) {
 fail("Unexpected LagoonException: " + e.getMessage());
 }
 }

 private String[] args(String... args) {
 return args;
 }
}