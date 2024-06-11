package macaw.util.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListParentForm parentFormMock;
 private SessionProperties sessionPropertiesMock;
 private UserInterfaceFactory userInterfaceFactoryMock;
 private DisplayableList displayableListMock;
 private Log logMock;

 @Before
 public void setUp() {
 this.parentFormMock = Mockito.mock(DisplayableListParentForm.class);
 this.sessionPropertiesMock = Mockito.mock(SessionProperties.class);
 this.userInterfaceFactoryMock = Mockito.mock(UserInterfaceFactory.class);
 this.displayableListMock = Mockito.mock(DisplayableList.class);
 this.logMock = Mockito.mock(Log.class);

 this.displayableListPanel = new DisplayableListPanel(this.sessionPropertiesMock, this.parentFormMock, "Test Panel", true);
 }

 @Test
 public void testInit(){
 // Test that init sets the correct properties
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testBuildUI(){
 // Test that buildUI creates the correct UI components
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testCreateControlButtonPanel(){
 // Test that createControlButtonPanel creates the correct UI components
 JPanel controlButtonPanel = displayableListPanel.createControlButtonPanel();
 assertTrue(controlButtonPanel.getComponentCount() > 0);
 }

 @Test
 public void testSetNewRecord(){
 // Test that setNewRecord updates the button states correctly
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord());
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());

 displayableListPanel.setNewRecord(false);
 assertFalse(displayableListPanel.isNewRecord());
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertTrue(displayableListPanel.isEditEnabled());
 assertTrue(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());
 }

 @Test
 public void testSetDisplayableListItemSelector(){
 // Test that setDisplayableListItemSelector sets the listItemSelector property correctly
 DisplayableListItemSelector listItemSelectorMock = Mockito.mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelectorMock);
 assertEquals(listItemSelectorMock, displayableListPanel.getListItemSelector());
 }

 @Test
 public void testSetDisplayableListItemAdder(){
 // Test that setDisplayableListItemAdder sets the listItemAdder property correctly
 DisplayableListItemAdder listItemAdderMock = Mockito.mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(listItemAdderMock);
 assertEquals(listItemAdderMock, displayableListPanel.getListItemAdder());
 }

 @Test
 public void testSetDisplayableListItemEditor(){
 // Test that setDisplayableListItemEditor sets the listItemEditor property correctly
 DisplayableListItemEditor listItemEditorMock = Mockito.mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(listItemEditorMock);
 assertEquals(listItemEditorMock, displayableListPanel.getListItemEditor());
 }

 @Test
 public void testSetDisplayableListItemViewer(){
 // Test that setDisplayableListItemViewer sets the listItemViewer property correctly
 DisplayableListItemViewer listItemViewerMock = Mockito.mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(listItemViewerMock);
 assertEquals(listItemViewerMock, displayableListPanel.getListItemViewer());
 }

 @Test
 public void testSetDisplayableListItemDeleter(){
 // Test that setDisplayableListItemDeleter sets the listItemDeleter property correctly
 DisplayableListItemDeleter listItemDeleterMock = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleterMock);
 assertEquals(listItemDeleterMock, displayableListPanel.getListItemDeleter());
 }

 @Test
 public void testAddListItem(){
 // Test that addListItem adds an item to the displayList using the listItemAdder
 Mockito.doReturn(Mockito.mock(Displayable.class)).when(displayableListMock).createNewDisplayable();
 displayableListPanel.setDisplayableList(displayableListMock);
 displayableListPanel.addListItem();
 assertEquals(1, displayableListMock.size());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder(){
 // Test that addListItem throws an error when the listItemAdder is not set
 displayableListPanel.setDisplayableList(displayableListMock);
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem(){
 // Test that editSelectedListItem edits an item in the displayList using the listItemEditor
 Displayable selectedListItemMock = Mockito.mock(Displayable.class);
 Mockito.doReturn(selectedListItemMock).when(displayableListMock).getSelectedItem();
 displayableListPanel.setDisplayableList(displayableListMock);
 displayableListPanel.editSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor(){
 // Test that editSelectedListItem throws an error when the listItemEditor is not set
 displayableListPanel.setDisplayableList(displayableListMock);
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem(){
 // Test that viewSelectedListItem displays an item from the displayList using the listItemViewer
 Displayable selectedListItemMock = Mockito.mock(Displayable.class);
 Mockito.doReturn(selectedListItemMock).when(displayableListMock).getSelectedItem();
 displayableListPanel.setDisplayableList(displayableListMock);
 displayableListPanel.viewSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer(){
 // Test that viewSelectedListItem throws an error when the listItemViewer is not set
 displayableListPanel.setDisplayableList(displayableListMock);
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems(){
 // Test that deleteSelectedListItems deletes items from the displayList using the listItemDeleter
 Mockito.doNothing().when(displayableListMock).deleteItems(Mockito.anyString(), Mockito.anyList());
 displayableListPanel.setDisplayableList(displayableListMock);
 displayableListPanel.deleteSelectedListItems();
 assertEquals(0, displayableListMock.size());
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMissingDeleter(){
 // Test that deleteSelectedListItems throws an error when the listItemDeleter is not set
 displayableListPanel.setDisplayableList(displayableListMock);
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems(){
 // Test that selectListItems selects items in the displayList using the listItemSelector
 Mockito.doNothing().when(displayableListMock).selectItems(Mockito.anyString(), Mockito.anyList());
 displayableListPanel.setDisplayableList(displayableListMock);
 displayableListPanel.selectListItems();
 assertEquals(1, displayableListMock.size());
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsMissingSelector(){
 // Test that selectListItems throws an error when the listItemSelector is not set
 displayableListPanel.setDisplayableList(displayableListMock);
 displayableListPanel.selectListItems();
 }
}