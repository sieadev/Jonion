package dev.siea.jonion.configuration;

public abstract class PluginConfig {
    public abstract String getString(String key);

    public String getString(String key, String defaultValue) {
        String value = getString(key);
        return value != null ? value : defaultValue;
    }

    public abstract int getInt(String key);

    public int getInt(String key, int defaultValue) {
        int value = getInt(key);
        return value != 0 ? value : defaultValue;
    }

    public abstract boolean getBoolean(String key);

    public boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean(key) || defaultValue;
    }

    public abstract double getDouble(String key);

    public double getDouble(String key, double defaultValue) {
        double value = getDouble(key);
        return value != 0.0 ? value : defaultValue;
    }

    public abstract long getLong(String key);

    public long getLong(String key, long defaultValue) {
        long value = getLong(key);
        return value != 0L ? value : defaultValue;
    }

    public abstract float getFloat(String key);

    public float getFloat(String key, float defaultValue) {
        float value = getFloat(key);
        return value != 0.0f ? value : defaultValue;
    }

    public abstract byte getByte(String key);

    public byte getByte(String key, byte defaultValue) {
        byte value = getByte(key);
        return value != 0 ? value : defaultValue;
    }

    public abstract short getShort(String key);

    public short getShort(String key, short defaultValue) {
        short value = getShort(key);
        return value != 0 ? value : defaultValue;
    }

    public abstract char getChar(String key);

    public char getChar(String key, char defaultValue) {
        char value = getChar(key);
        return value != '\u0000' ? value : defaultValue;
    }

    public abstract Object get(String key);

    public Object get(String key, Object defaultValue) {
        Object value = get(key);
        return value != null ? value : defaultValue;
    }

    public abstract void set(String key, Object value);

    public abstract void save();
}