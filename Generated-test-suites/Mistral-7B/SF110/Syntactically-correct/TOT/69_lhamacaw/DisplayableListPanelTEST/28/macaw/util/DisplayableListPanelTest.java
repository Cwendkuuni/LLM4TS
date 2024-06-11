import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import macaw.util.DisplayableListPanel;
import macaw.businessLayer.DisplayableListItemEditor;
import macaw.businessLayer.DisplayableListItemAdder;
import macaw.businessLayer.DisplayableListItemSelector;
import macaw.businessLayer.DisplayableListItemDeleter;
import macaw.businessLayer.DisplayableListParentForm;
import macaw.system.Log;
import macaw.system.SessionProperties;
import macaw.system.UserInterfaceFactory;
import macaw.util.DisplayableList;
import macaw.util.Displayable;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListPanel spyDisplayableListPanel;
 private DisplayableList displayableListMock;
 private DisplayableListItemAdder listItemAdderMock;
 private DisplayableListItemEditor listItemEditorMock;
 private DisplayableListItemSelector listItemSelectorMock;
 private DisplayableListItemDeleter listItemDeleterMock;
 private DisplayableListParentForm parentFormMock;
 private SessionProperties sessionPropertiesMock;
 private UserInterfaceFactory userInterfaceFactoryMock;
 private Log logMock;

 @Before
 public void setUp() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", false, false);
 spyDisplayableListPanel = spy(displayableListPanel);
 displayableListMock = mock(DisplayableList.class);
 listItemAdderMock = mock(DisplayableListItemAdder.class);
 listItemEditorMock = mock(DisplayableListItemEditor.class);
 listItemSelectorMock = mock(DisplayableListItemSelector.class);
 listItemDeleterMock = mock(DisplayableListItemDeleter.class);
 parentFormMock = mock(DisplayableListParentForm.class);
 sessionPropertiesMock = mock(SessionProperties.class);
 userInterfaceFactoryMock = mock(UserInterfaceFactory.class);
 logMock = mock(Log.class);
 when(sessionPropertiesMock.getLog()).thenReturn(logMock);
 when(sessionPropertiesMock.getUserInterfaceFactory()).thenReturn(userInterfaceFactoryMock);
 when(userInterfaceFactoryMock.createButton(anyString())).thenReturn(mock(JButton.class));
 when(userInterfaceFactoryMock.createLabel(anyString())).thenReturn(mock(JLabel.class));
 when(userInterfaceFactoryMock.createPanel()).thenReturn(mock(JPanel.class));
 when(userInterfaceFactoryMock.createScrollPane(any(JComponent.class))).thenReturn(mock(JScrollPane.class));
 when(displayableListMock.getListScrollPane()).thenReturn(mock(JScrollPane.class));
 when(displayableListMock.getAllItems()).thenReturn(new ArrayList<>());
 when(displayableListMock.isEmpty()).thenReturn(true);
 when(displayableListMock.getSelectedItem()).thenReturn(mock(Displayable.class));
 when(displayableListMock.itemsAreSelected()).thenReturn(false);
 when(displayableListPanel.getDisplayableList()).thenReturn(displayableListMock);
 when(displayableListPanel.isNewRecord()).thenReturn(false);
 when(displayableListPanel.getDisplayableListItemSelector()).thenReturn(listItemSelectorMock);
 when(displayableListPanel.getDisplayableListItemAdder()).thenReturn(listItemAdderMock);
 when(displayableListPanel.getDisplayableListItemEditor()).thenReturn(listItemEditorMock);
 when(displayableListPanel.getDisplayableListItemDeleter()).thenReturn(listItemDeleterMock);
 when(displayableListPanel.getDisplayableListParentForm()).thenReturn(parentFormMock);
 }

 @Test
 public void testInit() {
 displayableListPanel.init(sessionPropertiesMock, parentFormMock, "Test Panel", false, false);
 verify(displayableListPanel, times(1)).init(sessionPropertiesMock, parentFormMock, "Test Panel", false, false);
 }

 @Test
 public void testBuildUI() {
 displayableListPanel.buildUI();
 verify(spyDisplayableListPanel, times(1)).buildUI();
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelectorMock);
 verify(displayableListPanel, times(1)).setDisplayableListItemSelector(listItemSelectorMock);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdderMock);
 verify(displayableListPanel, times(1)).setDisplayableListItemAdder(listItemAdderMock);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditorMock);
 verify(displayableListPanel, times(1)).setDisplayableListItemEditor(listItemEditorMock);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleterMock);
 verify(displayableListPanel, times(1)).setDisplayableListItemDeleter(listItemDeleterMock);
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 verify(parentFormMock, times(1)).commitChanges();
 verify(listItemAdderMock, times(1)).addItems(eq("Test Owner"), eq(displayableListMock));
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", false, false);
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 displayableListPanel.editSelectedListItem();
 verify(parentFormMock, times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMissingEditor() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", false, false);
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 displayableListPanel.viewSelectedListItem();
 verify(listItemViewerMock, times(1)).viewListItem(any(Displayable.class));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemMissingViewer() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", false, false);
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableListPanel.deleteSelectedListItems();
 verify(parentFormMock, times(1)).commitChanges();
 verify(listItemDeleterMock, times(1)).deleteListItems(eq("Test Owner"), eq(displayableListMock));
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWarningDeclined() {
 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", false, false);
 doAnswer(invocation -> {
 JOptionPane.showConfirmDialog(null, "Warning message", "Warning dialog title", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
 return 0;
 }).when(listItemDeleterMock).deleteListItems(anyString(), any(DisplayableList.class));
 displayableListPanel.deleteSelectedListItems();
 }
}