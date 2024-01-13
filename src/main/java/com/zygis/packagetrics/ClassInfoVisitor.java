package com.zygis.packagetrics;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

public class ClassInfoVisitor extends GenericVisitorAdapter<ClassInfo, Void> {

	@Override
	public ClassInfo visit(ClassOrInterfaceDeclaration n, Void arg) {
		String className = n.getFullyQualifiedName().get();
		ReferenceType classType = n.isInterface() ? ReferenceType.INTERFACE : n.isAbstract() ? ReferenceType.ABSTRACT_CLASS : ReferenceType.CLASS;
		return new ClassInfo(className, classType);
	}

	@Override
	public ClassInfo visit(RecordDeclaration n, Void arg) {
		String className = n.getFullyQualifiedName().get();
		return new ClassInfo(className, ReferenceType.RECORD);
	}

	@Override
	public ClassInfo visit(EnumDeclaration n, Void arg) {
		String className = n.getFullyQualifiedName().get();
		return new ClassInfo(className, ReferenceType.ENUM);
	}
}
