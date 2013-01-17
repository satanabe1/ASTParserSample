package info.haxahaxa.astparser.sample.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * メソッドの中身を削除したり，フィールドを削除する
 * 
 * @author satanabe1
 * 
 */
public class SampleVisitor2 extends ASTVisitor {
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

	/**
	 * メソッド内の処理をdelete!
	 */
	public boolean visit(MethodDeclaration node) {
		node.getBody().statements().clear();
		return super.visit(node);
	}
}
