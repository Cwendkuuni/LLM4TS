package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import org.junit.Before;
import org.junit.Test;
import static macaw.system.MacawMessages.getMessage;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.junit.After;
import org.mockito.Mockito;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListParentForm parentFormMock;
 private SessionProperties sessionPropertiesMock;
 private UserInterfaceFactory userInterfaceFactoryMock;
 private Log logMock;
 private DisplayableList displayableListMock;
 private DisplayableListItemSelector listItemSelectorMock;
 private DisplayableListItemAdder listItemAdderMock;
 private DisplayableListItemEditor listItemEditorMock;
 private DisplayableListItemViewer listItemViewerMock;
 private DisplayableListItemDeleter listItemDeleterMock;

 @Before
 public void setUp() {
 sessionPropertiesMock = Mockito.mock(SessionProperties.class);
 parentFormMock = Mockito.mock(DisplayableListParentForm.class);
 userInterfaceFactoryMock = Mockito.mock(UserInterfaceFactory.class);
 logMock = Mockito.mock(Log.class);
 displayableListMock = Mockito.mock(DisplayableList.class);
 listItemSelectorMock = Mockito.mock(DisplayableListItemSelector.class);
 listItemAdderMock = Mockito.mock(DisplayableListItemAdder.class);
 listItemEditorMock = Mockito.mock(DisplayableListItemEditor.class);
 listItemViewerMock = Mockito.mock(DisplayableListItemViewer.class);
 listItemDeleterMock = Mockito.mock(DisplayableListItemDeleter.class);

 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Displayable List Panel", true, true);
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 }

 @Test
 public void testDisplayableListPanel_Constructor_AllowWriteAccess() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Displayable List Panel", true);
 assertEquals("Displayable List Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 }

 @Test
 public void testDisplayableListPanel_Constructor_AllowWriteAccessAndRecordEditing() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Displayable List Panel", true, true);
 assertEquals("Displayable List Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testInit() {
 displayableListPanel.init(sessionPropertiesMock, parentFormMock, "Displayable List Panel", true, true);

 Mockito.verify(parentFormMock).commitChanges();
 Mockito.verifyNoMoreInteractions(sessionPropertiesMock);
 Mockito.verifyNoMoreInteractions(userInterfaceFactoryMock);
 Mockito.verifyNoMoreInteractions(logMock);
 }

 @Test
 public void testInit_AllowWriteAccessOnly() {
 displayableListPanel.init(sessionPropertiesMock, parentFormMock, "Displayable List Panel", true, false);

 Mockito.verify(parentFormMock).commitChanges();
 Mockito.verifyNoMoreInteractions(sessionPropertiesMock);
 Mockito.verifyNoMoreInteractions(userInterfaceFactoryMock);
 Mockito.verifyNoMoreInteractions(logMock);
 }

 @Test
 public void testBuildUI() {
 displayableListPanel.buildUI();

 JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test(expected = MacawException.class)
 public void testAddListItem_MissingAdder() {
 displayableListPanel.addListItem();
 fail("Should throw MacawException for missing addItem method");
 }

 @Test
 public void testEditSelectedListItem() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable displayable = Mockito.mock(Displayable.class);
 Mockito.when(displayableListMock.getSelectedItem()).thenReturn(displayable);
 Mockito.when(displayableListMock.getAllItems()).thenReturn(masterList);
 Mockito.when(listItemEditorMock.editListItem(Mockito.eq("List Owner Name"), Mockito.eq(displayable), Mockito.eq(displayableListMock)))
 .thenReturn(displayable);

 displayableListPanel.displayableList = displayableListMock;
 displayableListPanel.listItemEditor = listItemEditorMock;
 displayableListPanel.listOwnerName = "List Owner Name";

 displayableListPanel.editSelectedListItem();

 Mockito.verify(displayableListMock).commitChanges();
 Mockito.verify(listItemEditorMock).editListItem(Mockito.eq("List Owner Name"), Mockito.eq(displayable), Mockito.eq(displayableListMock));
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItem_MissingEditor() {
 displayableListPanel.editSelectedListItem();
 fail("Should throw MacawException for missing editListItem method");
 }

 @Test
 public void testViewSelectedListItem() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable displayable = Mockito.mock(Displayable.class);
 Mockito.when(displayableListMock.getSelectedItem()).thenReturn(displayable);
 Mockito.when(listItemViewerMock.viewListItem(displayable)).thenReturn();

 displayableListPanel.displayableList = displayableListMock;
 displayableListPanel.listItemViewer = listItemViewerMock;

 displayableListPanel.viewSelectedListItem();

 Mockito.verify(displayableListMock).commitChanges();
 Mockito.verify(listItemViewerMock).viewListItem(displayable);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItem_MissingViewer() {
 displayableListPanel.viewSelectedListItem();
 fail("Should throw MacawException for missing viewListItem method");
 }

 @Test
 public void testDeleteSelectedListItems() {
 JOptionPane mockOptionPane = Mockito.mock(JOptionPane.class);
 Mockito.when(JOptionPane.showConfirmDialog(Mockito.any(Component.class), Mockito.anyString(), Mockito.anyString(), Mockito.eq(0), Mockito.eq(2)))
 .thenReturn(JOptionPane.YES_OPTION);
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable displayable1 = Mockito.mock(Displayable.class);
 Displayable displayable2 = Mockito.mock(Displayable.class);
 Mockito.when(displayableListMock.getAllItems()).thenReturn(masterList);
 Mockito.when(displayableListMock.getSelectedItem()).thenReturn(displayable1);
 Mockito.when(displayableListMock.itemsAreSelected()).thenReturn(true);
 Mockito.when(listItemDeleterMock.deleteListItems(Mockito.eq("List Owner Name"), Mockito.eq(displayableListMock)))
 .thenReturn(masterList);

 displayableListPanel.displayableList = displayableListMock;
 displayableListPanel.listItemDeleter = listItemDeleterMock;
 displayableListPanel.listOwnerName = "List Owner Name";

 displayableListPanel.deleteSelectedListItems();

 Mockito.verify(displayableListMock).commitChanges();
 Mockito.verify(listItemDeleterMock).deleteListItems(Mockito.eq("List Owner Name"), Mockito.eq(displayableListMock));
 Mockito.verify(JOptionPane.showConfirmDialog(Mockito.any(Component.class), Mockito.anyString(), Mockito.anyString(), Mockito.eq(0), Mockito.eq(2)))
 .then();
 }

 @Test
 public void testDeleteSelectedListItems_NoSelectedItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 Mockito.when(displayableListMock.getAllItems()).thenReturn(masterList);
 Mockito.when(displayableListMock.getSelectedItem()).thenReturn(null);
 Mockito.when(displayableListMock.itemsAreSelected()).thenReturn(false);
 Mockito.when(listItemDeleterMock.deleteListItems(Mockito.eq("List Owner Name"), Mockito.eq(displayableListMock)))
 .thenThrow(new MacawException("Error"));

 displayableListPanel.displayableList = displayableListMock;
 displayableListPanel.listItemDeleter = listItemDeleterMock;
 displayableListPanel.listOwnerName = "List Owner Name";

 try {
 displayableListPanel.deleteSelectedListItems();
 fail("Should throw MacawException for unselected items");
 } catch (MacawException e) {
 // Expected
 }

 Mockito.verify(displayableListMock).commitChanges();
 Mockito.verifyZeroInteractions(listItemDeleterMock);
 }

 @Test
 public void testSelectListItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable displayable1 = Mockito.mock(Displayable.class);
 Displayable displayable2 = Mockito.mock(Displayable.class);
 Mockito.when(displayableListMock.getAllItems()).thenReturn(masterList);
 Mockito.when(displayableListMock.getSelectedItem()).thenReturn(null);
 Mockito.when(displayableListMock.itemsAreSelected()).thenReturn(false);
 Mockito.when(listItemSelectorMock.selectListItems(Mockito.eq("List Owner Name"), Mockito.eq(displayableListMock)))
 .thenReturn(masterList);

 displayableListPanel.displayableList = displayableListMock;
 displayableListPanel.listItemSelector = listItemSelectorMock;
 displayableListPanel.listOwnerName = "List Owner Name";

 displayableListPanel.selectListItems();

 Mockito.verify(displayableListMock).commitChanges();
 Mockito.verify(listItemSelectorMock).selectListItems(Mockito.eq("List Owner Name"), Mockito.eq(displayableListMock));
 }

 @Test(expected = MacawException.class)
 public void testSelectListItems_MissingSelector() {
 displayableListPanel.selectListItems();
 fail("Should throw MacawException for missing selectListItems method");
 }

 @Test
 public void testUpdateButtonStates() {
 Mockito.when(displayableListMock.isEmpty()).thenReturn(false);
 Mockito.when(displayableListMock.itemsAreSelected()).thenReturn(false);
 Mockito.when(displayableListMock.getSelectedItem()).thenReturn(null);
 displayableListPanel.isNewRecord = false;
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.allowRecordEditing = true;
 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testUpdateButtonStates_NewRecord() {
 Mockito.when(displayableListMock.isEmpty()).thenReturn(false);
 Mockito.when(displayableListMock.itemsAreSelected()).thenReturn(false);
 Mockito.when(displayableListMock.getSelectedItem()).thenReturn(null);
 displayableListPanel.isNewRecord = true;
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testUpdateButtonStates_NoWriteAccess() {
 Mockito.when(displayableListMock.isEmpty()).thenReturn(false);
 Mockito.when(displayableListMock.itemsAreSelected()).thenReturn(false);
 Mockito.when(displayableListMock.getSelectedItem()).thenReturn(null);
 displayableListPanel.isNewRecord = false;
 displayableListPanel.allowWriteAccess = false;
 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testUpdateButtonStates_SelectedItem() {
 Mockito.when(displayableListMock.isEmpty()).thenReturn(false);
 Mockito.when(displayableListMock.itemsAreSelected()).thenReturn(true);
 Mockito.when(displayableListMock.getSelectedItem()).thenReturn(Mockito.mock(Displayable.class));
 displayableListPanel.isNewRecord = false;
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.allowRecordEditing = true;
 displayableListPanel.updateButtonStates();

 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable displayable1 = Mockito.mock(Displayable.class);
 Displayable displayable2 = Mockito.mock(Displayable.class);
 Mockito.when(displayableListMock.getAllItems()).thenReturn(masterList);
 Mockito.when(displayableListMock.setDisplayItems(masterList)).thenReturn();

 displayableListPanel.displayableList = displayableListMock;
 displayableListPanel.setDisplayableItems(masterList);

 Mockito.verify(displayableListMock).setDisplayItems(masterList);
 }

 @Test
 public void testActionPerformed() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);

 displayableListPanel.select = Mockito.mock(JButton.class);
 Mockito.when(displayableListPanel.select.doClick()).thenCallRealMethod();
 Mockito.when(displayableListPanel.select.getName()).thenReturn("select");
 Mockito.when(displayableListPanel.add.getName()).thenReturn("add");
 Mockito.when(displayableListPanel.edit.getName()).thenReturn("edit");
 Mockito.when(displayableListPanel.view.getName()).thenReturn("view");
 Mockito.when(displayableListPanel.delete.getName()).thenReturn("delete");

 displayableListPanel.actionPerformed(actionEvent);

 assertEquals("select", event.getSource().getName());
 }

}