package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableListParentForm parentForm;
 private DisplayableList displayableList;
 private JPanel panel;
 private JButton select, add, edit, view, delete;
 private User user;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 user = new User("testUserID", "testUserName");
 when(sessionProperties.getProperty("currentUser")).thenReturn(user);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 parentForm = mock(DisplayableListParentForm.class);
 displayableList = mock(DisplayableList.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 panel = displayableListPanel.getPanel();
 select = displayableListPanel.select;
 add = displayableListPanel.add;
 edit = displayableListPanel.edit;
 view = displayableListPanel.view;
 delete = displayableListPanel.delete;
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 sessionProperties = null;
 userInterfaceFactory = null;
 parentForm = null;
 displayableList = null;
 panel = null;
 select = null;
 add = null;
 edit = null;
 view = null;
 delete = null;
 }

 @Test
 public void testDisplayableListPanelWithAllowWriteAccess() {
 // Test constructor with allowWriteAccess = true
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 // Test button states
 assertTrue(select.isEnabled());
 assertTrue(add.isEnabled());
 assertFalse(edit.isEnabled());
 assertFalse(view.isEnabled());
 assertFalse(delete.isEnabled());
 }

 @Test
 public void testDisplayableListPanelWithAllowWriteAccessAndAllowRecordEditing() {
 // Test constructor with allowWriteAccess = true and allowRecordEditing = true
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true, true);
 // Test button states
 assertTrue(select.isEnabled());
 assertTrue(add.isEnabled());
 assertTrue(edit.isEnabled());
 assertTrue(view.isEnabled());
 assertTrue(delete.isEnabled());
 }

 @Test
 public void testDisplayableListPanelWithAllowWriteAccessButNoAllowRecordEditing() {
 // Test constructor with allowWriteAccess = true but no allowRecordEditing
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true, false);
 // Test button states
 assertTrue(select.isEnabled());
 assertTrue(add.isEnabled());
 assertFalse(edit.isEnabled());
 assertTrue(view.isEnabled());
 assertFalse(delete.isEnabled());
 }

 @Test
 public void testDisplayableListPanelWithNoAllowWriteAccess() {
 // Test constructor with allowWriteAccess = false
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false);
 // Test button states
 assertFalse(select.isEnabled());
 assertFalse(add.isEnabled());
 assertFalse(edit.isEnabled());
 assertTrue(view.isEnabled());
 }

 @Test
 public void testAddListItem() {
 // Set up mocks for addListItem() method calls
 DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
 when(parentForm.getListItemAdder()).thenReturn(listItemAdder);
 // Test addListItem() method call
 displayableListPanel.addListItem();
 // Verify addListItem() method was called
 verify(listItemAdder).addItems(displayableListPanel.listOwnerName, displayableList);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 // Set up parentForm without listItemAdder
 when(parentForm.getListItemAdder()).thenReturn(null);
 // Test addListItem() method call
 displayableListPanel.addListItem();
 // Verify MacawException is thrown
 }

 @Test
 public void testEditSelectedListItem() {
 // Set up mocks for editListItem() method calls
 DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
 when(parentForm.getListItemEditor()).thenReturn(listItemEditor);
 // Test editSelectedListItem() method call
 displayableListPanel.editSelectedListItem();
 // Verify editListItem() method was called
 verify(listItemEditor).editListItem(displayableListPanel.listOwnerName, any(Displayable.class), eq(displayableList));
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 // Set up parentForm without listItemEditor
 when(parentForm.getListItemEditor()).thenReturn(null);
 // Test editSelectedListItem() method call
 displayableListPanel.editSelectedListItem();
 // Verify MacawException is thrown
 }

 @Test
 public void testViewSelectedListItem() {
 // Set up mock for viewListItem() method calls
 DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
 when(parentForm.getListItemViewer()).thenReturn(listItemViewer);
 // Test viewSelectedListItem() method call
 displayableListPanel.viewSelectedListItem();
 // Verify viewListItem() method was called
 verify(listItemViewer).viewListItem(any(Displayable.class));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 // Set up parentForm without listItemViewer
 when(parentForm.getListItemViewer()).thenReturn(null);
 // Test viewSelectedListItem() method call
 displayableListPanel.viewSelectedListItem();
 // Verify MacawException is thrown
 }

 @Test
 public void testDeleteSelectedListItems() {
 // Set up mocks for deleteListItems() method calls
 DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
 when(parentForm.getListItemDeleter()).thenReturn(listItemDeleter);
 // Set up JOptionPane.showConfirmDialog() method call
 doAnswer(invocation -> {
 return JOptionPane.showConfirmDialog(any(Component.class), anyString(), anyString(), 0, 2) == 0;
 }).when(parentForm).getConfirmationForDeletion();
 // Test deleteSelectedListItems() method call
 displayableListPanel.deleteSelectedListItems();
 // Verify deleteListItems() method was called
 verify(listItemDeleter).deleteListItems(displayableListPanel.listOwnerName, displayableList);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 // Set up parentForm without listItemDeleter
 when(parentForm.getListItemDeleter()).thenReturn(null);
 // Test deleteSelectedListItems() method call
 displayableListPanel.deleteSelectedListItems();
 // Verify MacawException is thrown
 }

 @Test
 public void testSelectListItems() {
 // Set up mock for selectListItems() method calls
 DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
 when(parentForm.getListItemSelector()).thenReturn(listItemSelector);
 // Test selectListItems() method call
 displayableListPanel.selectListItems();
 // Verify selectListItems() method was called
 verify(listItemSelector).selectListItems(displayableListPanel.listOwnerName, displayableList);
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsWithMissingSelector() {
 // Set up parentForm without listItemSelector
 when(parentForm.getListItemSelector()).thenReturn(null);
 // Test selectListItems() method call
 displayableListPanel.selectListItems();
 // Verify MacawException is thrown
 }

 @Test
 public void testUpdateButtonStates() {
 // Test updateButtonStates() method call with list empty and not new record
 displayableListPanel.setDisplayableItems(new ArrayList<>());
 assertFalse(add.isEnabled());
 assertFalse(delete.isEnabled());
 if (displayableListPanel.allowRecordEditing) {
 assertFalse(edit.isEnabled());
 }
 else {
 assertFalse(view.isEnabled());
 }
 assertTrue(select.isEnabled());
 // Test updateButtonStates() method call with list empty and new record
 displayableListPanel.setNewRecord(true);
 assertFalse(add.isEnabled());
 assertFalse(delete.isEnabled());
 if (displayableListPanel.allowRecordEditing) {
 assertFalse(edit.isEnabled());
 }
 else {
 assertFalse(view.isEnabled());
 }
 assertTrue(select.isEnabled());
 // Test updateButtonStates() method call with list not empty and not new record
 displayableListPanel.setDisplayableItems(new ArrayList<>());
 displayableListPanel.updateButtonStates();
 assertTrue(select.isEnabled());
 if (displayableListPanel.allowRecordEditing) {
 assertTrue(edit.isEnabled());
 }
 else {
 assertTrue(view.isEnabled());
 }
 assertTrue(add.isEnabled());
 assertTrue(delete.isEnabled());
 // Test updateButtonStates() method call with list not empty and new record
 displayableListPanel.setNewRecord(true);
 displayableListPanel.setDisplayableItems(new ArrayList<>());
 displayableListPanel.updateButtonStates();
 assertFalse(add.isEnabled());
 assertFalse(delete.isEnabled());
 if (displayableListPanel.allowRecordEditing) {
 assertFalse(edit.isEnabled());
 }
 else {
 assertFalse(view.isEnabled());
 }
 assertTrue(select.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 // Test setDisplayableItems() method call
 List<Displayable> masterList = new ArrayList<>();
 Displayable d1 = new Displayable();
 Displayable d2 = new Displayable();
 masterList.add(d1);
 masterList.add(d2);
 displayableListPanel.setDisplayableItems(masterList);
 assertTrue(displayableListPanel.displayableList.getAllItems().size() == 2);
 }

 @Test
 public void testSave() {
 // Test save() method call
 displayableListPanel.save();
 assertTrue(displayableListPanel.masterList.size() == displayableListPanel.displayableList.getAllItems().size());
 }

 @Test
 public void testGetAllListItems() {
 // Test getAllListItems() method call
 List<Displayable> allListItems = displayableListPanel.getAllListItems();
 assertTrue(allListItems.size() == displayableListPanel.displayableList.getAllItems().size());
 }

}