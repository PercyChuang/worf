package orj.worf.web.base.propertyeditors;

import orj.worf.web.base.interceptor.FilterStatus;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.util.HtmlUtils;

public class CustomStringTrimmerEditor extends StringTrimmerEditor {
    private final String stringToDelete;
    private final boolean fullDelete;
    private final boolean emptyAsNull;

    public CustomStringTrimmerEditor(final String stringToDelete, final boolean fullDelete, final boolean emptyAsNull) {
        super(stringToDelete, emptyAsNull);
        this.stringToDelete = stringToDelete;
        this.fullDelete = fullDelete;
        this.emptyAsNull = emptyAsNull;
    }

    @Override
    public void setAsText(final String text) {
        if (this.fullDelete) {
            if (text == null) {
                setValue(null);
            } else {
                String value = text.trim();
                if (this.stringToDelete != null && this.stringToDelete.equals(value)) {
                    value = null;
                }
                if (this.emptyAsNull && value.isEmpty()) {
                    setValue(null);
                } else {
                    setValue(escape(value));
                }
            }
        } else {
            super.setAsText(escape(text));
        }
    }

    private String escape(final String value) {
        if (FilterStatus.basicPreventionLoaded) {
            return value;
        }
        return HtmlUtils.htmlEscape(value);
    }
}