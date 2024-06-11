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
 private ArrayList<Displayable> masterList;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 masterList = new ArrayList<>();
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 }

 @Test
 public void testDisplayableListPanelConstruction() {
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertNotNull(displayableListPanel.getPanel());
 assertNull(displayableListPanel.getListItemSelector());
 assertNull(displayableListPanel.getListItemAdder());
 assertNull(displayableListPanel.getListItemEditor());
 assertNull(displayableListPanel.getListItemViewer());
 assertNull(displayableListPanel.getListItemDeleter());
 assertFalse(displayableListPanel.isNewRecord());
 assertNull(displayableListPanel.getDisplayableList());
 assertNull(displayableListPanel.getSelect());
 assertNull(displayableListPanel.getAdd());
 assertNull(displayableListPanel.getEdit());
 assertNull(displayableListPanel.getView());
 assertNull(displayableListPanel.getDelete());
 }

 @Test
 public void testDisplayableListPanelWithAddButton() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel.getAdd());
 }

 @Test
 public void testDisplayableListPanelWithEditButton() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true, true, null);
 assertNotNull(displayableListPanel.getEdit());
 }

 @Test
 public void testDisplayableListPanelWithViewButton() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true, null, null);
 assertNotNull(displayableListPanel.getView());
 }

 @Test
 public void testDisplayableListPanelWithDeleteButton() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true, null, null, true);
 assertNotNull(displayableListPanel.getDelete());
 }

 @Test
 public void testDisplayableListPanelWithNoWriteAccess() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false, true);
 assertFalse(displayableListPanel.isNewRecord());
 assertFalse(displayableListPanel.allowWriteAccess);
 assertNull(displayableListPanel.getAdd());
 assertNull(displayableListPanel.getDelete());
 }

 @Test
 public void testDisplayableListPanelSetup() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.buildUI();
 assertNotNull(displayableListPanel.getPanel());
 assertNotNull(displayableListPanel.getSelect());
 assertNotNull(displayableListPanel.getAdd());
 assertNotNull(displayableListPanel.getEdit());
 assertNotNull(displayableListPanel.getView());
 assertNotNull(displayableListPanel.getDelete());
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord());
 }

 @Test
 public void testSetDisplayableList() {
 displayableListPanel.setDisplayableList(new DisplayableList(null));
 DisplayableList displayableList = displayableListPanel.getDisplayableList();
 assertNotNull(displayableList);
 }

 @Test
 public void testSelectListItems() {
 JOptionPane.showMessageDialog(new JFrame(), "SelectListItems test");
 DisplayableListItemSelector mockSelector = Mockito.mock(DisplayableListItemSelector.class);
 displayableListPanel.setListItemSelector(mockSelector);
 displayableListPanel.selectListItems();
 verify(mockSelector).selectListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }

 @Test
 public void testAddListItem() {
 JOptionPane.showMessageDialog(new JFrame(), "AddListItem test");
 DisplayableListItemAdder mockAdder = Mockito.mock(DisplayableListItemAdder.class);
 displayableListPanel.setListItemAdder(mockAdder);
 displayableListPanel.addListItem();
 verify(mockAdder).addItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }

 @Test
 public void testEditSelectedListItem() {
 JOptionPane.showMessageDialog(new JFrame(), "EditSelectedListItem test");
 DisplayableListItemEditor mockEditor = Mockito.mock(DisplayableListItemEditor.class);
 displayableListPanel.setListItemEditor(mockEditor);
 Displayable selectedListItem = new Displayable();
 DisplayableListItem selectedListItemDisplayable = new DisplayableListItem(selectedListItem);
 displayableListPanel.displayableList.add(selectedListItemDisplayable);
 displayableListPanel.displayableList.setSelectedItem(selectedListItemDisplayable);
 displayableListPanel.editSelectedListItem();
 verify(mockEditor).editListItem(displayableListPanel.listOwnerName, selectedListItem, displayableListPanel.displayableList);
 }

 @Test
 public void testViewSelectedListItem() {
 JOptionPane.showMessageDialog(new JFrame(), "ViewSelectedListItem test");
 DisplayableListItemViewer mockViewer = Mockito.mock(DisplayableListItemViewer.class);
 displayableListPanel.setListItemViewer(mockViewer);
 Displayable selectedListItem = new Displayable();
 DisplayableListItem selectedListItemDisplayable = new DisplayableListItem(selectedListItem);
 displayableListPanel.displayableList.add(selectedListItemDisplayable);
 displayableListPanel.displayableList.setSelectedItem(selectedListItemDisplayable);
 displayableListPanel.viewSelectedListItem();
 verify(mockViewer).viewListItem(selectedListItem);
 }

 @Test
 public void testDeleteSelectedListItems() {
 JOptionPane.showMessageDialog(new JFrame(), "DeleteSelectedListItems test");
 DisplayableListItemDeleter mockDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel.setListItemDeleter(mockDeleter);
 Displayable selectedListItem1 = new Displayable();
 DisplayableListItem selectedListItemDisplayable1 = new DisplayableListItem(selectedListItem1);
 Displayable selectedListItem2 = new Displayable();
 DisplayableListItem selectedListItemDisplayable2 = new DisplayableListItem(selectedListItem2);
 displayableListPanel.displayableList.add(selectedListItemDisplayable1);
 displayableListPanel.displayableList.add(selectedListItemDisplayable2);
 displayableListPanel.displayableList.setSelectedItem(selectedListItemDisplayable1);
 displayableListPanel.deleteSelectedListItems();
 verify(mockDeleter).deleteListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 }

 @Test
 public void testUpdateButtonStates() {
 JOptionPane.showMessageDialog(new JFrame(), "UpdateButtonStates test");
 DisplayableListItemSelector mockSelector = Mockito.mock(DisplayableListItemSelector.class);
 DisplayableListItemAdder mockAdder = Mockito.mock(DisplayableListItemAdder.class);
 DisplayableListItemEditor mockEditor = Mockito.mock(DisplayableListItemEditor.class);
 DisplayableListItemViewer mockViewer = Mockito.mock(DisplayableListItemViewer.class);
 DisplayableListItemDeleter mockDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel.setListItemSelector(mockSelector);
 displayableListPanel.setListItemAdder(mockAdder);
 displayableListPanel.setListItemEditor(mockEditor);
 displayableListPanel.setListItemViewer(mockViewer);
 displayableListPanel.setListItemDeleter(mockDeleter);
 displayableListPanel.displayableList.add(new DisplayableListItem(new Displayable()));
 displayableListPanel.displayableList.add(new DisplayableListItem(new Displayable()));
 displayableListPanel.displayableList.setSelectedItem(new DisplayableListItem(new Displayable()));
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.getSelect().isEnabled());
 assertTrue(displayableListPanel.getAdd().isEnabled());
 assertTrue(displayableListPanel.getEdit().isEnabled());
 assertTrue(displayableListPanel.getDelete().isEnabled());
 }

 @Test
 public void testUpdateButtonStatesNewRecord() {
 JOptionPane.showMessageDialog(new JFrame(), "UpdateButtonStatesNewRecord test");
 DisplayableListItemSelector mockSelector = Mockito.mock(DisplayableListItemSelector.class);
 DisplayableListItemAdder mockAdder = Mockito.mock(DisplayableListItemAdder.class);
 DisplayableListItemEditor mockEditor = Mockito.mock(DisplayableListItemEditor.class);
 DisplayableListItemViewer mockViewer = Mockito.mock(DisplayableListItemViewer.class);
 DisplayableListItemDeleter mockDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel.setListItemSelector(mockSelector);
 displayableListPanel.setListItemAdder(mockAdder);
 displayableListPanel.setListItemEditor(mockEditor);
 displayableListPanel.setListItemViewer(mockViewer);
 displayableListPanel.setListItemDeleter(mockDeleter);
 displayableListPanel.setNewRecord(true);
 displayableListPanel.displayableList.setSelectedItem(null);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.getSelect().isEnabled());
 assertFalse(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 JOptionPane.showMessageDialog(new JFrame(), "SetDisplayableItems test");
 List<Displayable> masterListItems = Arrays.asList(new Displayable(), new Displayable());
 displayableListPanel.setDisplayableItems(masterListItems);
 assertEquals(masterListItems.size(), displayableListPanel.displayableList.getAllItems().size());
 }

 @Test
 public void testSave() {
 JOptionPane.showMessageDialog(new JFrame(), "Save test");
 displayableListPanel.save();
 assertEquals(displayableListPanel.displayableList.getAllItems().size(), masterList.size());
 }

 @Test
 public void testGetAllListItems() {
 JOptionPane.showMessageDialog(new JFrame(), "GetAllListItems test");
 List<Displayable> allItems = displayableListPanel.getAllListItems();
 assertEquals(displayableListPanel.displayableList.getAllItems().size(), allItems.size());
 }

 @Test
 public void testSetListOwnerName() {
 JOptionPane.showMessageDialog(new JFrame(), "SetListOwnerName test");
 String listOwnerName = "testListOwnerName";
 displayableListPanel.setListOwnerName(listOwnerName);
 assertEquals(listOwnerName, displayableListPanel.listOwnerName);
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 masterList.clear();
 sessionProperties = null;
 parentForm = null;
 }
}