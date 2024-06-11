package macaw.util;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.system.MacawException;
import macaw.util.DisplayableListPanel;
import macaw.util.DisplayableListParentForm;
import macaw.util.MacawMessages;
import macaw.util.User;
import macaw.util.UserInterfaceFactory;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListParentForm parentForm;
 private User currentUser;
 private SessionProperties sessionProperties;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 log = new Log();
 userInterfaceFactory = new UserInterfaceFactory();
 currentUser = new User("testUser", "testPassword");
 sessionProperties.setProperty("currentUser", currentUser);
 parentForm = new DisplayableListParentForm(sessionProperties);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 parentForm = null;
 sessionProperties = null;
 log = null;
 userInterfaceFactory = null;
 currentUser = null;
 }

 @Test
 public void testDisplayableListPanelConstructor() {
 assertEquals("Incorrect number of arguments in first constructor", 4, DisplayableListPanel.constructorCount);
 assertEquals("Incorrect number of arguments in second constructor", 5, DisplayableListPanel.constructorCount);
 DisplayableListPanel.constructorCount = 0;
 }

 @Test
 public void testInit() {
 SessionProperties sessionPropertiesMock = new SessionProperties();
 DisplayableListParentForm parentFormMock = new DisplayableListParentForm(sessionPropertiesMock);
 String panelTitleText = "Test Panel";
 boolean allowWriteAccess = true;
 DisplayableListPanel displayableListPanelMock = spy(new DisplayableListPanel(sessionPropertiesMock, parentFormMock, panelTitleText, allowWriteAccess));

 verifyNoMoreInteractions(displayableListPanelMock.getDisplayableList());
 verifyNoMoreInteractions(displayableListPanelMock.getListItemSelector());
 verifyNoMoreInteractions(displayableListPanelMock.getListItemAdder());
 verifyNoMoreInteractions(displayableListPanelMock.getListItemEditor());
 verifyNoMoreInteractions(displayableListPanelMock.getListItemViewer());
 verifyNoMoreInteractions(displayableListPanelMock.getListItemDeleter());

 doCallRealMethod().when(displayableListPanelMock);
 displayableListPanelMock.init(sessionPropertiesMock, parentFormMock, panelTitleText, allowWriteAccess, false);

 verify(displayableListPanelMock.getDisplayableList()).setMasterList(argument());
 verify(displayableListPanelMock.getDisplayableList()).setParentForm(argument());
 verify(displayableListPanelMock.getListItemSelector()).setDisplayableList(displayableListPanelMock);
 verify(displayableListPanelMock.getListItemAdder()).setDisplayableListPanel(displayableListPanelMock);
 verify(displayableListPanelMock.getListItemEditor()).setDisplayableListPanel(displayableListPanelMock);
 verify(displayableListPanelMock.getListItemViewer()).setDisplayableListPanel(displayableListPanelMock);
 verify(displayableListPanelMock.getListItemDeleter()).setDisplayableListPanel(displayableListPanelMock);
 }

 @Test
 public void testBuildUI() {
 DisplayableListPanel displayableListPanelMock = spy(new DisplayableListPanel(null, null, "Test Panel", true, false));
 doCallRealMethod().when(displayableListPanelMock);
 displayableListPanelMock.buildUI();
 assertNotNull("Panel should not be null", displayableListPanelMock.getPanel());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel.addListItem();
 fail("An exception should have been thrown");
 }

 @Test
 public void testEditSelectedListItemMissingEditor() throws MacawException {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 Displayable selectedListItem = new Displayable("Test Displayable", "Test DisplayName");
 displayableListPanel.setDisplayableListItemEditor(mock(DisplayableListItemEditor.class));
 displayableListPanel.setDisplayableList(new DisplayableList(userInterfaceFactory));
 displayableListPanel.getDisplayableList().add(selectedListItem);
 displayableListPanel.editSelectedListItem();
 fail("An exception should have been thrown");
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() throws MacawException {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 Displayable selectedListItem = new Displayable("Test Displayable", "Test DisplayName");
 displayableListPanel.setDisplayableListItemViewer(mock(DisplayableListItemViewer.class));
 displayableListPanel.setDisplayableList(new DisplayableList(userInterfaceFactory));
 displayableListPanel.getDisplayableList().add(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 fail("An exception should have been thrown");
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter() throws MacawException {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 ArrayList<Displayable> listItems = new ArrayList<>();
 listItems.add(new Displayable("Test Displayable", "Test DisplayName"));
 displayableListPanel.setDisplayableList(new DisplayableList(userInterfaceFactory));
 displayableListPanel.getDisplayableList().addAll(listItems);
 displayableListPanel.deleteSelectedListItems();
 fail("An exception should have been thrown");
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsMissingSelector() throws MacawException {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 ArrayList<Displayable> listItems = new ArrayList<>();
 listItems.add(new Displayable("Test Displayable", "Test DisplayName"));
 displayableListPanel.setDisplayableList(new DisplayableList(userInterfaceFactory));
 displayableListPanel.getDisplayableList().addAll(listItems);
 displayableListPanel.selectListItems();
 fail("An exception should have been thrown");
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 DisplayableList list = new DisplayableList(userInterfaceFactory);
 list.add(new Displayable("Test Displayable", "Test DisplayName"));
 displayableListPanel.setDisplayableList(list);
 displayableListPanel.setDisplayableListItemAdder(mock(DisplayableListItemAdder.class));
 displayableListPanel.setDisplayableListItemEditor(mock(DisplayableListItemEditor.class));
 displayableListPanel.setDisplayableListItemViewer(mock(DisplayableListItemViewer.class));
 displayableListPanel.setDisplayableListItemDeleter(mock(DisplayableListItemDeleter.class));
 assertFalse("Add button should be disabled when not new record", displayableListPanel.isAddEnabled());
 assertFalse("Delete button should be disabled when no items selected", displayableListPanel.isDeleteEnabled());
 assertFalse("Edit button should be disabled when no items selected", displayableListPanel.isEditEnabled());
 assertFalse("View button should be disabled when no items selected", displayableListPanel.isViewEnabled());
 assertFalse("Select button should be disabled when no items selected", displayableListPanel.isSelectEnabled());
 list.setSelectedItem(list.getAllItems().get(0));
 assertTrue("Add button should be enabled when new record", displayableListPanel.isAddEnabled());
 assertFalse("Delete button should be disabled when no items selected", displayableListPanel.isDeleteEnabled());
 assertFalse("Edit button should be disabled when no items selected", displayableListPanel.isEditEnabled());
 assertFalse("View button should be disabled when no items selected", displayableListPanel.isViewEnabled());
 assertFalse("Select button should be disabled when no items selected", displayableListPanel.isSelectEnabled());
 list.clearSelection();
 assertFalse("Add button should be disabled when not new record", displayableListPanel.isAddEnabled());
 assertFalse("Delete button should be disabled when no items selected", displayableListPanel.isDeleteEnabled());
 assertFalse("Edit button should be disabled when no items selected", displayableListPanel.isEditEnabled());
 assertFalse("View button should be disabled when no items selected", displayableListPanel.isViewEnabled());
 assertFalse("Select button should be disabled when no items selected", displayableListPanel.isSelectEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Displayable", "Test DisplayName"));
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals("Master list should have the same size as displayable list", masterList.size(), displayableListPanel.getDisplayableList().getAllItems().size());
 }

 @Test
 public void testSave() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Displayable", "Test DisplayName"));
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.save();
 assertEquals("Master list should have the same size as displayable list after save", masterList.size(), displayableListPanel.getMasterList().size());
 }
}