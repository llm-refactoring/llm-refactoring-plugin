package ca.ualberta.cs.serl.wikidev;

import java.sql.Timestamp;

public class InvocationParser {
	
	private String databaseName;
	private String username;
	private String password;
	private String fromdate;
	private String todate;
	private int project;
	private double lowerBound;
	private double upperBound;
	private double interval;
	private String window;
	
	public InvocationParser() {
		this.databaseName = "";
		this.username = "";
		this.password = "";
		this.fromdate = "";
		this.todate = "";
		this.project = -1;
		this.lowerBound = 0.1;
		this.upperBound = 0.9;
		this.interval = 0.1;
		this.window = "";
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFromdate() {
		return fromdate;
	}

	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}

	public String getTodate() {
		return todate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	public int getProject() {
		return project;
	}

	public void setProject(int project) {
		this.project = project;
	}

	public double getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(double lowerBound) {
		this.lowerBound = lowerBound;
	}

	public double getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(double upperBound) {
		this.upperBound = upperBound;
	}

	public double getInterval() {
		return interval;
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}

	public String getWindow() {
		return window;
	}

	public void setWindow(String window) {
		this.window = window;
	}
	
	public void parseArgs(String[] args) {
		if(args.length < 7) {
			System.out.println("Please specify all required arguments (database, username, password, from date, to date, project and time window type");
			System.exit(1);
		}
		this.databaseName = args[0];
		this.username = args[1];
		this.password = args[2];
		try {
			Timestamp.valueOf(args[3]);
			Timestamp.valueOf(args[4]);
		}
		catch(IllegalArgumentException e) {
			System.out.println("Please specify the date in the format yyyy-mm-dd hh:mm:ss");
			System.exit(1);
		}
		this.fromdate = args[3];
		this.todate = args[4];
		try {
			this.project = Integer.parseInt(args[5]);
		}
		catch(NumberFormatException e) {
			System.out.println("Please specify the project id.");
			System.exit(1);
		}
		this.window = args[6];
		int i=7;
		while(i<args.length) {
			if(args[i].equals("-lb")) {
				i++;
				if(Double.parseDouble(args[i]) >=0.0) {
					this.lowerBound = Double.parseDouble(args[i]);
				}
				else {
					System.out.println("The lower bound cannot be less than 0.0");
					System.exit(1);
				}
				i++;
			}
			else if(args[i].equals("-ub")) {
				i++;
				if(Double.parseDouble(args[i]) <= 1.0) {
					this.upperBound = Double.parseDouble(args[i]);
				}
				else {
					System.out.println("The upper bound cannot be more than 1.0");
					System.exit(1);
				}
				i++;
			}
			else if(args[i].equals("-i")) {
				i++;
				if(Double.parseDouble(args[i]) >=0.01) {
					this.interval = Double.parseDouble(args[i]);
				}
				else {
					System.out.println("The interval cannot be less than 0.01");
					System.exit(1);
				}
				i++;
			}
		}
	}

}
