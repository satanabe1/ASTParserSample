package info.haxahaxa.astparser.sample.visitor;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

/**
 * コード生成サンプル<br>
 * メソッド内に変数宣言を追加する<br>
 * 配列やジェネリクスな変数を生成したり
 * 
 * @author satanabe1
 * 
 */
public class SampleVisitor4 extends ASTVisitor {

	@SuppressWarnings("unused")
	public boolean visit(MethodDeclaration node) {
		AST ast = node.getAST();

		// boolean型の変数fugaの宣言を追加する
		VariableDeclarationFragment hogefuga = addVariableDec(node,
				PrimitiveType.BOOLEAN, "fuga");

		// Mogeクラスの変数piyoの宣言を追加する
		VariableDeclarationFragment mogepiyo = addVariableDec(node, "Moge",
				"piyo");
		// 変数piyoを変数fooで初期化する
		mogepiyo.setInitializer(ast.newSimpleName("foo"));

		// java.util.Map<String,Integer> map宣言を追加する
		VariableDeclarationFragment listDecFragment = addVariableDec(node,
				"java.util.Map", "map", new String[] { "String", "Integer" });
		// 変数mapをnullで初期化する
		listDecFragment.setInitializer(ast.newNullLiteral());

		// int[][][] array宣言を追加する
		VariableDeclarationFragment arrayDecFragment = addVariableDec(node,
				"String", "array", 3);

		return super.visit(node);
	}

	/**
	 * メソッドに対して普通の変数宣言を追加する<br>
	 * ただしプリミティブ型専用<br>
	 * ex: int num;
	 * 
	 * @param node
	 *            弄りたいメソッド
	 * @param primitiveType
	 *            追加したい変数の型
	 * @param fieldName
	 *            追加したい変数の変数名
	 * @return 追加した変数宣言<br>
	 *         実際は"int num;"全体ではなく"num"の部分
	 */
	@SuppressWarnings("unchecked")
	private VariableDeclarationFragment addVariableDec(MethodDeclaration node,
			Code primitiveType, String fieldName) {
		AST ast = node.getAST();
		//
		VariableDeclarationFragment vdFragment = ast
				.newVariableDeclarationFragment();
		vdFragment.setName(ast.newSimpleName(fieldName));
		//
		VariableDeclarationStatement vdStatement = ast
				.newVariableDeclarationStatement(vdFragment);
		vdStatement.setType(ast.newPrimitiveType(primitiveType));

		node.getBody().statements().add(vdStatement);
		return vdFragment;
	}

	/**
	 * メソッドに対して普通の変数宣言を追加する<br>
	 * ただしプリミティブ型以外<br>
	 * ex: String str;
	 * 
	 * @param node
	 *            弄りたいメソッド
	 * @param typeName
	 *            追加したい変数のクラス名
	 * @param fieldName
	 *            追加したい変数の変数名
	 * @return 追加した変数宣言<br>
	 *         実際は"String str;"全体ではなく"str"の部分
	 */
	@SuppressWarnings("unchecked")
	private VariableDeclarationFragment addVariableDec(MethodDeclaration node,
			String typeName, String fieldName) {
		AST ast = node.getAST();
		//
		VariableDeclarationFragment vdFragment = ast
				.newVariableDeclarationFragment();
		vdFragment.setName(ast.newSimpleName(fieldName));
		//
		VariableDeclarationStatement vdStatement = ast
				.newVariableDeclarationStatement(vdFragment);
		vdStatement.setType(ast.newSimpleType(ast.newName(typeName)));

		node.getBody().statements().add(vdStatement);
		return vdFragment;
	}

	/**
	 * メソッドに対して配列の変数宣言を追加する<br>
	 * ex: String[][] str;
	 * 
	 * @param node
	 *            弄りたいメソッド
	 * @param typeName
	 *            追加したい変数のクラス名
	 * @param fieldName
	 *            追加したい変数の変数名
	 * @param depth
	 *            配列の次元
	 * @return 追加した変数宣言<br>
	 *         実際は"String[][] str;"全体ではなく"str"の部分
	 */
	@SuppressWarnings("unchecked")
	private VariableDeclarationFragment addVariableDec(MethodDeclaration node,
			String typeName, String fieldName, int depth) {
		AST ast = node.getAST();
		//
		VariableDeclarationFragment vdFragment = ast
				.newVariableDeclarationFragment();
		vdFragment.setName(ast.newSimpleName(fieldName));
		//
		Type filedType = ast.newSimpleType(ast.newName(typeName));
		for (int i = 0; i < depth; i++) {
			filedType = ast.newArrayType(filedType);
		}
		//
		VariableDeclarationStatement vdStatement = ast
				.newVariableDeclarationStatement(vdFragment);
		vdStatement.setType(filedType);

		node.getBody().statements().add(vdStatement);
		return vdFragment;
	}

	/**
	 * メソッドに対してジェネリック型の変数宣言を追加する<br>
	 * ex: List&lt;String&gt; list;
	 * 
	 * @param node
	 *            弄りたいメソッド
	 * @param typeName
	 *            追加したい変数のクラス名
	 * @param fieldName
	 *            追加したい変数の変数名
	 * @param generics
	 *            &lt;...&gt;の中身を列挙する
	 * @return 追加した変数宣言<br>
	 *         実際は"List&lt;String&gt; list;"全体ではなく"list"の部分
	 */
	@SuppressWarnings("unchecked")
	private VariableDeclarationFragment addVariableDec(MethodDeclaration node,
			String typeName, String fieldName, String[] generics) {
		AST ast = node.getAST();
		//
		VariableDeclarationFragment vdFragment = ast
				.newVariableDeclarationFragment();
		vdFragment.setName(ast.newSimpleName(fieldName));
		//
		ParameterizedType parameterizedType = ast.newParameterizedType(ast
				.newSimpleType(ast.newName(typeName)));
		for (String gen : generics) {
			parameterizedType.typeArguments().add(
					ast.newSimpleType(ast.newName(gen)));
		}
		//
		VariableDeclarationStatement vdStatement = ast
				.newVariableDeclarationStatement(vdFragment);
		vdStatement.setType(parameterizedType);

		node.getBody().statements().add(vdStatement);
		return vdFragment;
	}
}
