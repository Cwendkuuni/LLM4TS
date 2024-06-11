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
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.add(tabbedPane, BorderLayout.CENTER);
 frame.pack();
 frame.setSize(300, 200);
 frame.setVisible(true);
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testTabbedPaneConstructor() {
 TabbedPane actualTabbedPane = new TabbedPane(attributes);
 assertEquals("Constructed TabbedPane with correct tab placement",
 expectedTabbedPane.getTabLayoutPolicy(), actualTabbedPane.getTabLayoutPolicy());
 }

 @Test
 public void testFindComponentAtNull() {
 int x = 10, y = 10;
 assertNull("FindComponentAt returns null when no component is found at given coordinates", tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAtComponent() throws Exception {
 JPanel panel = new JPanel();
 tabbedPane.addTab("Test tab 1", panel);
 int x = 10, y = 10;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertTrue("FindComponentAt returns correct component at given coordinates", panel.equals(actualComponent));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFindComponentAtIndexOutOfBounds() throws Exception {
 int x = 10, y = 10;
 tabbedPane.removeAll();
 tabbedPane.addTab("Test tab 1", new JPanel());
 tabbedPane.addTab("Test tab 2", new JPanel());
 tabbedPane.setSelectedIndex(3); // index out of bounds
 assertNull("FindComponentAt returns null when index is out of bounds", tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAtContainer() throws Exception {
 JPanel container = new JPanel();
 JLabel label = new JLabel("Test label");
 container.add(label);
 tabbedPane.addTab("Test tab 1", container);
 int x = 10, y = 10;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertTrue("FindComponentAt returns correct component at given coordinates when container is involved", label.equals(actualComponent));
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane.addTab("Test tab 1", new JPanel());
 tabbedPane.addTab("Test tab 2", new JPanel());
 tabbedPane.setSelectedIndex(1);
 assertEquals("SetSelectedIndex sets the correct index", 1, tabbedPane.getSelectedIndex());
 }

 @Test
 public void testSetSelectedIndexOutOfBounds() {
 tabbedPane.addTab("Test tab 1", new JPanel());
 tabbedPane.addTab("Test tab 2", new JPanel());
 tabbedPane.setSelectedIndex(2); // index out of bounds
 assertEquals("SetSelectedIndex does not throw exception when index is out of bounds", 0, tabbedPane.getSelectedIndex());
 }

 @Test
 public void testSetSelectedComponentNull() {
 tabbedPane.addTab("Test tab 1", new JPanel());
 tabbedPane.addTab("Test tab 2", new JPanel());
 tabbedPane.setSelectedComponent(null);
 assertEquals("SetSelectedComponent sets the correct component when null is provided", null, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponentNotNull() throws Exception {
 JPanel panel = new JPanel();
 tabbedPane.addTab("Test tab 1", panel);
 tabbedPane.setSelectedComponent(panel);
 assertEquals("SetSelectedComponent sets the correct component", panel, tabbedPane.getSelectedComponent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedComponentInvalidComponentType() throws Exception {
 String text = "Test tab";
 tabbedPane.addTab(text, new String("Test string"));
 tabbedPane.setSelectedComponent(new String("Test string"));
 fail("SetSelectedComponent should throw exception when provided component is not a JComponent");
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane.addTab("Test tab 1", new JPanel());
 tabbedPane.addTab("Test tab 2", new JPanel());
 tabbedPane.removeTabAt(0);
 assertEquals("RemoveTabAt removes the correct tab at given index", 1, tabbedPane.getTabCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAtIndexOutOfBounds() {
 tabbedPane.addTab("Test tab 1", new JPanel());
 tabbedPane.addTab("Test tab 2", new JPanel());
 tabbedPane.removeTabAt(3); // index out of bounds
 assertEquals("RemoveTabAt does not throw exception when index is out of bounds", 2, tabbedPane.getTabCount());
 }

 @Test
 public void testHandleSelectedFileNull() {
 tabbedPane.setSelectedComponent(null);
 assertFalse("HandleSelectedFile is not called when no selected component", tabbedPane.handleSelectedFileCalled);
 }

 @Test
 public void testHandleSelectedFileNotNull() {
 JPanel panel = new JPanel();
 File file = new File("test.txt");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(file);
 tabbedPane.addTab("Test tab 1", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 tabbedPane.handleSelectedFile();
 assertTrue("HandleSelectedFile is called when a selected component is present", tabbedPane.handleSelectedFileCalled);
 }

}