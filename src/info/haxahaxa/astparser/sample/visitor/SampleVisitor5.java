package info.haxahaxa.astparser.sample.visitor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
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
		addFieldDec(node, Modifier.PRIVATE, "Hoge", "fuga");

		// Map<String, Integer>型メソッドmogeを生成
		MethodDeclaration methodDeclaration = addMethodDec(node,
				Modifier.PUBLIC | Modifier.SYNCHRONIZED,
				"java.util.Map<String,Integer>", "moge");
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
		fieldDec.setType(ast.newSimpleType(ast.newName(typeName)));
		// フィールドにmodifierを追加する
		fieldDec.modifiers().addAll(ast.newModifiers(modifiers));
		node.bodyDeclarations().add(fieldDec);
		return vdFragment;
	}

	private MethodDeclaration addMethodDec(TypeDeclaration node, int modifiers,
			PrimitiveType returnType, String methodName) {
		return null;
	}

	@SuppressWarnings("unchecked")
	private MethodDeclaration addMethodDec(TypeDeclaration node, int modifiers,
			String returnTypeName, String methodName) {
		AST ast = node.getAST();

		if (isGenericsType(returnTypeName)) {
			System.out.println("HIT");
			Pattern pattern = Pattern
					.compile("(\"[^\"]*(?:\"\"[^\"]*)*\"|[^,]*),");
		}
		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
		methodDeclaration.modifiers().addAll(ast.newModifiers(modifiers));// mod
		methodDeclaration.setReturnType2(ast.newSimpleType(ast
				.newName(returnTypeName)));// type
		methodDeclaration.setName(ast.newSimpleName(methodName));// name
		node.bodyDeclarations().add(methodDeclaration);
		return methodDeclaration;
	}

	private boolean isGenericsType(String typeName) {
		return Pattern.matches(".*<.*>", typeName);
	}

	private Type getType(String typeName) {
		return null;
	}
}
