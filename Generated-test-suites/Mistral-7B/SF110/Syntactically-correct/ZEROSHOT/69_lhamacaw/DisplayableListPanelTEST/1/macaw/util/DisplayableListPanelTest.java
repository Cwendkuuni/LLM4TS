package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.system.SessionProperties;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 log = mock(Log.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowRecordEditingFalse() {
 assertNull(displayableListPanel.getListItemEditor());
 assertNull(displayableListPanel.getListItemDeleter());
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowRecordEditingTrue() {
 DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
 DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
 when(userInterfaceFactory.createDisplayableListItemEditor()).thenReturn(listItemEditor);
 when(userInterfaceFactory.createDisplayableListItemDeleter()).thenReturn(listItemDeleter);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 verify(userInterfaceFactory, times(1)).createDisplayableListItemEditor();
 verify(userInterfaceFactory, times(1)).createDisplayableListItemDeleter();
 assertEquals(listItemEditor, displayableListPanel.getListItemEditor());
 assertEquals(listItemDeleter, displayableListPanel.getListItemDeleter());
 }

 @Test
 public void testSetNewRecord() {
 boolean isNewRecord = true;
 displayableListPanel.setNewRecord(isNewRecord);
 verify(parentForm, times(1)).commitChanges();
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
 assertEquals(listItemSelector, displayableListPanel.getListItemSelector());
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertEquals(listItemAdder, displayableListPanel.getListItemAdder());
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertEquals(listItemEditor, displayableListPanel.getListItemEditor());
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertEquals(listItemViewer, displayableListPanel.getListItemViewer());
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertEquals(listItemDeleter, displayableListPanel.getListItemDeleter());
 }

 @Test
 public void testAddListItem() {
 DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
 when(listItemAdder.addItems("test", displayableListPanel.displayableList)).thenReturn(new ArrayList<>());
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.addListItem();
 verify(parentForm, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel.addListItem();
 fail("AddListItem should throw MacawException when listItemAdder is null");
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.editSelectedListItem();
 fail("EditSelectedListItem should throw MacawException when listItemEditor is null");
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 fail("ViewSelectedListItem should throw MacawException when listItemViewer is null");
 }

 @Test
 public void testDeleteSelectedListItems() {
 int dialogResult = JOptionPane.showConfirmDialog(null, "Warning message", "Warning title", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
 when(sessionProperties.getLog()).thenReturn(log);
 when(log.displayErrorDialog(any(MacawException.class))).thenReturn(true);
 when(displayableListPanel.parentForm.commitChanges()).thenReturn(true);
 when(displayableListPanel.displayableList.itemsAreSelected()).thenReturn(true);
 when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(mock(Displayable.class));
 DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
 when(userInterfaceFactory.createDisplayableListItemDeleter()).thenReturn(listItemDeleter);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 displayableListPanel.deleteSelectedListItems();
 verify(parentForm, times(1)).commitChanges();
 verify(listItemDeleter, times(1)).deleteListItems("test", displayableListPanel.displayableList);
 }

 @Test
 public void testSelectListItems() {
 DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
 when(userInterfaceFactory.createDisplayableListItemSelector()).thenReturn(listItemSelector);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.selectListItems();
 verify(parentForm, times(1)).commitChanges();
 }

 @Test
 public void testUpdateButtonStatesWithNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getView().isEnabled());
 assertFalse(displayableListPanel.getSelect().isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithDisplayableListEmpty() {
 when(displayableListPanel.displayableList.isEmpty()).thenReturn(true);
 assertFalse(displayableListPanel.getDelete().isEnabled());
 if (displayableListPanel.allowRecordEditing) {
 assertFalse(displayableListPanel.getEdit().isEnabled());
 } else {
 assertFalse(displayableListPanel.getView().isEnabled());
 }
 }

 @Test
 public void testUpdateButtonStatesWithDisplayableListNotEmpty() {
 when(displayableListPanel.displayableList.isEmpty()).thenReturn(false);
 assertFalse(displayableListPanel.getAdd().isEnabled());
 assertTrue(displayableListPanel.getDelete().isEnabled());
 if (displayableListPanel.allowRecordEditing) {
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertTrue(displayableListPanel.getView().isEnabled());
 } else {
 assertFalse(displayableListPanel.getView().isEnabled());
 }
 }

 @Test
 public void testUpdateButtonStatesWithAllowWriteAccessFalse() {
 assertFalse(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 if (displayableListPanel.displayableList.isEmpty()) {
 assertFalse(displayableListPanel.getView().isEnabled());
 } else if (!displayableListPanel.isNewRecord) {
 assertTrue(displayableListPanel.getView().isEnabled());
 }
 }
}