package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

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
 public void testFindComponentAt_ValidComponent() {
 JComponent component = new JLabel("Valid Component");
 tabbedPane.addTab("Tab1", component);
 int x = component.getX() + 10;
 int y = component.getY() + 10;
 Component result = tabbedPane.findComponentAt(x, y);
 assertEquals(component, result);
 }

 @Test
 public void testFindComponentAt_InvalidCoordinates() {
 Component result = tabbedPane.findComponentAt(-10, 0);
 assertNull(result);
 }

 @Test
 public void testFindComponentAt_OutsideComponent() {
 tabbedPane.addTab("Tab1", new JLabel("Tab1"));
 tabbedPane.addTab("Tab2", new JLabel("Tab2"));
 Component result = tabbedPane.findComponentAt(100, 100);
 assertNull(result);
 }

 @Test
 public void testFindComponentAt_NullComponent() {
 JComponent nullComponent = null;
 tabbedPane.addTab("Tab1", nullComponent);
 int x = 0;
 int y = 0;
 Component result = tabbedPane.findComponentAt(x, y);
 assertNull(result);
 }

 @Test
 public void testSetSelectedIndex_ValidIndex() {
 tabbedPane.addTab("Tab1", new JLabel("Tab1"));
 tabbedPane.addTab("Tab2", new JLabel("Tab2"));
 tabbedPane.setSelectedIndex(0);
 assertEquals("Tab1", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testSetSelectedIndex_InvalidIndex() {
 tabbedPane.addTab("Tab1", new JLabel("Tab1"));
 tabbedPane.addTab("Tab2", new JLabel("Tab2"));
 tabbedPane.setSelectedIndex(2);
 assertEquals(null, tabbedPane.getComponentAt(0));
 }

 @Test
 public void testSetSelectedComponent_ValidComponent() {
 JComponent validComponent = new JLabel("Valid Component");
 tabbedPane.addTab("Tab1", validComponent);
 tabbedPane.setSelectedComponent(validComponent);
 assertEquals(validComponent, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent_NullComponent() {
 JComponent nullComponent = null;
 tabbedPane.addTab("Tab1", nullComponent);
 tabbedPane.setSelectedComponent(nullComponent);
 assertNull(tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt_ValidIndex() {
 JLabel label1 = new JLabel("Tab1");
 JLabel label2 = new JLabel("Tab2");
 tabbedPane.addTab("Tab1", label1);
 tabbedPane.addTab("Tab2", label2);
 tabbedPane.removeTabAt(0);
 assertEquals(1, tabbedPane.getComponentCount());
 }

 @Test
 public void testRemoveTabAt_InvalidIndex() {
 JLabel label1 = new JLabel("Tab1");
 JLabel label2 = new JLabel("Tab2");
 tabbedPane.addTab("Tab1", label1);
 tabbedPane.addTab("Tab2", label2);
 tabbedPane.removeTabAt(3);
 assertEquals(2, tabbedPane.getComponentCount());
 }

 @Test
 public void testHandleSelectedFile_ValidFileFollowingPane() {
 File file = new File("testFile.txt");
 FileFollowingPane pane = new FileFollowingPane(file);
 tabbedPane.addTab("Test Tab", pane);
 tabbedPane.setSelectedComponent(pane);
 verify(attributes).setLastFileChooserDirectory(file.getParentFile());
 }

 @Test
 public void testHandleSelectedFile_NullFileFollowingPane() {
 tabbedPane.setSelectedComponent(null);
 verify(attributes, never()).setLastFileChooserDirectory(any());
 }
}