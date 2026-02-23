package dev.siea.jonion.configuration;

/**
 * Abstract base for plugin configuration access: typed getters, optional default values,
 * setters, and save.
 * <p>
 * Implementations (e.g. {@link YamlPluginConfig}, {@link XmlPluginConfig}) back the config
 * with a specific format. Keys are typically dot-separated paths. Used by
 * {@link dev.siea.jonion.Plugin#getDefaultConfig()} and {@link dev.siea.jonion.Plugin#getConfig(String)}.
 * </p>
 *
 * @see YamlPluginConfig
 * @see XmlPluginConfig
 * @see dev.siea.jonion.configuration.finder.PluginConfigurationFinder
 */
public abstract class PluginConfig {
    /** Returns the string value for the key, or null if absent. */
    public abstract String getString(String key);

    /** Returns the string value for the key, or the default if absent or null. */
    public String getString(String key, String defaultValue) {
        String value = getString(key);
        return value != null ? value : defaultValue;
    }

    /** Returns the int value for the key (0 if absent or unparseable). */
    public abstract int getInt(String key);

    /** Returns the int value for the key, or the default if absent or zero. */
    public int getInt(String key, int defaultValue) {
        int value = getInt(key);
        return value != 0 ? value : defaultValue;
    }

    /** Returns the boolean value for the key. */
    public abstract boolean getBoolean(String key);

    /** Returns the boolean value for the key, or the default if absent or false. */
    public boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean(key) || defaultValue;
    }

    /** Returns the double value for the key (0.0 if absent or unparseable). */
    public abstract double getDouble(String key);

    /** Returns the double value for the key, or the default if absent or zero. */
    public double getDouble(String key, double defaultValue) {
        double value = getDouble(key);
        return value != 0.0 ? value : defaultValue;
    }

    /** Returns the long value for the key (0L if absent or unparseable). */
    public abstract long getLong(String key);

    /** Returns the long value for the key, or the default if absent or zero. */
    public long getLong(String key, long defaultValue) {
        long value = getLong(key);
        return value != 0L ? value : defaultValue;
    }

    /** Returns the float value for the key (0.0f if absent or unparseable). */
    public abstract float getFloat(String key);

    /** Returns the float value for the key, or the default if absent or zero. */
    public float getFloat(String key, float defaultValue) {
        float value = getFloat(key);
        return value != 0.0f ? value : defaultValue;
    }

    /** Returns the byte value for the key (0 if absent or unparseable). */
    public abstract byte getByte(String key);

    /** Returns the byte value for the key, or the default if absent or zero. */
    public byte getByte(String key, byte defaultValue) {
        byte value = getByte(key);
        return value != 0 ? value : defaultValue;
    }

    /** Returns the short value for the key (0 if absent or unparseable). */
    public abstract short getShort(String key);

    /** Returns the short value for the key, or the default if absent or zero. */
    public short getShort(String key, short defaultValue) {
        short value = getShort(key);
        return value != 0 ? value : defaultValue;
    }

    /** Returns the char value for the key (null character if absent or empty). */
    public abstract char getChar(String key);

    /** Returns the char value for the key, or the default if absent or null character. */
    public char getChar(String key, char defaultValue) {
        char value = getChar(key);
        return value != '\u0000' ? value : defaultValue;
    }

    /** Returns the raw value for the key. */
    public abstract Object get(String key);

    /** Returns the raw value for the key, or the default if absent or null. */
    public Object get(String key, Object defaultValue) {
        Object value = get(key);
        return value != null ? value : defaultValue;
    }

    /** Sets the value for the key. */
    public abstract void set(String key, Object value);

    /** Persists the configuration to disk. */
    public abstract void save();
}