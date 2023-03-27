import java.util.ArrayList;

class Competition {
	private static Competition competition;
	ArrayList<Swimmer> swimmersList;
	ArrayList<Judge> judgesList;
	ArrayList<Spectator> spectatorsList;
	ArrayList<SupportingStaffPerson> supportingStaffList;
	Scoreboard scoreboard = new Scoreboard();
	SwimmingPool swimmingPool = new SwimmingPool(5,true);
	
	private Competition(int swimmers, int judges, int spectators, int supportingStaff) {
		ArrayList<Swimmer> List1 = new ArrayList<Swimmer>();
		for (int i=0; i<swimmers; i++) {
			Swimmer swimmer = new Swimmer(Integer.parseInt("1"+i),"Swimmer"+(i+1));
			List1.add(swimmer);
		}
		this.swimmersList = List1;
		
		ArrayList<Judge> List2 = new ArrayList<Judge>();
		for (int i=0; i<judges; i++) {
			Judge judge = new Judge(Integer.parseInt("2"+i),"Judge"+(i+1));
			List2.add(judge);
		}
		this.judgesList = List2;
		
		ArrayList<Spectator> List3 = new ArrayList<Spectator>();
		for (int i=0; i<spectators; i++) {
			Spectator spectator = new Spectator(Integer.parseInt("3"+i),"Spectator"+(i+1));
			List3.add(spectator);
		}
		this.spectatorsList = List3;
		
		ArrayList<SupportingStaffPerson> List4 = new ArrayList<SupportingStaffPerson>();
		for (int i=0; i<supportingStaff; i++) {
			SupportingStaffPerson supportingStaffPerson = new SupportingStaffPerson(Integer.parseInt("4"+i),"SupportingStaffPerson"+(i+1));
			List4.add(supportingStaffPerson);
		}
		this.supportingStaffList = List4;
		
	}
	public static Competition newCompetition(int swimmers, int judges, int spectators, int supportingStaff) {
		if(competition==null)
			competition = new Competition(swimmers, judges, spectators, supportingStaff);
		return competition;
	}
	public void startCompetition () {
		for (int i=0; i<this.swimmersList.size(); i++) {
			this.swimmersList.get(i).swim();
		}
	}
}

class SwimmingPool {
	int numberOfLanes;
	boolean isPavilionAvailable;
	
	public SwimmingPool(int numberOfLanes, boolean AvailabilityOfPavilion) {
		this.numberOfLanes = numberOfLanes;
		this.isPavilionAvailable = AvailabilityOfPavilion;
	}
}

class Person {
	String name;
	int id;
	
	public Person(int id, String name) {
		this.name = name;
		this.id = id;
	}
	public void checkScoreboard(Scoreboard scoreboard) {
		scoreboard.checkScoreboard(this);
	}
}

class Swimmer extends Person {
	static int finishedCount=0;
	int position;
	
	public Swimmer(int id, String name) {
		super(id, name);
	}
	public void swim() {
		System.out.println(name + " started swimming...");
		this.touchPad();
	}
	public void touchPad() {
		finishedCount += 1;
		this.position = finishedCount;
		Scoreboard.updateScoreboard(name+" finished at "+this.position+"th position");
	}
}

class Judge extends Person {
	public Judge (int id, String name) {
		super(id, name);
	}
	public void blowWhistle(Competition competition) {
		System.out.println("   "+this.name+" started competition !!!");
		competition.startCompetition();
	}
}

class Spectator extends Person {
	public Spectator (int id, String name) {
		super(id, name);
	}
}
class SupportingStaffPerson extends Person {
	public SupportingStaffPerson (int id, String name) {
		super(id, name);
	}
}

class Scoreboard {
	static String displayText = "";
	public static void updateScoreboard(String text) {
		displayText += text;
		displayText += "\n";
	}
	public void checkScoreboard(Person person) {
		System.out.println("\n"+person.name+" checked scoreboard.");
		System.out.println("Current scoreboard states~");
		System.out.print(displayText);
	}
}


public class SimulateCompetition {

	public static void main(String[] args) {
		int numberOfSwimmers = Integer.parseInt(args[0]);
		int numberOfJudges = Integer.parseInt(args[1]);
		int numberOfSpectators = Integer.parseInt(args[2]);
		int numberOfSupportingStaff = Integer.parseInt(args[3]);
		Competition competition = Competition.newCompetition(numberOfSwimmers, numberOfJudges, numberOfSpectators, numberOfSupportingStaff);
		if (numberOfJudges>0) {
			competition.judgesList.get(0).blowWhistle(competition);
		} else {
			System.out.println("Cannot start competition without a judge !");
		}
		if (numberOfSpectators>0) {
			competition.spectatorsList.get(numberOfSpectators-1).checkScoreboard(competition.scoreboard);
		} else {
			System.out.println("No spectators to watch competition");
		}
	}

}
