package ghm.follow.gui;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;

import org.junit.Before;
import org.junit.Test;

public class TabbedPaneTest {

 private JTabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = mock(FollowAppAttributes.class);
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testFindComponentAt_ValidComponent() {
 Component component = new JLabel("Test Component");
 tabbedPane.add(component, "Test Tab");
 int x = 10;
 int y = 10;
 Component expectedComponent = component;
 Component result = tabbedPane.findComponentAt(x, y);
 assertSame(expectedComponent, result);
 }

 @Test
 public void testFindComponentAt_OutsideComponent() {
 Component result = tabbedPane.findComponentAt(-1, -1);
 assertNull(result);
 }

 @Test
 public void testFindComponentAt_ContainerComponent() {
 JTabbedPane tabbedPaneWithContainer = new TabbedPane(attributes);
 JPanel container = new JPanel();
 tabbedPaneWithContainer.add(container, "Test Tab");
 int x = 10;
 int y = 10;
 Component expectedComponent = new JLabel("Test Label");
 container.add(expectedComponent);
 Component result = tabbedPaneWithContainer.findComponentAt(x, y);
 assertSame(expectedComponent, result);
 }

 @Test
 public void testSetSelectedIndex_ValidIndex() {
 tabbedPane.addTab("Test Tab 1", mock(Component.class));
 tabbedPane.addTab("Test Tab 2", mock(Component.class));
 tabbedPane.setSelectedIndex(0);
 assertSame("Test Tab 1", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testSetSelectedIndex_InvalidIndex() {
 tabbedPane.addTab("Test Tab 1", mock(Component.class));
 tabbedPane.addTab("Test Tab 2", mock(Component.class));
 tabbedPane.setSelectedIndex(-1);
 assertSame("Test Tab 1", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testSetSelectedComponent_ValidComponent() {
 FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertSame(fileFollowingPane, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent_NullComponent() {
 tabbedPane.setSelectedComponent(null);
 assertNull(tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt_ValidIndex() {
 tabbedPane.addTab("Test Tab 1", mock(Component.class));
 tabbedPane.addTab("Test Tab 2", mock(Component.class));
 tabbedPane.removeTabAt(0);
 assertNull(tabbedPane.getComponentAt(0));
 }

 @Test
 public void testRemoveTabAt_InvalidIndex() {
 tabbedPane.addTab("Test Tab 1", mock(Component.class));
 tabbedPane.addTab("Test Tab 2", mock(Component.class));
 tabbedPane.removeTabAt(2);
 assertSame("Test Tab 1", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testHandleSelectedFile_ValidComponent() {
 FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
 when(fileFollowingPane.getFollowedFile()).thenReturn(new File("Test File"));
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertSame(new File("Test File").getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFile_NullComponent() {
 tabbedPane.handleSelectedFile();
 assertNull(attributes.getLastFileChooserDirectory());
 }
}