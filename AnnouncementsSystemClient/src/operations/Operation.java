package operations;

public class Operation {
	protected String op;
	
	public Operation(String op) {
		this.op = op;
	}

	@Override
	public String toString() {
		return "Operation{" +
				"op='" + op + '\'' +
				'}';
	}
}
