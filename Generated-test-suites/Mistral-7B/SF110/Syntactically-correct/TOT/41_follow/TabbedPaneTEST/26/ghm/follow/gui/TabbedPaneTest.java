package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;

import ghm.follow.config.FollowAppAttributes;
import ghm.follow.config.FileFollowingPane;

import org.junit.Before;
import org.junit.Test;

public class TabbedPaneTest {

 private JTabbedPane tabbedPane;
 private FollowAppAttributes attributes;
 private FileFollowingPane fileFollowingPane;

 @Before
 public void setUp() {
 attributes = mock(FollowAppAttributes.class);
 fileFollowingPane = mock(FileFollowingPane.class);
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testFindComponentAt_ValidComponent() {
 JComponent validComponent = new JLabel("Valid Component");
 tabbedPane.add(validComponent, "Test Tab");
 int x = validComponent.getX();
 int y = validComponent.getY();
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertTrue(actualComponent instanceof JLabel);
 assertEquals(validComponent, actualComponent);
 }

 @Test
 public void testFindComponentAt_OutsideComponent() {
 int x = 10;
 int y = 10;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertNull(actualComponent);
 }

 @Test
 public void testSetSelectedIndex_ValidIndex() {
 tabbedPane.addTab("Tab 1", fileFollowingPane);
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.setSelectedIndex(0);
 FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getComponentAt(0);
 assertEquals("Tab 1", tabbedPane.getTitleAt(0));
 assertEquals(fileFollowingPane, selectedPane);
 }

 @Test
 public void testSetSelectedIndex_InvalidIndex() {
 tabbedPane.addTab("Tab 1", fileFollowingPane);
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.setSelectedIndex(2);
 assertNull(tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent_ValidComponent() {
 tabbedPane.addTab("Tab 1", fileFollowingPane);
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.setSelectedComponent(fileFollowingPane);
 FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getComponentAt(0);
 assertEquals("Tab 1", tabbedPane.getTitleAt(0));
 assertEquals(fileFollowingPane, selectedPane);
 }

 @Test
 public void testSetSelectedComponent_NullComponent() {
 tabbedPane.addTab("Tab 1", fileFollowingPane);
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.setSelectedComponent(null);
 assertNull(tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt_ValidIndex() {
 tabbedPane.addTab("Tab 1", fileFollowingPane);
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.removeTabAt(0);
 assertNull(tabbedPane.getComponentAt(0));
 assertEquals("Tab 2", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testRemoveTabAt_InvalidIndex() {
 tabbedPane.addTab("Tab 1", fileFollowingPane);
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.removeTabAt(2);
 assertNull(tabbedPane.getComponentAt(1));
 assertEquals("Tab 1", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testHandleSelectedFile_ValidFileFollowingPane() {
 when(fileFollowingPane.getFollowedFile()).thenReturn(new File("testFile"));
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(new File("testFile").getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFile_NullFileFollowingPane() {
 tabbedPane.setSelectedComponent(null);
 assertNull(attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFile_EmptyFileFollowingPane() {
 FileFollowingPane emptyFileFollowingPane = new FileFollowingPane();
 tabbedPane.setSelectedComponent(emptyFileFollowingPane);
 assertNull(attributes.getLastFileChooserDirectory());
 }
}