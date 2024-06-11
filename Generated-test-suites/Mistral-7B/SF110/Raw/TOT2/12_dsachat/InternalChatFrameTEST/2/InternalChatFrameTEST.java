
import org.junit.Assert;
import org.junit.Test;
import dsachat.client.gui.InternalChatFrame;
import dsachat.client.gui.MainFrame;

public class InternalChatFrameTest {

    @Test
    public void testInternalChatFrameConstructor() {
        MainFrame parent = new MainFrame();
        InternalChatFrame chatFrame = new InternalChatFrame(parent);
        Assert.assertEquals("Chat", chatFrame.getTitle());
    }

    @Test
    public void testAddText() {
        String line = "You: Test line";
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        chatFrame.addText(line);
        String chatLog = chatFrame.getChatlog();
        Assert.assertEquals(line, chatLog.substring(0, chatLog.indexOf("\n") + line.length()));
    }

    @Test
    public void testChangeColorContextMenuItem() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String user = "TestUser";
        chatFrame.addUser(user);
        chatFrame.addText(user + ": Test message");
        Color newColor = Color.RED;
        chatFrame.list.setSelectedValue(user, true);
        chatFrame.listPop.getMenuComponent(0).doClick();
        JColorChooser chooser = (JColorChooser) chatFrame.listPop.getComponent(0);
        chooser.setSelectedColor(newColor);
        chooser.setVisible(true);
        chooser.pack();
        chooser.setVisible(false);
        String chatLog = chatFrame.getChatlog();
        Assert.assertEquals(user + ": Test message\n" + user + " (Changed Color)", chatLog.substring(0, chatLog.lastIndexOf("\n") + user.length() + "\n".length()));
    }

    @Test
    public void testAddUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String user = "TestUser";
        chatFrame.addUser(user);
        String[] userList = chatFrame.getUserlist();
        Assert.assertEquals(1, userList.length);
        Assert.assertEquals(user, userList[0]);
    }

    @Test
    public void testDeleteUser() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String user = "TestUser";
        chatFrame.addUser(user);
        chatFrame.deleteUser(user);
        String[] userList = chatFrame.getUserlist();
        Assert.assertEquals(0, userList.length);
    }

    @Test
    public void testWhisperContextMenuItem() {
        InternalChatFrame chatFrame = new InternalChatFrame(new MainFrame());
        String user1 = "User1";
        String user2 = "User2";
        chatFrame.addUser(user1);
        chatFrame.addUser(user2);
        chatFrame.addText(user1 + ": Test message for " + user2);
        chatFrame.list.setSelectedValue(user1, true);
        chatFrame.listPop.getMenuComponent(1).doClick();
        String chatLog = chatFrame.getChatlog();
        Assert.assertEquals("/w " + user2 + " Test message for " + user2 + "\n", chatLog.substring(chatLog.lastIndexOf("\n") + user1.length() + "\n".length(), chatLog.length()));
    }
}