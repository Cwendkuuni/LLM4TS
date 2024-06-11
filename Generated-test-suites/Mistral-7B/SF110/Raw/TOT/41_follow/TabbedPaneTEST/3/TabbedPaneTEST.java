import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import java.awt.*;
import java.io.File;

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
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 expectedTabbedPane = new JTabbedPane(SwingConstants.TOP);
 }

 @Test
 public void testConstructor() {
 assertEquals(SwingConstants.TOP, tabbedPane.getTabPlacement());
 tabbedPane = new TabbedPane(null);
 assertNull(tabbedPane.attributes);
 }

 @Test
 public void testFindComponentAt() {
 final JPanel testPanel = new JPanel();
 tabbedPane.addTab("Test", testPanel);
 final int x = 10, y = 10;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(testPanel, actualComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 final JPanel testPanel1 = new JPanel();
 final JPanel testPanel2 = new JPanel();
 tabbedPane.addTab("Test1", testPanel1);
 tabbedPane.addTab("Test2", testPanel2);
 tabbedPane.setSelectedIndex(0);
 assertEquals(testPanel1, tabbedPane.getComponentAt(0));
 }

 @Test
 public void testSetSelectedComponent() {
 final JPanel testPanel1 = new JPanel();
 final JPanel testPanel2 = new JPanel();
 tabbedPane.addTab("Test1", testPanel1);
 tabbedPane.addTab("Test2", testPanel2);
 final FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 final JPanel testPanel1 = new JPanel();
 final JPanel testPanel2 = new JPanel();
 tabbedPane.addTab("Test1", testPanel1);
 tabbedPane.addTab("Test2", testPanel2);
 tabbedPane.removeTabAt(0);
 assertEquals(testPanel2, tabbedPane.getComponentAt(0));
 }

 @Test(expected = NullPointerException.class)
 public void testFindComponentAtNullComponent() {
 tabbedPane.addTab("Test", null);
 tabbedPane.findComponentAt(0, 0);
 }

 @Test
 public void testHandleSelectedFile() {
 final FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.addTab("Test", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(fileFollowingPane.getFollowedFile().getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFileNullComponent() {
 tabbedPane.handleSelectedFile();
 assertNull(attributes.getLastFileChooserDirectory());
 }
}