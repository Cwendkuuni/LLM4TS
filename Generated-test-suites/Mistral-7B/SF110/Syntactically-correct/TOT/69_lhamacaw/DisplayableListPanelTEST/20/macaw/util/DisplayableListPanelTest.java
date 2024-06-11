import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import macaw.util.DisplayableListPanel;
import macaw.businessLayer.*; //For mocking DisplayableListItemSelector, DisplayableListItemAdder, DisplayableListItemEditor, DisplayableListItemDeleter, DisplayableList, Displayable
import static macaw.businessLayer.MacawMessages.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableList displayableListMock;
 private DisplayableListItemSelector listItemSelectorMock;
 private DisplayableListItemAdder listItemAdderMock;
 private DisplayableListItemEditor listItemEditorMock;
 private DisplayableListItemDeleter listItemDeleterMock;
 private SessionProperties sessionPropertiesMock;
 private UserInterfaceFactory userInterfaceFactoryMock;
 private DisplayableListParentForm parentFormMock;

 @Before
 public void setUp() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel Title", true, false);
 displayableListMock = mock(DisplayableList.class);
 listItemSelectorMock = mock(DisplayableListItemSelector.class);
 listItemAdderMock = mock(DisplayableListItemAdder.class);
 listItemEditorMock = mock(DisplayableListItemEditor.class);
 listItemDeleterMock = mock(DisplayableListItemDeleter.class);
 when(displayableListMock.isEmpty()).thenReturn(false); //Assuming there is at least one item in the list for testing purposes
 displayableListPanel.setDisplayableList(displayableListMock);
 displayableListPanel.setDisplayableListItemSelector(listItemSelectorMock);
 displayableListPanel.setDisplayableListItemAdder(listItemAdderMock);
 displayableListPanel.setDisplayableListItemEditor(listItemEditorMock);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleterMock);
 userInterfaceFactoryMock = mock(UserInterfaceFactory.class);
 sessionPropertiesMock = new SessionProperties();
 sessionPropertiesMock.setLog(new Log());
 sessionPropertiesMock.setUserInterfaceFactory(userInterfaceFactoryMock);
 parentFormMock = mock(DisplayableListParentForm.class);
 }

 @Test
 public void testInit() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel Title", true, false);
 verify(userInterfaceFactoryMock).createButton(eq(MacawMessages.getMessage("general.buttons.select"))).times(1);
 verify(userInterfaceFactoryMock).createButton(eq(MacawMessages.getMessage("general.buttons.add"))).times(1);
 verify(userInterfaceFactoryMock).createButton(eq(MacawMessages.getMessage("general.buttons.edit"))).times(0);
 verify(userInterfaceFactoryMock).createButton(eq(MacawMessages.getMessage("general.buttons.view"))).times(0);
 verify(userInterfaceFactoryMock).createButton(eq(MacawMessages.getMessage("general.buttons.delete"))).times(0);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertEquals(true, displayableListPanel.isNewRecord);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelectorMock);
 verify(displayableListPanel).setListItemSelector(eq(listItemSelectorMock));
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdderMock);
 verify(displayableListPanel).setListItemAdder(eq(listItemAdderMock));
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditorMock);
 verify(displayableListPanel).setListItemEditor(eq(listItemEditorMock));
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewerMock);
 verify(displayableListPanel).setListItemViewer(eq(listItemViewerMock));
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleterMock);
 verify(displayableListPanel).setListItemDeleter(eq(listItemDeleterMock));
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 verify(parentFormMock).commitChanges();
 verify(listItemAdderMock).addItems(eq("Test Owner Name"), eq(displayableListMock));
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutListItemAdder() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel Title", true, false);
 displayableListPanel.addListItem();
 fail("MacawException should be thrown when listItemAdder is null");
 }

 @Test
 public void testEditSelectedListItem() {
 displayableListPanel.editSelectedListItem();
 verify(parentFormMock).commitChanges();
 verify(listItemEditorMock).editListItem(eq("Test Owner Name"), argThat(displayable -> displayable.getDisplayName().equals("Old Display Name")), eq(displayableListMock), eq(displayableListMock));
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithoutListItemEditor() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel Title", true, false);
 displayableListPanel.editSelectedListItem();
 fail("MacawException should be thrown when listItemEditor is null");
 }

 @Test
 public void testViewSelectedListItem() {
 displayableListPanel.viewSelectedListItem();
 verify(listItemViewerMock).viewListItem(argThat(displayable -> displayable.getDisplayName().equals(displayableListMock.getSelectedItem().getDisplayName())));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithoutListItemViewer() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel Title", true, false);
 displayableListPanel.viewSelectedListItem();
 fail("MacawException should be thrown when listItemViewer is null");
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableListPanel.deleteSelectedListItems();
 verify(parentFormMock).commitChanges();
 verify(listItemDeleterMock).deleteListItems(eq("Test Owner Name"), eq(displayableListMock));
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithoutListItemDeleter() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel Title", true, false);
 displayableListPanel.deleteSelectedListItems();
 fail("MacawException should be thrown when listItemDeleter is null");
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();
 verify(listItemSelectorMock).selectListItems(eq("Test Owner Name"), eq(displayableListMock));
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsWithoutListItemSelector() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel Title", true, false);
 displayableListPanel.selectListItems();
 fail("MacawException should be thrown when listItemSelector is null");
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.updateButtonStates();
 //Add assertions for different button states based on allowWriteAccess, isNewRecord, and displayableList size
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSave() {
 displayableListPanel.save();
 assertTrue(displayableListPanel.masterList.size() > 0);
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> allListItems = displayableListPanel.getAllListItems();
 assertEquals(displayableListMock.getAllItems().size(), allListItems.size());
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test Owner Name");
 assertEquals("Test Owner Name", displayableListPanel.listOwnerName);
 }

}