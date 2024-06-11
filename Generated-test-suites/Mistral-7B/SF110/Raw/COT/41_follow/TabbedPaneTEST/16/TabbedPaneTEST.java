package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

import org.junit.Test;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private JFrame frame;
 private JTabbedPane expectedTabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 frame = new JFrame();
 expectedTabbedPane = new JTabbedPane(SwingConstants.TOP);
 frame.setJMenuBar(null);
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.add(tabbedPane, BorderLayout.CENTER);
 frame.setSize(600, 600);
 frame.setVisible(true);
 }

 @Test
 public void testTabbedPaneConstructor() {
 // Basic functionality test
 TabbedPane actualTabbedPane = new TabbedPane(attributes);
 assertEquals(expectedTabbedPane.getTabPlacement(), actualTabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAt_WhenCoordinatesOutsideTabbedPane_ExpectNull() {
 int x = 10;
 int y = 20;
 assertNull(tabbedPane.findComponentAt(x, y));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAt_WhenNullComponent_ExpectIllegalArgumentException() {
 tabbedPane.add(null);
 int x = 10;
 int y = 20;
 tabbedPane.findComponentAt(x, y);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFindComponentAt_WhenIndexOutOfBounds_ExpectIndexOutOfBoundsException() {
 tabbedPane.addTab("Tab1", new JLabel("Label1"));
 tabbedPane.findComponentAt(-1, 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFindComponentAt_WhenYOutOfBounds_ExpectIndexOutOfBoundsException() {
 tabbedPane.addTab("Tab1", new JLabel("Label1"));
 tabbedPane.findComponentAt(0, -1);
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane.addTab("Tab1", new JLabel("Label1"));
 tabbedPane.addTab("Tab2", new JLabel("Label2"));
 tabbedPane.setSelectedIndex(1);
 assertEquals("Tab2", tabbedPane.getTitleAt(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndex_WhenIndexOutOfBounds_ExpectIndexOutOfBoundsException() {
 tabbedPane.setSelectedIndex(2);
 }

 @Test(expected = NullPointerException.class)
 public void testSetSelectedComponent_WhenNullComponent_ExpectNullPointerException() {
 tabbedPane.setSelectedComponent(null);
 }

 @Test
 public void testSetSelectedComponent() {
 tabbedPane.addTab("Tab1", new JLabel("Label1"));
 FileFollowingPane pane1 = (FileFollowingPane) tabbedPane.getComponentAt(0);
 tabbedPane.setSelectedComponent(pane1);
 assertEquals(pane1, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane.addTab("Tab1", new JLabel("Label1"));
 tabbedPane.addTab("Tab2", new JLabel("Label2"));
 tabbedPane.removeTabAt(0);
 assertEquals(1, tabbedPane.getTabCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAt_WhenIndexOutOfBounds_ExpectIndexOutOfBoundsException() {
 tabbedPane.removeTabAt(2);
 }

 @Test
 public void testHandleSelectedFile_WhenSelectedComponentIsNull_ExpectNoOp() {
 tabbedPane.setSelectedComponent(null);
 assertFalse(attributes.isLastFileChooserDirectorySet());
 }

 @Test
 public void testHandleSelectedFile_WhenSelectedComponentIsNotNull() {
 File file = new File("testFile.txt");
 FileFollowingPane pane = new FileFollowingPane(file);
 tabbedPane.addTab("Tab1", pane);
 tabbedPane.setSelectedComponent(pane);
 assertTrue(attributes.isLastFileChooserDirectorySet());
 assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

}