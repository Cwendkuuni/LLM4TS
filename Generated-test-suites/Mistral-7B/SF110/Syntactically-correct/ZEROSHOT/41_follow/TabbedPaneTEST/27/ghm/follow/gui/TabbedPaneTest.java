package ghm.follow.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneConstruction() {
 // Test that the TabbedPane is initialized with the correct tab placement
 int tabPlacement = attributes.getTabPlacement();
 assertEquals(tabPlacement, tabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAt() {
 Point point = new Point(10, 20);
 Component expectedComponent = new JLabel("Expected Component");
 tabbedPane.add(expectedComponent, "Tab 1");

 Component actualComponent = tabbedPane.findComponentAt(point.x, point.y);
 assertEquals(expectedComponent, actualComponent);
 }

 @Test
 public void testFindComponentAt_OutsideComponent() {
 Point point = new Point(10, 20);
 tabbedPane.add(new JLabel("Component 1"), "Tab 1");
 tabbedPane.add(new JLabel("Component 2"), "Tab 2");

 Component actualComponent = tabbedPane.findComponentAt(point.x, point.y);
 assertNull(actualComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 int indexToSelect = 1;
 tabbedPane.add(new JLabel("Tab 1"), "Tab 1");
 tabbedPane.add(new JLabel("Tab 2"), "Tab 2");
 tabbedPane.setSelectedIndex(indexToSelect);

 FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getComponentAt(indexToSelect);
 assertNotNull(selectedPane);
 }

 @Test
 public void testSetSelectedComponent() {
 JLabel label = new JLabel("Component to set as selected");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("testFile.txt"), attributes);
 fileFollowingPane.add(label);
 tabbedPane.add(fileFollowingPane, "Tab 1");
 tabbedPane.setSelectedComponent(fileFollowingPane);

 FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getSelectedComponent();
 assertEquals(fileFollowingPane, selectedPane);
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane.add(new JLabel("Tab 1"), "Tab 1");
 tabbedPane.add(new JLabel("Tab 2"), "Tab 2");
 tabbedPane.removeTabAt(0);

 int expectedNumberOfTabs = 1;
 int actualNumberOfTabs = tabbedPane.getTabCount();
 assertEquals(expectedNumberOfTabs, actualNumberOfTabs);
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("testFile.txt"), attributes);
 tabbedPane.add(fileFollowingPane, "Tab 1");
 tabbedPane.setSelectedComponent(fileFollowingPane);

 File expectedParentDirectory = new File("directory/where/testFile.txt/is/located");
 File actualParentDirectory = attributes.getLastFileChooserDirectory();
 assertEquals(expectedParentDirectory, actualParentDirectory);
 }
}