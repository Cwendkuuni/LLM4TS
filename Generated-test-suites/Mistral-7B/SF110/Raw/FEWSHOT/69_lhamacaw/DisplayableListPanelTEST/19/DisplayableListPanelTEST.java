package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import macaw.businessLayer.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;
 private JPanel panel;
 private DisplayableList displayableList;
 private JButton selectButton;
 private JButton addButton;
 private JButton editButton;
 private JButton viewButton;
 private JButton deleteButton;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 log = mock(Log.class);
 displayableList = mock(DisplayableList.class);
 selectButton = mock(JButton.class);
 addButton = mock(JButton.class);
 editButton = mock(JButton.class);
 viewButton = mock(JButton.class);
 deleteButton = mock(JButton.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);

 when(sessionProperties.getLog()).thenReturn(log);
 when(userInterfaceFactory.createButton(anyString())).thenAnswer((Answer<JButton>) invocation -> invocation.getArguments()[0]);
 when(parentForm.commitChanges()).thenReturn(true);
 }

 @Test
 public void testDisplayableListPanelCreation() {
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isNewRecord());
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector mockSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockSelector);
 assertEquals(mockSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder mockAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(mockAdder);
 assertEquals(mockAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor mockEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(mockEditor);
 assertEquals(mockEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer mockViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(mockViewer);
 assertEquals(mockViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter mockDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(mockDeleter);
 assertEquals(mockDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 assertNotNull(displayableList);
 verify(parentForm, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutAdder() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.addListItem();
 fail("Add List Item without Adder should throw MacawException");
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable mockDisplayable = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(mockDisplayable);
 displayableListPanel.editSelectedListItem();
 assertNotNull(displayableList);
 verify(parentForm, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithoutEditor() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.editSelectedListItem();
 fail("Edit Selected List Item without Editor should throw MacawException");
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable mockDisplayable = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(mockDisplayable);
 displayableListPanel.viewSelectedListItem();
 assertNotNull(displayableList);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithoutViewer() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.viewSelectedListItem();
 fail("View Selected List Item without Viewer should throw MacawException");
 }

 @Test
 public void testDeleteSelectedListItems() {
 when(JOptionPane.showConfirmDialog(any(Component.class), anyString(), anyString(), 0, 2)).thenReturn(JOptionPane.YES_OPTION);
 displayableListPanel.deleteSelectedListItems();
 assertNotNull(displayableList);
 verify(parentForm, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithoutDeleter() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.deleteSelectedListItems();
 fail("Delete Selected List Items without Deleter should throw MacawException");
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();
 assertNotNull(displayableList);
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isNewRecord);
 assertFalse(displayableListPanel.allowWriteAccess);
 assertFalse(displayableListPanel.allowRecordEditing);
 assertTrue(displayableListPanel.displayableList.isEmpty());
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.isNewRecord = true;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isNewRecord);
 assertFalse(displayableListPanel.allowWriteAccess);
 assertFalse(displayableListPanel.allowRecordEditing);
 assertFalse(displayableListPanel.displayableList.isEmpty());
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isNewRecord);
 assertTrue(displayableListPanel.allowWriteAccess);
 assertFalse(displayableListPanel.allowRecordEditing);
 assertFalse(displayableListPanel.displayableList.isEmpty());
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.allowRecordEditing = true;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isNewRecord);
 assertTrue(displayableListPanel.allowWriteAccess);
 assertTrue(displayableListPanel.allowRecordEditing);
 assertFalse(displayableListPanel.displayableList.isEmpty());
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.displayableList.clear();
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isNewRecord);
 assertTrue(displayableListPanel.allowWriteAccess);
 assertTrue(displayableListPanel.allowRecordEditing);
 assertTrue(displayableListPanel.displayableList.isEmpty());
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.displayableList.add(mockDisplayable);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isNewRecord);
 assertTrue(displayableListPanel.allowWriteAccess);
 assertTrue(displayableListPanel.allowRecordEditing);
 assertFalse(displayableListPanel.displayableList.isEmpty());
 assertFalse(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> mockMasterList = new ArrayList<>();
 mockMasterList.add(mockDisplayable);
 displayableListPanel.setDisplayableItems(mockMasterList);
 assertNotNull(displayableList);
 assertEquals(mockMasterList, displayableListPanel.masterList);
 }

 @Test
 public void testSave() {
 displayableListPanel.save();
 assertNotNull(displayableList);
 assertEquals(displayableList.getAllItems(), displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> allListItems = displayableListPanel.getAllListItems();
 assertNotNull(allListItems);
 }

 @Test
 public void testSetListOwnerName() {
 String listOwnerName = "Test List Owner";
 displayableListPanel.setListOwnerName(listOwnerName);
 assertEquals(listOwnerName, displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformed() {
 ActionEvent mockActionEvent = mock(ActionEvent.class);
 when(selectButton.isEnabled()).thenReturn(true);
 when(addButton.isEnabled()).thenReturn(true);
 when(editButton.isEnabled()).thenReturn(true);
 when(viewButton.isEnabled()).thenReturn(true);
 when(deleteButton.isEnabled()).thenReturn(true);

 displayableListPanel.actionPerformed(mockActionEvent);
 verify(parentForm, times(1)).commitChanges();
 }
}