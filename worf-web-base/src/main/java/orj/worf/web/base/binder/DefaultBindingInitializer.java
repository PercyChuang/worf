package orj.worf.web.base.binder;

import java.util.Date;

import orj.worf.web.base.propertyeditors.CustomDateEditor;
import orj.worf.web.base.propertyeditors.CustomRESTNumberEditor;
import orj.worf.web.base.propertyeditors.CustomStringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

public class DefaultBindingInitializer extends ConfigurableWebBindingInitializer implements WebBindingInitializer {
	@Override
	public void initBinder(final WebDataBinder binder, final WebRequest request) {
		super.initBinder(binder, request);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(true, -1));
		binder.registerCustomEditor(String.class, new CustomStringTrimmerEditor("rest_empty", true, false));
		binder.registerCustomEditor(Integer.class, new CustomRESTNumberEditor(Integer.class, true));
	}
}