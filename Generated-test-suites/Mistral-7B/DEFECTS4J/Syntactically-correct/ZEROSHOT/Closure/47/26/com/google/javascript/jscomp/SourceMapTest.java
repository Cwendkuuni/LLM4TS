import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.Format.*;
import static com.google.javascript.jscomp.SourceMap.DetailLevel.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {

  private static final String SOURCE_FILE = "TestSourceFile.js";
  private static final String SOURCE_FILE_REPLACED = "TestSourceFile_replaced.js";

  private SourceMap sourceMap;
  private OutputStream outputStream;

  @Before
  public void setUp() throws IOException {
    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("src/", "test/")
    );

    Format format = V1;
    DetailLevel detailLevel = ALL;

    outputStream = new ByteArrayOutputStream();
    sourceMap = new SourceMap(format.getInstance()).setPrefixMappings(prefixMappings);
  }

  @Test
  public void testAddMapping() {
    Node node = Node.newNode(Node.SOURCE_FILE, SOURCE_FILE);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String expectedSourceMap = "{\"version\":" + SourceMapFormat.V1.getValue() + ",\"file\":" + SOURCE_FILE + ",\"sources\":[\"TestSourceFile.js\"],\"names\":[],\"mappings\":[\"" + SOURCE_FILE + "\",0,[1,1,1,1,0,10]],\"sourcesContent\":[]}";
    String actualSourceMap = getSourceMapAsString();

    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testAddMappingWithDetailLevel() {
    Node node = Node.newNode(Node.CALL, "myFunction", null, null, null, null);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);
    sourceMap = new SourceMap(V2.getInstance()).setDetailLevel(SYMBOLS).setPrefixMappings(prefixMappings);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String expectedSourceMap = "{\"version\":" + SourceMapFormat.V2.getValue() + ",\"file\":" + SOURCE_FILE + ",\"sourceRoot\":\"test/\",\"sources\":[\"TestSourceFile.js\"],\"names\":[],\"mappings\":[{\"original\":{\"start\":{\"line\":1,\"column\":0},\"end\":{\"line\":1,\"column\":10}},\"generated\":{\"line\":1,\"column\":0},\"source\":\"TestSourceFile.js\"}]}";
    String actualSourceMap = getSourceMapAsString();

    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testFixupSourceLocation() {
    String fixedSourceFile = sourceMap.fixupSourceLocation(SOURCE_FILE);
    assertEquals(TEST_RESOURCE_DIR + SOURCE_FILE_REPLACED, fixedSourceFile);
  }

  @Test
  public void testValidate() throws IOException {
    sourceMap.validate(true);
  }

  @Test(expected = AssertionError.class)
  public void testValidateWithInvalidInput() throws IOException {
    sourceMap.validate(false);
  }

  @Test
  public void testAppendTo() throws IOException {
    sourceMap.appendTo(outputStream, "TestSourceMap");

    String expectedSourceMap = "{\"version\":" + SourceMapFormat.V1.getValue() + ",\"file\":" + SOURCE_FILE + ",\"sources\":[\"TestSourceFile.js\"],\"names\":[],\"mappings\":[],\"sourcesContent\":[],\"fileContents\":[\"TestSourceMap\"]}";
    String actualSourceMap = new String(outputStream.toByteArray());

    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testReset() throws IOException {
    sourceMap.appendTo(outputStream, "TestSourceMap");
    sourceMap.reset();

    String expectedSourceMap = "";
    String actualSourceMap = new String(outputStream.toByteArray());

    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testSetWrapperPrefix() throws IOException {
    sourceMap.setWrapperPrefix("test_");
    sourceMap.appendTo(outputStream, "TestSourceMap");

    String expectedSourceMap = "{\"version\":" + SourceMapFormat.V1.getValue() + ",\"file\":" + SOURCE_FILE + ",\"sourceRoot\":\"test/\",\"sources\":[\"TestSourceFile.js\"],\"names\":[],\"mappings\":[],\"sourcesContent\":[],\"fileContents\":[\"test_TestSourceMap\"]}";
    String actualSourceMap = new String(outputStream.toByteArray());

    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testSetStartingPosition() throws IOException {
    sourceMap.setStartingPosition(1, 1);
    sourceMap.appendTo(outputStream, "TestSourceMap");

    String expectedSourceMap = "{\"version\":" + SourceMapFormat.V1.getValue() + ",\"file\":" + SOURCE_FILE + ",\"sourceRoot\":\"\",\"sources\":[],\"names\":[],\"mappings\":[],\"sourcesContent\":[],\"fileContents\":[\"TestSourceMap\"],\"start\":{\"line\":1,\"column\":1}}";
    String actualSourceMap = new String(outputStream.toByteArray());

    assertEquals(expectedSourceMap, actualSourceMap);
  }

  private String getSourceMapAsString() throws IOException {
    sourceMap.appendTo(outputStream, "TestSourceMap");
    return new String(outputStream.toByteArray());
  }

  private static final String TEST_RESOURCE_DIR = "test/";
}