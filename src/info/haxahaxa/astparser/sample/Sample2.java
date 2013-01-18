package info.haxahaxa.astparser.sample;

import info.haxahaxa.astparser.sample.visitor.*;
import info.haxahaxa.astparser.util.Envs;
import info.haxahaxa.astparser.util.SourceFile;

import java.io.File;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

/**
 * メソッドの中身を削除したり，フィールドを削除する
 * 
 * @author satanabe1
 * 
 */
public class Sample2 {

	private static ASTVisitor visitor = new SampleVisitor2();

	public static void main(String[] arg) throws Exception {
		SourceFile sourceFile = new SourceFile("src" + File.separator
				+ "samples" + File.separator + "AntFileGen.java");
		CompilationUnit unit;
		ASTParser astParser = ASTParser.newParser(AST.JLS4);
		// 以下の setBindingsRecovery setStatementsRecovery はおまじない．
		// 完成しているソースコードを解析する時は呼ぶ必要ない．
		// 詳しく知りたいならば，IMBのASTParser関連のドキュメントとかを参照すべき．
		astParser.setBindingsRecovery(true);
		astParser.setStatementsRecovery(true);
		// 次の setResolveBindings と setEnvironment が重要！！
		// setResolveBindings(true) をしておかないとまともに解析はできない．
		// setResolveBindings をまともに機能させるために setEnvironment が必要．
		astParser.setResolveBindings(true);
		// setEnvironment の第一引数にはクラスパスの配列．第二引数にはソースコードを検索するパスの配列
		// 第三第四については何も考えず null, true ．納得いかない時はIBMのASTPa...
		astParser.setEnvironment(Envs.getClassPath(), Envs.getSourcePath(),
				null, true);

		// 解析対象のソースコードの入力とか
		astParser.setUnitName(sourceFile.getFilePath());// なんでもいいから名前を設定しておく
		astParser.setSource(sourceFile.getSourceCode().toCharArray());// 解析対象コードを設定する
		unit = (CompilationUnit) astParser.createAST(new NullProgressMonitor());
		unit.recordModifications();// ASTへの操作履歴のようなものを有効に
		// 解析 & 変換実行
		unit.accept(visitor);

		// 変換結果を文字列で取得
		String code = getCode(sourceFile.getSourceCode(), unit);
		System.out.println(code);
	}

	/**
	 * ASTを文字列のコードに戻すメソッド
	 * 
	 * @param code
	 *            元のコード
	 * @param unit
	 *            ASTVisitorで操作を行ったヤツ
	 * @return ソースコード
	 */
	private static String getCode(String code, CompilationUnit unit) {
		org.eclipse.jface.text.IDocument eDoc = new Document(code);
		TextEdit edit = unit.rewrite(eDoc, null);
		try {
			edit.apply(eDoc);
			return eDoc.get();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
