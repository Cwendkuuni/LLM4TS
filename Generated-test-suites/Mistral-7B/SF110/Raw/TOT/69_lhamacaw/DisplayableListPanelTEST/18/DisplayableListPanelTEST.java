package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.util.Displayable;
import macaw.util.DisplayableListItemAdder;
import macaw.util.DisplayableListItemDeleter;
import macaw.util.DisplayableListItemEditor;
import macaw.util.DisplayableListItemSelector;
import macaw.util.DisplayableListItemViewer;
import macaw.util.DisplayableListPanel;
import macaw.util.DisplayableListPanel.MacawErrorType;
import macaw.util.MacawMessages;

import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListParentForm parentForm;
 private SessionProperties sessionProperties;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemDeleter listItemDeleter;
 private DisplayableListItemViewer listItemViewer;
 private String panelTitleText;
 private String currentUserID;
 private boolean allowWriteAccess;
 private boolean allowRecordEditing;
 private String listOwnerName;
 private ArrayList<Displayable> masterList;
 private ArrayList<Displayable> masterSourceVariableCollection;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 log = mock(Log.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 displayableList = mock(DisplayableList.class);
 listItemSelector = mock(DisplayableListItemSelector.class);
 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);
 listItemViewer = mock(DisplayableListItemViewer.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", allowWriteAccess, allowRecordEditing);
 displayableListPanel.userInterfaceFactory = userInterfaceFactory;
 displayableListPanel.sessionProperties = sessionProperties;
 displayableListPanel.log = log;
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.listItemSelector = listItemSelector;
 displayableListPanel.listItemAdder = listItemAdder;
 displayableListPanel.listItemEditor = listItemEditor;
 displayableListPanel.listItemDeleter = listItemDeleter;
 displayableListPanel.listItemViewer = listItemViewer;
 displayableListPanel.panelTitleText = "Test Panel Title";
 displayableListPanel.currentUserID = "TestUserID";
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.allowRecordEditing = true;
 displayableListPanel.listOwnerName = "TestListOwnerName";
 displayableListPanel.masterList = new ArrayList<Displayable>();
 displayableListPanel.masterSourceVariableCollection = new ArrayList<Displayable>();
 }

 @Test
 public void testInit() {
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);

 verify(userInterfaceFactory, times(6)).createButton(isA(String.class));
 }

 @Test
 public void testBuildUI() {
 displayableListPanel.buildUI();

 verify(userInterfaceFactory, times(1)).createLabel(eq(panelTitleText));
 verify(userInterfaceFactory, times(1)).createPanel();
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 displayableListPanel.updateButtonStates();

 verify(add, times(1)).setEnabled(false);
 verify(delete, times(1)).setEnabled(false);
 verify(edit, times(1)).setEnabled(false);
 verify(view, times(1)).setEnabled(false);
 verify(select, times(1)).setEnabled(false);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 verify(listItemSelector, times(1)).setDisplayableList(displayableList);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 verify(listItemAdder, times(1)).init(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 verify(listItemEditor, times(1)).init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 verify(listItemViewer, times(1)).init(displayableList);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 verify(listItemDeleter, times(1)).init(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();

 verify(parentForm, times(1)).commitChanges();
 verify(listItemAdder, times(1)).addItems(eq("TestListOwnerName"), eq(displayableList));
 }

 @Test(expected = MacawException.class)
 public void testAddListItem_MissingAdder() {
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 displayableListPanel.displayableList.setSelectedItem(selectedListItem);
 displayableListPanel.editSelectedListItem();

 verify(parentForm, times(1)).commitChanges();
 verify(listItemEditor, times(1)).editListItem("TestListOwnerName", selectedListItem, displayableList);
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItem_MissingEditor() {
 Displayable selectedListItem = mock(Displayable.class);
 displayableListPanel.displayableList.setSelectedItem(selectedListItem);
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 displayableListPanel.displayableList.setSelectedItem(selectedListItem);
 displayableListPanel.viewSelectedListItem();

 verify(listItemViewer, times(1)).viewListItem(eq(selectedListItem));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItem_MissingViewer() {
 Displayable selectedListItem = mock(Displayable.class);
 displayableListPanel.displayableList.setSelectedItem(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableListPanel.displayableList.setSelectedItems(new ArrayList<Displayable>());
 displayableListPanel.deleteSelectedListItems();

 verify(parentForm, times(1)).commitChanges();
 verify(listItemDeleter, times(1)).deleteListItems("TestListOwnerName", displayableList);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItems_MissingDeleter() {
 displayableListPanel.displayableList.setSelectedItems(new ArrayList<Displayable>());
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();

 verify(listItemSelector, times(1)).selectListItems("TestListOwnerName", displayableList);
 }

 @Test(expected = MacawException.class)
 public void testSelectListItems_MissingSelector() {
 displayableListPanel.selectListItems();
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.updateButtonStates();

 if (allowWriteAccess) {
 if (isNewRecord) {
 verify(add, times(1)).setEnabled(false);
 verify(delete, times(1)).setEnabled(false);
 verify(edit, times(1)).setEnabled(false);
 verify(view, times(1)).setEnabled(false);
 verify(select, times(1)).setEnabled(false);
 }
 else {
 verify(add, times(1)).setEnabled(true);
 verify(select, times(1)).setEnabled(true);
 verify(delete, times(1)).setEnabled(true);
 verify(edit, times(1)).setEnabled(true);
 verify(view, times(1)).setEnabled(true);
 }
 }
 else {
 verify(add, times(1)).setEnabled(false);
 verify(delete, times(1)).setEnabled(false);
 verify(edit, times(1)).setEnabled(false);
 }
}

 @Test
 public void testSetDisplayableItems() {
 List<Displayable> displayables = new ArrayList<Displayable>();
 displayableListPanel.setDisplayableItems(displayables);

 verify(displayableList, times(1)).setDisplayItems(displayables);
 }

 @Test
 public void testSave() {
 displayableListPanel.save();

 assertEquals(displayableListPanel.displayableList.getAllItems(), displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> allListItems = displayableListPanel.getAllListItems();

 assertEquals(displayableListPanel.displayableList.getAllItems(), allListItems);
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("TestListOwnerName2");

 assertEquals("TestListOwnerName2", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformed_Select() {
 displayableListPanel.selectListItems();
 displayableListPanel.select = mock(JButton.class);
 doCallRealMethod().when(displayableListPanel).actionPerformed(eq(new ActionEvent(displayableListPanel.select, ActionEvent.ACTION_PERFORMED, "TestActionEvent")));

 displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.select, ActionEvent.ACTION_PERFORMED, "TestActionEvent"));

 verify(listItemSelector, times(1)).selectListItems("TestListOwnerName", displayableList);
 }

 @Test
 public void testActionPerformed_Add() {
 displayableListPanel.addListItem();
 displayableListPanel.add = mock(JButton.class);
 doCallRealMethod().when(displayableListPanel).actionPerformed(eq(new ActionEvent(displayableListPanel.add, ActionEvent.ACTION_PERFORMED, "TestActionEvent")));

 displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.add, ActionEvent.ACTION_PERFORMED, "TestActionEvent"));

 verify(parentForm, times(1)).commitChanges();
 verify(listItemAdder, times(1)).addItems("TestListOwnerName", displayableList);
 }

 @Test
 public void testActionPerformed_Edit() {
 Displayable selectedListItem = mock(Displayable.class);
 displayableListPanel.displayableList.setSelectedItem(selectedListItem);
 displayableListPanel.editSelectedListItem();
 displayableListPanel.edit = mock(JButton.class);
 doCallRealMethod().when(displayableListPanel).actionPerformed(eq(new ActionEvent(displayableListPanel.edit, ActionEvent.ACTION_PERFORMED, "TestActionEvent")));

 displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.edit, ActionEvent.ACTION_PERFORMED, "TestActionEvent"));

 verify(parentForm, times(1)).commitChanges();
 verify(listItemEditor, times(1)).editListItem("TestListOwnerName", selectedListItem, displayableList);
 }

 @Test
 public void testActionPerformed_View() {
 Displayable selectedListItem = mock(Displayable.class);
 displayableListPanel.displayableList.setSelectedItem(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 displayableListPanel.view = mock(JButton.class);
 doCallRealMethod().when(displayableListPanel).actionPerformed(eq(new ActionEvent(displayableListPanel.view, ActionEvent.ACTION_PERFORMED, "TestActionEvent")));

 displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.view, ActionEvent.ACTION_PERFORMED, "TestActionEvent"));

 verify(listItemViewer, times(1)).viewListItem(selectedListItem);
 }

 @Test
 public void testActionPerformed_Delete() {
 displayableListPanel.deleteSelectedListItems();
 displayableListPanel.delete = mock(JButton.class);
 doCallRealMethod().when(displayableListPanel).actionPerformed(eq(new ActionEvent(displayableListPanel.delete, ActionEvent.ACTION_PERFORMED, "TestActionEvent")));

 displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.delete, ActionEvent.ACTION_PERFORMED, "TestActionEvent"));

 verify(parentForm, times(1)).commitChanges();
 verify(listItemDeleter, times(1)).deleteListItems("TestListOwnerName", displayableList);
 }
}