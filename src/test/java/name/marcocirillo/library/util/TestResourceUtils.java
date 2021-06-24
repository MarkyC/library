package name.marcocirillo.library.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.io.UncheckedIOException;

@Component
public class TestResourceUtils {
    @Autowired
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

    public String objectToString(Object o) {
        try {
            return jackson2HttpMessageConverter.getObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
