package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Before
 public void setUp() {
 attributes = mock(FollowAppAttributes.class);
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneConstructor() {
 assertEquals(SwingConstants.TOP_ABOVE_COMPONENT, tabbedPane.getTabPlacement());
 assertNull(tabbedPane.attributes);
 }

 @Test
 public void testConstructorWithAttributes() {
 assertEquals(SwingConstants.TOP_ABOVE_COMPONENT, tabbedPane.getTabPlacement());
 assertSame(attributes, tabbedPane.attributes);
 }

 @Test
 public void testFindComponentAt() {
 int x = 10, y = 20;
 tabbedPane.addTab("Tab1", new JLabel("Label1"));
 tabbedPane.addTab("Tab2", new JLabel("Label2"));

 Component component = tabbedPane.findComponentAt(x, y);
 assertNull(component);

 component = tabbedPane.getComponentAt(0);
 assertEquals(0, component.getX());
 assertEquals(0, component.getY());

 component = tabbedPane.findComponentAt(x, y + component.getHeight());
 assertSame(tabbedPane.getComponentAt(1), component);
 }

 @Test
 public void testSetSelectedIndex() {
 FileFollowingPane pane1 = new FileFollowingPane(new File("File1.txt"));
 FileFollowingPane pane2 = new FileFollowingPane(new File("File2.txt"));
 tabbedPane.addTab("Tab1", pane1);
 tabbedPane.addTab("Tab2", pane2);
 tabbedPane.setSelectedIndex(0);

 verify(attributes).setLastFileChooserDirectory(pane1.getFollowedFile().getParentFile());
 }

 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane pane1 = new FileFollowingPane(new File("File1.txt"));
 FileFollowingPane pane2 = new FileFollowingPane(new File("File2.txt"));
 tabbedPane.addTab("Tab1", pane1);
 tabbedPane.addTab("Tab2", pane2);
 tabbedPane.setSelectedComponent(pane1);

 verify(attributes).setLastFileChooserDirectory(pane1.getFollowedFile().getParentFile());
 }

 @Test
 public void testRemoveTabAt() {
 FileFollowingPane pane1 = new FileFollowingPane(new File("File1.txt"));
 FileFollowingPane pane2 = new FileFollowingPane(new File("File2.txt"));
 tabbedPane.addTab("Tab1", pane1);
 tabbedPane.addTab("Tab2", pane2);
 tabbedPane.removeTabAt(0);

 verify(attributes, never()).setLastFileChooserDirectory(pane1.getFollowedFile().getParentFile());
 }

 @Test
 public void testHandleSelectedFileWithNoSelectedComponent() {
 tabbedPane.handleSelectedFile();
 verify(attributes, never()).setLastFileChooserDirectory(anyFile());
 }
}