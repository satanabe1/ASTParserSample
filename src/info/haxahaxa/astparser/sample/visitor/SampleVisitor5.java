package info.haxahaxa.astparser.sample.visitor;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
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
		int modifiers = Modifier.PRIVATE | Modifier.STATIC;// "private static"
		addSimpleFieldDec(node, modifiers, "Hoge", "fuga");
		addSimpleMethodDec(node, modifiers, "Moge", "piyo");
		return super.visit(node);
	}

	private void addSimpleFieldDec(TypeDeclaration node, int modifiers,
			String typeName, String fieldName) {
		AST ast = node.getAST();
		// まずは変数宣言とする
		VariableDeclarationFragment variableDec = ast
				.newVariableDeclarationFragment();
		
		FieldDeclaration fieldDec = ast.newFieldDeclaration(variableDec);
		fieldDec.setType(ast.newSimpleType(ast.newName(typeName)));
		node.bodyDeclarations().add(fieldDec);
	}

	private void addSimpleMethodDec(TypeDeclaration node, int modifiers,
			String returnTypeName, String methodName) {
		AST ast = node.getAST();

	}

}
