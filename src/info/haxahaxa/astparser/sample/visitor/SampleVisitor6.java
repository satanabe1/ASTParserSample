package info.haxahaxa.astparser.sample.visitor;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * コード生成サンプル<br>
 * メソッド呼び出し
 * 
 * @author satanabe1
 * 
 */
public class SampleVisitor6 extends ASTVisitor {

	public boolean visit(TypeDeclaration node) {
		return super.visit(node);
	}

	@SuppressWarnings("unchecked")
	public boolean visit(MethodDeclaration node) {
		AST ast = node.getAST();
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("moge.Moge"));

		methodInvocation.setName(ast.newSimpleName("hogehoge"));
		node.getBody().statements()
				.add(ast.newExpressionStatement(methodInvocation));
		return super.visit(node);
	}
}
