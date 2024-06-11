package macaw.util;

import macaw.businessLayer.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableList displayableList;
 private ArrayList<Displayable> masterList;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private SessionProperties sessionProperties;
 private Log log;

 @Before
 public void setup() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new UserInterfaceFactory();
 log = new Log();
 sessionProperties.setLog(log);
 parentForm = new DisplayableListParentForm();
 displayableList = new DisplayableList(userInterfaceFactory);
 masterList = new ArrayList<>();
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.setMasterList(masterList);
 displayableListPanel.setListOwnerName("testListOwnerName");
 }

 @Test
 public void testInit() {
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 assertNull(displayableListPanel.getListItemSelector());
 assertNull(displayableListPanel.getListItemAdder());
 assertNull(displayableListPanel.getListItemEditor());
 assertNull(displayableListPanel.getListItemViewer());
 assertNull(displayableListPanel.getListItemDeleter());
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isAllowWriteAccess());
 assertFalse(displayableListPanel.isAllowRecordEditing());
 assertFalse(displayableListPanel.allowWriteAccess);
 assertFalse(displayableListPanel.allowRecordEditing);
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 assertTrue(displayableListPanel.allowWriteAccess);
 assertTrue(displayableListPanel.allowRecordEditing);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector() {
 @Override
 public void selectListItems(String listOwnerName, DisplayableList displayableList) {
 // implementation here
 }
 };
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertEquals(listItemSelector, displayableListPanel.getListItemSelector());
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder() {
 @Override
 public void addItems(String listOwnerName, DisplayableList displayableList) {
 // implementation here
 }
 };
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertEquals(listItemAdder, displayableListPanel.getListItemAdder());
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor() {
 @Override
 public void editListItem(String listOwnerName, Displayable selectedListItem, DisplayableList displayableList) {
 // implementation here
 }
 };
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertEquals(listItemEditor, displayableListPanel.getListItemEditor());
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer() {
 @Override
 public void viewListItem(Displayable selectedListItem) {
 // implementation here
 }
 };
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertEquals(listItemViewer, displayableListPanel.getListItemViewer());
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter() {
 @Override
 public void deleteListItems(String listOwnerName, DisplayableList displayableList) {
 // implementation here
 }
 };
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertEquals(listItemDeleter, displayableListPanel.getListItemDeleter());
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 // implementation to test addListItem method here
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedListItem = new Displayable("Test Displayable");
 masterList.add(selectedListItem);
 displayableList.setDisplayItems(masterList);
 displayableList.setSelectedItem(selectedListItem);
 displayableListPanel.editSelectedListItem();
 // implementation to test editSelectedListItem method here
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = new Displayable("Test Displayable");
 masterList.add(selectedListItem);
 displayableList.setDisplayItems(masterList);
 displayableList.setSelectedItem(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 // implementation to test viewSelectedListItem method here
 }

 @Test
 public void testDeleteSelectedListItems() {
 masterList.add(new Displayable("Displayable 1"));
 masterList.add(new Displayable("Displayable 2"));
 displayableList.setDisplayItems(masterList);
 displayableList.setSelectedItems(Arrays.asList(masterList.get(0), masterList.get(1)));
 displayableListPanel.deleteSelectedListItems();
 // implementation to test deleteSelectedListItems method here
 }

 @Test
 public void testSelectListItems() {
 displayableList.setSelectedItems(Arrays.asList(new Displayable("Displayable 1"), new Displayable("Displayable 2")));
 displayableListPanel.selectListItems();
 // implementation to test selectListItems method here
 }

 @Test
 public void testUpdateButtonStates() {
 // implementation to test updateButtonStates method here
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> newMasterList = new ArrayList<>();
 newMasterList.add(new Displayable("Displayable 1"));
 newMasterList.add(new Displayable("Displayable 2"));
 displayableListPanel.setDisplayableItems(newMasterList);
 assertEquals(newMasterList, displayableListPanel.getMasterList());
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> savedList = new ArrayList<>();
 savedList.addAll(displayableList.getAllItems());
 assertEquals(savedList, displayableListPanel.getMasterList());
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> allItems = displayableList.getAllItems();
 assertEquals(allItems, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 String newListOwnerName = "New List Owner Name";
 displayableListPanel.setListOwnerName(newListOwnerName);
 assertEquals(newListOwnerName, displayableListPanel.getListOwnerName());
 }

 @Test
 public void testActionPerformed_Select() {
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "SELECT");
 displayableListPanel.actionPerformed(actionEvent);
 displayableListPanel.selectListItems();
 }

 @Test
 public void testActionPerformed_Add() {
 displayableListPanel.actionPerformed(new ActionEvent(new Object(), 0, "ADD"));
 displayableListPanel.addListItem();
 }

 @Test
 public void testActionPerformed_Edit() {
 Displayable selectedListItem = new Displayable("Test Displayable");
 masterList.add(selectedListItem);
 displayableList.setDisplayItems(masterList);
 displayableList.setSelectedItem(selectedListItem);
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "EDIT");
 displayableListPanel.actionPerformed(actionEvent);
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testActionPerformed_View() {
 Displayable selectedListItem = new Displayable("Test Displayable");
 masterList.add(selectedListItem);
 displayableList.setDisplayItems(masterList);
 displayableList.setSelectedItem(selectedListItem);
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "VIEW");
 displayableListPanel.actionPerformed(actionEvent);
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testActionPerformed_Delete() {
 masterList.add(new Displayable("Displayable 1"));
 masterList.add(new Displayable("Displayable 2"));
 displayableList.setDisplayItems(masterList);
 displayableList.setSelectedItems(Arrays.asList(masterList.get(0), masterList.get(1)));
 ActionEvent actionEvent = new ActionEvent(new Object(), 0, "DELETE");
 displayableListPanel.actionPerformed(actionEvent);
 displayableListPanel.deleteSelectedListItems();
 }
}