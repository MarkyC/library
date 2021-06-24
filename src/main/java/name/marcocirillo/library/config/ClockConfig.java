package name.marcocirillo.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Global Clock Configuration for the application.
 *
 * This makes classes that use Instant.now() (and friends) more testable,
 * since you'd use Instant.now(someClock), where someClock can be mocked/manipulated during tests
 */
@Configuration
public class ClockConfig {
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
