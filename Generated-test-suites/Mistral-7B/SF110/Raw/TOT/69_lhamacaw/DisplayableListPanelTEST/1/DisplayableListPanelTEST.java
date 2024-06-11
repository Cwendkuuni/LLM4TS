package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableListItemSelector listItemSelectorMock;
 private DisplayableListItemAdder listItemAdderMock;
 private DisplayableListItemEditor listItemEditorMock;
 private DisplayableListItemViewer listItemViewerMock;
 private DisplayableListItemDeleter listItemDeleterMock;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 log = mock(Log.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 listItemSelectorMock = mock(DisplayableListItemSelector.class);
 listItemAdderMock = mock(DisplayableListItemAdder.class);
 listItemEditorMock = mock(DisplayableListItemEditor.class);
 listItemViewerMock = mock(DisplayableListItemViewer.class);
 listItemDeleterMock = mock(DisplayableListItemDeleter.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setUserInterfaceFactory(userInterfaceFactory);
 displayableListPanel.setLog(log);
 when(sessionProperties.getLog()).thenReturn(log);
 when(sessionProperties.getProperty("currentUser")).thenReturn(new User("testUserID"));
 when(userInterfaceFactory.createLabel(anyString())).thenAnswer((Answer<JLabel>) invocation -> new JLabel((String) invocation.getArguments()[0]));
 when(userInterfaceFactory.createButton(anyString())).thenAnswer((Answer<JButton>) invocation -> new JButton((String) invocation.getArguments()[0]));
 }

 @Test
 public void testInit() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel Title", true, true);
 verify(sessionProperties, times(1)).getLog();
 verify(parentForm, times(1)).commitChanges();
 verify(displayableListPanel, times(1)).setUserInterfaceFactory(userInterfaceFactory);
 verify(displayableListPanel, times(1)).setLog(log);
 }

 @Test
 public void testBuildUI() {
 displayableListPanel.buildUI();
 JPanel panel = displayableListPanel.getPanel();
 assertTrue("Panel should not be null", panel != null);
 }

 @Test
 public void testCreateControlButtonPanel() {
 JPanel controlButtonPanel = displayableListPanel.createControlButtonPanel();
 assertTrue("Control button panel should not be null", controlButtonPanel != null);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 verify(displayableListPanel, times(1)).updateButtonStates();
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelectorMock);
 verify(displayableListPanel, times(1)).setListItemSelector(listItemSelectorMock);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdderMock);
 verify(displayableListPanel, times(1)).setListItemAdder(listItemAdderMock);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditorMock);
 verify(displayableListPanel, times(1)).setListItemEditor(listItemEditorMock);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewerMock);
 verify(displayableListPanel, times(1)).setListItemViewer(listItemViewerMock);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleterMock);
 verify(displayableListPanel, times(1)).setListItemDeleter(listItemDeleterMock);
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 verify(parentForm, times(1)).commitChanges();
 verify(listItemAdderMock, times(1)).addItems(eq("Test List Owner"), displayableListPanel.getDisplayableList());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.addListItem();
 fail("Should throw MacawException for missing adder");
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable testDisplayable = new Displayable();
 when(displayableListPanel.getDisplayableList().getSelectedItem()).thenReturn(testDisplayable);
 displayableListPanel.editSelectedListItem();
 verify(parentForm, times(1)).commitChanges();
 verify(listItemEditorMock, times(1)).editListItem(eq("Test List Owner"), testDisplayable, displayableListPanel.getDisplayableList());
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 Displayable testDisplayable = new Displayable();
 when(displayableListPanel.getDisplayableList().getSelectedItem()).thenReturn(testDisplayable);
 displayableListPanel.editSelectedListItem();
 fail("Should throw MacawException for missing editor");
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable testDisplayable = new Displayable();
 when(displayableListPanel.getDisplayableList().getSelectedItem()).thenReturn(testDisplayable);
 displayableListPanel.viewSelectedListItem();
 verify(listItemViewerMock, times(1)).viewListItem(testDisplayable);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 Displayable testDisplayable = new Displayable();
 when(displayableListPanel.getDisplayableList().getSelectedItem()).thenReturn(testDisplayable);
 displayableListPanel.viewSelectedListItem();
 fail("Should throw MacawException for missing viewer");
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableListPanel.deleteSelectedListItems();
 verify(parentForm, times(1)).commitChanges();
 verify(listItemDeleterMock, times(1)).deleteListItems(eq("Test List Owner"), displayableListPanel.getDisplayableList());
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.deleteSelectedListItems();
 fail("Should throw MacawException for missing deleter");
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();
 verify(listItemSelectorMock, times(1)).selectListItems(eq("Test List Owner"), displayableListPanel.getDisplayableList());
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 verify(displayableListPanel, times(1)).setDisplayableListItemSelector(listItemSelectorMock);
 verify(displayableListPanel, times(1)).setDisplayableListItemAdder(listItemAdderMock);
 verify(displayableListPanel, times(1)).setDisplayableListItemEditor(listItemEditorMock);
 verify(displayableListPanel, times(1)).setDisplayableListItemViewer(listItemViewerMock);
 verify(displayableListPanel, times(1)).setDisplayableListItemDeleter(listItemDeleterMock);

 JButton selectButton = displayableListPanel.getSelectButton();
 JButton addButton = displayableListPanel.getAddButton();
 JButton editButton = displayableListPanel.getEditButton();
 JButton viewButton = displayableListPanel.getViewButton();
 JButton deleteButton = displayableListPanel.getDeleteButton();

 assertFalse("Select button should be disabled when no items are selected", selectButton.isEnabled());
 assertFalse("Add button should be disabled when no add functionality is allowed", addButton.isEnabled());
 assertTrue("Edit button should be enabled when record editing is allowed", editButton.isEnabled());
 assertTrue("View button should be enabled when record editing is allowed", viewButton.isEnabled());
 assertFalse("Delete button should be disabled when no items are selected", deleteButton.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithItemsSelected() {
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 displayableListPanel.selectListItems();

 JButton selectButton = displayableListPanel.getSelectButton();
 JButton addButton = displayableListPanel.getAddButton();
 JButton editButton = displayableListPanel.getEditButton();
 JButton viewButton = displayableListPanel.getViewButton();
 JButton deleteButton = displayableListPanel.getDeleteButton();

 assertTrue("Select button should be enabled when items are selected", selectButton.isEnabled());
 assertFalse("Add button should be disabled when no add functionality is allowed", addButton.isEnabled());
 assertTrue("Edit button should be enabled when record editing is allowed", editButton.isEnabled());
 assertTrue("View button should be enabled when record editing is allowed", viewButton.isEnabled());
 assertTrue("Delete button should be enabled when items are selected", deleteButton.isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithNoWriteAccess() {
 displayableListPanel.setNewRecord(false);
 displayableListPanel.setAllowWriteAccess(false);
 displayableListPanel.updateButtonStates();

 JButton selectButton = displayableListPanel.getSelectButton();
 JButton addButton = displayableListPanel.getAddButton();
 JButton editButton = displayableListPanel.getEditButton();
 JButton viewButton = displayableListPanel.getViewButton();
 JButton deleteButton = displayableListPanel.getDeleteButton();

 assertTrue("Select button should be enabled when no write access is required", selectButton.isEnabled());
 assertFalse("Add button should be disabled when no add functionality is allowed", addButton.isEnabled());
 assertFalse("Edit button should be disabled when no write access is required", editButton.isEnabled());
 assertFalse("View button should be disabled when no write access is required", viewButton.isEnabled());
 assertFalse("Delete button should be disabled when no write access is required", deleteButton.isEnabled());
 }

}