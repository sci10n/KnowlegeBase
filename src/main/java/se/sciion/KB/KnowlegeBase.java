package se.sciion.KB;

import java.util.HashSet;

/*
 * Contains a system for storing ground facts. Results are false according to CWA
 */
public class KnowlegeBase {

	private HashSet<String> facts;

	public KnowlegeBase() {
		facts 	= new HashSet<>();
	}
	
	public boolean add(String fact) {
		if(fact.startsWith("!")) {
			System.err.print("Negation can only be used in lookup");
			return false;
		}
		return facts.add(fact);
	}
	
	public boolean remove(String fact) {
		if(fact.startsWith("!")) {
			System.err.print("Negation can only be used in lookup");
			return false;
		}
		return facts.remove(fact);
	}
	
	public boolean lookup(String fact) {
		boolean negation = fact.startsWith("!");
		if(negation) {
			String modFact = fact.substring(1);
			return !facts.contains(modFact);
		}
		
		return facts.contains(fact);
	}
	
	@Override
	public String toString() {
		String output = "";
		for(String f: facts) {
			output += f + "\n";
		}
		return output;
	}
}
