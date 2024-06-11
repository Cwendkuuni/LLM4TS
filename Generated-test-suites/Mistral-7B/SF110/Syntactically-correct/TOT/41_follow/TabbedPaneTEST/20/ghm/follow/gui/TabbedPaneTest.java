import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.gui.TabbedPane;
import ghm.follow.config.FollowAppAttributes;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.File;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.add(new FileFollowingPane(new File("testFile1.txt")));
 tabbedPane.add(new FileFollowingPane(new File("testFile2.txt")));
 tabbedPane.setPreferredSize(new Dimension(500, 500));
 }

 @Test
 public void testFindComponentAt() {
 int x = 50, y = 50;
 Component expectedComponent = tabbedPane.getComponentAt(1); // second tab
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(expectedComponent, actualComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane.setSelectedIndex(1);
 FileFollowingPane actualPane = (FileFollowingPane)tabbedPane.getSelectedComponent();
 File expectedFile = new File("testFile2.txt");
 File actualFile = actualPane.getFollowedFile();
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane newPane = new FileFollowingPane(new File("testFile3.txt"));
 tabbedPane.setSelectedComponent(newPane);
 File expectedFile = new File("testFile3.txt");
 File actualFile = ((FileFollowingPane)tabbedPane.getSelectedComponent()).getFollowedFile();
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane.removeTabAt(1);
 assertEquals(1, tabbedPane.getComponentCount());
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane pane = (FileFollowingPane)tabbedPane.getSelectedComponent();
 File actualParent = pane.getFollowedFile().getParentFile();
 File expectedParent = new File("."); // assuming testFile1.txt is the initial selected file
 assertEquals(expectedParent, actualParent);
 tabbedPane.setSelectedIndex(1); // change selection to testFile2.txt
 actualParent = pane.getFollowedFile().getParentFile();
 assertEquals(new File(".").getParentFile(), actualParent); // test parent directory is updated correctly
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testInvalidIndex() {
 tabbedPane.setSelectedIndex(100);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullComponent() {
 tabbedPane.setSelectedComponent(null);
 }
}