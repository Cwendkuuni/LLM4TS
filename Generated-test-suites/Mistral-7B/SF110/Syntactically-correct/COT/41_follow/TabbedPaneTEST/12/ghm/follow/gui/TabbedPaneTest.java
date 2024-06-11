package ghm.follow.gui.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Point;
import java.io.File;

import org.junit.Test;

import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Test
 public void testTabbedPaneConstructor() {
 tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAtNullResult() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.setComponentAt(0, new JLabel("Label 1"));
 tabbedPane.setComponentAt(1, new JLabel("Label 2"));
 tabbedPane.setSelectedIndex(0);

 Point point = new Point(10, 10);
 Component result = tabbedPane.findComponentAt(point.x, point.y);
 assertNull(result);
 }

 @Test
 public void testFindComponentAtComponentResult() throws Exception {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.setComponentAt(0, new JLabel("Label 1"));
 tabbedPane.setComponentAt(1, new JLabel("Label 2"));
 tabbedPane.setSelectedIndex(0);

 Component expectedComponent = tabbedPane.getComponentAt(0);
 Point point = new Point(0, 0);
 Component result = tabbedPane.findComponentAt(point.x, point.y);
 assertSame(expectedComponent, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedIndexOutOfBounds() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.setComponentAt(0, new JLabel("Label 1"));
 tabbedPane.setComponentAt(1, new JLabel("Label 2"));

 tabbedPane.setSelectedIndex(2);
 }

 @Test
 public void testSetSelectedComponentNull() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.setComponentAt(0, new JLabel("Label 1"));
 tabbedPane.setComponentAt(1, new JLabel("Label 2"));
 tabbedPane.setSelectedComponent(null);

 assertNull(tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponentFileFollowingPane() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.setComponentAt(0, new FileFollowingPane(new File("test1.txt")));
 tabbedPane.setComponentAt(1, new FileFollowingPane(new File("test2.txt")));
 FileFollowingPane expectedPane = (FileFollowingPane)tabbedPane.getComponentAt(0);
 tabbedPane.setSelectedComponent(expectedPane);

 FileFollowingPane resultPane = (FileFollowingPane)tabbedPane.getSelectedComponent();
 assertSame(expectedPane, resultPane);
 }

 @Test
 public void testRemoveTabAtZeroIndex() throws Exception {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.setComponentAt(0, new JLabel("Label 1"));
 tabbedPane.setComponentAt(1, new JLabel("Label 2"));
 tabbedPane.setSelectedIndex(0);

 tabbedPane.removeTabAt(0);

 assertEquals(1, tabbedPane.getComponentCount());
 }

 @Test
 public void testRemoveTabAtLastIndex() throws Exception {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.setComponentAt(0, new JLabel("Label 1"));
 tabbedPane.setComponentAt(1, new JLabel("Label 2"));
 tabbedPane.setSelectedIndex(1);

 tabbedPane.removeTabAt(1);

 assertEquals(1, tabbedPane.getComponentCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAtOutOfBoundsIndex() throws Exception {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.setComponentAt(0, new JLabel("Label 1"));
 tabbedPane.setComponentAt(1, new JLabel("Label 2"));
 tabbedPane.setSelectedIndex(2);

 tabbedPane.removeTabAt(1);
 }

 @Test
 public void testHandleSelectedFileNullResult() {
 tabbedPane = new TabbedPane(attributes);
 FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
 tabbedPane.setSelectedComponent(pane);
 tabbedPane.handleSelectedFile();

 assertNull(attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFileNonNullResult() {
 tabbedPane = new TabbedPane(attributes);
 FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
 tabbedPane.setSelectedComponent(pane);
 tabbedPane.handleSelectedFile();

 File expectedDirectory = new File("test");
 File resultDirectory = attributes.getLastFileChooserDirectory();
 assertSame(expectedDirectory, resultDirectory);
 }

}