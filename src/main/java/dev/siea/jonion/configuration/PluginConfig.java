package dev.siea.jonion.configuration;

/**
 * Abstract base for plugin configuration access: key presence check, typed getters with
 * optional default values, setters, and save.
 * <p>
 * Implementations (e.g. {@link YamlPluginConfig}, {@link XmlPluginConfig}) back the config
 * with a specific format. Keys are typically dot-separated paths. The default-value
 * overloads (e.g. {@link #getString(String, String)}) use {@link #containsKey(String)} so
 * the default is only returned when the key is absent. Used by
 * {@link dev.siea.jonion.Plugin#getDefaultConfig()} and {@link dev.siea.jonion.Plugin#getConfig(String)}.
 * </p>
 *
 * @see YamlPluginConfig
 * @see XmlPluginConfig
 * @see dev.siea.jonion.configuration.finder.PluginConfigurationFinder
 */
public abstract class PluginConfig {
    /**
     * Returns whether a key is present in the configuration.
     * Default-value overloads use this to apply the default only when the key is absent.
     *
     * @param key the config key (dot-separated path)
     * @return true if the key exists
     */
    public abstract boolean containsKey(String key);

    /** Returns the string value for the key, or null if absent. */
    public abstract String getString(String key);

    /** Returns the string value for the key, or the default if absent or null. */
    public String getString(String key, String defaultValue) {
        return containsKey(key) ? getString(key) : defaultValue;
    }

    /** Returns the int value for the key (0 if absent or unparseable). */
    public abstract int getInt(String key);

    /** Returns the int value for the key, or the default if absent or zero. */
    public int getInt(String key, int defaultValue) {
        return containsKey(key) ? getInt(key) : defaultValue;
    }

    /** Returns the boolean value for the key. */
    public abstract boolean getBoolean(String key);

    /** Returns the boolean value for the key, or the default if absent or false. */
    public boolean getBoolean(String key, boolean defaultValue) {
        return containsKey(key) ? getBoolean(key) : defaultValue;
    }

    /** Returns the double value for the key (0.0 if absent or unparseable). */
    public abstract double getDouble(String key);

    /** Returns the double value for the key, or the default if absent or zero. */
    public double getDouble(String key, double defaultValue) {
        return containsKey(key) ? getDouble(key) : defaultValue;
    }

    /** Returns the long value for the key (0L if absent or unparseable). */
    public abstract long getLong(String key);

    /** Returns the long value for the key, or the default if absent or zero. */
    public long getLong(String key, long defaultValue) {
        return containsKey(key) ? getLong(key) : defaultValue;
    }

    /** Returns the float value for the key (0.0f if absent or unparseable). */
    public abstract float getFloat(String key);

    /** Returns the float value for the key, or the default if absent or zero. */
    public float getFloat(String key, float defaultValue) {
        return containsKey(key) ? getFloat(key) : defaultValue;
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
        return containsKey(key) ? getShort(key) : defaultValue;
    }

    /** Returns the char value for the key (null character if absent or empty). */
    public abstract char getChar(String key);

    /** Returns the char value for the key, or the default if absent or null character. */
    public char getChar(String key, char defaultValue) {
        return containsKey(key) ? getChar(key) : defaultValue;
    }

    /** Returns the raw value for the key. */
    public abstract Object get(String key);

    /** Returns the raw value for the key, or the default if absent or null. */
    public Object get(String key, Object defaultValue) {
        return containsKey(key) ? get(key) : defaultValue;
    }

    /** Sets the value for the key. */
    public abstract void set(String key, Object value);

    /** Persists the configuration to disk. */
    public abstract void save();
}