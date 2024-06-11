package ghm.follow.gui;

import static org.junit.Assert.*;

import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

public class TabbedPaneTest {

 private JTabbedPane tabbedPane;
 private FollowAppAttributes attributes;
 private JFileChooser fileChooser;
 private FileFollowingPane fileFollowingPane;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 fileChooser = new JFileChooser();
 fileFollowingPane = new FileFollowingPane();
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 }

 @Test
 public void testFindComponentAt() {
 int x = 10;
 int y = 10;
 Component actual = tabbedPane.findComponentAt(x, y);
 assertNull(actual);

 JTextField textField = new JTextField();
 tabbedPane.addTab("TextField Tab", textField);
 x += textField.getPreferredSize().width + 10;
 y += textField.getPreferredSize().height + 10;
 actual = tabbedPane.findComponentAt(x, y);
 assertNotNull(actual);
 assertEquals(textField, actual);
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane.setSelectedIndex(0);
 assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
 tabbedPane.setSelectedIndex(1);
 assertNull(tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent() {
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
 tabbedPane.setSelectedComponent(fileChooser);
 assertNull(tabbedPane.getSelectedComponent());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAtNegativeIndex() {
 tabbedPane.removeTabAt(-1);
 }

 @Test
 public void testHandleSelectedFile() {
 File testFile = new File("testFile.txt");
 fileFollowingPane.setFollowedFile(testFile);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(testFile.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFileWithNullComponent() {
 tabbedPane.setSelectedComponent(null);
 assertNull(attributes.getLastFileChooserDirectory());
 }
}