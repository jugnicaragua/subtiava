package ni.org.jug.subtiava.validation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author aalaniz
 * @version 1.0
 */
enum DateComparator {
    DATE(java.sql.Date.class, LocalDate.class) {
        @Override
        int doCompareDateToReferenceDate(Object date, long yearsToAdd) {
            LocalDate fieldValue = date instanceof java.sql.Date ? ((java.sql.Date) date).toLocalDate() : (LocalDate) date;
            LocalDate referenceDate = LocalDate.now().plusYears(yearsToAdd);
            return fieldValue.compareTo(referenceDate);
        }
    },
    CALENDAR(GregorianCalendar.class) {
        @Override
        int doCompareDateToReferenceDate(Object date, long yearsToAdd) {
            Calendar calendar = (Calendar) date;
            LocalDateTime fieldValue = LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
            LocalDateTime referenceDate = LocalDateTime.now(calendar.getTimeZone().toZoneId()).plusYears(yearsToAdd);
            return fieldValue.compareTo(referenceDate);
        }
    },
    ZONED(ZonedDateTime.class) {
        @Override
        int doCompareDateToReferenceDate(Object date, long yearsToAdd) {
            ZonedDateTime fieldValue = (ZonedDateTime) date;
            ZonedDateTime referenceDate = ZonedDateTime.now(fieldValue.getZone()).plusYears(yearsToAdd);
            return fieldValue.compareTo(referenceDate);
        }
    },
    TIMESTAMP(java.sql.Timestamp.class, Date.class, LocalDateTime.class) {
        @Override
        int doCompareDateToReferenceDate(Object date, long yearsToAdd) {
            LocalDateTime referenceDate = LocalDateTime.now().plusYears(yearsToAdd);
            LocalDateTime fieldValue = null;

            if (date instanceof java.sql.Timestamp) {
                fieldValue = ((java.sql.Timestamp) date).toLocalDateTime();
            } else if (date instanceof Date) {
                fieldValue = ((Date) date).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            } else if (date instanceof LocalDateTime) {
                fieldValue = (LocalDateTime) date;
            }

            return fieldValue.compareTo(referenceDate);
        }
    };

    private static final String UNSUPPORTED_DATE_TYPE = "Can't verify the value, unsupported date type %s";

    private static final Map<Class<?>, DateComparator> CACHE = new HashMap<>();
    static {
        for (DateComparator constraint : DateComparator.values()) {
            for (Class<?> clazz : constraint.types) {
                CACHE.put(clazz, constraint);
            }
        }
    }

    private final List<Class<?>> types;

    DateComparator(Class<?>... types) {
        this.types = Arrays.asList(types);
    }

    public boolean isBefore(Object date) {
        return compareDateToReferenceDate(date) < 0;
    }

    public boolean isBeforeOrEqual(Object date) {
        return compareDateToReferenceDate(date) <= 0;
    }

    public boolean isAfter(Object date) {
        return compareDateToReferenceDate(date) > 0;
    }

    public boolean isAfterOrEqual(Object date) {
        return compareDateToReferenceDate(date) >= 0;
    }

    public boolean isGreaterThanOrEqualToAge(Object date, int age) {
        return compareDateToReferenceDate(date, -age) <= 0;
    }

    public boolean isLessThanOrEqualToAge(Object date, int age) {
        return compareDateToReferenceDate(date, -age) >= 0;
    }

    public boolean isEqualToAge(Object date, int age) {
        return compareDateToReferenceDate(date, -age) <= 0 && compareDateToReferenceDate(date, -age - 1) > 0;
    }

    private int compareDateToReferenceDate(Object date) {
        return compareDateToReferenceDate(date, 0);
    }

    private int compareDateToReferenceDate(Object date, long yearsToAdd) {
        Class<?> type = Objects.requireNonNull(date, "[date] is required").getClass();
        if (!types.contains(type)) {
            throw new IllegalArgumentException(String.format(UNSUPPORTED_DATE_TYPE, type.getName()));
        }
        return doCompareDateToReferenceDate(date, yearsToAdd);
    }

    abstract int doCompareDateToReferenceDate(Object date, long yearsToAdd);

    public static DateComparator ofType(Class<?> type) {
        DateComparator constraint = CACHE.get(type);
        if (constraint == null) {
            throw new IllegalArgumentException(String.format(UNSUPPORTED_DATE_TYPE, type.getName()));
        }
        return constraint;
    }
}
