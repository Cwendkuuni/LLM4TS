package macaw.util.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import macaw.businessLayer.Displayable;
import macaw.businessLayer.DisplayableList;
import macaw.businessLayer.DisplayableListItemEditor;
import macaw.businessLayer.DisplayableListItemSelector;
import macaw.businessLayer.DisplayableListItemAdder;
import macaw.businessLayer.DisplayableListItemDeleter;
import macaw.businessLayer.DisplayableListItemViewer;
import macaw.system.Log;
import macaw.system.SessionProperties;
import macaw.system.UserInterfaceFactory;
import macaw.util.DisplayableListPanel;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableListParentForm parentForm;
 private Log log;
 private DisplayableList displayableList;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemDeleter listItemDeleter;
 private DisplayableListItemViewer listItemViewer;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 parentForm = mock(DisplayableListParentForm.class);
 log = mock(Log.class);
 displayableList = mock(DisplayableList.class);
 listItemSelector = mock(DisplayableListItemSelector.class);
 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);
 listItemViewer = mock(DisplayableListItemViewer.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);

 when(sessionProperties.getLog()).thenReturn(log);
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(userInterfaceFactory);
 when(userInterfaceFactory.createButton(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(userInterfaceFactory.createLabel(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(userInterfaceFactory.createPanel()).thenAnswer(invocation -> invocation.getArguments()[0]);

 doReturn(displayableList).when(displayableListPanel).displayableList;
 doReturn(listItemSelector).when(displayableListPanel).listItemSelector;
 doReturn(listItemAdder).when(displayableListPanel).listItemAdder;
 doReturn(listItemEditor).when(displayableListPanel).listItemEditor;
 doReturn(listItemDeleter).when(displayableListPanel).listItemDeleter;
 doReturn(listItemViewer).when(displayableListPanel).listItemViewer;
 }

 @Test
 public void testDisplayableListPanelCreation() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertEquals(listItemSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertEquals(listItemAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertEquals(listItemEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertEquals(listItemDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertEquals(listItemViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testGetPanel() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetNewRecord() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord);
 }

 @Test
 public void testAddListItem() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.addListItem();
 verify(displayableList, times(1)).addItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutAdder() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.addListItem();
 fail("Should throw MacawException when listItemAdder is null");
 }

 @Test
 public void testEditSelectedListItem() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 Displayable selectedListItem = mock(Displayable.class);
 doReturn(selectedListItem).when(displayableList).getSelectedItem();
 when(listItemEditor.editListItem(displayableListPanel.listOwnerName, selectedListItem, displayableListPanel.displayableList)).thenAnswer(invocation -> {});
 displayableListPanel.editSelectedListItem();
 verify(listItemEditor, times(1)).editListItem(displayableListPanel.listOwnerName, selectedListItem, displayableListPanel.displayableList);
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithoutEditor() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 Displayable selectedListItem = mock(Displayable.class);
 doReturn(selectedListItem).when(displayableList).getSelectedItem();
 displayableListPanel.editSelectedListItem();
 fail("Should throw MacawException when listItemEditor is null");
 }

 @Test
 public void testViewSelectedListItem() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 Displayable selectedListItem = mock(Displayable.class);
 doReturn(selectedListItem).when(displayableList).getSelectedItem();
 doReturn(listItemViewer).when(displayableListPanel).listItemViewer;
 displayableListPanel.viewSelectedListItem();
 verify(listItemViewer, times(1)).viewListItem(selectedListItem);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithoutViewer() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 Displayable selectedListItem = mock(Displayable.class);
 doReturn(selectedListItem).when(displayableList).getSelectedItem();
 displayableListPanel.viewSelectedListItem();
 fail("Should throw MacawException when listItemViewer is null");
 }

 @Test
 public void testDeleteSelectedListItems() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 Displayable selectedListItem1 = mock(Displayable.class);
 Displayable selectedListItem2 = mock(Displayable.class);
 doReturn(Arrays.asList(selectedListItem1, selectedListItem2)).when(displayableList).getAllItems();
 doReturn(new String[]{"item1", "item2"}).when(displayableList).getSelectedItemNames();
 when(listItemDeleter.deleteListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList)).thenAnswer(invocation -> {});
 displayableListPanel.deleteSelectedListItems();
 verify(listItemDeleter, times(1)).deleteListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithoutDeleter() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 Displayable selectedListItem1 = mock(Displayable.class);
 Displayable selectedListItem2 = mock(Displayable.class);
 doReturn(Arrays.asList(selectedListItem1, selectedListItem2)).when(displayableList).getAllItems();
 doReturn(new String[]{"item1", "item2"}).when(displayableList).getSelectedItemNames();
 displayableListPanel.deleteSelectedListItems();
 fail("Should throw MacawException when listItemDeleter is null");
 }

 @Test
 public void testSelectListItems() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 Displayable listItem1 = mock(Displayable.class);
 Displayable listItem2 = mock(Displayable.class);
 doReturn(Arrays.asList(listItem1, listItem2)).when(displayableList).getAllItems();
 doReturn(new String[]{"item1", "item2"}).when(displayableList).getSelectedItemNames();
 doReturn(listItemSelector).when(displayableListPanel).listItemSelector;
 displayableListPanel.selectListItems();
 verify(listItemSelector, times(1)).selectListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsWithoutSelector() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 Displayable listItem1 = mock(Displayable.class);
 Displayable listItem2 = mock(Displayable.class);
 doReturn(Arrays.asList(listItem1, listItem2)).when(displayableList).getAllItems();
 doReturn(new String[]{"item1", "item2"}).when(displayableList).getSelectedItemNames();
 displayableListPanel.selectListItems();
 fail("Should throw MacawException when listItemSelector is null");
 }

 @Test
 public void testUpdateButtonStates() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 ArrayList<Displayable> masterList = new ArrayList<Displayable>();
 masterList.add(new Displayable("item1"));
 masterList.add(new Displayable("item2"));
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(false, displayableListPanel.add.isEnabled());
 assertEquals(false, displayableListPanel.delete.isEnabled());
 assertEquals(false, displayableListPanel.edit.isEnabled());
 assertEquals(false, displayableListPanel.view.isEnabled());
 assertEquals(false, displayableListPanel.select.isEnabled());

 when(displayableList.isEmpty()).thenReturn(false);
 displayableListPanel.updateButtonStates();
 assertEquals(true, displayableListPanel.add.isEnabled());
 assertEquals(true, displayableListPanel.select.isEnabled());
 assertEquals(true, displayableListPanel.delete.isEnabled());
 assertEquals(true, displayableListPanel.edit.isEnabled());
 assertEquals(true, displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSave() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 ArrayList<Displayable> masterList = new ArrayList<Displayable>();
 masterList.add(new Displayable("item1"));
 masterList.add(new Displayable("item2"));
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }
}