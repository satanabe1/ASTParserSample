package info.haxahaxa.astparser.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jface.text.Document;

/**
 * 文字列からASTParserで扱えるTypeノードを生成するクラス<br>
 * プリミティブもいけるよ！<br>
 * 配列もいけるよ！<br>
 * ジェネリクスもいけるよ！<br>
 * 
 * @author satanabe1
 * 
 */
public class TypeBuilder {

	private Type type;

	/**
	 * 
	 * @param typeString
	 *            生成したいクラスの文字列<br>
	 *            ex: new TypeBuilder("double")<br>
	 *            ex: new TypeBuilder("Hoge[][]")<br>
	 * 
	 */
	public TypeBuilder(final String typeString) {
		final List<Type> types = new ArrayList<Type>();
		// ";"が一つでもあれば弾く
		if (typeString.contains(";")) {
			throw new IllegalArgumentException(typeString);
		}

		Document doc = new Document("class tmpclass { " + typeString
				+ " tmpfield;}\n");
		// コンパイルオプションはsourceとtargetだけ1.7に
		Map<String, String> comap = new HashMap<String, String>();
		comap.put(JavaCore.COMPILER_SOURCE, "1.7");
		comap.put(JavaCore.COMPILER_COMPLIANCE, "1.7");
		ASTParser astParser = ASTParser.newParser(AST.JLS4);
		astParser.setUnitName("tmpclass");
		astParser.setCompilerOptions(comap);
		astParser.setSource(doc.get().toCharArray());
		CompilationUnit unit = (CompilationUnit) astParser
				.createAST(new NullProgressMonitor());
		unit.accept(new ASTVisitor() {
			public boolean visit(FieldDeclaration node) {
				// publicとかstaticとか、余計な修飾子がついてたら弾く
				if (Modifier.NONE != node.getModifiers()) {
					throw new IllegalArgumentException(typeString);
				}
				types.add(node.getType());
				return super.visit(node);
			}
		});

		// IProblemが一つでもあれば弾く
		if (unit.getProblems().length > 0) {
			throw new IllegalArgumentException(typeString);
		}
		type = types.get(0);
	}

	/**
	 * Typeノードを生成する
	 * 
	 * @param target
	 *            Typeノードを追加したいAST
	 * @return Typeノード
	 */
	public Type build(AST target) {
		return (Type) ASTNode.copySubtree(target, type);
	}
}
