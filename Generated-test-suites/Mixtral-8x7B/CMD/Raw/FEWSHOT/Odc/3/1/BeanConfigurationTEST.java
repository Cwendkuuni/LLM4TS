import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import org.junit.Test;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.test.util.ReflectionTestUtils;

@Configuration
public class BeanConfigurationTest {

@Test
public void testPasswordEncoder() {
PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
String encodedPassword = passwordEncoder.encode("test");
assertFalse(encodedPassword.equals("test"));
}

@Test
public void testObjectMapper() throws Exception {
ObjectMapper objectMapper = new ObjectMapper(null, null, null, JacksonFactory.unsafeJsonMapper());
objectMapper.registerModule(JacksonModules.sensitiveInputHandling(input -> {}));
objectMapper.registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()));

Internationalizable internationalizable = new Internationalizable() {
@Override
public String getString(String key) {
return key;
}
};
I18nOutputSerializer i18nOutputSerializer = new I18nOutputSerializer();
ReflectionTestUtils.setField(i18nOutputSerializer, "internationalizable", internationalizable);

ObjectMapper.DefaultTyping typicalMapping = ObjectMapper.DefaultTyping.NON_FINAL;
Object writeValueAsString = i18nOutputSerializer.serialize(null, typicalMapping);
assertNotNull(writeValueAsString);
}

@Test
public void testDefaultHttpFirewall() {
HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
defaultHttpFirewall.setAllowUrlEncodedSlash(true);
assertEquals(true, defaultHttpFirewall.getAllowUrlEncodedSlash());
}

@Test
public void testNullCloudMetadataClient() throws Exception {
CloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();

when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(false);
when(cloudMetadataClient.supportsTenantInstance()).thenReturn(false);
when(cloudMetadataClient.needsOBTenantName()).thenReturn(false);
when(cloudMetadataClient.needsSysTenantUser()).thenReturn(false);
when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
when(cloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);

List<OBInstance> listInstances = cloudMetadataClient.listInstances();
assertEquals(0, listInstances.size());

List<OBTenant> listTenants = cloudMetadataClient.listTenants("instanceId");
assertEquals(0, listTenants.size());

OBTenant getTenant = cloudMetadataClient.getTenant("instanceId", "tenantId");
assertNull(getTenant);

List<OBDatabaseUser> listDatabaseUsers = cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
assertEquals(0, listDatabaseUsers.size());

OBTenantEndpoint getTenantEndpoint = cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
assertEquals(OceanBaseAccessMode.DIRECT, getTenantEndpoint.getAccessMode());

OBDatabaseUser getSysTenantUser = cloudMetadataClient.getSysTenantUser("instanceId");
assertNull(getSysTenantUser);
}

@Test
public void testCookieSerializer() {
CookieSerializer serializer = new DefaultCookieSerializer();
serializer.setCookieName("JSESSIONID");
assertEquals("JSESSIONID", serializer.getCookieName());
}

@Primary
@Bean
public SensitivePropertyHandler sensitivePropertyHandler() {
return input -> {};
}

@Primary
@Bean
public CloudMetadataClient cloudMetadataClient() {
return mock(CloudMetadataClient.class);
}

@Bean
public ObjectMapper objectMapper() {
return mock(ObjectMapper.class);
}

@Bean
public PasswordEncoder passwordEncoder() {
return mock(PasswordEncoder.class);
}

@Bean
public HttpFirewall defaultHttpFirewall() {
return mock(HttpFirewall.class);
}

@Bean
public CookieSerializer cookieSerializer() {
return mock(CookieSerializer.class);
}
}