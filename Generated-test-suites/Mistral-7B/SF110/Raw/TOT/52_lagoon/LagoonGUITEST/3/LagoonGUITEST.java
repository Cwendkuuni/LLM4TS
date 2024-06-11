package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.LagoonProcessor;
import nu.staldal.lagoon.PropertyFileWriter;
import nu.staldal.xtree.XMLException;

public class LagoonGUITest {

 private LagoonGUI appFrame;
 private InputComponent sitemapFile;
 private InputComponent sourceDir;
 private InputComponent targetURL;

 @Before
 public void setup() {
 appFrame = mock(LagoonGUI.class);
 MockitoAnnotations.initMocks(this);
 sitemapFile = mock(InputComponent.class);
 sourceDir = mock(InputComponent.class);
 targetURL = mock(InputComponent.class);
 when(appFrame.getInputPanel()).thenReturn(mock(Panel.class));
 when(appFrame.getInputPanel().getComponents()).thenReturn(new Object[3]);
 when(appFrame.getInputPanel().add(any(Component.class))).thenAnswer(invocation -> {
 throw new UnsupportedOperationException("Adding components to the input panel is not supported in tests.");
 });
 when(appFrame.getButtonPanel()).thenReturn(mock(Panel.class));
 when(appFrame.getButtonPanel().getComponents()).thenReturn(new Object[5]);
 when(appFrame.getButtonPanel().add(any(Component.class))).thenAnswer(invocation -> {
 throw new UnsupportedOperationException("Adding components to the button panel is not supported in tests.");
 });
 when(appFrame.getCenterPanel()).thenReturn(mock(Panel.class));
 when(appFrame.getCenterPanel().getComponents()).thenReturn(new Object[2]);
 when(appFrame.getCenterPanel().add(any(Component.class), anyString())).thenAnswer(invocation -> {
 throw new UnsupportedOperationException("Adding components to the center panel is not supported in tests.");
 });
 when(appFrame.getFrame()).thenReturn(appFrame);
 when(appFrame.getLoadButton()).thenReturn(mock(Button.class));
 when(appFrame.getSaveButton()).thenReturn(mock(Button.class));
 when(appFrame.getBuildButton()).thenReturn(mock(Button.class));
 when(appFrame.getForceButton()).thenReturn(mock(Button.class));
 when(appFrame.getExitButton()).thenReturn(mock(Button.class));
 when(appFrame.getProgressArea()).thenReturn(mock(TextArea.class));
 when(appFrame.getStatusLabel()).thenReturn(mock(Label.class));
 when(appFrame.getProcessor()).thenReturn(null);
 when(appFrame.isDEBUG()).thenReturn(false);
 when(appFrame.show()).thenReturn(true);
 when(appFrame.loadProperties(any(File.class))).thenReturn(true);
 when(appFrame.loadPropertyFile()).thenReturn(null);
 when(appFrame.init()).thenThrow(IOException.class);
 when(appFrame.exit()).thenReturn();

 sitemapFile = spy(sitemapFile);
 sourceDir = spy(sourceDir);
 targetURL = spy(targetURL);
 }

 @Test
 public void testLoadProperties_validFile_loadsPropertiesSuccessfully() throws Exception {
 final File propertyFile = new File("propertyFile.txt");
 doReturn(propertyFile).when(appFrame).getFileDialogReturnValue();
 doReturn(true).when(appFrame).loadProperties(propertyFile);

 appFrame.loadPropertyFile();

 verify(appFrame).loadProperties(propertyFile);
 }

 @Test(expected = NullPointerException.class)
 public void testLoadProperties_nullFile_throwsException() throws Exception {
 doReturn(null).when(appFrame).getFileDialogReturnValue();

 appFrame.loadPropertyFile();
 }

 @Test
 public void testInit_validData_initializesSuccessfully() throws Exception {
 final File sitemapFileValue = new File("sitemap.xml");
 when(sitemapFile.getValue()).thenReturn(sitemapFileValue.getAbsolutePath());
 when(sourceDir.getValue()).thenReturn("sourceDir");
 when(targetURL.getValue()).thenReturn("http://example.com");
 when(appFrame.processor).thenReturn(mock(LagoonProcessor.class));
 when(appFrame.getSitemapFile()).thenReturn(sitemapFile);
 when(appFrame.getSourceDir()).thenReturn(sourceDir);
 when(appFrame.getTargetURL()).thenReturn(targetURL);

 doNothing().when(appFrame).init();

 appFrame.init();

 verify(appFrame).loadProperties(any(File.class));
 verify(appFrame).init();
 }

 @Test(expected = IOException.class)
 public void testInit_fileNotFoundException_throwsException() throws Exception {
 when(sitemapFile.getValue()).thenReturn("nonExistentFile.xml");
 when(appFrame.processor).thenReturn(mock(LagoonProcessor.class));

 doThrow(FileNotFoundException.class).when(appFrame).init();

 appFrame.init();
 }

 @Test(expected = LagoonException.class)
 public void testInit_xmlException_throwsException() throws Exception {
 when(sitemapFile.getValue()).thenReturn("invalidXml.xml");
 when(appFrame.processor).thenReturn(mock(LagoonProcessor.class));

 doThrow(XMLException.class).when(appFrame).init();

 appFrame.init();
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInit_missingPassword_showsPasswordDialog() throws Exception {
 when(sitemapFile.getValue()).thenReturn("sitemap.xml");
 when(sourceDir.getValue()).thenReturn("sourceDir");
 when(targetURL.getValue()).thenReturn("http://example.com");
 when(appFrame.processor).thenReturn(mock(LagoonProcessor.class));

 doNothing().when(appFrame).init();
 doCallRealMethod().when(appFrame).init();

 ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
 verify(appFrame, times(1)).showMessageDialog(any(), msgCaptor.capture());
 assertEquals("Password needed to access target", msgCaptor.getValue());
 }

 @Test(expected = AuthenticationException.class)
 public void testInit_invalidPassword_showsPasswordDialogTwice() throws Exception {
 when(sitemapFile.getValue()).thenReturn("sitemap.xml");
 when(sourceDir.getValue()).thenReturn("sourceDir");
 when(targetURL.getValue()).thenReturn("http://example.com");
 when(appFrame.processor).thenReturn(mock(LagoonProcessor.class));

 doThrow(AuthenticationMissingException.class).when(appFrame).init();
 doThrow(AuthenticationException.class).when(appFrame).init();

 ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
 verify(appFrame, times(2)).showMessageDialog(any(), msgCaptor.capture());
 assertEquals("Invalid password, try again", msgCaptor.getAllValues().get(1));
 }

 @Test
 public void testBuild_noForce_buildsOnlyIfDataChanged() throws Exception {
 final File sitemapFileValue = new File("sitemap.xml");
 when(sitemapFile.getValue()).thenReturn(sitemapFileValue.getAbsolutePath());
 when(sourceDir.getValue()).thenReturn("sourceDir");
 when(targetURL.getValue()).thenReturn("http://example.com");
 when(appFrame.processor).thenReturn(mock(LagoonProcessor.class));
 when(appFrame.getSitemapFile()).thenReturn(sitemapFile);
 when(appFrame.getSourceDir()).thenReturn(sourceDir);
 when(appFrame.getTargetURL()).thenReturn(targetURL);

 doNothing().when(appFrame).build(false);
 doCallRealMethod().when(appFrame).build(false);

 verify(appFrame, never()).getProcessor().build(false);
 }

 @Test
 public void testBuild_noForce_buildsIfSitemapChanged() throws Exception {
 final File sitemapFileValue = new File("sitemap.xml");
 when(sitemapFile.getValue()).thenReturn(sitemapFileValue.getAbsolutePath());
 when(sourceDir.getValue()).thenReturn("sourceDir");
 when(targetURL.getValue()).thenReturn("http://example.com");
 when(appFrame.processor).thenReturn(mock(LagoonProcessor.class));
 when(appFrame.getSitemapFile()).thenReturn(sitemapFile);
 when(appFrame.getSourceDir()).thenReturn(sourceDir);
 when(appFrame.getTargetURL()).thenReturn(targetURL);
 when(sitemapFile.hasChanged()).thenReturn(true);

 doNothing().when(appFrame).build(false);
 doCallRealMethod().when(appFrame).build(false);

 verify(appFrame.getProcessor()).build(false);
 }

 @Test
 public void testBuild_noForce_buildsIfSourceDirChanged() throws Exception {
 final File sitemapFileValue = new File("sitemap.xml");
 when(sitemapFile.getValue()).thenReturn(sitemapFileValue.getAbsolutePath());
 when(sourceDir.getValue()).thenReturn("sourceDirOld");
 when(targetURL.getValue()).thenReturn("http://example.com");
 when(appFrame.processor).thenReturn(mock(LagoonProcessor.class));
 when(appFrame.getSitemapFile()).thenReturn(sitemapFile);
 when(appFrame.getSourceDir()).thenReturn(sourceDir);
 when(appFrame.getTargetURL()).thenReturn(targetURL);
 when(sourceDir.hasChanged()).thenReturn(true);

 doNothing().when(appFrame).build(false);
 doCallRealMethod().when(appFrame).build(false);

 verify(appFrame.getProcessor()).build(false);
 }

 @Test
 public void testBuild_noForce_buildsIfTargetUrlChanged() throws Exception {
 final File sitemapFileValue = new File("sitemap.xml");
 when(sitemapFile.getValue()).thenReturn(sitemapFileValue.getAbsolutePath());
 when(sourceDir.getValue()).thenReturn("sourceDir");
 when(targetURL.getValue()).thenReturn("http://example.comOld");
 when(appFrame.processor).thenReturn(mock(LagoonProcessor.class));
 when(appFrame.getSitemapFile()).thenReturn(sitemapFile);
 when(appFrame.getSourceDir()).thenReturn(sourceDir);
 when(appFrame.getTargetURL()).thenReturn(targetURL);
 when(targetURL.hasChanged()).thenReturn(true);

 doNothing().when(appFrame).build(false);
 doCallRealMethod().when(appFrame).build(false);

 verify(appFrame.getProcessor()).build(false);
 }

 @Test
 public void testBuild_force_buildsAlways() throws Exception {
 final File sitemapFileValue = new File("sitemap.xml");
 when(sitemapFile.getValue()).thenReturn(sitemapFileValue.getAbsolutePath());
 when(sourceDir.getValue()).thenReturn("sourceDir");
 when(targetURL.getValue()).thenReturn("http://example.com");
 when(appFrame.processor).thenReturn(mock(LagoonProcessor.class));
 when(appFrame.getSitemapFile()).thenReturn(sitemapFile);
 when(appFrame.getSourceDir()).thenReturn(sourceDir);
 when(appFrame.getTargetURL()).thenReturn(targetURL);
 when(sitemapFile.hasChanged()).thenReturn(false);
 when(sourceDir.hasChanged()).thenReturn(false);
 when(targetURL.hasChanged()).thenReturn(false);

 doNothing().when(appFrame).build(true);
 doCallRealMethod().when(appFrame).build(true);

 verify(appFrame.getProcessor()).build(true);
 }

 @Test
 public void testExit_showsMessageDialogAndExits() throws Exception {
 doNothing().when(appFrame).destroy();
 doCallRealMethod().when(appFrame).exit();

 ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
 verify(appFrame, times(1)).showMessageDialog(any(), msgCaptor.capture());
 assertEquals("Are you sure you want to exit?", msgCaptor.getValue());
 verify(appFrame).destroy();
 }
}