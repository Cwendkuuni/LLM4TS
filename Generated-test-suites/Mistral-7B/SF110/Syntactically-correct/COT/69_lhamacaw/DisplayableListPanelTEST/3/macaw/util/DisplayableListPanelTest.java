package macaw.util;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.*;
import org.mockito.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 log = mock(Log.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 }

 @Test
 public void testDisplayableListPanelConstructor1() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelConstructor2() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testInit() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 ArgumentCaptor<String> titleLabelTextCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<JButton> selectButtonCaptor = ArgumentCaptor.forClass(JButton.class);
 ArgumentCaptor<JButton> addButtonCaptor = ArgumentCaptor.forClass(JButton.class);
 ArgumentCaptor<JButton> editButtonCaptor = ArgumentCaptor.forClass(JButton.class);
 ArgumentCaptor<JButton> viewButtonCaptor = ArgumentCaptor.forClass(JButton.class);
 ArgumentCaptor<JButton> deleteButtonCaptor = ArgumentCaptor.forClass(JButton.class);

 verify(userInterfaceFactory).createLabel(titleLabelTextCaptor.capture());
 assertEquals("Test Panel", titleLabelTextCaptor.getValue());

 verify(userInterfaceFactory).createButton(selectButtonCaptor.capture());
 assertEquals(MacawMessages.getMessage("general.buttons.select"), selectButtonCaptor.getValue().getText());

 verify(userInterfaceFactory).createButton(addButtonCaptor.capture());
 assertEquals(MacawMessages.getMessage("general.buttons.add"), addButtonCaptor.getValue().getText());

 verify(userInterfaceFactory).createButton(editButtonCaptor.capture());
 assertEquals(MacawMessages.getMessage("general.buttons.edit"), editButtonCaptor.getValue().getText());

 verify(userInterfaceFactory).createButton(viewButtonCaptor.capture());
 assertEquals(MacawMessages.getMessage("general.buttons.view"), viewButtonCaptor.getValue().getText());

 verify(userInterfaceFactory).createButton(deleteButtonCaptor.capture());
 assertEquals(MacawMessages.getMessage("general.buttons.delete"), deleteButtonCaptor.getValue().getText());

 verify(parentForm).commitChanges();
 }

 @Test
 public void testBuildUI() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test(expected = MacawException.class)
 public void testAddListItem_missingAdder() {
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem_missingEditor() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 displayableListPanel.setDisplayableListItemEditor(null);
 try {
 displayableListPanel.editSelectedListItem();
 fail();
 } catch (MacawException e) {
 // Expected
 }
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItem_missingViewer() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 displayableListPanel.setDisplayableListItemViewer(null);
 try {
 displayableListPanel.viewSelectedListItem();
 fail();
 } catch (MacawException e) {
 // Expected
 }
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 displayableListPanel.setDisplayableListItemDeleter(mock(DisplayableListItemDeleter.class));

 JOptionPane mockOptionPane = mock(JOptionPane.class);
 doAnswer(invocation -> {
 return JOptionPane.showConfirmDialog(invocation.getArgument(0), MacawMessages.getMessage("general.warning.deletionIsPermanent"), MacawMessages.getMessage("general.warning.title"), 0, 0);
 }).when(mockOptionPane).showConfirmDialog(any(), anyString(), anyString(), anyInt(), anyInt());

 displayableListPanel.deleteSelectedListItems();
 verify(displayableListPanel.getDisplayableListItemDeleter()).deleteListItems(eq("Test Panel"), argThat(new IsDisplayableListInstance()));
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 displayableListPanel.setDisplayableListItemSelector(mock(DisplayableListItemSelector.class));

 displayableListPanel.selectListItems();
 verify(displayableListPanel.getDisplayableListItemSelector()).selectListItems(eq("Test Panel"), argThat(new IsDisplayableListInstance()));
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 displayableListPanel.setDisplayableList(new DisplayableList(userInterfaceFactory));

 displayableListPanel.updateButtonStates();
 verify(displayableListPanel.getDisplayableList()).setEnabled(false, false, false, false, false, false);
 }

 @Test
 public void testUpdateButtonStates_allowWriteAccess() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 displayableListPanel.setDisplayableList(new DisplayableList(userInterfaceFactory));
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 verify(displayableListPanel.getDisplayableList()).setEnabled(true, true, true, true, true, true);
 }

 @Test
 public void testUpdateButtonStates_noNewRecord() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 displayableListPanel.setDisplayableList(new DisplayableList(userInterfaceFactory));
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 verify(displayableListPanel.getDisplayableList()).setEnabled(true, false, true, true, true, false);
 }

 @Test
 public void testUpdateButtonStates_noWriteAccess() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false, true);
 displayableListPanel.init();
 displayableListPanel.setDisplayableList(new DisplayableList(userInterfaceFactory));
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 verify(displayableListPanel.getDisplayableList()).setEnabled(false, false, false, true, false, false);
 }

 @Test
 public void testSetDisplayableItems() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Item 1", "Test Display Name 1"));
 masterList.add(new Displayable("Test Item 2", "Test Display Name 2"));
 displayableListPanel.setDisplayableItems(masterList);
 assertSame(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 DisplayableList displayableList = new DisplayableList(userInterfaceFactory);
 displayableList.add(new Displayable("Test Item 1", "Test Display Name 1"));
 displayableList.add(new Displayable("Test Item 2", "Test Display Name 2"));
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.save();
 assertEquals(displayableList, displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 DisplayableList displayableList = new DisplayableList(userInterfaceFactory);
 displayableList.add(new Displayable("Test Item 1", "Test Display Name 1"));
 displayableList.add(new Displayable("Test Item 2", "Test Display Name 2"));
 displayableListPanel.displayableList = displayableList;
 assertEquals(displayableList.getAllItems(), displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.setListOwnerName("Test Owner");
 assertEquals("Test Owner", displayableListPanel.listOwnerName);
 }

 @Test(expected = NullPointerException.class)
 public void testActionPerformed_nullSource() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 displayableListPanel.actionPerformed(null);
 }

 @Test
 public void testActionPerformed_select() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 displayableListPanel.setDisplayableListItemSelector(mock(DisplayableListItemSelector.class));
 displayableListPanel.selectListItems();
 verify(displayableListPanel.getDisplayableListItemSelector()).selectListItems(eq("Test Panel"), argThat(new IsDisplayableListInstance()));
 }

 @Test
 public void testActionPerformed_add() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 displayableListPanel.setDisplayableListItemAdder(mock(DisplayableListItemAdder.class));
 displayableListPanel.addListItem();
 verify(displayableListPanel.getDisplayableListItemAdder()).addItems(eq("Test Panel"), argThat(new IsDisplayableListInstance()));
 }

 @Test
 public void testActionPerformed_edit() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 Displayable selectedListItem = new Displayable("Test Item 1", "Test Display Name 1");
 Displayable newListItem = new Displayable("Test Item 1 Updated", "Test Display Name 1 Updated");
 displayableListPanel.setDisplayableListItemEditor(mock(DisplayableListItemEditor.class));
 displayableListPanel.displayableList.add(selectedListItem);
 ArgumentCaptor<Displayable> selectedListItemCaptor = ArgumentCaptor.forClass(Displayable.class);
 ArgumentCaptor<String> listOwnerNameCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Displayable> newListItemCaptor = ArgumentCaptor.forClass(Displayable.class);
 ArgumentCaptor<DisplayableList> displayableListCaptor = ArgumentCaptor.forClass(DisplayableList.class);

 doAnswer(invocation -> {
 DisplayableListItemEditor editor = invocation.getArgument(0);
 return editor.editListItem("Test Panel", selectedListItem, displayableListCaptor.capture());
 }).when(mock(DisplayableListItemEditor.class)).editListItem(eq("Test Panel"), any(Displayable.class), any(DisplayableList.class));

 displayableListPanel.editSelectedListItem();
 assertEquals(newListItem, displayableListCaptor.getValue().getAllItems().get(0));
 }

 @Test
 public void testActionPerformed_view() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 Displayable selectedListItem = new Displayable("Test Item 1", "Test Display Name 1");
 displayableListPanel.displayableList.add(selectedListItem);
 displayableListPanel.setDisplayableListItemViewer(mock(DisplayableListItemViewer.class));
 displayableListPanel.viewSelectedListItem();
 verify(displayableListPanel.getDisplayableListItemViewer()).viewListItem(eq(selectedListItem));
 }

 @Test
 public void testActionPerformed_delete() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.init();
 Displayable selectedListItem1 = new Displayable("Test Item 1", "Test Display Name 1");
 Displayable selectedListItem2 = new Displayable("Test Item 2", "Test Display Name 2");
 DisplayableList displayableList = new DisplayableList(userInterfaceFactory);
 displayableList.add(selectedListItem1);
 displayableList.add(selectedListItem2);
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.setDisplayableListItemDeleter(mock(DisplayableListItemDeleter.class));
 JOptionPane mockOptionPane = mock(JOptionPane.class);
 doAnswer(invocation -> {
 return JOptionPane.showConfirmDialog(invocation.getArgument(0), MacawMessages.getMessage("general.warning.deletionIsPermanent"), MacawMessages.getMessage("general.warning.title"), 0, 2);
 }).when(mockOptionPane).showConfirmDialog(any(), anyString(), anyString(), anyInt(), anyInt());

 displayableListPanel.deleteSelectedListItems();
 verify(displayableListPanel.getDisplayableListItemDeleter()).deleteListItems(eq("Test Panel"), argThat(new IsDisplayableListInstance()));
 }

}