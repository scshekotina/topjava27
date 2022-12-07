package ru.javawebinar.topjava.util.formatConverter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private static final String PATTERN = "yyyy-MM-dd";

    @Nullable
    @Override
    public LocalDate convert(String source) {
        return LocalDate.parse(source, DateTimeFormatter.ofPattern(PATTERN));
    }
}
