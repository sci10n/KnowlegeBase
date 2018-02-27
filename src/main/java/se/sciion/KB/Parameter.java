package se.sciion.KB;

public class Parameter {

	private String value;
	
	public Parameter(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean isGround() {
		return Utils.matchGround(value);
	}
	
	@Override
	public String toString() {
		return value;
	}
}
