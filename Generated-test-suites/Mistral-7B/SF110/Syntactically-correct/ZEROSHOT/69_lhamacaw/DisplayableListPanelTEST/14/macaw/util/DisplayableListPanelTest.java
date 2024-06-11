package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private User user;
 private DisplayableListItemSelector listItemSelectorMock;
 private DisplayableListItemAdder listItemAdderMock;
 private DisplayableListItemEditor listItemEditorMock;
 private DisplayableListItemViewer listItemViewerMock;
 private DisplayableListItemDeleter listItemDeleterMock;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 user = Mockito.mock(User.class);
 Mockito.when(sessionProperties.getProperty("currentUser")).thenReturn(user);
 listItemSelectorMock = Mockito.mock(DisplayableListItemSelector.class);
 listItemAdderMock = Mockito.mock(DisplayableListItemAdder.class);
 listItemEditorMock = Mockito.mock(DisplayableListItemEditor.class);
 listItemViewerMock = Mockito.mock(DisplayableListItemViewer.class);
 listItemDeleterMock = Mockito.mock(DisplayableListItemDeleter.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowWriteAccessAndAllowRecordEditing() {
 // Test construction with allowWriteAccess and allowRecordEditing set to true
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowWriteAccessAndAllowRecordEditingFalse() {
 // Test construction with allowWriteAccess set to true and allowRecordEditing set to false
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowWriteAccessFalse() {
 // Test construction with allowWriteAccess set to false
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false, true);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testInit() {
 // Test initialization of DisplayableListPanel
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);

 // Check that sessionProperties, parentForm, and panelTitleText have been set
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());

 // Check that allowWriteAccess and allowRecordEditing have been set
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testInitWithAllowWriteAccessFalse() {
 // Test initialization of DisplayableListPanel with allowWriteAccess set to false
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", false, true);

 // Check that sessionProperties, parentForm, and panelTitleText have been set
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());

 // Check that allowWriteAccess is false
 assertFalse(displayableListPanel.isAllowWriteAccess());
 }

 @Test
 public void testBuildUI() {
 // Test building UI of DisplayableListPanel
 displayableListPanel.buildUI();

 // Check that titleLabel and displayableList have been added to the panel
 JPanel panel = displayableListPanel.getPanel();
 JLabel titleLabel = (JLabel)panel.getComponent(0);
 assertEquals("Test Panel", titleLabel.getText());
 JScrollPane scrollPane = (JScrollPane)panel.getComponent(1);
 DisplayableList displayableList = (DisplayableList)scrollPane.getViewport().getComponent(0);
 assertNotNull(displayableList);
 }

 @Test
 public void testCreateControlButtonPanel() {
 // Test creating control button panel
 JPanel controlButtonPanel = displayableListPanel.createControlButtonPanel();

 // Check that select, add, edit, view, and delete buttons have been added to the panel
 JButton selectButton = (JButton)controlButtonPanel.getComponent(0);
 assertEquals("general.buttons.select", selectButton.getText());
 JButton addButton = (JButton)controlButtonPanel.getComponent(1);
 assertEquals("general.buttons.add", addButton.getText());
 JButton editButton = (JButton)controlButtonPanel.getComponent(2);
 assertEquals("general.buttons.edit", editButton.getText());
 JButton viewButton = (JButton)controlButtonPanel.getComponent(3);
 assertEquals("general.buttons.view", viewButton.getText());
 JButton deleteButton = (JButton)controlButtonPanel.getComponent(4);
 assertEquals("general.buttons.delete", deleteButton.getText());
 }

 @Test
 public void testSetNewRecord() {
 // Test setting newRecord to true and updating button states
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());

 // Test setting newRecord to false and updating button states
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.isAddEnabled());
 assertTrue(displayableListPanel.isSelectEnabled());
 assertTrue(displayableListPanel.isDeleteEnabled());
 assertTrue(displayableListPanel.isEditEnabled());
 assertTrue(displayableListPanel.isViewEnabled());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 // Test setting listItemSelector and checking if it is the same object
 DisplayableListItemSelector testSelector = Mockito.mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(testSelector);
 assertEquals(testSelector, displayableListPanel.getListItemSelector());
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 // Test setting listItemAdder and checking if it is the same object
 DisplayableListItemAdder testAdder = Mockito.mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(testAdder);
 assertEquals(testAdder, displayableListPanel.getListItemAdder());
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 // Test setting listItemEditor and checking if it is the same object
 DisplayableListItemEditor testEditor = Mockito.mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(testEditor);
 assertEquals(testEditor, displayableListPanel.getListItemEditor());
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 // Test setting listItemViewer and checking if it is the same object
 DisplayableListItemViewer testViewer = Mockito.mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(testViewer);
 assertEquals(testViewer, displayableListPanel.getListItemViewer());
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 // Test setting listItemDeleter and checking if it is the same object
 DisplayableListItemDeleter testDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(testDeleter);
 assertEquals(testDeleter, displayableListPanel.getListItemDeleter());
 }

 @Test
 public void testAddListItem() {
 // Test adding a list item using listItemAdder
 Mockito.doAnswer(invocation -> {
 // Perform some logic here, for example, adding an item to a list
 }).when(listItemAdderMock).addItems(displayableListPanel.getListOwnerName(), displayableListPanel.getDisplayableList());
 displayableListPanel.addListItem();

 // Check that addListItem() method has called listItemAdder.addItems() method
 verify(listItemAdderMock).addItems(displayableListPanel.getListOwnerName(), displayableListPanel.getDisplayableList());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 // Test adding a list item without listItemAdder
 displayableListPanel.addListItem();
 fail("MacawException should have been thrown");
 }

 @Test
 public void testEditSelectedListItem() {
 // Test editing a selected list item using listItemEditor
 Mockito.doAnswer(invocation -> {
 // Perform some logic here, for example, editing an item in a list
 }).when(listItemEditorMock).editListItem(displayableListPanel.getListOwnerName(), Mockito.any(Displayable.class), Mockito.any(DisplayableList.class));
 displayableListPanel.editSelectedListItem();

 // Check that editSelectedListItem() method has called listItemEditor.editListItem() method
 verify(listItemEditorMock).editListItem(displayableListPanel.getListOwnerName(), Mockito.any(Displayable.class), Mockito.any(DisplayableList.class));
 }

 @Test
 public void testViewSelectedListItem() {
 // Test viewing a selected list item using listItemViewer
 Mockito.doAnswer(invocation -> {
 // Perform some logic here, for example, displaying an item in a dialog
 }).when(listItemViewerMock).viewListItem(Mockito.any(Displayable.class));
 displayableListPanel.viewSelectedListItem();

 // Check that viewSelectedListItem() method has called listItemViewer.viewListItem() method
 verify(listItemViewerMock).viewListItem(Mockito.any(Displayable.class));
 }

 @Test
 public void testDeleteSelectedListItems() {
 // Test deleting selected list items using listItemDeleter
 Mockito.doAnswer(invocation -> {
 // Perform some logic here, for example, deleting items from a list
 }).when(listItemDeleterMock).deleteListItems(displayableListPanel.getListOwnerName(), displayableListPanel.getDisplayableList());
 displayableListPanel.deleteSelectedListItems();

 // Check that deleteSelectedListItems() method has called listItemDeleter.deleteListItems() method
 verify(listItemDeleterMock).deleteListItems(displayableListPanel.getListOwnerName(), displayableListPanel.getDisplayableList());
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 // Test deleting selected list items without listItemDeleter
 displayableListPanel.deleteSelectedListItems();
 fail("MacawException should have been thrown");
 }
}