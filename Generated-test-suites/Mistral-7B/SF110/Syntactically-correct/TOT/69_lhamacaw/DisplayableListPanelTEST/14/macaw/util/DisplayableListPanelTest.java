import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.util.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListParentForm parentFormMock;
 private SessionProperties sessionPropertiesMock;
 private UserInterfaceFactory userInterfaceFactoryMock;
 private Log logMock;
 private User currentUserMock;
 private ArrayList<Displayable> masterList;
 private DisplayableList displayableListMock;
 private DisplayableListItemSelector listItemSelectorMock;
 private DisplayableListItemAdder listItemAdderMock;
 private DisplayableListItemEditor listItemEditorMock;
 private DisplayableListItemViewer listItemViewerMock;
 private DisplayableListItemDeleter listItemDeleterMock;

 @Before
 public void setup() {
 this.parentFormMock = mock(DisplayableListParentForm.class);
 this.sessionPropertiesMock = mock(SessionProperties.class);
 this.userInterfaceFactoryMock = mock(UserInterfaceFactory.class);
 this.logMock = mock(Log.class);
 this.currentUserMock = mock(User.class);
 this.masterList = new ArrayList<>();
 this.displayableListMock = mock(DisplayableList.class);
 this.listItemSelectorMock = mock(DisplayableListItemSelector.class);
 this.listItemAdderMock = mock(DisplayableListItemAdder.class);
 this.listItemEditorMock = mock(DisplayableListItemEditor.class);
 this.listItemViewerMock = mock(DisplayableListItemViewer.class);
 this.listItemDeleterMock = mock(DisplayableListItemDeleter.class);

 when(this.sessionPropertiesMock.getProperty("currentUser")).thenReturn(this.currentUserMock);
 when(this.sessionPropertiesMock.getLog()).thenReturn(this.logMock);
 when(this.userInterfaceFactoryMock.createButton(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);

 this.displayableListPanel = new DisplayableListPanel(this.sessionPropertiesMock, this.parentFormMock, "Test Displayable List Panel", true, true);
 this.displayableListPanel.setDisplayableListItemSelector(this.listItemSelectorMock);
 this.displayableListPanel.setDisplayableListItemAdder(this.listItemAdderMock);
 this.displayableListPanel.setDisplayableListItemEditor(this.listItemEditorMock);
 this.displayableListPanel.setDisplayableListItemViewer(this.listItemViewerMock);
 this.displayableListPanel.setDisplayableListItemDeleter(this.listItemDeleterMock);
 this.displayableListPanel.setDisplayableList(this.displayableListMock);
 }

 @Test
 public void testInit() {
 this.displayableListPanel.init(this.sessionPropertiesMock, this.parentFormMock, "Test Displayable List Panel", true, true);

 verify(this.parentFormMock).commitChanges();
 verify(this.displayableListMock).setDisplayItems(this.masterList);
 }

 @Test
 public void testSetNewRecord() {
 this.displayableListPanel.setNewRecord(true);

 verify(this.add).setEnabled(false);
 verify(this.delete).setEnabled(false);
 verify(this.edit).setEnabled(false);
 verify(this.view).setEnabled(false);
 verify(this.select).setEnabled(false);
 }

 @Test
 public void testAddListItem() {
 this.displayableListPanel.addListItem();

 verify(this.parentFormMock).commitChanges();
 verify(this.listItemAdderMock).addItems("Test Displayable List Panel", this.displayableListMock);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 this.displayableListPanel = new DisplayableListPanel(this.sessionPropertiesMock, this.parentFormMock, "Test Displayable List Panel", true, false);
 this.displayableListPanel.addListItem();
 fail("MacawException expected");
 }

 @Test
 public void testEditSelectedListItem() {
 this.displayableListPanel.editSelectedListItem();

 verify(this.parentFormMock).commitChanges();
 verify(this.listItemEditorMock).editListItem("Test Displayable List Panel", any(Displayable.class), this.displayableListMock);
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 this.displayableListPanel = new DisplayableListPanel(this.sessionPropertiesMock, this.parentFormMock, "Test Displayable List Panel", true, false);
 this.displayableListPanel.editSelectedListItem();
 fail("MacawException expected");
 }

 @Test
 public void testViewSelectedListItem() {
 this.displayableListPanel.viewSelectedListItem();

 verify(this.listItemViewerMock).viewListItem(any(Displayable.class));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 this.displayableListPanel = new DisplayableListPanel(this.sessionPropertiesMock, this.parentFormMock, "Test Displayable List Panel", true, false);
 this.displayableListPanel.viewSelectedListItem();
 fail("MacawException expected");
 }

 @Test
 public void testDeleteSelectedListItems() {
 this.displayableListPanel.deleteSelectedListItems();

 verify(this.parentFormMock).commitChanges();
 verify(this.listItemDeleterMock).deleteListItems("Test Displayable List Panel", this.displayableListMock);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter() {
 this.displayableListPanel = new DisplayableListPanel(this.sessionPropertiesMock, this.parentFormMock, "Test Displayable List Panel", true, false);
 this.displayableListPanel.deleteSelectedListItems();
 fail("MacawException expected");
 }

 @Test
 public void testSelectListItems() {
 this.displayableListPanel.selectListItems();

 verify(this.listItemSelectorMock).selectListItems("Test Displayable List Panel", this.displayableListMock);
 }

 @Test
 public void testUpdateButtonStates() {
 this.displayableListPanel.updateButtonStates();

 if (this.displayableListMock.isEmpty()) {
 verify(this.delete).setEnabled(false);
 verify(this.edit).setEnabled(false);
 } else {
 verify(this.delete).setEnabled(true);
 verify(this.edit).setEnabled(true);
 }
 }

 @Test
 public void testSetDisplayableItems() {
 this.displayableListPanel.setDisplayableItems(new ArrayList<>());
 this.displayableListPanel.updateButtonStates();

 verify(this.delete).setEnabled(false);
 verify(this.edit).setEnabled(false);
 }

 @Test
 public void testSave() {
 this.displayableListPanel.save();

 assertEquals(this.masterList, this.displayableListMock.getAllItems());
 }

 @Test
 public void testGetAllListItems() {
 assertTrue(this.displayableListPanel.getAllListItems().isEmpty());
 }

 @Test
 public void testSetListOwnerName() {
 this.displayableListPanel.setListOwnerName("Test List Owner");

 assertEquals("Test List Owner", this.displayableListPanel.getListOwnerName());
 }

 @Test
 public void testActionPerformed() {
 this.displayableListPanel.actionPerformed(new ActionEvent(this.add, ActionEvent.ACTION_PERFORMED, ""));
 this.displayableListPanel.addListItem();

 this.displayableListPanel.actionPerformed(new ActionEvent(this.edit, ActionEvent.ACTION_PERFORMED, ""));
 this.displayableListPanel.editSelectedListItem();

 this.displayableListPanel.actionPerformed(new ActionEvent(this.view, ActionEvent.ACTION_PERFORMED, ""));
 this.displayableListPanel.viewSelectedListItem();

 this.displayableListPanel.actionPerformed(new ActionEvent(this.delete, ActionEvent.ACTION_PERFORMED, ""));
 this.displayableListPanel.deleteSelectedListItems();
 }
}