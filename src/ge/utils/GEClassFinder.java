package ge.utils;

import ge.geometry.GEGeometry;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GEClassFinder {

    private File[] classFiles = new File[]{};

    public GEClassFinder(String packageName){
        try {
            classFiles = getPackageContent(packageName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File[] getPackageContent(String packageName) throws IOException {
        ArrayList<File> list = new ArrayList<File>(0);
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            File dir = new File(url.getFile());
            Collections.addAll(list, Objects.requireNonNull(dir.listFiles()));
        }
        return list.toArray(new File[]{});
    }

    public Class<?>[] getClassesArray(){
        ArrayList<Class<?>> list = new ArrayList<Class<?>>(0);
        for(File f : classFiles){
            String name = f.getName();
            if (name.contains(".")) {
                name = name.substring(0, name.lastIndexOf('.'));
            }
            try {
                Class<?> cl = Class.forName("ge.geometry." + name);
                list.add(cl);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list.toArray(new Class<?>[]{});
    }
}
