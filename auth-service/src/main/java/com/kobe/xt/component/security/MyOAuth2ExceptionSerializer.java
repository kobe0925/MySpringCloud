package com.kobe.xt.component.security;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.kobe.xt.exception.MyOAuth2Exception;
import com.kobe.xt.response.ResponseDTO;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 异常序列化器
 * @date 2022/9/22 14:01
 */
@Component
public class MyOAuth2ExceptionSerializer extends StdSerializer<MyOAuth2Exception> {

    public MyOAuth2ExceptionSerializer(){
        super(MyOAuth2Exception.class);
    }

    @Override
    public void serialize(MyOAuth2Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ResponseDTO dto = new ResponseDTO();
        dto.setMessage(e.getMessage());
        dto.setResult(false);
        dto.setRespCode(e.getHttpErrorCode());
        jsonGenerator.writeObject(dto);
    }
}
