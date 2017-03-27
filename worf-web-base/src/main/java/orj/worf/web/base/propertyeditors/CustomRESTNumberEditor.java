package orj.worf.web.base.propertyeditors;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.util.StringUtils;

public class CustomRESTNumberEditor extends CustomNumberEditor {
	public CustomRESTNumberEditor(final Class<? extends Number> numberClass, final boolean allowEmpty)
			throws IllegalArgumentException {
		super(numberClass, allowEmpty);
	}

	@Override
	public void setAsText(final String text) throws IllegalArgumentException {
		if (StringUtils.hasText(text) && "rest_empty".equals(text.trim())) {
			setValue(null);
		} else {
			super.setAsText(text);
		}
	}
}