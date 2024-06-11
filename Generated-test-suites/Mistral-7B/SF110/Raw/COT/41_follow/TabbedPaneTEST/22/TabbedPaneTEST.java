package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.FileFollowingPane;
import ghm.follow.gui.TabbedPane;

import java.awt.*;
import java.io.File;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private JFrame frame;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 frame = new JFrame();
 tabbedPane = new TabbedPane(attributes);
 frame.getContentPane().add(tabbedPane);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(() -> tabbedPane.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE));
 }

 @After
 public void tearDown() {
 SwingUtilities.invokeLater(tabbedPane::dispose);
 frame.dispose();
 }

 // Test constructor
 @Test
 public void testTabbedPaneConstructor() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 // Test findComponentAt method
 @Test
 public void testFindComponentAt() {
 Component component = SwingUtilities.getRoot(new JButton("Test"));
 tabbedPane.add(component, "Test Tab");
 int x = component.getX();
 int y = component.getY();
 Component expectedComponent = component;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals("FindComponentAt test failed", expectedComponent, actualComponent);
 }

 // Test setSelectedIndex method
 @Test
 public void testSetSelectedIndex() {
 FileFollowingPane pane1 = new FileFollowingPane(new File("Test1.txt"));
 tabbedPane.add(pane1, "Test Tab 1");
 FileFollowingPane pane2 = new FileFollowingPane(new File("Test2.txt"));
 tabbedPane.add(pane2, "Test Tab 2");
 tabbedPane.setSelectedIndex(0);
 assertEquals("SelectedIndex test failed", pane1, tabbedPane.getSelectedComponent());
 tabbedPane.setSelectedIndex(1);
 assertEquals("SelectedIndex test failed", pane2, tabbedPane.getSelectedComponent());
 }

 // Test setSelectedComponent method
 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane pane1 = new FileFollowingPane(new File("Test1.txt"));
 FileFollowingPane pane2 = new FileFollowingPane(new File("Test2.txt"));
 tabbedPane.add(pane1, "Test Tab 1");
 tabbedPane.add(pane2, "Test Tab 2");
 tabbedPane.setSelectedComponent(pane1);
 assertEquals("SelectedComponent test failed", pane1, tabbedPane.getSelectedComponent());
 tabbedPane.setSelectedComponent(pane2);
 assertEquals("SelectedComponent test failed", pane2, tabbedPane.getSelectedComponent());
 }

 // Test removeTabAt method
 @Test
 public void testRemoveTabAt() {
 FileFollowingPane pane1 = new FileFollowingPane(new File("Test1.txt"));
 FileFollowingPane pane2 = new FileFollowingPane(new File("Test2.txt"));
 tabbedPane.add(pane1, "Test Tab 1");
 tabbedPane.add(pane2, "Test Tab 2");
 tabbedPane.removeTabAt(0);
 assertFalse("RemoveTabAt test failed", tabbedPane.getComponentAt(0) instanceof FileFollowingPane);
 }

 // Test handleSelectedFile method - null selected component
 @Test(expected = NullPointerException.class)
 public void testHandleSelectedFileNullSelectedComponent() {
 tabbedPane.handleSelectedFile();
 }

 // Test handleSelectedFile method - null follow file
 @Test(expected = NullPointerException.class)
 public void testHandleSelectedFileNullFollowFile() {
 FileFollowingPane pane = new FileFollowingPane(null);
 tabbedPane.setSelectedComponent(pane);
 tabbedPane.handleSelectedFile();
 }

 // Test handleSelectedFile method
 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane pane = new FileFollowingPane(new File("Test.txt"));
 tabbedPane.setSelectedComponent(pane);
 File parent = new File("TestDirectory");
 assertEquals("TestHandleSelectedFile test failed", parent, tabbedPane.attributes.getLastFileChooserDirectory());
 }
}