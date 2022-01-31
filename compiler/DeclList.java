package compiler;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import ast.ASTNode;

public class DeclList {

	private List<Entry<String, ASTNode>> declList;

	public DeclList() {
		declList = new ArrayList<>();
	}

	public void add(String id, ASTNode node) {
		declList.add(new AbstractMap.SimpleEntry<>(id, node));
	}

	public Iterator<Entry<String, ASTNode>> iterator() {
		return declList.iterator();
	}
}
