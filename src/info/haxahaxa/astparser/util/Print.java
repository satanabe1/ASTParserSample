package info.haxahaxa.astparser.util;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

/**
 * 
 * @author satanabe1
 * 
 */
public class Print {
	private static final String STYLE_TITLE = Envs.getLineSeparator()
			+ "/* ===================================================== * "
			+ Envs.getLineSeparator() //
			+ " *   %-50s " //
			+ Envs.getLineSeparator() //
			+ " * ===================================================== */";

	private static final String STYLE_HEADLINE = ">>> %-50s ";
	private static final String STYLE_PLAIN = "%-16s: %s";

	public static void printTitle(String title) {
		System.out.println(String.format(STYLE_TITLE, title));
	}

	public static void printHeadline(String header) {
		System.out.println(String.format(STYLE_HEADLINE, header));
	}

	public static void printModifiers(String tag, int modifiers) {
		StringBuilder sb = new StringBuilder();
		if (Modifier.isAbstract(modifiers))
			sb.append(ModifierKeyword.ABSTRACT_KEYWORD + " ");
		if (Modifier.isFinal(modifiers))
			sb.append(ModifierKeyword.FINAL_KEYWORD + " ");
		if (Modifier.isNative(modifiers))
			sb.append(ModifierKeyword.NATIVE_KEYWORD + " ");
		if (Modifier.isPrivate(modifiers))
			sb.append(ModifierKeyword.PRIVATE_KEYWORD + " ");
		if (Modifier.isProtected(modifiers))
			sb.append(ModifierKeyword.PROTECTED_KEYWORD + " ");
		if (Modifier.isPublic(modifiers))
			sb.append(ModifierKeyword.PUBLIC_KEYWORD + " ");
		if (Modifier.isStatic(modifiers))
			sb.append(ModifierKeyword.STATIC_KEYWORD + " ");
		if (Modifier.isStrictfp(modifiers))
			sb.append(ModifierKeyword.STRICTFP_KEYWORD + " ");
		if (Modifier.isSynchronized(modifiers))
			sb.append(ModifierKeyword.SYNCHRONIZED_KEYWORD + " ");
		if (Modifier.isTransient(modifiers))
			sb.append(ModifierKeyword.TRANSIENT_KEYWORD + " ");
		if (Modifier.isVolatile(modifiers))
			sb.append(ModifierKeyword.VOLATILE_KEYWORD + " ");
		System.out.println(String.format(STYLE_PLAIN, tag, sb));
	}

	public static void printMessage(String tag, String mes) {
		System.out.println(String.format(STYLE_PLAIN, tag, mes));
	}

	public static void printMessage(String tag, ITypeBinding[] mes) {
		StringBuilder sb = null;
		if (mes != null) {
			sb = new StringBuilder();
			for (ITypeBinding obj : mes) {
				sb.append(obj.getBinaryName() + ", ");
			}
		}
		System.out.println(String.format(STYLE_PLAIN, tag, sb));
	}
}
