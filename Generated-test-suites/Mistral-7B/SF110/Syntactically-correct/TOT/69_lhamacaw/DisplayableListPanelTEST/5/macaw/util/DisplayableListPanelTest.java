package macaw.util.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListParentForm parentFormMock;
 private SessionProperties sessionPropertiesMock;
 private UserInterfaceFactory userInterfaceFactoryMock;
 private Log logMock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 sessionPropertiesMock = mock(SessionProperties.class);
 parentFormMock = mock(DisplayableListParentForm.class);
 userInterfaceFactoryMock = mock(UserInterfaceFactory.class);
 logMock = mock(Log.class);

 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", true, true);
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 }

 @Test
 public void testInit() {
 // Test initializing DisplayableListPanel with basic parameters
 assertNotNull(displayableListPanel);
 // Verify that sessionProperties, parentForm, and userInterfaceFactory have been set
 verify(sessionPropertiesMock).getLog();
 verify(parentFormMock).commitChanges();
 verify(userInterfaceFactoryMock, atLeastOnce()).createButton(eq("Select"));
 verify(userInterfaceFactoryMock, atLeastOnce()).createButton(eq("Add"));
 verify(userInterfaceFactoryMock, atLeastOnce()).createButton(eq("Edit"));
 verify(userInterfaceFactoryMock, atLeastOnce()).createButton(eq("View"));
 verify(userInterfaceFactoryMock, atLeastOnce()).createButton(eq("Delete"));
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 // Test setting displayableListItemSelector
 DisplayableListItemSelector mockSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockSelector);
 // Verify that displayableListItemSelector has been set
 assertEquals(mockSelector, displayableListPanel.getDisplayableListItemSelector());
 }

 @Test
 public void testAddListItem() {
 // Test adding a new list item
 when(parentFormMock.commitChanges()).thenReturn(true);
 DisplayableListItemAdder mockAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(mockAdder);

 displayableListPanel.addListItem();
 // Verify that addListItem method calls commitChanges, addItems, and updateButtonStates
 verify(parentFormMock).commitChanges();
 verify(mockAdder).addItems(eq("Test Owner Name"), displayableListPanel.getDisplayableList());
 verify(displayableListPanel).updateButtonStates();
 }

 @Test(expected = MacawException.class)
 public void testAddListItem_MissingAdder() {
 when(parentFormMock.commitChanges()).thenReturn(true);

 displayableListPanel.addListItem();
 fail("AddListItem method should throw MacawException when displayableListItemAdder is null");
 }

 @Test
 public void testEditSelectedListItem() {
 // Test editing a selected list item
 when(parentFormMock.commitChanges()).thenReturn(true);
 DisplayableListItemEditor mockEditor = mock(DisplayableListItemEditor.class);
 Displayable selectedListItem = new Displayable();
 displayableListPanel.setDisplayableListItemEditor(mockEditor);
 displayableListPanel.getDisplayableList().add(selectedListItem);

 displayableListPanel.editSelectedListItem();
 // Verify that editListItem method is called with the correct arguments
 verify(mockEditor).editListItem(eq("Test Owner Name"), eq(selectedListItem), eq(displayableListPanel.getDisplayableList()));
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItem_MissingEditor() {
 when(parentFormMock.commitChanges()).thenReturn(true);
 Displayable selectedListItem = new Displayable();
 displayableListPanel.getDisplayableList().add(selectedListItem);

 displayableListPanel.editSelectedListItem();
 fail("EditSelectedListItem method should throw MacawException when displayableListItemEditor is null");
 }

 @Test
 public void testViewSelectedListItem() {
 // Test viewing a selected list item
 DisplayableListItemViewer mockViewer = mock(DisplayableListItemViewer.class);
 Displayable selectedListItem = new Displayable();
 displayableListPanel.setDisplayableListItemViewer(mockViewer);
 displayableListPanel.getDisplayableList().add(selectedListItem);

 displayableListPanel.viewSelectedListItem();
 // Verify that viewListItem method is called with the correct argument
 verify(mockViewer).viewListItem(eq(selectedListItem));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItem_MissingViewer() {
 Displayable selectedListItem = new Displayable();
 displayableListPanel.getDisplayableList().add(selectedListItem);

 displayableListPanel.viewSelectedListItem();
 fail("ViewSelectedListItem method should throw MacawException when displayableListItemViewer is null");
 }

 @Test
 public void testDeleteSelectedListItems() {
 // Test deleting selected list items
 when(parentFormMock.commitChanges()).thenReturn(true);
 DisplayableListItemDeleter mockDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(mockDeleter);
 Displayable selectedListItem1 = new Displayable();
 Displayable selectedListItem2 = new Displayable();
 displayableListPanel.getDisplayableList().add(selectedListItem1);
 displayableListPanel.getDisplayableList().add(selectedListItem2);

 displayableListPanel.deleteSelectedListItems();
 // Verify that deleteListItems method is called with the correct arguments
 verify(mockDeleter).deleteListItems(eq("Test Owner Name"), displayableListPanel.getDisplayableList());
 // Verify that updateButtonStates method is called
 verify(displayableListPanel).updateButtonStates();
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItems_MissingDeleter() {
 Displayable selectedListItem1 = new Displayable();
 Displayable selectedListItem2 = new Displayable();
 displayableListPanel.getDisplayableList().add(selectedListItem1);
 displayableListPanel.getDisplayableList().add(selectedListItem2);

 displayableListPanel.deleteSelectedListItems();
 fail("DeleteSelectedListItems method should throw MacawException when displayableListItemDeleter is null");
 }

 @Test
 public void testSelectListItems() {
 // Test selecting list items
 DisplayableListItemSelector mockSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockSelector);
 Displayable listItem1 = new Displayable();
 Displayable listItem2 = new Displayable();
 displayableListPanel.getDisplayableList().add(listItem1);
 displayableListPanel.getDisplayableList().add(listItem2);

 displayableListPanel.selectListItems();
 // Verify that selectListItems method calls selectListItems on displayableListItemSelector
 verify(mockSelector).selectListItems(eq("Test Owner Name"), displayableListPanel.getDisplayableList());
 }

 @Test
 public void testUpdateButtonStates() {
 // Test updating button states
 DisplayableList list = new DisplayableList(userInterfaceFactoryMock);
 list.add(new Displayable());
 list.add(new Displayable());

 displayableListPanel.setDisplayableList(list);
 displayableListPanel.updateButtonStates();
 // Verify that buttons are disabled when there are no items and not in new record mode
 assertTrue(displayableListPanel.getAdd().isDisabled());
 assertTrue(displayableListPanel.getDelete().isDisabled());
 assertTrue(displayableListPanel.getEdit().isDisabled());
 assertTrue(displayableListPanel.getView().isDisabled());
 // Verify that buttons are enabled when there is at least one item and in new record mode
 displayableListPanel.setNewRecord(true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.getAdd().isEnabled());
 assertTrue(displayableListPanel.getSelect().isEnabled());
 assertTrue(displayableListPanel.getDelete().isDisabled());
 assertTrue(displayableListPanel.getEdit().isDisabled());
 assertTrue(displayableListPanel.getView().isDisabled());
 // Verify that buttons are enabled when there is at least one item and not in new record mode
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.getAdd().isEnabled());
 assertTrue(displayableListPanel.getSelect().isEnabled());
 assertTrue(displayableListPanel.getDelete().isEnabled());
 assertTrue(displayableListPanel.getEdit().isEnabled());
 assertTrue(displayableListPanel.getView().isEnabled());
 }

}