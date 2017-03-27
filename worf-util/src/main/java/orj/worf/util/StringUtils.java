package orj.worf.util;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final String US_ASCII = "US-ASCII";
    private static final String ISO_LATIN1 = "ISO-8859-1";
    private static final String UTF16 = "UTF-16";
    private static final String UTF16BE = "UTF-16BE";
    private static final String UTF16LE = "UTF-16LE";

    public static byte[] getBytesIso8859_1(final String string) {
        return getBytesUnchecked(string, ISO_LATIN1);
    }

    public static byte[] getBytesUsAscii(final String string) {
        return getBytesUnchecked(string, US_ASCII);
    }

    public static byte[] getBytesUtf16(final String string) {
        return getBytesUnchecked(string, UTF16);
    }

    public static byte[] getBytesUtf16Be(final String string) {
        return getBytesUnchecked(string, UTF16BE);
    }

    public static byte[] getBytesUtf16Le(final String string) {
        return getBytesUnchecked(string, UTF16LE);
    }

    public static byte[] getBytesUtf8(final String string) {
        return getBytesUnchecked(string, "UTF-8");
    }

    public static byte[] getBytesUnchecked(final String string, final String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw newIllegalStateException(charsetName, e);
        }
    }

    private static IllegalStateException newIllegalStateException(final String charsetName,
            final UnsupportedEncodingException e) {
        return new IllegalStateException(charsetName + ": " + e);
    }

    public static String newString(final byte[] bytes, final String charsetName) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw newIllegalStateException(charsetName, e);
        }
    }

    public static String newStringIso8859_1(final byte[] bytes) {
        return newString(bytes, ISO_LATIN1);
    }

    public static String newStringUsAscii(final byte[] bytes) {
        return newString(bytes, US_ASCII);
    }

    public static String newStringUtf16(final byte[] bytes) {
        return newString(bytes, UTF16);
    }

    public static String newStringUtf16Be(final byte[] bytes) {
        return newString(bytes, UTF16BE);
    }

    public static String newStringUtf16Le(final byte[] bytes) {
        return newString(bytes, UTF16LE);
    }

    public static String newStringUtf8(final byte[] bytes) {
        return newString(bytes, "UTF-8");
    }

    public static String appendWithOSLineSeparator(final List<String> str) {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        for (String s : str) {
            sb.append(s);
            sb.append(lineSeparator);
        }
        return sb.toString();
    }

    /**
     * Trim leading and trailing whitespace from the given String.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimWhitespace(final String str) {
        return org.springframework.util.StringUtils.trimWhitespace(str);
    }

    /**
     * Trim <i>all</i> whitespace from the given String: leading, trailing, and in between characters.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimAllWhitespace(final String str) {
        return org.springframework.util.StringUtils.trimAllWhitespace(str);
    }

    /**
     * Trim leading whitespace from the given String.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimLeadingWhitespace(final String str) {
        return org.springframework.util.StringUtils.trimLeadingWhitespace(str);
    }

    /**
     * Trim trailing whitespace from the given String.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimTrailingWhitespace(final String str) {
        return org.springframework.util.StringUtils.trimTrailingWhitespace(str);
    }

    /**
     * Trim all occurrences of the supplied leading character from the given String.
     *
     * @param str the String to check
     * @param leadingCharacter the leading character to be trimmed
     * @return the trimmed String
     */
    public static String trimLeadingCharacter(final String str, final char leadingCharacter) {
        return org.springframework.util.StringUtils.trimLeadingCharacter(str, leadingCharacter);
    }

    /**
     * Trim all occurrences of the supplied trailing character from the given String.
     *
     * @param str the String to check
     * @param trailingCharacter the trailing character to be trimmed
     * @return the trimmed String
     */
    public static String trimTrailingCharacter(final String str, final char trailingCharacter) {
        return org.springframework.util.StringUtils.trimTrailingCharacter(str, trailingCharacter);
    }

    /**
     * Test whether the given string matches the given substring at the given index.
     *
     * @param str the original string (or StringBuilder)
     * @param index the index in the original string to start matching against
     * @param substring the substring to match at the given index
     */
    public static boolean substringMatch(final CharSequence str, final int index, final CharSequence substring) {
        return org.springframework.util.StringUtils.substringMatch(str, index, substring);
    }

    /**
     * Count the occurrences of the substring in string s.
     *
     * @param str string to search in. Return 0 if this is null.
     * @param sub string to search for. Return 0 if this is null.
     */
    public static int countOccurrencesOf(final String str, final String sub) {
        return org.springframework.util.StringUtils.countOccurrencesOf(str, sub);
    }

    /**
     * Delete all occurrences of the given substring.
     *
     * @param inString the original String
     * @param pattern the pattern to delete all occurrences of
     * @return the resulting String
     */
    public static String delete(final String inString, final String pattern) {
        return org.springframework.util.StringUtils.delete(inString, pattern);
    }

    /**
     * Delete any character in a given String.
     *
     * @param inString the original String
     * @param charsToDelete a set of characters to delete. E.g. "az\n" will delete 'a's, 'z's and new lines.
     * @return the resulting String
     */
    public static String deleteAny(final String inString, final String charsToDelete) {
        return org.springframework.util.StringUtils.deleteAny(inString, charsToDelete);
    }

    // ---------------------------------------------------------------------
    // Convenience methods for working with formatted Strings
    // ---------------------------------------------------------------------

    /**
     * Quote the given String with single quotes.
     *
     * @param str the input String (e.g. "myString")
     * @return the quoted String (e.g. "'myString'"), or {@code null} if the input was {@code null}
     */
    public static String quote(final String str) {
        return org.springframework.util.StringUtils.quote(str);
    }

    /**
     * Turn the given Object into a String with single quotes if it is a String; keeping the Object as-is else.
     *
     * @param obj the input Object (e.g. "myString")
     * @return the quoted String (e.g. "'myString'"), or the input object as-is if not a String
     */
    public static Object quoteIfString(final Object obj) {
        return org.springframework.util.StringUtils.quoteIfString(obj);
    }

    /**
     * Unqualify a string qualified by a '.' dot character. For example, "this.name.is.qualified", returns "qualified".
     *
     * @param qualifiedName the qualified name
     */
    public static String unqualify(final String qualifiedName) {
        return org.springframework.util.StringUtils.unqualify(qualifiedName);
    }

    /**
     * Unqualify a string qualified by a separator character. For example, "this:name:is:qualified" returns "qualified"
     * if using a ':' separator.
     *
     * @param qualifiedName the qualified name
     * @param separator the separator
     */
    public static String unqualify(final String qualifiedName, final char separator) {
        return org.springframework.util.StringUtils.unqualify(qualifiedName, separator);
    }

    /**
     * Extract the filename from the given path, e.g. "mypath/myfile.txt" -> "myfile.txt".
     *
     * @param path the file path (may be {@code null})
     * @return the extracted filename, or {@code null} if none
     */
    public static String getFilename(final String path) {
        return org.springframework.util.StringUtils.getFilename(path);
    }

    /**
     * Extract the filename extension from the given path, e.g. "mypath/myfile.txt" -> "txt".
     *
     * @param path the file path (may be {@code null})
     * @return the extracted filename extension, or {@code null} if none
     */
    public static String getFilenameExtension(final String path) {
        return org.springframework.util.StringUtils.getFilenameExtension(path);
    }

    /**
     * Strip the filename extension from the given path, e.g. "mypath/myfile.txt" -> "mypath/myfile".
     *
     * @param path the file path (may be {@code null})
     * @return the path with stripped filename extension, or {@code null} if none
     */
    public static String stripFilenameExtension(final String path) {
        return org.springframework.util.StringUtils.stripFilenameExtension(path);
    }

    /**
     * Apply the given relative path to the given path, assuming standard Java folder separation (i.e. "/" separators).
     *
     * @param path the path to start from (usually a full file path)
     * @param relativePath the relative path to apply (relative to the full file path above)
     * @return the full file path that results from applying the relative path
     */
    public static String applyRelativePath(final String path, final String relativePath) {
        return org.springframework.util.StringUtils.applyRelativePath(path, relativePath);
    }

    /**
     * Normalize the path by suppressing sequences like "path/.." and inner simple dots. <p>The result is convenient for
     * path comparison. For other uses, notice that Windows separators ("\") are replaced by simple slashes.
     *
     * @param path the original path
     * @return the normalized path
     */
    public static String cleanPath(final String path) {
        return org.springframework.util.StringUtils.cleanPath(path);
    }

    /**
     * Compare two paths after normalization of them.
     *
     * @param path1 first path for comparison
     * @param path2 second path for comparison
     * @return whether the two paths are equivalent after normalization
     */
    public static boolean pathEquals(final String path1, final String path2) {
        return org.springframework.util.StringUtils.pathEquals(path1, path2);
    }

    /**
     * Parse the given {@code localeString} value into a {@link Locale}. <p>This is the inverse operation of
     * {@link Locale#toString Locale's toString}.
     *
     * @param localeString the locale String, following {@code Locale's} {@code toString()} format ("en", "en_UK", etc);
     *        also accepts spaces as separators, as an alternative to underscores
     * @return a corresponding {@code Locale} instance
     * @throws IllegalArgumentException in case of an invalid locale specification
     */
    public static Locale parseLocaleString(final String localeString) {
        return org.springframework.util.StringUtils.parseLocaleString(localeString);
    }

    /**
     * Determine the RFC 3066 compliant language tag, as used for the HTTP "Accept-Language" header.
     *
     * @param locale the Locale to transform to a language tag
     * @return the RFC 3066 compliant language tag as String
     */
    public static String toLanguageTag(final Locale locale) {
        return org.springframework.util.StringUtils.toLanguageTag(locale);
    }

    /**
     * Parse the given {@code timeZoneString} value into a {@link TimeZone}.
     *
     * @param timeZoneString the time zone String, following {@link TimeZone#getTimeZone(String)} but throwing
     *        {@link IllegalArgumentException} in case of an invalid time zone specification
     * @return a corresponding {@link TimeZone} instance
     * @throws IllegalArgumentException in case of an invalid time zone specification
     */
    public static TimeZone parseTimeZoneString(final String timeZoneString) {
        return org.springframework.util.StringUtils.parseTimeZoneString(timeZoneString);
    }

    // ---------------------------------------------------------------------
    // Convenience methods for working with String arrays
    // ---------------------------------------------------------------------

    /**
     * Append the given String to the given String array, returning a new array consisting of the input array contents
     * plus the given String.
     *
     * @param array the array to append to (can be {@code null})
     * @param str the String to append
     * @return the new array (never {@code null})
     */
    public static String[] addStringToArray(final String[] array, final String str) {
        return org.springframework.util.StringUtils.addStringToArray(array, str);
    }

    /**
     * Concatenate the given String arrays into one, with overlapping array elements included twice. <p>The order of
     * elements in the original arrays is preserved.
     *
     * @param array1 the first array (can be {@code null})
     * @param array2 the second array (can be {@code null})
     * @return the new array ({@code null} if both given arrays were {@code null})
     */
    public static String[] concatenateStringArrays(final String[] array1, final String[] array2) {
        return org.springframework.util.StringUtils.concatenateStringArrays(array1, array2);
    }

    /**
     * Merge the given String arrays into one, with overlapping array elements only included once. <p>The order of
     * elements in the original arrays is preserved (with the exception of overlapping elements, which are only included
     * on their first occurrence).
     *
     * @param array1 the first array (can be {@code null})
     * @param array2 the second array (can be {@code null})
     * @return the new array ({@code null} if both given arrays were {@code null})
     */
    public static String[] mergeStringArrays(final String[] array1, final String[] array2) {
        return org.springframework.util.StringUtils.mergeStringArrays(array1, array2);
    }

    /**
     * Turn given source String array into sorted array.
     *
     * @param array the source array
     * @return the sorted array (never {@code null})
     */
    public static String[] sortStringArray(final String[] array) {
        return org.springframework.util.StringUtils.sortStringArray(array);
    }

    /**
     * Copy the given Collection into a String array. The Collection must contain String elements only.
     *
     * @param collection the Collection to copy
     * @return the String array ({@code null} if the passed-in Collection was {@code null})
     */
    public static String[] toStringArray(final Collection<String> collection) {
        return org.springframework.util.StringUtils.toStringArray(collection);
    }

    /**
     * Copy the given Enumeration into a String array. The Enumeration must contain String elements only.
     *
     * @param enumeration the Enumeration to copy
     * @return the String array ({@code null} if the passed-in Enumeration was {@code null})
     */
    public static String[] toStringArray(final Enumeration<String> enumeration) {
        return org.springframework.util.StringUtils.toStringArray(enumeration);
    }

    /**
     * Trim the elements of the given String array, calling {@code String.trim()} on each of them.
     *
     * @param array the original String array
     * @return the resulting array (of the same size) with trimmed elements
     */
    public static String[] trimArrayElements(final String[] array) {
        return org.springframework.util.StringUtils.trimArrayElements(array);
    }

    /**
     * Remove duplicate Strings from the given array. Also sorts the array, as it uses a TreeSet.
     *
     * @param array the String array
     * @return an array without duplicates, in natural sort order
     */
    public static String[] removeDuplicateStrings(final String[] array) {
        return org.springframework.util.StringUtils.removeDuplicateStrings(array);
    }

    /**
     * Split a String at the first occurrence of the delimiter. Does not include the delimiter in the result.
     *
     * @param toSplit the string to split
     * @param delimiter to split the string up with
     * @return a two element array with index 0 being before the delimiter, and index 1 being after the delimiter
     *         (neither element includes the delimiter); or {@code null} if the delimiter wasn't found in the given
     *         input String
     */
    public static String[] split(final String toSplit, final String delimiter) {
        return org.springframework.util.StringUtils.split(toSplit, delimiter);
    }

    /**
     * Take an array Strings and split each element based on the given delimiter. A {@code Properties} instance is then
     * generated, with the left of the delimiter providing the key, and the right of the delimiter providing the value.
     * <p>Will trim both the key and value before adding them to the {@code Properties} instance.
     *
     * @param array the array to process
     * @param delimiter to split each element using (typically the equals symbol)
     * @return a {@code Properties} instance representing the array contents, or {@code null} if the array to process
     *         was null or empty
     */
    public static Properties splitArrayElementsIntoProperties(final String[] array, final String delimiter) {
        return org.springframework.util.StringUtils.splitArrayElementsIntoProperties(array, delimiter);
    }

    /**
     * Take an array Strings and split each element based on the given delimiter. A {@code Properties} instance is then
     * generated, with the left of the delimiter providing the key, and the right of the delimiter providing the value.
     * <p>Will trim both the key and value before adding them to the {@code Properties} instance.
     *
     * @param array the array to process
     * @param delimiter to split each element using (typically the equals symbol)
     * @param charsToDelete one or more characters to remove from each element prior to attempting the split operation
     *        (typically the quotation mark symbol), or {@code null} if no removal should occur
     * @return a {@code Properties} instance representing the array contents, or {@code null} if the array to process
     *         was {@code null} or empty
     */
    public static Properties splitArrayElementsIntoProperties(final String[] array, final String delimiter,
            final String charsToDelete) {
        return org.springframework.util.StringUtils.splitArrayElementsIntoProperties(array, delimiter, charsToDelete);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer. Trims tokens and omits empty tokens. <p>The
     * given delimiters string is supposed to consist of any number of delimiter characters. Each of those characters
     * can be used to separate tokens. A delimiter is always a single character; for multi-character delimiters,
     * consider using {@code delimitedListToStringArray}
     *
     * @param str the String to tokenize
     * @param delimiters the delimiter characters, assembled as String (each of those characters is individually
     *        considered as delimiter).
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(final String str, final String delimiters) {
        return org.springframework.util.StringUtils.tokenizeToStringArray(str, delimiters);
    }

    /**
     * Tokenize the given String into a String array via a StringTokenizer. <p>The given delimiters string is supposed
     * to consist of any number of delimiter characters. Each of those characters can be used to separate tokens. A
     * delimiter is always a single character; for multi-character delimiters, consider using
     * {@code delimitedListToStringArray}
     *
     * @param str the String to tokenize
     * @param delimiters the delimiter characters, assembled as String (each of those characters is individually
     *        considered as delimiter)
     * @param trimTokens trim the tokens via String's {@code trim}
     * @param ignoreEmptyTokens omit empty tokens from the result array (only applies to tokens that are empty after
     *        trimming; StringTokenizer will not consider subsequent delimiters as token in the first place).
     * @return an array of the tokens ({@code null} if the input String was {@code null})
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see #delimitedListToStringArray
     */
    public static String[] tokenizeToStringArray(final String str, final String delimiters, final boolean trimTokens,
            final boolean ignoreEmptyTokens) {
        return org.springframework.util.StringUtils.tokenizeToStringArray(str, delimiters, trimTokens,
                ignoreEmptyTokens);
    }

    /**
     * Take a String which is a delimited list and convert it to a String array. <p>A single delimiter can consists of
     * more than one character: It will still be considered as single delimiter string, rather than as bunch of
     * potential delimiter characters - in contrast to {@code tokenizeToStringArray}.
     *
     * @param str the input String
     * @param delimiter the delimiter between elements (this is a single delimiter, rather than a bunch individual
     *        delimiter characters)
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(final String str, final String delimiter) {
        return org.springframework.util.StringUtils.delimitedListToStringArray(str, delimiter);
    }

    /**
     * Take a String which is a delimited list and convert it to a String array. <p>A single delimiter can consists of
     * more than one character: It will still be considered as single delimiter string, rather than as bunch of
     * potential delimiter characters - in contrast to {@code tokenizeToStringArray}.
     *
     * @param str the input String
     * @param delimiter the delimiter between elements (this is a single delimiter, rather than a bunch individual
     *        delimiter characters)
     * @param charsToDelete a set of characters to delete. Useful for deleting unwanted line breaks: e.g. "\r\n\f" will
     *        delete all new lines and line feeds in a String.
     * @return an array of the tokens in the list
     * @see #tokenizeToStringArray
     */
    public static String[] delimitedListToStringArray(final String str, final String delimiter,
            final String charsToDelete) {
        return org.springframework.util.StringUtils.delimitedListToStringArray(str, delimiter, charsToDelete);
    }

    /**
     * Convert a CSV list into an array of Strings.
     *
     * @param str the input String
     * @return an array of Strings, or the empty array in case of empty input
     */
    public static String[] commaDelimitedListToStringArray(final String str) {
        return org.springframework.util.StringUtils.commaDelimitedListToStringArray(str);
    }

    /**
     * Convenience method to convert a CSV string list to a set. Note that this will suppress duplicates.
     *
     * @param str the input String
     * @return a Set of String entries in the list
     */
    public static Set<String> commaDelimitedListToSet(final String str) {
        return org.springframework.util.StringUtils.commaDelimitedListToSet(str);
    }

    /**
     * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for {@code toString()}
     * implementations.
     *
     * @param coll the Collection to display
     * @param delim the delimiter to use (probably a ",")
     * @param prefix the String to start each element with
     * @param suffix the String to end each element with
     * @return the delimited String
     */
    public static String collectionToDelimitedString(final Collection<?> coll, final String delim, final String prefix,
            final String suffix) {
        return org.springframework.util.StringUtils.collectionToDelimitedString(coll, delim, prefix, suffix);
    }

    /**
     * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for {@code toString()}
     * implementations.
     *
     * @param coll the Collection to display
     * @param delim the delimiter to use (probably a ",")
     * @return the delimited String
     */
    public static String collectionToDelimitedString(final Collection<?> coll, final String delim) {
        return org.springframework.util.StringUtils.collectionToDelimitedString(coll, delim);
    }

    /**
     * Convenience method to return a Collection as a CSV String. E.g. useful for {@code toString()} implementations.
     *
     * @param coll the Collection to display
     * @return the delimited String
     */
    public static String collectionToCommaDelimitedString(final Collection<?> coll) {
        return org.springframework.util.StringUtils.collectionToCommaDelimitedString(coll);
    }

    /**
     * Convenience method to return a String array as a delimited (e.g. CSV) String. E.g. useful for {@code toString()}
     * implementations.
     *
     * @param arr the array to display
     * @param delim the delimiter to use (probably a ",")
     * @return the delimited String
     */
    public static String arrayToDelimitedString(final Object[] arr, final String delim) {
        return org.springframework.util.StringUtils.arrayToDelimitedString(arr, delim);
    }

    /**
     * Convenience method to return a String array as a CSV String. E.g. useful for {@code toString()} implementations.
     *
     * @param arr the array to display
     * @return the delimited String
     */
    public static String arrayToCommaDelimitedString(final Object[] arr) {
        return org.springframework.util.StringUtils.arrayToCommaDelimitedString(arr);
    }
}
