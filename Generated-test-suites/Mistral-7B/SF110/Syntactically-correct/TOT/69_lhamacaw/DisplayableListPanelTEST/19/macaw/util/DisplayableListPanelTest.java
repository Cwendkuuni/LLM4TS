package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.system.MacawException;
import macaw.system.MacawMessages;
import macaw.system.SessionProperties;
import macaw.system.User;
import macaw.system.UserInterfaceFactory;
import macaw.util.Displayable;
import macaw.util.DisplayableList;
import macaw.util.DisplayableListItemAdder;
import macaw.util.DisplayableListItemEditor;
import macaw.util.DisplayableListItemDeleter;
import macaw.util.DisplayableListItemSelector;
import macaw.util.DisplayableListItemViewer;
import macaw.util.DisplayableListPanel;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableList displayableList;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemDeleter listItemDeleter;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemViewer listItemViewer;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 log = mock(Log.class);

 displayableList = new DisplayableList(userInterfaceFactory);
 displayableListPanel = new DisplayableListPanel(sessionProperties, null, "", false, false);
 displayableListPanel.init(sessionProperties, null, "", false, false);

 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);
 listItemSelector = mock(DisplayableListItemSelector.class);
 listItemViewer = mock(DisplayableListItemViewer.class);

 when(sessionProperties.getLog()).thenReturn(log);
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(userInterfaceFactory);
 when(userInterfaceFactory.createButton(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);

 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 displayableList = null;
 listItemAdder = null;
 listItemEditor = null;
 listItemDeleter = null;
 listItemSelector = null;
 listItemViewer = null;
 sessionProperties = null;
 userInterfaceFactory = null;
 log = null;
 }

 @Test
 public void testInit() {
 final User user = new User("testuser", "testpassword");
 sessionProperties.setProperty("currentUser", user);
 sessionProperties.setProperty("log", log);

 displayableListPanel.init(sessionProperties, null, "", true, true);
 verify(sessionProperties, times(1)).setProperty("currentUser", user);
 verify(sessionProperties, times(1)).setProperty("log", log);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetNewRecordFalse() {
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testAddListItem() {
 when(listItemAdder.addItems(anyString(), any(DisplayableList.class))).thenReturn(true);
 displayableListPanel.addListItem();
 assertTrue(displayableListPanel.add.isEnabled());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMacawException() {
 when(listItemAdder.addItems(anyString(), any(DisplayableList.class))).thenThrow(new MacawException("Test error"));
 displayableListPanel.addListItem();
 fail("MacawException expected");
 }

 @Test
 public void testEditSelectedListItem() {
 final Displayable displayable = new Displayable("Test Displayable");
 displayableList.add(displayable);
 displayableListPanel.displayableList = displayableList;
 when(listItemEditor.editListItem(anyString(), any(Displayable.class), any(DisplayableList.class))).thenReturn(displayable);
 displayableListPanel.editSelectedListItem();
 assertEquals("Test Displayable", displayableList.getSelectedItem().getDisplayName());
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMacawException() {
 final Displayable displayable = new Displayable("Test Displayable");
 displayableList.add(displayable);
 displayableListPanel.displayableList = displayableList;
 when(listItemEditor.editListItem(anyString(), any(Displayable.class), any(DisplayableList.class))).thenThrow(new MacawException("Test error"));
 displayableListPanel.editSelectedListItem();
 fail("MacawException expected");
 }

 @Test
 public void testViewSelectedListItem() {
 final Displayable displayable = new Displayable("Test Displayable");
 displayableList.add(displayable);
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.viewSelectedListItem();
 assertTrue(displayableListViewer.viewListItem(displayable).isDisplayed());
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMacawException() {
 final Displayable displayable = new Displayable("Test Displayable");
 displayableList.add(displayable);
 displayableListPanel.displayableList = displayableList;
 when(listItemViewer.viewListItem(any(Displayable.class))).thenThrow(new MacawException("Test error"));
 displayableListPanel.viewSelectedListItem();
 fail("MacawException expected");
 }

 @Test
 public void testDeleteSelectedListItems() {
 when(JOptionPane.showConfirmDialog(any(Component.class), anyString(), anyString(), 0, 2)).thenReturn(JOptionPane.YES_OPTION);
 when(listItemDeleter.deleteListItems(anyString(), any(DisplayableList.class))).thenReturn(true);
 displayableList.add(new Displayable("Test Displayable 1"));
 displayableList.add(new Displayable("Test Displayable 2"));
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.deleteSelectedListItems();
 assertTrue(displayableList.isEmpty());
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMacawException() {
 when(listItemDeleter.deleteListItems(anyString(), any(DisplayableList.class))).thenThrow(new MacawException("Test error"));
 displayableList.add(new Displayable("Test Displayable 1"));
 displayableList.add(new Displayable("Test Displayable 2"));
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.deleteSelectedListItems();
 fail("MacawException expected");
 }

 @Test
 public void testSelectListItems() {
 final List<Displayable> displayables = new ArrayList<>();
 displayables.add(new Displayable("Test Displayable 1"));
 displayables.add(new Displayable("Test Displayable 2"));
 displayableList.setDisplayItems(displayables);
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.selectListItems();
 assertEquals(2, displayableList.getSelectedItems().size());
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsMacawException() {
 final List<Displayable> displayables = new ArrayList<>();
 displayables.add(new Displayable("Test Displayable 1"));
 displayables.add(new Displayable("Test Displayable 2"));
 displayableList.setDisplayItems(displayables);
 displayableListPanel.displayableList = displayableList;
 when(listItemSelector.selectListItems(anyString(), any(DisplayableList.class))).thenThrow(new MacawException("Test error"));
 displayableListPanel.selectListItems();
 fail("MacawException expected");
 }

}