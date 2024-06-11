import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.compiler.Compiler;
import com.google.javascript.jscomp.compiler.CompilerOptions;
import com.google.javascript.jscomp.compiler.CompilerPass;
import com.google.javascript.jscomp.parser.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RenamePrototypesTest {

  @Test
  public void testSimpleRename() {
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);
    CompilerPass renamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), false, null, null);

    TokenStream ts = new TokenStream(new StringBuilder());
    Token begin = new Token(Token.START_FILE, 0, 0);
    ts.putNext(begin);
    ts.putNext(new Token(Token.VAR, 1, 1));
    ts.putNext(new Token(Token.IDENT, "proto", 3, 5));
    ts.putNext(new Token(Token.ASSIGN, 1, 9));
    ts.putNext(new Token(Token.OBJLIT, 1, 11));
    ts.putNext(new Token(Token.STRING, "getProp", 10, 12));
    ts.putNext(new Token(Token.COLON, 1, 17));
    ts.putNext(new Token(Token.FUNCTION, 1, 18));
    ts.putNext(new Token(Token.RETURN, 1, 21));
    ts.putNext(new Token(Token.END_FUNCTION, 1, 22));
    ts.putNext(new Token(Token.END_OBJLIT, 1, 23));
    ts.putNext(new Token(Token.SEMICOLON, 1, 24));
    ts.putNext(new Token(Token.END_FILE, 0, 0));

    Node root = Compiler.parseSingleFile(compiler, options, ts, null);
    renamePrototypes.process(null, root);

    ArgumentCaptor<Property> propertyArgCaptor = ArgumentCaptor.forClass(Property.class);
    assertEquals(1, compiler.getDiagnostics().length());
    assertEquals("Renaming 'proto.getProp' to '_$RenamedGetProp'",
        compiler.getDiagnostics()[0].getMessage());

    Node renamedNode = findNodeWithName("_$RenamedGetProp", root);
    Property renamedProperty = propertiesFromMap(renamePrototypes.getPropertyMap()).get("getProp");
    assertEquals(renamedProperty.newName, renamedNode.getFirstChild().getString());
  }

  private Node findNodeWithName(String name, Node node) {
    if (node.getType() == Token.STRING) {
      if (node.getString().equals(name)) {
        return node;
      }
    }

    for (Node child : node.getChildren()) {
      Node result = findNodeWithName(name, child);
      if (result != null) {
        return result;
      }
    }

    return null;
  }

  private Map<String, Property> propertiesFromMap(Map<String, String> map) {
    Map<String, Property> properties = new HashMap<String, Property>();
    for (Map.Entry<String, String> entry : map.entrySet()) {
      properties.put(entry.getKey(), new Property(entry.getKey()));
    }
    return properties;
  }

  private static class Property {
    private final String name;
    private String newName;

    Property(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  private static class RenamePrototypes {
    private static Map<String, Property> properties = new HashMap<String, Property>();

    static Property getProperty(String name) {
      Property prop = properties.get(name);
      if (prop == null) {
        prop = new Property(name);
        properties.put(name, prop);
      }
      return prop;
    }
  }
}