package info.haxahaxa.astparser.sample.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * メソッドを削除したり，フィールドを削除するサンプル
 * 
 * @author satanabe1
 * 
 */
public class SampleVisitor2 extends ASTVisitor {

	/**
	 * メソッド宣言をdelete!
	 */
	public boolean visit(MethodDeclaration node) {
		node.delete();
		return super.visit(node);
	}

	/**
	 * フィールド宣言をdelete!
	 */
	public boolean visit(FieldDeclaration node) {
		node.delete();
		return super.visit(node);
	}

	/**
	 * javadocもついでにdelete!
	 */
	public boolean visit(Javadoc node) {
		node.delete();
		return super.visit(node);
	}

	/**
	 * ダメ押しにimportもdelete!
	 */
	public boolean visit(ImportDeclaration node) {
		node.delete();
		return super.visit(node);
	}
}
