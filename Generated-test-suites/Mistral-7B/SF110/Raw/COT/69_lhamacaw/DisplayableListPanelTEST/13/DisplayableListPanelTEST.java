package macaw.util;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemViewer listItemViewer;
 private DisplayableListItemDeleter listItemDeleter;

 @Before
 public void setup() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", false);
 }

 @Test
 public void testConstructorWithAllowWriteAccess() {
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testInit() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 verify(parentForm).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testInitWithMissingAdder() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 verify(parentForm).commitChanges();
 Mockito.doThrow(new MacawException("Missing DisplayListAdder")).when(listItemAdder).addItems(eq("Displayable Owner"), eq(displayableListPanel.displayableList));
 displayableListPanel.init(sessionProperties, parentForm, "Displayable List Panel", true, true);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingEditor() {
 displayableListPanel.setDisplayableListItemEditor(null);
 displayableListPanel.addListItem();
 verify(sessionProperties.getLog()).displayErrorDialog(eq("Missing DisplayListEditor"));
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetNewRecordFalse() {
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 if (displayableListPanel.displayableList.isEmpty()) {
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 }
 else {
 assertFalse(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertSame(listItemSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPaneel.setDisplayableListItemAdder(listItemAdder);
 assertSame(listItemAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertSame(listItemEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertSame(listItemViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertSame(listItemDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 verify(parentForm).commitChanges();
 verify(listItemAdder).addItems(eq("Displayable Owner"), eq(displayableListPanel.displayableList));
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 displayableListPanel.editSelectedListItem();
 verify(sessionProperties.getLog()).displayErrorDialog(eq("Missing DisplayListEditor"));
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = Mockito.mock(Displayable.class);
 Mockito.when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 verify(listItemViewer).viewListItem(eq(selectedListItem));
 }

 @Test
 public void testDeleteSelectedListItemsWithNoSelectedItems() {
 verifyZeroInteractions(displayableListPanel.listItemDeleter);
 }

 @Test
 public void testDeleteSelectedListItemsWithSelectedItems() throws Exception {
 Displayable selectedListItem1 = Mockito.mock(Displayable.class);
 Displayable selectedListItem2 = Mockito.mock(Displayable.class);
 Mockito.when(displayableListPanel.displayableList.getSelectedItems()).thenReturn(Arrays.asList(selectedListItem1, selectedListItem2));
 JOptionPane mockOptionPane = Mockito.mock(JOptionPane.class);
 Mockito.when(JOptionPane.showConfirmDialog(eq(null), anyString(), anyString(), eq(0), anyInt())).thenReturn(JOptionPane.YES_OPTION);
 Mockito.when(sessionProperties.getLog()).thenReturn(mockOptionPane);
 displayableListPanel.deleteSelectedListItems();
 verify(listItemDeleter).deleteListItems(eq("Displayable Owner"), eq(displayableListPanel.displayableList));
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();
 verify(listItemSelector).selectListItems(eq("Displayable Owner"), eq(displayableListPanel.displayableList));
 }

 @Test
 public void testUpdateButtonStates() {
 Displayable listItem1 = Mockito.mock(Displayable.class);
 Displayable listItem2 = Mockito.mock(Displayable.class);
 Mockito.when(displayableListPanel.displayableList.getSelectedItems()).thenReturn(Arrays.asList(listItem1, listItem2));
 displayableListPanel.updateButtonStates();
 if (displayableListPanel.allowWriteAccess) {
 if (displayableListPanel.isNewRecord) {
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }
 else {
 assertFalse(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 }
 }
 else {
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 if (displayableListPanel.displayableList.isEmpty()) {
 assertFalse(displayableListPanel.view.isEnabled());
 }
 else if (!displayableListPanel.isNewRecord) {
 assertFalse(displayableListPanel.view.isEnabled());
 }
 }
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(Mockito.mock(Displayable.class));
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
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
 displayableListPanel.setListOwnerName("Displayable Owner");
 assertEquals("Displayable Owner", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformedWithSelectButton() {
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 displayableListPanel.selectListItems();
 }

 @Test
 public void testActionPerformedWithAddButton() {
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 displayableListPanel.addListItem();
 }

 @Test
 public void testActionPerformedWithEditButton() {
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testActionPerformedWithViewButton() {
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testActionPerformedWithDeleteButton() {
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 displayableListPanel.deleteSelectedListItems();
 }
}