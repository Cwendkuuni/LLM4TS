package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 attributes = mock(FollowAppAttributes.class);
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testConstructor() {
 assertEquals(attributes.getTabPlacement(), tabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAt_whenXAndYAreInsideComponent_returnsComponent() throws Exception {
 JLabel label = new JLabel("Test Label");
 Container container = new JPanel();
 container.add(label);
 tabbedPane.addTab("Test Tab", container);
 int x = container.getX() + label.getX();
 int y = container.getY() + label.getY();
 Component component = tabbedPane.findComponentAt(x, y);
 assertEquals(label, component);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAt_whenXAndYAreOutsideComponent_returnsNull() throws Exception {
 int x = 100;
 int y = 100;
 Component component = tabbedPane.findComponentAt(x, y);
 assertNull(component);
 }

 @Test
 public void testSetSelectedIndex() throws Exception {
 JLabel label1 = new JLabel("Test Label 1");
 Container container1 = new JPanel();
 container1.add(label1);
 tabbedPane.addTab("Test Tab 1", container1);

 JLabel label2 = new JLabel("Test Label 2");
 Container container2 = new JPanel();
 container2.add(label2);
 tabbedPane.addTab("Test Tab 2", container2);

 tabbedPane.setSelectedIndex(0);
 assertEquals("Test Tab 1", tabbedPane.getTitleAt(0));

 tabbedPane.setSelectedIndex(1);
 assertEquals("Test Tab 2", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testSetSelectedComponent_whenComponentIsFileFollowingPane_handleSelectedFileIsCalled() throws Exception {
 FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 verify(attributes).setLastFileChooserDirectory(fileFollowingPane.getFollowedFile().getParentFile());
 }

 @Test
 public void testRemoveTabAt() throws Exception {
 JLabel label = new JLabel("Test Label");
 Container container = new JPanel();
 container.add(label);
 tabbedPane.addTab("Test Tab", container);
 int initialSize = tabbedPane.getComponentCount();
 tabbedPane.removeTabAt(0);
 assertEquals(initialSize - 1, tabbedPane.getComponentCount());
 }

 @Test
 public void testHandleSelectedFile() throws Exception {
 FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
 File file = mock(File.class);
 when(fileFollowingPane.getFollowedFile()).thenReturn(file);
 tabbedPane.handleSelectedFile();
 assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
 }
}