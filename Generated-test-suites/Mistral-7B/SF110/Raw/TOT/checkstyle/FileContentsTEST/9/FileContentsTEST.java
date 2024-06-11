import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineNumberUtil;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

public class FileContentsTest {

  @Test
  public void testFileContentsConstruction() {
    String fileName = "test.java";
    String[] lines = {"public class TestClass", "", "/**", " * This is a Javadoc comment", " */", "int testMethod();"};

    FileText fileTextMock = mock(FileText.class);
    when(fileTextMock.get(0)).thenReturn("public class TestClass");
    when(fileTextMock.get(1)).thenReturn("");
    when(fileTextMock.get(2)).thenReturn("/**");
    when(fileTextMock.get(3)).thenReturn(" * This is a Javadoc comment");
    when(fileTextMock.get(4)).thenReturn(" */");
    when(fileTextMock.get(5)).thenReturn("int testMethod();");

    File fileMock = mock(File.class);
    when(fileMock.toString()).thenReturn(fileName);

    FileContents fileContents = new FileContents(fileName, fileTextMock);

    Map<Integer, TextBlock> javadocComments = fileContents.getJavadocComments();
    TextBlock expectedJavadoc = new TextBlock(" * This is a Javadoc comment", 3, 1, 3, 31);
    TextBlock actualJavadoc = javadocComments.get(1);

    assertEquals(Collections.singletonMap(1, expectedJavadoc), javadocComments);
    assertNotNull(actualJavadoc);
    assertEquals(expectedJavadoc.getText(), actualJavadoc.getText());
    assertEquals(expectedJavadoc.getStartLineNo(), actualJavadoc.getStartLineNo());
    assertEquals(expectedJavadoc.getStartColumnNo(), actualJavadoc.getStartColumnNo());
    assertEquals(expectedJavadoc.getEndLineNo(), actualJavadoc.getEndLineNo());
    assertEquals(expectedJavadoc.getEndColumnNo(), actualJavadoc.getEndColumnNo());

    List<TextBlock> cComments = fileContents.getCComments().get(1);
    TextBlock cComment = cComments.get(0);

    assertEquals(Collections.singletonList(cComment), cComments);
    assertNotNull(cComment);
    assertEquals("", cComment.getText());
    assertEquals(LineNumberUtil.LINE_NUMBER, cComment.getStartLineNo());
    assertEquals(LineNumberUtil.COLUMN_NUMBER, cComment.getStartColumnNo());
    assertEquals(LineNumberUtil.LINE_NUMBER, cComment.getEndLineNo());
    assertEquals(LineNumberUtil.COLUMN_NUMBER, cComment.getEndColumnNo());
  }
}