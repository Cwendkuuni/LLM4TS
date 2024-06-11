package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.Log;
import static macaw.system.MacawMessages.getMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListParentForm parentForm;
 private SessionProperties sessionProperties;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;
 private JPanel panel;
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
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 log = Mockito.mock(Log.class);
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 displayableList = Mockito.mock(DisplayableList.class);
 select = Mockito.mock(JButton.class);
 add = Mockito.mock(JButton.class);
 edit = Mockito.mock(JButton.class);
 view = Mockito.mock(JButton.class);
 delete = Mockito.mock(JButton.class);
 listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List", true);
 displayableListPanel.init(sessionProperties, parentForm, "Displayable List", true, true);
 displayableListPanel.buildUI();
 displayableListPanel.setPanel(panel);
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.setNewRecord(false);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 }

 @Test
 public void testConstructorWithAllowRecordEditingFalse() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List", true);
 displayableListPanel.init(sessionProperties, parentForm, "Displayable List", true, false);
 assertNull(displayableListPanel.getEdit());
 }

 @Test
 public void testSetPanel() {
 assertNull(displayableListPanel.getPanel());
 displayableListPanel.buildUI();
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testAddListItem() {
 Mockito.when(listItemAdder.addItems(displayableListPanel.listOwnerName, displayableList)).thenReturn(new ArrayList<>());
 displayableListPanel.addListItem();
 Mockito.verify(displayableList).commitChanges();
 Mockito.verify(listItemAdder).addItems(displayableListPanel.listOwnerName, displayableList);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 displayableListPanel.listItemAdder = null;
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 Mockito.when(listItemEditor.editListItem(displayableListPanel.listOwnerName, Mockito.any(Displayable.class), Mockito.any(DisplayableList.class))).thenReturn(new Displayable());
 Mockito.when(displayableList.getSelectedItem()).thenReturn(Mockito.any(Displayable.class));
 displayableListPanel.editSelectedListItem();
 Mockito.verify(displayableList).commitChanges();
 Mockito.verify(listItemEditor).editListItem(displayableListPanel.listOwnerName, Mockito.any(Displayable.class), Mockito.any(DisplayableList.class));
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 displayableListPanel.listItemEditor = null;
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 Mockito.when(listItemViewer.viewListItem(Mockito.any(Displayable.class))).thenReturn(new Object());
 Mockito.when(displayableList.getSelectedItem()).thenReturn(Mockito.any(Displayable.class));
 displayableListPanel.viewSelectedListItem();
 Mockito.verify(listItemViewer).viewListItem(Mockito.any(Displayable.class));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 displayableListPanel.listItemViewer = null;
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 Mockito.when(displayableList.commitChanges()).thenReturn(true);
 Mockito.when(listItemDeleter.deleteListItems(displayableListPanel.listOwnerName, displayableList)).thenReturn(new ArrayList<>());
 Mockito.when(JOptionPane.showConfirmDialog(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
 displayableListPanel.deleteSelectedListItems();
 Mockito.verify(displayableList).commitChanges();
 Mockito.verify(listItemDeleter).deleteListItems(displayableListPanel.listOwnerName, displayableList);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 displayableListPanel.listItemDeleter = null;
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 Mockito.when(listItemSelector.selectListItems(displayableListPanel.listOwnerName, displayableList)).thenReturn(new ArrayList<>());
 displayableListPanel.selectListItems();
 Mockito.verify(displayableList).commitChanges();
 Mockito.verify(listItemSelector).selectListItems(displayableListPanel.listOwnerName, displayableList);
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.isNewRecord = false;
 displayableList.setEmpty(false);
 listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 displayableListPanel.updateButtonStates();
 Mockito.verify(select).setEnabled(true);
 Mockito.verify(add).setEnabled(true);
 Mockito.verify(edit).setEnabled(true);
 Mockito.verify(view).setEnabled(true);
 Mockito.verify(delete).setEnabled(true);
 }

 @Test
 public void testUpdateButtonStatesWithEmptyList() {
 displayableListPanel.isNewRecord = false;
 displayableList.setEmpty(true);
 displayableListPanel.updateButtonStates();
 Mockito.verify(select).setEnabled(false);
 Mockito.verify(add).setEnabled(false);
 Mockito.verify(edit).setEnabled(false);
 Mockito.verify(view).setEnabled(false);
 Mockito.verify(delete).setEnabled(false);
 }

 @Test
 public void testUpdateButtonStatesWithNewRecord() {
 displayableListPanel.isNewRecord = true;
 displayableList.setEmpty(false);
 listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 displayableListPanel.updateButtonStates();
 Mockito.verify(select).setEnabled(false);
 Mockito.verify(add).setEnabled(true);
 Mockito.verify(edit).setEnabled(false);
 Mockito.verify(view).setEnabled(false);
 Mockito.verify(delete).setEnabled(false);
 }
}