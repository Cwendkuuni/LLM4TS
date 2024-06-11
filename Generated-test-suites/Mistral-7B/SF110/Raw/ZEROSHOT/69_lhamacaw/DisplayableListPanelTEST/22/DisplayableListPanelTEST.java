package macaw.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;
 private DisplayableList displayableList;
 private JButton select;
 private JButton add;
 private JButton edit;
 private JButton view;
 private JButton delete;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemViewer listItemViewer;
 private DisplayableListItemDeleter listItemDeleter;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 log = mock(Log.class);
 displayableList = mock(DisplayableList.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 select = new JButton();
 add = new JButton();
 edit = new JButton();
 view = new JButton();
 delete = new JButton();
 displayableListPanel.setDisplayableListItemSelector(listItemSelector = mock(DisplayableListItemSelector.class));
 displayableListPanel.setDisplayableListItemAdder(listItemAdder = mock(DisplayableListItemAdder.class));
 displayableListPanel.setDisplayableListItemEditor(listItemEditor = mock(DisplayableListItemEditor.class));
 displayableListPanel.setDisplayableListItemViewer(listItemViewer = mock(DisplayableListItemViewer.class));
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter = mock(DisplayableListItemDeleter.class));
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.buildUI();
 displayableListPanel.add(select, 1, 1);
 displayableListPanel.add(add, 2, 1);
 displayableListPanel.add(edit, 3, 1);
 displayableListPanel.add(view, 4, 1);
 displayableListPanel.add(delete, 5, 1);
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 }

 @Test
 public void testConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testConstructorWithAllowWriteAccessAndDontAllowRecordEditing() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testConstructorWithDontAllowWriteAccess() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testBuildUI() {
 assertEquals(6, displayableListPanel.getComponentCount());
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertEquals(true, displayableListPanel.isNewRecord);
 }

 @Test
 public void testGetPanel() {
 JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 assertNotNull(displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 assertNotNull(displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 assertNotNull(displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 assertNotNull(displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 assertNotNull(displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 when(listItemAdder.addItems(displayableListPanel.listOwnerName, displayableList)).thenReturn(new ArrayList<Displayable>());
 displayableListPanel.addListItem();
 assertEquals(1, displayableListPanel.displayableList.getAllItems().size());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWhenListItemAdderIsNull() {
 displayableListPanel.listItemAdder = null;
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 when(displayableList.isEmpty()).thenReturn(false);
 when(displayableList.getSelectedItem()).thenReturn(new Displayable());
 when(listItemEditor.editListItem(displayableListPanel.listOwnerName, new Displayable(), displayableList)).thenReturn(new Displayable());
 displayableListPanel.editSelectedListItem();
 assertEquals(1, displayableListPanel.displayableList.getAllItems().size());
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWhenDisplayableListIsEmpty() {
 displayableListPanel.editSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWhenListItemEditorIsNull() {
 displayableListPanel.listItemEditor = null;
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 when(displayableList.isEmpty()).thenReturn(false);
 when(displayableList.getSelectedItem()).thenReturn(new Displayable());
 when(listItemViewer.viewListItem(new Displayable())).thenReturn(null);
 displayableListPanel.viewSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWhenDisplayableListIsEmpty() {
 displayableListPanel.viewSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWhenListItemViewerIsNull() {
 displayableListPanel.listItemViewer = null;
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 when(displayableList.isEmpty()).thenReturn(false);
 when(displayableList.getAllItems().get(0).getItemID()).thenReturn("itemID1");
 when(displayableList.getAllItems().get(1).getItemID()).thenReturn("itemID2");
 when(JOptionPane.showConfirmDialog(null, "Warning message", "Warning title", 0, 2)).thenReturn(0);
 when(listItemDeleter.deleteListItems(displayableListPanel.listOwnerName, displayableList)).thenReturn(Arrays.asList(new Displayable[] { new Displayable(), new Displayable() }));
 displayableListPanel.deleteSelectedListItems();
 assertEquals(0, displayableListPanel.displayableList.getAllItems().size());
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWhenDisplayableListIsEmpty() {
 displayableListPanel.deleteSelectedListItems();
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWhenListItemDeleterIsNull() {
 displayableListPanel.listItemDeleter = null;
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsWhenDisplayableListIsEmpty() {
 displayableListPanel.selectListItems();
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.updateButtonStates();
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(1, displayableListPanel.displayableList.getAllItems().size());
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.save();
 assertEquals(1, masterList.size());
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> allListItems = new ArrayList<>();
 allListItems.add(new Displayable());
 when(displayableList.getAllItems()).thenReturn(allListItems);
 ArrayList<Displayable> result = displayableListPanel.getAllListItems();
 assertEquals(1, result.size());
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test Owner Name");
 assertEquals("Test Owner Name", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformedWhenSelectButtonIsPressed() {
 when(listItemSelector.selectListItems(displayableListPanel.listOwnerName, displayableList)).thenReturn(new ArrayList<>());
 displayableListPanel.actionPerformed(new ActionEvent(select, 0, "Test"));
 }

 @Test
 public void testActionPerformedWhenAddButtonIsPressed() {
 displayableListPanel.actionPerformed(new ActionEvent(add, 0, "Test"));
 }

 @Test
 public void testActionPerformedWhenEditButtonIsPressed() {
 when(displayableList.isEmpty()).thenReturn(true);
 displayableListPanel.actionPerformed(new ActionEvent(edit, 0, "Test"));
 }

 @Test
 public void testActionPerformedWhenViewButtonIsPressed() {
 when(displayableList.isEmpty()).thenReturn(true);
 displayableListPanel.actionPerformed(new ActionEvent(view, 0, "Test"));
 }

 @Test
 public void testActionPerformedWhenDeleteButtonIsPressed() {
 when(displayableList.isEmpty()).thenReturn(true);
 displayableListPanel.actionPerformed(new ActionEvent(delete, 0, "Test"));
 }
}