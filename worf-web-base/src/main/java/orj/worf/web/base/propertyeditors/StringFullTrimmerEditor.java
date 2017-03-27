package orj.worf.web.base.propertyeditors;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;

public class StringFullTrimmerEditor extends StringTrimmerEditor {
	private final String stringToDelete;
	private final boolean fullDelete;
	private final boolean emptyAsNull;

	public StringFullTrimmerEditor(final String stringToDelete, final boolean fullDelete, final boolean emptyAsNull) {
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
					setValue(value);
				}
			}
		} else {
			super.setAsText(text);
		}
	}
}