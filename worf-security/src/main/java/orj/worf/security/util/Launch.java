package orj.worf.security.util;

import javax.annotation.PostConstruct;

class Launch {
    @PostConstruct
    public void launch() {
        SecurityUtils.launch();
        RSAUtils.launch();
    }
}
