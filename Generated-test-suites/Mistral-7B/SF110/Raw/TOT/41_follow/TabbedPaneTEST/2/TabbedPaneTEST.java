package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.io.*;
import java.awt.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 @Mock private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testFindComponentAt_ValidCoordinates_ReturnsComponent() {
 JComponent component = mock(JComponent.class);
 tabbedPane.add(component, "TestTab");
 int x = 10, y = 20;
 Component result = tabbedPane.findComponentAt(x, y);
 assertTrue(result instanceof JComponent);
 assertEquals(component, result);
 }

 @Test
 public void testFindComponentAt_InvalidCoordinates_ReturnsNull() {
 int x = 10, y = 20;
 Component result = tabbedPane.findComponentAt(x, y);
 assertNull(result);
 }

 @Test
 public void testFindComponentAt_ComponentOutsideBoundaries_ReturnsNull() {
 JComponent component = mock(JComponent.class);
 tabbedPane.add(component, "TestTab");
 int x = 10, y = 50;
 Component result = tabbedPane.findComponentAt(x, y);
 assertNull(result);
 }

 @Test
 public void testSetSelectedIndex_ValidIndex_SetsSelectedIndex() {
 tabbedPane.addTab("TestTab1", new JLabel("Test1"));
 tabbedPane.addTab("TestTab2", new JLabel("Test2"));
 tabbedPane.setSelectedIndex(1);
 assertEquals("TestTab2", tabbedPane.getTitleAt(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndex_InvalidIndex_ThrowsException() {
 tabbedPane.addTab("TestTab1", new JLabel("Test1"));
 tabbedPane.addTab("TestTab2", new JLabel("Test2"));
 tabbedPane.setSelectedIndex(2);
 }

 @Test
 public void testSetSelectedComponent_ValidComponent_SetsSelectedComponent() {
 JComponent component = mock(JComponent.class);
 tabbedPane.add(component, "TestTab");
 tabbedPane.setSelectedComponent(component);
 assertEquals(component, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent_NullComponent_DoesNotThrowException() {
 tabbedPane.setSelectedComponent(null);
 }

 @Test
 public void testRemoveTabAt_ValidIndex_RemovesTab() {
 tabbedPane.addTab("TestTab1", new JLabel("Test1"));
 tabbedPane.addTab("TestTab2", new JLabel("Test2"));
 tabbedPane.removeTabAt(1);
 assertFalse(tabbedPane.containsComponent("TestTab2"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAt_InvalidIndex_ThrowsException() {
 tabbedPane.addTab("TestTab1", new JLabel("Test1"));
 tabbedPane.addTab("TestTab2", new JLabel("Test2"));
 tabbedPane.removeTabAt(2);
 }

 @Test
 public void testHandleSelectedFile_ValidComponent_SetsLastFileChooserDirectory() {
 FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
 when(fileFollowingPane.getFollowedFile()).thenReturn(new File("path/to/file"));
 tabbedPane.add(fileFollowingPane, "TestTab");
 tabbedPane.setSelectedComponent(fileFollowingPane);
 verify(attributes).setLastFileChooserDirectory(fileFollowingPane.getFollowedFile().getParentFile());
 }
}