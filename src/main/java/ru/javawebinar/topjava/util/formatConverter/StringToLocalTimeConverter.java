package ru.javawebinar.topjava.util.formatConverter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {

    private static final String PATTERN = "HH:mm";

    @Nullable
    @Override
    public LocalTime convert(String source) {
        return LocalTime.parse(source, DateTimeFormatter.ofPattern(PATTERN));
    }
}
