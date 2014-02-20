package info.haxahaxa.astparser.sample.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * プロキシメソッドの生成
 * 
 * @author satanabe1
 */
public class SampleVisitor7 extends ASTVisitor {

	@SuppressWarnings("unchecked")
	public boolean visit(TypeDeclaration node) {
		List<Object> memberDecl = new ArrayList<Object>();
		memberDecl.addAll(node.bodyDeclarations());
		for (Object mem : memberDecl) {
			if (mem instanceof MethodDeclaration) {
				MethodDeclaration methodDecl = (MethodDeclaration) mem;
				MethodDeclaration proxyMethod = generateProxy(methodDecl);
				appendJavadoc(methodDecl, "コレが改名された本体");
				appendJavadoc(proxyMethod, "これがプロキシメソッド");
			}
		}
		return super.visit(node);
	}

	/**
	 * プロキシメソッドを生成する<br>
	 * 1.メソッド宣言のコピー<br>
	 * 2.コピー元のメソッド宣言の改名<br>
	 * 3.コピー先メソッド宣言のbodyへ、コピー元へのメソッド呼び出しを追加<br>
	 * の3つの処理をする
	 * 
	 * @param methodDecl
	 *            プロキシを作りたいメソッド
	 * @return プロキシメソッド
	 */
	@SuppressWarnings("unchecked")
	private MethodDeclaration generateProxy(MethodDeclaration methodDecl) {
		AST ast = methodDecl.getAST();
		TypeDeclaration classDecl = (TypeDeclaration) methodDecl.getParent();
		// まず、メソッドのコピーを作る
		MethodDeclaration copied = copy(methodDecl, classDecl);
		// コピーができたら、元々のメソッド宣言は名前を変更してしまう
		methodDecl.setName(ast.newSimpleName("$" + methodDecl.getName()));
		// コピーして出来たメソッド宣言はプロキシにするために空っぽにする
		copied.setBody(ast.newBlock());
		// 次に、コピーされたメソッドを呼び出すコードを生成する(コピーが実体ということにする)(元から存在したメソッド宣言をプロキシということにする)

		// メソッド呼び出し
		MethodInvocation invocation = ast.newMethodInvocation();
		// 呼び出し対象のメソッド名を設定
		invocation.setName(ast.newSimpleName(methodDecl.getName().toString()));
		// 引数を設定
		for (Object obj : methodDecl.parameters()) {
			SingleVariableDeclaration svd = (SingleVariableDeclaration) obj;
			invocation.arguments().add(
					ast.newSimpleName(svd.getName().toString()));
		}
		Statement invokeStatement = null;
		if (methodDecl.getReturnType2().isPrimitiveType()
				&& PrimitiveType.toCode(methodDecl.getReturnType2().toString())
						.equals(PrimitiveType.VOID)) {
			invokeStatement = ast.newReturnStatement();
			((ReturnStatement) invokeStatement).setExpression(invocation);
		} else {
			invokeStatement = ast.newExpressionStatement(invocation);
		}
		// 空っぽのメソッド宣言に、メソッド呼び出し文を追加する
		copied.getBody().statements().add(invokeStatement);
		return copied;
	}

	/**
	 * メソッド宣言をコピーする
	 * 
	 * @param srcMethodDecl
	 *            コピー元のメソッド宣言
	 * @param distClassDecl
	 *            コピーしたメソッドをはりつける先のクラス宣言
	 * @return コピーしてできたメソッド宣言
	 */
	@SuppressWarnings("unchecked")
	private MethodDeclaration copy(MethodDeclaration srcMethodDecl,
			TypeDeclaration distClassDecl) {
		AST ast = distClassDecl.getAST();
		MethodDeclaration copied = ast.newMethodDeclaration();

		// メソッド名の先頭に copied$ をつける
		// copied.setName(ast.newSimpleName(srcMethodDecl.getName().toString()));
		copied.setName((SimpleName) ASTNode.copySubtree(ast,
				srcMethodDecl.getName()));
		// public とか static とかをコピー
		copied.modifiers().addAll(
				ASTNode.copySubtrees(ast, srcMethodDecl.modifiers()));
		// 戻り値型をコピー
		copied.setReturnType2((Type) ASTNode.copySubtree(ast,
				srcMethodDecl.getReturnType2()));
		// 引数をコピー
		copied.parameters().addAll(
				ASTNode.copySubtrees(ast, srcMethodDecl.parameters()));
		// 中身をコピー
		copied.setBody((Block) ASTNode.copySubtree(ast, srcMethodDecl.getBody()));
		// クラス宣言の中身にコピーしたメソッドを追加
		distClassDecl.bodyDeclarations().add(copied);
		return copied;
	}

	/**
	 * おまけ<br>
	 * メソッド宣言にjavadocをくっつける
	 * 
	 * @param methodDecl
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Javadoc appendJavadoc(MethodDeclaration methodDecl, String text) {
		AST ast = methodDecl.getAST();
		TextElement textElement = ast.newTextElement();
		textElement.setText(text);
		TagElement tag = ast.newTagElement();
		tag.fragments().add(textElement);
		Javadoc javadoc = ast.newJavadoc();
		javadoc.tags().add(tag);
		methodDecl.setJavadoc(javadoc);
		return javadoc;
	}
}
