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

public class CustomClassLoader extends URLClassLoader {

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