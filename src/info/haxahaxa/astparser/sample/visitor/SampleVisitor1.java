package info.haxahaxa.astparser.sample.visitor;

import info.haxahaxa.astparser.util.Print;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;


/**
 * クラス名やフィールド，メソッドの概要を表示するサンプル
 * 
 * @author satanabe1
 * 
 */
public class SampleVisitor1 extends ASTVisitor {
	/**
	 * クラス宣言が見つかると呼ばれるメソッド
	 */
	public boolean visit(TypeDeclaration node) {
		Print.printTitle("クラス宣言");

		ITypeBinding typeBinding = node.resolveBinding();// 詳細な情報をITypeBindingインスタンスを使って取得したい
		ITypeBinding superClass = typeBinding.getSuperclass();// 親クラスの取得
		ITypeBinding[] interfaces = typeBinding.getInterfaces();// インターフェースの取得
		String className = typeBinding.getBinaryName();// クラス名の取得
		int modifiers = typeBinding.getModifiers();// "public static"とかの識別子

		Print.printMessage("ClassName", className);
		Print.printModifiers("Modifiers", modifiers);
		Print.printMessage("SuperClass", superClass.getBinaryName());
		Print.printMessage("Interfaces", interfaces);
		return super.visit(node);
	}

	/**
	 * フィールド宣言が見つかると呼ばれるメソッド
	 */
	public boolean visit(FieldDeclaration node) {
		Print.printTitle("フィールド宣言");
		Print.printModifiers("Modifiers", node.getModifiers());
		Print.printMessage("Type", node.getType().toString());

		List<?> fragments = node.fragments();
		for (Object frg : fragments) {
			if (frg instanceof VariableDeclarationFragment) {
				IVariableBinding variableBinding = ((VariableDeclarationFragment) frg)
						.resolveBinding();
				Print.printMessage("Name", variableBinding.getName());
			}
		}
		return super.visit(node);
	}

	/**
	 * メソッド宣言が見つかると呼ばれるメソッド
	 */
	public boolean visit(MethodDeclaration node) {
		Print.printTitle("メソッド宣言");
		Print.printMessage("MethodName", node.getName()
				.getFullyQualifiedName());
		Print.printModifiers("Modifiers", node.getModifiers());
		Print.printMessage("ReturnType", node.getReturnType2() + "");
		Print.printMessage("Parameters", node.parameters().toString());
		return super.visit(node);
	}
}
