package orj.worf.web.base.viewresolver;

import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.ViewResolver;

public class ViewResolverConfigurer implements Ordered {

    private int order;

    private List<ViewResolver> viewResolvers;

    public List<ViewResolver> getViewResolvers() {
        return viewResolvers;
    }

    public void setViewResolvers(final List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    public void setOrder(final int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

}
