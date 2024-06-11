package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TabbedPaneTest {

 @Mock
 private FollowAppAttributes attributes;

 @InjectMocks
 private TabbedPane tabbedPane;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testTabbedPaneConstruction() {
 tabbedPane = new TabbedPane(attributes);
 assertSame(attributes, tabbedPane.attributes);
 }

 @Test
 public void testFindComponentAt_whenCoordinatesNotContained_thenReturnNull() {
 final Point point = new Point(-1, -1);
 assertNull(tabbedPane.findComponentAt(point.x, point.y));
 }

 @Test
 public void testFindComponentAt_whenCoordinatesContainedInVisibleComponent_thenReturnComponent() throws IOException {
 final FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
 when(fileFollowingPane.getFollowedFile()).thenReturn(new File("test.file"));
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 final Component expectedComponent = fileFollowingPane;
 final Point point = new Point(0, 0);
 final ArgumentCaptor<Point> argumentCaptor = ArgumentCaptor.forClass(Point.class);
 tabbedPane.findComponentAt(point.x, point.y);
 assertEquals(expectedComponent, tabbedPane.getSelectedComponent());
 // Verify that findComponentAt method was called with the correct argument
 tabbedPane.verify(fileFollowingPane).findComponentAt(argumentCaptor.capture());
 final Component componentCaptured = argumentCaptor.getValue();
 assertSame(expectedComponent, componentCaptured);
 }

 @Test
 public void testSetSelectedIndex_thenHandleSelectedFile() {
 final FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedIndex(0);
 tabbedPane.handleSelectedFile();
 final File expectedFile = new File(".");
 assertEquals(expectedFile, attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testSetSelectedComponent_thenHandleSelectedFile() {
 final FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 tabbedPane.handleSelectedFile();
 final File expectedFile = new File(".");
 assertEquals(expectedFile, attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testRemoveTabAt_thenHandleSelectedFile() {
 final FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.removeTabAt(0);
 tabbedPane.handleSelectedFile();
 final File expectedFile = null;
 assertEquals(expectedFile, attributes.getLastFileChooserDirectory());
 }
}