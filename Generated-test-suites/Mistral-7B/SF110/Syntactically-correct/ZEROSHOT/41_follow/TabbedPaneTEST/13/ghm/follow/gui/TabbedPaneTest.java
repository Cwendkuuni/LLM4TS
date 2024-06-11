package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneConstructionWithAttributes() {
 TabbedPane expectedTabbedPane = new TabbedPane(attributes);
 assertEquals("TabbedPane constructed correctly with attributes", expectedTabbedPane.getTabPlacement(), tabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAtReturnNullWhenOutsideComponent() {
 int x = 10;
 int y = 20;
 assertNull("findComponentAt returns null when outside component", tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAtReturnComponentWhenInsideComponent() {
 JLabel label = new JLabel("Test Label");
 tabbedPane.addTab("Test Tab", new JPanel(), label);
 int x = 5;
 int y = 5;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertTrue("findComponentAt returns expected component", actualComponent instanceof JLabel);
 assertEquals("findComponentAt returns expected label text", "Test Label", actualComponent.getText());
 }

 @Test
 public void testSetSelectedIndex() {
 JPanel firstTab = new JPanel();
 tabbedPane.addTab("First Tab", firstTab);
 tabbedPane.setSelectedIndex(0);
 FileFollowingPane firstTabFileFollowingPane = (FileFollowingPane) tabbedPane.getComponentAt(0);
 File firstTabFile = new File("FirstTabFile.txt");
 firstTabFileFollowingPane.setFollowedFile(firstTabFile);

 JPanel secondTab = new JPanel();
 tabbedPane.addTab("Second Tab", secondTab);
 tabbedPane.setSelectedIndex(1);
 FileFollowingPane secondTabFileFollowingPane = (FileFollowingPane) tabbedPane.getComponentAt(1);
 File secondTabFile = new File("SecondTabFile.txt");
 secondTabFileFollowingPane.setFollowedFile(secondTabFile);

 assertEquals("Selected index correctly set to first tab", firstTabFile.getAbsolutePath(), tabbedPane.getSelectedComponent().getSelectedFile().getAbsolutePath());
 }

 @Test
 public void testSetSelectedComponent() {
 JPanel firstTab = new JPanel();
 tabbedPane.addTab("First Tab", firstTab);
 FileFollowingPane firstTabFileFollowingPane = (FileFollowingPane) firstTab;
 File firstTabFile = new File("FirstTabFile.txt");
 firstTabFileFollowingPane.setFollowedFile(firstTabFile);

 JPanel secondTab = new JPanel();
 tabbedPane.addTab("Second Tab", secondTab);
 FileFollowingPane secondTabFileFollowingPane = (FileFollowingPane) secondTab;
 File secondTabFile = new File("SecondTabFile.txt");
 secondTabFileFollowingPane.setFollowedFile(secondTabFile);

 tabbedPane.setSelectedComponent(firstTabFileFollowingPane);
 assertEquals("Selected component correctly set to first tab", firstTabFile.getAbsolutePath(), tabbedPane.getSelectedComponent().getSelectedFile().getAbsolutePath());
 }

 @Test
 public void testRemoveTabAt() throws IOException {
 JPanel tabToBeRemoved = new JPanel();
 tabbedPane.addTab("Tab to be removed", tabToBeRemoved);
 assertEquals("Initially 3 components in TabbedPane", 3, tabbedPane.getComponentCount());

 FileFollowingPane tabToBeRemovedFileFollowingPane = (FileFollowingPane) tabbedPane.getComponentAt(0);
 File tabToBeRemovedFile = new File("TabToBeRemovedFile.txt");
 tabToBeRemovedFileFollowingPane.setFollowedFile(tabToBeRemovedFile);

 tabbedPane.removeTabAt(0);
 assertEquals("After removal of tab, 2 components in TabbedPane", 2, tabbedPane.getComponentCount());
 assertNull("Selected component is null after removal of tab", tabbedPane.getSelectedComponent());
 }

 @Test
 public void testHandleSelectedFileSetsLastFileChooserDirectory() {
 JPanel firstTab = new JPanel();
 FileFollowingPane firstTabFileFollowingPane = new FileFollowingPane();
 firstTabFileFollowingPane.setFollowedFile(new File("FirstTabFile.txt"));
 firstTab.add(firstTabFileFollowingPane);
 tabbedPane.addTab("First Tab", firstTab);
 tabbedPane.setSelectedComponent(firstTabFileFollowingPane);

 JPanel secondTab = new JPanel();
 FileFollowingPane secondTabFileFollowingPane = new FileFollowingPane();
 secondTabFileFollowingPane.setFollowedFile(new File("SecondTabFile.txt"));
 secondTab.add(secondTabFileFollowingPane);
 tabbedPane.addTab("Second Tab", secondTab);
 tabbedPane.setSelectedComponent(secondTabFileFollowingPane);

 File expectedLastFileChooserDirectory = new File("SecondTabFile.txt").getParentFile();
 assertEquals("Last file chooser directory correctly set", expectedLastFileChooserDirectory, attributes.getLastFileChooserDirectory());
 }

 @After
 public void tearDown() {
 tabbedPane = null;
 attributes = null;
 }
}