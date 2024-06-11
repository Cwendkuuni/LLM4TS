package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.*;
import nu.staldal.xtree.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;
 private InputComponent sitemapFileMock;
 private InputComponent sourceDirMock;
 private InputComponent targetURLMock;
 private Frame frameMock;
 private WindowEvent windowEventMock;
 private ActionEvent actionEventMock;
 private TextArea progressAreaMock;
 private Label statusLabelMock;
 private LagoonProcessor processorMock;

 @Before
 public void setup() {
 lagoonGUI = new LagoonGUI();
 sitemapFileMock = Mockito.mock(InputComponent.class);
 sourceDirMock = Mockito.mock(InputComponent.class);
 targetURLMock = Mockito.mock(InputComponent.class);
 progressAreaMock = Mockito.mock(TextArea.class);
 statusLabelMock = Mockito.mock(Label.class);
 frameMock = Mockito.mock(Frame.class);
 windowEventMock = Mockito.mock(WindowEvent.class);
 actionEventMock = Mockito.mock(ActionEvent.class);

 Mockito.when(lagoonGUI.getInputPanel()).thenReturn(new Panel());
 Mockito.when(lagoonGUI.getCenterPanel()).thenReturn(new Panel());
 Mockito.when(lagoonGUI.getButtonPanel()).thenReturn(new Panel());

 Mockito.when(lagoonGUI.getSitemapFile()).thenReturn(sitemapFileMock);
 Mockito.when(lagoonGUI.getSourceDir()).thenReturn(sourceDirMock);
 Mockito.when(lagoonGUI.getTargetURL()).thenReturn(targetURLMock);
 Mockito.when(lagoonGUI.getProgressArea()).thenReturn(progressAreaMock);
 Mockito.when(lagoonGUI.getStatusLabel()).thenReturn(statusLabelMock);

 Mockito.when(lagoonGUI.addWindowListener(ArgumentMatchers.any(WindowListener.class))).then(invocation -> {
 invocation.getArgument(0).windowOpened(windowEventMock);
 });

 processorMock = Mockito.mock(LagoonProcessor.class);
 Mockito.when(lagoonGUI.new LagoonProcessor(ArgumentMatchers.eq(lagoonGUI.targetURL.getValue()), ArgumentMatchers.any(Element.class), ArgumentMatchers.eq(new File(lagoonGUI.sourceDir.getValue())), ArgumentMatchers.eq(lagoonGUI.password), ArgumentMatchers.any(PrintWriter.class), ArgumentMatchers.any(PrintWriter.class))).thenReturn(processorMock);
 }

 @Test
 public void testConstructor() {
 assertNotNull(lagoonGUI);
 assertEquals("Lagoon", lagoonGUI.getTitle());
 }

 @Test
 public void testWindowListenerMethods() {
 lagoonGUI.windowOpened(windowEventMock);
 verify(lagoonGUI, times(1)).windowOpened(windowEventMock);
 }

 @Test
 public void testLoadButtonActionPerformed() {
 lagoonGUI.loadButton.addActionListener(Mockito.mock(ActionListener.class));
 lagoonGUI.loadButton.requestFocusInWindow();
 lagoonGUI.loadButton.doClick();

 Mockito.verify(lagoonGUI, times(1)).loadPropertyFile();
 }

 @Test
 public void testSaveButtonActionPerformed() {
 lagoonGUI.saveButton.addActionListener(Mockito.mock(ActionListener.class));
 lagoonGUI.saveButton.requestFocusInWindow();
 lagoonGUI.saveButton.doClick();

 Mockito.verify(lagoonGUI, times(1)).savePropertyFile();
 }

 @Test
 public void testBuildButtonActionPerformedFalse() throws Exception {
 lagoonGUI.buildButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(final ActionEvent e) {
 lagoonGUI.build(false);
 }
 });

 lagoonGUI.buildButton.requestFocusInWindow();
 lagoonGUI.buildButton.doClick();

 Mockito.verify(lagoonGUI, times(1)).init();
 Mockito.verify(processorMock, times(1)).build(false);
 }

 @Test
 public void testBuildButtonActionPerformedTrue() throws Exception {
 lagoonGUI.buildButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(final ActionEvent e) {
 lagoonGUI.build(true);
 }
 });

 lagoonGUI.buildButton.requestFocusInWindow();
 lagoonGUI.buildButton.doClick();

 Mockito.verify(lagoonGUI, times(1)).init();
 Mockito.verify(processorMock, times(1)).build(true);
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuildWithNonExistentSitemapFile() throws Exception {
 sitemapFileMock.setValue("nonExistentFile.xml");
 lagoonGUI.buildButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(final ActionEvent e) {
 lagoonGUI.build(false);
 }
 });

 lagoonGUI.buildButton.requestFocusInWindow();
 lagoonGUI.buildButton.doClick();
 }

 @Test
 public void testExitButtonActionPerformed() {
 lagoonGUI.exitButton.addActionListener(new ActionListener() {
 @Override
 public void actionPerformed(final ActionEvent e) {
 lagoonGUI.exit();
 }
 });

 lagoonGUI.exitButton.requestFocusInWindow();
 lagoonGUI.exitButton.doClick();

 Mockito.verify(lagoonGUI, times(1)).exit();
 }

 @Test
 public void testInit() throws Exception {
 lagoonGUI.init();

 Mockito.verify(lagoonGUI, times(1)).destroy();
 Mockito.verify(lagoonGUI, times(1)).init();
 Mockito.verify(processorMock, times(1)).build(false);
 }

 @Test
 public void testLoadProperties() throws Exception {
 final File propertyFile = new File("test.properties");
 lagoonGUI.loadProperties(propertyFile);

 Mockito.verify(lagoonGUI, times(1)).sitemapFile.setValue("sitemapFile");
 Mockito.verify(lagoonGUI, times(1)).sourceDir.setValue("sourceDir");
 Mockito.verify(lagoonGUI, times(1)).targetURL.setValue("targetURL");
 }

 @Test
 public void testSaveProperties() throws Exception {
 final File propertyFile = new File("test.properties");
 lagoonGUI.savePropertyFile();

 Mockito.verify(lagoonGUI, times(1)).getInputPanel();
 Mockito.verify(lagoonGUI, times(1)).getCenterPanel();
 Mockito.verify(lagoonGUI, times(1)).getButtonPanel();
 Mockito.verify(lagoonGUI, times(1)).getInputPanel();
 Mockito.verify(lagoonGUI, times(1)).getSourceDir();
 Mockito.verify(lagoonGUI, times(1)).getTargetURL();
 Mockito.verify(lagoonGUI, times(1)).getPassword();

 Mockito.verify(frameMock).pack();
 Mockito.verify(lagoonGUI, times(1)).setBackground(Color.lightGray);
 Mockito.verify(propertyFile).mkdirs();
 Mockito.verify(new FileWriter(propertyFile)).write("sitemapFile=sitemapFile\nsourceDir=sourceDir\ntargetURL=targetURL\npassword=\n");
 }
}