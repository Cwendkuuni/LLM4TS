package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private JButton selectButton, addButton, editButton, viewButton, deleteButton;
 private DisplayableListParentForm parentFormMock;
 private SessionProperties sessionPropertiesMock;
 private Log logMock;
 private UserInterfaceFactory userInterfaceFactoryMock;
 private DisplayableList displayableListMock;
 private DisplayableListItemSelector listItemSelectorMock;
 private DisplayableListItemAdder listItemAdderMock;
 private DisplayableListItemEditor listItemEditorMock;
 private DisplayableListItemViewer listItemViewerMock;
 private DisplayableListItemDeleter listItemDeleterMock;

 @Before
 public void setUp() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", true);

 selectButton = mock(JButton.class);
 addButton = mock(JButton.class);
 editButton = mock(JButton.class);
 viewButton = mock(JButton.class);
 deleteButton = mock(JButton.class);

 displayableListPanel.select = selectButton;
 displayableListPanel.add = addButton;
 displayableListPanel.edit = editButton;
 displayableListPanel.view = viewButton;
 displayableListPanel.delete = deleteButton;

 parentFormMock = mock(DisplayableListParentForm.class);
 sessionPropertiesMock = mock(SessionProperties.class);
 logMock = mock(Log.class);
 userInterfaceFactoryMock = mock(UserInterfaceFactory.class);
 displayableListMock = mock(DisplayableList.class);

 when(sessionPropertiesMock.getProperty("log")).thenReturn(logMock);
 when(sessionPropertiesMock.getUserInterfaceFactory()).thenReturn(userInterfaceFactoryMock);
 when(userInterfaceFactoryMock.createLabel(anyString())).thenReturn(mock(JLabel.class));
 when(userInterfaceFactoryMock.createPanel()).thenReturn(mock(JPanel.class));
 when(userInterfaceFactoryMock.createButton(anyString())).thenReturn(selectButton);
 when(userInterfaceFactoryMock.createButton(anyString())).thenReturn(addButton);
 when(userInterfaceFactoryMock.createButton(anyString())).thenReturn(editButton);
 when(userInterfaceFactoryMock.createButton(anyString())).thenReturn(viewButton);
 when(userInterfaceFactoryMock.createButton(anyString())).thenReturn(deleteButton);
 when(userInterfaceFactoryMock.createScrollPane(any(JComponent.class))).thenReturn(mock(JScrollPane.class));
 when(displayableListMock.getListScrollPane()).thenReturn(mock(JScrollPane.class));
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowWriteAccessAndAllowRecordEditing() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", true, true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowWriteAccessAndNoAllowRecordEditing() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", true, false);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelConstructionWithoutAllowWriteAccess() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", false);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelectorMock);
 verify(displayableListPanel, times(1)).setDisplayableListItemSelector(listItemSelectorMock);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdderMock);
 verify(displayableListPanel, times(1)).setDisplayableListItemAdder(listItemAdderMock);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditorMock);
 verify(displayableListPanel, times(1)).setDisplayableListItemEditor(listItemEditorMock);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewerMock);
 verify(displayableListPanel, times(1)).setDisplayableListItemViewer(listItemViewerMock);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleterMock);
 verify(displayableListPanel, times(1)).setDisplayableListItemDeleter(listItemDeleterMock);
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 verify(parentFormMock, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutListItemAdder() {
 displayableListPanel.addListItem();
 verify(parentFormMock, times(1)).commitChanges();
 verify(displayableListMock, never()).addItems(anyString(), anyList());
 }

 @Test
 public void testEditSelectedListItem() {
 displayableListPanel.editSelectedListItem();
 verify(parentFormMock, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithoutListItemEditor() {
 displayableListPanel.editSelectedListItem();
 verify(parentFormMock, times(1)).commitChanges();
 verify(displayableListMock, never()).editListItem(anyString(), any(Displayable.class), anyList());
 }

 @Test
 public void testViewSelectedListItem() {
 displayableListPanel.viewSelectedListItem();
 verify(parentFormMock, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithoutListItemViewer() {
 displayableListPanel.viewSelectedListItem();
 verify(parentFormMock, times(1)).commitChanges();
 verify(displayableListMock, never()).viewListItem(any(Displayable.class));
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableListPanel.deleteSelectedListItems();
 verify(parentFormMock, times(1)).commitChanges();
 verify(parentFormMock, times(1)).showErrorDialog(anyString(), anyString());
 verify(displayableListMock, times(1)).deleteListItems(anyString(), anyList());
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();
 verify(parentFormMock, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsWithoutListItemSelector() {
 displayableListPanel.selectListItems();
 verify(parentFormMock, times(1)).commitChanges();
 verify(displayableListMock, never()).selectListItems(anyString(), anyList());
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", true);
 displayableListPanel.setDisplayableList(displayableListMock);
 displayableListPanel.setIsNewRecord(true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.setIsNewRecord(false);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.displayableList.setDisplayItems(Collections.singletonList(new Displayable()));
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.allowWriteAccess = false;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }
}