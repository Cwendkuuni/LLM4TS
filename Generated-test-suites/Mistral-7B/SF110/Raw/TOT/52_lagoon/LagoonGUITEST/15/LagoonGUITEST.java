package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.LagoonProcessor;
import nu.staldal.lagoon.PropertyFileReader;
import nu.staldal.lagoon.PropertyFileWriter;
import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.PasswordDialog;
import nu.staldal.lagoon.core.YesNoQueryDialog;
import nu.staldal.xtree.TreeBuilder;
import nu.staldal.xtree.XMLReader;

public class LagoonGUITest {

 private LagoonGUI gui;
 private PropertyFileReader mockPropertyFileReader;
 private PropertyFileWriter mockPropertyFileWriter;
 private PasswordDialog mockPasswordDialog;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 this.gui = new LagoonGUI();
 this.mockPropertyFileReader = mock(PropertyFileReader.class);
 this.mockPropertyFileWriter = mock(PropertyFileWriter.class);
 this.mockPasswordDialog = mock(PasswordDialog.class);
 this.gui.setPropertyFileReader(this.mockPropertyFileReader);
 this.gui.setPropertyFileWriter(this.mockPropertyFileWriter);
 this.gui.setPasswordDialog(this.mockPasswordDialog);
 }

 @Test
 public void testMain() throws IOException, LagoonException {
 final String[] args = new String[]{"testPropertiesFile.properties"};
 this.gui.main(args);
 verify(this.mockPropertyFileReader).readFile(eq(new File("testPropertiesFile.properties")));
 }

 @Test
 public void testInitialize() throws IOException, LagoonException, InterruptedException, AuthenticationException, AuthenticationMissingException {
 final File sitemapFile = new File("sitemap.xml");
 final File propertyFile = new File("testPropertiesFile.properties");
 final File sourceDir = new File("src");
 final File targetURL = new File("http://localhost:8080");
 final String password = "testPassword";

 when(this.mockPropertyFileReader.readFile(eq(propertyFile))).thenReturn(getTestProperties());
 when(this.mockPasswordDialog.show()).thenReturn(password);
 when(this.mockPasswordDialog.getPassword()).thenReturn(password);
 when(this.mockYesNoQueryDialog.show()).thenReturn(true);

 this.gui.loadPropertyFile();
 this.gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 this.gui.sourceDir.setValue(sourceDir.getAbsolutePath());
 this.gui.targetURL.setValue(targetURL.getAbsolutePath());
 this.gui.init();

 verify(this.mockPropertyFileWriter).writeFile(eq(propertyFile), argThat(new PropertyFileMatcher(getTestProperties())));
 verify(this.mockPropertyFileWriter).writeFile(eq(propertyFile), argThat(new PropertyFileMatcher(getInitializedProperties())));
 verify(this.mockPasswordDialog, times(1)).show();
 verify(this.mockPropertyFileReader).readFile(eq(sitemapFile));
 verify(this.mockXMLReader).parse(eq(sitemapFile), eq(true));
 ArgumentCaptor<LagoonProcessor> processorArgumentCaptor = ArgumentCaptor.forClass(LagoonProcessor.class);
 verify(this.mockPrintWriter).println(eq("--- Start build ---"));
 verify(this.mockLagoonProcessor).build(eq(false));
 verify(this.mockPrintWriter).println(eq("--- Build finished successfully " + anyString() + " ---"));
 }

 @Test
 public void testBuild() throws IOException, LagoonException, InterruptedException, AuthenticationException, AuthenticationMissingException {
 this.gui.loadPropertyFile();
 this.gui.sitemapFile.hasChanged();
 this.gui.sourceDir.hasChanged();
 this.gui.targetURL.hasChanged();
 this.gui.init();
 this.gui.processor = mock(LagoonProcessor.class);
 this.gui.build(false);
 verify(this.mockPrintWriter).println(eq("--- Start build ---"));
 verify(this.mockLagoonProcessor).build(eq(false));
 verify(this.mockPrintWriter).println(eq("--- Build finished successfully " + anyString() + " ---"));
 }

 @Test
 public void testBuildWithForce() throws IOException, LagoonException, InterruptedException, AuthenticationException, AuthenticationMissingException {
 this.gui.loadPropertyFile();
 this.gui.sitemapFile.hasChanged();
 this.gui.sourceDir.hasChanged();
 this.gui.targetURL.hasChanged();
 this.gui.init();
 this.gui.processor = mock(LagoonProcessor.class);
 this.gui.build(true);
 verify(this.mockPrintWriter).println(eq("--- Start build ---"));
 verify(this.mockLagoonProcessor).build(eq(true));
 verify(this.mockPrintWriter).println(eq("--- Build finished successfully " + anyString() + " ---"));
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuildWithMissingSitemapFile() throws IOException, LagoonException, InterruptedException, AuthenticationException, AuthenticationMissingException {
 this.gui.loadPropertyFile();
 this.gui.sitemapFile.setValue("");
 this.gui.sourceDir.setValue(new File("src").getAbsolutePath());
 this.gui.targetURL.setValue(new File("http://localhost:8080").getAbsolutePath());
 this.gui.init();
 this.gui.build(false);
 }

 private Properties getTestProperties() {
 final Properties prop = new Properties();
 prop.setProperty("sitemapFile", "sitemap.xml");
 prop.setProperty("sourceDir", "src");
 prop.setProperty("targetURL", "http://localhost:8080");
 prop.setProperty("password", "testPassword");
 return prop;
 }

 private Properties getInitializedProperties() {
 final Properties prop = new Properties();
 prop.setProperty("sitemapFile", "sitemap.xml");
 prop.setProperty("sourceDir", "src");
 prop.setProperty("targetURL", "http://localhost:8080");
 prop.setProperty("password", "testPassword");
 prop.setProperty("processor.lastModified", "1234567890123");
 return prop;
 }

 private static XMLReader mockXMLReader = mock(XMLReader.class);
 private static PrintWriter mockPrintWriter = mock(PrintWriter.class);
 private static LagoonProcessor mockLagoonProcessor = mock(LagoonProcessor.class);

 static {
 try {
 TreeBuilder.setXMLReader(mockXMLReader);
 }
 catch (SAXException e) {
 e.printStackTrace();
 }
 try {
 System.setOut(new PrintStream(new TextAreaWriter(mockPrintWriter)));
 }
 catch (IOException e) {
 e.printStackTrace();
 }
 }
}