package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import macaw.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListParentForm parentFormMock;
 private SessionProperties sessionPropertiesMock;
 private Log logMock;
 private UserInterfaceFactory userInterfaceFactoryMock;
 private DisplayableList displayableListMock;
 private DisplayableListItemSelector listItemSelectorMock;
 private DisplayableListItemAdder listItemAdderMock;
 private DisplayableListItemEditor listItemEditorMock;
 private DisplayableListItemViewer listItemViewerMock;
 private DisplayableListItemDeleter listItemDeleterMock;

 @Before
 public void setUp() {
 sessionPropertiesMock = mock(SessionProperties.class);
 parentFormMock = mock(DisplayableListParentForm.class);
 logMock = mock(Log.class);
 userInterfaceFactoryMock = mock(UserInterfaceFactory.class);
 displayableListMock = mock(DisplayableList.class);
 listItemSelectorMock = mock(DisplayableListItemSelector.class);
 listItemAdderMock = mock(DisplayableListItemAdder.class);
 listItemEditorMock = mock(DisplayableListItemEditor.class);
 listItemViewerMock = mock(DisplayableListItemViewer.class);
 listItemDeleterMock = mock(DisplayableListItemDeleter.class);

 when(sessionPropertiesMock.getLog()).thenReturn(logMock);
 when(sessionPropertiesMock.getProperty("currentUser")).thenReturn(new User());
 when(sessionPropertiesMock.getUserInterfaceFactory()).thenReturn(userInterfaceFactoryMock);

 displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Panel", true, true);
 displayableListPanel.init();
 }

 @Test
 public void testInit() {
 // Test that init method initializes all required variables
 verify(userInterfaceFactoryMock).createButton(anyString());
 verify(userInterfaceFactoryMock, times(5)).createButton(anyString());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelectorMock);
 verify(displayableListPanel).listItemSelector = listItemSelectorMock;
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdderMock);
 verify(displayableListPanel).listItemAdder = listItemAdderMock;
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditorMock);
 verify(displayableListPanel).listItemEditor = listItemEditorMock;
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewerMock);
 verify(displayableListPanel).listItemViewer = listItemViewerMock;
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleterMock);
 verify(displayableListPanel).listItemDeleter = listItemDeleterMock;
 }

 @Test
 public void testAddListItem() {
 // Test that addListItem method calls addItems method on the DisplayableList and updates button states
 doNothing().when(listItemAdderMock).addItems(anyString(), displayableListMock);
 displayableListPanel.setDisplayableListItemAdder(listItemAdderMock);
 displayableListPanel.addListItem();
 verify(listItemAdderMock).addItems(anyString(), displayableListMock);
 displayableListPanel.updateButtonStates();
 verify(displayableListPanel, times(5)).setEnabled(false);
 }

 // Add other test cases here for editSelectedListItem, viewSelectedListItem, deleteSelectedListItems, selectListItems, and updateButtonStates methods

 @After
 public void tearDown() {
 displayableListPanel = null;
 }
}