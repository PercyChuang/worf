package orj.worf.web.base.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.StringUtils;

public class CustomRESTDateEditor extends PropertyEditorSupport {
	private final boolean allowEmpty;
	private final int exactDateLength;
	private final String[] patterns = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };

	public CustomRESTDateEditor(final boolean allowEmpty, final int exactDateLength) {
		this.allowEmpty = allowEmpty;
		this.exactDateLength = exactDateLength;
	}

	@Override
	public void setAsText(final String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text) || StringUtils.hasText(text)
				&& "rest_empty".equals(text.trim())) {
			setValue(null);
		} else {
			if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
				throw new IllegalArgumentException("Could not parse date: it is not exactly" + this.exactDateLength
						+ "characters long");
			}
			try {
				setValue(DateUtils.parseDate(text, this.patterns));
			} catch (ParseException ex) {
				throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
			}
		}
	}

	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		return value != null ? DateFormatUtils.format(value, patterns[1]) : "";
	}
}