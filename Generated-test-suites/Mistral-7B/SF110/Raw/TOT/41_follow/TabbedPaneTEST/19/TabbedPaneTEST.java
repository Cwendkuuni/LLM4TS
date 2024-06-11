package ghm.follow.gui;

import static org.junit.Assert.*;

import javax.swing.JTabbedPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import ghm.follow.config.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.io.File;

public class TabbedPaneTest {

 private static final String TEST_FILE = "testFile.txt";
 private static final String TEST_DIRECTORY = "testDirectory";

 @org.junit.BeforeClass
 public static void setupClass() {
 FollowAppAttributes attributes = new FollowAppAttributes();
 attributes.setTabPlacement(JTabbedPane.TOP);
 }

 @org.junit.Before
 public void setUp() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 JTabbedPane tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab", new FileFollowingPane(new File(TEST_FILE)));
 tabbedPane.setPreferredSize(new Dimension(500, 500));
 tabbedPane.setBackground(Color.WHITE);
 frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(() -> tabbedPane.setSelectedIndex(0));
 }

 @org.junit.Test
 public void testFindComponentAt_ValidCoordinates_ReturnsSelectedComponent() {
 int x = 10;
 int y = 10;
 Component expectedComponent = tabbedPane.getComponentAt(0);
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(expectedComponent, actualComponent);
 }

 @org.junit.Test
 public void testFindComponentAt_InvalidCoordinates_ReturnsNull() {
 int x = 10;
 int y = 15;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertNull(actualComponent);
 }

 @org.junit.Test
 public void testSetSelectedIndex_ValidIndex_SetsSelectedTabAndUpdatesLastFileChooserDirectory() {
 File testFile = new File(TEST_FILE);
 File directory = testFile.getParentFile();
 tabbedPane.setSelectedIndex(1);
 FollowAppAttributes attributes = FollowAppAttributes.getInstance();
 assertEquals(directory, attributes.getLastFileChooserDirectory());
 }

 @org.junit.Test
 public void testSetSelectedComponent_ValidComponent_SetsSelectedTabAndUpdatesLastFileChooserDirectory() {
 File testFile = new File(TEST_FILE);
 FileFollowingPane pane = new FileFollowingPane(testFile);
 tabbedPane.setSelectedComponent(pane);
 FollowAppAttributes attributes = FollowAppAttributes.getInstance();
 assertEquals(testFile.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @org.junit.Test
 public void testRemoveTabAt_ValidIndex_RemovesTabAndUpdatesLastFileChooserDirectory() {
 File testFile = new File(TEST_FILE);
 tabbedPane.removeTabAt(0);
 FollowAppAttributes attributes = FollowAppAttributes.getInstance();
 assertNull(attributes.getLastFileChooserDirectory());
 }

 @org.junit.Test
 public void testSetSelectedIndex_InvalidIndex_ThrowsIllegalArgumentException() {
 try {
 tabbedPane.setSelectedIndex(-1);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testSetSelectedComponent_NullComponent_ThrowsNullPointerException() {
 try {
 tabbedPane.setSelectedComponent(null);
 fail();
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @org.junit.Test
 public void testHandleSelectedFile_ValidFile_UpdatesLastFileChooserDirectory() {
 File testFile = new File(TEST_FILE);
 FileFollowingPane pane = new FileFollowingPane(testFile);
 tabbedPane.setSelectedComponent(pane);
 FollowAppAttributes attributes = FollowAppAttributes.getInstance();
 assertEquals(testFile.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @org.junit.Test
 public void testHandleSelectedFile_NullSelectedComponent_DoesNotUpdateLastFileChooserDirectory() {
 tabbedPane.setSelectedComponent(null);
 FollowAppAttributes attributes = FollowAppAttributes.getInstance();
 assertNull(attributes.getLastFileChooserDirectory());
 }

 @org.junit.Test
 public void testSetTabPlacement_ValidValue_SetsTabPlacement() {
 FollowAppAttributes attributes = new FollowAppAttributes();
 attributes.setTabPlacement(JTabbedPane.RIGHT);
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertEquals(JTabbedPane.RIGHT, tabbedPane.getTabPlacement());
 }

 @org.junit.Test
 public void testSetTabPlacement_InvalidValue_ThrowsIllegalArgumentException() {
 FollowAppAttributes attributes = new FollowAppAttributes();
 try {
 attributes.setTabPlacement(JTabbedPane.LEFT_TO_RIGHT);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @org.junit.After
 public void tearDown() {
 SwingUtilities.invokeLater(() -> frame.dispose());
 }

 @org.junit.AfterClass
 public static void teardownClass() {
 FollowAppAttributes.getInstance().clear();
 }

}