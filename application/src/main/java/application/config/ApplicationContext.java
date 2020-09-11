package application.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ApplicationContext {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationContext.class);

    @Value("${server.port}")
    private int port;

    @Value("${spring.application.name}")
    private String name;

    @PostConstruct
    public void start() {
        LOG.info("running \"" + name + "\" at http://localhost:" + port);
    }


}
