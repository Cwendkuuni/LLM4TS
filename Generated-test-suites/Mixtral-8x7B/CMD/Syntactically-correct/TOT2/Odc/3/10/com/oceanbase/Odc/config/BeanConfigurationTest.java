import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeanConfiguration.class)
@ActiveProfiles("unittest")
public class BeanConfigurationTest {

 @Autowired
 private BeanConfiguration beanConfiguration;

 @Autowired
 private PasswordEncoder passwordEncoder;

 @Autowired
 private ObjectMapper objectMapper;

 @Autowired
 private CloudMetadataClient cloudMetadataClient;

 @Autowired
 private CookieSerializer cookieSerializer;

 @Test
 public void testPasswordEncoder() {
 assertNotNull(beanConfiguration.passwordEncoder());
 assertEquals(true, passwordEncoder instanceof BCryptPasswordEncoder);
 }

 @Test
 public void testObjectMapper() {
 CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
 .addSerializer(Internationalizable.class, new I18nOutputSerializer());
 ObjectMapper jsonMapper = JacksonFactory.unsafeJsonMapper()
 .registerModule(JacksonModules.sensitiveInputHandling(input -> "decrypted"))
 .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
 assertEquals(jsonMapper, beanConfiguration.objectMapper(null));
 }

 @Test
 public void testDefaultHttpFirewall() {
 assertEquals(true, beanConfiguration.defaultHttpFirewall() instanceof DefaultHttpFirewall);
 assertEquals(true, beanConfiguration.defaultHttpFirewall().getAllowUrlEncodedSlash());
 }

 @Test
 public void testCloudMetadataClient() {
 assertNotNull(beanConfiguration.cloudMetadataClient());
 assertEquals(true, cloudMetadataClient instanceof BeanConfiguration.NullCloudMetadataClient);
 }

 @Test
 public void testCookieSerializer() {
 CookieSerializer serializer = beanConfiguration.cookieSerializer();
 assertNotNull(serializer);
 assertEquals("JSESSIONID", serializer.getCookieName());
 }

 @Test
 public void testNullCloudMetadataClient() {
 BeanConfiguration.NullCloudMetadataClient nullCloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
 assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
 assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
 assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
 assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
 assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
 assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants("instanceId"));
 assertThrows(UnsupportedException.class, () ->
 nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
 OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
 assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
 assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));
 }

}