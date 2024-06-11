package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import macaw.businessLayer.Displayable;
import macaw.businessLayer.DisplayableListItemEditor;
import macaw.businessLayer.DisplayableListItemSelector;
import macaw.businessLayer.DisplayableListItemAdder;
import macaw.businessLayer.DisplayableListItemDeleter;
import macaw.businessLayer.DisplayableListItemViewer;
import macaw.businessLayer.DisplayableListParentForm;
import macaw.system.Log;
import macaw.system.SessionProperties;
import macaw.util.DisplayableListPanel;
import macaw.util.UserInterfaceFactory;

public class DisplayableListPanelTest {

 @Mock
 private SessionProperties sessionProperties;

 @Mock
 private DisplayableListParentForm parentForm;

 @Mock
 private Log log;

 @Mock
 private UserInterfaceFactory userInterfaceFactory;

 @Mock
 private DisplayableListItemSelector listItemSelector;

 @Mock
 private DisplayableListItemAdder listItemAdder;

 @Mock
 private DisplayableListItemEditor listItemEditor;

 @Mock
 private DisplayableListItemDeleter listItemDeleter;

 @Mock
 private DisplayableListItemViewer listItemViewer;

 @InjectMocks
 private DisplayableListPanel displayableListPanel;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testConstructorWithAllowWriteAccessOnly() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 verify(parentForm, times(1)).commitChanges();
 }

 @Test
 public void testConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 verify(parentForm, times(1)).commitChanges();
 }

 @Test
 public void testInitWithAllowWriteAccessOnly() {
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true));
 doNothing().when(displayableListPanel).buildUI();
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, false);
 verify(sessionProperties, times(1)).getLog();
 verify(parentForm, times(1)).commitChanges();
 }

 @Test
 public void testInitWithAllowWriteAccessAndAllowRecordEditing() {
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true));
 doNothing().when(displayableListPanel).buildUI();
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);
 verify(sessionProperties, times(1)).getLog();
 verify(parentForm, times(1)).commitChanges();
 }

 @Test
 public void testBuildUI() {
 displayableListPanel.buildUI();
 verify(displayableListPanel.userInterfaceFactory, times(3)).createButton(anyString());
 }

 @Test
 public void testCreateControlButtonPanel() {
 final JPanel controlButtonPanel = displayableListPanel.createControlButtonPanel();
 assertEquals(4, controlButtonPanel.getComponentCount());
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.setDisplayableListItemAdder(mock(DisplayableListItemAdder.class));
 displayableListPanel.addListItem();
 verify(parentForm, times(1)).commitChanges();
 verify(displayableListPanel.listItemAdder, times(1)).addItems(eq("Test Owner"), any(DisplayableList.class));
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutAdder() {
 displayableListPanel.addListItem();
 fail("Should have thrown an exception");
 }

 @Test
 public void testEditSelectedListItem() {
 displayableListPanel.setDisplayableListItemEditor(mock(DisplayableListItemEditor.class));
 displayableListPanel.displayableList.add(mock(Displayable.class));
 displayableListPanel.displayableList.setSelectedItem(mock(Displayable.class));
 displayableListPanel.editSelectedListItem();
 verify(parentForm, times(1)).commitChanges();
 verify(displayableListPanel.listItemEditor, times(1)).editListItem(eq("Test Owner"), any(Displayable.class), any(DisplayableList.class));
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithoutEditor() {
 displayableListPanel.editSelectedListItem();
 fail("Should have thrown an exception");
 }

 @Test
 public void testViewSelectedListItem() {
 displayableListPanel.setDisplayableListItemViewer(mock(DisplayableListItemViewer.class));
 displayableListPanel.displayableList.add(new Displayable("Test DisplayName"));
 displayableListPanel.displayableList.setSelectedItem(new Displayable("Test DisplayName"));
 displayableListPanel.viewSelectedListItem();
 verify(displayableListPanel.listItemViewer, times(1)).viewListItem(any(Displayable.class));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithoutViewer() {
 displayableListPanel.viewSelectedListItem();
 fail("Should have thrown an exception");
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableListPanel.setDisplayableListItemDeleter(mock(DisplayableListItemDeleter.class));
 displayableListPanel.displayableList.add(mock(Displayable.class));
 displayableListPanel.displayableList.add(mock(Displayable.class));
 displayableListPanel.displayableList.setSelectedItems(Arrays.asList(mock(Displayable.class), mock(Displayable.class)));
 displayableListPanel.deleteSelectedListItems();
 verify(parentForm, times(1)).commitChanges();
 verify(displayableListPanel.listItemDeleter, times(1)).deleteListItems(eq("Test Owner"), any(DisplayableList.class));
 }

 @Test
 public void testDeleteSelectedListItemsWithoutDeleter() {
 displayableListPanel.deleteSelectedListItems();
 fail("Should have thrown an exception");
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.setDisplayableListItemSelector(mock(DisplayableListItemSelector.class));
 displayableListPanel.displayableList.add(new Displayable("Test DisplayName 1"));
 displayableListPanel.displayableList.add(new Displayable("Test DisplayName 2"));
 displayableListPanel.displayableList.add(new Displayable("Test DisplayName 3"));
 displayableListPanel.selectListItems();
 verify(displayableListPanel.listItemSelector, times(1)).selectListItems(eq("Test Owner"), any(DisplayableList.class));
 }

 @Test
 public void testUpdateButtonStatesWithAllowWriteAccess() {
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true));
 doNothing().when(displayableListPanel).updateButtonStates();
 displayableListPanel.displayableList.add(mock(Displayable.class));
 displayableListPanel.isNewRecord = false;
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.select.isEnabled());
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithAllowWriteAccessAndNoSelectedItems() {
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true));
 doNothing().when(displayableListPanel).updateButtonStates();
 displayableListPanel.displayableList.add(mock(Displayable.class));
 displayableListPanel.isNewRecord = false;
 displayableListPanel.displayableList.clearSelection();
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithAllowWriteAccessAndSelectedItem() {
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true));
 doNothing().when(displayableListPanel).updateButtonStates();
 displayableListPanel.displayableList.add(mock(Displayable.class));
 displayableListPanel.displayableList.setSelectedItem(mock(Displayable.class));
 displayableListPanel.isNewRecord = false;
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithNoWriteAccess() {
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false));
 doNothing().when(displayableListPanel).updateButtonStates();
 displayableListPanel.displayableList.add(mock(Displayable.class));
 displayableListPanel.isNewRecord = false;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithNoWriteAccessAndNoSelectedItems() {
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false));
 doNothing().when(displayableListPanel).updateButtonStates();
 displayableListPanel.displayableList.add(mock(Displayable.class));
 displayableListPanel.isNewRecord = false;
 displayableListPanel.displayableList.clearSelection();
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithNoWriteAccessAndSelectedItem() {
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false));
 doNothing().when(displayableListPanel).updateButtonStates();
 displayableListPanel.displayableList.add(mock(Displayable.class));
 displayableListPanel.displayableList.setSelectedItem(mock(Displayable.class));
 displayableListPanel.isNewRecord = false;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 List<Displayable> displayables = new ArrayList<>();
 displayables.add(new Displayable("Test DisplayName 1"));
 displayables.add(new Displayable("Test DisplayName 2"));
 displayableListPanel.setDisplayableItems(displayables);
 assertEquals(2, displayableListPanel.displayableList.size());
 }

 @Test
 public void testSave() {
 List<Displayable> displayables = Arrays.asList(new Displayable("Test DisplayName 1"), new Displayable("Test DisplayName 2"));
 displayableListPanel.displayableList.setDisplayItems(displayables);
 displayableListPanel.save();
 assertEquals(2, displayableListPanel.masterList.size());
 }

 @Test
 public void testGetAllListItems() {
 List<Displayable> displayables = Arrays.asList(new Displayable("Test DisplayName 1"), new Displayable("Test DisplayName 2"));
 displayableListPanel.displayableList.setDisplayItems(displayables);
 assertEquals(displayables, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test Owner");
 assertEquals("Test Owner", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformedWithSelectButton() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 displayableListPanel.select = mock(JButton.class);
 doNothing().when(displayableListPanel).selectListItems();
 when(displayableListPanel.select.doClick()).thenReturn(actionEvent);
 displayableListPanel.actionPerformed(actionEvent);
 verify(displayableListPanel).selectListItems();
 }

 @Test
 public void testActionPerformedWithAddButton() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 displayableListPanel.add = mock(JButton.class);
 doNothing().when(displayableListPanel).addListItem();
 when(displayableListPanel.add.doClick()).thenReturn(actionEvent);
 displayableListPanel.actionPerformed(actionEvent);
 verify(displayableListPanel).addListItem();
 }

 @Test
 public void testActionPerformedWithEditButton() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 displayableListPanel.edit = mock(JButton.class);
 doNothing().when(displayableListPanel).editSelectedListItem();
 when(displayableListPanel.edit.doClick()).thenReturn(actionEvent);
 displayableListPanel.actionPerformed(actionEvent);
 verify(displayableListPanel).editSelectedListItem();
 }

 @Test
 public void testActionPerformedWithViewButton() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 displayableListPanel.view = mock(JButton.class);
 doNothing().when(displayableListPanel).viewSelectedListItem();
 when(displayableListPanel.view.doClick()).thenReturn(actionEvent);
 displayableListPanel.actionPerformed(actionEvent);
 verify(displayableListPanel).viewSelectedListItem();
 }

 @Test
 public void testActionPerformedWithDeleteButton() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 displayableListPanel.delete = mock(JButton.class);
 doNothing().when(displayableListPanel).deleteSelectedListItems();
 when(displayableListPanel.delete.doClick()).thenReturn(actionEvent);
 displayableListPanel.actionPerformed(actionEvent);
 verify(displayableListPanel).deleteSelectedListItems();
 }
}