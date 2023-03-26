package com.user.helper;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
	
	private String sender;
	private String recipient;
	private String msgBody;
	private String subject;
	private String attachment;
	private Map<String,Object> properties;
}
