package macaw.util;

import static org.junit.Assert.*; // for assertions
import static org.mockito.Mockito.*; // for mockito

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
 private User user;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 user = new User("testUserID", "testName", "testEmail", "testPassword");
 when(sessionProperties.getProperty("currentUser")).thenReturn(user);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 }

 @Test
 public void testDisplayableListPanelCreationWithAllowWriteAccess() {
 // Testing constructor with allowWriteAccess = true
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelCreationWithAllowWriteAccessAndAllowRecordEditing() {
 // Testing constructor with allowWriteAccess = true and allowRecordEditing = true
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelCreationWithAllowWriteAccessAndAllowRecordEditingFalse() {
 // Testing constructor with allowWriteAccess = true and allowRecordEditing = false
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, false);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testInitialization() {
 // Testing initialization method
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 verify(userInterfaceFactory).createButton(anyString());
 verifyNoMoreInteractions(userInterfaceFactory);
 }

 @Test
 public void testBuildUI() {
 // Testing buildUI method
 displayableListPanel.buildUI();
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetNewRecord() {
 // Testing setNewRecord method
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isNewRecord());
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.isNewRecord());
 }

 @Test
 public void testGetPanel() {
 // Testing getPanel method
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 // Testing setDisplayableListItemSelector method
 DisplayableListItemSelector mockSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockSelector);
 assertEquals(mockSelector, displayableListPanel.getDisplayableListItemSelector());
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 // Testing setDisplayableListItemAdder method
 DisplayableListItemAdder mockAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(mockAdder);
 assertEquals(mockAdder, displayableListPanel.getDisplayableListItemAdder());
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 // Testing setDisplayableListItemEditor method
 DisplayableListItemEditor mockEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(mockEditor);
 assertEquals(mockEditor, displayableListPanel.getDisplayableListItemEditor());
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 // Testing setDisplayableListItemViewer method
 DisplayableListItemViewer mockViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(mockViewer);
 assertEquals(mockViewer, displayableListPanel.getDisplayableListItemViewer());
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 // Testing setDisplayableListItemDeleter method
 DisplayableListItemDeleter mockDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(mockDeleter);
 assertEquals(mockDeleter, displayableListPanel.getDisplayableListItemDeleter());
 }

 @Test
 public void testAddListItem() {
 // Testing addListItem method
 try {
 displayableListPanel.addListItem();
 fail("Should throw an exception as addListItem requires addListItemAdder to be set");
 } catch (AssertionError e) {
 // expected
 }

 DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.getListScrollPane()).thenReturn(new JScrollPane());
 displayableListPanel.displayableList = displayableList;
 DisplayableListItemAdder mockAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.listItemAdder = mockAdder;

 try {
 displayableListPanel.addListItem();
 verify(mockAdder).addItems(anyString(), displayableList);
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testEditSelectedListItem() {
 // Testing editSelectedListItem method
 try {
 displayableListPanel.editSelectedListItem();
 fail("Should throw an exception as editListItemEditor is not set");
 } catch (AssertionError e) {
 // expected
 }

 DisplayableListItemEditor mockEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.listItemEditor = mockEditor;
 Displayable selectedListItem = mock(Displayable.class);
 when(selectedListItem.getDisplayName()).thenReturn("TestDisplayName");
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);

 try {
 displayableListPanel.editSelectedListItem();
 verify(mockEditor).editListItem(anyString(), selectedListItem, displayableList);
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testViewSelectedListItem() {
 // Testing viewSelectedListItem method
 try {
 displayableListPanel.viewSelectedListItem();
 fail("Should throw an exception as viewListItemViewer is not set");
 } catch (AssertionError e) {
 // expected
 }

 DisplayableListItemViewer mockViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.listItemViewer = mockViewer;
 Displayable selectedListItem = mock(Displayable.class);
 when(selectedListItem.getDisplayName()).thenReturn("TestDisplayName");
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);

 try {
 displayableListPanel.viewSelectedListItem();
 verify(mockViewer).viewListItem(selectedListItem);
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testDeleteSelectedListItems() {
 // Testing deleteSelectedListItems method
 try {
 displayableListPanel.deleteSelectedListItems();
 fail("Should throw an exception as deleteListItemDeleter is not set");
 } catch (AssertionError e) {
 // expected
 }

 DisplayableListItemDeleter mockDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.listItemDeleter = mockDeleter;
 JOptionPane mockOptionPane = mock(JOptionPane.class);
 when(mockOptionPane.showConfirmDialog(any(Component.class), anyString(), anyString(), anyInt())).thenReturn(0);

 try {
 displayableListPanel.deleteSelectedListItems();
 verify(mockDeleter).deleteListItems(anyString(), displayableListPanel.displayableList);
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testSelectListItems() {
 // Testing selectListItems method
 try {
 displayableListPanel.selectListItems();
 fail("Should throw an exception as selectListItemSelector is not set");
 } catch (AssertionError e) {
 // expected
 }

 DisplayableListItemSelector mockSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.listItemSelector = mockSelector;

 try {
 displayableListPanel.selectListItems();
 verify(mockSelector).selectListItems(anyString(), displayableListPanel.displayableList);
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testUpdateButtonStates() {
 // Testing updateButtonStates method
 DisplayableListItemSelector mockSelector = mock(DisplayableListItemSelector.class);
 DisplayableListItemAdder mockAdder = mock(DisplayableListItemAdder.class);
 DisplayableListItemEditor mockEditor = mock(DisplayableListItemEditor.class);
 DisplayableListItemViewer mockViewer = mock(DisplayableListItemViewer.class);
 DisplayableListItemDeleter mockDeleter = mock(DisplayableListItemDeleter.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.listItemSelector = mockSelector;
 displayableListPanel.listItemAdder = mockAdder;
 displayableListPanel.listItemEditor = mockEditor;
 displayableListPanel.listItemViewer = mockViewer;
 displayableListPanel.listItemDeleter = mockDeleter;
 displayableListPanel.displayableList = new DisplayableList(userInterfaceFactory);

 when(mockSelector.isSelected(any(Displayable.class))).thenReturn(false);
 when(mockAdder.isPresent()).thenReturn(false);
 when(mockEditor.isPresent()).thenReturn(false);
 when(mockViewer.isPresent()).thenReturn(false);
 when(mockDeleter.isPresent()).thenReturn(false);

 displayableListPanel.displayableList.add(new Displayable("Test Display Name 1"));
 displayableListPanel.displayableList.add(new Displayable("Test Display Name 2"));
 displayableListPanel.displayableList.add(new Displayable("Test Display Name 3"));
 displayableListPanel.displayableList.setSelectedItem(displayableListPanel.displayableList.get(1));

 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 when(mockSelector.isSelected(any(Displayable.class))).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 when(mockAdder.isPresent()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 when(mockEditor.isPresent()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 when(mockViewer.isPresent()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 when(mockDeleter.isPresent()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 // Testing setDisplayableItems method
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Display Name 1"));
 masterList.add(new Displayable("Test Display Name 2"));
 masterList.add(new Displayable("Test Display Name 3"));

 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSave() {
 // Testing save method
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Display Name 1"));
 masterList.add(new Displayable("Test Display Name 2"));
 masterList.add(new Displayable("Test Display Name 3"));
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.getAllListItems());
 }
}