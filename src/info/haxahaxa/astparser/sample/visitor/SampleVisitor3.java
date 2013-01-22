package info.haxahaxa.astparser.sample.visitor;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * クラス名と，親クラスと，インターフェースを変更するサンプル
 * 
 * @author satanabe1
 * 
 */
public class SampleVisitor3 extends ASTVisitor {

	public boolean visit(TypeDeclaration node) {
		// クラス名を変更する
		setClassName(node, "HogeHoge");

		// 親クラスを変更する
		setSuperClass(node, "java.applet.Applet");

		// インターフェースを変更する
		setSuperInterfaces(node, "java.io.Serializable", "PiyoPiyo");

		return super.visit(node);
	}

	/**
	 * クラス名を変更する
	 * 
	 * @param node
	 *            変更したいクラス宣言ノード
	 * @param simpleClassName
	 *            変更したい名前
	 */
	private void setClassName(TypeDeclaration node, String simpleClassName) {
		AST ast = node.getAST();
		SimpleName simpleName = ast.newSimpleName(simpleClassName);
		node.setName(simpleName);
	}

	/**
	 * 親クラスを変更する
	 * 
	 * @param node
	 *            変更したいクラス宣言ノード
	 * @param superClassName
	 *            継承したいクラス名
	 */
	private void setSuperClass(TypeDeclaration node, String superClassName) {
		AST ast = node.getAST();
		Name name = ast.newName(superClassName);
		Type superClassType = ast.newSimpleType(name);
		node.setSuperclassType(superClassType);
	}

	/**
	 * インターフェースを変更する
	 * 
	 * @param node
	 *            変更したいクラスノード
	 * @param newInterfaceNames
	 *            実装したいインターフェースの一覧
	 */
	@SuppressWarnings("unchecked")
	private void setSuperInterfaces(TypeDeclaration node,
			String... newInterfaceNames) {
		AST ast = node.getAST();
		node.superInterfaceTypes().clear();// 古いインターフェースを全て削除
		for (String interfaceName : newInterfaceNames) {
			Name name = ast.newName(interfaceName);// 新しい名前を作って・・・
			Type interfaceType = ast.newSimpleType(name);
			node.superInterfaceTypes().add(interfaceType);// インターフェースのリストに追加する
		}
	}

	/**
	 * 出力される無駄な情報を削減する為にメソッド宣言を削除
	 */
	public boolean visit(MethodDeclaration node) {
		node.delete();
		return super.visit(node);
	}
}
