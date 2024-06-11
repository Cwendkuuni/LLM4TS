import org.junit. Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class jgaapGUITest {

 private jgaapGUI jgaapGUI;
 private guiDriver mockDriver;

 @Before
 public void setUp() {
 mockDriver = Mockito.mock(guiDriver.class);
 jgaapGUI = spy(new jgaapGUI());
 doReturn(mockDriver).when(jgaapGUI).driver;
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testInitComponents() {
 jgaapGUI.initComponents();
 // Check if components are initialized correctly
 assertComponentInitialized(jgaapGUI.menuBar, "North");
 assertComponentInitialized(jgaapGUI.paneOpts, "Center");
 }

 @Test
 public void testActionPerformedExit() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 doCallRealMethod().when(jgaapGUI).actionPerformed(actionEvent);
 jgaapGUI.menuFileExit.doClick();
 Assert.assertEquals(0, jgaapGUI.getWidth());
 Assert.assertEquals(0, jgaapGUI.getHeight());
 }

 @Test
 public void testActionPerformedImport() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 doCallRealMethod().when(jgaapGUI).actionPerformed(actionEvent);
 JFileChooser fileChooser = Mockito.mock(JFileChooser.class);
 jgaapGUI.chooseFile = fileChooser;
 ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
 when(fileChooser.showOpenDialog(jgaapGUI)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File("test.txt"));
 jgaapGUI.buttonLocal.doClick();
 verify(mockDriver, times(1)).addDocument("test.txt", "");
 }

 @Test
 public void testActionPerformedCanonicize() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 doCallRealMethod().when(jgaapGUI).actionPerformed(actionEvent);
 jgaapGUI.buttonCanonicize.doClick();
 ArgumentCaptor<guiDriver.preprocessEngineDriver> preprocessEngineDriverArgumentCaptor = ArgumentCaptor.forClass(guiDriver.preprocessEngineDriver.class);
 verify(mockDriver, times(1)).preprocessEngineDriver(jgaapGUI.canonicizers, jgaapGUI.paneOpts, Mockito.any(JProgressBar.class));
 }

 @Test
 public void testActionPerformedCreateEventSet() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 doCallRealMethod().when(jgaapGUI).actionPerformed(actionEvent);
 jgaapGUI.buttonEventSet.doClick();
 ArgumentCaptor<String> eventSetTypeArgumentCaptor = ArgumentCaptor.forClass(String.class);
 verify(mockDriver, times(1)).createEventSetDriver(eventSetTypeArgumentCaptor.capture());
 String eventSetType = eventSetTypeArgumentCaptor.getValue();
 Assert.assertEquals("characters", eventSetType);
 }

 @Test
 public void testActionPerformedAnalyze() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 doCallRealMethod().when(jgaapGUI).actionPerformed(actionEvent);
 jgaapGUI.buttonAnalyze.doClick();
 ArgumentCaptor<String> statisticalAnalysisTypeArgumentCaptor = ArgumentCaptor.forClass(String.class);
 verify(mockDriver, times(1)).runStatisticalAnalysisDriver(statisticalAnalysisTypeArgumentCaptor.capture());
 String statisticalAnalysisType = statisticalAnalysisTypeArgumentCaptor.getValue();
 Assert.assertEquals("crossentropy", statisticalAnalysisType);
 }

 @Test
 public void testActionPerformedLoadDemo() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 doCallRealMethod().when(jgaapGUI).actionPerformed(actionEvent);
 jgaapGUI.menuFileDemo.doClick();
 verify(mockDriver, times(1)).loadDemo();
 }

 private void assertComponentInitialized(Component component, String location) {
 Assert.assertNotNull(component);
 Assert.assertEquals(component.getName(), location);
 }
}