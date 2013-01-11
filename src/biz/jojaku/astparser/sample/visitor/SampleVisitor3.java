package biz.jojaku.astparser.sample.visitor;

import java.util.ArrayList;
import java.util.List;

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
		List<String> interfaceNames = new ArrayList<String>();
		interfaceNames.add("java.io.Serializable");
		interfaceNames.add("PiyoPiyo");
		setSuperInterfaces(node, interfaceNames);

		return super.visit(node);
	}

	private void setClassName(TypeDeclaration node, String simpleClassName) {
		AST ast = node.getAST();
		SimpleName simpleName = ast.newSimpleName(simpleClassName);
		node.setName(simpleName);
	}

	private void setSuperClass(TypeDeclaration node, String superClassName) {
		AST ast = node.getAST();
		Name name = ast.newName(superClassName);
		Type superClassType = ast.newSimpleType(name);
		node.setSuperclassType(superClassType);
	}

	@SuppressWarnings("unchecked")
	private void setSuperInterfaces(TypeDeclaration node,
			List<String> newInterfaceNames) {
		AST ast = node.getAST();
		node.superInterfaceTypes().clear();// 古いインターフェースを全て削除
		for (String interfaceName : newInterfaceNames) {
			Name name = ast.newName(interfaceName);// 新しいノードを作って・・・
			Type interfaceType = ast.newSimpleType(name);
			node.superInterfaceTypes().add(interfaceType);// インターフェースのリストに追加する
		}
	}

	/**
	 * 無駄情報削減の為にメソッド宣言を削除
	 */
	public boolean visit(MethodDeclaration node) {
		node.delete();
		return super.visit(node);
	}
}
