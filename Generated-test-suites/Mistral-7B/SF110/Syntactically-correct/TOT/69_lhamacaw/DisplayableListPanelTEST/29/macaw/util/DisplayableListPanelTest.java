package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import macaw.businessLayer.Displayable;
import macaw.system.Log;
import macaw.system.MacawException;
import macaw.system.MacawMessages;
import macaw.system.SessionProperties;
import macaw.system.User;
import macaw.system.UserInterfaceFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableListParentForm parentForm;
 private Log log;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 sessionProperties = mock(SessionProperties.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 parentForm = mock(DisplayableListParentForm.class);
 log = mock(Log.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 }

 @Test
 public void testInit() {
 // Test initialization with valid arguments
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 verify(parentForm, times(1)).commitChanges();
 // Test initialization with missing arguments
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 verify(parentForm, never()).commitChanges();
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 verify(parentForm, times(1)).setDisplayableListItemSelector(listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 verify(parentForm, times(1)).setDisplayableListItemAdder(listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 verify(parentForm, times(1)).setDisplayableListItemEditor(listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 verify(parentForm, times(1)).setDisplayableListItemViewer(listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 verify(parentForm, times(1)).setDisplayableListItemDeleter(listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 // Test adding an item with a valid adder
 DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
 when(listItemAdder.addItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList)).thenReturn(new ArrayList<Displayable>());
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.addListItem();
 verify(sessionProperties, times(1)).commitChanges();
 // Test adding an item with an error
 when(listItemAdder.addItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList)).thenThrow(new MacawException("Error adding item"));
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.addListItem();
 verify(log, times(1)).displayErrorDialog(any(MacawException.class));
 }

 @Test
 public void testEditSelectedListItem() {
 // Test editing an item with a valid editor
 DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
 when(listItemEditor.editListItem(displayableListPanel.listOwnerName, any(Displayable.class), displayableListPanel.displayableList)).thenReturn(new Displayable());
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.editSelectedListItem();
 verify(sessionProperties, times(1)).commitChanges();
 // Test editing an item with an error
 when(listItemEditor.editListItem(displayableListPanel.listOwnerName, any(Displayable.class), displayableListPanel.displayableList)).thenThrow(new MacawException("Error editing item"));
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.editSelectedListItem();
 verify(log, times(1)).displayErrorDialog(any(MacawException.class));
 }

 @Test
 public void testViewSelectedListItem() {
 // Test viewing an item with a valid viewer
 DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
 when(listItemViewer.viewListItem(any(Displayable.class))).thenReturn(null);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 displayableListPanel.viewSelectedListItem();
 // Test viewing an item with an error
 when(listItemViewer.viewListItem(any(Displayable.class))).thenThrow(new MacawException("Error viewing item"));
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 displayableListPanel.viewSelectedListItem();
 verify(log, times(1)).displayErrorDialog(any(MacawException.class));
 }

 @Test
 public void testDeleteSelectedListItems() {
 // Test deleting items with a confirm dialog
 when(JOptionPane.showConfirmDialog(any(Component.class), anyString(), anyString(), 0)).thenReturn(JOptionPane.YES_OPTION);
 DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
 when(listItemDeleter.deleteListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList)).thenReturn(new ArrayList<Displayable>());
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 displayableListPanel.deleteSelectedListItems();
 verify(sessionProperties, times(1)).commitChanges();
 // Test deleting items without a confirm dialog
 when(JOptionPane.showConfirmDialog(any(Component.class), anyString(), anyString(), 0)).thenReturn(JOptionPane.NO_OPTION);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 displayableListPanel.deleteSelectedListItems();
 verify(log, times(1)).displayErrorDialog(eq("You did not confirm deletion."));
 }

 @Test
 public void testSelectListItems() {
 // Test selecting items with a valid selector
 DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
 when(listItemSelector.selectListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList)).thenReturn(new ArrayList<Displayable>());
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.selectListItems();
 verify(sessionProperties, times(1)).commitChanges();
 // Test selecting items without items selected
 when(listItemSelector.selectListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList)).thenThrow(new MacawException("No items selected"));
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.selectListItems();
 verify(log, times(1)).displayErrorDialog(eq("No items were selected for modification."));
 }

 @Test
 public void testUpdateButtonStates() {
 // Test update button states with new record and no write access
 DisplayableList displayableList = new DisplayableList(userInterfaceFactory);
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.isNewRecord = true;
 displayableListPanel.allowWriteAccess = false;
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled() == false);
 assertTrue(displayableListPanel.delete.isEnabled() == false);
 assertTrue(displayableListPanel.edit.isEnabled() == false);
 assertTrue(displayableListPanel.view.isEnabled() == false);
 assertTrue(displayableListPanel.select.isEnabled() == false);
 // Test update button states with new record and write access
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled() == false);
 assertTrue(displayableListPanel.delete.isEnabled() == false);
 assertTrue(displayableListPanel.edit.isEnabled() == false);
 assertTrue(displayableListPanel.view.isEnabled() == false);
 assertTrue(displayableListPanel.select.isEnabled() == false);
 // Test update button states with existing item and write access
 displayableListPanel.displayableList.add(new Displayable("Test Item"));
 displayableListPanel.isNewRecord = false;
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled() == true);
 assertTrue(displayableListPanel.delete.isEnabled() == true);
 assertTrue(displayableListPanel.edit.isEnabled() == true);
 assertTrue(displayableListPanel.view.isEnabled() == true);
 assertTrue(displayableListPanel.select.isEnabled() == true);
 // Test update button states with existing item and no write access
 displayableListPanel.allowWriteAccess = false;
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled() == false);
 assertTrue(displayableListPanel.delete.isEnabled() == false);
 assertTrue(displayableListPanel.edit.isEnabled() == false);
 assertTrue(displayableListPanel.view.isEnabled() == true);
 assertTrue(displayableListPanel.select.isEnabled() == true);
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<Displayable>();
 masterList.add(new Displayable("Test Item 1"));
 masterList.add(new Displayable("Test Item 2"));
 displayableListPanel.setDisplayableItems(masterList);
 verify(displayableListPanel.displayableList, times(1)).setDisplayItems(masterList);
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> masterList = new ArrayList<Displayable>();
 masterList.add(new Displayable("Test Item 1"));
 masterList.add(new Displayable("Test Item 2"));
 displayableListPanel.displayableList.setAllItems(masterList);
 displayableListPanel.save();
 verify(masterList, times(1)).clear();
 verify(displayableListPanel.displayableList, times(1)).getAllItems();
 verify(masterList, times(1)).addAll(displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> allListItems = new ArrayList<Displayable>();
 allListItems.add(new Displayable("Test Item 1"));
 allListItems.add(new Displayable("Test Item 2"));
 when(displayableListPanel.displayableList.getAllItems()).thenReturn(allListItems);
 assertEquals(allListItems, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 String listOwnerName = "Test List Owner";
 displayableListPanel.setListOwnerName(listOwnerName);
 verify(parentForm, times(1)).setListOwnerName(listOwnerName);
 }
}