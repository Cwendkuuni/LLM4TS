import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import macaw.businessLayer.*;
import macaw.system.*;
import macaw.util.*;
import static org.junit.Assert.*;

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
 when(sessionProperties.getLog()).thenReturn(log);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 }

 @Test
 public void testInit() {
 assertNotNull(displayableListPanel.getPanel());
 verify(userInterfaceFactory, times(6)).createButton(anyString());
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetNewRecordFalse() {
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutAdder() {
 displayableListPanel.addListItem();
 fail("Should throw MacawException when there is no addListItemAdder");
 }

 @Test
 public void testEditSelectedListItemWithoutEditor() {
 try {
 displayableListPanel.editSelectedListItem();
 fail("Should throw MacawException when there is no DisplayableListItemEditor");
 }
 catch (MacawException e) {
 // expected
 }
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithoutViewer() {
 try {
 displayableListPanel.viewSelectedListItem();
 fail("Should throw MacawException when there is no DisplayableListItemViewer");
 }
 catch (MacawException e) {
 // expected
 }
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithoutDeleter() {
 try {
 displayableListPanel.deleteSelectedListItems();
 fail("Should throw MacawException when there is no DisplayableListItemDeleter");
 }
 catch (MacawException e) {
 // expected
 }
 }

 @Test
 public void testDeleteSelectedListItems() {
 final DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.itemsAreSelected()).thenReturn(true);
 when(displayableList.getSelectedItem()).thenReturn(mock(Displayable.class));
 displayableListPanel.deleteSelectedListItems();
 verify(parentForm).commitChanges();
 verify(log).displayErrorDialog(anyString());
 }

 @Test
 public void testSelectListItems() {
 final DisplayableList displayableList = mock(DisplayableList.class);
 when(displayableList.itemsAreSelected()).thenReturn(false);
 displayableListPanel.selectListItems();
 verify(log).displayErrorDialog(anyString());
 }
}