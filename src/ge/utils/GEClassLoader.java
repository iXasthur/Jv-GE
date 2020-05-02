package ge.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarFile;

public class GEClassLoader {

    private static File[] getPackageContent(String packageName) throws IOException {
        ArrayList<File> list = new ArrayList<File>(0);
        String fixedPackageName = packageName.replace('.', '/');
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(fixedPackageName);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            File dir = new File(URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8));
            Collections.addAll(list, Objects.requireNonNull(dir.listFiles()));
        }
        return list.toArray(new File[]{});
    }

    public static Class<?>[] getClassesFromPackage(String packageName) throws IOException {
        ArrayList<Class<?>> list = new ArrayList<Class<?>>(0);
        for(File f : getPackageContent(packageName)){
            String name = f.getName();
            if (name.contains(".")) {
                name = name.substring(0, name.lastIndexOf('.'));
            }
            try {
                Class<?> cl = Class.forName(packageName + "." + name);
                list.add(cl);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list.toArray(new Class<?>[]{});
    }

    public static Class<?>[] getClassesFromJARsDirectory(String directoryPath) throws Exception {
        File directory = new File(directoryPath);

        if (directory.exists()) {
            File[] jars = directory.listFiles(((dir, name) -> name.endsWith(".jar")));
            if (jars != null && jars.length != 0) {

                Vector<String> classes = new Vector<>();
                Vector<URL> urls = new Vector<>();

                for (File file : jars) {
                    JarFile jarFile = new JarFile(file);
                    jarFile.stream().forEach(jarEntry -> {
                        if (jarEntry.getName().endsWith(".class")) classes.add(jarEntry.getName());
                    });
                    urls.add(file.toURI().toURL());
                }

                URL[] urlsArray = new URL[urls.size()];
                for (int i = 0; i < urls.size(); i++) {
                    urlsArray[i] = urls.elementAt(i);
                }

                URLClassLoader classLoader = new URLClassLoader(urlsArray);
                Vector<Class<?>> loadedClasses = new Vector<>();

                classes.forEach(className -> {
                    try {
                        loadedClasses.add(classLoader.loadClass(className.replaceAll("/", ".").replace(".class", "")));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });

                classLoader.clearAssertionStatus();
                classLoader.close();

                Class<?>[] arr = new Class<?>[loadedClasses.size()];
                for (int i = 0; i < loadedClasses.size(); i++) {
                    arr[i] = loadedClasses.elementAt(i);
                }
                return arr;
            } else {
                throw new Exception("Directory does not exist");
            }

        }

        return null;
    }

    public static Constructor<?> getGeometryClassConstructor(Class<?> cl) {
        final int neededParameterCount = 1;
        final Class<?> neededParameterTypeGeometryClass = GERegularBoundingBox.class;

        Constructor<?>[] constructors = cl.getConstructors();
        for (Constructor<?> constructor : constructors) {
            boolean parameterCountCheck = (constructor.getParameterCount() == neededParameterCount);
            boolean parameterTypeCheck = (constructor.getParameterTypes()[0] == neededParameterTypeGeometryClass);
            if (parameterCountCheck && parameterTypeCheck) {
                return constructor;
            }
        }
        return null;
    }

    public static Class<?>[] filterToGeometryClasses(Class<?>[] _classes) {
        Vector<Class<?>> classes = new Vector<>(Arrays.asList(_classes));
        for (Class<?> cl : (Vector<Class<?>>) classes.clone()) {
            if (getGeometryClassConstructor(cl) == null)  {
                classes.remove(cl);
            }
        }

        Class<?>[] filteredArray = new Class<?>[classes.size()];
        for (int i = 0; i < classes.size(); i++) {
            filteredArray[i] = classes.elementAt(i);
        }
        return filteredArray;
    }
}
