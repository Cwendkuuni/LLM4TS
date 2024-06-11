package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidades;

 @Before
 public void setup() {
  form = new ProcessarEntidades();
  entidades = new ArrayList<>();
  salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarWithValidFileAndEntidades() {
  Entidade entidade1 = new Entidade("Teste1", 1);
  Entidade entidade2 = new Entidade("Teste2", 2);
  entidades.add(entidade1);
  entidades.add(entidade2);

  File expectedFile = new File("expectedPath/test.jaw.xml");
  salvar.arquivo = expectedFile.getAbsolutePath();
  salvar.entidade = entidades;
  salvar.salvar();

  File actualFile = new File(salvar.arquivo);
  FileFilter xmlFilter = new Salvar().new Filtro2();

  assertTrue(actualFile.exists());
  assertTrue(actualFile.isFile());
  assertTrue(xmlFilter.accept(actualFile));
 }

 @Test(expected = Exception.class)
 public void testSalvarWithNullEntidades() {
  salvar.salvar();
 }

 @Test
 public void testSalvarWithInvalidFileExtension() {
  salvar.arquivo = "invalidPath/test.txt";
  salvar.salvar();
 }

 @Test(expected = IOException.class)
 public void testSerializacaoThrowsIOExceptionWhenSaving() {
  Salvar salvar = new Salvar(form, Salvar.SALVAR_XML);
  salvar.entidade = new ArrayList<>();
  salvar.arquivo = "path/to/file.xml";

  SerializacaoXML serializaXML = new SerializacaoXML();
  String xml = serializaXML.entidades2XML(salvar.entidade);

  FileWriter escrevedor = null;
  try {
   escrevedor = new FileWriter(salvar.arquivo);
   escrevedor.write(xml);
   escrevedor.flush();
   escrevedor.close();
  } catch (IOException ex) {
   fail("FileWriter should not throw an IOException");
  }

  Salvar salvar2 = new Salvar(form, Salvar.SALVAR_XML);
  salvar2.entidade = new ArrayList<>();
  salvar2.arquivo = salvar.arquivo;

  try {
   salvar2.salvar();
  } catch (Exception e) {
   fail("Salvar should not throw an exception with valid file and entidades");
  }
 }

 @Test
 public void testSalvarWithValidFileAndNoEntidades() {
  salvar.arquivo = "path/to/file.jaw";
  salvar.salvar();

  File actualFile = new File(salvar.arquivo);
  FileFilter jawFilter = new Salvar().new Filtro();

  assertTrue(actualFile.exists());
  assertTrue(actualFile.isFile());
  assertTrue(jawFilter.accept(actualFile));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSalvarWithNegativeTipoSalvar() {
  Salvar salvar = new Salvar(form, -1);
 }

}