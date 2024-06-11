import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Before;
import org.junit.Test;

public class FileTextTest {

    private File file;
    private FileText fileText;

    @Before
    public void setUp() throws Exception {
        file = new File("test.txt");
        fileText = new FileText(file, "UTF-8");
    }

    // Expert 1 test cases
    // ...

    // Expert 2 test cases
    // ...

    // Expert 3 test cases
    // ...
}