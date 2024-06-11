import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class jgaapGUITest {

 private JFrame frame;
 private jgaapGUI gui;

 @Test
 public void testConstructor() {
 gui = new jgaapGUI();
 assertNotNull(gui);
 }

 @Test
 public void testInitComponents() {
 gui = new jgaapGUI();
 assertNotNull(gui.menuBar);
 assertNotNull(gui.menuFile);
 assertNotNull(gui.menuFileExit);
 assertNotNull(gui.menuFileDemo);
 assertNotNull(gui.menuHelp);
 assertNotNull(gui.menuHelpContents);
 assertNotNull(gui.menuHelpAbout);
 assertNotNull(gui.paneOpts);
 // Add more assertions for other components as needed
 }

 @Test
 public void testMenuFileExit() {
 gui = new jgaapGUI();
 gui.menuFileExit.doClick();
 assertTrue(guiIsClosed());
 }

 @Test
 public void testMenuFileDemo() {
 gui = new jgaapGUI();
 gui.menuFileDemo.doClick();
 assertTrue(gui.loadDemoCalled());
 }

 @Test(expected = NullPointerException.class)
 public void testImportNullFile() {
 gui = new jgaapGUI();
 gui.chooseFile.setSelectedFile(null);
 gui.buttonLocal.doClick();
 }

 @Test
 public void testImportKnownDocument() {
 gui = new jgaapGUI();
 JFileChooser chooser = mock(JFileChooser.class);
 when(chooser.showOpenDialog(eq(gui))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(chooser.getSelectedFile()).thenReturn(new File("test.txt"));
 gui.chooseFile = chooser;
 gui.buttonLocal.doClick();
 String expected = "test.txt";
 assertEquals(expected, gui.listKnown.getText().substring(gui.listKnown.getText().lastIndexOf("\n") + 1));
 }

 @Test
 public void testImportUnknownDocument() {
 gui = new jgaapGUI();
 JFileChooser chooser = mock(JFileChooser.class);
 when(chooser.showOpenDialog(eq(gui))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(chooser.getSelectedFile()).thenReturn(new File("test.txt"));
 gui.chooseFile = chooser;
 gui.buttonLocal.doClick();
 String expected = "test.txt";
 assertEquals(expected, gui.listUnknown.getText().substring(gui.listUnknown.getText().lastIndexOf("\n") + 1));
 }

 @Test
 public void testCanonicize() {
 gui = new jgaapGUI();
 gui.buttonCanonicize.doClick();
 // Add assertions to check the state of the GUI or other objects after canonicize button pressed
 }

 // Add more test cases for other methods and edge cases as needed

 private boolean guiIsClosed() {
 Frame frame = Toolkit.getDefaultToolkit().getFrames()[0];
 return frame == null;
 }

 private boolean guiLoadDemoCalled() {
 // Add code here to check if the loadDemo() method has been called in the jgaapGUI class
 return false;
 }
}