package se.sciion.KB.resolution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import se.sciion.KB.Clause;
import se.sciion.KB.KnowlegeBase;
import se.sciion.KB.Parameter;
import se.sciion.KB.Utils;

/*
 * clause1(x) :- clause2(x, ground1)
 * 
 * 
 */
public class ResolutionTree {
	
	public class Binding {
		
		public String value;
		public ArrayList<Parameter> parameters;
		
		public Binding() {
			parameters = new ArrayList<>();
		}
		
		@Override
		public String toString() {
			String out = "";
			for(Parameter p: parameters) {
				out += p.toString() + "\t";
			}
			return out;
		}
	}
	
	public ArrayList<Clause> resolv(String clause, KnowlegeBase base) {
		
		ArrayList<Clause> validClauses = new ArrayList<>();
		
		String[] strings;
		if(clause.contains(":-")) {
			strings = clause.split(":-");
		}
		else {
			strings = new String[]{clause};
		}
		
		Clause[] clauses = new Clause[strings.length];
		for(int i = 0; i < clauses.length; i++) {
			clauses[i] = new Clause(Utils.parse(strings[i]));
		}
		
		HashMap<String, Binding> bindings = new HashMap<>();
		
		for(Clause c: clauses) {
			for(Parameter p: c.getParameters()) {
				
				// We don't care for bindings if it is ground.
				if(p.isGround()) {
					continue;
				}
				
				// New binding
				if(!bindings.containsKey(p.getValue())) {
					Binding b = new Binding();
					bindings.put(p.getValue(),b);
				}
				// Assign binding to parameter
				bindings.get(p.getValue()).parameters.add(p);
			}
		}
		
		int numBindings = bindings.size();
		for(String[] subset: Utils.getSubsets(new ArrayList<String>(base.getUniverse()), numBindings)) {

			int bindingOffset = 0;
			
			for(Binding b: bindings.values()) {
				String universeSymbol = subset[bindingOffset];
				
				for(Parameter p: b.parameters) {
					p.setValue(universeSymbol);
				}
				bindingOffset++;
			}

			boolean valid = true;
			for(Clause c: clauses) {
				if(!base.lookup(c)) {
					valid = false;
				}
			}
			
			if(valid) {
				validClauses.add(new Clause(Utils.parse(clauses[clauses.length - 1].toString())));
			}
		}
		return validClauses;
		
	}
}
