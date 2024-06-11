package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import macaw.businessLayer.User;
import macaw.system.Log;
import macaw.system.SessionProperties;
import macaw.util.Displayable;
import macaw.util.DisplayableList;
import macaw.util.DisplayableListItemAdder;
import macaw.util.DisplayableListItemEditor;
import macaw.util.DisplayableListItemDeleter;
import macaw.util.DisplayableListItemSelector;
import macaw.util.DisplayableListItemViewer;
import macaw.util.DisplayableListPanel;
import macaw.util.MacawException;

import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest {

 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private DisplayableListPanel displayableListPanel;
 private DisplayableList list;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemDeleter listItemDeleter;
 private DisplayableListItemViewer listItemViewer;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 list = new DisplayableList(new DisplayableListFactory());
 displayableListPanel.setDisplayableList(list);
 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemSelector = mock(DisplayableListItemSelector.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);
 listItemViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 }

 @Test
 public void testInit() {
 assertNotNull(displayableListPanel);
 assertEquals("Displayable List Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 assertNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isAllowWriteAccess());
 assertFalse(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testSetNewRecordFalse() {
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testGetPanel() {
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector selector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(selector);
 assertEquals(selector, displayableListPanel.getDisplayableListItemSelector());
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder adder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(adder);
 assertEquals(adder, displayableListPanel.getDisplayableListItemAdder());
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor editor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(editor);
 assertEquals(editor, displayableListPanel.getDisplayableListItemEditor());
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer viewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(viewer);
 assertEquals(viewer, displayableListPanel.getDisplayableListItemViewer());
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter deleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(deleter);
 assertEquals(deleter, displayableListPanel.getDisplayableListItemDeleter());
 }

 @Test
 public void testAddListItem() {
 when(listItemAdder.addItems(eq("List Owner"), eq(list))).thenReturn(Arrays.asList(new Displayable("Item 1"), new Displayable("Item 2")));
 displayableListPanel.addListItem();
 assertFalse(displayableListPanel.isNewRecord());
 assertEquals(2, list.getSize());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemException() {
 when(listItemAdder.addItems(eq("List Owner"), eq(list))).thenThrow(new MacawException("Add Failed"));
 displayableListPanel.addListItem();
 fail("Adding an item should have thrown an exception");
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedItem = mock(Displayable.class);
 when(list.getSelectedItem()).thenReturn(selectedItem);
 when(listItemEditor.editListItem(eq("List Owner"), eq(selectedItem), eq(list)))
 .thenReturn(new Displayable("Edited Item"));
 displayableListPanel.editSelectedListItem();
 assertEquals("Edited Item", list.getSelectedItem().getDisplayName());
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemException() {
 when(list.getSelectedItem()).thenReturn(null);
 displayableListPanel.editSelectedListItem();
 fail("Editing an item should have thrown an exception");
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedItem = mock(Displayable.class);
 when(list.getSelectedItem()).thenReturn(selectedItem);
 when(listItemViewer.viewListItem(eq(selectedItem))).thenCallRealMethod();
 displayableListPanel.viewSelectedListItem();
 verify(listItemViewer, times(1)).viewListItem(selectedItem);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemException() {
 when(list.getSelectedItem()).thenReturn(null);
 displayableListPanel.viewSelectedListItem();
 fail("Viewing an item should have thrown an exception");
 }

 @Test
 public void testDeleteSelectedListItems() {
 when(list.itemsAreSelected()).thenReturn(true);
 when(listItemDeleter.deleteListItems(eq("List Owner"), eq(list))).thenCallRealMethod();
 displayableListPanel.deleteSelectedListItems();
 verify(listItemDeleter, times(1)).deleteListItems(eq("List Owner"), eq(list));
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsException() {
 when(list.itemsAreSelected()).thenReturn(false);
 displayableListPanel.deleteSelectedListItems();
 fail("Deleting items should have thrown an exception");
 }

 @Test
 public void testSelectListItems() {
 when(listItemSelector.selectListItems(eq("List Owner"), eq(list))).thenCallRealMethod();
 displayableListPanel.selectListItems();
 verify(listItemSelector, times(1)).selectListItems(eq("List Owner"), eq(list));
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsException() {
 when(listItemSelector.selectListItems(eq("List Owner"), eq(list))).thenThrow(new MacawException("Select Failed"));
 displayableListPanel.selectListItems();
 fail("Selecting items should have thrown an exception");
 }

 @Test
 public void testUpdateButtonStates() {
 List<Displayable> items = new ArrayList<Displayable>();
 items.add(mock(Displayable.class));
 list.setDisplayItems(items);
 when(list.isEmpty()).thenReturn(false);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.isAddEnabled());
 assertTrue(displayableListPanel.isDeleteEnabled());
 assertTrue(displayableListPanel.isEditEnabled());
 assertTrue(displayableListPanel.isViewEnabled());
 assertTrue(displayableListPanel.isSelectEnabled());
 }

 @Test
 public void testUpdateButtonStatesEmpty() {
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());
 }

 @Test
 public void testUpdateButtonStatesNewRecord() {
 displayableListPanel.setNewRecord(true);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());
 }

}