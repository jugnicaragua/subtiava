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
            LocalDate fieldValue = convertDateParam(date);
            LocalDate referenceDate = LocalDate.now().plusYears(yearsToAdd);
            return fieldValue.compareTo(referenceDate);
        }

        @Override
        LocalDate convertDateParam(Object date) {
            if (date instanceof java.sql.Date) {
                return ((java.sql.Date) date).toLocalDate();
            } else if (date instanceof LocalDate) {
                return (LocalDate) date;
            } else {
                throw new IllegalArgumentException(String.format(UNSUPPORTED_DATE_TYPE, date.getClass().getName()));
            }
        }

        @Override
        public int getYear(Object date) {
            return convertDateParam(date).getYear();
        }

        @Override
        public int getMonth(Object date) {
            return convertDateParam(date).getMonthValue();
        }

        @Override
        public int getDay(Object date) {
            return convertDateParam(date).getDayOfMonth();
        }
    },
    ZONED_TIMESTAMP(GregorianCalendar.class, ZonedDateTime.class) {
        @Override
        int doCompareDateToReferenceDate(Object date, long yearsToAdd) {
            LocalDateTime fieldValue = convertDateParam(date);
            LocalDateTime referenceDate = null;

            if (date instanceof Calendar) {
                Calendar calendar = (Calendar) date;
                referenceDate = LocalDateTime.now(calendar.getTimeZone().toZoneId()).plusYears(yearsToAdd);
            } else if (date instanceof ZonedDateTime) {
                ZonedDateTime zoned = (ZonedDateTime) date;
                referenceDate = ZonedDateTime.now(zoned.getZone()).plusYears(yearsToAdd).toLocalDateTime();
            } else {
                throw new IllegalArgumentException(String.format(UNSUPPORTED_DATE_TYPE, date.getClass().getName()));
            }

            return fieldValue.compareTo(referenceDate);
        }

        @Override
        LocalDateTime convertDateParam(Object date) {
            if (date instanceof Calendar) {
                Calendar calendar = (Calendar) date;
                return LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());
            } else if (date instanceof ZonedDateTime) {
                return ((ZonedDateTime) date).toLocalDateTime();
            } else {
                throw new IllegalArgumentException(String.format(UNSUPPORTED_DATE_TYPE, date.getClass().getName()));
            }
        }

        @Override
        public int getYear(Object date) {
            return convertDateParam(date).getYear();
        }

        @Override
        public int getMonth(Object date) {
            return convertDateParam(date).getMonthValue();
        }

        @Override
        public int getDay(Object date) {
            return convertDateParam(date).getDayOfMonth();
        }
    },
    TIMESTAMP(java.sql.Timestamp.class, Date.class, LocalDateTime.class) {
        @Override
        int doCompareDateToReferenceDate(Object date, long yearsToAdd) {
            LocalDateTime referenceDate = LocalDateTime.now().plusYears(yearsToAdd);
            LocalDateTime fieldValue = convertDateParam(date);
            return fieldValue.compareTo(referenceDate);
        }

        @Override
        LocalDateTime convertDateParam(Object date) {
            if (date instanceof java.sql.Timestamp) {
                return ((java.sql.Timestamp) date).toLocalDateTime();
            } else if (date instanceof Date) {
                return ((Date) date).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            } else if (date instanceof LocalDateTime) {
                return (LocalDateTime) date;
            } else {
                throw new IllegalArgumentException(String.format(UNSUPPORTED_DATE_TYPE, date.getClass().getName()));
            }
        }

        @Override
        public int getYear(Object date) {
            return convertDateParam(date).getYear();
        }

        @Override
        public int getMonth(Object date) {
            return convertDateParam(date).getMonthValue();
        }

        @Override
        public int getDay(Object date) {
            return convertDateParam(date).getDayOfMonth();
        }
    };

    private static final String UNSUPPORTED_DATE_TYPE = "Unsupported date type %s";

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

    abstract Object convertDateParam(Object date);

    public abstract int getYear(Object date);

    public abstract int getMonth(Object date);

    public abstract int getDay(Object date);

    public static DateComparator ofType(Class<?> type) {
        DateComparator constraint = CACHE.get(type);
        if (constraint == null) {
            throw new IllegalArgumentException(String.format(UNSUPPORTED_DATE_TYPE, type.getName()));
        }
        return constraint;
    }
}
