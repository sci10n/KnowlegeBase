package se.sciion.KB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.sciion.KB.resolution.ResolutionTree;

/*
 * Contains a system for storing ground facts. Results are false according to CWA
 */
public class KnowlegeBase {

	// Store facts as hashed string
	private HashSet<String> facts;
	private HashSet<String> universe;
	
	// Store object references for facts
	private HashMap<String,Object> objectMap;
	
	public KnowlegeBase() {
		facts 	= new HashSet<>();
		universe = new HashSet<>();
		objectMap = new HashMap<String,Object>();
	}
		
	public boolean add(String fact) {
		if(fact.startsWith("!")) {
			System.err.print("Negation can only be used in lookup");
			return false;
		}
		
		String[] tmp = Utils.parse(fact);
		for(String s: Arrays.copyOfRange(tmp, 1, tmp.length)) {
			universe.add(s);
		}
		
		return facts.add(fact);
	}
	
	public <T> void add(String fact, T t) {
		facts.add(fact);
		
		String[] tmp = Utils.parse(fact);
		for(String s: Arrays.copyOfRange(tmp, 1, tmp.length)) {
			universe.add(s);
		}
		
		objectMap.put(fact, t);
	}
	
	public boolean remove(String fact) {
		if(fact.startsWith("!")) {
			System.err.print("Negation can only be used in lookup");
			return false;
		}
		
		ArrayList<Clause> matches = fetch(fact);
		if(matches.isEmpty()) {
			return false;
		}
		
		boolean check = true;
		for(final Clause c: matches) {
			// Remove done using variables
			if(c.containsVariables()) {
				System.err.println("Clause " + c + " contains variables during fact removal");
				continue;
			}
			
			String c_str = c.toString();
			if(objectMap.containsKey(c_str)) {
				objectMap.remove(c_str);
			}
			
			check &= facts.remove(c_str);
		}
		
		return check;
	}
	
	public <T> T query(String fact, Class<T> clazz) {
		if(!lookup(fact))
			return null;
		
		if(objectMap.get(fact).getClass().isAssignableFrom(clazz)) {
			return (T) objectMap.get(fact);	
		}
		return null;
	}
	
	public <T> T query(Clause fact, Class<T> clazz) {
		return query(fact.toString(), clazz);
	}
	
	// Only works with ground
	public boolean lookup(Clause clause) {
		return lookup(clause.toString());
	}
	
	public boolean lookup(String fact) {
		boolean negation = fact.startsWith("!");
		if(negation) {
			String modFact = fact.substring(1);
			
			return !lookup(modFact);
		}
		
		Clause c = new Clause(Utils.parse(fact));

		if(!c.containsVariables())
			return facts.contains(fact);
		else
			return lookup_lifted(c);
	}
	
	private boolean lookup_lifted(Clause fact) {
		Pattern regex = Pattern.compile(fact.getMatchRegex());
		for(String f: facts) {
			Matcher m = regex.matcher(f);
			if(m.matches())
				return true;
		}
		return false;
	}
	
	
	public final HashSet<String> getUniverse() {
		return universe;
	}
	
	// Fetches all entries which matches the given clause. Limit symbol identifies to a-z0-1,
	// parameters with other characters will be treaded as variables.
	public ArrayList<Clause> fetch(String clause) {
		ResolutionTree tree = new ResolutionTree();
		return  tree.resolv(clause, this);
	}

	@Override
	public String toString() {
		String output = "Knowlege Base\n";
		
		List<String> tmp = new ArrayList<String>(facts);
		Collections.sort(tmp);
		for(String f: tmp) {
			output += f+ "\n";
		}
		output += "==== Object Map ====\n";
		tmp = new ArrayList<String>(objectMap.keySet());
		Collections.sort(tmp);
		for(String k: tmp) {
			output += k + " -> " + objectMap.get(k) + "\n";
		}
		return output;
	}
}
