package hulio13.articlesApi.domain.utils;

import java.util.regex.Pattern;

public class UrlValidator {
    private final static Pattern startsWithHttp = Pattern.compile(
            "^https?:\\/\\/(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]" +
                    "{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&\\/=]*)$"
    );

    private final static Pattern notStartsWithHttp = Pattern.compile(
            "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&//=]*)$"
    );

    public static boolean IsUrlValid(String value) {
        return !startsWithHttp.matcher(value).find() &&
                !notStartsWithHttp.matcher(value).find();
    }
}
