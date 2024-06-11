package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

import org.junit.Before;
import org.junit.Test;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 // Test constructor 
 @Test
 public void testTabbedPaneConstruction() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 // Test findComponentAt method with valid coordinates
 @Test
 public void testFindComponentAtValidCoordinates() {
 Component component = tabbedPane.findComponentAt(10, 10);
 assertNotNull(component);
 }

 // Test findComponentAt method with invalid coordinates
 @Test(expected = NullPointerException.class)
 public void testFindComponentAtInvalidCoordinates() {
 Component component = tabbedPane.findComponentAt(-1, -1);
 fail("Expected NullPointerException");
 }

 // Test setSelectedIndex method
 @Test
 public void testSetSelectedIndex() {
 tabbedPane.setSelectedIndex(0);
 assertEquals(0, tabbedPane.getSelectedIndex());
 }

 // Test setSelectedComponent method
 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane pane = new FileFollowingPane();
 tabbedPane.setSelectedComponent(pane);
 assertEquals(pane, tabbedPane.getSelectedComponent());
 }

 // Test removeTabAt method
 @Test
 public void testRemoveTabAt() {
 FileFollowingPane pane = new FileFollowingPane();
 tabbedPane.addTab("Test Tab", pane);
 tabbedPane.removeTabAt(0);
 assertEquals(0, tabbedPane.getComponentCount());
 }

 // Test handleSelectedFile method with null selected component
 @Test
 public void testHandleSelectedFileNullComponent() {
 tabbedPane.handleSelectedFile();
 assertNull(attributes.getLastFileChooserDirectory());
 }

 // Test handleSelectedFile method with valid selected component
 @Test
 public void testHandleSelectedFileValidComponent() {
 FileFollowingPane pane = new FileFollowingPane();
 pane.setFollowedFile(new File("test/path/file.txt"));
 tabbedPane.addTab("Test Tab", pane);
 tabbedPane.setSelectedComponent(pane);
 tabbedPane.handleSelectedFile();
 assertEquals(new File("test/path"), attributes.getLastFileChooserDirectory());
 }

 // Test exception handling in setSelectedIndex method
 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndexOutOfBounds() {
 tabbedPane.setSelectedIndex(10);
 fail("Expected IndexOutOfBoundsException");
 }

 // Test exception handling in setSelectedComponent method
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedComponentNullComponent() {
 tabbedPane.setSelectedComponent(null);
 fail("Expected IllegalArgumentException");
 }

}