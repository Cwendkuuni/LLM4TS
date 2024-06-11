package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.*;
import org.junit.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 parentForm = mock(DisplayableListParentForm.class);
 log = mock(Log.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 }

 @Test
 public void testDisplayableListPanelConstructor1() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testDisplayableListPanelConstructor2() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testInit() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 verify(parentForm).commitChanges();
 // Add more assertions as needed for specific initializations
 }

 @Test
 public void testBuildUI() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutAdder() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 displayableListPanel.addListItem();
 fail("AddListItem should throw MacawException when listItemAdder is null");
 }

 @Test
 public void testEditSelectedListItemWithoutEditor() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 try {
 displayableListPanel.editSelectedListItem();
 fail("editSelectedListItem should throw MacawException when listItemEditor is null");
 }
 catch (MacawException e) {
 // Expected exception
 }
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithoutViewer() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 try {
 displayableListPanel.viewSelectedListItem();
 fail("viewSelectedListItem should throw MacawException when listItemViewer is null");
 }
 catch (MacawException e) {
 // Expected exception
 }
 }

 @Test
 public void testDeleteSelectedListItemsWithoutDeleter() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 try {
 displayableListPanel.deleteSelectedListItems();
 fail("deleteSelectedListItems should throw MacawException when listItemDeleter is null");
 }
 catch (MacawException e) {
 // Expected exception
 }
}

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemSelector mockSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockSelector);
 assertSame(mockSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemAdder mockAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(mockAdder);
 assertSame(mockAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemEditor mockEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(mockEditor);
 assertSame(mockEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemViewer mockViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(mockViewer);
 assertSame(mockViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemDeleter mockDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(mockDeleter);
 assertSame(mockDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testSetNewRecordWithIsNewRecordTrue() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord);
 }

 @Test
 public void testSetNewRecordWithIsNewRecordFalse() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setNewRecord(false);
 assertFalse(displayableListPanel.isNewRecord);
 }

 @Test
 public void testGetPanel() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test
 public void testUpdateButtonStatesWithAllowWriteAccessAndNoItems() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithAllowWriteAccessAndItems() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 ArrayList<Displayable> items = new ArrayList<>();
 Displayable item = new Displayable();
 items.add(item);
 displayableListPanel.displayableList.setDisplayItems(items);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithNoWriteAccessAndNoItems() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", false, true);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithNoWriteAccessAndItems() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", false, true);
 ArrayList<Displayable> items = new ArrayList<>();
 Displayable item = new Displayable();
 items.add(item);
 displayableListPanel.displayableList.setDisplayItems(items);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable item = new Displayable();
 masterList.add(item);
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableList displayableList = new DisplayableList(sessionProperties.getUserInterfaceFactory());
 ArrayList<Displayable> items = new ArrayList<>();
 Displayable item = new Displayable();
 items.add(item);
 displayableList.setDisplayItems(items);
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.save();
 assertTrue(displayableListPanel.masterList.containsAll(displayableList.getAllItems()));
 }

 @Test
 public void testDeleteSelectedListItemsWithNoItemsSelected() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.deleteSelectedListItems();
 assertTrue(displayableListPanel.displayableList.isEmpty());
 }

 @Test
 public void testDeleteSelectedListItemsWithItemsSelected() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 ArrayList<Displayable> items = new ArrayList<>();
 Displayable item1 = new Displayable();
 items.add(item1);
 Displayable item2 = new Displayable();
 items.add(item2);
 displayableListPanel.displayableList.setDisplayItems(items);
 displayableListPanel.displayableList.selectItem(item1);
 displayableListPanel.deleteSelectedListItems();
 assertFalse(displayableListPanel.displayableList.getAllItems().contains(item1));
 assertFalse(displayableListPanel.displayableList.getAllItems().contains(item2));
 }

 @Test
 public void testSelectListItemsWithNoItemsSelected() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.selectListItems();
 assertTrue(displayableListPanel.displayableList.isEmpty());
 }

 @Test
 public void testSelectListItemsWithItemsSelected() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 ArrayList<Displayable> items = new ArrayList<>();
 Displayable item1 = new Displayable();
 items.add(item1);
 Displayable item2 = new Displayable();
 items.add(item2);
 displayableListPanel.displayableList.setDisplayItems(items);
 displayableListPanel.displayableList.selectItem(item1);
 displayableListPanel.selectListItems();
 assertTrue(displayableListPanel.displayableList.getSelectedItems().contains(item1));
 }

}