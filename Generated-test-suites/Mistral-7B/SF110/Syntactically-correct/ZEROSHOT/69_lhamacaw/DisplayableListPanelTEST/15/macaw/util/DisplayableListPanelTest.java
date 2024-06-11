package macaw.util;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import org.junit.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private JPanel panel;
 private DisplayableListParentForm parentForm;
 private SessionProperties sessionProperties;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemDeleter listItemDeleter;
 private DisplayableListItemViewer listItemViewer;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 log = Mockito.mock(Log.class);
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 displayableList = Mockito.mock(DisplayableList.class);
 listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 listItemViewer = Mockito.mock(DisplayableListItemViewer.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 }

 @Test
 public void testInit() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertEquals("Test Panel Title", displayableListPanel.getPanelTitleText());
 }

 @Test
 public void testInitWithAllowRecordEditingFalse() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, false);
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel Title", true, false);
 assertFalse(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testBuildUI() {
 displayableListPanel.buildUI();
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertEquals(listItemSelector, displayableListPanel.getListItemSelector());
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertEquals(listItemAdder, displayableListPanel.getListItemAdder());
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertEquals(listItemEditor, displayableListPanel.getListItemEditor());
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertEquals(listItemViewer, displayableListPanel.getListItemViewer());
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertEquals(listItemDeleter, displayableListPanel.getListItemDeleter());
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.setDisplayableList(displayableList);
 Mockito.when(displayableList.isEmpty()).thenReturn(false);
 Mockito.when(listItemAdder.addItems(ArgumentMatchers.eq("Test List Owner Name"), ArgumentMatchers.eq(displayableList)))
 .thenReturn(new ArrayList<>());
 displayableListPanel.addListItem();
 verify(displayableList, times(1)).addItem(ArgumentMatchers.any(Displayable.class));
 }

 @Test(expected = MacawException.class)
 public void testAddListItemThrowsMacawException() {
 Mockito.when(displayableList.isEmpty()).thenReturn(false);
 Mockito.when(listItemAdder.addItems(ArgumentMatchers.eq("Test List Owner Name"), ArgumentMatchers.eq(displayableList)))
 .thenThrow(new MacawException("Test MacawException"));
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 Mockito.when(displayableList.getSelectedItem()).thenReturn(Mockito.mock(Displayable.class));
 Mockito.when(listItemEditor.editListItem(ArgumentMatchers.eq("Test List Owner Name"), ArgumentMatchers.eq(Mockito.any(Displayable.class)), ArgumentMatchers.eq(displayableList)))
 .thenReturn(Mockito.mock(Displayable.class));
 displayableListPanel.editSelectedListItem();
 verify(displayableList, times(1)).setSelectedItem(ArgumentMatchers.any(Displayable.class));
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemThrowsMacawException() {
 Mockito.when(displayableList.getSelectedItem()).thenReturn(Mockito.mock(Displayable.class));
 Mockito.when(listItemEditor.editListItem(ArgumentMatchers.eq("Test List Owner Name"), ArgumentMatchers.eq(Mockito.any(Displayable.class)), ArgumentMatchers.eq(displayableList)))
 .thenThrow(new MacawException("Test MacawException"));
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 Mockito.when(displayableList.getSelectedItem()).thenReturn(Mockito.mock(Displayable.class));
 Mockito.when(listItemViewer.viewListItem(ArgumentMatchers.any(Displayable.class))).thenReturn( UnitTestUtil.createMockJOptionPane() );
 displayableListPanel.viewSelectedListItem();
 assertEquals(0, JOptionPane.showConfirmDialog(ArgumentMatchers.any(Component.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.eq(0), ArgumentMatchers.eq(true)));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemThrowsMacawException() {
 Mockito.when(displayableList.getSelectedItem()).thenReturn(Mockito.mock(Displayable.class));
 Mockito.when(listItemViewer.viewListItem(ArgumentMatchers.any(Displayable.class))).thenThrow(new MacawException("Test MacawException"));
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 Mockito.when(displayableList.isEmpty()).thenReturn(false);
 Mockito.when(JOptionPane.showConfirmDialog(ArgumentMatchers.any(Component.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.eq(0), ArgumentMatchers.eq(true)))
 .thenReturn(JOptionPane.YES_OPTION);
 Mockito.when(listItemDeleter.deleteListItems(ArgumentMatchers.eq("Test List Owner Name"), ArgumentMatchers.eq(displayableList)))
 .thenReturn(new ArrayList<>());
 displayableListPanel.deleteSelectedListItems();
 verify(displayableList, times(1)).removeAll(ArgumentMatchers.anyList(Displayable.class));
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsThrowsMacawException() {
 Mockito.when(displayableList.isEmpty()).thenReturn(false);
 Mockito.when(JOptionPane.showConfirmDialog(ArgumentMatchers.any(Component.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.eq(0), ArgumentMatchers.eq(true)))
 .thenReturn(JOptionPane.NO_OPTION);
 Mockito.when(listItemDeleter.deleteListItems(ArgumentMatchers.eq("Test List Owner Name"), ArgumentMatchers.eq(displayableList)))
 .thenThrow(new MacawException("Test MacawException"));
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 Mockito.when(listItemSelector.selectListItems(ArgumentMatchers.eq("Test List Owner Name"), ArgumentMatchers.eq(displayableList)))
 .thenReturn(new ArrayList<>());
 displayableListPanel.selectListItems();
 verify(displayableList, times(1)).requestFocus();
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsThrowsMacawException() {
 Mockito.when(listItemSelector.selectListItems(ArgumentMatchers.eq("Test List Owner Name"), ArgumentMatchers.eq(displayableList)))
 .thenThrow(new MacawException("Test MacawException"));
 displayableListPanel.selectListItems();
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.setDisplayableList(displayableList);
 Mockito.when(displayableList.isEmpty()).thenReturn(false);
 Mockito.when(displayableList.getSelectedItem()).thenReturn(Mockito.mock(Displayable.class));
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.isAddButtonEnabled());
 assertTrue(displayableListPanel.isSelectButtonEnabled());
 assertTrue(displayableListPanel.isDeleteButtonEnabled());
 assertTrue(displayableListPanel.isEditButtonEnabled());
 assertTrue(displayableListPanel.isViewButtonEnabled());
 }

 @Test
 public void testUpdateButtonStatesWithEmptyList() {
 displayableListPanel.setDisplayableList(displayableList);
 Mockito.when(displayableList.isEmpty()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isAddButtonEnabled());
 assertFalse(displayableListPanel.isSelectButtonEnabled());
 assertFalse(displayableListPanel.isDeleteButtonEnabled());
 assertFalse(displayableListPanel.isEditButtonEnabled());
 assertFalse(displayableListPanel.isViewButtonEnabled());
 }
}