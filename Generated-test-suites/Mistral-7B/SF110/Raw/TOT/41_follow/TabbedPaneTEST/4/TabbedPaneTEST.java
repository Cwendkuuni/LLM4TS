package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 attributes = mock(FollowAppAttributes.class);
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testFindComponentAt_ValidComponent() {
 JComponent component = new JLabel("Test Component");
 tabbedPane.addTab("Test Tab", component);
 int x = component.getX();
 int y = component.getY();
 JComponent returnedComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(component, returnedComponent);
 }

 @Test
 public void testFindComponentAt_NoComponent() {
 tabbedPane.findComponentAt(-1, -1);
 assertNull(tabbedPane.findComponentAt(-1, -1));
 }

 @Test
 public void testFindComponentAt_ComponentNotVisible() {
 JComponent component = new JLabel("Test Component");
 tabbedPane.addTab("Test Tab", component);
 component.setVisible(false);
 int x = component.getX();
 int y = component.getY();
 JComponent returnedComponent = tabbedPane.findComponentAt(x, y);
 assertNull(returnedComponent);
 }

 @Test
 public void testFindComponentAt_Container() {
 JPanel container = new JPanel();
 tabbedPane.addTab("Test Tab", container);
 int x = container.getX();
 int y = container.getY();
 JComponent component = new JLabel("Test Component");
 container.add(component);
 JComponent returnedComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(component, returnedComponent);
 }

 @Test
 public void testFindComponentAt_ContainerNotVisible() {
 JPanel container = new JPanel();
 tabbedPane.addTab("Test Tab", container);
 container.setVisible(false);
 int x = container.getX();
 int y = container.getY();
 JComponent component = new JLabel("Test Component");
 container.add(component);
 JComponent returnedComponent = tabbedPane.findComponentAt(x, y);
 assertNull(returnedComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Component 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Component 2"));
 tabbedPane.setSelectedIndex(1);
 assertEquals("Test Tab 2", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
 }

 @Test
 public void testSetSelectedIndex_InvalidIndex() {
 tabbedPane.setSelectedIndex(-1);
 assertEquals(0, tabbedPane.getSelectedIndex());
 }

 @Test
 public void testSetSelectedComponent() {
 JComponent component = new JLabel("Test Component");
 tabbedPane.addTab("Test Tab", component);
 tabbedPane.setSelectedComponent(component);
 assertEquals(component, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent_NullComponent() {
 tabbedPane.setSelectedComponent(null);
 assertNull(tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Component 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Component 2"));
 tabbedPane.removeTabAt(1);
 assertEquals(1, tabbedPane.getComponentCount());
 }

 @Test
 public void testRemoveTabAt_InvalidIndex() {
 tabbedPane.removeTabAt(-1);
 assertEquals(2, tabbedPane.getComponentCount());
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
 tabbedPane.setSelectedComponent(pane);
 assertEquals("test.txt", tabbedPane.attributes.getLastFileChooserDirectory().getPath());
 }
}