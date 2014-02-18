package info.haxahaxa.astparser.sample.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * コード生成サンプル<br>
 * メソッド呼び出しと、戻り値の代入
 * 
 * @author satanabe1
 * 
 */
public class SampleVisitor6 extends ASTVisitor {

	public boolean visit(TypeDeclaration node) {

		return super.visit(node);
	}

	// @SuppressWarnings("unchecked")
	public boolean visit(MethodDeclaration node) {
		AST ast = node.getAST();
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(ast.newName("moge.Moge"));

		methodInvocation.setName(ast.newSimpleName("hogehoge"));
		// node.getBody().statements()
		// .add(ast.newExpressionStatement(methodInvocation));
		return super.visit(node);
	}

	public boolean visit(MethodInvocation node) {
		Expression r = node.getExpression();
		if (r instanceof Name) {
			Name name = (Name) r;

			for (Name fname : fieldNames) {
				if (fname.equals(name)) {
					System.out.println("HITTTTT");
				} else {
					System.out.println(fname.getFullyQualifiedName() + "/"
							+ name.getFullyQualifiedName());
				}
			}
		}
		return super.visit(node);
	}

	List<Name> fieldNames = new ArrayList<Name>();

	public boolean visit(FieldDeclaration node) {
		VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) node
				.fragments().get(0);

		fieldNames.add(variableDeclarationFragment.getName());

		System.err.println(variableDeclarationFragment.getName());
		return super.visit(node);
	}
}
