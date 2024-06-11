package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private JButton select;
 private JButton add;
 private JButton edit;
 private JButton view;
 private JButton delete;
 private ArrayList<Displayable> masterList;
 private ArrayList<Displayable> masterSourceVariableCollection;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 log = mock(Log.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 displayableList = mock(DisplayableList.class);
 masterList = new ArrayList<>();
 masterSourceVariableCollection = new ArrayList<>();
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 select = mock(JButton.class);
 add = mock(JButton.class);
 edit = mock(JButton.class);
 view = mock(JButton.class);
 delete = mock(JButton.class);

 doReturn(select).when(displayableListPanel).getSelect();
 doReturn(add).when(displayableListPanel).getAdd();
 doReturn(edit).when(displayableListPanel).getEdit();
 doReturn(view).when(displayableListPanel).getView();
 doReturn(delete).when(displayableListPanel).getDelete();

 doReturn(displayableListPanel).when(userInterfaceFactory).createButton(anyString());
 doReturn(displayableListPanel).when(userInterfaceFactory).createPanel();
 doReturn(displayableListPanel).when(displayableListPanel).getPanel();
 }

 @Test
 public void testConstructorWithAllowWriteAccess() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 assertTrue(displayableListPanel.isNewRecord());
 }

 @Test
 public void testConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertFalse(displayableListPanel.isNewRecord());
 }

 @Test
 public void testInit() {
 when(sessionProperties.getLog()).thenReturn(log);
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);
 verify(parentForm, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testInitWithMissingAdder() {
 when(sessionProperties.getLog()).thenReturn(log);
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true));
 doNothing().when(displayableListPanel).commitChanges();
 doThrow(new MacawException("Missing Display List Adder")).when(parentForm).commitChanges();
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);
 }

 @Test(expected = MacawException.class)
 public void testInitWithMissingEditor() {
 when(sessionProperties.getLog()).thenReturn(log);
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true));
 doNothing().when(displayableListPanel).commitChanges();
 doThrow(new MacawException("Missing Display List Editor")).when(parentForm).commitChanges();
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, false);
 }

 @Test
 public void testBuildUI() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.buildUI();
 verify(parentForm, times(1)).commitChanges();
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord());
}

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertEquals(listItemSelector, displayableListPanel.listItemSelector);
}

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertEquals(listItemAdder, displayableListPanel.listItemAdder);
}

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertEquals(listItemEditor, displayableListPanel.listItemEditor);
}

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertEquals(listItemViewer, displayableListPanel.listItemViewer);
}

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertEquals(listItemDeleter, displayableListPanel.listItemDeleter);
}

 @Test
 public void testAddListItem() {
 doReturn(displayableList).when(displayableListPanel).displayableList;
 doReturn(log).when(sessionProperties).getLog();
 displayableListPanel.addListItem();
 verify(parentForm, times(1)).commitChanges();
 verify(displayableList).addItems(anyString(), eq(displayableList));
}

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 doReturn(displayableList).when(displayableListPanel).displayableList;
 doReturn(log).when(sessionProperties).getLog();
 doNothing().when(parentForm).commitChanges();
 doThrow(new MacawException("Missing Display List Adder")).when(displayableList).addItems(anyString(), any(DisplayableList.class));
 displayableListPanel.addListItem();
}

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 doReturn(displayableList).when(displayableListPanel).displayableList;
 doReturn(selectedListItem).when(displayableList).getSelectedItem();
 doReturn(log).when(sessionProperties).getLog();
 displayableListPanel.editSelectedListItem();
 verify(parentForm, times(1)).commitChanges();
 verify(displayableList).editListItem(anyString(), eq(selectedListItem), any(DisplayableList.class));
}

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 doReturn(displayableList).when(displayableListPanel).displayableList;
 doReturn(selectedListItem).when(displayableList).getSelectedItem();
 doReturn(log).when(sessionProperties).getLog();
 doNothing().when(parentForm).commitChanges();
 doThrow(new MacawException("Missing Display List Editor")).when(displayableList).editListItem(anyString(), any(Displayable.class), any(DisplayableList.class));
 displayableListPanel.editSelectedListItem();
}

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 doReturn(displayableList).when(displayableListPanel).displayableList;
 doReturn(selectedListItem).when(displayableList).getSelectedItem();
 doReturn(mock(DisplayableListItemViewer.class)).when(displayableListPanel).listItemViewer;
 doReturn(log).when(sessionProperties).getLog();
 displayableListPanel.viewSelectedListItem();
 verify(listItemViewer).viewListItem(eq(selectedListItem));
}

 @Test
 public void testDeleteSelectedListItems() {
 Displayable selectedListItem = mock(Displayable.class);
 doReturn(displayableList).when(displayableListPanel).displayableList;
 doReturn(selectedListItem).when(displayableList).getSelectedItem();
 doReturn(log).when(sessionProperties).getLog();
 doReturn(true).when(JOptionPane.showConfirmDialog(any(Component.class), anyString(), anyString(), 0));
 doNothing().when(displayableList).deleteListItems(anyString(), any(DisplayableList.class));
 displayableListPanel.deleteSelectedListItems();
 verify(parentForm, times(1)).commitChanges();
 verify(displayableList).deleteListItems(anyString(), any(DisplayableList.class));
}

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 Displayable selectedListItem = mock(Displayable.class);
 doReturn(displayableList).when(displayableListPanel).displayableList;
 doReturn(selectedListItem).when(displayableList).getSelectedItem();
 doReturn(log).when(sessionProperties).getLog();
 doNothing().when(parentForm).commitChanges();
 doThrow(new MacawException("Missing Display List Deleter")).when(displayableList).deleteListItems(anyString(), any(DisplayableList.class));
 displayableListPanel.deleteSelectedListItems();
}

 @Test
 public void testSelectListItems() {
 doReturn(displayableList).when(displayableListPanel).displayableList;
 doReturn(log).when(sessionProperties).getLog();
 displayableListPanel.selectListItems();
 verify(displayableList).selectListItems(anyString(), any(DisplayableList.class));
}

 @Test(expected = MacawException.class)
 public void testSelectListItemsWithMissingSelector() {
 doReturn(displayableList).when(displayableListPanel).displayableList;
 doReturn(log).when(sessionProperties).getLog();
 doThrow(new MacawException("Missing Display List Selector")).when(displayableList).selectListItems(anyString(), any(DisplayableList.class));
 displayableListPanel.selectListItems();
}

 @Test
 public void testUpdateButtonStates() {
 doReturn(true).when(displayableListPanel).isNewRecord();
 doReturn(true).when(displayableListPanel).allowWriteAccess;
 doReturn(true).when(displayableList).isEmpty();
 doReturn(true).when(displayableListPanel).allowRecordEditing;
 displayableListPanel.updateButtonStates();
 assertFalse(select.isEnabled());
 assertFalse(add.isEnabled());
 assertFalse(edit.isEnabled());
 assertFalse(view.isEnabled());
 assertFalse(delete.isEnabled());
}

 @Test
 public void testUpdateButtonStatesWithNewRecord() {
 doReturn(true).when(displayableListPanel).isNewRecord();
 doReturn(true).when(displayableListPanel).allowWriteAccess;
 doReturn(false).when(displayableList).isEmpty();
 doReturn(true).when(displayableListPanel).allowRecordEditing;
 displayableListPanel.updateButtonStates();
 assertFalse(select.isEnabled());
 assertTrue(add.isEnabled());
 assertFalse(edit.isEnabled());
 assertFalse(view.isEnabled());
 assertFalse(delete.isEnabled());
}

 @Test
 public void testUpdateButtonStatesWithAllowWriteAccessButNoItems() {
 doReturn(false).when(displayableListPanel).isNewRecord();
 doReturn(true).when(displayableListPanel).allowWriteAccess;
 doReturn(true).when(displayableList).isEmpty();
 doReturn(false).when(displayableListPanel).allowRecordEditing;
 displayableListPanel.updateButtonStates();
 assertFalse(select.isEnabled());
 assertFalse(add.isEnabled());
 assertFalse(edit.isEnabled());
 assertFalse(view.isEnabled());
 assertFalse(delete.isEnabled());
}

 @Test
 public void testUpdateButtonStatesWithAllowWriteAccessAndItems() {
 doReturn(false).when(displayableListPanel).isNewRecord();
 doReturn(true).when(displayableListPanel).allowWriteAccess;
 doReturn(false).when(displayableList).isEmpty();
 doReturn(true).when(displayableListPanel).allowRecordEditing;
 displayableListPanel.updateButtonStates();
 assertTrue(select.isEnabled());
 assertTrue(add.isEnabled());
 assertTrue(edit.isEnabled());
 assertTrue(view.isEnabled());
 assertTrue(delete.isEnabled());
}

 @Test
 public void testUpdateButtonStatesWithNoWriteAccess() {
 doReturn(false).when(displayableListPanel).isNewRecord();
 doReturn(false).when(displayableListPanel).allowWriteAccess;
 doReturn(false).when(displayableList).isEmpty();
 doReturn(false).when(displayableListPanel).allowRecordEditing;
 displayableListPanel.updateButtonStates();
 assertFalse(select.isEnabled());
 assertFalse(add.isEnabled());
 assertTrue(edit.isEnabled());
 assertFalse(view.isEnabled());
}

 @Test
 public void testUpdateButtonStatesWithNoWriteAccessButItems() {
 doReturn(false).when(displayableListPanel).isNewRecord();
 doReturn(false).when(displayableListPanel).allowWriteAccess;
 doReturn(false).when(displayableList).isEmpty();
 doReturn(true).when(displayableListPanel).allowRecordEditing;
 displayableListPanel.updateButtonStates();
 assertTrue(select.isEnabled());
 assertTrue(add.isEnabled());
 assertFalse(edit.isEnabled());
 assertTrue(view.isEnabled());
}

 @Test
 public void testSetDisplayableItems() {
 doReturn(displayableList).when(displayableListPanel).displayableList;
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableList.getAllItems());
}

 @Test
 public void testSave() {
 displayableListPanel.save();
 assertEquals(displayableList.getAllItems(), masterList);
}

 @Test
 public void testGetAllListItems() {
 assertEquals(displayableList.getAllItems(), displayableListPanel.getAllListItems());
}

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test Owner");
 assertEquals("Test Owner", displayableListPanel.listOwnerName);
}

 @Test
 public void testActionPerformed() {
 ActionEvent event = mock(ActionEvent.class);
 doReturn("select").when(event).getSource();
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel).selectListItems();
}

 @Test
 public void testActionPerformedWithEditButton() {
 ActionEvent event = mock(ActionEvent.class);
 doReturn("edit").when(event).getSource();
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel).editSelectedListItem();
}

 @Test
 public void testActionPerformedWithViewButton() {
 ActionEvent event = mock(ActionEvent.class);
 doReturn("view").when(event).getSource();
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel).viewSelectedListItem();
}

 @Test
 public void testActionPerformedWithDeleteButton() {
 ActionEvent event = mock(ActionEvent.class);
 doReturn("delete").when(event).getSource();
 displayableListPanel.actionPerformed(event);
 verify(displayableListPanel).deleteSelectedListItems();
}
}