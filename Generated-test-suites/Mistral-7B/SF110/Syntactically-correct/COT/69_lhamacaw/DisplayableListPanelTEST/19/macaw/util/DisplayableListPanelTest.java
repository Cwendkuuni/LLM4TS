package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import static macaw.system.MacawMessages.getMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListParentForm parentForm;
 private SessionProperties sessionProperties;
 private DisplayableListPanel displayableListPanel;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemDeleter listItemDeleter;
 private JButton select;
 private JButton add;
 private JButton edit;
 private JButton view;
 private JButton delete;
 private ArrayList<Displayable> masterList;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 displayableList = Mockito.mock(DisplayableList.class);
 listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 select = Mockito.mock(JButton.class);
 add = Mockito.mock(JButton.class);
 edit = Mockito.mock(JButton.class);
 view = Mockito.mock(JButton.class);
 delete = Mockito.mock(JButton.class);
 masterList = new ArrayList<>();
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 displayableListPanel.add(select);
 displayableListPanel.add(add);
 displayableListPanel.add(edit);
 displayableListPanel.add(view);
 displayableListPanel.add(delete);
 }

 @Test
 public void testConstructorWithAllowRecordEditingFalse() {
 DisplayableListPanel displayableListPanel1 = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 assertNull(displayableListPanel1.listItemEditor);
 }

 @Test
 public void testConstructorWithAllowRecordEditingTrue() {
 DisplayableListPanel displayableListPanel2 = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel2.listItemEditor);
 }

 @Test
 public void testInitMethod() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel.userInterfaceFactory);
 assertNotNull(displayableListPanel.sessionProperties);
 assertNotNull(displayableListPanel.parentForm);
 assertNull(displayableListPanel.listItemSelector);
 assertNull(displayableListPanel.listItemAdder);
 assertNull(displayableListPanel.listItemEditor);
 assertNull(displayableListPanel.listItemDeleter);
 assertFalse(displayableListPanel.isNewRecord);
 }

 @Test
 public void testGetPanelMethod() {
 JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test
 public void testSetDisplayableListItemSelectorMethod() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertSame(listItemSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdderMethod() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertSame(listItemAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditorMethod() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertSame(listItemEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemDeleterMethod() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertSame(listItemDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItemMethod() {
 try {
 displayableListPanel.addListItem();
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 }
 catch (MacawException e) {
 fail("Should not throw an exception");
 }
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMethodMissingAdder() {
 displayableListPanel.addListItem();
 fail("Should throw a MacawException");
 }

 @Test
 public void testEditSelectedListItemMethod() {
 try {
 displayableListPanel.editSelectedListItem();
 fail("Should throw an exception as there are no selected items");
 }
 catch (MacawException e) {
 assertEquals("Test Panel: General error: Unselected items", e.getMessage());
 }
 }

 @Test
 public void testViewSelectedListItemMethod() {
 try {
 Displayable selectedListItem = Mockito.mock(Displayable.class);
 Mockito.when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 fail("Should throw an exception as there are no selected items");
 }
 catch (MacawException e) {
 assertEquals("Test Panel: General error: Unselected items", e.getMessage());
 }
 }

 @Test
 public void testDeleteSelectedListItemsMethod() {
 try {
 displayableListPanel.deleteSelectedListItems();
 fail("Should throw an exception as there are no selected items");
 }
 catch (MacawException e) {
 assertEquals("Test Panel: General error: Unselected items", e.getMessage());
 }
 }

 @Test
 public void testDeleteSelectedListItemsMethodWithConfirmation() {
 Mockito.when(JOptionPane.showConfirmDialog(Mockito.any(Component.class), anyString(), anyString(), anyInt())).thenReturn(JOptionPane.YES_OPTION);
 Mockito.when(displayableList.getSelectedItems()).thenReturn(masterList);
 try {
 displayableListPanel.deleteSelectedListItems();
 assertEquals(0, masterList.size());
 }
 catch (MacawException e) {
 fail("Should not throw an exception");
 }
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMethodWithoutConfirmation() {
 Mockito.when(JOptionPane.showConfirmDialog(Mockito.any(Component.class), anyString(), anyString(), anyInt())).thenReturn(JOptionPane.NO_OPTION);
 try {
 displayableListPanel.deleteSelectedListItems();
 fail("Should throw an exception");
 }
 catch (MacawException e) {
 assertEquals("Test Panel: General warning: Deletion is permanent", e.getMessage());
 }
 }

 @Test
 public void testSelectListItemsMethod() {
 try {
 Mockito.when(listItemSelector.selectListItems("Test Panel", displayableList)).thenReturn(true);
 displayableListPanel.selectListItems();
 assertTrue(displayableList.itemsAreSelected());
 }
 catch (MacawException e) {
 fail("Should not throw an exception");
 }
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsMethodWithNoSelectedItems() {
 try {
 Mockito.when(listItemSelector.selectListItems("Test Panel", displayableList)).thenReturn(false);
 displayableListPanel.selectListItems();
 fail("Should throw an exception");
 }
 catch (MacawException e) {
 assertEquals("Test Panel: General error: Unselected items", e.getMessage());
 }
 }

 @Test
 public void testUpdateButtonStatesMethodWithWriteAccessTrueAndNewRecordFalse() {
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesMethodWithWriteAccessTrueAndNewRecordTrue() {
 displayableListPanel.setNewRecord(true);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesMethodWithWriteAccessFalse() {
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 if (displayableList.isEmpty()) {
 assertTrue(displayableListPanel.view.isEnabled());
 }
 else {
 assertTrue(displayableListPanel.view.isEnabled());
 }
 }

 @Test
 public void testSetDisplayableItemsMethod() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable displayable = Mockito.mock(Displayable.class);
 masterList.add(displayable);
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(displayable, displayableListPanel.displayableList.getAllItems().get(0));
 }

 @Test
 public void testSaveMethod() {
 displayableListPanel.save();
 assertEquals(displayableList.getAllItems(), masterList);
 }

 @Test
 public void testGetAllListItemsMethod() {
 assertEquals(displayableList.getAllItems(), displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerNameMethod() {
 displayableListPanel.setListOwnerName("Test Owner");
 assertEquals("Test Owner", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformedMethodSelectButton() {
 Mockito.when(listItemSelector.selectListItems("Test Panel", displayableList)).thenReturn(true);
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 assertTrue(displayableList.itemsAreSelected());
 }

 @Test
 public void testActionPerformedMethodAddButton() {
 try {
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 fail("Should throw an exception as add button should be disabled");
 }
 catch (MacawException e) {
 assertEquals("Test Panel: Display list add button is disabled", e.getMessage());
 }
 }

 @Test
 public void testActionPerformedMethodEditButton() {
 try {
 Displayable selectedListItem = Mockito.mock(Displayable.class);
 Mockito.when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 fail("Should throw an exception as edit button should be disabled");
 }
 catch (MacawException e) {
 assertEquals("Test Panel: Display list edit button is disabled", e.getMessage());
 }
 }

 @Test
 public void testActionPerformedMethodViewButton() {
 try {
 Displayable selectedListItem = Mockito.mock(Displayable.class);
 Mockito.when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 fail("Should throw an exception as view button should be disabled");
 }
 catch (MacawException e) {
 assertEquals("Test Panel: Display list view button is disabled", e.getMessage());
 }
 }

 @Test
 public void testActionPerformedMethodDeleteButton() {
 try {
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 fail("Should throw an exception as delete button should be disabled");
 }
 catch (MacawException e) {
 assertEquals("Test Panel: Display list delete button is disabled", e.getMessage());
 }
 }
}