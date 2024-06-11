package macaw.util.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import macaw.businessLayer.*;
import macaw.system.*;
import macaw.util.*;
import javax.swing.*;
import org.junit.*;
import org.mockito.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private JPanel panel;
 private MockSessionProperties mockSessionProperties;
 private MockDisplayableListParentForm mockParentForm;

 @Before
 public void setup() {
 mockSessionProperties = mock(SessionProperties.class);
 mockParentForm = mock(DisplayableListParentForm.class);
 sessionProperties = mockSessionProperties;
 parentForm = mockParentForm;
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", false);
 }

 @Test
 public void testInit() {
 assertEquals("Test Panel Title", displayableListPanel.getPanelTitleText());
 assertNull(displayableListPanel.getListItemSelector());
 assertNull(displayableListPanel.getListItemAdder());
 assertNull(displayableListPanel.getListItemEditor());
 assertNull(displayableListPanel.getListItemViewer());
 assertNull(displayableListPanel.getListItemDeleter());
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testAddListItem() {
 when(parentForm.commitChanges()).thenReturn(true);
 DisplayableListItemAdder mockListItemAdder = mock(DisplayableListItemAdder.class);
 DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.getListScrollPane()).thenReturn(new JScrollPane());
 displayableListPanel.setDisplayableListItemAdder(mockListItemAdder);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.addListItem();
 verify(mockListItemAdder, times(1)).addItems("Test Owner Name", displayableList);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 when(parentForm.commitChanges()).thenReturn(true);
 displayableListPanel.addListItem();
 fail("Should have thrown a MacawException for missing DisplayableListItemAdder");
 }

 @Test
 public void testEditSelectedListItem() {
 when(parentForm.commitChanges()).thenReturn(true);
 DisplayableListItemEditor mockListItemEditor = mock(DisplayableListItemEditor.class);
 Displayable mockDisplayable = mock(Displayable.class);
 DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.getSelectedItem()).thenReturn(mockDisplayable);
 when(mockDisplayable.getDisplayName()).thenReturn("Old Name");
 when(displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.setDisplayableListItemEditor(mockListItemEditor);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.editSelectedListItem();
 verify(mockListItemEditor, times(1)).editListItem("Test Owner Name", mockDisplayable, displayableList);
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 when(parentForm.commitChanges()).thenReturn(true);
 when(displayableListPanel.displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.editSelectedListItem();
 fail("Should have thrown a MacawException for missing DisplayableListItemEditor");
 }

 @Test
 public void testViewSelectedListItem() {
 when(parentForm.commitChanges()).thenReturn(true);
 DisplayableListItemViewer mockListItemViewer = mock(DisplayableListItemViewer.class);
 Displayable mockDisplayable = mock(Displayable.class);
 displayableListPanel.setDisplayableListItemViewer(mockListItemViewer);
 displayableListPanel.setDisplayableList(new DisplayableList(null));
 displayableListPanel.viewSelectedListItem();
 verify(mockListItemViewer, times(1)).viewListItem(mockDisplayable);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 when(parentForm.commitChanges()).thenReturn(true);
 when(displayableListPanel.displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.viewSelectedListItem();
 fail("Should have thrown a MacawException for missing DisplayableListItemViewer");
 }

 @Test
 public void testDeleteSelectedListItems() {
 when(parentForm.commitChanges()).thenReturn(true);
 DisplayableListItemDeleter mockListItemDeleter = mock(DisplayableListItemDeleter.class);
 DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.setDisplayableListItemDeleter(mockListItemDeleter);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.deleteSelectedListItems();
 verify(mockListItemDeleter, times(1)).deleteListItems("Test Owner Name", displayableList);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 when(parentForm.commitChanges()).thenReturn(true);
 when(displayableListPanel.displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.deleteSelectedListItems();
 fail("Should have thrown a MacawException for missing DisplayableListItemDeleter");
 }

 @Test
 public void testSelectListItems() {
 when(parentForm.commitChanges()).thenReturn(true);
 DisplayableListItemSelector mockListItemSelector = mock(DisplayableListItemSelector.class);
 DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.setDisplayItems(anyList())).thenReturn(displayableList);
 displayableListPanel.setDisplayableListItemSelector(mockListItemSelector);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.selectListItems();
 verify(mockListItemSelector, times(1)).selectListItems("Test Owner Name", displayableList);
 }

 @Test
 public void testUpdateButtonStates() {
 DisplayableListItemAdder mockListItemAdder = mock(DisplayableListItemAdder.class);
 DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.isEmpty()).thenReturn(false);
 when(displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.setDisplayableListItemAdder(mockListItemAdder);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.setAllowWriteAccess(true);
 displayableListPanel.setAllowRecordEditing(true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithNoWriteAccess() {
 DisplayableListItemAdder mockListItemAdder = mock(DisplayableListItemAdder.class);
 DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.isEmpty()).thenReturn(false);
 when(displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.setDisplayableListItemAdder(mockListItemAdder);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.setAllowWriteAccess(false);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithEmptyList() {
 DisplayableListItemAdder mockListItemAdder = mock(DisplayableListItemAdder.class);
 DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.isEmpty()).thenReturn(true);
 displayableListPanel.setDisplayableListItemAdder(mockListItemAdder);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithNoRecordEditing() {
 DisplayableListItemAdder mockListItemAdder = mock(DisplayableListItemAdder.class);
 DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.isEmpty()).thenReturn(false);
 displayableListPanel.setDisplayableListItemAdder(mockListItemAdder);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.setAllowWriteAccess(true);
 displayableListPanel.setAllowRecordEditing(false);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 }

 @After
 public void tearDown() {
 reset(mockSessionProperties);
 reset(mockParentForm);
 }
}