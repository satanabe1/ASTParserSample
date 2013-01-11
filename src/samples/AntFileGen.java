package samples;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * XMLの文字列を生成するクラス
 * 
 */
public final class AntFileGen implements java.lang.Cloneable {
	private String projectName = "ProjectName";
	private String baseDir = ".", srcDir = "src", classDir = "classes";
	private String srcEncoding = "utf8";
	private String srcVersion = "1.6";
	private String debugFlg = "on";

	/**
	 * 
	 * @param arg
	 */
	public static void main(String arg[]) {
		try {
			AntFileGen afg = new AntFileGen();
			afg.setClassDir("dist");
			System.out.print(afg.getXmlString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public AntFileGen() {
	}

	/**
	 * 
	 */
	public String getXmlString() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.newDocument();

			/* <project name="check" default="main" basedir="." > */
			Element projectTag = document.createElement("project");// <-rootドキュメント
			projectTag.setAttribute("name", projectName);
			projectTag.setAttribute("default", "main");
			projectTag.setAttribute("basedir", baseDir);
			document.appendChild(projectTag);

			/* 各種プロパティタグの生成 */
			/* <property name="src.dir" location="src"/> */
			{
				Element srcDirPropTag = document.createElement("property");
				srcDirPropTag.setAttribute("name", "src.dir");
				srcDirPropTag.setAttribute("location", srcDir);
				projectTag.appendChild(srcDirPropTag);
			}
			/* <property name="class.dir" location="classes"/> */
			{
				Element classDirPropTag = document.createElement("property");
				classDirPropTag.setAttribute("name", "class.dir");
				classDirPropTag.setAttribute("location", classDir);
				projectTag.appendChild(classDirPropTag);
			}
			/* <property name="src.encoding" value="utf8"/> */
			{
				Element srcEncPropTag = document.createElement("property");
				srcEncPropTag.setAttribute("name", "src.encoding");
				srcEncPropTag.setAttribute("value", srcEncoding);
				projectTag.appendChild(srcEncPropTag);
			}
			/* <property name="src.version" value="1.6"/> */
			{
				Element srcVersionProp = document.createElement("property");
				srcVersionProp.setAttribute("name", "src.version");
				srcVersionProp.setAttribute("value", srcVersion);
				projectTag.appendChild(srcVersionProp);
			}

			/*
			 * <target name="main" depends="init"> <javac srcdir="${src.dir}"
			 * destdir="${class.dir}" source="${src.version}"
			 * target="${src.version}" encoding="${src.encoding}" debug="on"
			 * includeAntRuntime="on"> </javac> </target>
			 */
			{
				Element mainTargetTag = document.createElement("target");
				mainTargetTag.setAttribute("name", "main");
				mainTargetTag.setAttribute("depends", "init");
				projectTag.appendChild(mainTargetTag);
				Element javacTag = document.createElement("javac");
				javacTag.setAttribute("srcdir", "${src.dir}");
				javacTag.setAttribute("destdir", "${class.dir}");
				javacTag.setAttribute("source", "${src.version}");
				javacTag.setAttribute("target", "${src.version}");
				javacTag.setAttribute("encoding", "${src.encoding}");
				javacTag.setAttribute("debug", debugFlg);
				javacTag.setAttribute("includeAntRuntime", "on");
				Text javacText = document.createTextNode(" ");
				javacTag.appendChild(javacText);
				mainTargetTag.appendChild(javacTag);
			}
			/*
			 * <target name="init"> <mkdir dir="${class.dir}" /> <delete>
			 * <fileset dir="${class.dir}" includes="** /*.class" /> </delete>
			 * </target>
			 */
			{
				Element initTargetTag = document.createElement("target");
				initTargetTag.setAttribute("name", "init");
				Element mkdirTag = document.createElement("mkdir");
				mkdirTag.setAttribute("dir", "${class.dir}");
				initTargetTag.appendChild(mkdirTag);
				Element deleteTag = document.createElement("delete");
				initTargetTag.appendChild(deleteTag);
				Element filesetTag = document.createElement("fileset");
				filesetTag.setAttribute("dir", "${class.dir}");
				filesetTag.setAttribute("includes", "**/*.class");
				deleteTag.appendChild(filesetTag);

				projectTag.appendChild(initTargetTag);
			}

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			// transformer.setOutputProperty(
			// OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "2");

			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);

			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			return writer.toString();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public void writeDown(String distPath, String xmlName) {
		try {
			File distDir = new File(distPath);
			if (!distDir.exists()) {
				distDir.mkdirs();
			}
			File xmlFile = new File(distPath + File.separatorChar + xmlName);
			FileWriter fw = new FileWriter(xmlFile);
			fw.write(getXmlString());
			fw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @return the baseDir
	 */
	public String getBaseDir() {
		return baseDir;
	}

	/**
	 * @return the srcDir
	 */
	public String getSrcDir() {
		return srcDir;
	}

	/**
	 * @return the classDir
	 */
	public String getClassDir() {
		return classDir;
	}

	/**
	 * @return the srcEncoding
	 */
	public String getSrcEncoding() {
		return srcEncoding;
	}

	/**
	 * @return the srcVersion
	 */
	public String getSrcVersion() {
		return srcVersion;
	}

	/**
	 * @return the debugFlg
	 */
	public String getDebugFlg() {
		return debugFlg;
	}

	/**
	 * @param projectName
	 *            the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @param baseDir
	 *            the baseDir to set
	 */
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	/**
	 * @param srcDir
	 *            the srcDir to set
	 */
	public void setSrcDir(String srcDir) {
		this.srcDir = srcDir;
	}

	/**
	 * @param classDir
	 *            the classDir to set
	 */
	public void setClassDir(String classDir) {
		this.classDir = classDir;
	}

	/**
	 * @param srcEncoding
	 *            the srcEncoding to set
	 */
	public void setSrcEncoding(String srcEncoding) {
		this.srcEncoding = srcEncoding;
	}

	/**
	 * @param srcVersion
	 *            the srcVersion to set
	 */
	public void setSrcVersion(String srcVersion) {
		this.srcVersion = srcVersion;
	}

	/**
	 * @param flg
	 *            the debugFlg to set
	 */
	public void setDebugFlg(boolean flg) {
		if (flg) {
			this.debugFlg = "on";
		} else {
			this.debugFlg = "off";
		}
	}
}