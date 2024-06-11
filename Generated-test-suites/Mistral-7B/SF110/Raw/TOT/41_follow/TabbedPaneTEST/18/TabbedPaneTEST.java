import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.FileFollowingPane;
import ghm.follow.gui.TabbedPane;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testFindComponentAt_ValidComponent() {
 final FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("test.txt"));
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedIndex(0);

 final Component expectedComponent = fileFollowingPane;
 final Component actualComponent = tabbedPane.findComponentAt(10, 10);

 assertSame("Expected the selected component to be returned", expectedComponent, actualComponent);
 }

 @Test
 public void testFindComponentAt_OutsideComponent() {
 tabbedPane.addTab("Test Tab", new FileFollowingPane(new File("test.txt")));
 tabbedPane.setSelectedIndex(0);

 final Component actualComponent = tabbedPane.findComponentAt(10, 10);

 assertNull("Expected null to be returned as no component was found", actualComponent);
 }

 @Test
 public void testFindComponentAt_NullComponent() {
 final Component nullComponent = null;
 tabbedPane.addTab("Test Tab", new FileFollowingPane(new File("test.txt")));
 tabbedPane.setSelectedIndex(0);

 final Component actualComponent = tabbedPane.findComponentAt(10, 10);

 assertNull("Expected null to be returned as no component was found", actualComponent);
 }

 @Test
 public void testFindComponentAt_ComponentOutsideContainer() {
 final Container container = new JPanel();
 tabbedPane.addTab("Test Tab", container);
 tabbedPane.setSelectedIndex(0);

 final Component expectedComponent = container;
 final Component actualComponent = tabbedPane.findComponentAt(10, 10);

 assertSame("Expected the container to be returned as it is the selected component", expectedComponent, actualComponent);
 }

 @Test
 public void testFindComponentAt_ComponentInsideContainer() {
 final JLabel label = new JLabel("Test Label");
 final Container container = new JPanel();
 container.add(label);
 tabbedPane.addTab("Test Tab", container);
 tabbedPane.setSelectedIndex(0);

 final Component expectedComponent = label;
 final Component actualComponent = tabbedPane.findComponentAt(10, 10);

 assertSame("Expected the label to be returned as it is the selected component", expectedComponent, actualComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 final FileFollowingPane fileFollowingPane1 = new FileFollowingPane(new File("test1.txt"));
 final FileFollowingPane fileFollowingPane2 = new FileFollowingPane(new File("test2.txt"));
 tabbedPane.addTab("Test Tab 1", fileFollowingPane1);
 tabbedPane.addTab("Test Tab 2", fileFollowingPane2);
 tabbedPane.setSelectedIndex(0);

 final File followedFile1 = fileFollowingPane1.getFollowedFile();
 final File lastFileChooserDirectory = attributes.getLastFileChooserDirectory();

 assertSame("Expected the first file's parent file to be set as the last file chooser directory", followedFile1.getParentFile(), lastFileChooserDirectory);

 tabbedPane.setSelectedIndex(1);

 final File followedFile2 = fileFollowingPane2.getFollowedFile();
 final File lastFileChooserDirectory2 = attributes.getLastFileChooserDirectory();

 assertSame("Expected the second file's parent file to be set as the last file chooser directory", followedFile2.getParentFile(), lastFileChooserDirectory2);
 }

 @Test
 public void testSetSelectedComponent() {
 final FileFollowingPane fileFollowingPane1 = new FileFollowingPane(new File("test1.txt"));
 final FileFollowingPane fileFollowingPane2 = new FileFollowingPane(new File("test2.txt"));
 tabbedPane.addTab("Test Tab 1", fileFollowingPane1);
 tabbedPane.addTab("Test Tab 2", fileFollowingPane2);
 tabbedPane.setSelectedComponent(fileFollowingPane1);

 final File followedFile1 = fileFollowingPane1.getFollowedFile();
 final File lastFileChooserDirectory = attributes.getLastFileChooserDirectory();

 assertSame("Expected the first file's parent file to be set as the last file chooser directory", followedFile1.getParentFile(), lastFileChooserDirectory);

 tabbedPane.setSelectedComponent(fileFollowingPane2);

 final File followedFile2 = fileFollowingPane2.getFollowedFile();
 final File lastFileChooserDirectory2 = attributes.getLastFileChooserDirectory();

 assertSame("Expected the second file's parent file to be set as the last file chooser directory", followedFile2.getParentFile(), lastFileChooserDirectory2);
 }

 @Test
 public void testRemoveTabAt() {
 final FileFollowingPane fileFollowingPane1 = new FileFollowingPane(new File("test1.txt"));
 final FileFollowingPane fileFollowingPane2 = new FileFollowingPane(new File("test2.txt"));
 tabbedPane.addTab("Test Tab 1", fileFollowingPane1);
 tabbedPane.addTab("Test Tab 2", fileFollowingPane2);
 tabbedPane.setSelectedIndex(0);
 tabbedPane.removeTabAt(0);

 final File followedFile1 = fileFollowingPane1.getFollowedFile();
 final File lastFileChooserDirectory = attributes.getLastFileChooserDirectory();

 assertNull("Expected the first file's followed file to be null after removal", followedFile1);

 final File followedFile2 = fileFollowingPane2.getFollowedFile();
 final File lastFileChooserDirectory2 = attributes.getLastFileChooserDirectory();

 assertSame("Expected the second file's parent file to be set as the last file chooser directory", followedFile2.getParentFile(), lastFileChooserDirectory2);
 }
}