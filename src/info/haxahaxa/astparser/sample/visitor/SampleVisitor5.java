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

	/**
	 * フィールドの追加や、メソッドの追加はクラスに対して行われるので、TypeDeclarationに引っ掛ける
	 */
	public boolean visit(TypeDeclaration node) {
		// Hoge型フィールドfugaを private static で生成
		addFieldDec(node, Modifier.PRIVATE | Modifier.STATIC, "double", "fuga");

		// Map<String, Integer>型メソッドmogeを生成
		MethodDeclaration methodDeclaration = addMethodDec(node,
				Modifier.SYNCHRONIZED,
				"java.util.Map<Integer,? extends String>", "moge");

		// 生成されたmogeメソッドに引数を付ける
		addMethodParams(methodDeclaration, "int", "arg1", "String", "arg2");

		// コンストラクタを生成
		addConstructor(node, node.getModifiers());

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

	/**
	 * MethodDeclarationのインスタンスに引数を追加する
	 * 
	 * @param params
	 *            偶数番:型名<br>
	 *            奇数番:変数名<br>
	 */
	@SuppressWarnings("unchecked")
	private void addMethodParams(MethodDeclaration methodDeclaration,
			String... params) {
		AST ast = methodDeclaration.getAST();
		for (int i = 0; i < params.length; i += 2) {
			SingleVariableDeclaration singleVariableDeclaration = ast
					.newSingleVariableDeclaration();
			// 型を指定
			singleVariableDeclaration.setType(new TypeBuilder(params[i])
					.build(ast));
			// 変数名を指定
			singleVariableDeclaration.setName(ast.newSimpleName(params[i + 1]));
			methodDeclaration.parameters().add(singleVariableDeclaration);
		}
	}

	@SuppressWarnings("unchecked")
	private MethodDeclaration addConstructor(TypeDeclaration node, int modifiers) {
		AST ast = node.getAST();
		// メソッド宣言の生成
		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
		// 修飾子
		methodDeclaration.modifiers().addAll(ast.newModifiers(modifiers));
		// 名前
		methodDeclaration.setName(ast.newSimpleName(node.getName().toString()));
		// コンストラクタにする
		methodDeclaration.setConstructor(true);
		node.bodyDeclarations().add(methodDeclaration);
		return methodDeclaration;
	}

}
