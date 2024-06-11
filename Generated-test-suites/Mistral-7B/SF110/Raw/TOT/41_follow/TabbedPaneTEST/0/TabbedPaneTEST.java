package ghm.follow.gui;

import static org.junit.Assert.*;

import javax.swing.JTabbedPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.*;
import java.io.File;
import ghm.follow.config.FollowAppAttributes;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private JTabbedPane tabbedPane;
 private FileFollowingPane fileFollowingPane;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 fileFollowingPane = new FileFollowingPane();
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 }

 @Test
 public void testFindComponentAt() {
 int x = 10;
 int y = 20;
 Component expectedComponent = fileFollowingPane;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(expectedComponent, actualComponent);
 }

 @Test
 public void testFindComponentAtOutsideComponent() {
 int x = 10;
 int y = 100;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertNull(actualComponent);
 }

 @Test
 public void testFindComponentAtOutsideTabbedPane() {
 int x = 150;
 int y = 150;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertNull(actualComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 int index = 0;
 tabbedPane.setSelectedIndex(index);
 FileFollowingPane actualPane = (FileFollowingPane)tabbedPane.getComponentAt(index);
 FileFollowingPane expectedPane = fileFollowingPane;
 assertEquals(expectedPane, actualPane);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedIndexNegativeIndex() {
 tabbedPane.setSelectedIndex(-1);
 }

 @Test
 public void testSetSelectedComponent() {
 tabbedPane.setSelectedComponent(fileFollowingPane);
 FileFollowingPane actualPane = (FileFollowingPane)tabbedPane.getSelectedComponent();
 assertEquals(fileFollowingPane, actualPane);
 }

 @Test
 public void testSetSelectedComponentNull() {
 tabbedPane.setSelectedComponent(null);
 assertEquals(null, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 int indexToRemove = 0;
 FileFollowingPane tabToRemove = fileFollowingPane;
 tabbedPane.removeTabAt(indexToRemove);
 assertFalse(tabbedPane.getComponentAt(indexToRemove) instanceof FileFollowingPane);
 assertEquals(tabToRemove.getFollowedFile().getName(), attributes.getLastFileChooserDirectory().getName());
 }

 @Test
 public void testRemoveTabAtLastTab() {
 int indexToRemove = tabbedPane.getTabCount() - 1;
 FileFollowingPane tabToRemove = (FileFollowingPane)tabbedPane.getComponentAt(indexToRemove);
 tabbedPane.removeTabAt(indexToRemove);
 assertEquals(null, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testHandleSelectedFile() {
 File followedFile = new File("testfile.txt");
 fileFollowingPane.setFollowedFile(followedFile);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(followedFile.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFileNullComponent() {
 tabbedPane.handleSelectedFile();
 assertNull(attributes.getLastFileChooserDirectory());
 }
}