package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.Log;
import static macaw.util.MacawMessages.getMessage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 log = Mockito.mock(Log.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 }

 @Test
 public void testConstructorWithAllowWriteAccess() {
 // Test that constructor is called correctly with allowWriteAccess set to true
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 }

 @Test
 public void testConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 // Test that constructor is called correctly with allowWriteAccess and allowRecordEditing both set to true
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testInit() {
 // Test that init method is called correctly
 Mockito.verify(parentForm).commitChanges();
 // Verify that buttons are created and added to the panel with correct labels
 final String selectText = getMessage("general.buttons.select");
 Mockito.verify(parentForm).createButton(ArgumentMatchers.eq(selectText)).thenReturn(displayableListPanel.select);
 final String addText = getMessage("general.buttons.add");
 Mockito.verify(parentForm).createButton(ArgumentMatchers.eq(addText)).thenReturn(displayableListPanel.add);
 final String editText = getMessage("general.buttons.edit");
 Mockito.verify(parentForm).createButton(ArgumentMatchers.eq(editText)).thenReturn(displayableListPanel.edit);
 final String viewText = getMessage("general.buttons.view");
 Mockito.verify(parentForm).createButton(ArgumentMatchers.eq(viewText)).thenReturn(displayableListPanel.view);
 final String deleteText = getMessage("general.buttons.delete");
 Mockito.verify(parentForm).createButton(ArgumentMatchers.eq(deleteText)).thenReturn(displayableListPanel.delete);
 }

 @Test
 public void testSetNewRecord() {
 // Test that setNewRecord method updates button states correctly
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord());
 assertFalse(displayableListPanel.isAllowRecordEditing());
 assertFalse(displayableListPanel.isAllowWriteAccess());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());
 displayableListPanel.setNewRecord(false);
 assertFalse(displayableListPanel.isNewRecord());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isDeleteEnabled());
 assertTrue(displayableListPanel.isEditEnabled());
 assertTrue(displayableListPanel.isViewEnabled());
 assertTrue(displayableListPanel.isSelectEnabled());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel.addListItem();
 fail("AddListItem should throw MacawException when listItemAdder is null");
 }

 @Test
 public void testEditSelectedListItem() {
 // Test that editSelectedListItem method updates button states correctly
 displayableListPanel.setDisplayableListItemEditor(Mockito.mock(DisplayableListItemEditor.class));
 final Displayable displayable = Mockito.mock(Displayable.class);
 displayableListPanel.displayableList.add(displayable);
 displayableListPanel.displayableList.setSelectedItem(displayable);
 displayableListPanel.editSelectedListItem();
 assertTrue(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());
 }

 @Test
 public void testViewSelectedListItem() {
 // Test that viewSelectedListItem method updates button states correctly
 displayableListPanel.setDisplayableListItemViewer(Mockito.mock(DisplayableListItemViewer.class));
 final Displayable displayable = Mockito.mock(Displayable.class);
 displayableListPanel.displayableList.add(displayable);
 displayableListPanel.displayableList.setSelectedItem(displayable);
 displayableListPanel.viewSelectedListItem();
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());
 assertTrue(displayableListPanel.isViewEnabled());
 }

 @Test
 public void testDeleteSelectedListItems() {
 // Test that deleteSelectedListItems method updates button states correctly
 displayableListPanel.setDisplayableListItemDeleter(Mockito.mock(DisplayableListItemDeleter.class));
 JOptionPane.showConfirmDialog = Mockito.mock(JOptionPane.class);
 displayableListPanel.deleteSelectedListItems();
 assertTrue(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());
 Mockito.verify(JOptionPane.showConfirmDialog, Mockito.times(1)).showConfirmDialog(ArgumentMatchers.eq(null), ArgumentMatchers.eq(getMessage("general.warning.deletionIsPermanent")), ArgumentMatchers.eq(0), ArgumentMatchers.eq(2));
 }

 @Test
 public void testSelectListItems() {
 // Test that selectListItems method updates button states correctly
 displayableListPanel.setDisplayableListItemSelector(Mockito.mock(DisplayableListItemSelector.class));
 displayableListPanel.selectListItems();
 assertTrue(displayableListPanel.isSelectEnabled());
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 }

 @Test
 public void testUpdateButtonStates() {
 // Test that updateButtonStates method updates button states correctly
 final DisplayableList displayableList = Mockito.mock(DisplayableList.class);
 displayableList.add(Mockito.mock(Displayable.class));
 displayableList.add(Mockito.mock(Displayable.class));
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());
 displayableListPanel.isNewRecord = true;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());
 displayableListPanel.isNewRecord = false;
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.isAddEnabled());
 assertTrue(displayableListPanel.isSelectEnabled());
 assertTrue(displayableListPanel.isDeleteEnabled());
 assertTrue(displayableListPanel.isEditEnabled());
 assertTrue(displayableListPanel.isViewEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 // Test that setDisplayableItems method sets displayItems correctly
 final List<Displayable> displayables = new ArrayList<>();
 displayables.add(Mockito.mock(Displayable.class));
 displayables.add(Mockito.mock(Displayable.class));
 displayableListPanel.setDisplayableItems(displayables);
 assertEquals(displayables, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 // Test that save method saves all list items to masterList
 final List<Displayable> masterList = new ArrayList<>();
 masterList.add(Mockito.mock(Displayable.class));
 masterList.add(Mockito.mock(Displayable.class));
 displayableListPanel.masterList = masterList;
 final List<Displayable> displayableList = new ArrayList<>();
 displayableList.add(Mockito.mock(Displayable.class));
 displayableList.add(Mockito.mock(Displayable.class));
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.save();
 assertEquals(displayableList, displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 // Test that getAllListItems method returns all list items from displayableList
 assertEquals(displayableListPanel.displayableList.getAllItems(), displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 // Test that setListOwnerName method sets listOwnerName correctly
 displayableListPanel.setListOwnerName("Test Owner");
 assertEquals("Test Owner", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformed() {
 // Test that actionPerformed method calls correct method based on button pressed
 displayableListPanel.add = Mockito.mock(JButton.class);
 displayableListPanel.select = Mockito.mock(JButton.class);
 displayableListPanel.edit = Mockito.mock(JButton.class);
 displayableListPanel.view = Mockito.mock(JButton.class);
 displayableListPanel.delete = Mockito.mock(JButton.class);
 Mockito.verify(displayableListPanel.select).addActionListener(ArgumentMatchers.isInstanceOf(DisplayableListPanel.class));
 Mockito.verify(displayableListPanel.add).addActionListener(ArgumentMatchers.isInstanceOf(DisplayableListPanel.class));
 Mockito.verify(displayableListPanel.edit).addActionListener(ArgumentMatchers.isInstanceOf(DisplayableListPanel.class));
 Mockito.verify(displayableListPanel.view).addActionListener(ArgumentMatchers.isInstanceOf(DisplayableListPanel.class));
 Mockito.verify(displayableListPanel.delete).addActionListener(ArgumentMatchers.isInstanceOf(DisplayableListPanel.class));
 }
}