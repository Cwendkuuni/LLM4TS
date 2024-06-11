package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import macaw.system.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;
 private User user;
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
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 log = Mockito.mock(Log.class);
 user = Mockito.mock(User.class);
 Mockito.when(sessionProperties.getLog()).thenReturn(log);
 Mockito.when(sessionProperties.getProperty("currentUser")).thenReturn(user);
 displayableList = Mockito.mock(DisplayableList.class);
 listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 }

 @Test
 public void testConstructorWithAllowWriteAccess() {
 assertNotNull(displayableListPanel);
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 }

 @Test
 public void testConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 DisplayableListPanel displayableListPanelWithEditing = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertTrue(displayableListPanelWithEditing.isAllowWriteAccess());
 assertTrue(displayableListPanelWithEditing.isAllowRecordEditing());
 }

 @Test
 public void testInit() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testBuildUI() {
 JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isNewRecord());
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertEquals(listItemSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertEquals(listItemAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertEquals(listItemEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertEquals(listItemViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertEquals(listItemDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 Mockito.when(listItemAdder.addItems(ArgumentMatchers.eq("Test Owner"), ArgumentMatchers.eq(displayableList))).thenReturn(new ArrayList<>());
 displayableListPanel.addListItem();
 assertTrue(displayableList.itemsAreSelected());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.addListItem();
 fail("AddListItem should throw MacawException when listItemAdder is null");
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedListItem = Mockito.mock(Displayable.class);
 Mockito.when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 Mockito.when(listItemEditor.editListItem(ArgumentMatchers.eq("Test Owner"), ArgumentMatchers.eq(selectedListItem), ArgumentMatchers.eq(displayableList))).thenReturn(new Displayable());
 displayableListPanel.editSelectedListItem();
 assertTrue(displayableList.itemsAreSelected());
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.editSelectedListItem();
 fail("EditSelectedListItem should throw MacawException when listItemEditor is null");
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = Mockito.mock(Displayable.class);
 Mockito.when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 Mockito.when(listItemViewer.viewListItem(selectedListItem)).thenReturn(new JFrame());
 displayableListPanel.viewSelectedListItem();
 assertTrue(displayableList.itemsAreSelected());
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.viewSelectedListItem();
 fail("ViewSelectedListItem should throw MacawException when listItemViewer is null");
 }

 @Test
 public void testDeleteSelectedListItems() {
 Mockito.when(displayableList.itemsAreSelected()).thenReturn(true);
 Mockito.when(listItemDeleter.deleteListItems(ArgumentMatchers.eq("Test Owner"), ArgumentMatchers.eq(displayableList))).thenReturn(new ArrayList<>());
 displayableListPanel.deleteSelectedListItems();
 assertFalse(displayableList.itemsAreSelected());
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.deleteSelectedListItems();
 fail("DeleteSelectedListItems should throw MacawException when listItemDeleter is null");
 }

 @Test
 public void testSelectListItems() {
 Mockito.when(listItemSelector.selectListItems(ArgumentMatchers.eq("Test Owner"), ArgumentMatchers.eq(displayableList))).thenReturn(new ArrayList<>());
 displayableListPanel.selectListItems();
 assertTrue(displayableList.itemsAreSelected());
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsWithMissingSelector() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.selectListItems();
 fail("SelectListItems should throw MacawException when listItemSelector is null");
 }

 @Test
 public void testUpdateButtonStates() {
 boolean isNewRecord = true;
 displayableListPanel.setNewRecord(isNewRecord);
 JButton[] buttons = {displayableListPanel.select, displayableListPanel.add, displayableListPanel.edit, displayableListPanel.view, displayableListPanel.delete};
 for (JButton button : buttons) {
 if (button != null) {
 assertEquals(false, button.isEnabled());
 }
 }

 displayableListPanel.displayableList.addItem(new Displayable());
 assertFalse(displayableList.isEmpty());
 isNewRecord = false;
 displayableListPanel.setNewRecord(isNewRecord);
 for (JButton button : buttons) {
 if (button != null) {
 if (button == displayableListPanel.add) {
 assertEquals(true, button.isEnabled());
 }
 else {
 assertEquals(true, button.isEnabled());
 }
 }
 }
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.save();
 assertEquals(masterList, displayableList.getAllItems());
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test Owner");
 assertEquals("Test Owner", displayableListPanel.listOwnerName);
 }
}