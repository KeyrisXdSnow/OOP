package classLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarFile;

public class MyClassLoader {

    private final String filesDirectory;
    private ArrayList<Class> loadedClasses ;
    private URLClassLoader classLoader;

    public MyClassLoader(String filesDirectory) {
        this.filesDirectory = filesDirectory;
    }

    public void loadJar() throws Exception {
        File directory = new File(filesDirectory);

        if (!directory.exists()) throw new Exception("Directory does not exist");
        else {

            File[] jars = directory.listFiles(((dir, name) -> name.endsWith(".jar")));
            if (jars != null && jars.length != 0) {

                ArrayList<String> classes = new ArrayList<>();
                ArrayList<URL> urls = new ArrayList<>();

                for (File file : jars) {
                    JarFile jarFile = new JarFile(file);
                    jarFile.stream().forEach(jarEntry -> {
                        if (jarEntry.getName().endsWith(".class")) classes.add(jarEntry.getName());
                    });
                    urls.add(file.toURI().toURL());
                }
                load(urls.toArray(new URL[urls.size()]), classes);
            }
        }
    }

    private void load(URL[] urls, ArrayList<String> classes) {
        classLoader = new URLClassLoader(urls, this.getClass().getClassLoader());
        loadedClasses = new ArrayList<>();

        classes.forEach(className -> {
            try {
                loadedClasses.add(classLoader.loadClass(className.replaceAll("/", ".").replace(".class", "")));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void closeClassLoader () throws IOException {
        classLoader.clearAssertionStatus();
        classLoader.close();
    }

    public ArrayList<Class> getLoadedClasses() {
        return loadedClasses;
    }
}