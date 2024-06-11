import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import macaw.businessLayer.*;
import macaw.system.*;
import macaw.util.*;
import static macaw.util.MacawMessages.*;
import static org.junit.Assert.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;
 private User currentUser;
 private ArrayList<Displayable> masterList;
 private ArrayList<Displayable> masterSourceVariableCollection;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 log = mock(Log.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 currentUser = new User("testUser", "testPassword");
 when(sessionProperties.getLog()).thenReturn(log);
 when(sessionProperties.getProperty("currentUser")).thenReturn(currentUser);
 when(sessionProperties.getUserInterfaceFactory()).thenReturn(userInterfaceFactory);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 masterList = new ArrayList<>();
 masterSourceVariableCollection = new ArrayList<>();
 displayableListPanel.setDisplayableItems(masterList);
 }

 @Test
 public void testInit() {
 assertEquals("Displayable List Panel", displayableListPanel.getPanelTitleText());
 assertFalse(displayableListPanel.isNewRecord());
 verify(parentForm, times(1)).commitChanges();
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord());
 }

 @Test
 public void testGetPanel() {
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector mockListItemSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockListItemSelector);
 verify(mockListItemSelector, times(1)).setListOwnerName("Displayable List Panel");
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder mockListItemAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(mockListItemAdder);
 verify(mockListItemAdder, times(1)).setListOwnerName("Displayable List Panel");
 verify(parentForm, times(1)).commitChanges();
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor mockListItemEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(mockListItemEditor);
 verify(mockListItemEditor, times(1)).setListOwnerName("Displayable List Panel");
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer mockListItemViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(mockListItemViewer);
 verify(mockListItemViewer, times(1)).setListOwnerName("Displayable List Panel");
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter mockListItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(mockListItemDeleter);
 verify(mockListItemDeleter, times(1)).setListOwnerName("Displayable List Panel");
 }

 @Test
 public void testAddListItem() {
 when(userInterfaceFactory.createButton("Add")).thenReturn(mock(JButton.class));
 displayableListPanel.addListItem();
 verify(parentForm, times(1)).commitChanges();
 verify(displayableListPanel.listItemAdder, times(1)).addItems("Displayable List Panel", displayableListPanel.displayableList);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 displayableListPanel.addListItem();
 fail("MacawException expected");
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable mockDisplayable = mock(Displayable.class);
when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(mockDisplayable);
 DisplayableListItemEditor mockListItemEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(mockListItemEditor);
 displayableListPanel.editSelectedListItem();
 verify(parentForm, times(1)).commitChanges();
 verify(mockListItemEditor, times(1)).editListItem("Displayable List Panel", mockDisplayable, displayableListPanel.displayableList);
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 displayableListPanel.editSelectedListItem();
 fail("MacawException expected");
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable mockDisplayable = mock(Displayable.class);
 when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(mockDisplayable);
 DisplayableListItemViewer mockListItemViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(mockListItemViewer);
 displayableListPanel.viewSelectedListItem();
 verify(mockListItemViewer, times(1)).viewListItem(mockDisplayable);
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 displayableListPanel.viewSelectedListItem();
 fail("MacawException expected");
 }

 @Test
 public void testDeleteSelectedListItems() {
 when(userInterfaceFactory.createButton("Delete")).thenReturn(mock(JButton.class));
 when(JOptionPane.showConfirmDialog(null, anyString(), anyString(), 0, 2)).thenReturn(JOptionPane.YES_OPTION);
 displayableListPanel.deleteSelectedListItems();
 verify(parentForm, times(1)).commitChanges();
 verify(displayableListPanel.listItemDeleter, times(1)).deleteListItems("Displayable List Panel", displayableListPanel.displayableList);
 }

 @Test
 public void testSelectListItems() {
 DisplayableListItemSelector mockListItemSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockListItemSelector);
 displayableListPanel.selectListItems();
 verify(mockListItemSelector, times(1)).selectListItems("Displayable List Panel", displayableListPanel.displayableList);
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 masterList.add(new Displayable("Test Displayable 1"));
 masterList.add(new Displayable("Test Displayable 2"));
 when(displayableListPanel.displayableList.isEmpty()).thenReturn(false);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isNewRecord);
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.delete.isEnabled());

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, false);
 masterList.clear();
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isNewRecord);
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
}

 @Test
 public void testSave() {
 when(displayableListPanel.displayableList.getAllItems()).thenReturn(masterList);
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> allListItems = displayableListPanel.getAllListItems();
 assertEquals(masterList, allListItems);
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test List Owner");
 assertEquals("Test List Owner", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformedSelect() {
 when(displayableListPanel.displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.actionPerformed(mock(ActionEvent.class));
 displayableListPanel.selectListItems();
 }

 @Test
 public void testActionPerformedAdd() {
 displayableListPanel.actionPerformed(mock(ActionEvent.class));
 displayableListPanel.addListItem();
 }

 @Test
 public void testActionPerformedEdit() {
 when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(new Displayable("Test Displayable"));
 when(displayableListPanel.displayableList.getAllItems()).thenReturn(masterList);
 displayableListPanel.actionPerformed(mock(ActionEvent.class));
 displayableListPanel.editSelectedListItem();
 assertEquals("Test Displayable", displayableListPanel.displayableList.getSelectedItem().getDisplayName());
 }

 @Test
 public void testActionPerformedView() {
 when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(new Displayable("Test Displayable"));
 when(displayableListPanel.displayableList.getAllItems()).thenReturn(masterList);
 displayableListPanel.actionPerformed(mock(ActionEvent.class));
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testActionPerformedDelete() {
 when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(new Displayable("Test Displayable"));
 when(displayableListPanel.displayableList.getAllItems()).thenReturn(masterList);
 when(JOptionPane.showConfirmDialog(null, anyString(), anyString(), 0, 2)).thenReturn(JOptionPane.YES_OPTION);
 displayableListPanel.actionPerformed(mock(ActionEvent.class));
 displayableListPanel.deleteSelectedListItems();
 assertEquals(masterList.size() - 1, displayableListPanel.displayableList.size());
 }

}