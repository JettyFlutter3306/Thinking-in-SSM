package cn.element.web.test;

import cn.element.web.servlet.ResponseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class TestObjectMapper {
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void testObjectMapper() throws JsonProcessingException {
        ResponseEntity<Void> entity = ResponseEntity.notFound();
        String s = mapper.writeValueAsString(entity);
        log.debug("返回的数据: {}", s);
    }

}
