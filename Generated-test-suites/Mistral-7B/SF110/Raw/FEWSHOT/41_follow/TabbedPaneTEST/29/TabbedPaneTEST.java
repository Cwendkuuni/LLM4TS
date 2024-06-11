package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;
 private FileFollowingPane fileFollowingPane;

 @Before
 public void setUp() {
 attributes = mock(FollowAppAttributes.class);
 tabbedPane = new TabbedPane(attributes);
 fileFollowingPane = mock(FileFollowingPane.class);
 }

 @Test
 public void testTabbedPaneConstructor() {
 assertEquals(SwingConstants.TOP_ABOVE_COMPONENTS, tabbedPane.getTabPlacement());
 assertNull(tabbedPane.attributes);
 }

 @Test
 public void testTabbedPaneSetAttributes() {
 tabbedPane.setAttributes(attributes);
 assertEquals(attributes, tabbedPane.attributes);
 }

 @Test
 public void testFindComponentAtNullReturn() {
 int x = -1, y = -1;
 assertNull(tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAtComponentReturned() {
 JComponent testComponent = mock(JComponent.class);
 tabbedPane.add(testComponent);
 int x = testComponent.getX(), y = testComponent.getY();
 assertEquals(testComponent, tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAtContainerReturned() {
 JComponent testContainer = mock(JComponent.class);
 JComponent testComponent = mock(JComponent.class);
 testContainer.add(testComponent);
 tabbedPane.add(testContainer);
 int x = testComponent.getX(), y = testComponent.getY();
 assertEquals(testComponent, tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testSetSelectedIndex() {
 int index = 0;
 tabbedPane.setSelectedIndex(index);
 verify(attributes).setLastFileChooserDirectory(tabbedPane.getComponentAt(index).getParentFile());
 }

 @Test
 public void testSetSelectedComponent() {
 tabbedPane.setSelectedComponent(fileFollowingPane);
 verify(attributes).setLastFileChooserDirectory(fileFollowingPane.getFollowedFile().getParentFile());
 }

 @Test
 public void testRemoveTabAt() {
 FileFollowingPane firstTab = mock(FileFollowingPane.class);
 FileFollowingPane secondTab = mock(FileFollowingPane.class);
 tabbedPane.add(firstTab);
 tabbedPane.add(secondTab);
 tabbedPane.removeTabAt(0);
 verify(attributes).setLastFileChooserDirectory(firstTab.getFollowedFile().getParentFile());
 }

 @Test
 public void testHandleSelectedFile() {
 File testFile = new File("testFile.txt");
 FileFollowingPane testPane = new FileFollowingPane(testFile);
 tabbedPane.add(testPane);
 tabbedPane.setSelectedIndex(0);
 tabbedPane.handleSelectedFile();
 assertEquals(testFile.getParentFile(), attributes.getLastFileChooserDirectory());
 }
}