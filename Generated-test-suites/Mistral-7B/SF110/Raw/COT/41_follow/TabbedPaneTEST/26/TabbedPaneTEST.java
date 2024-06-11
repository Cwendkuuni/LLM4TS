package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;
import ghm.follow.gui.FileFollowingPane;

import java.awt.*;
import java.io.File;

public class TabbedPaneTest {

 private static final String TEST_FILE_PATH = "testFile.txt";
 private static final String TEST_DIRECTORY_PATH = "testDirectory";

 @org.junit.Before
 public void setUp() {
 FollowAppAttributes attributes = new FollowAppAttributes();
 TabbedPane tabbedPane = new TabbedPane(attributes);
 FileTestHelper.createTestFile(TEST_FILE_PATH);
 FileTestHelper.createTestDirectory(TEST_DIRECTORY_PATH);
 attributes.setLastFileChooserDirectory(new File(TEST_DIRECTORY_PATH));
}

 @org.junit.After
 public void tearDown() {
 FileTestHelper.deleteTestFile(TEST_FILE_PATH);
 FileTestHelper.deleteTestDirectory(TEST_DIRECTORY_PATH);
}

 @org.junit.Test
 public void testTabbedPaneConstructor() {
 TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
 assertNotNull(tabbedPane);
 }

 @org.junit.Test
 public void testFindComponentAt() {
 final JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(800, 600);
 frame.setVisible(true);

 JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
 tabbedPane.addTab("Test Tab", new JLabel("Test Label"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));
 tabbedPane.setBounds(10, 10, 500, 400);
 frame.add(tabbedPane);

 int x = 100, y = 100;
 Component component = tabbedPane.findComponentAt(x, y);
 assertNotNull(component);
}

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testFindComponentAtOutsideBounds() {
 Component component = new TabbedPane(new FollowAppAttributes()).findComponentAt(10, 10);
 assertNull(component);
}

 @org.junit.Test
 public void testSetSelectedIndex() {
 TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
 FileFollowingPane pane1 = new FileFollowingPane(new File("testFile.txt"));
 FileFollowingPane pane2 = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.addTab("Tab 1", pane1);
 tabbedPane.addTab("Tab 2", pane2);
 tabbedPane.setSelectedIndex(1);
 assertEquals(pane2, tabbedPane.getComponentAt(1));
}

 @org.junit.Test
 public void testSetSelectedComponent() {
 TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
 FileFollowingPane pane1 = new FileFollowingPane(new File("testFile.txt"));
 FileFollowingPane pane2 = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.addTab("Tab 1", pane1);
 tabbedPane.addTab("Tab 2", pane2);
 tabbedPane.setSelectedComponent(pane2);
 assertEquals(pane2, tabbedPane.getComponentAt(1));
}

 @org.junit.Test
 public void testRemoveTabAt() {
 TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
 FileFollowingPane pane1 = new FileFollowingPane(new File("testFile.txt"));
 FileFollowingPane pane2 = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.addTab("Tab 1", pane1);
 tabbedPane.addTab("Tab 2", pane2);
 tabbedPane.removeTabAt(0);
 assertNull(tabbedPane.getComponentAt(0));
}

 @org.junit.Test
 public void testHandleSelectedFile() {
 TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
 FileFollowingPane pane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.addTab("Test Tab", pane);
 tabbedPane.setSelectedComponent(pane);
 tabbedPane.handleSelectedFile();
 assertEquals(new File(TEST_DIRECTORY_PATH), tabbedPane.getAttributes().getLastFileChooserDirectory());
}

 @org.junit.Test(expected = NullPointerException.class)
 public void testHandleSelectedFileNullComponent() {
 TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
 tabbedPane.handleSelectedFile();
}
}