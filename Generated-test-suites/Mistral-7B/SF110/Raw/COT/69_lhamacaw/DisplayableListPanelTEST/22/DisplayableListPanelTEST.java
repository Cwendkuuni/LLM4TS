package macaw.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import macaw.businessLayer.*;
import macaw.system.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemViewer listItemViewer;
 private DisplayableListItemDeleter listItemDeleter;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 listItemSelector = mock(DisplayableListItemSelector.class);
 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemViewer = mock(DisplayableListItemViewer.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 sessionProperties = null;
 parentForm = null;
 userInterfaceFactory = null;
 listItemSelector = null;
 listItemAdder = null;
 listItemEditor = null;
 listItemViewer = null;
 listItemDeleter = null;
 }

 @Test
 public void testConstructor() {
 DisplayableListPanel displayableListPanel1 = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 DisplayableListPanel displayableListPanel2 = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 }

 @Test
 public void testInit() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);
 }

 @Test
 public void testBuildUI() {
 //TODO: add test cases for buildUI method
 }

 @Test
 public void testCreateControlButtonPanel() {
 //TODO: add test cases for createControlButtonPanel method
 }

 @Test
 public void testSetNewRecord() {
 //TODO: add test cases for setNewRecord method
 }

 @Test
 public void testGetPanel() {
 //TODO: add test cases for getPanel method
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertTrue(displayableListPanel.getDisplayableListItemSelector() instanceof DisplayableListItemSelector);
 }

 @Test(expected = NullPointerException.class)
 public void testSetDisplayableListItemSelectorNull() {
 displayableListPanel.setDisplayableListItemSelector(null);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertTrue(displayableListPanel.getDisplayableListItemAdder() instanceof DisplayableListItemAdder);
 }

 @Test(expected = NullPointerException.class)
 public void testSetDisplayableListItemAdderNull() {
 displayableListPanel.setDisplayableListItemAdder(null);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertTrue(displayableListPanel.getDisplayableListItemEditor() instanceof DisplayableListItemEditor);
 }

 @Test(expected = NullPointerException.class)
 public void testSetDisplayableListItemEditorNull() {
 displayableListPanel.setDisplayableListItemEditor(null);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertTrue(displayableListPanel.getDisplayableListItemViewer() instanceof DisplayableListItemViewer);
 }

 @Test(expected = NullPointerException.class)
 public void testSetDisplayableListItemViewerNull() {
 displayableListPanel.setDisplayableListItemViewer(null);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertTrue(displayableListPanel.getDisplayableListItemDeleter() instanceof DisplayableListItemDeleter);
 }

 @Test(expected = NullPointerException.class)
 public void testSetDisplayableListItemDeleterNull() {
 displayableListPanel.setDisplayableListItemDeleter(null);
 }

 @Test
 public void testAddListItem() {
 //TODO: add test cases for addListItem method
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 //TODO: add test cases for editSelectedListItem method
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 //TODO: add test cases for viewSelectedListItem method
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 //TODO: add test cases for deleteSelectedListItems method
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter() {
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 //TODO: add test cases for selectListItems method
 }

 @Test
 public void testUpdateButtonStates() {
 //TODO: add test cases for updateButtonStates method
 }

 @Test
 public void testSetDisplayableItems() {
 //TODO: add test cases for setDisplayableItems method
 }

 @Test
 public void testSave() {
 //TODO: add test cases for save method
 }

 @Test
 public void testGetAllListItems() {
 //TODO: add test cases for getAllListItems method
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test List Owner");
 assertEquals("Test List Owner", displayableListPanel.getListOwnerName());
 }

 @Test(expected = NullPointerException.class)
 public void testSetListOwnerNameNull() {
 displayableListPanel.setListOwnerName(null);
 }

 @Test
 public void testActionPerformedSelect() {
 when(listItemSelector.selectListItems(displayableListPanel.getListOwnerName(), displayableListPanel.displayableList)).thenReturn(true);
 displayableListPanel.selectListItems();
 assertTrue(displayableListPanel.isNewRecord());
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testActionPerformedAdd() {
 when(listItemAdder.addItems(displayableListPanel.getListOwnerName(), displayableListPanel.displayableList)).thenReturn(true);
 when(parentForm.commitChanges()).thenReturn(true);
 displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.add, 0, ""));
 assertFalse(displayableListPanel.isNewRecord());
 assertFalse(displayableListPanel.select.isEnabled());
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testActionPerformedEdit() {
 when(listItemEditor.editListItem(displayableListPanel.getListOwnerName(), displayableListPanel.displayableList.getSelectedItem(), displayableListPanel.displayableList)).thenReturn(true);
 when(parentForm.commitChanges()).thenReturn(true);
 displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.edit, 0, ""));
 assertFalse(displayableListPanel.isNewRecord());
 assertFalse(displayableListPanel.select.isEnabled());
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testActionPerformedView() {
 when(listItemViewer.viewListItem(displayableListPanel.displayableList.getSelectedItem())).thenReturn(true);
 displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.view, 0, ""));
 assertFalse(displayableListPanel.isNewRecord());
 assertFalse(displayableListPanel.select.isEnabled());
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testActionPerformedDelete() {
 when(parentForm.commitChanges()).thenReturn(true);
 when(listItemDeleter.deleteListItems(displayableListPanel.getListOwnerName(), displayableListPanel.displayableList)).thenReturn(true);
 displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.delete, 0, ""));
 assertFalse(displayableListPanel.isNewRecord());
 assertFalse(displayableListPanel.select.isEnabled());
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 }
}