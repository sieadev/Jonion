package dev.siea.jonion.configuration;

public abstract class PluginConfig {
    /**
     * Returns whether a key is present in the configuration.
     * Default-value overloads use this to apply the default only when the key is absent.
     */
    public abstract boolean containsKey(String key);

    public abstract String getString(String key);

    public String getString(String key, String defaultValue) {
        return containsKey(key) ? getString(key) : defaultValue;
    }

    public abstract int getInt(String key);

    public int getInt(String key, int defaultValue) {
        return containsKey(key) ? getInt(key) : defaultValue;
    }

    public abstract boolean getBoolean(String key);

    public boolean getBoolean(String key, boolean defaultValue) {
        return containsKey(key) ? getBoolean(key) : defaultValue;
    }

    public abstract double getDouble(String key);

    public double getDouble(String key, double defaultValue) {
        return containsKey(key) ? getDouble(key) : defaultValue;
    }

    public abstract long getLong(String key);

    public long getLong(String key, long defaultValue) {
        return containsKey(key) ? getLong(key) : defaultValue;
    }

    public abstract float getFloat(String key);

    public float getFloat(String key, float defaultValue) {
        return containsKey(key) ? getFloat(key) : defaultValue;
    }

    public abstract byte getByte(String key);

    public byte getByte(String key, byte defaultValue) {
        return containsKey(key) ? getByte(key) : defaultValue;
    }

    public abstract short getShort(String key);

    public short getShort(String key, short defaultValue) {
        return containsKey(key) ? getShort(key) : defaultValue;
    }

    public abstract char getChar(String key);

    public char getChar(String key, char defaultValue) {
        return containsKey(key) ? getChar(key) : defaultValue;
    }

    public abstract Object get(String key);

    public Object get(String key, Object defaultValue) {
        return containsKey(key) ? get(key) : defaultValue;
    }

    public abstract void set(String key, Object value);

    public abstract void save();
}