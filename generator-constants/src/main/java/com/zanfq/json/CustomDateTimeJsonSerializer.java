package com.zanfq.json;

import java.io.IOException;
import java.util.Date;

import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomDateTimeJsonSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		if(null != value){
			gen.writeString(LocalDateTime.fromDateFields(value).toString("yyy-MM-dd HH:mm:ss"));
		}
	}
	
}
