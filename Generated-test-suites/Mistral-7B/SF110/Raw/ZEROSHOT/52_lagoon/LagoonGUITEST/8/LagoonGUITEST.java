package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.MessageDialog;
import nu.staldal.lagoon.core.PasswordDialog;
import nu.staldal.lagoon.core.Properties;
import nu.staldal.lagoon.core.YesNoQueryDialog;
import nu.staldal.lagoon.gui.InputComponent;
import nu.staldal.lagoon.gui.LagoonGUI;
import nu.staldal.lagoon.gui.TextAreaWriter;
import nu.staldal.xtree.Element;

@RunWith(MockitoJUnitRunner.class)
public class LagoonGUITest {

 private static final String TEST_PROPERTY_FILE = "test.properties";
 private static final String TEST_SIMAP_FILE = "testSitemap.xml";
 private static final String TEST_SOURCE_DIR = "testSourceDir";
 private static final String TEST_TARGET_URL = "http://test.com";
 private static final String TEST_PASSWORD = "testPassword";

 @Mock
 private InputComponent sitemapFile;
 @Mock
 private InputComponent sourceDir;
 @Mock
 private InputComponent targetURL;
 @Mock
 private TextAreaWriter progressArea;
 @Mock
 private Label statusLabel;
 @Mock
 private MessageDialog messageDialog;
 @Mock
 private PasswordDialog passwordDialog;
 @Mock
 private YesNoQueryDialog yesNoQueryDialog;

 private LagoonGUI lagoonGUI;

 @Before
 public void setUp() {
 lagoonGUI = new LagoonGUI();
 Mockito.when(sitemapFile.getValue()).thenReturn(TEST_SIMAP_FILE);
 Mockito.when(sourceDir.getValue()).thenReturn(TEST_SOURCE_DIR);
 Mockito.when(targetURL.getValue()).thenReturn(TEST_TARGET_URL);
 Mockito.when(sitemapFile.hasChanged()).thenReturn(false);
 Mockito.when(sourceDir.hasChanged()).thenReturn(false);
 Mockito.when(targetURL.hasChanged()).thenReturn(false);
 Mockito.when(progressArea.write("")).thenReturn(0);
 Mockito.when(progressArea.write(Mockito.anyString())).then(invocation -> invocation.getArgument(0).length());
 Mockito.when(statusLabel.getText()).thenReturn("Not initialized");
 }

 @After
 public void tearDown() {
 lagoonGUI.dispose();
 }

 @Test
 public void testInitialize() throws Exception {
 lagoonGUI.add(sitemapFile, null);
 lagoonGUI.add(sourceDir, null);
 lagoonGUI.add(targetURL, null);
 lagoonGUI.add(statusLabel, null);
 lagoonGUI.add(progressArea, null);

 final Element sitemapTree = Mockito.mock(Element.class);
 Mockito.when(sitemapTree.getTagName()).thenReturn("sitemap");

 final File sitemapFileMock = Mockito.mock(File.class);
 Mockito.when(sitemapFileMock.exists()).thenReturn(true);
 Mockito.when(sitemapFileMock.lastModified()).thenReturn(System.currentTimeMillis());
 Mockito.when(sitemapFileMock.getAbsolutePath()).thenReturn(TEST_SIMAP_FILE);

 Mockito.when(sitemapFile.getValue()).thenReturn(TEST_SIMAP_FILE);
 Mockito.when(new File(TEST_SOURCE_DIR)).exists().thenReturn(true);

 lagoonGUI.init();

 ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<String> titleCaptor = ArgumentCaptor.forClass(String.class);
 Mockito.verify(messageDialog).show(Mockito.eq(LagoonGUI.this), messageCaptor.capture(), titleCaptor.capture());
 assertEquals("Initializing error", messageCaptor.getValue());
 assertEquals("Initializing", titleCaptor.getValue());
 }

 @Test
 public void testBuildWithoutChange() throws Exception {
 lagoonGUI.add(sitemapFile, null);
 lagoonGUI.add(sourceDir, null);
 lagoonGUI.add(targetURL, null);
 lagoonGUI.add(statusLabel, null);
 lagoonGUI.add(progressArea, null);

 final Element sitemapTree = Mockito.mock(Element.class);
 Mockito.when(sitemapTree.getTagName()).thenReturn("sitemap");

 final File sitemapFileMock = Mockito.mock(File.class);
 Mockito.when(sitemapFileMock.exists()).thenReturn(true);
 Mockito.when(sitemapFileMock.lastModified()).thenReturn(System.currentTimeMillis());
 Mockito.when(sitemapFileMock.getAbsolutePath()).thenReturn(TEST_SIMAP_FILE);
 Mockito.when(sitemapFile.getValue()).thenReturn(TEST_SIMAP_FILE);
 Mockito.when(new File(TEST_SOURCE_DIR)).exists().thenReturn(true);

 lagoonGUI.init();

 final File fileMock = Mockito.mock(File.class);
 Mockito.when(fileMock.exists()).thenReturn(true);
 Mockito.when(fileMock.getName()).thenReturn("testFile.html");

 final LagoonProcessor processorMock = Mockito.mock(LagoonProcessor.class);
 Mockito.when(processorMock.build(true)).thenReturn(true);
 Mockito.when(processorMock.destroy()).thenReturn(true);

 Mockito.when(new File(TEST_SOURCE_DIR, "testFile.html")).thenReturn(fileMock);
 Mockito.when(processorMock.getProcessor()).thenReturn("testProcessor");
 Mockito.when(processorMock.getProgressWriter()).thenReturn(progressArea);

 lagoonGUI.build(processorMock);

 ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<String> titleCaptor = ArgumentCaptor.forClass(String.class);
 Mockito.verify(messageDialog).show(Mockito.eq(LagoonGUI.this), messageCaptor.capture(), titleCaptor.capture());
 assertEquals("Build finished successfully 1 s", messageCaptor.getValue());
 assertEquals("Building", titleCaptor.getValue());
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithChange() throws Exception {
 lagoonGUI.add(sitemapFile, null);
 lagoonGUI.add(sourceDir, null);
 lagoonGUI.add(targetURL, null);
 lagoonGUI.add(statusLabel, null);
 lagoonGUI.add(progressArea, null);

 final Element sitemapTree = Mockito.mock(Element.class);
 Mockito.when(sitemapTree.getTagName()).thenReturn("sitemap");

 final File sitemapFileMock = Mockito.mock(File.class);
 Mockito.when(sitemapFileMock.exists()).thenReturn(true);
 Mockito.when(sitemapFileMock.lastModified()).thenReturn(System.currenttimeMillis() - 1000L);
 Mockito.when(sitemapFileMock.getAbsolutePath()).thenReturn(TEST_SIMAP_FILE);
 Mockito.when(sitemapFile.getValue()).thenReturn(TEST_SIMAP_FILE);
 Mockito.when(new File(TEST_SOURCE_DIR)).exists().thenReturn(true);

 lagoonGUI.init();

 final File fileMock = Mockito.mock(File.class);
 Mockito.when(fileMock.exists()).thenReturn(true);
 Mockito.when(fileMock.getName()).thenReturn("testFile.html");

 final LagoonProcessor processorMock = Mockito.mock(LagoonProcessor.class);
 Mockito.when(processorMock.build(true)).thenReturn(true);
 Mockito.when(processorMock.destroy()).thenReturn(true);

 Mockito.when(new File(TEST_SOURCE_DIR, "testFile.html")).thenReturn(fileMock);
 Mockito.when(processorMock.getProcessor()).thenReturn("testProcessor");
 Mockito.when(processorMock.getProgressWriter()).thenReturn(progressArea);

 lagoonGUI.build(processorMock);

 ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<String> titleCaptor = ArgumentCaptor.forClass(String.class);
 Mockito.verify(messageDialog).show(Mockito.eq(LagoonGUI.this), messageCaptor.capture(), titleCaptor.capture());
 assertEquals("Building error", messageCaptor.getValue());
 assertEquals("Building error", titleCaptor.getValue());
 }

 @Test
 public void testLoadProperties() throws Exception {
 final Properties properties = new Properties();
 properties.setProperty("sitemapFile", TEST_SIMAP_FILE);
 properties.setProperty("sourceDir", TEST_SOURCE_DIR);
 properties.setProperty("targetURL", TEST_TARGET_URL);
 properties.setProperty("password", TEST_PASSWORD);

 lagoonGUI.add(sitemapFile, null);
 lagoonGUI.add(sourceDir, null);
 lagoonGUI.add(targetURL, null);
 lagoonGUI.add(statusLabel, null);

 final File sitemapFileMock = Mockito.mock(File.class);
 Mockito.when(sitemapFileMock.exists()).thenReturn(true);
 Mockito.when(sitemapFileMock.lastModified()).thenReturn(System.currenttimeMillis());
 Mockito.when(sitemapFileMock.getAbsolutePath()).thenReturn(TEST_SIMAP_FILE);

 Mockito.when(sitemapFile.getValue()).thenReturn(TEST_SIMAP_FILE);
 Mockito.when(new File(TEST_SOURCE_DIR)).exists().thenReturn(true);

 Mockito.when(new File(TEST_PROPERTY_FILE)).exists().thenReturn(true);

 lagoonGUI.loadProperties(new File(TEST_PROPERTY_FILE));

 ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<InputComponent> componentCaptor = ArgumentCaptor.forClass(InputComponent.class);
 Mockito.verify(sitemapFile).setValue(valueCaptor.capture());
 assertEquals(TEST_SIMAP_FILE, valueCaptor.getValue());
 Mockito.verify(sourceDir).setValue(valueCaptor.capture());
 assertEquals(TEST_SOURCE_DIR, valueCaptor.getValue());
 Mockito.verify(targetURL).setValue(valueCaptor.capture());
 assertEquals(TEST_TARGET_URL, valueCaptor.getValue());
 }

 @Test
 public void testSaveProperties() throws Exception {
 lagoonGUI.add(sitemapFile, null);
 lagoonGUI.add(sourceDir, null);
 lagoonGUI.add(targetURL, null);
 lagoonGUI.add(statusLabel, null);

 final File propertyFileMock = Mockito.mock(File.class);
 Mockito.when(propertyFileMock.exists()).thenReturn(false);

 Mockito.when(new File(TEST_PROPERTY_FILE)).exists().thenReturn(false);

 lagoonGUI.savePropertyFile();

 Mockito.verify(messageDialog).show(Mockito.eq(LagoonGUI.this), Mockito.isA(String.class), Mockito.isA(String.class));
 }

 @Test
 public void testSavePropertiesWithPassword() throws Exception {
 lagoonGUI.add(sitemapFile, null);
 lagoonGUI.add(sourceDir, null);
 lagoonGUI.add(targetURL, null);
 lagoonGUI.add(statusLabel, null);

 final File propertyFileMock = Mockito.mock(File.class);
 Mockito.when(propertyFileMock.exists()).thenReturn(false);

 Mockito.when(new File(TEST_PROPERTY_FILE)).exists().thenReturn(false);

 lagoonGUI.password = TEST_PASSWORD;

 Mockito.when(passwordDialog.show()).thenReturn(true);
 Mockito.when(yesNoQueryDialog.show()).thenReturn(true);

 lagoonGUI.savePropertyFile();

 Mockito.verify(messageDialog).show(Mockito.eq(LagoonGUI.this), Mockito.isA(String.class), Mockito.isA(String.class));
 }

 @Test
 public void testLoadPropertyFile() throws Exception {
 final File propertyFileMock = Mockito.mock(File.class);
 Mockito.when(propertyFileMock.exists()).thenReturn(true);
 Mockito.when(propertyFileMock.length()).thenReturn(123L);
 Mockito.when(new File(TEST_PROPERTY_FILE)).exists().thenReturn(true);

 Mockito.when(new FileReader(TEST_PROPERTY_FILE)).thenReturn(new FileReader("test.properties"));
 Mockito.when(sitemapFile.setValue(Mockito.anyString())).then(invocation -> invocation.getArgument(0).substring(0, 123));

 lagoonGUI.add(sitemapFile, null);
 lagoonGUI.loadPropertyFile();

 Mockito.verify(new FileReader(TEST_PROPERTY_FILE)).close();
 }
}