package com.thiscompany.ttrack.enums.converter;

import jakarta.persistence.AttributeConverter;
import org.springframework.context.i18n.LocaleContextHolder;

@SuppressWarnings("ConverterNotAnnotatedInspection")
public abstract class GenericParamConverter<T extends Enum<T>> implements AttributeConverter<T, String> {
	
	private final Class<T> targetEnum;
	
	protected GenericParamConverter(Class<T> targetEnum) {
		this.targetEnum = targetEnum;
	}
	
	@Override
	public String convertToDatabaseColumn(T attribute) {
		if (attribute == null) {
			throw new IllegalArgumentException("Unable to convert attribute: " + attribute);
		}
		return attribute.name().toLowerCase(LocaleContextHolder.getLocale());
	}
	
	@Override
	public T convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		try {
			return Enum.valueOf(targetEnum, dbData.toUpperCase(LocaleContextHolder.getLocale()));
		}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Unable to convert data: " + dbData);
		}
	}
	
}
