package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;
import java.util.*;

import org.junit.*;
import org.mockito.*;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;
 private FileFollowingPane fileFollowingPane;

 @Before
 public void setUp() {
 attributes = mock(FollowAppAttributes.class);
 tabbedPane = new TabbedPane(attributes);
 fileFollowingPane = mock(FileFollowingPane.class);
 }

 @Test
 public void testTabbedPaneConstructor() {
 // Test that the TabbedPane constructor sets the tab placement correctly
 FollowAppAttributes attributes = new FollowAppAttributes();
 attributes.setTabPlacement(SwingConstants.LEFT);
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertEquals(SwingConstants.LEFT, tabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAt() {
 // Test that findComponentAt returns null if the component is not found
 int x = 10, y = 20;
 assertNull(tabbedPane.findComponentAt(x, y));

 // Test that findComponentAt returns the correct component if it is found
 JButton button = new JButton();
 tabbedPane.addTab("Test Tab", button);
 assertEquals(button, tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testSetSelectedIndex() {
 int index = 1;
 tabbedPane.addTab("Tab 1", fileFollowingPane);
 tabbedPane.addTab("Tab 2", fileFollowingPane);
 tabbedPane.setSelectedIndex(index);
 assertEquals("Tab 2", tabbedPane.getTitleAt(index));
 }

 @Test
 public void testSetSelectedComponent() {
 tabbedPane.addTab("Tab 1", fileFollowingPane);
 tabbedPane.addTab("Tab 2", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals("Tab 1", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane.addTab("Tab 1", fileFollowingPane);
 tabbedPane.addTab("Tab 2", fileFollowingPane);
 tabbedPane.removeTabAt(0);
 assertFalse(tabbedPane.getComponentAt(0) instanceof FileFollowingPane);
 }

 @Test
 public void testHandleSelectedFile() {
 // Test that handleSelectedFile sets the last file chooser directory correctly
 File file = new File("test.txt");
 File parent = file.getParentFile();
 FileFollowingPane fileFollowingPane = new FileFollowingPane(attributes, file);
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 tabbedPane.handleSelectedFile();
 assertEquals(parent, attributes.getLastFileChooserDirectory());
 }

 @After
 public void tearDown() {
 tabbedPane = null;
 attributes = null;
 fileFollowingPane = null;
 }
}