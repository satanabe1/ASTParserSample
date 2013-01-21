package info.haxahaxa.astparser.sample.visitor;

import info.haxahaxa.astparser.util.TypeBuilder;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * コード生成サンプル<br>
 * 新しくフィールドを追加したり，メソッドを追加したりする
 * 
 * @author satanabe1
 * 
 */
public class SampleVisitor5 extends ASTVisitor {

	public boolean visit(TypeDeclaration node) {
		// Hoge型フィールドfugaを private static で生成
		addFieldDec(node, Modifier.PRIVATE, "double", "fuga");

		// Map<String, Integer>型メソッドmogeを生成
		addMethodDec(node, Modifier.SYNCHRONIZED,
				"java.util.Map<List<String[]>, java.lang.Integer>[]", "moge");

		// mogeメソッドの引数違いを生成する
		addMethodDec(node, Modifier.SYNCHRONIZED,
				"java.util.Map<List<String[]>, java.lang.Integer>[]", "moge",
				"Argument", "arg1", "Argument[]", "arg2");

		return super.visit(node);
	}

	@SuppressWarnings("unchecked")
	private VariableDeclarationFragment addFieldDec(TypeDeclaration node,
			int modifiers, String typeName, String fieldName) {
		AST ast = node.getAST();
		// まずは変数宣言を生成する
		VariableDeclarationFragment vdFragment = ast
				.newVariableDeclarationFragment();
		vdFragment.setName(ast.newSimpleName(fieldName));
		// 次にそれをフィールド宣言に入れる
		FieldDeclaration fieldDec = ast.newFieldDeclaration(vdFragment);
		fieldDec.setType(new TypeBuilder(typeName).build(ast));
		// フィールドにmodifierを追加する
		fieldDec.modifiers().addAll(ast.newModifiers(modifiers));
		node.bodyDeclarations().add(fieldDec);
		return vdFragment;
	}

	@SuppressWarnings("unchecked")
	private MethodDeclaration addMethodDec(TypeDeclaration node, int modifiers,
			String returnTypeName, String methodName) {
		AST ast = node.getAST();

		// メソッド宣言を作る時は、文字通りnewMethodDeclaration
		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
		// 修飾子を付けたい時は、newModifiersしてaddAllすればいい
		methodDeclaration.modifiers().addAll(ast.newModifiers(modifiers));
		// メソッドの戻り値の型はsetReturnType2！Type型で指定
		methodDeclaration.setReturnType2(new TypeBuilder(returnTypeName)
				.build(ast));
		// 名前付け
		methodDeclaration.setName(ast.newSimpleName(methodName));// name
		// 最後にクラス宣言のbodyDeclarationsにaddして完成
		node.bodyDeclarations().add(methodDeclaration);
		return methodDeclaration;
	}

	@SuppressWarnings("unchecked")
	private MethodDeclaration addMethodDec(TypeDeclaration node, int modifiers,
			String returnTypeName, String methodName, String... argments) {
		AST ast = node.getAST();

		// メソッド宣言を作る時は、文字通りnewMethodDeclaration
		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
		// 修飾子を付けたい時は、newModifiersしてaddAllすればいい
		methodDeclaration.modifiers().addAll(ast.newModifiers(modifiers));
		// メソッドの戻り値の型はsetReturnType2！Type型で指定
		methodDeclaration.setReturnType2(new TypeBuilder(returnTypeName)
				.build(ast));
		// 名前付け
		methodDeclaration.setName(ast.newSimpleName(methodName));// name

		// そして引数を追加する
		if (argments.length % 2 == 1) {
			throw new IllegalArgumentException("引数の型と名前の個数が変");
		}
		for (int i = 0; i < argments.length; i += 2) {
			SingleVariableDeclaration argDec = ast
					.newSingleVariableDeclaration();
			argDec.setType(new TypeBuilder(argments[i]).build(ast));
			argDec.setName(ast.newSimpleName(argments[i + 1]));
			methodDeclaration.parameters().add(argDec);
		}
		// 最後にクラス宣言のbodyDeclarationsにaddして完成
		node.bodyDeclarations().add(methodDeclaration);
		return methodDeclaration;
	}
}
