package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import org.junit.*;
import org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List", true);
 }

 @Test
 public void testInitWithAllowWriteAccess() {
 // Test initializing with allowWriteAccess set to true
 displayableListPanel.init(sessionProperties, parentForm, "Displayable List", true, false);

 // Check that the panelTitleText was set correctly
 assertEquals("Displayable List", displayableListPanel.getPanelTitleText());

 // Check that the allowWriteAccess was set correctly
 assertTrue(displayableListPanel.isAllowWriteAccess());
 }

 @Test
 public void testInitWithAllowWriteAccessAndAllowRecordEditing() {
 // Test initializing with allowWriteAccess and allowRecordEditing set to true
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List", true, true);

 // Check that the panelTitleText was set correctly
 assertEquals("Displayable List", displayableListPanel.getPanelTitleText());

 // Check that the allowWriteAccess and allowRecordEditing were set correctly
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testBuildUI() {
 // Test building UI
 displayableListPanel.buildUI();

 // Check that the panel was created
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 // Test setting displayableListItemSelector
 DisplayableListItemSelector mockListItemSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockListItemSelector);

 // Check that the listItemSelector was set
 assertEquals(mockListItemSelector, displayableListPanel.getListItemSelector());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutListItemAdder() {
 // Test adding a list item without listItemAdder
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 // Test editing a selected list item
 DisplayableListItemEditor mockListItemEditor = mock(DisplayableListItemEditor.class);
 doReturn(mockListItemEditor).when(displayableListPanel).getListItemEditor();
 when(mockListItemEditor.editListItem(displayableListPanel.getListOwnerName(), eq(mock(Displayable.class)), eq(displayableListPanel.getDisplayableList())))
 .thenReturn(mock(Displayable.class));

 List<Displayable> masterList = new ArrayList<>();
 Displayable listItemToEdit = new Displayable("Test Displayable");
 masterList.add(listItemToEdit);
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.setNewRecord(false);
 displayableListPanel.setDisplayableListItemEditor(mockListItemEditor);
 displayableListPanel.selectListItems();
 displayableListPanel.editSelectedListItem();

 // Check that the selected list item was edited
 Displayable editedListItem = mockListItemEditor.editListItem(displayableListPanel.getListOwnerName(), listItemToEdit, displayableListPanel.getDisplayableList()).get();
 assertEquals(listItemToEdit.getDisplayName(), editedListItem.getDisplayName());
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithoutListItemViewer() {
 // Test viewing a selected list item without listItemViewer
 doReturn(mock(Displayable.class)).when(displayableListPanel).getSelectedListItem();
 DisplayableListItemViewer mockListItemViewer = mock(DisplayableListItemViewer.class);
 doReturn(mockListItemViewer).when(displayableListPanel).getListItemViewer();

 displayableListPanel.setDisplayableListItemViewer(mockListItemViewer);
 displayableListPanel.viewSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithoutListItemDeleter() {
 // Test deleting selected list items without listItemDeleter
 doReturn(true).when(displayableListPanel).displayableListItemsAreSelected();
 DisplayableListItemDeleter mockListItemDeleter = mock(DisplayableListItemDeleter.class);
 doReturn(mockListItemDeleter).when(displayableListPanel).getListItemDeleter();

 displayableListPanel.setDisplayableListItemDeleter(mockListItemDeleter);
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testUpdateButtonStates() {
 // Test updating button states
 displayableListPanel.updateButtonStates();

 // Check that the add button is disabled when in new record mode
 assertFalse(displayableListPanel.getAddButton().isEnabled());

 // Check that the delete button is disabled when no items are selected
 assertFalse(displayableListPanel.getDeleteButton().isEnabled());

 // Check that the edit button is disabled when no items are selected
 assertFalse(displayableListPanel.getEditButton().isEnabled());

 // Check that the view button is disabled when no items are selected
 assertFalse(displayableListPanel.getViewButton().isEnabled());

 // Check that the select button is enabled when no items are selected
 assertTrue(displayableListPanel.getSelectButton().isEnabled());

 // Check that the add button is enabled when not in new record mode and there are items to add to
 List<Displayable> masterList = new ArrayList<>();
 Displayable listItemToAdd = new Displayable("Test Displayable");
 masterList.add(listItemToAdd);
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.getAddButton().isEnabled());

 // Check that the delete button is enabled when an item is selected for deletion
 List<Displayable> selectedListItems = new ArrayList<>();
 Displayable listItemToDelete = new Displayable("Test Displayable to Delete");
 selectedListItems.add(listItemToDelete);
 doReturn(selectedListItems).when(displayableListPanel).getSelectedListItems();
 assertTrue(displayableListPanel.getDeleteButton().isEnabled());

 // Check that the edit button is enabled when an item is selected for editing
 Displayable listItemToEdit = new Displayable("Test Displayable to Edit");
 masterList.add(listItemToEdit);
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.setNewRecord(false);
 doReturn(listItemToEdit).when(displayableListPanel).getSelectedListItem();
 assertTrue(displayableListPanel.getEditButton().isEnabled());

 // Check that the view button is enabled when an item is selected for viewing
 assertTrue(displayableListPanel.getViewButton().isEnabled());
 }
}