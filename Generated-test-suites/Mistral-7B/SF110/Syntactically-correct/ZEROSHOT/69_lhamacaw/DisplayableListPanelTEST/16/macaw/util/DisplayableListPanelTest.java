package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import org.junit.*;
import static macaw.util.MacawMessages.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private JFrame frame;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 parentForm = mock(DisplayableListParentForm.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccess() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testBuildUI() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test
 public void testSetNewRecord() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.add.isEnabled());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertSame(displayableListPanel.listItemSelector, listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertSame(displayableListPanel.listItemAdder, listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertSame(displayableListPanel.listItemEditor, listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertSame(displayableListPanel.listItemViewer, listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertSame(displayableListPanel.listItemDeleter, listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListParentForm parentFormMock = mock(DisplayableListParentForm.class);
 DisplayableListItemAdder listItemAdderMock = mock(DisplayableListItemAdder.class);
 when(parentFormMock.commitChanges()).thenReturn(true);
 when(listItemAdderMock.addItems(eq("Test List Owner Name"), anyList())).thenReturn(true);
 displayableListPanel.setDisplayableListItemAdder(listItemAdderMock);
 displayableListPanel.setDisplayableListParentForm(parentFormMock);
 displayableListPanel.addListItem();
 assertTrue(displayableListPanel.displayableList.size() > 0);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutListItemAdder() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.addListItem();
 fail("MacawException expected");
 }

 @Test
 public void testEditSelectedListItem() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemEditor listItemEditorMock = mock(DisplayableListItemEditor.class);
 when(listItemEditorMock.editListItem(eq("Test List Owner Name"), any(Displayable.class), anyList())).thenReturn(true);
 displayableListPanel.setDisplayableListItemEditor(listItemEditorMock);
 DisplayableListParentForm parentFormMock = mock(DisplayableListParentForm.class);
 when(parentFormMock.commitChanges()).thenReturn(true);
 Displayable selectedListItem = new Displayable();
 displayableListPanel.displayableList.add(selectedListItem);
 displayableListPanel.displayableList.setSelectedItem(selectedListItem);
 displayableListPanel.editSelectedListItem();
 assertTrue(selectedListItem.isModified());
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithoutListItemEditor() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.editSelectedListItem();
 fail("MacawException expected");
 }

 @Test
 public void testViewSelectedListItem() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemViewer listItemViewerMock = mock(DisplayableListItemViewer.class);
 when(listItemViewerMock.viewListItem(any(Displayable.class))).thenReturn(true);
 Displayable selectedListItem = new Displayable();
 displayableListPanel.displayableList.add(selectedListItem);
 displayableListPanel.displayableList.setSelectedItem(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 assertTrue(listItemViewerMock.viewListItemCallCount > 0);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithoutListItemViewer() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.viewSelectedListItem();
 fail("MacawException expected");
 }

 @Test
 public void testDeleteSelectedListItems() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemDeleter listItemDeleterMock = mock(DisplayableListItemDeleter.class);
 when(parentForm.commitChanges()).thenReturn(true);
 when(listItemDeleterMock.deleteListItems(eq("Test List Owner Name"), anyList())).thenReturn(true);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleterMock);
 Displayable selectedListItem1 = new Displayable();
 Displayable selectedListItem2 = new Displayable();
 displayableListPanel.displayableList.add(selectedListItem1);
 displayableListPanel.displayableList.add(selectedListItem2);
 displayableListPanel.displayableList.setSelectedItems(new Object[]{selectedListItem1, selectedListItem2});
 displayableListPanel.deleteSelectedListItems();
 assertTrue(listItemDeleterMock.deleteListItemsCallCount > 0);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithoutListItemDeleter() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.deleteSelectedListItems();
 fail("MacawException expected");
 }

 @Test
 public void testSelectListItems() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemSelector listItemSelectorMock = mock(DisplayableListItemSelector.class);
 when(listItemSelectorMock.selectListItems(eq("Test List Owner Name"), anyList())).thenReturn(true);
 displayableListPanel.setDisplayableListItemSelector(listItemSelectorMock);
 DisplayableListParentForm parentFormMock = mock(DisplayableListParentForm.class);
 Displayable selectedListItem1 = new Displayable();
 Displayable selectedListItem2 = new Displayable();
 displayableListPanel.displayableList.add(selectedListItem1);
 displayableListPanel.displayableList.add(selectedListItem2);
 displayableListPanel.displayableList.setSelectedItems(new Object[]{selectedListItem1});
 displayableListPanel.selectListItems();
 assertTrue(listItemSelectorMock.selectListItemsCallCount > 0);
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsWithoutListItemSelector() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.selectListItems();
 fail("MacawException expected");
 }

 @Test
 public void testUpdateButtonStates() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 when(displayableListPanel.displayableList.isEmpty()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 when(displayableListPanel.displayableList.isEmpty()).thenReturn(false);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", false, true);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 displayableListPanel.save();
 assertEquals(displayableListPanel.displayableList.getAllItems(), displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 ArrayList<Displayable> allListItems = displayableListPanel.getAllListItems();
 assertEquals(allListItems, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSetListOwnerName() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 displayableListPanel.setListOwnerName("Test List Owner Name");
 assertEquals("Test List Owner Name", displayableListPanel.listOwnerName);
 }

 @After
 public void tearDown() {
 frame.dispose();
 }
}