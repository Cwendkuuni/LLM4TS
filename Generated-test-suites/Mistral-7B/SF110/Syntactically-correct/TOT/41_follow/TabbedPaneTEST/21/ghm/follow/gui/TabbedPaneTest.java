package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributesMock;

 @Before
 public void setup() {
 attributesMock = mock(FollowAppAttributes.class);
 tabbedPane = new TabbedPane(attributesMock);
 }

 @Test
 public void testFindComponentAt_WithValidCoordinatesAndVisibleComponent_ReturnsComponent() {
 final Component visibleComponent = mock(Component.class);
 final Container container = mock(Container.class);
 when(container.findComponentAt(anyInt(), anyInt())).thenReturn(visibleComponent);
 when(tabbedPane.getComponentAt(0)).thenReturn(container);
 tabbedPane.add(container, "Test Tab");

 final int x = 10;
 final int y = 20;
 final Component result = tabbedPane.findComponentAt(x, y);
 verify(container).findComponentAt(x - container.getX(), y - container.getY());
 assertEquals(visibleComponent, result);
 }

 @Test
 public void testFindComponentAt_WithInvalidCoordinates_ReturnsNull() {
 final int x = 10;
 final int y = 20;
 final Component result = tabbedPane.findComponentAt(x, y);
 assertNull(result);
 }

 @Test
 public void testFindComponentAt_WithOutsideComponent_ReturnsNull() {
 final int x = 10;
 final int y = 20;
 final int tabbedPaneWidth = 300;
 final int tabbedPaneHeight = 400;
 tabbedPane.setBounds(10, 10, tabbedPaneWidth, tabbedPaneHeight);
 tabbedPane.setSelectedIndex(0);

 final int selectedComponentX = 150;
 final int selectedComponentY = 250;
 final Component selectedComponent = tabbedPane.getComponentAt(0);
 final int selectedComponentWidth = selectedComponent.getSize().width;
 final int selectedComponentHeight = selectedComponent.getSize().height;
 final int componentX = selectedComponentX + (selectedComponentWidth / 2);
 final int componentY = selectedComponentY + (selectedComponentHeight / 2);

 final int xWithOffset = componentX - tabbedPane.getX();
 final int yWithOffset = componentY - tabbedPane.getY();

 final Component result = tabbedPane.findComponentAt(xWithOffset, yWithOffset);
 assertNull(result);
 }

 @Test
 public void testSetSelectedIndex_SetsSelectedIndexAndCallsHandleSelectedFile() {
 tabbedPane.add("Tab 1", new JLabel("Tab 1"));
 tabbedPane.add("Tab 2", new JLabel("Tab 2"));
 tabbedPane.add("Tab 3", new JLabel("Tab 3"));
 tabbedPane.setSelectedIndex(1);
 verify(attributesMock).setLastFileChooserDirectory(any(File.class));
 }

 @Test
 public void testSetSelectedComponent_SetsSelectedComponentAndCallsHandleSelectedFile() {
 final FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
 tabbedPane.add("Tab 1", new JLabel("Tab 1"));
 tabbedPane.add(fileFollowingPane, "Tab 2");
 tabbedPane.setSelectedComponent(fileFollowingPane);
 verify(attributesMock).setLastFileChooserDirectory(any(File.class));
 }

 @Test
 public void testRemoveTabAt_RemovesTabAndCallsHandleSelectedFile() {
 tabbedPane.add("Tab 1", new JLabel("Tab 1"));
 tabbedPane.add("Tab 2", new JLabel("Tab 2"));
 tabbedPane.removeTabAt(0);
 verify(attributesMock).setLastFileChooserDirectory(any(File.class));
 }

 @Test
 public void testHandleSelectedFile_SetsLastFileChooserDirectoryWhenSelectedComponentIsFileFollowingPane() {
 final FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
 when(fileFollowingPane.getFollowedFile()).thenReturn(new File("path/to/file"));
 tabbedPane.add("Tab 1", new JLabel("Tab 1"));
 tabbedPane.add(fileFollowingPane, "Tab 2");
 tabbedPane.setSelectedComponent(fileFollowingPane);
 verify(attributesMock).setLastFileChooserDirectory(new File("path/to/file").getParentFile());
 }
}