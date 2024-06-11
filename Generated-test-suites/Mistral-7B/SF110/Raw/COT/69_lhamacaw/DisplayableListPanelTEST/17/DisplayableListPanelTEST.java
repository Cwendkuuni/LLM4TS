package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;
 private DisplayableList displayableList;
 private JButton select;
 private JButton add;
 private JButton edit;
 private JButton view;
 private JButton delete;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 log = Mockito.mock(Log.class);
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableList = Mockito.mock(DisplayableList.class);
 select = Mockito.mock(JButton.class);
 add = Mockito.mock(JButton.class);
 edit = Mockito.mock(JButton.class);
 view = Mockito.mock(JButton.class);
 delete = Mockito.mock(JButton.class);

 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.buildUI();
 displayableListPanel.setDisplayableList(displayableList);

 Mockito.when(sessionProperties.getLog()).thenReturn(log);
 Mockito.when(parentForm.commitChanges()).thenReturn(true);

 displayableListPanel.select = select;
 displayableListPanel.add = add;
 displayableListPanel.edit = edit;
 displayableListPanel.view = view;
 displayableListPanel.delete = delete;
 }

 @Test
 public void testConstructorWithAllowWriteAccessOnly() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isNewRecord());
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.isNewRecord());
 }

 @Test
 public void testGetPanel() {
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertEquals(listItemSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertEquals(listItemAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertEquals(listItemEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertEquals(listItemViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertEquals(listItemDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 assertTrue(displayableList.itemsAreSelected());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutListItemAdder() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.addListItem();
 fail("AddListItem should have thrown MacawException when listItemAdder is null");
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedListItem = Mockito.mock(Displayable.class);
 Mockito.when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.editSelectedListItem();
 assertTrue(selectedListItem.isModified());
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithoutListItemEditor() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.editSelectedListItem();
 fail("EditSelectedListItem should have thrown MacawException when listItemEditor is null");
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = Mockito.mock(Displayable.class);
 Mockito.when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 assertFalse(displayableList.itemsAreSelected());
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithoutListItemViewer() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.viewSelectedListItem();
 fail("ViewSelectedListItem should have thrown MacawException when listItemViewer is null");
 }

 @Test
 public void testDeleteSelectedListItems() {
 Mockito.when(displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.deleteSelectedListItems();
 assertTrue(displayableList.isEmpty());
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithoutListItemDeleter() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.deleteSelectedListItems();
 fail("DeleteSelectedListItems should have thrown MacawException when listItemDeleter is null");
 }

 @Test
 public void testSelectListItems() {
 List<Displayable> displayables = new ArrayList<>();
 for (int i = 0; i < 5; i++) {
 Displayable displayable = Mockito.mock(Displayable.class);
 displayables.add(displayable);
 }
 Mockito.when(displayableList.getAllItems()).thenReturn(displayables);
 displayableListPanel.selectListItems();
 for (Displayable displayable : displayables) {
 Mockito.verify(displayable).setSelected(true);
 }
 }

 @Test
 public void testUpdateButtonStates() {
 Mockito.when(displayableList.isEmpty()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertFalse(add.isEnabled());
 assertFalse(edit.isEnabled());
 assertFalse(view.isEnabled());
 assertFalse(delete.isEnabled());

 Mockito.when(displayableList.isEmpty()).thenReturn(false);
 displayableListPanel.updateButtonStates();
 assertTrue(add.isEnabled());
 assertTrue(select.isEnabled());
 assertTrue(edit.isEnabled());
 assertTrue(view.isEnabled());
 assertTrue(delete.isEnabled());

 Mockito.when(displayableList.isEmpty()).thenReturn(true);
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 assertFalse(add.isEnabled());
 assertFalse(edit.isEnabled());
 assertFalse(view.isEnabled());
 assertFalse(delete.isEnabled());
 }

 @Test
 public void testSave() {
 List<Displayable> masterList = new ArrayList<>();
 Displayable displayable = Mockito.mock(Displayable.class);
 masterList.add(displayable);
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.getAllListItems());
 }
}