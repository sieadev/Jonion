package dev.siea.jonion.loader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Class loader that loads classes from a plugin JAR and its manifest Class-Path entries.
 * <p>
 * Extends {@link URLClassLoader} with the JAR path as the primary URL. If the JAR has a
 * {@code Class-Path} attribute in its manifest, those entries are added as additional URLs
 * so the plugin can resolve dependencies packaged or referenced alongside it. Used by
 * {@link dev.siea.jonion.PluginWrapper} to create an isolated class-loading context per plugin.
 * </p>
 *
 * @see dev.siea.jonion.PluginWrapper
 * @see URLClassLoader
 */
public class CustomClassLoader extends URLClassLoader {

    /**
     * Creates a class loader that loads from the given JAR and its manifest Class-Path.
     *
     * @param jarPath path to the plugin JAR file
     * @param parent  the parent class loader for delegation
     * @throws IOException if the JAR cannot be opened or read
     */
    public CustomClassLoader(Path jarPath, ClassLoader parent) throws IOException {
        super(buildUrls(jarPath), parent);
    }

    private static URL[] buildUrls(Path jarPath) throws IOException {
        List<URL> urls = new ArrayList<>();
        urls.add(jarPath.toUri().toURL());

        Path baseDir = jarPath.getParent() != null ? jarPath.getParent() : Paths.get(".");

        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            Manifest manifest = jarFile.getManifest();
            if (manifest != null) {
                String classPath = manifest.getMainAttributes().getValue("Class-Path");
                if (classPath != null) {
                    for (String entry : classPath.split("\\s+")) {
                        if (entry.isEmpty()) {
                            continue;
                        }
                        try {
                            URL url = new URL(entry);
                            urls.add(url);
                        } catch (MalformedURLException e) {
                            Path resolved = baseDir.resolve(entry);
                            urls.add(resolved.toUri().toURL());
                        }
                    }
                }
            }
        }

        return urls.toArray(new URL[0]);
    }

    /**
     * Loads the class, delegating to the parent first and then finding in this loader's URLs.
     *
     * @param name the binary name of the class
     * @return the loaded class
     * @throws ClassNotFoundException if the class cannot be found
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            return super.loadClass(name);
        } catch (ClassNotFoundException e) {
            try {
                return findClass(name);
            } catch (ClassNotFoundException ex) {
                throw e;
            }
        }
    }
}