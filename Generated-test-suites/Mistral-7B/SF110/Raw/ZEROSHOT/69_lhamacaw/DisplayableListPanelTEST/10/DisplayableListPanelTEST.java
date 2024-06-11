package macaw.util;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.system.SessionProperties;
import macaw.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 parentForm = mock(DisplayableListParentForm.class);
 log = mock(Log.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 sessionProperties = null;
 parentForm = null;
 log = null;
 userInterfaceFactory = null;
 }

 @Test
 public void testDisplayableListPanelConstruction() {
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testDisplayableListPanelWithFalseAllowWriteAccess() {
 DisplayableListPanel displayableListPanelWithFalseAllowWriteAccess = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 assertFalse(displayableListPanelWithFalseAllowWriteAccess.allowWriteAccess);
 }

 @Test
 public void testDisplayableListPanelWithFalseAllowRecordEditing() {
 DisplayableListPanel displayableListPanelWithFalseAllowRecordEditing = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 assertFalse(displayableListPanelWithFalseAllowRecordEditing.allowRecordEditing);
 }

 @Test
 public void testInitialize() {
 //TODO: Implement test for initialize method
 }

 @Test
 public void testBuildUI() {
 //TODO: Implement test for buildUI method
 }

 @Test
 public void testCreateControlButtonPanel() {
 //TODO: Implement test for createControlButtonPanel method
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord);
 }

 @Test
 public void testGetPanel() {
 //TODO: Implement test for getPanel method
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector displayableListItemSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(displayableListItemSelector);
 assertEquals(displayableListItemSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder displayableListItemAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(displayableListItemAdder);
 assertEquals(displayableListItemAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor displayableListItemEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(displayableListItemEditor);
 assertEquals(displayableListItemEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer displayableListItemViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(displayableListItemViewer);
 assertEquals(displayableListItemViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter displayableListItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(displayableListItemDeleter);
 assertEquals(displayableListItemDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 //TODO: Implement test for addListItem method
 }

 @Test
 public void testEditSelectedListItem() {
 //TODO: Implement test for editSelectedListItem method
 }

 @Test
 public void testViewSelectedListItem() {
 //TODO: Implement test for viewSelectedListItem method
 }

 @Test
 public void testDeleteSelectedListItems() {
 //TODO: Implement test for deleteSelectedListItems method
 }

 @Test
 public void testSelectListItems() {
 //TODO: Implement test for selectListItems method
 }

 @Test
 public void testUpdateButtonStates() {
 //TODO: Implement test for updateButtonStates method
 }

 @Test
 public void testSetDisplayableItems() {
 //TODO: Implement test for setDisplayableItems method
 }

 @Test
 public void testSave() {
 //TODO: Implement test for save method
 }

 @Test
 public void testGetAllListItems() {
 List<Displayable> allListItems = new ArrayList<>();
 allListItems.add(new Displayable("Test 1"));
 allListItems.add(new Displayable("Test 2"));
 when(displayableListPanel.displayableList.getAllItems()).thenReturn(allListItems);
 List<Displayable> result = displayableListPanel.getAllListItems();
 assertEquals(allListItems, result);
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test List Owner");
 assertEquals("Test List Owner", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformedWithSelectButton() {
 ActionEvent selectEvent = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(selectEvent);
 verify(parentForm).commitChanges();
 verify(displayableListPanel.listItemSelector).selectListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }

 @Test
 public void testActionPerformedWithAddButton() {
 ActionEvent addEvent = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(addEvent);
 verify(parentForm).commitChanges();
 }

 @Test
 public void testActionPerformedWithEditButton() {
 Displayable editDisplayable = new Displayable("Test Displayable for Edit");
 ActionEvent editEvent = new ActionEvent(displayableListPanel.edit, 0, "TEST");
 when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(editDisplayable);
 displayableListPanel.actionPerformed(editEvent);
 verify(parentForm).commitChanges();
 verify(displayableListPanel.listItemEditor).editListItem(displayableListPanel.listOwnerName, editDisplayable, displayableListPanel.displayableList);
 }

 @Test
 public void testActionPerformedWithViewButton() {
 Displayable viewDisplayable = new Displayable("Test Displayable for View");
 ActionEvent viewEvent = new ActionEvent(displayableListPanel.view, 0, "TEST");
 when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(viewDisplayable);
 displayableListPanel.actionPerformed(viewEvent);
 verify(displayableListItemViewer).viewListItem(viewDisplayable);
 }

 @Test
 public void testActionPerformedWithDeleteButton() {
 ActionEvent deleteEvent = mock(ActionEvent.class);
 when(displayableListPanel.displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.actionPerformed(deleteEvent);
 verify(parentForm).commitChanges();
 verify(displayableListPanel.listItemDeleter).deleteListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }
}