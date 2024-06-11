import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setup() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setUsername("testUser");
connectionConfig.setPassword("testPassword");
connectionConfig.setHost("testHost");
connectionConfig.setPort(1234);
connectionConfig.setDefaultSchema("testSchema");

Map<String, String> parameters = new HashMap<>();
parameters.put("paramKey1", "paramValue1");
connectionConfig.setJdbcUrlParameters(parameters);

this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetJdbcUrl() {
String expectedJdbcUrl = "jdbc:mysql://testHost:1234/testSchema?paramKey1=paramValue1";
assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
}

// ... Test other methods and edge cases here

}

class MockConnectionExtensionPoint implements ConnectionExtensionPoint, ApplicationEventPublisherAware {

private ApplicationEventPublisher applicationEventPublisher;

@Override
public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
this.applicationEventPublisher = applicationEventPublisher;
}

@Override
public String generateJdbcUrl(String host, Integer port, String defaultSchema,
Map<String, String> parameters) {
throw new NotImplementedException();
}

@Override
public String getDriverClassName() {
throw new NotImplementedException();
}

@Override
public List<ConnectionInitializer> getConnectionInitializers() {
return Collections.emptyList();
}

}