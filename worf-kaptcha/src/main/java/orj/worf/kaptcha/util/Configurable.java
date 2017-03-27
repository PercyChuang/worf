package orj.worf.kaptcha.util;

public abstract class Configurable {
    private Config config;

    public Configurable() {
        this.config = null;
    }

    public Config getConfig() {
        return this.config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}