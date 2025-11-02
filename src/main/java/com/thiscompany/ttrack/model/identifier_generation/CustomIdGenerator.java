package com.thiscompany.ttrack.model.identifier_generation;

import com.fasterxml.uuid.Generators;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.util.Properties;

public class CustomIdGenerator implements IdentifierGenerator {
	
	private String prefix;
	
	@Override
	public Object generate(SharedSessionContractImplementor ssci, Object o) {
		String uuid = Generators.timeBasedEpochGenerator().generate().toString();
		return this.prefix + uuid;
	}
	
	@Override
	public void configure(Type type, Properties parameters, ServiceRegistry serviceRegistry) {
		prefix = parameters.getProperty("prefix");
	}
	
}
