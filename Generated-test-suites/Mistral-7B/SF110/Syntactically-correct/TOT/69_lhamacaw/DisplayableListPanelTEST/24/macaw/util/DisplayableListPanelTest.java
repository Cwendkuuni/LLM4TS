package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.system.SessionProperties;
import macaw.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private DisplayableList displayableList;
 private JButton addButton;
 private JButton editButton;
 private JButton viewButton;
 private JButton deleteButton;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 displayableList = mock(DisplayableList.class);
 addButton = mock(JButton.class);
 editButton = mock(JButton.class);
 viewButton = mock(JButton.class);
 deleteButton = mock(JButton.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);
 when(sessionProperties.getLog()).thenReturn(mock(Log.class));
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.setDisplayableListItemSelector(mock(DisplayableListItemSelector.class));
 displayableListPanel.setDisplayableListItemAdder(mock(DisplayableListItemAdder.class));
 displayableListPanel.setDisplayableListItemEditor(mock(DisplayableListItemEditor.class));
 displayableListPanel.setDisplayableListItemViewer(mock(DisplayableListItemViewer.class));
 displayableListPanel.setDisplayableListItemDeleter(mock(DisplayableListItemDeleter.class));
 }

 @Test
 public void testInit() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, false);
 verify(parentForm).commitChanges();
 }

 @Test
 public void testAddListItem() {
 doNothing().when(displayableList).addItems(anyString(), any(DisplayableList.class));
 displayableListPanel.addListItem();
 verify(parentForm).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 doThrow(new MacawException("Mock Exception")).when(displayableList).addItems(anyString(), any(DisplayableList.class));
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 doNothing().when(displayableList).getSelectedItem();
 doNothing().when(displayableList).setSelectedItem(any(Displayable.class));
 doNothing().when(displayableListItemEditor).editListItem(anyString(), any(Displayable.class), any(DisplayableList.class));
 displayableListPanel.editSelectedListItem();
 verify(displayableList).getSelectedItem();
 verify(displayableListItemEditor).editListItem(anyString(), any(Displayable.class), any(DisplayableList.class));
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 doThrow(new MacawException("Mock Exception")).when(displayableListItemEditor).editListItem(anyString(), any(Displayable.class), any(DisplayableList.class));
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 doNothing().when(displayableList).getSelectedItem();
 doNothing().when(displayableListItemViewer).viewListItem(any(Displayable.class));
 displayableListPanel.viewSelectedListItem();
 verify(displayableList).getSelectedItem();
 verify(displayableListItemViewer).viewListItem(any(Displayable.class));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 doThrow(new MacawException("Mock Exception")).when(displayableListItemViewer).viewListItem(any(Displayable.class));
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 doNothing().when(parentForm).commitChanges();
 doNothing().when(displayableList).deleteListItems(anyString(), any(DisplayableList.class));
 displayableListPanel.deleteSelectedListItems();
 verify(parentForm).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter() {
 doThrow(new MacawException("Mock Exception")).when(displayableList).deleteListItems(anyString(), any(DisplayableList.class));
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 doNothing().when(displayableListItemSelector).selectListItems(anyString(), any(DisplayableList.class));
 displayableListPanel.selectListItems();
 verify(displayableListItemSelector).selectListItems(anyString(), any(DisplayableList.class));
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsMissingSelector() {
 doThrow(new MacawException("Mock Exception")).when(displayableListItemSelector).selectListItems(anyString(), any(DisplayableList.class));
 displayableListPanel.selectListItems();
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 verify(addButton, never()).setEnabled(false);
 verify(editButton, never()).setEnabled(false);
 verify(viewButton, never()).setEnabled(false);
 verify(deleteButton, never()).setEnabled(false);
 verify(selectButton, never()).setEnabled(false);

 displayableListPanel.setNewRecord(true);
 displayableListPanel.updateButtonStates();
 verify(addButton, never()).setEnabled(false);
 verify(editButton, never()).setEnabled(false);
 verify(viewButton, never()).setEnabled(false);
 verify(deleteButton, never()).setEnabled(false);
 verify(selectButton, never()).setEnabled(false);

 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 verify(addButton).setEnabled(true);
 verify(selectButton).setEnabled(true);
 verify(deleteButton, never()).setEnabled(false);
 verify(editButton, never()).setEnabled(false);
 verify(viewButton, never()).setEnabled(false);

 displayableListPanel.setNewRecord(false);
 displayableListPanel.setAllowRecordEditing(false);
 displayableListPanel.updateButtonStates();
 verify(addButton).setEnabled(true);
 verify(selectButton).setEnabled(true);
 verify(deleteButton, never()).setEnabled(false);
 verify(editButton, never()).setEnabled(false);
 verify(viewButton).setEnabled(true);

 displayableListPanel.setNewRecord(false);
 displayableListPanel.setAllowRecordEditing(true);
 displayableListPanel.updateButtonStates();
 verify(addButton).setEnabled(true);
 verify(selectButton).setEnabled(true);
 verify(deleteButton).setEnabled(true);
 verify(editButton).setEnabled(true);
 verify(viewButton).setEnabled(true);
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<Displayable>();
 masterList.add(mock(Displayable.class));
 displayableListPanel.setDisplayableItems(masterList);
 verify(displayableList).setDisplayItems(masterList);
 }

 @Test
 public void testSave() {
 displayableListPanel.save();
 verify(masterList).clear();
 verify(displayableList).getAllItems();
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> allListItems = new ArrayList<Displayable>();
 allListItems.add(mock(Displayable.class));
 when(displayableList.getAllItems()).thenReturn(allListItems);
 ArrayList<Displayable> actualListItems = displayableListPanel.getAllListItems();
 assertEquals(allListItems, actualListItems);
 }

 @Test
 public void testSetListOwnerName() {
 String listOwnerName = "Test Owner";
 displayableListPanel.setListOwnerName(listOwnerName);
 assertEquals(listOwnerName, displayableListPanel.getListOwnerName());
 }

 @Test
 public void testActionPerformedSelectListItems() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(actionEvent);
 verify(displayableListItemSelector).selectListItems(anyString(), any(DisplayableList.class));
 }

 @Test
 public void testActionPerformedAddListItem() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(actionEvent);
 verify(displayableListItemAdder, never()).addItems(anyString(), any(DisplayableList.class));
 }

 @Test
 public void testActionPerformedEditListItem() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(actionEvent);
 verify(displayableListItemEditor, never()).editListItem(anyString(), any(Displayable.class), any(DisplayableList.class));
 }

 @Test
 public void testActionPerformedViewListItem() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(actionEvent);
 verify(displayableListItemViewer, never()).viewListItem(any(Displayable.class));
 }

 @Test
 public void testActionPerformedDeleteListItems() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(actionEvent);
 verify(displayableListItemDeleter, never()).deleteListItems(anyString(), any(DisplayableList.class));
 }
}