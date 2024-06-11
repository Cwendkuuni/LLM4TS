package macaw.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import macaw.businessLayer.*;
import macaw.system.Log;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private JPanel panel;
 private JButton select;
 private JButton add;
 private JButton edit;
 private JButton view;
 private JButton delete;
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
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 }

 @Test
 public void testDisplayableListPanelConstructionWithWriteAccessAndRecordEditing() {
 // Test construction with write access and record editing
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelConstructionWithWriteAccessButNoRecordEditing() {
 // Test construction with write access but no record editing
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 assertNotNull(displayableListPanel.getPanel());
 assertNull(displayableListPanel.listItemEditor);
 }

 @Test
 public void testDisplayableListPanelConstructionWithoutWriteAccess() {
 // Test construction without write access
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false, true);
 assertNotNull(displayableListPanel.getPanel());
 assertNull(displayableListPanel.add);
 assertNull(displayableListPanel.delete);
 }

 @Test
 public void testBuildUI() {
 // Test buildUI method
 displayableListPanel.buildUI();
 assertNotNull(displayableListPanel.panel);
 assertNotNull(displayableListPanel.displayableList);
 }

 @Test
 public void testCreateControlButtonPanel() {
 // Test createControlButtonPanel method
 JPanel controlButtonPanel = displayableListPanel.createControlButtonPanel();
 assertNotNull(controlButtonPanel);
 }

 @Test
 public void testSetNewRecord() {
 // Test setNewRecord method
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 // Test setDisplayableListItemSelector method
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertNotNull(displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 // Test setDisplayableListItemAdder method
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertNotNull(displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 // Test setDisplayableListItemEditor method
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertNotNull(displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 // Test setDisplayableListItemViewer method
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertNotNull(displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 // Test setDisplayableListItemDeleter method
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertNotNull(displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 // Test addListItem method
 when(listItemAdder.addItems(eq("Test OwnerName"), eq(displayableList))).thenReturn(new ArrayList<>());
 displayableListPanel.addListItem();
 assertFalse(displayableListPanel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemThrowsExceptionWhenAdderIsNull() {
 // Test addListItem method throws exception when adder is null
 displayableListPanel.listItemAdder = null;
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 // Test editSelectedListItem method
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 when(listItemEditor.editListItem(eq("Test OwnerName"), eq(selectedListItem), eq(displayableList))).thenReturn(selectedListItem);
 displayableListPanel.editSelectedListItem();
 assertFalse(displayableListPanel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemThrowsExceptionWhenEditorIsNull() {
 // Test editSelectedListItem method throws exception when editor is null
 displayableListPanel.listItemEditor = null;
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 // Test viewSelectedListItem method
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 when(listItemViewer.viewListItem(eq(selectedListItem))).thenReturn(null);
 displayableListPanel.viewSelectedListItem();
 assertFalse(displayableListPanel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemThrowsExceptionWhenViewerIsNull() {
 // Test viewSelectedListItem method throws exception when viewer is null
 displayableListPanel.listItemViewer = null;
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 // Test deleteSelectedListItems method
 when(displayableList.itemsAreSelected()).thenReturn(true);
 when(listItemDeleter.deleteListItems(eq("Test OwnerName"), eq(displayableList))).thenReturn(new ArrayList<>());
 displayableListPanel.deleteSelectedListItems();
 assertFalse(displayableListPanel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsThrowsExceptionWhenDeleterIsNull() {
 // Test deleteSelectedListItems method throws exception when deleter is null
 displayableListPanel.listItemDeleter = null;
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 // Test selectListItems method
 displayableListPanel.selectListItems();
 assertFalse(displayableListPanel.isNewRecord);
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsThrowsExceptionWhenSelectorIsNull() {
 // Test selectListItems method throws exception when selector is null
 displayableListPanel.listItemSelector = null;
 displayableListPanel.selectListItems();
 }

 @Test
 public void testUpdateButtonStates() {
 // Test updateButtonStates method
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isNewRecord);
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 // Test setDisplayableItems method
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(mock(Displayable.class));
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 // Test save method
 displayableListPanel.save();
 assertEquals(displayableListPanel.displayableList.getAllItems(), displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 // Test getAllListItems method
 assertEquals(displayableListPanel.displayableList.getAllItems(), displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 // Test setListOwnerName method
 displayableListPanel.setListOwnerName("Test OwnerName");
 assertEquals("Test OwnerName", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformed() {
 // Test actionPerformed method
 ActionEvent actionEvent = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(actionEvent);
 }
}