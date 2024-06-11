package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.util.*;
import org.junit.Test;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableList displayableList;
 private DisplayableListParentForm parentForm;
 private SessionProperties sessionProperties;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 // Initialize test objects
 sessionProperties = new SessionProperties();
 log = new Log();
 userInterfaceFactory = new UserInterfaceFactory();
 parentForm = new DisplayableListParentForm();
 displayableList = new DisplayableList(userInterfaceFactory);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 // Test constructor with allowWriteAccess and allowRecordEditing
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccessOnly() {
 // Test constructor with allowWriteAccess only
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, false);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testEmptyDisplayableListPanel() {
 // Test empty DisplayableListPanel
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertEquals(0, displayableListPanel.displayableList.getAllItems().size());
 }

 @Test
 public void testDisplayableListPanelWithItems() {
 // Test DisplayableListPanel with items
 final Displayable displayable1 = new Displayable("Displayable 1", "DisplayName 1");
 final Displayable displayable2 = new Displayable("Displayable 2", "DisplayName 2");
 displayableList.addItem(displayable1);
 displayableList.addItem(displayable2);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setDisplayableList(displayableList);
 assertEquals(2, displayableListPanel.displayableList.getAllItems().size());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutAdder() {
 // Test adding a list item without an adder
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.addListItem();
 fail("Adding a list item without an adder should throw an exception");
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithoutEditor() {
 // Test editing a selected list item without an editor
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setDisplayableList(displayableList);
 displayableList.addItem(new Displayable("Test Item", "Test DisplayName"));
 displayableList.setSelectedIndex(0);
 displayableListPanel.editSelectedListItem();
 fail("Editing a selected list item without an editor should throw an exception");
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithoutViewer() {
 // Test viewing a selected list item without a viewer
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setDisplayableList(displayableList);
 displayableList.addItem(new Displayable("Test Item", "Test DisplayName"));
 displayableList.setSelectedIndex(0);
 displayableListPanel.viewSelectedListItem();
 fail("Viewing a selected list item without a viewer should throw an exception");
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithoutDeleter() {
 // Test deleting selected list items without a deleter
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setDisplayableList(displayableList);
 displayableList.addItem(new Displayable("Test Item 1", "Test DisplayName 1"));
 displayableList.addItem(new Displayable("Test Item 2", "Test DisplayName 2"));
 displayableList.setSelectedIndices(new int[] {0, 1});
 displayableListPanel.deleteSelectedListItems();
 fail("Deleting selected list items without a deleter should throw an exception");
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsWithoutSelector() {
 // Test selecting list items without a selector
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.selectListItems();
 fail("Selecting list items without a selector should throw an exception");
 }

 @Test
 public void testSetNewRecord() {
 // Test setting new record flag
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isNewRecord());
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.isNewRecord());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 // Test setting displayable list item selector
 final DisplayableListItemSelector mockListItemSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockListItemSelector);
 assertEquals(mockListItemSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 // Test setting displayable list item adder
 final DisplayableListItemAdder mockListItemAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(mockListItemAdder);
 assertEquals(mockListItemAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 // Test setting displayable list item editor
 final DisplayableListItemEditor mockListItemEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(mockListItemEditor);
 assertEquals(mockListItemEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 // Test setting displayable list item viewer
 final DisplayableListItemViewer mockListItemViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(mockListItemViewer);
 assertEquals(mockListItemViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 // Test setting displayable list item deleter
 final DisplayableListItemDeleter mockListItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(mockListItemDeleter);
 assertEquals(mockListItemDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testUpdateButtonStates() {
 // Test update button states
 final Displayable displayable1 = new Displayable("Displayable 1", "DisplayName 1");
 final Displayable displayable2 = new Displayable("Displayable 2", "DisplayName 2");
 displayableList.addItem(displayable1);
 displayableList.addItem(displayable2);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setDisplayableList(displayableList);
 displayableList.setSelectedIndex(0);
 displayableListPanel.updateButtonStates();
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
 displayableList.setSelectedIndex(1);
 assertTrue(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 displayableListPanel.allowRecordEditing = true;
 assertTrue(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 // Test setting displayable items
 final ArrayList<Displayable> masterList = new ArrayList<Displayable>();
 masterList.add(new Displayable("Displayable 1", "DisplayName 1"));
 masterList.add(new Displayable("Displayable 2", "DisplayName 2"));
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(2, displayableListPanel.displayableList.getAllItems().size());
 }

 @Test
 public void testSave() {
 // Test saving displayable list items
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.save();
 assertEquals(displayableListPanel.masterList.size(), displayableListPanel.displayableList.getAllItems().size());
 }

 @Test
 public void testGetAllListItems() {
 // Test getting all list items
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setDisplayableList(displayableList);
 assertEquals(displayableList.getAllItems(), displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 // Test setting list owner name
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setListOwnerName("Test List Owner");
 assertEquals("Test List Owner", displayableListPanel.listOwnerName);
 }
}