package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private DisplayableList displayableList;
 private JButton select;
 private JButton add;
 private JButton edit;
 private JButton view;
 private JButton delete;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 log = Mockito.mock(Log.class);
 displayableList = Mockito.mock(DisplayableList.class);
 select = Mockito.mock(JButton.class);
 add = Mockito.mock(JButton.class);
 edit = Mockito.mock(JButton.class);
 view = Mockito.mock(JButton.class);
 delete = Mockito.mock(JButton.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false);
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", false, false);
 displayableListPanel.buildUI();
 }

 @Test
 public void testConstructorWithAllowWriteAccessButNotAllowRecordEditing() {
 DisplayableListPanel tempDisplayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false, false);
 assertNull(tempDisplayableListPanel.listItemEditor);
 assertNotNull(tempDisplayableListPanel.listItemViewer);
 }

 @Test
 public void testConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 DisplayableListPanel tempDisplayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(tempDisplayableListPanel.listItemEditor);
 assertNotNull(tempDisplayableListPanel.listItemViewer);
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
 JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(Mockito.mock(DisplayableListItemSelector.class));
 assertNotNull(displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(Mockito.mock(DisplayableListItemAdder.class));
 assertNotNull(displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(Mockito.mock(DisplayableListItemEditor.class));
 assertNotNull(displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(Mockito.mock(DisplayableListItemViewer.class));
 assertNotNull(displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(Mockito.mock(DisplayableListItemDeleter.class));
 assertNotNull(displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 try {
 displayableListPanel.addListItem();
 fail("Should throw MacawException");
 } catch (MacawException e) {
 // Expected
 }
 displayableListPanel.listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 Mockito.doCallRealMethod().when(displayableListPanel.listItemAdder).addItems(displayableListPanel.listOwnerName, displayableList);
 displayableListPanel.addListItem();
 assertEquals(1, displayableList.size());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable testDisplayable = Mockito.mock(Displayable.class);
 Mockito.when(displayableList.getSelectedItem()).thenReturn(testDisplayable);
 try {
 displayableListPanel.editSelectedListItem();
 fail("Should throw MacawException");
 } catch (MacawException e) {
 // Expected
 }
 displayableListPanel.listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 Mockito.doCallRealMethod().when(displayableListPanel.listItemEditor).editListItem(displayableListPanel.listOwnerName, testDisplayable, displayableList);
 displayableListPanel.editSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable testDisplayable = Mockito.mock(Displayable.class);
 Mockito.when(displayableList.getSelectedItem()).thenReturn(testDisplayable);
 try {
 displayableListPanel.viewSelectedListItem();
 fail("Should throw MacawException");
 } catch (MacawException e) {
 // Expected
 }
 displayableListPanel.listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 Mockito.doCallRealMethod().when(displayableListPanel.listItemViewer).viewListItem(testDisplayable);
 displayableListPanel.viewSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 try {
 displayableListPanel.deleteSelectedListItems();
 fail("Should throw MacawException");
 } catch (MacawException e) {
 // Expected
 }
 displayableListPanel.listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 Mockito.doCallRealMethod().when(displayableListPanel.listItemDeleter).deleteListItems(displayableListPanel.listOwnerName, displayableList);
 displayableListPanel.deleteSelectedListItems();
 assertEquals(0, displayableList.size());
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter() {
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 List<Displayable> testList = new ArrayList<>();
 Displayable testDisplayable1 = Mockito.mock(Displayable.class);
 Displayable testDisplayable2 = Mockito.mock(Displayable.class);
 testList.add(testDisplayable1);
 testList.add(testDisplayable2);
 Mockito.when(displayableList.getAllItems()).thenReturn(testList);
 try {
 displayableListPanel.selectListItems();
 fail("Should not throw MacawException");
 } catch (MacawException e) {
 // Not expected
 }
 Mockito.doCallRealMethod().when(displayableListPanel.listItemSelector).selectListItems(displayableListPanel.listOwnerName, displayableList);
 displayableListPanel.selectListItems();
 assertTrue(displayableList.itemsAreSelected());
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.isNewRecord = false;
 Mockito.when(displayableList.isEmpty()).thenReturn(false);
 Mockito.when(displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertTrue(select.isEnabled());
 assertTrue(edit.isEnabled());
 assertFalse(add.isEnabled());
 assertTrue(view.isEnabled());
 assertTrue(delete.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesNoSelection() {
 displayableListPanel.isNewRecord = false;
 Mockito.when(displayableList.isEmpty()).thenReturn(false);
 Mockito.when(displayableList.itemsAreSelected()).thenReturn(false);
 displayableListPanel.updateButtonStates();
 assertFalse(select.isEnabled());
 assertFalse(edit.isEnabled());
 assertFalse(add.isEnabled());
 assertFalse(view.isEnabled());
 assertFalse(delete.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesNewRecord() {
 displayableListPanel.isNewRecord = true;
 displayableListPanel.updateButtonStates();
 assertFalse(select.isEnabled());
 assertFalse(edit.isEnabled());
 assertFalse(add.isEnabled());
 assertFalse(view.isEnabled());
 assertFalse(delete.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesEmptyDisplayList() {
 displayableListPanel.isNewRecord = false;
 Mockito.when(displayableList.isEmpty()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertFalse(select.isEnabled());
 assertFalse(edit.isEnabled());
 assertFalse(add.isEnabled());
 assertFalse(view.isEnabled());
 assertTrue(delete.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesNoWriteAccess() {
 displayableListPanel.isNewRecord = false;
 displayableListPanel.allowWriteAccess = false;
 Mockito.when(displayableList.isEmpty()).thenReturn(false);
 Mockito.when(displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertFalse(select.isEnabled());
 assertFalse(edit.isEnabled());
 assertFalse(add.isEnabled());
 assertTrue(view.isEnabled());
 assertFalse(delete.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 List<Displayable> testList = new ArrayList<>();
 Displayable testDisplayable1 = Mockito.mock(Displayable.class);
 Displayable testDisplayable2 = Mockito.mock(Displayable.class);
 testList.add(testDisplayable1);
 testList.add(testDisplayable2);
 displayableListPanel.displayableList.setDisplayItems(testList);
 displayableListPanel.setDisplayableItems(testList);
 assertEquals(testList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 List<Displayable> testList = new ArrayList<>();
 Displayable testDisplayable1 = Mockito.mock(Displayable.class);
 Displayable testDisplayable2 = Mockito.mock(Displayable.class);
 testList.add(testDisplayable1);
 testList.add(testDisplayable2);
 Mockito.when(displayableList.getAllItems()).thenReturn(testList);
 displayableListPanel.save();
 assertEquals(testList, displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 List<Displayable> testList = new ArrayList<>();
 Displayable testDisplayable1 = Mockito.mock(Displayable.class);
 Displayable testDisplayable2 = Mockito.mock(Displayable.class);
 testList.add(testDisplayable1);
 testList.add(testDisplayable2);
 Mockito.when(displayableList.getAllItems()).thenReturn(testList);
 List<Displayable> returnedList = displayableListPanel.getAllListItems();
 assertEquals(testList, returnedList);
 }
}