import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import macaw.util.DisplayableListPanel;
import macaw.businessLayer.DisplayableListItemAdder;
import macaw.businessLayer.DisplayableListItemEditor;
import macaw.businessLayer.DisplayableListItemDeleter;
import macaw.businessLayer.DisplayableListItemSelector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListParentForm parentForm;
 private SessionProperties sessionProperties;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemDeleter listItemDeleter;
 private DisplayableListItemSelector listItemSelector;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 displayableList = mock(DisplayableList.class);
 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);
 listItemSelector = mock(DisplayableListItemSelector.class);

 when(sessionProperties.getLog()).thenReturn(mock(Log.class));
 when(userInterfaceFactory.createButton(anyString())).thenReturn(mock(JButton.class));
 when(userInterfaceFactory.createPanel()).thenReturn(mock(JPanel.class));
 when(userInterfaceFactory.createLabel(anyString())).thenReturn(mock(JLabel.class));
 when(userInterfaceFactory.getListScrollPane(any(JList.class))).thenReturn(mock(JScrollPane.class));
 when(userInterfaceFactory.createGridBagConstraints()).thenReturn(mock(GridBagConstraints.class));

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 displayableListPanel.init(sessionProperties, parentForm, "Displayable List Panel", true, true);
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 }

 @Test
 public void testInit() {
 // Testing init method with allowWriteAccess and allowRecordEditing set to true
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 displayableListPanel.init(sessionProperties, parentForm, "Displayable List Panel", true, true);

 verify(parentForm, times(1)).commitChanges();
 verifyNoMoreInteractions(parentForm);

 // Testing init method with allowWriteAccess set to false and allowRecordEditing set to false
 DisplayableListPanel displayableListPanel2 = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", false, false);
 displayableListPanel2.init(sessionProperties, parentForm, "Displayable List Panel", false, false);

 verify(parentForm, times(1)).commitChanges();
 verifyNoMoreInteractions(parentForm);
 }

 @Test
 public void testAddListItem() {
 // Testing addListItem method with listItemAdder not null
 doReturn(true).when(listItemAdder).addItems(eq("Displayable List"), eq(displayableList));
 displayableListPanel.addListItem();

 verify(parentForm, times(1)).commitChanges();
 verify(listItemAdder, times(1)).addItems(eq("Displayable List"), eq(displayableList));

 // Testing addListItem method with listItemAdder null
 displayableListPanel.listItemAdder = null;
 displayableListPanel.addListItem();

 verify(parentForm, times(1)).commitChanges();
 verifyNoInteractions(listItemAdder);
 }

 @Test
 public void testEditSelectedListItem() {
 // Testing editSelectedListItem method with listItemEditor not null
 doReturn("New Displayable Name").when(listItemEditor).editListItem(eq("Displayable List"), any(Displayable.class), eq(displayableList));
 Displayable selectedListItem = mock(Displayable.class);
 displayableListPanel.displayableList.setSelectedValue(selectedListItem);
 displayableListPanel.editSelectedListItem();

 verify(parentForm, times(1)).commitChanges();
 verify(listItemEditor, times(1)).editListItem(eq("Displayable List"), eq(selectedListItem), eq(displayableList));

 // Testing editSelectedListItem method with listItemEditor null
 displayableListPanel.listItemEditor = null;
 displayableListPanel.editSelectedListItem();

 verify(parentForm, times(1)).commitChanges();
 verifyNoInteractions(listItemEditor);
 }

 @Test
 public void testViewSelectedListItem() {
 // Testing viewSelectedListItem method with listItemViewer not null
 Displayable selectedListItem = mock(Displayable.class);
 displayableListPanel.displayableList.setSelectedValue(selectedListItem);
 doNothing().when(listItemViewer).viewListItem(eq(selectedListItem));
 displayableListPanel.viewSelectedListItem();

 verify(parentForm, times(1)).commitChanges();
 verify(listItemViewer, times(1)).viewListItem(eq(selectedListItem));

 // Testing viewSelectedListItem method with listItemViewer null
 displayableListPanel.listItemViewer = null;
 displayableListPanel.viewSelectedListItem();

 verify(parentForm, times(1)).commitChanges();
 verifyNoInteractions(listItemViewer);
 }

 @Test
 public void testDeleteSelectedListItems() {
 // Testing deleteSelectedListItems method with listItemDeleter not null and user confirming deletion
 doReturn(true).when(listItemDeleter).deleteListItems(eq("Displayable List"), eq(displayableList));
 JOptionPane mockOptionPane = mock(JOptionPane.class);
 doReturn(mockOptionPane).when(parentForm).createDialog(any(Component.class), anyString(), anyInt(), anyInt(), any(Object[].class));
 doAnswer(i -> {
 if (i.getArgument(0) == JOptionPane.OK_OPTION) {
 return i.getArgument(1);
 }
 return null;
 }).when(mockOptionPane).showInputDialog(any(Component.class), anyString(), anyString(), anyInt(), anyObject(), anyObject(), anyObject());
 displayableListPanel.displayableList.setSelectedValues(Arrays.asList(new Displayable(), new Displayable()));
 displayableListPanel.deleteSelectedListItems();

 verify(parentForm, times(1)).commitChanges();
 verify(listItemDeleter, times(1)).deleteListItems(eq("Displayable List"), eq(displayableList));

 // Testing deleteSelectedListItems method with listItemDeleter not null and user cancelling deletion
 doReturn(false).when(listItemDeleter).deleteListItems(eq("Displayable List"), eq(displayableList));
 displayableListPanel.displayableList.setSelectedValues(Arrays.asList(new Displayable(), new Displayable()));
 displayableListPanel.deleteSelectedListItems();

 verifyNoInteractions(listItemDeleter);
 }

 @Test
 public void testSelectListItems() {
 // Testing selectListItems method with listItemSelector not null
 doReturn(true).when(listItemSelector).selectListItems(eq("Displayable List"), eq(displayableList));
 displayableListPanel.displayableList.setSelectedValue(null);
 displayableListPanel.selectListItems();

 verify(parentForm, times(1)).commitChanges();
 verify(listItemSelector, times(1)).selectListItems(eq("Displayable List"), eq(displayableList));

 // Testing selectListItems method with listItemSelector null
 displayableListPanel.listItemSelector = null;
 displayableListPanel.selectListItems();

 verifyNoInteractions(listItemSelector);
 }

 @Test
 public void testUpdateButtonStates() {
 // Testing updateButtonStates method with isNewRecord set to false and allowWriteAccess set to true
 displayableListPanel.isNewRecord = false;
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.updateButtonStates();

 verify(displayableList, times(1)).isEmpty();
 verify(listItemDeleter, times(0)).deleteListItems(anyString(), any(DisplayableList.class));
 verify(listItemEditor, times(0)).editListItem(anyString(), any(Displayable.class), any(DisplayableList.class));
 verify(listItemViewer, times(0)).viewListItem(any(Displayable.class));
 verify(listItemAdder, times(0)).addItems(anyString(), any(DisplayableList.class));
 verify(add, times(1)).setEnabled(true);
 verify(select, times(1)).setEnabled(true);
 verify(edit, times(1)).setEnabled(true);
 verify(view, times(1)).setEnabled(true);
 verify(delete, times(1)).setEnabled(true);

 // Testing updateButtonStates method with isNewRecord set to false and allowWriteAccess set to false
 displayableListPanel.isNewRecord = false;
 displayableListPanel.allowWriteAccess = false;
 displayableListPanel.updateButtonStates();

 verify(displayableList, times(1)).isEmpty();
 verify(listItemDeleter, times(0)).deleteListItems(anyString(), any(DisplayableList.class));
 verify(listItemEditor, times(0)).editListItem(anyString(), any(Displayable.class), any(DisplayableList.class));
 verify(listItemViewer, times(1)).viewListItem(any(Displayable.class));
 verify(listItemAdder, times(0)).addItems(anyString(), any(DisplayableList.class));
 verify(add, times(1)).setEnabled(false);
 verify(select, times(1)).setEnabled(true);
 verify(edit, times(0)).setEnabled(false);
 verify(view, times(1)).setEnabled(true);
 verify(delete, times(0)).setEnabled(false);

 // Testing updateButtonStates method with isNewRecord set to true and allowWriteAccess set to true
 displayableListPanel.isNewRecord = true;
 displayableListPanel.allowWriteAccess = true;
 displayableListPanel.updateButtonStates();

 verify(displayableList, times(1)).isEmpty();
 verify(listItemDeleter, times(0)).deleteListItems(anyString(), any(DisplayableList.class));
 verify(listItemEditor, times(0)).editListItem(anyString(), any(Displayable.class), any(DisplayableList.class));
 verify(listItemViewer, times(0)).viewListItem(any(Displayable.class));
 verify(listItemAdder, times(1)).addItems(anyString(), any(DisplayableList.class));
 verify(add, times(1)).setEnabled(false);
 verify(select, times(1)).setEnabled(false);
 verify(edit, times(0)).setEnabled(false);
 verify(view, times(0)).setEnabled(false);
 verify(delete, times(0)).setEnabled(false);
 }

 @Test
 public void testSetDisplayableItems() {
 List<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 masterList.add(new Displayable());
 displayableListPanel.masterList = masterList;
 displayableListPanel.displayableList.setDisplayItems(masterList);
 displayableListPanel.updateButtonStates();

 verify(displayableList, times(1)).setDisplayItems(masterList);
 verify(listItemDeleter, times(1)).deleteListItems(eq("Displayable List"), eq(displayableList));
 verify(listItemEditor, times(0)).editListItem(anyString(), any(Displayable.class), any(DisplayableList.class));
 verify(listItemViewer, times(0)).viewListItem(any(Displayable.class));
 verify(listItemAdder, times(0)).addItems(anyString(), any(DisplayableList.class));
 verify(add, times(0)).setEnabled(false);
 verify(select, times(1)).setEnabled(true);
 verify(edit, times(1)).setEnabled(true);
 verify(view, times(1)).setEnabled(true);
 verify(delete, times(1)).setEnabled(true);
 }

 @Test
 public void testSave() {
 displayableListPanel.masterList.clear();
 List<Displayable> currentList = Arrays.asList(new Displayable(), new Displayable());
 displayableListPanel.displayableList.setDisplayItems(currentList);
 displayableListPanel.save();

 verify(displayableListPanel.masterList, times(1)).clear();
 verify(displayableListPanel.masterList, times(1)).addAll(currentList);
 }

 @Test
 public void testGetAllListItems() {
 List<Displayable> allListItems = Arrays.asList(new Displayable(), new Displayable());
 when(displayableList.getAllItems()).thenReturn(allListItems);
 List<Displayable> listItems = displayableListPanel.getAllListItems();

 verify(displayableList.getAllItems(), times(1)).equals(allListItems);
 verify(listItems, times(1)).equals(allListItems);
 }

 @Test
 public void testSetListOwnerName() {
 String listOwnerName = "Displayable List";
 displayableListPanel.setListOwnerName(listOwnerName);

 verify(displayableListPanel, times(1)).listOwnerName = listOwnerName;
 }
}