package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.*;
import org.xml.sax.*;
import nu.staldal.lagoon.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import org.junit.*;
import org.mockito.*;

public class LagoonGUITest {

 private LagoonGUI appFrame;
 private MockitoSession mockitoSession;

 @Before
 public void setup() {
 mockitoSession = MockitoAnnotations.initMocks(this);
 appFrame = spy(new LagoonGUI());
 }

 @After
 public void tearDown() {
 mockitoSession.close();
 }

 @Test
 public void testConstructor() {
 // Test that the constructor initializes the necessary components
 assertEquals("Lagoon", appFrame.getTitle());
 assertTrue(appFrame.getLayout() instanceof GridLayout);
 assertNotNull(appFrame.inputPanel);
 assertNotNull(appFrame.centerPanel);
 assertNotNull(appFrame.buttonPanel);
 assertNotNull(appFrame.statusLabel);
 assertNotNull(appFrame.progressArea);
 assertNull(appFrame.processor);
 assertNull(appFrame.sitemapLastModified);
 }

 @Test
 public void testLoadProperties() {
 // Test that loadProperties method loads properties from file
 File propertyFile = mock(File.class);
 when(propertyFile.exists()).thenReturn(true);
 when(propertyFile.getAbsolutePath()).thenReturn("test.properties");
 doReturn("sitemapFile=test.xml").when(appFrame).getProperty(Mockito.any(Properties.class), Mockito.eq("sitemapFile"));
 doReturn("sourceDir=src").when(appFrame).getProperty(Mockito.any(Properties.class), Mockito.eq("sourceDir"));
 doReturn("targetURL=http://localhost").when(appFrame).getProperty(Mockito.any(Properties.class), Mockito.eq("targetURL"));
 doReturn("password=test").when(appFrame).getProperty(Mockito.any(Properties.class), Mockito.eq("password"));
 appFrame.loadProperties(propertyFile);
 verify(appFrame.sitemapFile).setValue("test.xml");
 verify(appFrame.sourceDir).setValue("src");
 verify(appFrame.targetURL).setValue("http://localhost");
 verify(appFrame.password, times(1)).equals("test");
 }

 @Test
 public void testSaveProperties() {
 // Test that saveProperties method saves properties to file
 File propertyFile = mock(File.class);
 when(propertyFile.exists()).thenReturn(false);
 when(propertyFile.getAbsolutePath()).thenReturn("test.properties");
 doReturn(appFrame.sitemapFile.getValue()).when(appFrame).getProperty(Mockito.any(Properties.class), Mockito.eq("sitemapFile"));
 doReturn(appFrame.sourceDir.getValue()).when(appFrame).getProperty(Mockito.any(Properties.class), Mockito.eq("sourceDir"));
 doReturn(appFrame.targetURL.getValue()).when(appFrame).getProperty(Mockito.any(Properties.class), Mockito.eq("targetURL"));
 doReturn("test".toCharArray()).when(appFrame).getPassword();
 doReturn(true).when(appFrame).showDialog(Mockito.any(Component.class), Mockito.eq("Save property file"), Mockito.anyString(), Mockito.eq("Yes"), Mockito.eq("No"));
 appFrame.savePropertyFile();
 verify(appFrame.getFileDialog(Mockito.any(Component.class), Mockito.eq("Save property file"), 1)).show();
 verify(appFrame.getFileStream(propertyFile)).ifPresent(out -> out.println("sitemapFile=" + appFrame.sitemapFile.getValue()));
 verify(appFrame.getFileStream(propertyFile)).ifPresent(out -> out.println("sourceDir=" + appFrame.sourceDir.getValue()));
 verify(appFrame.getFileStream(propertyFile)).ifPresent(out -> out.println("targetURL=" + appFrame.targetURL.getValue()));
 verify(appFrame.getFileStream(propertyFile), times(appFrame.password != null ? 2 : 0)).ifPresent(out -> out.println("password=" + appFrame.password));
 }

 @Test
 public void testBuild() throws Exception {
 // Test that build method calls init and passes the correct arguments to the LagoonProcessor
 when(appFrame.sitemapFile.getValue()).thenReturn("test.xml");
 when(appFrame.sourceDir.getValue()).thenReturn("src");
 when(appFrame.targetURL.getValue()).thenReturn("http://localhost");
 File sitemapFile = mock(File.class);
 when(sitemapFile.lastModified()).thenReturn(1L);
 when(sitemapFile.getAbsolutePath()).thenReturn("test.xml");
 doReturn(mock(LagoonProcessor.class)).when(appFrame).newLagoonProcessor();
 appFrame.build(false);
 verify(appFrame).init();
 verify(appFrame.processor).build(false, appFrame, sitemapFile.getAbsolutePath());
 }

 @Test
 public void testBuildWithForce() throws Exception {
 // Test that build method calls init and passes the correct arguments to the LagoonProcessor with force flag
 when(appFrame.sitemapFile.getValue()).thenReturn("test.xml");
 when(appFrame.sourceDir.getValue()).thenReturn("src");
 when(appFrame.targetURL.getValue()).thenReturn("http://localhost");
 File sitemapFile = mock(File.class);
 when(sitemapFile.lastModified()).thenReturn(2L);
 when(sitemapFile.getAbsolutePath()).thenReturn("test.xml");
 doReturn(mock(LagoonProcessor.class)).when(appFrame).newLagoonProcessor();
 appFrame.build(true);
 verify(appFrame).init();
 verify(appFrame.processor).build(true, appFrame, sitemapFile.getAbsolutePath());
 }

 @Test
 public void testInitWithValidSitemapFile() throws Exception {
 // Test that init method initializes the LagoonProcessor with a valid sitemap file
 when(appFrame.sitemapFile.getValue()).thenReturn("test.xml");
 when(appFrame.sourceDir.getValue()).thenReturn("src");
 when(appFrame.targetURL.getValue()).thenReturn("http://localhost");
 File sitemapFile = mock(File.class);
 when(sitemapFile.lastModified()).thenReturn(1L);
 when(sitemapFile.getAbsolutePath()).thenReturn("test.xml");
 doReturn(mock(PrintWriter.class)).when(appFrame).newPrintWriter(Mockito.any(TextArea.class), Mockito.eq(true));
 appFrame.init();
 verify(appFrame).newLagoonProcessor("http://localhost", sitemapFile.getAbsolutePath(), "src", "test", mock(PrintWriter.class), mock(PrintWriter.class));
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInitWithMissingPassword() throws Exception {
 // Test that init method throws AuthenticationMissingException when password is not specified
 when(appFrame.sitemapFile.getValue()).thenReturn("test.xml");
 when(appFrame.sourceDir.getValue()).thenReturn("src");
 when(appFrame.targetURL.getValue()).thenReturn("http://localhost");
 File sitemapFile = mock(File.class);
 doReturn(mock(PrintWriter.class)).when(appFrame).newPrintWriter(Mockito.any(TextArea.class), Mockito.eq(true));
 appFrame.init();
 }

 @Test(expected = AuthenticationException.class)
 public void testInitWithInvalidPassword() throws Exception {
 // Test that init method throws AuthenticationException when password is invalid
 when(appFrame.sitemapFile.getValue()).thenReturn("test.xml");
 when(appFrame.sourceDir.getValue()).thenReturn("src");
 when(appFrame.targetURL.getValue()).thenReturn("http://localhost");
 when(appFrame.password.equals("test")).thenReturn(false);
 File sitemapFile = mock(File.class);
 doReturn(mock(PrintWriter.class)).when(appFrame).newPrintWriter(Mockito.any(TextArea.class), Mockito.eq(true));
 appFrame.init();
 }

 @Test
 public void testExit() throws Exception {
 // Test that exit method closes the application
 appFrame.exit();
 verify(appFrame).destroy();
 verify(appFrame).dispose();
 }
}