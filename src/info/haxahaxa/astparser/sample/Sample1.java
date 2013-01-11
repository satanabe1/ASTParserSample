package info.haxahaxa.astparser.sample;

import info.haxahaxa.astparser.Envs;
import info.haxahaxa.astparser.SourceFile;
import info.haxahaxa.astparser.sample.visitor.*;

import java.io.File;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;


/**
 * クラス名やフィールド，メソッドの概要を表示するサンプル
 * 
 * @author satanabe1
 * 
 */
public class Sample1 {
	private static ASTVisitor visitor = new SampleVisitor1();

	public static void main(String[] args) throws Exception {
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
		// 解析実行
		unit.accept(visitor);
	}
}
