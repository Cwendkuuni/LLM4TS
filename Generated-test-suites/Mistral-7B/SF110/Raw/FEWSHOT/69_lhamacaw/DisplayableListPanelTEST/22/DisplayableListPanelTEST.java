package macaw.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableList displayableList;
 private DisplayableListParentForm parentForm;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;
 private User currentUser;
 private String panelTitleText;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemViewer listItemViewer;
 private DisplayableListItemDeleter listItemDeleter;
 private boolean allowWriteAccess;
 private boolean allowRecordEditing;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 log = Mockito.mock(Log.class);
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 currentUser = Mockito.mock(User.class);
 panelTitleText = "Displayable List Panel";
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 displayableList = Mockito.mock(DisplayableList.class);
 listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.userInterfaceFactory = userInterfaceFactory;
 displayableListPanel.sessionProperties = sessionProperties;
 displayableListPanel.log = log;
 displayableListPanel.parentForm = parentForm;
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.listItemSelector = listItemSelector;
 displayableListPanel.listItemAdder = listItemAdder;
 displayableListPanel.listItemEditor = listItemEditor;
 displayableListPanel.listItemViewer = listItemViewer;
 displayableListPanel.listItemDeleter = listItemDeleter;
 }

 @Test
 public void testPanelTitleText() {
 assertEquals(panelTitleText, displayableListPanel.panelTitleText);
 }

 @Test
 public void testAllowWriteAccess() {
 assertEquals(allowWriteAccess, displayableListPanel.allowWriteAccess);
 }

 @Test
 public void testAllowRecordEditing() {
 assertEquals(allowRecordEditing, displayableListPanel.allowRecordEditing);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertEquals(listItemSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertEquals(listItemAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertEquals(listItemEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertEquals(listItemViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertEquals(listItemDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 try {
 displayableListPanel.addListItem();
 Mockito.verify(parentForm).commitChanges();
 Mockito.verify(listItemAdder).addItems(displayableListPanel.listOwnerName, displayableList);
 } catch (MacawException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 displayableListPanel.allowWriteAccess = false;
 displayableListPanel.addListItem();
 fail("Should throw MacawException for missing addItem method");
 }

 @Test
 public void testEditSelectedListItem() {
 try {
 displayableListPanel.editSelectedListItem();
 Mockito.verify(parentForm).commitChanges();
 } catch (MacawException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 displayableList.itemsAreSelected = () -> true;
 displayableListPanel.allowWriteAccess = false;
 displayableListPanel.allowRecordEditing = false;
 displayableListPanel.editSelectedListItem();
 fail("Should throw MacawException for missing editListItem method");
 }

 @Test
 public void testViewSelectedListItem() {
 try {
 displayableListPanel.viewSelectedListItem();
 Mockito.verify(listItemViewer).viewListItem(displayableList.getSelectedItem());
 } catch (MacawException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 displayableList.itemsAreSelected = () -> true;
 displayableListPanel.viewSelectedListItem();
 fail("Should throw MacawException for missing viewListItem method");
 }

 @Test
 public void testDeleteSelectedListItems() {
 try {
 displayableListPanel.deleteSelectedListItems();
 Mockito.verify(parentForm).commitChanges();
 Mockito.verify(listItemDeleter).deleteListItems(displayableListPanel.listOwnerName, displayableList);
 final int result = Mockito.mock(JOptionPane.class).showConfirmDialog(Mockito.mock(Component.class), Mockito.mock(String.class), "Warning", 0, 2);
 assertEquals(0, result);
 } catch (MacawException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 displayableList.itemsAreSelected = () -> true;
 displayableListPanel.deleteSelectedListItems();
 fail("Should throw MacawException for missing deleteListItems method");
 }

 @Test
 public void testSelectListItems() {
 try {
 displayableListPanel.selectListItems();
 Mockito.verify(listItemSelector).selectListItems(displayableListPanel.listOwnerName, displayableList);
 } catch (MacawException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsWithMissingSelector() {
 displayableListPanel.selectListItems();
 fail("Should throw MacawException for missing selectListItems method");
 }

 @Test
 public void testUpdateButtonStates() {
 Mockito.when(displayableList.isEmpty()).thenReturn(false);
 displayableListPanel.updateButtonStates();
 Mockito.verify(select).setEnabled(true);
 Mockito.verify(add).setEnabled(true);
 Mockito.verify(edit).setEnabled(true);
 Mockito.verify(view).setEnabled(true);
 Mockito.verify(delete).setEnabled(true);

 Mockito.when(displayableList.isEmpty()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 Mockito.verify(select).setEnabled(true);
 Mockito.verify(add).setEnabled(false);
 Mockito.verify(edit).setEnabled(false);
 Mockito.verify(view).setEnabled(false);
 Mockito.verify(delete).setEnabled(false);
 }

 @Test
 public void testUpdateButtonStatesWithNewRecord() {
 displayableListPanel.isNewRecord = true;
 Mockito.when(displayableList.isEmpty()).thenReturn(false);
 displayableListPanel.updateButtonStates();
 Mockito.verify(select).setEnabled(true);
 Mockito.verify(add).setEnabled(false);
 Mockito.verify(edit).setEnabled(false);
 Mockito.verify(view).setEnabled(false);
 Mockito.verify(delete).setEnabled(false);
 }

 @Test
 public void testSetDisplayableItems() {
 List<Displayable> displayables = new ArrayList<Displayable>();
 displayables.add(Mockito.mock(Displayable.class));
 displayableListPanel.setDisplayableItems(displayables);
 assertEquals(displayables, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 displayableListPanel.save();
 assertEquals(displayableListPanel.displayableList.getAllItems(), displayableListPanel.masterList);
 }
}