package orj.worf.core.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class PrefixResourceBundleMessageSource extends ResourceBundleMessageSource {
    private static List<String> localeSuffixes;
    private Set<String> basenames;

    static {
        Locale[] availableLocales = Locale.getAvailableLocales();
        localeSuffixes = new ArrayList<String>(availableLocales.length);
        for (Locale availableLocale : availableLocales) {
            localeSuffixes.add("_" + availableLocale.toString() + ".properties");
        }
    }

    public Set<String> getBasenames() {
        return this.basenames;
    }

    @Required
    public void setLocationPatterns(final String[] locationPatterns) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(getBundleClassLoader());
        try {
            this.basenames = new HashSet<String>();
            for (String locationPattern : locationPatterns) {
                String prefix;
                int lastIndexOf = locationPattern.lastIndexOf('/');
                if (lastIndexOf == -1) {
                    prefix = "";
                } else {
                    prefix = locationPattern.substring(locationPattern.indexOf(':') + 1, lastIndexOf + 1);
                }
                Resource[] messageResources = resolver.getResources(locationPattern);
                for (Resource resource : messageResources) {
                    String resourceName = prefix + resource.getFilename();
                    for (String localeSuffix : localeSuffixes) {
                        if (resourceName.endsWith(localeSuffix)) {
                            this.basenames.add(resourceName.substring(0, resourceName.lastIndexOf(localeSuffix)));
                        }
                    }
                }
            }
            setBasenames(this.basenames.toArray(new String[this.basenames.size()]));
        } catch (IOException e) {
            this.logger.error("Cann't get resources[locationPattern = " + locationPatterns + "].", e);
            throw new IllegalArgumentException("Cann't get resources[locationPattern = " + locationPatterns + "].", e);
        }
    }

    public List<ResourceBundle> getResourceBundles(final Locale locale) {
        List<ResourceBundle> rbs = new LinkedList<ResourceBundle>();
        for (String basename : this.basenames) {
            ResourceBundle bundle = getResourceBundle(basename, locale);
            if (bundle != null) {
                rbs.add(bundle);
            }
        }
        return rbs;
    }
}
