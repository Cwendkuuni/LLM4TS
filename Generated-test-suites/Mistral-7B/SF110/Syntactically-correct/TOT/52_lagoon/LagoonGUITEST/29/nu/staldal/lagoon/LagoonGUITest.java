package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = Mockito.spy(new LagoonGUI()); // Create a spy to mock the window events
 }

 @Test
 public void testLoadProperties() {
 final File propertyFile = new File("test.properties");
 final Properties prop = new Properties();
 prop.setProperty("sitemapFile", "testSitemap.xml");
 prop.setProperty("sourceDir", "testSourceDir");
 prop.setProperty("targetURL", "testTargetURL");
 prop.setProperty("password", "testPassword");

 PowerMockito.mockStatic(FileDialog.class);
 PowerMockito.when(FileDialog.show(ArgumentMatchers.any(Frame.class), ArgumentMatchers.anyString())).thenReturn(propertyFile);

 appFrame.loadProperties(propertyFile);

 assertEquals("testSitemap.xml", appFrame.sitemapFile.getValue());
 assertEquals("testSourceDir", appFrame.sourceDir.getValue());
 assertEquals("testTargetURL", appFrame.targetURL.getValue());
 assertEquals("testPassword", appFrame.password);
 }

 @Test
 public void testSaveProperties() throws IOException {
 final FileDialog propertyFileDialog = Mockito.mock(FileDialog.class);
 final File propertyFile = new File("test.properties");

 PowerMockito.when(FileDialog.show(ArgumentMatchers.any(Frame.class), ArgumentMatchers.anyInt(), ArgumentMatchers.anyString())).thenReturn(propertyFileDialog);
 PowerMockito.when(propertyFileDialog.getFile()).thenReturn(propertyFile);

 appFrame.savePropertyFile();

 PowerMockito.verifyStatic(Properties.class).store(ArgumentMatchers.any(FileOutputStream.class), ArgumentMatchers.anyString());
 }

 @Test
 public void testBuild() throws IOException, AuthenticationException, LagoonException {
 final File sitemapFile = new File("testSitemap.xml");
 final File sourceDir = new File("testSourceDir");
 final String targetURL = "testTargetURL";
 final String password = "testPassword";
 final LagoonProcessor processor = Mockito.mock(LagoonProcessor.class);

 PowerMockito.when(new File(appFrame.sitemapFile.getValue())).thenReturn(sitemapFile);
 PowerMockito.when(new File(appFrame.sourceDir.getValue())).thenReturn(sourceDir);
 PowerMockito.when(new File(appFrame.targetURL.getValue())).thenReturn(new File(targetURL));
 PowerMockito.when(new LagoonProcessor(ArgumentMatchers.anyString(), ArgumentMatchers.any(Element.class), ArgumentMatchers.any(File.class), ArgumentMatchers.anyString(), ArgumentMatchers.any(PrintWriter.class), ArgumentMatchers.any(PrintWriter.class))).thenReturn(processor);

 appFrame.build(false);

 Mockito.verify(processor).build(false);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWithNullProcessor() {
 appFrame.build(false);
 }

 @Test
 public void testExit() {
 appFrame.exit();
 PowerMockito.verifyStatic(System.class).exit(ArgumentMatchers.anyInt());
 }
}