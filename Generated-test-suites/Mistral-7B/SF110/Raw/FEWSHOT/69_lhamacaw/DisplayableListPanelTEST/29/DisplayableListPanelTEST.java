package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import macaw.businessLayer.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemDeleter listItemDeleter;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 displayableList = mock(DisplayableList.class);
 listItemSelector = mock(DisplayableListItemSelector.class);
 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 displayableListPanel.setDisplayableList(displayableList);
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 assertEquals("Test Panel Title", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccessOnly() {
 assertEquals("Test Panel Title", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertFalse(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testInit() {
 verify(sessionProperties, times(2)).getLog();
 verify(parentForm, times(1)).commitChanges();
 verifyZeroInteractions(userInterfaceFactory, displayableList, listItemSelector, listItemAdder, listItemEditor, listItemDeleter);
 }

 @Test
 public void testBuildUI() {
 JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test
 public void testCreateControlButtonPanel() {
 JPanel controlButtonPanel = displayableListPanel.createControlButtonPanel();
 assertNotNull(controlButtonPanel);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 verify(listItemAdder).setEnabled(false);
 verify(listItemDeleter).setEnabled(false);
 verify(listItemEditor).setEnabled(false);
 verify(listItemViewer).setEnabled(false);
 verify(select).setEnabled(false);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 verify(listItemSelector).setListListPanel(displayableListPanel);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 verify(listItemAdder).setDisplayableListItemPanel(displayableListPanel);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 verify(listItemEditor).setDisplayableListItemPanel(displayableListPanel);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 verify(listItemViewer).setDisplayableListItemPanel(displayableListPanel);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 verify(listItemDeleter).setDisplayableListItemPanel(displayableListPanel);
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 verify(parentForm).commitChanges();
 verify(listItemAdder).addItems(eq("Test List Owner Name"), displayableList);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 displayableListPanel.addListItem();
 fail("AddListItem should throw MacawException when listItemAdder is null.");
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.editSelectedListItem();
 verify(parentForm).commitChanges();
 verify(listItemEditor).editListItem(eq("Test List Owner Name"), eq(selectedListItem), displayableList);
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.editSelectedListItem();
 fail("EditSelectedListItem should throw MacawException when listItemEditor is null.");
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 verify(listItemViewer).viewListItem(eq(selectedListItem));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.viewSelectedListItem();
 fail("ViewSelectedListItem should throw MacawException when listItemViewer is null.");
 }

 @Test
 public void testDeleteSelectedListItems() {
 verify(parentForm).commitChanges();
 verify(listItemDeleter).deleteListItems(eq("Test List Owner Name"), displayableList);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 verify(parentForm).commitChanges();
 fail("DeleteSelectedListItems should throw MacawException when listItemDeleter is null.");
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();
 verify(listItemSelector).selectListItems(eq("Test List Owner Name"), displayableList);
 }

 @Test
 public void testUpdateButtonStates() {
 // Test when isNewRecord is true
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());

 // Test when isNewRecord is false and list is empty
 displayableListPanel.setNewRecord(false);
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());

 // Test when isNewRecord is false and list is not empty
 displayableListPanel.getDisplayableList().add(new Displayable("Test Item 1"));
 displayableListPanel.getDisplayableList().add(new Displayable("Test Item 2"));
 assertTrue(displayableListPanel.isAddEnabled());
 assertTrue(displayableListPanel.isSelectEnabled());
 assertTrue(displayableListPanel.isDeleteEnabled());
 if (displayableListPanel.isAllowRecordEditing()) {
 assertTrue(displayableListPanel.isEditEnabled());
 }
 else {
 assertTrue(displayableListPanel.isViewEnabled());
 }
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Item 1"));
 masterList.add(new Displayable("Test Item 2"));
 displayableListPanel.setDisplayableItems(masterList);
 verify(displayableList).setDisplayItems(masterList);
 }

 @Test
 public void testSave() {
 displayableListPanel.save();
 assertEquals(displayableListPanel.getAllListItems().size(), displayableList.getAllItems().size());
 }

 @Test
 public void testGetAllListItems() {
 assertEquals(displayableListPanel.getAllListItems().size(), displayableList.getAllItems().size());
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test List Owner Name");
 assertEquals("Test List Owner Name", displayableListPanel.getListOwnerName());
 }

 @Test
 public void testActionPerformedWithSelectButton() {
 displayableListPanel.actionPerformed(mock(ActionEvent.class));
 verify(listItemSelector).selectListItems(eq("Test List Owner Name"), displayableList);
 }

 @Test
 public void testActionPerformedWithAddButton() {
 displayableListPanel.actionPerformed(mock(ActionEvent.class));
 verify(parentForm).commitChanges();
 verify(listItemAdder).addItems(eq("Test List Owner Name"), displayableList);
 }

 @Test
 public void testActionPerformedWithEditButton() {
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.actionPerformed(mock(ActionEvent.class));
 verify(parentForm).commitChanges();
 verify(listItemEditor).editListItem(eq("Test List Owner Name"), eq(selectedListItem), displayableList);
 }

 @Test
 public void testActionPerformedWithViewButton() {
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 displayableListPanel.actionPerformed(mock(ActionEvent.class));
 verify(listItemViewer).viewListItem(eq(selectedListItem));
 }

 @Test
 public void testActionPerformedWithDeleteButton() {
 verify(parentForm).commitChanges();
 verify(listItemDeleter).deleteListItems(eq("Test List Owner Name"), displayableList);
 }
}