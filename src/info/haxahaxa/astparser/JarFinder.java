package info.haxahaxa.astparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ASTParserを使うために必要なjarファイルのパスを調べるクラス<br>
 * 
 * @author satanabe1
 * 
 */
public class JarFinder {

	public static void main(String[] args) {
		try {
			if (args.length != 1) {
				help();
			}
			Properties properties = getProperties();
			List<FilenameFilter> resourceFilter = getFilenameFilters(properties);
			List<File> jarFiles = new ArrayList<File>();
			for (FilenameFilter filter : resourceFilter) {
				jarFiles.addAll(getFiles(new File(args[0]), filter));
			}
			// for (File jar : jarFiles) {
			// System.out.println("JAR_FILE : " + jar.getCanonicalPath());
			// }
			System.out.println("CLASSPATH : "
					+ generateClasspathString(jarFiles));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ヘルプ表示
	 */
	private static void help() {
		System.out.println(JarFinder.class.getName());
		System.out.println("Usage: java -jar {JAR} {ECLIPSE DIRECTORY}");
	}

	/**
	 * プロパティファイルをロードする
	 * 
	 * @return プロパティファイルをロードしたPropertiesクラスのインスタンス
	 * @throws IOException
	 */
	private static Properties getProperties() throws IOException {
		Properties properties = new Properties();
		String propFileName = "props" + File.separator
				+ JarFinder.class.getSimpleName() + ".properties";
		InputStream propStream = null;
		propStream = JarFinder.class.getClassLoader().getResourceAsStream(
				propFileName);
		if (propStream == null) {
			propStream = new FileInputStream(propFileName);
		}
		properties.load(propStream);
		return properties;
	}

	/**
	 * プロパティから，FilenameFilterのリストを生成する
	 * 
	 * @param properties
	 * @return
	 * @throws IOException
	 */
	private static List<FilenameFilter> getFilenameFilters(Properties properties)
			throws IOException {
		Enumeration<Object> keys = properties.keys();
		List<FilenameFilter> filters = new ArrayList<FilenameFilter>();
		while (keys.hasMoreElements()) {
			final String value = properties.getProperty(keys.nextElement()
					.toString());
			filters.add(new FilenameFilter() {
				Pattern hitPattern = Pattern.compile(value);

				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					Matcher matcher = hitPattern.matcher(name);
					return matcher.find();
				}
			});
		}
		return filters;
	}

	/**
	 * 基点となるディレクトリ以下のfilterに該当するファイルの一覧を取得する<br>
	 * 問答無用で再帰的に検索する
	 * 
	 * @param root
	 * @param filter
	 * @return ファイル一覧
	 */
	private static List<File> getFiles(File root, FilenameFilter filter) {
		List<File> files = new ArrayList<File>();
		getFiles(root, files, filter);
		return files;
	}

	/**
	 * 基点となるディレクトリ以下のfilterに該当するファイルの一覧を取得する
	 * 
	 * @param root
	 * @param list
	 * @param filter
	 */
	private static void getFiles(File root, List<File> list,
			FilenameFilter filter) {
		for (File child : root.listFiles(filter)) {
			list.add(child);
		}
		for (File child : root.listFiles()) {
			if (child.isDirectory()) {
				getFiles(child, list, filter);
			}
		}
	}

	/**
	 * ファイルのリストを{@link File#pathSeparator}で繋いだ文字列を生成する
	 * 
	 * @param jarList
	 * @return クラスパス形式の文字列
	 */
	private static String generateClasspathString(List<File> jarList) {
		StringBuilder sb = new StringBuilder();
		for (File path : jarList) {
			try {
				sb.append(path.getCanonicalPath());
			} catch (Exception ex) {
				sb.append(path.getAbsolutePath());
			}
			sb.append(File.pathSeparator);
		}
		return sb.toString();
	}
}
