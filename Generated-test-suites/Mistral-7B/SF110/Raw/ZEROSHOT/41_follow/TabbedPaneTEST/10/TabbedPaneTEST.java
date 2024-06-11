package ghm.follow.gui;

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

 @Test
 public void testTabbedPaneConstructor() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 JLabel label = new JLabel("Test Label");
 tabbedPane.addTab("Test Tab", new JPanel(), label);
 tabbedPane.setSelectedIndex(0);
 Point point = new Point(0, 0);
 Component expectedComponent = label;
 Component actualComponent = tabbedPane.findComponentAt(point.x, point.y);
 assertEquals(expectedComponent, actualComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("test.txt"));
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedIndex(0);
 FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getSelectedComponent();
 assertEquals(fileFollowingPane, selectedPane);
 }

 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("test.txt"));
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getSelectedComponent();
 assertEquals(fileFollowingPane, selectedPane);
 }

 @Test
 public void testRemoveTabAt() {
 JLabel label1 = new JLabel("Test Label 1");
 tabbedPane.addTab("Test Tab 1", new JPanel(), label1);
 JLabel label2 = new JLabel("Test Label 2");
 tabbedPane.addTab("Test Tab 2", new JPanel(), label2);
 tabbedPane.removeTabAt(0);
 assertFalse(tabbedPane.getComponentAt(0) instanceof JLabel);
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("test.txt"));
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedIndex(0);
 assertEquals(fileFollowingPane.getFollowedFile().getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testSetLastFileChooserDirectory() {
 File testDirectory = new File("testDirectory");
 tabbedPane.attributes.setLastFileChooserDirectory(testDirectory);
 assertEquals(testDirectory, tabbedPane.attributes.getLastFileChooserDirectory());
 }
}