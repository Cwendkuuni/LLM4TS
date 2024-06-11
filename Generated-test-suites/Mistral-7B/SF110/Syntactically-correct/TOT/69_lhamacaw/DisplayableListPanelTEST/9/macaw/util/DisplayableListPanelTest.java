package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import macaw.businessLayer.*;
import macaw.system.*;
import macaw.util.*;
import org.junit.*;
import org.mockito.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemViewer listItemViewer;
 private DisplayableListItemDeleter listItemDeleter;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 log = mock(Log.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", false, false);
 }

 @Test
 public void testInit() {
 // Test that init method initializes all instance variables
 assertNotNull(displayableListPanel.sessionProperties);
 assertNotNull(displayableListPanel.log);
 assertNotNull(displayableListPanel.parentForm);
 assertNotNull(displayableListPanel.panelTitleText);
 assertNotNull(displayableListPanel.currentUserID);
 assertNotNull(displayableListPanel.userInterfaceFactory);
 assertNull(displayableListPanel.masterList);
 assertNull(displayableListPanel.displayableList);
 assertNull(displayableListPanel.select);
 assertNull(displayableListPanel.add);
 assertNull(displayableListPanel.edit);
 assertNull(displayableListPanel.view);
 assertNull(displayableListPanel.delete);
 assertNull(displayableListPanel.listItemSelector);
 assertNull(displayableListPanel.listItemAdder);
 assertNull(displayableListPanel.listItemEditor);
 assertNull(displayableListPanel.listItemViewer);
 assertNull(displayableListPanel.listItemDeleter);
 assertNull(displayableListPanel.listOwnerName);
 assertFalse(displayableListPanel.allowWriteAccess);
 assertFalse(displayableListPanel.allowRecordEditing);
 assertNull(displayableListPanel.masterSourceVariableCollection);
 assertNull(displayableListPanel.panel);
 }

 @Test
 public void testBuildUI() {
 // Test that buildUI method creates and sets up the JPanel
 doReturn(mock(JPanel.class)).when(userInterfaceFactory).createPanel();

 displayableListPanel.buildUI();

 verify(userInterfaceFactory, times(1)).createLabel(displayableListPanel.panelTitleText);
 verify(userInterfaceFactory, times(1)).createButton(MacawMessages.getMessage("general.buttons.select"));
 // ... repeat for all other buttons
 }

 @Test
 public void testSetNewRecordFalse() {
 // Test that setNewRecord method sets button states correctly when isNewRecord is false
 displayableListPanel.setNewRecord(false);

 verify(displayableListPanel.add, times(1)).setEnabled(true);
 verify(displayableListPanel.select, times(1)).setEnabled(true);
 verify(displayableListPanel.delete, times(1)).setEnabled(true);
 verify(displayableListPanel.edit, times(1)).setEnabled(true);
 verify(displayableListPanel.view, times(1)).setEnabled(true);
 }

 @Test
 public void testSetNewRecordTrue() {
 // Test that setNewRecord method sets button states correctly when isNewRecord is true
 displayableListPanel.setNewRecord(true);

 verify(displayableListPanel.add, times(1)).setEnabled(false);
 verify(displayableListPanel.select, times(1)).setEnabled(false);
 verify(displayableListPanel.delete, times(1)).setEnabled(false);
 verify(displayableListPanel.edit, times(1)).setEnabled(false);
 verify(displayableListPanel.view, times(1)).setEnabled(false);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 // Test that setDisplayableListItemSelector method sets the listItemSelector instance variable
 DisplayableListItemSelector mockListItemSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockListItemSelector);
 assertSame(mockListItemSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 // Test that setDisplayableListItemAdder method sets the listItemAdder instance variable
 DisplayableListItemAdder mockListItemAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(mockListItemAdder);
 assertSame(mockListItemAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 // Test that setDisplayableListItemEditor method sets the listItemEditor instance variable
 DisplayableListItemEditor mockListItemEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(mockListItemEditor);
 assertSame(mockListItemEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 // Test that setDisplayableListItemViewer method sets the listItemViewer instance variable
 DisplayableListItemViewer mockListItemViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(mockListItemViewer);
 assertSame(mockListItemViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 // Test that setDisplayableListItemDeleter method sets the listItemDeleter instance variable
 DisplayableListItemDeleter mockListItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(mockListItemDeleter);
 assertSame(mockListItemDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 // Test that addListItem method calls the addItems method on the listItemAdder and updates button states
 displayableListPanel.addListItem();

 verify(displayableListPanel.listItemAdder, times(1)).addItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 verify(displayableListPanel, times(1)).updateButtonStates();
 }

 @Test
 public void testEditSelectedListItem() {
 // Test that editSelectedListItem method calls the editListItem method on the listItemEditor and updates button states
 Displayable mockDisplayable = mock(Displayable.class);
 displayableListPanel.displayableList.add(mockDisplayable);
 displayableListPanel.displayableList.setSelectedItem(mockDisplayable);

 doReturn(mockDisplayable).when(displayableListPanel.displayableList).getSelectedItem();
 doReturn(mock(String.class)).when(displayableListPanel).listOwnerName;

 displayableListPanel.editSelectedListItem();

 verify(displayableListPanel.listItemEditor, times(1)).editListItem(displayableListPanel.listOwnerName, mockDisplayable, displayableListPanel.displayableList);
 verify(displayableListPanel, times(1)).updateButtonStates();
 }

 @Test
 public void testViewSelectedListItem() {
 // Test that viewSelectedListItem method calls the viewListItem method on the listItemViewer
 Displayable mockDisplayable = mock(Displayable.class);
 displayableListPanel.displayableList.add(mockDisplayable);
 displayableListPanel.displayableList.setSelectedItem(mockDisplayable);

 doReturn(mockDisplayable).when(displayableListPanel.displayableList).getSelectedItem();

 displayableListPanel.viewSelectedListItem();

 verify(displayableListPanel.listItemViewer, times(1)).viewListItem(mockDisplayable);
 }

 @Test
 public void testDeleteSelectedListItems() {
 // Test that deleteSelectedListItems method calls the deleteListItems method on the listItemDeleter and updates button states
 final List<Displayable> itemsToDelete = Arrays.asList(new Displayable[3]);
 displayableListPanel.displayableList.setItems(itemsToDelete);

 doReturn(itemsToDelete).when(displayableListPanel.displayableList).getAllItems();
 doReturn("OwnerName").when(displayableListPanel).listOwnerName;
 doReturn(true).when(displayableListPanel).allowWriteAccess;
 doReturn(true).when(displayableListPanel).allowRecordEditing;
 doReturn(1).when(displayableListPanel).displayableList.getSelectedIndex();
 doReturn(itemsToDelete.get(0)).when(displayableListPanel.displayableList).getSelectedItem();

 displayableListPanel.deleteSelectedListItems();

 verify(displayableListPanel.listItemDeleter, times(1)).deleteListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 verify(displayableListPanel, times(1)).updateButtonStates();
 }

 @Test
 public void testSelectListItems() {
 // Test that selectListItems method calls the selectListItems method on the listItemSelector and updates button states
 final List<Displayable> itemsToSelect = Arrays.asList(new Displayable[3]);
 displayableListPanel.displayableList.setItems(itemsToSelect);

 doReturn(itemsToSelect).when(displayableListPanel.displayableList).getAllItems();
 doReturn("OwnerName").when(displayableListPanel).listOwnerName;
 doReturn(true).when(displayableListPanel).allowWriteAccess;

 displayableListPanel.selectListItems();

 verify(displayableListPanel.listItemSelector, times(1)).selectListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 verify(displayableListPanel, times(1)).updateButtonStates();
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.addListItem();

 fail("Should have thrown MacawException for missing listItemAdder");
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.allowRecordEditing = true;
 Displayable mockDisplayable = mock(Displayable.class);
 displayableListPanel.displayableList.add(mockDisplayable);
 displayableListPanel.displayableList.setSelectedItem(mockDisplayable);

 doReturn(mockDisplayable).when(displayableListPanel.displayableList).getSelectedItem();
 doReturn("OwnerName").when(displayableListPanel).listOwnerName;

 displayableListPanel.editSelectedListItem();

 fail("Should have thrown MacawException for missing listItemEditor");
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.allowRecordEditing = false;
 Displayable mockDisplayable = mock(Displayable.class);
 displayableListPanel.displayableList.add(mockDisplayable);
 displayableListPanel.displayableList.setSelectedItem(mockDisplayable);

 doReturn(mockDisplayable).when(displayableListPanel.displayableList).getSelectedItem();
 doReturn("OwnerName").when(displayableListPanel).listOwnerName;

 displayableListPanel.viewSelectedListItem();

 fail("Should have thrown MacawException for missing listItemViewer");
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter() {
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.allowRecordEditing = true;
 final List<Displayable> itemsToDelete = Arrays.asList(new Displayable[3]);
 displayableListPanel.displayableList.setItems(itemsToDelete);

 doReturn(itemsToDelete).when(displayableListPanel.displayableList).getAllItems();
 doReturn("OwnerName").when(displayableListPanel).listOwnerName;

 displayableListPanel.deleteSelectedListItems();

 fail("Should have thrown MacawException for missing listItemDeleter");
 }
}