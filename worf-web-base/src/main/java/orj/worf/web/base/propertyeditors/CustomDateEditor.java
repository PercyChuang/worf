package orj.worf.web.base.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;

import orj.worf.util.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;

public class CustomDateEditor extends PropertyEditorSupport {
    private final boolean allowEmpty;
    private final int exactDateLength;
    private final String[] patterns = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };

    public CustomDateEditor(final boolean allowEmpty, final int exactDateLength) {
        this.allowEmpty = allowEmpty;
        this.exactDateLength = exactDateLength;
    }

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        if (this.allowEmpty && StringUtils.isBlank(text) || "rest_empty".equals(text)) {
            setValue(null);
        } else {
            if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
                throw new IllegalArgumentException("Could not parse date: it is not exactly" + this.exactDateLength
                        + "characters long");
            }
            for (String pattern : patterns) {
                try {
                    setValue(DateFormatUtils.parse(pattern, text));
                    return;
                } catch (ParseException ex) {
                }
            }
            throw new IllegalArgumentException("Could not parse date: " + text);
        }
    }

    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return value != null ? DateFormatUtils.format(value, patterns[1]) : "";
    }
}