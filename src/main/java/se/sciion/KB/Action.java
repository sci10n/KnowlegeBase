package se.sciion.KB;

import java.util.Arrays;

public class Action {

	final String[] precond;
	final String[] add_effect;
	final String[] delete_effect;
	
	public Action(String[] precond, String[] add_effect, String[] delete_effect) {
		this.precond = precond;
		this.add_effect = add_effect;
		this.delete_effect = delete_effect;
	}

	public String[] getPrecond() {
		return precond;
	}

	public String[] getAdd_effect() {
		return add_effect;
	}

	public String[] getDelete_effect() {
		return delete_effect;
	}
	
	public boolean perform(KnowlegeBase kb) {
		boolean check = true;
		for(String s: precond) {
			check &= kb.lookup(s);
		}
		if(check) {
			for(String s: add_effect) {
				kb.add(s);
			}
			for(String s: delete_effect) {
				check &= kb.remove(s);
			}
		}
		return check;
	}
	
	@Override
	public String toString() {
		String output = "";
		output += Arrays.toString(precond) + "\n";
		output += Arrays.toString(add_effect) + "\n";
		output += Arrays.toString(delete_effect);
		
		return output;
	}
}
