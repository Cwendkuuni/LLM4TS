package macaw.util;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.*;
import org.junit.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 log = mock(Log.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccessTrue() {
 // Test constructor with allowWriteAccess true
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccessFalse() {
 // Test constructor with allowWriteAccess false
 DisplayableListPanel displayableListPanelWithAllowWriteAccessFalse = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false, false);
 assertFalse(displayableListPanelWithAllowWriteAccessFalse.isAllowWriteAccess());
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowRecordEditingTrue() {
 // Test constructor with allowRecordEditing true
 DisplayableListPanel displayableListPanelWithAllowRecordEditing = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertTrue(displayableListPanelWithAllowRecordEditing.isAllowRecordEditing());
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowRecordEditingFalse() {
 // Test constructor with allowRecordEditing false
 DisplayableListPanel displayableListPanelWithAllowRecordEditingFalse = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 assertFalse(displayableListPanelWithAllowRecordEditingFalse.isAllowRecordEditing());
 }

 @Test
 public void testBuildUI() {
 // Test buildUI method
 verify(userInterfaceFactory, times(1)).createPanel();
 verify(userInterfaceFactory, times(1)).createLabel("Test Panel");
 verify(userInterfaceFactory, times(1)).createScrollPane(displayableListPanel.displayableList.getListScrollPane());
 }

 @Test
 public void testSetNewRecordTrue() {
 // Test setNewRecord method with isNewRecord true
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetNewRecordFalse() {
 // Test setNewRecord method with isNewRecord false
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 if (displayableListPanel.displayableList.isEmpty()) {
 assertFalse(displayableListPanel.delete.isEnabled());
 if (displayableListPanel.allowRecordEditing) {
 assertFalse(displayableListPanel.edit.isEnabled());
 } else {
 assertFalse(displayableListPanel.view.isEnabled());
 }
 } else {
 assertTrue(displayableListPanel.delete.isEnabled());
 if (displayableListPanel.allowRecordEditing) {
 assertTrue(displayableListPanel.edit.isEnabled());
 } else {
 assertTrue(displayableListPanel.view.isEnabled());
 }
 }
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 // Test setDisplayableListItemSelector method
 DisplayableListItemSelector mockListItemSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockListItemSelector);
 assertEquals(mockListItemSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 // Test setDisplayableListItemAdder method
 DisplayableListItemAdder mockListItemAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(mockListItemAdder);
 assertEquals(mockListItemAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 // Test setDisplayableListItemEditor method
 DisplayableListItemEditor mockListItemEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(mockListItemEditor);
 assertEquals(mockListItemEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 // Test setDisplayableListItemViewer method
 DisplayableListItemViewer mockListItemViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(mockListItemViewer);
 assertEquals(mockListItemViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 // Test setDisplayableListItemDeleter method
 DisplayableListItemDeleter mockListItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(mockListItemDeleter);
 assertEquals(mockListItemDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 // Test addListItem method
 doNothing().when(parentForm).commitChanges();
 displayableListPanel.addListItem();
 verify(displayableListPanel.listItemAdder, times(1)).addItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 // Test addListItem method with missing adder
 doNothing().when(parentForm).commitChanges();
 displayableListPanel.listItemAdder = null;
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 // Test editSelectedListItem method
 doNothing().when(parentForm).commitChanges();
 doNothing().when(displayableListPanel.listItemEditor).editListItem(displayableListPanel.listOwnerName, any(Displayable.class), any(DisplayableList.class));
 displayableListPanel.editSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 // Test editSelectedListItem method with missing editor
 doNothing().when(parentForm).commitChanges();
 displayableListPanel.listItemEditor = null;
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 // Test viewSelectedListItem method
 doNothing().when(parentForm).commitChanges();
 doNothing().when(displayableListPanel.listItemViewer).viewListItem(any(Displayable.class));
 displayableListPanel.viewSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 // Test viewSelectedListItem method with missing viewer
 doNothing().when(parentForm).commitChanges();
 displayableListPanel.listItemViewer = null;
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 // Test deleteSelectedListItems method
 doNothing().when(parentForm).commitChanges();
 int result = JOptionPane.showConfirmDialog(mock(JFrame.class), "This is a warning message", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
 doNothing().when(displayableListPanel.listItemDeleter).deleteListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 displayableListPanel.deleteSelectedListItems();
 verify(displayableListPanel.listItemDeleter, times(1)).deleteListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 // Test deleteSelectedListItems method with missing deleter
 doNothing().when(parentForm).commitChanges();
 displayableListPanel.listItemDeleter = null;
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 // Test selectListItems method
 doNothing().when(parentForm).commitChanges();
 doNothing().when(displayableListPanel.listItemSelector).selectListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 displayableListPanel.selectListItems();
 verify(displayableListPanel.listItemSelector, times(1)).selectListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }

 @Test
 public void testUpdateButtonStates() {
 // Test updateButtonStates method
 Displayable masterDisplayable = mock(Displayable.class);
 DisplayableList mockDisplayableList = mock(DisplayableList.class);
 when(mockDisplayableList.getAllItems()).thenReturn(Arrays.asList(masterDisplayable));
 displayableListPanel.displayableList = mockDisplayableList;
 displayableListPanel.isNewRecord = false;
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.allowRecordEditing = true;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithAllowWriteAccessFalse() {
 // Test updateButtonStates method with allowWriteAccess false
 Displayable masterDisplayable = mock(Displayable.class);
 DisplayableList mockDisplayableList = mock(DisplayableList.class);
 when(mockDisplayableList.getAllItems()).thenReturn(Collections.singletonList(masterDisplayable));
 displayableListPanel.displayableList = mockDisplayableList;
 displayableListPanel.isNewRecord = false;
 displayableListPanel.allowWriteAccess = false;
 displayableListPanel.allowRecordEditing = true;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 if (displayableListPanel.displayableList.isEmpty()) {
 assertFalse(displayableListPanel.view.isEnabled());
 } else {
 assertTrue(displayableListPanel.view.isEnabled());
 }
 }

 @Test
 public void testUpdateButtonStatesWithDisplayableListEmpty() {
 // Test updateButtonStates method with displayableList empty
 Displayable masterDisplayable = mock(Displayable.class);
 DisplayableList mockDisplayableList = mock(DisplayableList.class);
 when(mockDisplayableList.getAllItems()).thenReturn(Collections.emptyList());
 displayableListPanel.displayableList = mockDisplayableList;
 displayableListPanel.isNewRecord = false;
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.allowRecordEditing = false;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithDisplayableListNotEmptyAndAllowRecordEditingFalse() {
 // Test updateButtonStates method with displayableList not empty and allowRecordEditing false
 Displayable masterDisplayable = mock(Displayable.class);
 DisplayableList mockDisplayableList = mock(DisplayableList.class);
 when(mockDisplayableList.getAllItems()).thenReturn(Arrays.asList(masterDisplayable));
 displayableListPanel.displayableList = mockDisplayableList;
 displayableListPanel.isNewRecord = false;
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.allowRecordEditing = false;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithDisplayableListNotEmptyAndAllowRecordEditingTrue() {
 // Test updateButtonStates method with displayableList not empty and allowRecordEditing true
 Displayable masterDisplayable = mock(Displayable.class);
 DisplayableList mockDisplayableList = mock(DisplayableList.class);
 when(mockDisplayableList.getAllItems()).thenReturn(Arrays.asList(masterDisplayable));
 displayableListPanel.displayableList = mockDisplayableList;
 displayableListPanel.isNewRecord = false;
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.allowRecordEditing = true;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 // Test setDisplayableItems method
 ArrayList<Displayable> masterList = new ArrayList<Displayable>();
 Displayable displayable = mock(Displayable.class);
 masterList.add(displayable);
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSave() {
 // Test save method
 ArrayList<Displayable> masterList = new ArrayList<Displayable>();
 Displayable currentListItem = mock(Displayable.class);
 masterList.add(currentListItem);
 displayableListPanel.displayableList.setDisplayItems(masterList);
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.masterList);
 }
}