package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import macaw.businessLayer.*;
import org.junit.*;
import org.mockito.*;

import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

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
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 }

 @Test
 public void testDisplayableListPanelConstruction() {
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelWithAllowRecordEditing() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertNotNull(displayableListPanel.getSelectButton());
 assertNotNull(displayableListPanel.getAddButton());
 assertNotNull(displayableListPanel.getEditButton());
 assertNotNull(displayableListPanel.getViewButton());
 assertNotNull(displayableListPanel.getDeleteButton());
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertEquals(true, displayableListPanel.isNewRecord);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector mockSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockSelector);
 verify(mockSelector, times(1)).init(displayableListPanel.getDisplayableList(), displayableListPanel.sessionProperties);
 }

 @Test
 public void testAddListItem() {
 doNothing().when(parentForm).commitChanges();
 displayableListPanel.addListItem();
 verify(displayableListPanel.listItemAdder, times(1)).addItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel.addListItem();
 fail("No MacawException was thrown");
 }

 @Test
 public void testEditSelectedListItem() {
 doNothing().when(parentForm).commitChanges();
 Displayable mockDisplayable = mock(Displayable.class);
 when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(mockDisplayable);
 displayableListPanel.editSelectedListItem();
 verify(displayableListPanel.listItemEditor, times(1)).editListItem(displayableListPanel.listOwnerName, mockDisplayable, displayableListPanel.displayableList);
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 doNothing().when(parentForm).commitChanges();
 displayableListPanel.editSelectedListItem();
 fail("No MacawException was thrown");
 }

 @Test
 public void testViewSelectedListItem() {
 doNothing().when(parentForm).commitChanges();
 Displayable mockDisplayable = mock(Displayable.class);
 when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(mockDisplayable);
 displayableListPanel.viewSelectedListItem();
 verify(displayableListPanel.listItemViewer, times(1)).viewListItem(mockDisplayable);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 doNothing().when(parentForm).commitChanges();
 displayableListPanel.viewSelectedListItem();
 fail("No MacawException was thrown");
 }

 @Test
 public void testDeleteSelectedListItems() {
 doNothing().when(parentForm).commitChanges();
 JOptionPane mockOptionPane = mock(JOptionPane.class);
 when(parentForm.getOptionPane()).thenReturn(mockOptionPane);
 int result = mockOptionPane.showConfirmDialog(eq(null), eq("general.warning.deletionIsPermanent"), eq("general.warning.title"), eq(0), eq(2));
 when(mockOptionPane.showConfirmDialog(eq(null), anyString(), anyString(), anyInt(), anyInt())).thenReturn(result);
 displayableListPanel.deleteSelectedListItems();
 verify(displayableListPanel.listItemDeleter, times(1)).deleteListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter() {
 doNothing().when(parentForm).commitChanges();
 int result = 0;
 when(parentForm.getOptionPane()).thenReturn(mock(JOptionPane.class));
 displayableListPanel.deleteSelectedListItems();
 fail("No MacawException was thrown");
 }

 @Test
 public void testSelectListItems() {
 doNothing().when(parentForm).commitChanges();
 DisplayableListItemSelector mockSelector = mock(DisplayableListItemSelector.class);
 when(displayableListPanel.listItemSelector).thenReturn(mockSelector);
 displayableListPanel.selectListItems();
 verify(mockSelector, times(1)).selectListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }

 @Test
 public void testUpdateButtonStates() {
 assertTrue(displayableListPanel.isNewRecord);
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 displayableListPanel.isNewRecord = false;
 when(displayableListPanel.displayableList.isEmpty()).thenReturn(false);
 when(displayableListPanel.displayableList.itemsAreSelected()).thenReturn(true);
 when(displayableListPanel.allowRecordEditing).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithEmptyList() {
 when(displayableListPanel.displayableList.isEmpty()).thenReturn(true);
 when(displayableListPanel.allowRecordEditing).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithNoWriteAccess() {
 when(displayableListPanel.displayableList.isEmpty()).thenReturn(false);
 when(displayableListPanel.displayableList.itemsAreSelected()).thenReturn(true);
 when(displayableListPanel.allowWriteAccess).thenReturn(false);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 }
}