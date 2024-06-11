package macaw.util;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.Log;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableList displayableListMock;
 private UserInterfaceFactory userInterfaceFactoryMock;
 private SessionProperties sessionPropertiesMock;
 private DisplayableListParentForm parentFormMock;
 private Log logMock;
 private DisplayableListItemSelector listItemSelectorMock;
 private DisplayableListItemAdder listItemAdderMock;
 private DisplayableListItemEditor listItemEditorMock;
 private DisplayableListItemViewer listItemViewerMock;
 private DisplayableListItemDeleter listItemDeleterMock;

 @Before
 public void setUp() {
 sessionPropertiesMock = mock(SessionProperties.class);
 logMock = mock(Log.class);
 userInterfaceFactoryMock = mock(UserInterfaceFactory.class);
 parentFormMock = mock(DisplayableListParentForm.class);
 displayableListMock = mock(DisplayableList.class);
 listItemSelectorMock = mock(DisplayableListItemSelector.class);
 listItemAdderMock = mock(DisplayableListItemAdder.class);
 listItemEditorMock = mock(DisplayableListItemEditor.class);
 listItemViewerMock = mock(DisplayableListItemViewer.class);
 listItemDeleterMock = mock(DisplayableListItemDeleter.class);

 when(sessionPropertiesMock.getLog()).thenReturn(logMock);
 when(sessionPropertiesMock.getUserInterfaceFactory()).thenReturn(userInterfaceFactoryMock);
 when(userInterfaceFactoryMock.createButton(anyString())).thenReturn(mock(JButton.class));
 when(parentFormMock.commitChanges()).thenReturn(true);
 when(displayableListMock.getAllItems()).thenReturn(new ArrayList<>());
 when(displayableListMock.isEmpty()).thenReturn(true);
 when(displayableListMock.itemsAreSelected()).thenReturn(false);
 when(displayableListMock.getSelectedItem()).thenReturn(null);
 when(displayableListMock.getListScrollPane()).thenReturn(mock(JScrollPane.class));
 when(displayableListMock.setDisplayItems(anyList())).thenReturn(true);

 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", true);
 displayableListPanel.displayableList = displayableListMock;
 }

 @Test
 public void testConstructor_SessionProperties_DisplayableListParentForm_String_Boolean() {
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testConstructor_SessionProperties_DisplayableListParentForm_String_Boolean_Boolean() {
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testInit() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", true, true);
 displayableListPanel.init(sessionPropertiesMock, parentFormMock, "Test Panel", true, true);

 verify(userInterfaceFactoryMock, times(3)).createButton(anyString());
 verify(parentFormMock, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testInit_MissingAdder_ShouldThrowMacawException() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", true, false);
 displayableListPanel.init(sessionPropertiesMock, parentFormMock, "Test Panel", true, false);
 }

 @Test(expected = MacawException.class)
 public void testInit_MissingEditor_ShouldThrowMacawException() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", true, true);
 displayableListPanel.init(sessionPropertiesMock, parentFormMock, "Test Panel", true, false);
 }

 @Test
 public void testBuildUI() {
 displayableListPanel.buildUI();
 verify(panel, times(1)).add(mock(JLabel.class));
 verify(panel, times(1)).add(mock(JScrollPane.class));
 }

 @Test
 public void testSetNewRecord_Boolean() {
 displayableListPanel.setNewRecord(true);
 displayableListPanel.updateButtonStates();
 verify(select, times(1)).setEnabled(false);
 verify(add, times(1)).setEnabled(false);
 verify(edit, times(1)).setEnabled(false);
 verify(view, times(1)).setEnabled(false);
 }

 @Test
 public void testSetDisplayableListItemSelector_DisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelectorMock);
 }

 @Test(expected = MacawException.class)
 public void testAddListItem_MissingAdder_ShouldThrowMacawException() {
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 displayableListPanel.editSelectedListItem();
 verify(parentFormMock, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItem_MissingEditor_ShouldThrowMacawException() {
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 displayableListPanel.viewSelectedListItem();
 verify(parentFormMock, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItem_MissingViewer_ShouldThrowMacawException() {
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableListPanel.deleteSelectedListItems();
 verify(parentFormMock, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItems_MissingDeleter_ShouldThrowMacawException() {
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();
 verify(listItemSelectorMock, times(1)).selectListItems(eq("Test Panel"), displayableListMock);
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.updateButtonStates();
 verify(add, times(1)).setEnabled(true);
 verify(select, times(1)).setEnabled(true);
 verify(delete, times(1)).setEnabled(false);
 verify(edit, times(1)).setEnabled(false);
 verify(view, times(1)).setEnabled(false);
 }

 @Test
 public void testSetDisplayableItems_ArrayList() {
 displayableListPanel.setDisplayableItems(new ArrayList<>());
 verify(displayableListMock, times(1)).setDisplayItems(anyList());
 }

 @Test
 public void testSave() {
 displayableListPanel.save();
 verify(masterList, times(1)).clear();
 verify(masterList, times(1)).addAll(eq(displayableListMock.getAllItems()));
 }

 @Test
 public void testGetAllListItems() {
 assertEquals(displayableListMock.getAllItems(), displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName_String() {
 displayableListPanel.setListOwnerName("Test Owner");
 assertEquals("Test Owner", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformed_ActionEvent() {
 ActionEvent event = mock(ActionEvent.class);
 doAnswer(invocation -> {
 }).when(displayableListPanel).selectListItems();
 doAnswer(invocation -> {
 }).when(displayableListPanel).addListItem();
 doAnswer(invocation -> {
 }).when(displayableListPanel).editSelectedListItem();
 doAnswer(invocation -> {
 }).when(displayableListPanel).viewSelectedListItem();
 doAnswer(invocation -> {
 }).when(displayableListPanel).deleteSelectedListItems();

 displayableListPanel.actionPerformed(event);

 if (event.getSource() == displayableListPanel.select) {
 verify(displayableListPanel).selectListItems();
 }
 else if (event.getSource() == displayableListPanel.add) {
 verify(displayableListPanel).addListItem();
 }
 else if (event.getSource() == displayableListPanel.edit) {
 verify(displayableListPanel).editSelectedListItem();
 }
 else if (event.getSource() == displayableListPanel.view) {
 verify(displayableListPanel).viewSelectedListItem();
 }
 else if (event.getSource() == displayableListPanel.delete) {
 verify(displayableListPanel).deleteSelectedListItems();
 }
 }
}