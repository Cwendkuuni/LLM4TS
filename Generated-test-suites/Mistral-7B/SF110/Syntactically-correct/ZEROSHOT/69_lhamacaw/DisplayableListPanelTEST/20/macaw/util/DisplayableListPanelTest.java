package macaw.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import org.junit.*;

public class DisplayableListPanelTest {

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 panelTitleText = "Test Panel";
 allowWriteAccess = true;
 allowRecordEditing = true;
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 currentUserID = "testUserID";
 masterList = new ArrayList<>();
 displayableList = mock(DisplayableList.class);
 select = mock(JButton.class);
 add = mock(JButton.class);
 edit = mock(JButton.class);
 view = mock(JButton.class);
 delete = mock(JButton.class);
 listItemSelector = mock(DisplayableListItemSelector.class);
 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemViewer = mock(DisplayableListItemViewer.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);
 isNewRecord = false;
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.buildUI();
 }

 @Test
 public void testDisplayableListPanelConstruction() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testDisplayableListPanelConstructionWithRecordEditing() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testInit() {
 when(sessionProperties.getProperty("currentUser")).thenReturn(new User("testUser", currentUserID, null));
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 assertNotNull(displayableListPanel.sessionProperties);
 assertNotNull(displayableListPanel.log);
 assertNotNull(displayableListPanel.parentForm);
 assertNotNull(displayableListPanel.panelTitleText);
 assertNotNull(displayableListPanel.currentUserID);
 assertNotNull(displayableListPanel.userInterfaceFactory);
 }

 @Test
 public void testBuildUI() {
 assertNotNull(displayableListPanel.panel);
 }

 @Test
 public void testCreateControlButtonPanel() {
 JPanel controlButtonPanel = displayableListPanel.createControlButtonPanel();
 assertNotNull(controlButtonPanel);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord);
 }

 @Test
 public void testGetPanel() {
 JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertNotNull(displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertNotNull(displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertNotNull(displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertNotNull(displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertNotNull(displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 when(listItemAdder.addItems(eq(listOwnerName), eq(displayableList))).thenReturn(new ArrayList<>());
 displayableListPanel.addListItem();
 assertTrue(displayableListPanel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 when(listItemEditor.editListItem(eq(listOwnerName), eq(selectedListItem), eq(displayableList))).thenReturn(selectedListItem);
 displayableListPanel.editSelectedListItem();
 assertFalse(displayableListPanel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 when(listItemViewer.viewListItem(eq(selectedListItem))).thenReturn(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 assertFalse(displayableListPanel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 when(listItemDeleter.deleteListItems(eq(listOwnerName), eq(displayableList))).thenReturn(new ArrayList<>());
 when(JOptionPane.showConfirmDialog(eq(null), eq("deletionIsPermanent"), eq("warning.title"), eq(0), eq(2))).thenReturn(0);
 displayableListPanel.deleteSelectedListItems();
 assertFalse(displayableListPanel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter() {
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 when(listItemSelector.selectListItems(eq(listOwnerName), eq(displayableList))).thenReturn(new ArrayList<>());
 displayableListPanel.selectListItems();
 assertFalse(displayableListPanel.isNewRecord);
 }

 @Test
 public void testUpdateButtonStates() {
 when(displayableList.isEmpty()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 when(displayableList.isEmpty()).thenReturn(false);
 when(allowWriteAccess).thenReturn(true);
 when(displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());

 when(displayableList.isEmpty()).thenReturn(false);
 when(allowWriteAccess).thenReturn(false);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> displayableItems = new ArrayList<>();
 displayableListPanel.setDisplayableItems(displayableItems);
 assertEquals(displayableItems, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> displayableItems = new ArrayList<>();
 displayableListPanel.save();
 assertEquals(displayableItems, displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 Displayable displayableItem = mock(Displayable.class);
 displayableListPanel.displayableList.add(displayableItem);
 ArrayList<Displayable> allListItems = displayableListPanel.getAllListItems();
 assertTrue(allListItems.contains(displayableItem));
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("TestListOwner");
 assertEquals("TestListOwner", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformedSelect() {
 displayableListPanel.selectListItems();
 verify(displayableListPanel.listItemSelector).selectListItems(eq(displayableListPanel.listOwnerName), eq(displayableListPanel.displayableList));
 }

 @Test
 public void testActionPerformedAdd() {
 displayableListPanel.addListItem();
 verify(displayableListPanel.listItemAdder).addItems(eq(displayableListPanel.listOwnerName), eq(displayableListPanel.displayableList));
 }

 @Test
 public void testActionPerformedEdit() {
 displayableListPanel.editSelectedListItem();
 verify(displayableListPanel.listItemEditor).editListItem(eq(displayableListPanel.listOwnerName), eq(displayableListPanel.displayableList.getSelectedItem()), eq(displayableListPanel.displayableList));
 }

 @Test
 public void testActionPerformedView() {
 displayableListPanel.viewSelectedListItem();
 verify(displayableListPanel.listItemViewer).viewListItem(eq(displayableListPanel.displayableList.getSelectedItem()));
 }

 @Test
 public void testActionPerformedDelete() {
 displayableListPanel.deleteSelectedListItems();
 verify(displayableListPanel.listItemDeleter).deleteListItems(eq(displayableListPanel.listOwnerName), eq(displayableListPanel.displayableList));
 }
}