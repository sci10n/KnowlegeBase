package se.sciion.KB;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Pattern;

public class Utils {

	private final static String groundRegex = "^[a-z0-9\\.\\-]+";
	private final static String variableRegex = "^[_A-Z]";
	
	private final static Pattern groundPattern;
	private final static Pattern variablePattern;
	
	private final static Stack<Integer> permutations;
	
	static {
		groundPattern = Pattern.compile(groundRegex);
		variablePattern =  Pattern.compile(variableRegex);
		
		permutations = new Stack<Integer>();
	}
	
	public final static String[] parse(final String clause) {
		final String name = clause.substring(0, clause.indexOf('('));
		final String paramList = clause.substring(clause.indexOf('(') + 1, clause.indexOf(')'));
		
		final String[] split = paramList.split(",");
		String[] params = new String[split.length + 1];
		params[0] = name;
		for(int i = 1; i < params.length; i++) {
			params[i] = split[i-1];
		}
		return params;
	}
	
	public final static ArrayList<String[]> getSubsets(ArrayList<String> set, int subsetSize){
		ArrayList<String[]> subsets = new ArrayList<>();
		subset(set, subsetSize, 0, subsets);
		return subsets;
	}
	
	private final static void subset(ArrayList<String> set, int subsetSize, int depth, ArrayList<String[]> subsets) {
		if(depth >= subsetSize) {
			String[] subset = new String[subsetSize];
			for(int i = 0; i < subsetSize; i++) {
				subset[i] = set.get(permutations.get(i));
			}
			subsets.add(subset);
			return;
		}
		
		for(int i = 0; i < set.size(); i++) {
			permutations.push(i);
			subset(set, subsetSize, depth + 1, subsets);
			permutations.pop();
		}
	}
	
	public final static String build(String name, String ...parameters) {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append('(');
		for(int i = 0; i < parameters.length-1; i++) {
			builder.append(parameters[i]);
			builder.append(',');
		}
		builder.append(')');
		return builder.toString();
	}
	
	public final static boolean matchGround(final String string) {
		return groundPattern.matcher(string).find();
	}
	
	public final static boolean matchVariable(final String string) {
		return variablePattern.matcher(string).find();

	}
}
