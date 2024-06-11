package ghm.follow.gui;

import static org.junit.Assert.*;
import ghm.follow.config.FollowAppAttributes;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = mock(FollowAppAttributes.class);
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneConstruction() {
 assertEquals(JTabbedPane.BOTTOM, tabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAt_WhenOutsideOfTabbedPane_ReturnsNull() {
 final int x = 10;
 final int y = 20;
 assertNull(tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAt_WhenComponentIsNotVisible_ReturnsNull() {
 final JLabel label = new JLabel("Test Label");
 tabbedPane.addTab("Tab 1", label);
 tabbedPane.setSelectedIndex(0);
 assertNull(tabbedPane.findComponentAt(0, 0));
 }

 @Test
 public void testFindComponentAt_WhenComponentIsVisible_ReturnsComponent() {
 final JLabel label = new JLabel("Test Label");
 tabbedPane.addTab("Tab 1", label);
 tabbedPane.setSelectedIndex(0);
 label.setLocation(0, 0);
 assertTrue(tabbedPane.findComponentAt(0, 0) instanceof JLabel);
 }

 @Test
 public void testSetSelectedIndex_CallsHandleSelectedFile() {
 final FileFollowingPane pane = mock(FileFollowingPane.class);
 tabbedPane.addTab("Test Tab", pane);
 tabbedPane.setSelectedIndex(0);
 verify(attributes).setLastFileChooserDirectory(any(File.class));
 }

 @Test
 public void testSetSelectedComponent_CallsHandleSelectedFile() {
 final FileFollowingPane pane = mock(FileFollowingPane.class);
 tabbedPane.addTab("Test Tab", pane);
 tabbedPane.setSelectedComponent(pane);
 verify(attributes).setLastFileChooserDirectory(any(File.class));
 }

 @Test
 public void testRemoveTabAt_CallsHandleSelectedFile() {
 final FileFollowingPane pane = mock(FileFollowingPane.class);
 tabbedPane.addTab("Test Tab", pane);
 tabbedPane.removeTabAt(0);
 verify(attributes).setLastFileChooserDirectory(any(File.class));
 }

 @Test
 public void testHandleSelectedFile_SetsLastFileChooserDirectory() {
 final FileFollowingPane pane = mock(FileFollowingPane.class);
 final File followedFile = new File("testFile.txt");
 pane.setFollowedFile(followedFile);
 tabbedPane.setSelectedComponent(pane);
 assertEquals(followedFile.getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());
 }
}