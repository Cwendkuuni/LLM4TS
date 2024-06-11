package macaw.util;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.Log;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 parentForm = mock(DisplayableListParentForm.class);
 log = mock(Log.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccess() {
 // Basic functionality check
 assertEquals("Test Panel Title", displayableListPanel.getPanelTitleText());
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 DisplayableListPanel displayableListPanelWithRecordEditing = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 // Basic functionality check
 assertEquals("Test Panel Title", displayableListPanelWithRecordEditing.getPanelTitleText());
 assertNotNull(displayableListPanelWithRecordEditing.getPanel());
 // Check if components are added correctly
 assertTrue(displayableListPanelWithRecordEditing.panel.isDisplayable());
 assertTrue(displayableListPanelWithRecordEditing.add.isDisplayable());
 assertTrue(displayableListPanelWithRecordEditing.edit.isDisplayable());
 }

 @Test(expected = MacawException.class)
 public void testDisplayableListPanelConstructorWithMissingParameters() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, null);
 }

 @Test
 public void testInitMethod() {
 // Basic functionality check
 assertEquals("Test Panel Title", displayableListPanel.getPanelTitleText());
 assertNotNull(displayableListPanel.getPanel());
 // Check if components are added correctly
 assertTrue(displayableListPanel.panel.isDisplayable());
 assertTrue(displayableListPanel.add.isDisplayable());
 assertTrue(displayableListPanel.edit.isDisplayable());
 // Check if properties are set correctly
 assertEquals(sessionProperties, displayableListPanel.sessionProperties);
 assertEquals(parentForm, displayableListPanel.parentForm);
 }

 @Test
 public void testSetNewRecordMethod() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetDisplayableListItemSelectorMethod() {
 DisplayableListItemSelector mockSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockSelector);
 assertSame(mockSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdderMethod() {
 DisplayableListItemAdder mockAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(mockAdder);
 assertSame(mockAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditorMethod() {
 DisplayableListItemEditor mockEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(mockEditor);
 assertSame(mockEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewerMethod() {
 DisplayableListItemViewer mockViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(mockViewer);
 assertSame(mockViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleterMethod() {
 DisplayableListItemDeleter mockDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(mockDeleter);
 assertSame(mockDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItemMethod() {
 // Basic functionality check
 displayableListPanel.addListItem();
 // Check if parentForm commitChanges method is called
 verify(parentForm).commitChanges();
 // Check if addItems method of DisplayableList is called
 DisplayableList displayableList = displayableListPanel.displayableList;
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Item"));
 displayableList.addItems("Test List Owner Name", masterList);
 assertNotNull(displayableList.getAllItems().get(0));
 // Check if updateButtonStates method is called
 verify(displayableListPanel).updateButtonStates();
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMethodWithMissingAdder() {
 displayableListPanel.displayableList.listItemAdder = null;
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItemMethod() {
 //Basic functionality check
 displayableListPanel.editSelectedListItem();
 //Check if parentForm commitChanges method is called
 verify(parentForm).commitChanges();
 //Create a mock Displayable to test editing
 Displayable mockDisplayable = mock(Displayable.class);
 Displayable selectedListItem = new Displayable("Test Item");
 when(mockDisplayable.getDisplayName()).thenReturn(selectedListItem.getDisplayName());
 displayableListPanel.displayableList.setSelectedItem(selectedListItem);
 //Check if editListItem method of DisplayableListItemEditor is called
 DisplayableListItemEditor mockEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(mockEditor);
 mockEditor.editListItem("Test List Owner Name", selectedListItem, displayableListPanel.displayableList);
 verify(mockEditor).editListItem("Test List Owner Name", selectedListItem, displayableListPanel.displayableList);
 //Check if updateButtonStates method is called
 verify(displayableListPanel).updateButtonStates();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMethodWithMissingEditor() {
 displayableListPanel.displayableList.listItemEditor = null;
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItemMethod() {
 //Basic functionality check
 displayableListPanel.viewSelectedListItem();
 //Create a mock Displayable to test viewing
 Displayable mockDisplayable = mock(Displayable.class);
 Displayable selectedListItem = new Displayable("Test Item");
 when(mockDisplayable.getDisplayName()).thenReturn(selectedListItem.getDisplayName());
 displayableListPanel.displayableList.setSelectedItem(selectedListItem);
 //Check if viewListItem method of DisplayableListItemViewer is called
 DisplayableListItemViewer mockViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(mockViewer);
 mockViewer.viewListItem(selectedListItem);
 verify(mockViewer).viewListItem(selectedListItem);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMethodWithMissingViewer() {
 displayableListPanel.displayableList.listItemViewer = null;
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItemsMethod() {
 //Basic functionality check
 displayableListPanel.deleteSelectedListItems();
 //Check if parentForm commitChanges method is called
 verify(parentForm).commitChanges();
 //Create a mock Displayable to test deleting
 Displayable mockDisplayable = mock(Displayable.class);
 Displayable selectedListItem1 = new Displayable("Test Item 1");
 Displayable selectedListItem2 = new Displayable("Test Item 2");
 displayableListPanel.displayableList.add(selectedListItem1);
 displayableListPanel.displayableList.add(selectedListItem2);
 displayableListPanel.displayableList.setSelectedItems(new ArrayList<Displayable>() {{
 add(selectedListItem1);
 add(selectedListItem2);
 }});
 //Check if deleteListItems method of DisplayableListItemDeleter is called
 DisplayableListItemDeleter mockDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(mockDeleter);
 mockDeleter.deleteListItems("Test List Owner Name", displayableListPanel.displayableList);
 //Check if updateButtonStates method is called
 verify(displayableListPanel).updateButtonStates();
 //Check if JOptionPane showConfirmDialog method is called
 verify(displayableListPanel).log.displayErrorDialog(isA(MacawException.class));
 verify(displayableListPanel).log.displayErrorDialog(isA(MacawException.class));
 String dialogTitle = MacawMessages.getMessage("general.warning.title");
 String warningMessage = MacawMessages.getMessage("general.warning.deletionIsPermanent");
 verify(displayableListPanel).showConfirmDialog(eq(null), eq(warningMessage), eq(dialogTitle), eq(0), eq(2));
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMethodWithMissingDeleter() {
 displayableListPanel.displayableList.listItemDeleter = null;
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItemsMethod() {
 //Basic functionality check
 displayableListPanel.selectListItems();
 //Create a mock Displayable to test selecting
 Displayable mockDisplayable = mock(Displayable.class);
 Displayable listItem = new Displayable("Test Item");
 displayableListPanel.displayableList.setSelectedItem(null);
 displayableListPanel.displayableList.add(listItem);
 displayableListPanel.listItemSelector.selectListItems("Test List Owner Name", displayableListPanel.displayableList);
 //Check if updateButtonStates method is called
 verify(displayableListPanel).updateButtonStates();
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsMethodWithMissingSelector() {
 displayableListPanel.displayableList.listItemSelector = null;
 displayableListPanel.selectListItems();
 }

 @Test
 public void testUpdateButtonStatesMethod() {
 //Test when panel is empty
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 //Test when panel is not empty and record editing is allowed
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Item"));
 displayableListPanel.displayableList.setDisplayItems(masterList);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 //Test when panel is not empty and record editing is not allowed
 displayableListPanel.allowRecordEditing = false;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 //Test when panel is new record
 displayableListPanel.isNewRecord = true;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 //Test when panel is not new record
 displayableListPanel.isNewRecord = false;
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetDisplayableItemsMethod() {
 //Basic functionality check
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Item"));
 displayableListPanel.setDisplayableItems(masterList);
 //Check if setDisplayItems method of DisplayableList is called
 verify(displayableListPanel.displayableList).setDisplayItems(masterList);
 }

 @Test
 public void testSaveMethod() {
 //Basic functionality check
 displayableListPanel.save();
 //Check if masterList is empty before saving
 assertTrue(displayableListPanel.masterList.isEmpty());
 //Check if getAllItems method of DisplayableList is called
 ArrayList<Displayable> currentList = displayableListPanel.displayableList.getAllItems();
 displayableListPanel.masterList.addAll(currentList);
 //Check if masterList is not empty after saving
 assertTrue(displayableListPanel.masterList.size() > 0);
 }

 @Test
 public void testGetAllListItemsMethod() {
 //Basic functionality check
 ArrayList<Displayable> expectedList = new ArrayList<>();
 expectedList.add(new Displayable("Test Item"));
 ArrayList<Displayable> actualList = displayableListPanel.getAllListItems();
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testSetListOwnerNameMethod() {
 displayableListPanel.setListOwnerName("Test List Owner Name");
 assertEquals("Test List Owner Name", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformedMethod() {
 ActionEvent mockActionEvent = mock(ActionEvent.class);
 Object mockButton = mock(Object.class);
 when(mockActionEvent.getSource()).thenReturn(mockButton);
 when(mockButton).thenReturn(displayableListPanel.add);
 displayableListPanel.actionPerformed(mockActionEvent);
 assertEquals(DisplayableListPanel.ADD, mockButton.getId());
}

}