package se.sciion.KB;

public class Clause {

	private final String clauseName;
	private final int numParameters;
	private final Parameter[] parameters;
	
	public Clause(String ...parameters) {
		this.clauseName = parameters[0];
		this.numParameters = parameters.length - 1;
		this.parameters = new Parameter[this.numParameters];
		
		for(int i = 0; i < this.numParameters; i++) {
			this.parameters[i] = new Parameter(parameters[i + 1]); 
		}
	}
	
	public final String getName() {
		return clauseName;
	}
	
	public final String getParamter(int i) {
		return parameters[i].getValue();
	}
	
	public final int getNumParameters() {
		return numParameters;
	}
	
	public final Parameter[] getParameters() {
		return parameters;
	}
	
	public boolean containsVariables() {
		boolean check = false;
		for(final Parameter p: parameters) {
			check |= Utils.matchVariable(p.getValue());
		}
		return check;
	}

	public final String getMatchRegex() {
		String matchRegex = clauseName + "\\(";
		
		for(int i = 0; i < numParameters; i++) {
			if(i > 0)
				matchRegex += ',';
			if(Utils.matchGround(parameters[i].getValue()))
					matchRegex += parameters[i];
			else {
				matchRegex += "[a-z0-9\\.\\-]+";
			}
		}
		
		return matchRegex + "\\)";
	}
	
	public final String toString() {
		String out = clauseName + "(";
		for(int i = 0; i < numParameters; i++) {
			if(i > 0)
				out += ',';
			out += parameters[i];
		}
		return out + ")";
		
	}
}
