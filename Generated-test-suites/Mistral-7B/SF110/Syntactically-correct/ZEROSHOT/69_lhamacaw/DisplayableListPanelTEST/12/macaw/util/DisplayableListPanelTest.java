package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 log = mock(Log.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 }

 @Test
 public void testInitializeComponents() {
 // Check that all components have been initialized correctly
 assertNotNull(displayableListPanel.getSelectButton());
 assertNotNull(displayableListPanel.getAddButton());
 assertNotNull(displayableListPanel.getEditButton());
 assertNotNull(displayableListPanel.getViewButton());
 assertNotNull(displayableListPanel.getDeleteButton());
 }

 @Test
 public void testBuildUI() {
 // Check that UI is built correctly
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetNewRecord() {
 // Test setting new record and updating button states
 displayableListPanel.setNewRecord(true);
 displayableListPanel.updateButtonStates();

 // Check that buttons are disabled
 assertFalse(displayableListPanel.getAddButton().isEnabled());
 assertFalse(displayableListPanel.getDeleteButton().isEnabled());
 assertFalse(displayableListPanel.getEditButton().isEnabled());
 assertFalse(displayableListPanel.getViewButton().isEnabled());
 assertFalse(displayableListPanel.getSelectButton().isEnabled());
 }

 @Test
 public void testAddListItem() {
 // Mock the DisplayableListItemAdder to return an empty list
 DisplayableListItemAdder displayableListItemAdder = mock(DisplayableListItemAdder.class);
 when(displayableListItemAdder.addItems(displayableListPanel.getListOwnerName(), displayableListPanel.getDisplayableList())).thenReturn(Collections.emptyList());

 displayableListPanel.setDisplayableListItemAdder(displayableListItemAdder);
 displayableListPanel.addListItem();

 // Check that no error message is displayed
 verifyNoMoreInteractions(log);

 // Check that buttons are updated correctly
 assertTrue(displayableListPanel.getAddButton().isEnabled());
 assertFalse(displayableListPanel.getDeleteButton().isEnabled());
 assertFalse(displayableListPanel.getEditButton().isEnabled());
 assertFalse(displayableListPanel.getViewButton().isEnabled());
 assertFalse(displayableListPanel.getSelectButton().isEnabled());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 // Test adding a list item without an adder and expect an error message
 displayableListPanel.addListItem();

 // Check that an error message is displayed
 verify(log).displayErrorDialog(eq(MacawErrorType.MISSING_DISPLAY_LIST_ADDER));
 }

 @Test
 public void testEditSelectedListItem() {
 // Mock the DisplayableListItemEditor to return an empty list
 DisplayableListItemEditor displayableListItemEditor = mock(DisplayableListItemEditor.class);
 when(displayableListItemEditor.editListItem(displayableListPanel.getListOwnerName(), any(Displayable.class), eq(displayableListPanel.getDisplayableList()))).thenReturn(new Displayable());

 displayableListPanel.setDisplayableListItemEditor(displayableListItemEditor);
 displayableListPanel.editSelectedListItem();

 // Check that no error message is displayed
 verifyNoMoreInteractions(log);

 // Check that buttons are updated correctly
 assertTrue(displayableListPanel.getAddButton().isEnabled());
 assertFalse(displayableListPanel.getDeleteButton().isEnabled());
 assertFalse(displayableListPanel.getEditButton().isEnabled());
 assertFalse(displayableListPanel.getViewButton().isEnabled());
 assertFalse(displayableListPanel.getSelectButton().isEnabled());
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 // Test editing a list item without an editor and expect an error message
 displayableListPanel.editSelectedListItem();

 // Check that an error message is displayed
 verify(log).displayErrorDialog(eq(MacawErrorType.MISSING_DISPLAY_LIST_EDITOR));
 }

 @Test public void testViewSelectedListItem() {
 // Mock the DisplayableListItemViewer to do nothing
 DisplayableListItemViewer displayableListItemViewer = mock(DisplayableListItemViewer.class);
 when(displayableListItemViewer.viewListItem(any(Displayable.class))).thenReturn(null);

 displayableListPanel.setDisplayableListItemViewer(displayableListItemViewer);
 displayableListPanel.viewSelectedListItem();

 // Check that no error message is displayed
 verifyNoMoreInteractions(log);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 // Test viewing a list item without a viewer and expect an error message
 displayableListPanel.viewSelectedListItem();

 // Check that an error message is displayed
 verify(log).displayErrorDialog(eq(MacawErrorType.MISSING_DISPLAY_LIST_SELECTOR));
 }

 @Test public void testDeleteSelectedListItems() {
 // Mock the DisplayableListItemDeleter to return an empty list
 DisplayableListItemDeleter displayableListItemDeleter = mock(DisplayableListItemDeleter.class);
 when(displayableListItemDeleter.deleteListItems(displayableListPanel.getListOwnerName(), displayableListPanel.getDisplayableList())).thenReturn(Collections.emptyList());

 displayableListPanel.setDisplayableListItemDeleter(displayableListItemDeleter);
 displayableListPanel.deleteSelectedListItems();

 // Check that no error message is displayed
 verifyNoMoreInteractions(log);

 // Check that buttons are updated correctly
 assertTrue(displayableListPanel.getAddButton().isEnabled());
 assertFalse(displayableListPanel.getDeleteButton().isEnabled());
 assertFalse(displayableListPanel.getEditButton().isEnabled());
 assertFalse(displayableListPanel.getViewButton().isEnabled());
 assertFalse(displayableListPanel.getSelectButton().isEnabled());
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 // Test deleting list items without a deleter and expect an error message
 displayableListPanel.deleteSelectedListItems();

 // Check that an error message is displayed
 verify(log).displayErrorDialog(eq(MacawErrorType.MISSING_DISPLAY_LIST_DELETER));
 }

 @Test public void testSelectListItems() {
 // Mock the DisplayableListItemSelector to select all items in the list
 DisplayableListItemSelector displayableListItemSelector = mock(DisplayableListItemSelector.class);
 when(displayableListItemSelector.selectListItems(displayableListPanel.getListOwnerName(), displayableListPanel.getDisplayableList())).thenReturn(true);

 displayableListPanel.setDisplayableListItemSelector(displayableListItemSelector);
 displayableListPanel.selectListItems();

 // Check that no error message is displayed
 verifyNoMoreInteractions(log);

 // Check that buttons are updated correctly
 assertTrue(displayableListPanel.getAddButton().isEnabled());
 assertFalse(displayableListPanel.getDeleteButton().isEnabled());
 assertFalse(displayableListPanel.getEditButton().isEnabled());
 assertFalse(displayableListPanel.getViewButton().isEnabled());
 assertFalse(displayableListPanel.getSelectButton().isEnabled());
 }

 @Test public void testUpdateButtonStates() {
 // Test updating button states with no selected items
 displayableListPanel.setDisplayableList(new DisplayableList(userInterfaceFactory));
 displayableListPanel.updateButtonStates();

 // Check that buttons are disabled
 assertFalse(displayableListPanel.getAddButton().isEnabled());
 assertFalse(displayableListPanel.getDeleteButton().isEnabled());
 assertFalse(displayableListPanel.getEditButton().isEnabled());
 assertFalse(displayableListPanel.getViewButton().isEnabled());
 assertFalse(displayableListPanel.getSelectButton().isEnabled());

 // Test updating button states with selected items
 Displayable displayable = new Displayable();
 displayableListPanel.getDisplayableList().add(displayable);
 displayableListPanel.getDisplayableList().setSelectedItem(displayable);
 displayableListPanel.updateButtonStates();

 // Check that buttons are enabled
 assertTrue(displayableListPanel.getAddButton().isEnabled());
 assertFalse(displayableListPanel.getDeleteButton().isEnabled());
 assertFalse(displayableListPanel.getEditButton().isEnabled());
 assertFalse(displayableListPanel.getViewButton().isEnabled());
 assertFalse(displayableListPanel.getSelectButton().isEnabled());
 }
}