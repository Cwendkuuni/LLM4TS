package macaw.util;

import static org.junit.Assert.*;
import org.junit.Test;
import static macaw.util.MacawMessages.getMessage;
import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemViewer listItemViewer;
 private DisplayableListItemDeleter listItemDeleter;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 parentForm = mock(DisplayableListParentForm.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 listItemSelector = mock(DisplayableListItemSelector.class);
 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemViewer = mock(DisplayableListItemViewer.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 }

 @Test
 public void testInit() {
 assertNotNull(displayableListPanel.getSessionProperties());
 assertNotNull(displayableListPanel.getParentForm());
 assertNull(displayableListPanel.getPanel());
 assertNull(displayableListPanel.getDisplayableList());
 assertNull(displayableListPanel.getSelect());
 assertNull(displayableListPanel.getAdd());
 assertNull(displayableListPanel.getEdit());
 assertNull(displayableListPanel.getView());
 assertNull(displayableListPanel.getDelete());
 }

 @Test
 public void testBuildUI() {
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testCreateControlButtonPanel() {
 JPanel controlButtonPanel = displayableListPanel.createControlButtonPanel();
 assertNotNull(controlButtonPanel);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getView().isEnabled());
 assertFalse(displayableListPanel.getSelect().isEnabled());
 }

 @Test
 public void testSetDisplayableList() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 DisplayableList displayableList = new DisplayableList(displayableListPanel.getUserInterfaceFactory());
 displayableList.setDisplayItems(masterList);
 displayableListPanel.setDisplayableList(displayableList);
 assertEquals(displayableList, displayableListPanel.getDisplayableList());
 }

 @Test
 public void testAddListItem() {
 assertThrows(MacawException.class, displayableListPanel::addListItem);
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 when(listItemEditor.editListItem(anyString(), any(Displayable.class), any(DisplayableList.class)))
 .thenReturn(selectedListItem);
 when(displayableListPanel.getDisplayableList().getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.editSelectedListItem();
 verify(listItemEditor).editListItem(eq(""), argThat((Displayable d) -> d.equals(selectedListItem)), any(DisplayableList.class));
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 when(listItemViewer.viewListItem(any(Displayable.class))).thenReturn(true);
 when(displayableListPanel.getDisplayableList().getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 verify(listItemViewer).viewListItem(eq(selectedListItem));
 }

 @Test
 public void testDeleteSelectedListItems() {
 when(displayableListPanel.getDisplayableList().itemsAreSelected()).thenReturn(true);
 displayableListPanel.deleteSelectedListItems();
 verify(listItemDeleter).deleteListItems(eq(""), argThat((DisplayableList dl) -> dl.equals(displayableListPanel.getDisplayableList())));
 }

 @Test
 public void testSelectListItems() {
 when(listItemSelector.selectListItems(anyString(), any(DisplayableList.class))).thenReturn(true);
 displayableListPanel.selectListItems();
 verify(listItemSelector).selectListItems(eq(""), argThat((DisplayableList dl) -> dl.equals(displayableListPanel.getDisplayableList())));
 }

 @Test
 public void testUpdateButtonStates() {
 // Test disabled state
 displayableListPanel.setNewRecord(true);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getView().isEnabled());
 assertFalse(displayableListPanel.getSelect().isEnabled());

 // Test enabled state
 ArrayList<Displayable> masterList = new ArrayList<>();
 DisplayableList displayableList = new DisplayableList(displayableListPanel.getUserInterfaceFactory());
 Displayable selectedListItem = new Displayable("Test Item");
 masterList.add(selectedListItem);
 displayableList.setDisplayItems(masterList);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.getAdd().isEnabled());
 assertTrue(displayableListPanel.getSelect().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getView().isEnabled());

 // Test write access with selected item
 Displayable anotherListItem = new Displayable("Another Test Item");
 masterList.add(anotherListItem);
 displayableList.setDisplayItems(masterList);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.setSelectedListItem(selectedListItem);
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.getAdd().isEnabled());
 assertTrue(displayableListPanel.getSelect().isEnabled());
 assertTrue(displayableListPanel.getDelete().isEnabled());
 assertTrue(displayableListPanel.getEdit().isEnabled());
 assertTrue(displayableListPanel.getView().isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 DisplayableList displayableList = new DisplayableList(displayableListPanel.getUserInterfaceFactory());
 Displayable selectedListItem = new Displayable("Test Item");
 masterList.add(selectedListItem);
 displayableList.setDisplayItems(masterList);
 displayableListPanel.setDisplayableList(displayableList);
 ArrayList<Displayable> newMasterList = new ArrayList<>();
 Displayable newDisplayableItem = new Displayable("New Test Item");
 newMasterList.add(newDisplayableItem);
 displayableList.setDisplayItems(newMasterList);
 displayableListPanel.setDisplayableItems(newMasterList);
 assertEquals(newDisplayableItem, displayableListPanel.getDisplayableList().getAllItems().get(0));
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 DisplayableList displayableList = new DisplayableList(displayableListPanel.getUserInterfaceFactory());
 Displayable selectedListItem = new Displayable("Test Item");
 masterList.add(selectedListItem);
 displayableList.setDisplayItems(masterList);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.getMasterList());
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 DisplayableList displayableList = new DisplayableList(displayableListPanel.getUserInterfaceFactory());
 Displayable selectedListItem = new Displayable("Test Item");
 masterList.add(selectedListItem);
 displayableList.setDisplayItems(masterList);
 displayableListPanel.setDisplayableList(displayableList);
 assertEquals(masterList, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 String listOwnerName = "Test List Owner";
 displayableListPanel.setListOwnerName(listOwnerName);
 assertEquals(listOwnerName, displayableListPanel.getListOwnerName());
 }

 @Test
 public void testActionPerformed() {
 ActionEvent actionEvent = mock(ActionEvent.class);
 when(displayableListPanel.getSelect()).thenReturn(displayableListPanel);
 when(displayableListPanel.getAdd()).thenReturn(displayableListPanel);
 when(displayableListPanel.getEdit()).thenReturn(displayableListPanel);
 when(displayableListPanel.getView()).thenReturn(displayableListPanel);
 when(displayableListPanel.getDelete()).thenReturn(displayableListPanel);
 when(listItemSelector.selectListItems(anyString(), any(DisplayableList.class))).thenReturn(true);
 when(listItemAdder.addItems(anyString(), any(DisplayableList.class))).thenReturn(true);
 when(listItemEditor.editListItem(anyString(), any(Displayable.class), any(DisplayableList.class))).thenReturn(true);
 when(listItemViewer.viewListItem(any(Displayable.class))).thenReturn(true);
 when(listItemDeleter.deleteListItems(anyString(), any(DisplayableList.class))).thenReturn(true);
 displayableListPanel.actionPerformed(actionEvent);
 verify(listItemSelector).selectListItems(eq(""), argThat((DisplayableList dl) -> dl.equals(displayableListPanel.getDisplayableList())));
 }
}