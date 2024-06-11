package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import macaw.businessLayer.*;
import macaw.system.*;
import macaw.util.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.annotations.*;

@RunWith(MockitoJUnitRunner.class)
public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListParentForm parentForm;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;
 private DisplayableList displayableList;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemDeleter listItemDeleter;

 @Mock
 private User user;
 @Mock
 private DisplayableListItem displayableListItem;
 @Mock
 private DisplayableList selectedDisplayableList;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 log = mock(Log.class);
 displayableList = mock(DisplayableList.class);
 listItemSelector = mock(DisplayableListItemSelector.class);
 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);

 when(sessionProperties.getLog()).thenReturn(log);
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(userInterfaceFactory);
 when(sessionProperties.getProperty("currentUser")).thenReturn(user);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);
 }

 @Test
 public void testInit() {
 // Test that the DisplayableListPanel is initialized with the correct arguments
 verify(parentForm, times(1)).commitChanges();
 verify(userInterfaceFactory, times(6)).createButton(anyString());
 verify(userInterfaceFactory, times(1)).createPanel();
 }

 @Test
 public void testGetPanel() {
 // Test that the DisplayableListPanel returns a JPanel when getPanel() is called
 assertEquals("panel", displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 verify(displayableListPanel, times(1)).setListItemSelector(listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 verify(displayableListPanel, times(1)).setListItemAdder(listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 verify(displayableListPanel, times(1)).setListItemEditor(listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 verify(displayableListPanel, times(1)).setListItemDeleter(listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 // Test that addListItem() calls commitChanges() and adds an item to the DisplayableList
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);

 doNothing().when(listItemAdder).addItems(anyString(), any(DisplayableList.class));
 displayableListPanel.addListItem();
 verify(parentForm, times(1)).commitChanges();
 verify(listItemAdder, times(1)).addItems(eq("Test Owner"), eq(displayableList));
 }

 @Test
 public void testEditSelectedListItem() {
 // Test that editSelectedListItem() calls commitChanges() and edits an item in the DisplayableList
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);

 when(displayableList.getSelectedItem()).thenReturn(displayableListItem);

 doNothing().when(listItemEditor).editListItem(anyString(), eq(displayableListItem), any(DisplayableList.class));
 displayableListPanel.editSelectedListItem();
 verify(parentForm, times(1)).commitChanges();
 verify(listItemEditor, times(1)).editListItem(eq("Test Owner"), eq(displayableListItem), eq(displayableList));
 }

 @Test
 public void testViewSelectedListItem() {
 // Test that viewSelectedListItem() calls commitChanges() and displays an item in a Viewer
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);

 when(displayableList.getSelectedItem()).thenReturn(displayableListItem);

 doNothing().when(listItemViewer).viewListItem(any(Displayable.class));
 displayableListPanel.viewSelectedListItem();
 verify(parentForm, times(1)).commitChanges();
 verify(listItemViewer, times(1)).viewListItem(eq(displayableListItem));
 }

 @Test
 public void testDeleteSelectedListItems() {
 // Test that deleteSelectedListItems() calls commitChanges() and deletes items from the DisplayableList
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);

 doNothing().when(listItemDeleter).deleteListItems(anyString(), any(DisplayableList.class));
 displayableListPanel.deleteSelectedListItems();
 verify(parentForm, times(1)).commitChanges();
 verify(listItemDeleter, times(1)).deleteListItems(eq("TestOwner"), eq(displayableList));
 }

 @Test
 public void testSelectListItems() {
 // Test that selectListItems() calls commitChanges() and selects items in the DisplayableList
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);

 doNothing().when(listItemSelector).selectListItems(anyString(), any(DisplayableList.class));
 displayableListPanel.selectListItems();
 verify(parentForm, times(1)).commitChanges();
 verify(listItemSelector, times(1)).selectListItems(eq("Test Owner"), eq(displayableList));
 }

 @Test
 public void testUpdateButtonStates() {
 // Test that updateButtonStates() enables and disables buttons correctly based on the state of the DisplayableList and the allowWriteAccess and allowRecordEditing flags
 displayableListPanel.setDisplayableList(selectedDisplayableList);
 displayableListPanel.setIsNewRecord(true);
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.allowRecordEditing = true;

 displayableList.clear();
 displayableList.add(displayableListItem);

 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableList.clear();
 displayableList.add(displayableListItem);
 displayableList.add(displayableListItem);

 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());

 displayableListPanel.allowWriteAccess = false;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 // Test that setDisplayableItems() sets the DisplayableList and updates the button states correctly
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(displayableListItem);
 displayableListPanel.setDisplayableItems(masterList);
 verify(displayableListPanel, times(1)).setDisplayItems(masterList);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSave() {
 // Test that save() clears the masterList and sets the DisplayableList to the current items
 displayableListPanel.save();
 verify(displayableList, times(1)).getAllItems();
 }
}