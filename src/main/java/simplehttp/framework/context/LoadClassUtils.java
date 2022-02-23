package simplehttp.framework.context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadClassUtils {

	private LoadClassUtils() {}
	
	public static List<Class<?>>loadClassFromPackge(String packageName) throws IOException{
		String packageLocation = packageName.replace(".", "/");
		InputStream inputPackageStream = ClassLoader.getSystemResourceAsStream(packageLocation);
		if(inputPackageStream == null) {
			return Collections.emptyList();
		}
		
		InputStreamReader streamReader = new InputStreamReader(inputPackageStream);
		BufferedReader reader = new BufferedReader(streamReader);
		
		List<Class<?>> classes = new ArrayList<>();
		reader.lines().forEach((line) -> {
			if(line.endsWith(".class")) {
				try {
					String className = line.replaceFirst(".class", "").trim();
					String completeClassName = packageName.concat(".").concat(className);
					Class<?> forClass = Class.forName(completeClassName);
					classes.add(forClass);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}else {
				try {
					classes.addAll( loadClassFromPackge(packageName.concat(".").concat(line)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		return classes;
	}
}
