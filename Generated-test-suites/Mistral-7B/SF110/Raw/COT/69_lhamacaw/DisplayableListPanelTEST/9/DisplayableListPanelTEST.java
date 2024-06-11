package macaw.util;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private ArrayList<Displayable> masterList;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 userInterfaceFactory = new MockUserInterfaceFactory();
 parentForm = new MockDisplayableListParentForm();
 masterList = new ArrayList<>();
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowWriteAccess() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowWriteAccessAndAllowRecordEditing() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test(expected = MacawException.class)
 public void testDisplayableListPanelConstructionWithoutAllowWriteAccess() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false, false);
 fail("Should have thrown MacawException");
 }

 @Test(expected = MacawException.class)
 public void testDisplayableListPanelConstructionWithAllowWriteAccessButNoAllowRecordEditing() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 assertFalse(displayableListPanel.allowRecordEditing);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isNewRecord);
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.isNewRecord);
 }

 @Test
 public void testGetPanel() {
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector mockSelector = new MockDisplayableListItemSelector();
 displayableListPanel.setDisplayableListItemSelector(mockSelector);
 assertEquals(mockSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder mockAdder = new MockDisplayableListItemAdder();
 displayableListPanel.setDisplayableListItemAdder(mockAdder);
 assertEquals(mockAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor mockEditor = new MockDisplayableListItemEditor();
 displayableListPanel.setDisplayableListItemEditor(mockEditor);
 assertEquals(mockEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer mockViewer = new MockDisplayableListItemViewer();
 displayableListPanel.setDisplayableListItemViewer(mockViewer);
 assertEquals(mockViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter mockDeleter = new MockDisplayableListItemDeleter();
 displayableListPanel.setDisplayableListItemDeleter(mockDeleter);
 assertEquals(mockDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 assertTrue(masterList.isEmpty());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 displayableListPanel.listItemAdder = null;
 displayableListPanel.addListItem();
 fail("Should have thrown MacawException");
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable mockDisplayable = new MockDisplayable();
 masterList.add(mockDisplayable);
 displayableListPanel.displayableList.setSelectedItem(mockDisplayable);
 displayableListPanel.editSelectedListItem();
 assertNotSame(mockDisplayable.getDisplayName(), mockDisplayable.getDisplayName());
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 displayableListPanel.listItemEditor = null;
 displayableListPanel.editSelectedListItem();
 fail("Should have thrown MacawException");
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable mockDisplayable = new MockDisplayable();
 masterList.add(mockDisplayable);
 displayableListPanel.displayableList.setSelectedItem(mockDisplayable);
 displayableListPanel.viewSelectedListItem();
 assertNotNull(mockDisplayable.getViewer());
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 displayableListPanel.listItemViewer = null;
 displayableListPanel.viewSelectedListItem();
 fail("Should have thrown MacawException");
 }

 @Test
 public void testDeleteSelectedListItems() {
 Displayable mockDisplayable = new MockDisplayable();
 masterList.add(mockDisplayable);
 displayableListPanel.displayableList.setSelectedItem(mockDisplayable);
 displayableListPanel.deleteSelectedListItems();
 assertFalse(masterList.contains(mockDisplayable));
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithNoSelectedItems() {
 displayableListPanel.deleteSelectedListItems();
 assertTrue(masterList.size() > 0);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 displayableListPanel.listItemDeleter = null;
 displayableListPanel.deleteSelectedListItems();
 fail("Should have thrown MacawException");
 }

 @Test
 public void testSelectListItems() {
 Displayable mockDisplayable = new MockDisplayable();
 masterList.add(mockDisplayable);
 displayableListPanel.displayableList.setSelectedItem(mockDisplayable);
 displayableListPanel.selectListItems();
 assertTrue(displayableListPanel.displayableList.isItemSelected(mockDisplayable));
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsWithNoItems() {
 displayableListPanel.displayableList.clear();
 displayableListPanel.selectListItems();
 fail("Should have thrown MacawException");
 }

 @Test
 public void testUpdateButtonStates() {
 Displayable mockDisplayable1 = new MockDisplayable();
 Displayable mockDisplayable2 = new MockDisplayable();
 masterList.add(mockDisplayable1);
 masterList.add(mockDisplayable2);
 displayableListPanel.displayableList.setSelectedItem(mockDisplayable1);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 displayableListPanel.displayableList.setSelectedItem(mockDisplayable2);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 Displayable mockDisplayable1 = new MockDisplayable();
 Displayable mockDisplayable2 = new MockDisplayable();
 masterList.add(mockDisplayable1);
 masterList.add(mockDisplayable2);
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 assertEquals(masterList, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 String name = "Test List Owner Name";
 displayableListPanel.setListOwnerName(name);
 assertEquals(name, displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformedWithSelectButton() {
 ActionEvent event = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "Select");
 displayableListPanel.actionPerformed(event);
 displayableListPanel.selectListItems();
 }

 @Test
 public void testActionPerformedWithAddButton() {
 ActionEvent event = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "Add");
 displayableListPanel.actionPerformed(event);
 displayableListPanel.addListItem();
 }

 @Test
 public void testActionPerformedWithEditButton() {
 ActionEvent event = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "Edit");
 Displayable mockDisplayable = new MockDisplayable();
 masterList.add(mockDisplayable);
 displayableListPanel.displayableList.setSelectedItem(mockDisplayable);
 displayableListPanel.actionPerformed(event);
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testActionPerformedWithViewButton() {
 ActionEvent event = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "View");
 Displayable mockDisplayable = new MockDisplayable();
 masterList.add(mockDisplayable);
 displayableListPanel.displayableList.setSelectedItem(mockDisplayable);
 displayableListPanel.actionPerformed(event);
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testActionPerformedWithDeleteButton() {
 ActionEvent event = new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, "Delete");
 Displayable mockDisplayable = new MockDisplayable();
 masterList.add(mockDisplayable);
 displayableListPanel.displayableList.setSelectedItem(mockDisplayable);
 displayableListPanel.actionPerformed(event);
 displayableListPanel.deleteSelectedListItems();
 }
}