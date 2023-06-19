package ua.mibal.googleSpreadsheetsToDb;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
@Configuration
@ComponentScan("ua.mibal.googleSpreadsheetsToDb")
@PropertySource("classpath:application.properties")
public class SpringConfig {
}
