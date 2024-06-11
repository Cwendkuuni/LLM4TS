package macaw.util.test;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private JPanel panel;
 private DisplayableList displayableList;
 private User currentUser;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 log = Mockito.mock(Log.class);
 currentUser = Mockito.mock(User.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableList = Mockito.mock(DisplayableList.class);
 displayableListPanel.displayableList = displayableList;
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccess() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 assertNotNull(displayableListPanel.select);
 assertNotNull(displayableListPanel.add);
 assertNotNull(displayableListPanel.edit);
 assertNotNull(displayableListPanel.view);
 assertNotNull(displayableListPanel.delete);
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel.select);
 assertNotNull(displayableListPanel.add);
 assertNotNull(displayableListPanel.edit);
 assertNotNull(displayableListPanel.view);
 assertNotNull(displayableListPanel.delete);
 }

 @Test
 public void testInitMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertEquals("Test Panel", displayableListPanel.panelTitleText);
 }

 @Test
 public void testBuildUIMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel.panel);
 }

 @Test
 public void testSetNewRecordMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutAdder() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItemMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 Displayable selectedListItem = Mockito.mock(Displayable.class);
 displayableList.add(selectedListItem);
 displayableList.setSelectedIndex(0);
 displayableListPanel.editSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableList.add(Mockito.mock(Displayable.class));
 displayableList.setSelectedIndex(0);
 displayableListPanel.listItemEditor = null;
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItemMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 Displayable selectedListItem = Mockito.mock(Displayable.class);
 displayableList.add(selectedListItem);
 displayableList.setSelectedIndex(0);
 displayableListPanel.listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 displayableListPanel.viewSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithoutDeleter() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.listItemDeleter = null;
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testDeleteSelectedListItemsWithConfirmation() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableList.add(Mockito.mock(Displayable.class));
 displayableList.add(Mockito.mock(Displayable.class));
 displayableListPanel.listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 ArgumentCaptor<String> dialogTitleArgCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<String> warningMessageArgCaptor = ArgumentCaptor.forClass(String.class);
 Mockito.verify(displayableListPanel.parentForm).commitChanges();
 Mockito.verify(displayableListPanel.listItemDeleter).deleteListItems(displayableListPanel.listOwnerName, displayableList);
 Mockito.verify(JOptionPane).showConfirmDialog(Mockito.eq(null), warningMessageArgCaptor.capture(), dialogTitleArgCaptor.capture(), Mockito.eq(0), Mockito.eq(2));
 assertEquals("General Warning", dialogTitleArgCaptor.getValue());
 assertEquals("Deletion is Permanent", warningMessageArgCaptor.getValue());
 }

 @Test
 public void testSelectListItemsMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableList.add(Mockito.mock(Displayable.class));
 displayableList.add(Mockito.mock(Displayable.class));
 displayableListPanel.listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 ArgumentCaptor<String> listOwnerNameArgCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<DisplayableList> displayableListArgCaptor = ArgumentCaptor.forClass(DisplayableList.class);
 Mockito.verify(displayableListPanel.listItemSelector).selectListItems(listOwnerNameArgCaptor.capture(), displayableListArgCaptor.capture());
 assertEquals("Test Panel", listOwnerNameArgCaptor.getValue());
 assertEquals(displayableList, displayableListArgCaptor.getValue());
 }

 @Test
 public void testUpdateButtonStatesMethodWhenWriteAccessEnabledAndHasNewRecord() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableList.clear();
 displayableListPanel.isNewRecord = true;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesMethodWhenWriteAccessEnabledAndHasData() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 Displayable displayable = Mockito.mock(Displayable.class);
 displayableList.add(displayable);
 displayableListPanel.isNewRecord = false;
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesMethodWhenWriteAccessDisabled() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false, true);
 displayableList.clear();
 displayableListPanel.isNewRecord = false;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesMethodWhenWriteAccessDisabledAndHasData() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false, true);
 Displayable displayable = Mockito.mock(Displayable.class);
 displayableList.add(displayable);
 displayableListPanel.isNewRecord = false;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableItemsMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(Mockito.mock(Displayable.class));
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.masterList);
 }

 @Test
 public void testSaveMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.save();
 assertEquals(displayableListPanel.displayableList.getAllItems(), displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItemsMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.displayableList.add(Mockito.mock(Displayable.class));
 displayableListPanel.displayableList.add(Mockito.mock(Displayable.class));
 List<Displayable> allListItems = displayableListPanel.getAllListItems();
 assertEquals(2, allListItems.size());
 }

 @Test
 public void testSetListOwnerNameMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.setListOwnerName("Test Owner");
 assertEquals("Test Owner", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformedMethodWithSelectButton() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 ActionEvent event = Mockito.mock(ActionEvent.class);
 Mockito.when(event.getSource()).thenReturn(displayableListPanel.select);
 displayableListPanel.actionPerformed(event);
 displayableListPanel.selectListItems();
 }

 @Test
 public void testActionPerformedMethodWithAddButton() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 ActionEvent event = Mockito.mock(ActionEvent.class);
 Mockito.when(event.getSource()).thenReturn(displayableListPanel.add);
 displayableListPanel.actionPerformed(event);
 displayableListPanel.addListItem();
 }

 @Test
 public void testActionPerformedMethodWithEditButton() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 ActionEvent event = Mockito.mock(ActionEvent.class);
 Mockito.when(event.getSource()).thenReturn(displayableListPanel.edit);
 displayableListPanel.actionPerformed(event);
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testActionPerformedMethodWithViewButton() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 ActionEvent event = Mockito.mock(ActionEvent.class);
 Mockito.when(event.getSource()).thenReturn(displayableListPanel.view);
 displayableListPanel.actionPerformed(event);
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testActionPerformedMethodWithDeleteButton() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 ActionEvent event = Mockito.mock(ActionEvent.class);
 Mockito.when(event.getSource()).thenReturn(displayableListPanel.delete);
 displayableListPanel.actionPerformed(event);
 displayableListPanel.deleteSelectedListItems();
 }

}