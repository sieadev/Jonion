package dev.siea.jonion.configuration;

import dev.siea.jonion.exceptions.ConfigSaveException;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class YamlPluginConfig extends PluginConfig {
    private final YamlConfiguration yamlConfig;
    private final File file;

    public YamlPluginConfig(YamlConfiguration yamlConfig, Path filePath) {
        this.yamlConfig = yamlConfig;
        this.file = new File(String.valueOf(filePath));
    }

    public YamlConfiguration getYamlConfiguration() {
        return yamlConfig;
    }

    @Override
    public String getString(String path) {
        return yamlConfig.getString(path);
    }

    @Override
    public int getInt(String path) {
        return yamlConfig.getInt(path);
    }

    @Override
    public boolean getBoolean(String path) {
        return yamlConfig.getBoolean(path);
    }

    @Override
    public double getDouble(String path) {
        return yamlConfig.getDouble(path);
    }

    @Override
    public long getLong(String path) {
        return yamlConfig.getLong(path);
    }

    @Override
    public float getFloat(String path) {
        return (float) yamlConfig.getDouble(path);
    }

    @Override
    public byte getByte(String path) {
        return (byte) yamlConfig.getInt(path);
    }

    @Override
    public short getShort(String key) {
        return (short) yamlConfig.getInt(key);
    }

    @Override
    public char getChar(String key) {
        return (char) yamlConfig.getInt(key);
    }

    @Override
    public Object get(String path) {
        return yamlConfig.get(path);
    }

    @Override
    public void set(String path, Object value) {
        yamlConfig.set(path, value);
    }

    @Override
    public void save() {
        try {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                throw new ConfigSaveException("Failed to create directories for the configuration file.");
            }
            if (!file.exists() && !file.createNewFile()) {
                throw new ConfigSaveException("Failed to create the configuration file.");
            }
            yamlConfig.save(file);
        } catch (IOException e) {
            throw new ConfigSaveException(e);
        }
    }
}
