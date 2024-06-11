package macaw.util;

import static org.junit.Assert.*;
import org.junit.Test;
import static macaw.util.MacawMessages.getMessage;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemDeleter listItemDeleter;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 parentForm = mock(DisplayableListParentForm.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 listItemSelector = mock(DisplayableListItemSelector.class);
 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 }

 @Test
 public void testInit() {
 assertEquals("Displayable List Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isNewRecord());
 assertNull(displayableListPanel.getPanel());
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.addListItem();
 verify(listItemAdder).addItems(displayableListPanel.getListOwnerName(), displayableListPanel.getDisplayableList());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel.addListItem();
 fail("Add list item without DisplayableListItemAdder should throw MacawException");
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 when(listItemEditor.editListItem(displayableListPanel.getListOwnerName(), selectedListItem, displayableListPanel.getDisplayableList())).thenReturn(selectedListItem);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.editSelectedListItem();
 // Verify that editListItem method was called
 verify(listItemEditor).editListItem(displayableListPanel.getListOwnerName(), displayableListPanel.getDisplayableList().getSelectedItem(), displayableListPanel.getDisplayableList());
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 displayableListPanel.editSelectedListItem();
 fail("Edit list item without DisplayableListItemEditor should throw MacawException");
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 when(listItemViewer.viewListItem(selectedListItem)).thenReturn(null);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 displayableListPanel.viewSelectedListItem();
 // Verify that viewListItem method was called
 verify(listItemViewer).viewListItem(displayableListPanel.getDisplayableList().getSelectedItem());
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 displayableListPanel.viewSelectedListItem();
 fail("View list item without DisplayableListItemViewer should throw MacawException");
 }

 @Test
 public void testDeleteSelectedListItems() {
 String dialogTitle = getMessage("general.warning.title");
 String warningMessage = getMessage("general.warning.deletionIsPermanent");
 when(JOptionPane.showConfirmDialog(eq(null), eq(warningMessage), eq(dialogTitle), eq(0), eq(2))).thenReturn(JOptionPane.YES_OPTION);
 displayableListPanel.deleteSelectedListItems();
 verify(listItemDeleter).deleteListItems(displayableListPanel.getListOwnerName(), displayableListPanel.getDisplayableList());
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter() {
 displayableListPanel.deleteSelectedListItems();
 fail("Delete list items without DisplayableListItemDeleter should throw MacawException");
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();
 // Verify that selectListItems method was called
 verify(listItemSelector).selectListItems(displayableListPanel.getListOwnerName(), displayableListPanel.getDisplayableList());
 }

 @Test
 public void testUpdateButtonStates() {
 DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.isEmpty()).thenReturn(false);
 when(displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.updateButtonStates();
 // Verify that buttons states were updated correctly
 assertTrue(displayableListPanel.isNewRecord());
 assertTrue(displayableListPanel.getAdd().isEnabled());
 assertTrue(displayableListPanel.getSelect().isEnabled());
 assertTrue(displayableListPanel.getEdit().isEnabled());
 assertTrue(displayableListPanel.getDelete().isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithNoSelection() {
 DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.isEmpty()).thenReturn(false);
 when(displayableList.itemsAreSelected()).thenReturn(false);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.updateButtonStates();
 // Verify that buttons states were updated correctly
 assertFalse(displayableListPanel.isNewRecord());
 assertFalse(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getSelect().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithNoItems() {
 DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.isEmpty()).thenReturn(true);
 when(displayableList.itemsAreSelected()).thenReturn(false);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.updateButtonStates();
 // Verify that buttons states were updated correctly
 assertFalse(displayableListPanel.isNewRecord());
 assertFalse(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getSelect().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithNewRecord() {
 DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.isEmpty()).thenReturn(false);
 when(displayableList.itemsAreSelected()).thenReturn(false);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.setNewRecord(true);
 displayableListPanel.updateButtonStates();
 // Verify that buttons states were updated correctly
 assertFalse(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 assertFalse(displayableListPanel.getSelect().isEnabled());
 }
}