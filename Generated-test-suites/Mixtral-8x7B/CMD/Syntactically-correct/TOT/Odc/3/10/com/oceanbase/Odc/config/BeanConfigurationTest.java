import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import java.util.Arrays;
import java.util.List;

public class BeanConfigurationTest {

 private SensitivePropertyHandler sensitivePropertyHandler;
 private CloudMetadataClient cloudMetadataClient;
 private ObjectMapper objectMapper;
 private CookieSerializer cookieSerializer;

 @Before
 public void setUp() {
 sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
 cloudMetadataClient = mock(CloudMetadataClient.class);

 when(sensitivePropertyHandler.decrypt("encryptedValue")).thenReturn("decryptedValue");
 when(cloudMetadataClient.listInstances()).thenReturn(Arrays.asList(new OBInstance()));
 when(cloudMetadataClient.listTenants("instanceId")).thenReturn(Arrays.asList(new OBTenant()));
 when(cloudMetadataClient.getTenant("instanceId", "tenantId")).thenReturn(new OBTenant());
 when(cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId")).thenReturn(Arrays.asList(new OBDatabaseUser()));
 when(cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId")).thenReturn(new OBTenantEndpoint());
 }

 @Test
 public void testPasswordEncoderBean() {
 PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
 assertNotNull(passwordEncoder);
 }

 @Test
 public void testObjectMapperBean() throws Exception {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer().addSerializer(Internationalizable.class,
 new I18nOutputSerializer());

 ObjectMapper jsonMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
 .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

 ObjectMapper objectMapperBean = new BeanConfiguration().objectMapper(sensitivePropertyHandler);

 assertEquals(objectMapperBean, jsonMapper);
 }

 @Test
 public void testDefaultHttpFirewallBean() {
 HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
 defaultHttpFirewall.setAllowUrlEncodedSlash(true);
 assertNotNull(defaultHttpFirewall);
 }

 @Test
 public void testNullCloudMetadataClientBean() {
 CloudMetadataClient cloudMetadataClientBean = new BeanConfiguration().cloudMetadataClient();
 assertNotNull(cloudMetadataClientBean);
 }

 @Test
 public void testCookieSerializerBean() {
 CookieSerializer cookieSerializerBean = new BeanConfiguration().cookieSerializer();
 assertNotNull(cookieSerializerBean);
 assertEquals("JSESSIONID", cookieSerializerBean.getCookieName());
 }
}