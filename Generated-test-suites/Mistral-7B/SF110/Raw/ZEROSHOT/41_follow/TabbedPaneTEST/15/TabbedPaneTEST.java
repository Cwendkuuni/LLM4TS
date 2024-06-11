package ghm.follow.gui.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

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
 assertSame(attributes.getTabPlacement(), tabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAtNullResult() {
 assertNull(tabbedPane.findComponentAt(10, 10));
 }

 @Test
 public void testFindComponentAtComponentResult() {
 when(fileFollowingPane.getX()).thenReturn(10);
 when(fileFollowingPane.getY()).thenReturn(10);
 when(fileFollowingPane.isVisible()).thenReturn(true);
 tabbedPane.addTab("Test", fileFollowingPane);
 Component result = tabbedPane.findComponentAt(10, 10);
 assertSame(fileFollowingPane, result);
 }

 @Test
 public void testSetSelectedIndex() {
 int index = 0;
 tabbedPane.setSelectedIndex(index);
 assertSame(index, tabbedPane.getSelectedIndex());
 }

 @Test
 public void testSetSelectedComponent() {
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertSame(fileFollowingPane, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 int index = 0;
 tabbedPane.addTab("Test", fileFollowingPane);
 tabbedPane.removeTabAt(index);
 assertFalse(tabbedPane.getComponentAt(index) instanceof FileFollowingPane);
 }

 @Test
 public void testHandleSelectedFile() {
 when(fileFollowingPane.getFollowedFile()).thenReturn(new File("TestFolder/TestFile.txt"));
 tabbedPane.setSelectedComponent(fileFollowingPane);
 tabbedPane.handleSelectedFile();
 assertEquals("TestFolder", attributes.getLastFileChooserDirectory().getAbsolutePath());
 }
}