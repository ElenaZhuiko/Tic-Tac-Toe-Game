package main.java;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("main.java")
@PropertySource("classpath:VisualizationTelegram.properties")
public class SpringConfig {
}
