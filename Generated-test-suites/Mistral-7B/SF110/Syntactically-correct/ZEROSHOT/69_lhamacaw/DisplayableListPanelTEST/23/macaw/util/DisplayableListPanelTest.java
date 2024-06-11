package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private User user;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemViewer listItemViewer;
 private DisplayableListItemDeleter listItemDeleter;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 user = Mockito.mock(User.class);
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 displayableList = Mockito.mock(DisplayableList.class);
 listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "DisplayableListPanel", true, true);
 }

 @Test
 public void testInit() {
 displayableListPanel.init(sessionProperties, parentForm, "DisplayableListPanel", true, true);

 assertEquals("DisplayableListPanel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());

 assertEquals(sessionProperties, displayableListPanel.getSessionProperties());
 assertEquals(parentForm, displayableListPanel.getParentForm());

 assertEquals("general.buttons.select", displayableListPanel.getSelect().getText());
 assertEquals("general.buttons.add", displayableListPanel.getAdd().getText());
 assertEquals("general.buttons.edit", displayableListPanel.getEdit().getText());
 assertEquals("general.buttons.view", displayableListPanel.getView().getText());
 assertEquals("general.buttons.delete", displayableListPanel.getDelete().getText());
 }

 @Test
 public void testBuildUI() {
 displayableListPanel.buildUI();

 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isAllowWriteAccess());
 assertFalse(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertNotNull(displayableListPanel.getListItemSelector());
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertNotNull(displayableListPanel.getListItemAdder());
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertNotNull(displayableListPanel.getListItemEditor());
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertNotNull(displayableListPanel.getListItemViewer());
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertNotNull(displayableListPanel.getListItemDeleter());
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 Mockito.verify(listItemAdder).addItems("DisplayableListPanel", displayableList);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "DisplayableListPanel", true, true);
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 displayableListPanel.editSelectedListItem();
 Mockito.verify(listItemEditor).editListItem("DisplayableListPanel", displayableList.getSelectedItem(), displayableList);
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "DisplayableListPanel", true, true);
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 displayableListPanel.viewSelectedListItem();
 Mockito.verify(listItemViewer).viewListItem(displayableList.getSelectedItem());
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "DisplayableListPanel", true, true);
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableListPanel.deleteSelectedListItems();
 Mockito.verify(listItemDeleter).deleteListItems("DisplayableListPanel", displayableList);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "DisplayableListPanel", true, true);
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();
 Mockito.verify(listItemSelector).selectListItems("DisplayableListPanel", displayableList);
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsWithMissingSelector() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "DisplayableListPanel", true, true);
 displayableListPanel.selectListItems();
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getView().isEnabled());
 assertFalse(displayableListPanel.getSelect().isEnabled());

 displayableList.addItem(Mockito.mock(Displayable.class));
 displayableListPanel.updateButtonStates();

 assertTrue(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getView().isEnabled());
 assertFalse(displayableListPanel.getSelect().isEnabled());

 displayableList.selectItem(0);
 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getView().isEnabled());
 assertTrue(displayableListPanel.getSelect().isEnabled());

 displayableList.addItem(Mockito.mock(Displayable.class));
 displayableList.selectItem(1);
 displayableListPanel.updateButtonStates();

 assertTrue(displayableListPanel.getAdd().isEnabled());
 assertTrue(displayableListPanel.getDelete().isEnabled());
 assertTrue(displayableListPanel.getEdit().isEnabled());
 assertTrue(displayableListPanel.getView().isEnabled());
 assertTrue(displayableListPanel.getSelect().isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 List<Displayable> masterList = new ArrayList<>();
 masterList.add(Mockito.mock(Displayable.class));
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 displayableListPanel.save();
 assertEquals(displayableListPanel.displayableList.getAllItems(), displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 List<Displayable> allListItems = displayableListPanel.getAllListItems();
 assertEquals(displayableListPanel.displayableList.getAllItems(), allListItems);
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("DisplayableListPanelOwner");
 assertEquals("DisplayableListPanelOwner", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformed() {
 ActionEvent event = Mockito.mock(ActionEvent.class);
 JButton button = Mockito.mock(JButton.class);
 Mockito.when(event.getSource()).thenReturn(button);
 displayableListPanel.actionPerformed(event);

 if (button == displayableListPanel.getSelect()) {
 displayableListPanel.selectListItems();
 } else if (button == displayableListPanel.getAdd()) {
 displayableListPanel.addListItem();
 } else if (button == displayableListPanel.getEdit()) {
 displayableListPanel.editSelectedListItem();
 } else if (button == displayableListPanel.getView()) {
 displayableListPanel.viewSelectedListItem();
 } else if (button == displayableListPanel.getDelete()) {
 displayableListPanel.deleteSelectedListItems();
 }
 }
}