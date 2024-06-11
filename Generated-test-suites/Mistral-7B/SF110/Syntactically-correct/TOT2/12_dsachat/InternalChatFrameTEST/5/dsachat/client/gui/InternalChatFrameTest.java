package dsachat.client.gui; //import statements here

public class InternalChatFrameTest {

    @org.junit.Before
    public void setUp() {
        // Initialize the InternalChatFrame instance for testing
        this.chatFrame = new InternalChatFrame(new MainFrame());
    }

    // Test case for the constructor
    @org.junit.Test
    public void testInternalChatFrameConstruction() {
        assertNotNull(this.chatFrame);
        assertEquals("Chat", this.chatFrame.getTitle());
    }

    // Test case for addText method with normal message
    @org.junit.Test
    public void testAddTextNormalMessage() {
        String line = "User1: Hello";
        this.chatFrame.addText(line);

        String chatLog = this.chatFrame.getChatlog();
        String expectedChatLog = "User1: Hello\n";

        assertEquals(expectedChatLog, chatLog);
    }

    // Test case for addText method with italic message
    @org.junit.Test
    public void testAddTextItalicMessage() {
        String line = "*User1*: Hello";
        this.chatFrame.addText(line);

        String chatLog = this.chatFrame.getChatlog();
        String expectedChatLog = "*User1*: Hello\n";

        assertEquals(expectedChatLog, chatLog);
    }

    // Test case for addText method with bold message
    @org.junit.Test
    public void testAddTextBoldMessage() {
        String line = "+User1+: Hello";
        this.chatFrame.addText(line);

        String chatLog = this.chatFrame.getChatlog();
        String expectedChatLog = "+User1+: Hello\n";

        assertEquals(expectedChatLog, chatLog);
    }

    // Test case for addText method with bold and italic message
    @org.junit.Test
    public void testAddTextBoldItalicMessage() {
        String line = "*+User1+*: Hello";
        this.chatFrame.addText(line);

        String chatLog = this.chatFrame.getChatlog();
        String expectedChatLog = "*+User1+*: Hello\n";

        assertEquals(expectedChatLog, chatLog);
    }

    // Test case for addStylesToDocument method
    @org.junit.Test
    public void testAddStylesToDocument() {
        this.chatFrame.addStylesToDocument(this.chatFrame.getDoc());

        Style regular = this.chatFrame.getDoc().getStyle("regular");
        Style italic = this.chatFrame.getDoc().getStyle("italic");
        Style bold = this.chatFrame.getDoc().getStyle("bold");
        Style boldItalic = this.chatFrame.getDoc().getStyle("bold-italic");
        Style name = this.chatFrame.getDoc().getStyle("name");
        Style system = this.chatFrame.getDoc().getStyle("system");
        Style small = this.chatFrame.getDoc().getStyle("small");
        Style large = this.chatFrame.getDoc().getStyle("large");

        assertNotNull(regular);
        assertNotNull(italic);
        assertNotNull(bold);
        assertNotNull(boldItalic);
        assertNotNull(name);
        assertNotNull(system);
        assertNotNull(small);
        assertNotNull(large);
    }

    // Test case for addUser method
    @org.junit.Test
    public void testAddUser() {
        this.chatFrame.addUser("User1");
        String[] userList = this.chatFrame.getUserlist();

        assertEquals(1, userList.length);
        assertEquals("User1", userList[0]);
    }

    private InternalChatFrame chatFrame;
}