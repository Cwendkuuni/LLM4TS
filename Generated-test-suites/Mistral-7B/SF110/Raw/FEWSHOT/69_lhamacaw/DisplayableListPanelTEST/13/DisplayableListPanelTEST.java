package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import macaw.businessLayer.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private DisplayableListItemSelector listItemSelectorMock;
 private DisplayableListItemAdder listItemAdderMock;
 private DisplayableListItemEditor listItemEditorMock;
 private DisplayableListItemViewer listItemViewerMock;
 private DisplayableListItemDeleter listItemDeleterMock;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 listItemSelectorMock = mock(DisplayableListItemSelector.class);
 listItemAdderMock = mock(DisplayableListItemAdder.class);
 listItemEditorMock = mock(DisplayableListItemEditor.class);
 listItemViewerMock = mock(DisplayableListItemViewer.class);
 listItemDeleterMock = mock(DisplayableListItemDeleter.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", false);
 displayableListPanel.setDisplayableListItemSelector(listItemSelectorMock);
 displayableListPanel.setDisplayableListItemAdder(listItemAdderMock);
 displayableListPanel.setDisplayableListItemEditor(listItemEditorMock);
 displayableListPanel.setDisplayableListItemViewer(listItemViewerMock);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleterMock);
 }

 @Test
 public void testDisplayableListPanelCreation() {
 assertEquals("Test Panel Title", displayableListPanel.getPanelTitleText());
 assertNull(displayableListPanel.getPanel());
 }

 @Test
 public void testConstructorWithAllowRecordEditing() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", false, true);
 assertTrue(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord);
 displayableListPanel.updateButtonStates();
 verify(listItemAdderMock, never()).addItems(eq("Test Owner Name"), anyList());
 }

 @Test
 public void testSetNewRecordFalse() {
 displayableListPanel.setNewRecord(false);
 assertFalse(displayableListPanel.isNewRecord);
 }

 @Test
 public void testGetPanel() {
 JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertEquals(listItemSelectorMock, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertEquals(listItemAdderMock, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertEquals(listItemEditorMock, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertEquals(listItemViewerMock, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertEquals(listItemDeleterMock, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 verify(parentForm).commitChanges();
 verify(listItemAdderMock).addItems(eq("Test Owner Name"), anyList());
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 when(listItemViewerMock.viewListItem(any(Displayable.class))).thenReturn(selectedListItem);
 displayableListPanel.editSelectedListItem();
 verify(parentForm).commitChanges();
 verify(listItemEditorMock).editListItem(eq("Test Owner Name"), eq(selectedListItem), anyList());
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(selectedListItem);
 when(listItemViewerMock.viewListItem(any(Displayable.class))).thenReturn(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 verify(listItemViewerMock).viewListItem(eq(selectedListItem));
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableListPanel.deleteSelectedListItems();
 verify(parentForm).commitChanges();
 verify(listItemDeleterMock).deleteListItems(eq("Test Owner Name"), anyList());
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();
 verify(listItemSelectorMock).selectListItems(eq("Test Owner Name"), anyList());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemException() {
 doThrow(new MacawException(MacawErrorType.MISSING_DISPLAY_LIST_ADDER, "Test Exception")).when(listItemAdderMock).addItems(anyString(), anyList());
 displayableListPanel.addListItem();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemException() {
 doThrow(new MacawException(MacawErrorType.MISSING_DISPLAY_LIST_EDITOR, "Test Exception")).when(listItemEditorMock).editListItem(anyString(), any(Displayable.class), anyList());
 displayableListPanel.editSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemException() {
 doThrow(new MacawException(MacawErrorType.MISSING_DISPLAY_LIST_SELECTOR, "Test Exception")).when(listItemViewerMock).viewListItem(any(Displayable.class));
 displayableListPanel.viewSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsException() {
 doThrow(new MacawException(MacawErrorType.MISSING_DISPLAY_LIST_DELETER, "Test Exception")).when(listItemDeleterMock).deleteListItems(anyString(), anyList());
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItemsException() {
 doThrow(new MacawException(MacawErrorType.MISSING_DISPLAY_LIST_SELECTOR, "Test Exception")).when(listItemSelectorMock).selectListItems(anyString(), anyList());
 displayableListPanel.selectListItems();
 }
}