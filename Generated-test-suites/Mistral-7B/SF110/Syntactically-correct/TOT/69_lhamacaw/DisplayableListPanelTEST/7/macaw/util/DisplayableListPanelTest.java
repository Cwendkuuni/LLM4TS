package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.system.MacawException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListParentForm parentFormMock;
 private UserInterfaceFactory userInterfaceFactoryMock;
 private SessionProperties sessionPropertiesMock;
 private DisplayableList displayableListMock;
 private DisplayableListItemSelector listItemSelectorMock;
 private DisplayableListItemAdder listItemAdderMock;
 private DisplayableListItemEditor listItemEditorMock;
 private DisplayableListItemViewer listItemViewerMock;
 private DisplayableListItemDeleter listItemDeleterMock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 this.parentFormMock = mock(DisplayableListParentForm.class);
 this.userInterfaceFactoryMock = mock(UserInterfaceFactory.class);
 this.sessionPropertiesMock = mock(SessionProperties.class);
 this.displayableListMock = mock(DisplayableList.class);
 this.listItemSelectorMock = mock(DisplayableListItemSelector.class);
 this.listItemAdderMock = mock(DisplayableListItemAdder.class);
 this.listItemEditorMock = mock(DisplayableListItemEditor.class);
 this.listItemViewerMock = mock(DisplayableListItemViewer.class);
 this.listItemDeleterMock = mock(DisplayableListItemDeleter.class);

 this.displayableListPanel = new DisplayableListPanel(this.sessionPropertiesMock, this.parentFormMock, "Test Title", true, true);
 this.displayableListPanel.setDisplayableList(this.displayableListMock);
 this.displayableListPanel.setDisplayableListItemSelector(this.listItemSelectorMock);
 this.displayableListPanel.setDisplayableListItemAdder(this.listItemAdderMock);
 this.displayableListPanel.setDisplayableListItemEditor(this.listItemEditorMock);
 this.displayableListPanel.setDisplayableListItemViewer(this.listItemViewerMock);
 this.displayableListPanel.setDisplayableListItemDeleter(this.listItemDeleterMock);
 }

 @Test
 public void testInit() {
 // check if the session properties is set
 assertEquals("Test Title", this.displayableListPanel.getPanelTitleText());
 assertTrue(this.displayableListPanel.isAllowWriteAccess());
 assertTrue(this.displayableListPanel.isAllowRecordEditing());
 // check if the user interface factory is set
 assertNotNull(this.displayableListPanel.getUserInterfaceFactory());
 // check if the parent form is set
 assertSame(this.parentFormMock, this.displayableListPanel.getParentForm());
 // check if the buttons are created and disabled
 assertNotNull(this.displayableListPanel.getSelect());
 assertNotNull(this.displayableListPanel.getAdd());
 assertNotNull(this.displayableListPanel.getEdit());
 assertNotNull(this.displayableListPanel.getView());
 assertNotNull(this.displayableListPanel.getDelete());
 // check if the buttons are disabled
 verify(this.listItemAdderMock, never()).addItems(anyString(), any(DisplayableList.class));
 verify(this.listItemEditorMock, never()).editListItem(anyString(), any(Displayable.class), any(DisplayableList.class));
 verify(this.listItemViewerMock, never()).viewListItem(any(Displayable.class));
 verify(this.listItemDeleterMock, never()).deleteListItems(anyString(), any(DisplayableList.class));
 }

 @Test
 public void testAddListItem() throws MacawException {
 // setup the mocks to return a list of Displayable objects
 when(this.listItemAdderMock.addItems(anyString(), any(DisplayableList.class))).thenReturn(new ArrayList<Displayable>());

 this.displayableListPanel.setNewRecord(false);
 this.displayableListPanel.addListItem();

 // check if the changes are committed before adding items
 verify(this.parentFormMock).commitChanges();
 // check if the items are added to the list
 verify(this.displayableListMock).addAll(anyCollectionOf(Displayable.class));
 // check if the button states are updated
 verify(this.displayableListPanel).updateButtonStates();
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 this.displayableListPanel.setNewRecord(false);
 this.displayableListPanel.addListItem();

 verify(this.parentFormMock).commitChanges();
 verify(this.listItemAdderMock, never()).addItems(anyString(), any(DisplayableList.class));

 // check if an exception is thrown when the adder is missing
 verifyThrows(MacawException.class, () -> this.displayableListPanel.addListItem());
 }

 @Test
 public void testEditSelectedListItem() throws MacawException {
 // setup the mocks to return a Displayable object
 when(this.listItemEditorMock.editListItem(anyString(), any(Displayable.class), any(DisplayableList.class))).thenReturn(new Displayable());

 this.displayableListPanel.setNewRecord(false);
 this.displayableListPanel.setDisplayableListItemSelector(this.listItemSelectorMock);
 this.displayableListPanel.setDisplayableListItemEditor(this.listItemEditorMock);
 this.displayableListPanel.displayableList.add(new Displayable());
 this.displayableListPanel.displayableList.setSelectedItem(new Displayable());
 this.displayableListPanel.editSelectedListItem();

 // check if the changes are committed before editing an item
 verify(this.parentFormMock).commitChanges();
 // check if the item is edited
 verify(this.listItemEditorMock).editListItem(anyString(), any(Displayable.class), any(DisplayableList.class));
 // check if the button states are updated
 verify(this.displayableListPanel).updateButtonStates();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 this.displayableListPanel.setNewRecord(false);
 this.displayableListPanel.setDisplayableListItemSelector(this.listItemSelectorMock);
 this.displayableListPanel.displayableList.add(new Displayable());
 this.displayableListPanel.displayableList.setSelectedItem(new Displayable());
 this.displayableListPanel.editSelectedListItem();

 verify(this.parentFormMock).commitChanges();
 verify(this.listItemEditorMock, never()).editListItem(anyString(), any(Displayable.class), any(DisplayableList.class));

 // check if an exception is thrown when the editor is missing
 verifyThrows(MacawException.class, () -> this.displayableListPanel.editSelectedListItem());
 }

 @Test
 public void testViewSelectedListItem() throws MacawException {
 // setup the mocks to return a Displayable object
 when(this.listItemViewerMock.viewListItem(any(Displayable.class))).thenReturn(null);

 this.displayableListPanel.setNewRecord(false);
 this.displayableListPanel.setDisplayableListItemSelector(this.listItemSelectorMock);
 this.displayableListPanel.setDisplayableListItemViewer(this.listItemViewerMock);
 this.displayableListPanel.displayableList.add(new Displayable());
 this.displayableListPanel.displayableList.setSelectedItem(new Displayable());
 this.displayableListPanel.viewSelectedListItem();

 // check if the changes are committed before viewing an item
 verify(this.parentFormMock).commitChanges();
 // check if the item is viewed
 verify(this.listItemViewerMock).viewListItem(any(Displayable.class));
 }

 @Test
 public void testDeleteSelectedListItems() throws MacawException {
 // setup the mocks to return a confirmation
 when(this.listItemDeleterMock.deleteListItems(anyString(), any(DisplayableList.class))).thenReturn(true);

 this.displayableListPanel.setNewRecord(false);
 this.displayableListPanel.displayableList.add(new Displayable());
 this.displayableListPanel.displayableList.add(new Displayable());
 this.displayableListPanel.displayableList.setSelectedItem(new Displayable());
 this.displayableListPanel.deleteSelectedListItems();

 // check if the changes are committed before deleting items
 verify(this.parentFormMock).commitChanges();
 // check if the items are deleted
 verify(this.listItemDeleterMock).deleteListItems(anyString(), any(DisplayableList.class));
 // check if a confirmation dialog is displayed
 verify(this.parentFormMock).displayConfirmationDialog(anyString(), anyString(), anyInt(), anyInt());
 // check if the button states are updated
 verify(this.displayableListPanel).updateButtonStates();
 }

 @Test
 public void testDeleteSelectedListItemsNoItemsSelected() throws MacawException {
 this.displayableListPanel.setNewRecord(false);
 this.displayableListPanel.displayableList.clear();
 this.displayableListPanel.deleteSelectedListItems();

 // check if the changes are committed before deleting items
 verify(this.parentFormMock).commitChanges();
 // check if no items are deleted
 verify(this.listItemDeleterMock, never()).deleteListItems(anyString(), any(DisplayableList.class));
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter() throws MacawException {
 this.displayableListPanel.setNewRecord(false);
 this.displayableListPanel.displayableList.add(new Displayable());
 this.displayableListPanel.displayableList.setSelectedItem(new Displayable());
 this.displayableListPanel.deleteSelectedListItems();

 verify(this.parentFormMock).commitChanges();
 verify(this.listItemDeleterMock, never()).deleteListItems(anyString(), any(DisplayableList.class));

 // check if an exception is thrown when the deleter is missing
 verifyThrows(MacawException.class, () -> this.displayableListPanel.deleteSelectedListItems());
 }

 @Test
 public void testSelectListItems() throws MacawException {
 // setup the mocks to select all items
 when(this.listItemSelectorMock.selectListItems(anyString(), any(DisplayableList.class))).thenReturn(true);

 this.displayableListPanel.setNewRecord(false);
 this.displayableListPanel.displayableList.add(new Displayable());
 this.displayableListPanel.displayableList.add(new Displayable());
 this.displayableListPanel.displayableList.add(new Displayable());
 this.displayableListPanel.selectListItems();

 // check if the changes are committed before selecting items
 verify(this.parentFormMock).commitChanges();
 // check if all items are selected
 verify(this.listItemSelectorMock).selectListItems(anyString(), any(DisplayableList.class));
 // check if the button states are updated
 verify(this.displayableListPanel).updateButtonStates();
 }

 @Test
 public void testSelectListItemsNoItemsSelected() throws MacawException {
 this.displayableListPanel.setNewRecord(false);
 this.displayableListPanel.displayableList.clear();
 this.displayableListPanel.selectListItems();

 // check if the changes are committed before selecting items
 verify(this.parentFormMock).commitChanges();
 // check if no items are selected
 verify(this.listItemSelectorMock, never()).selectListItems(anyString(), any(DisplayableList.class));
 }

}