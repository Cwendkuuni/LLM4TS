package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.xml.sax.helpers.DefaultHandler;

public class LagoonCLITest {

 private static final String SAMPLE_PROPERTY_FILE = "sample.property";
 private static final String SAMPLE_SITEMAP_FILE = "sample.sitemap.xml";
 private static final String SAMPLE_TARGET_URL = "http://example.com";
 private static final String SAMPLE_SOURCE_DIR = "src/test/resources/";
 private static final String SAMPLE_PASSWORD = "testPassword";
 private static final long SAMPLE_INTERVAL = 1000L;

 private LagoonCLI lagoonCLI;
 private DefaultHandler sitemapHandler;

 @Before
 public void setUp() {
 lagoonCLI = new LagoonCLI();
 sitemapHandler = mock(DefaultHandler.class);
 }

 @Test
 public void testMainWithValidArguments() throws IOException, SAXException, InterruptedException {
 // Given: valid arguments
 String[] args = {SAMPLE_PROPERTY_FILE, "1000"};

 // When: calling main method
 lagoonCLI.main(args);

 // Then: expected system out messages are printed
 verify(System.out, times(1)).println("Initializing Lagoon...");
 verify(System.out, times(1)).println("Lagoon initialized successfully");

 // And: expected methods are called
 ArgumentCaptor<String> targetURLCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<File> sitemapFileCaptor = ArgumentCaptor.forClass(File.class);
 ArgumentCaptor<File> sourceDirCaptor = ArgumentCaptor.forClass(File.class);
 ArgumentCaptor<PrintWriter> outWriterCaptor = ArgumentCaptor.forClass(PrintWriter.class);
 ArgumentCaptor<PrintWriter> errWriterCaptor = ArgumentCaptor.forClass(PrintWriter.class);

 verify(LagoonProcessor.class, times(1)).newInstance(
 targetURLCaptor.capture(),
 sitemapFileCaptor.capture(),
 sourceDirCaptor.capture(),
 eq(SAMPLE_PASSWORD),
 outWriterCaptor.capture(),
 errWriterCaptor.capture());

 assertEquals(SAMPLE_TARGET_URL, targetURLCaptor.getValue());
 assertEquals(new File(SAMPLE_SOURCE_DIR), sourceDirCaptor.getValue());
 assertEquals(new File(SAMPLE_SITEMAP_FILE), sitemapFileCaptor.getValue());
 }

 @Test
 public void testMainWithInvalidInterval() throws IOException, SAXException, InterruptedException {
 // Given: invalid interval argument
 String[] args = {SAMPLE_PROPERTY_FILE, "-1"};

 // When: calling main method
 lagoonCLI.main(args);

 // Then: expected system out message is printed
 verify(System.out, times(1)).println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
 }

 @Test
 public void testMainWithInvalidPropertyFile() throws IOException, SAXException, InterruptedException {
 // Given: invalid property file argument
 String[] args = {"nonExistent.property", "1000"};

 // When: calling main method
 lagoonCLI.main(args);

 // Then: expected system err message is printed
 verify(System.err, times(1)).println("Error while initializing Lagoon:\nFile not found: nonExistent.property");
 }

 @Test
 public void testMainWithInvalidArguments() throws IOException, SAXException, InterruptedException {
 // Given: invalid arguments
 String[] args = {SAMPLE_PROPERTY_FILE, "abc"};

 // When: calling main method
 lagoonCLI.main(args);

 // Then: expected system out message is printed
 verify(System.out, times(1)).println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
 }

 @Test
 public void testMainWithMissingPassword() throws IOException, SAXException, InterruptedException {
 // Given: missing password in properties file
 String[] args = {SAMPLE_PROPERTY_FILE};

 // When: calling main method
 lagoonCLI.main(args);

 // Then: expected system err message is printed
 verify(System.err, times(1)).println("Error while initializing Lagoon:\nPassword is required but not specified");
 }

 @Test
 public void testMainWithIncorrectPassword() throws IOException, SAXException, InterruptedException {
 // Given: incorrect password in properties file
 String[] args = {SAMPLE_PROPERTY_FILE, "incorrectPassword"};

 // When: calling main method
 lagoonCLI.main(args);
 // Then: expected system err message is printed
 verify(System.err, times(1)).println("Error while initializing Lagoon:\nIncorrect password");
 }

 @Test
 public void testBuildMethod() throws IOException, SAXException {
 // Given: LagoonProcessor instance
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(eq(false))).thenReturn(true);
 when(processor.build(eq(true))).thenReturn(false);

 // When: calling build method
 lagoonCLI.main(new String[] {SAMPLE_SITEMAP_FILE, "build"});

 // Then: expected methods are called
 verify(processor, times(1)).build(eq(false));
 }

 @Test
 public void testForceBuildMethod() throws IOException, SAXException {
 // Given: LagoonProcessor instance
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(eq(false))).thenReturn(true);
 when(processor.build(eq(true))).thenReturn(false);

 // When: calling force build method
 lagoonCLI.main(new String[] {SAMPLE_SITEMAP_FILE, "force"});

 // Then: expected methods are called
 verify(processor, times(1)).build(eq(true));
 }

 @Test
 public void testIdleMode() throws IOException, SAXException, InterruptedException {
 // Given: LagoonProcessor instance
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(eq(true))).thenReturn(false);
 when(processor.build(eq(false))).thenReturn(true);
 when(processor.destroy()).thenReturn(true);

 // When: calling idle mode
 lagoonCLI.main(new String[] {SAMPLE_PROPERTY_FILE, "1000"});

 // Then: expected methods are called
 verify(processor, times(1)).build(eq(false));
 verify(processor, times(1)).destroy();
 }

 @Test
 public void testIdleModeWithInvalidInterval() throws IOException, SAXException, InterruptedException {
 // Given: LagoonProcessor instance
 LagoonProcessor processor = mock(LagoonProcessor.class);
 when(processor.build(eq(true))).thenReturn(false);
 when(processor.build(eq(false))).thenReturn(true);
 when(processor.destroy()).thenReturn(true);

 // When: calling idle mode with invalid interval
 lagoonCLI.main(new String[] {SAMPLE_PROPERTY_FILE, "-1"});

 // Then: expected methods are not called
 verify(processor, never()).build(anyBoolean());
 verify(processor, never()).destroy();
 }
}