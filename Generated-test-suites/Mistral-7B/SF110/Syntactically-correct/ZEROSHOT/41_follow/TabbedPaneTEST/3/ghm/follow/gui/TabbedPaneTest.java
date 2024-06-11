package ghm.follow.gui;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneConstruction() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 int x = 10, y = 20;
 Component component = new JLabel("Test Component");
 tabbedPane.addTab("Test Tab", component);
 tabbedPane.setSelectedComponent(component);
 tabbedPane.validate();

 Component foundComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(component, foundComponent);
 }

 @Test
 public void testFindComponentAt_NotVisible() {
 int x = 10, y = 20;
 Component component = new JLabel("Test Component");
 tabbedPane.addTab("Test Tab", component);
 tabbedPane.validate();
 tabbedPane.setSelectedComponent(tabbedPane);

 Component foundComponent = tabbedPane.findComponentAt(x, y);
 assertNull(foundComponent);
 }

 @Test
 public void testFindComponentAt_WrongComponent() {
 int x = 10, y = 20;
 Component component1 = new JLabel("Test Component 1");
 Component component2 = new JLabel("Test Component 2");
 tabbedPane.addTab("Test Tab 1", component1);
 tabbedPane.addTab("Test Tab 2", component2);
 tabbedPane.validate();
 tabbedPane.setSelectedComponent(component1);

 Component foundComponent = tabbedPane.findComponentAt(x, y);
 assertNotSame(component2, foundComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 int index = 0;
 tabbedPane.setSelectedIndex(index);
 tabbedPane.validate();
 tabbedPane.handleSelectedFile();

 FileFollowingPane pane = (FileFollowingPane) tabbedPane.getComponentAt(index);
 File expectedParent = new File("."); // Default initial directory
 File actualParent = attributes.getLastFileChooserDirectory();
 assertEquals(expectedParent, actualParent);
 }

 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane pane = new FileFollowingPane();
 File file = new File("Test File");
 pane.setFollowedFile(file);
 tabbedPane.addTab("Test Tab", pane);
 tabbedPane.validate();
 tabbedPane.setSelectedComponent(pane);
 tabbedPane.handleSelectedFile();

 File expectedParent = file.getParentFile();
 File actualParent = attributes.getLastFileChooserDirectory();
 assertEquals(expectedParent, actualParent);
 }

 @Test
 public void testRemoveTabAt() throws IOException {
 File file1 = new File("Test File 1");
 FileFollowingPane pane1 = new FileFollowingPane();
 pane1.setFollowedFile(file1);
 tabbedPane.addTab("Test Tab 1", pane1);
 tabbedPane.validate();

 File file2 = new File("Test File 2");
 FileFollowingPane pane2 = new FileFollowingPane();
 pane2.setFollowedFile(file2);
 tabbedPane.addTab("Test Tab 2", pane2);
 tabbedPane.validate();
 tabbedPane.setSelectedComponent(pane2);
 tabbedPane.handleSelectedFile();

 tabbedPane.removeTabAt(0);
 tabbedPane.validate();
 tabbedPane.handleSelectedFile();

 File expectedParent = file2.getParentFile();
 File actualParent = attributes.getLastFileChooserDirectory();
 assertEquals(expectedParent, actualParent);
 }

 @Test
 public void testRemoveTabAt_EmptyTabbedPane() {
 tabbedPane.removeTabAt(0);
 tabbedPane.validate();
 tabbedPane.handleSelectedFile();

 File expectedParent = null;
 File actualParent = attributes.getLastFileChooserDirectory();
 assertNull(actualParent);
 }
}