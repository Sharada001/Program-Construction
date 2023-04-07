import java.util.ArrayList;

// interface specifying essential methods for MusicalPerformance class
interface IMusicalPerformance {
	public void viewVenue();
	public void viewYear();
	public void viewPerformanceName();
	public void setPerformanceName(String performanceName);
	public void viewTrackList(Artist artist);
	public void changeTrackList(String[] trackList);
	public void activateFeatures();
}

// interface specifying essential methods for MusicTrack class
interface IMusicTrack {
	public void viewMusicTrack();
}

// interface specifying essential methods for Artist Abstract class
interface IArtist {
	public void perform();
}

// interface specifying essential methods for Singer Abstract class
interface ISinger {
	public void sing();
}

// interface specifying essential methods for MainArtist class
interface IMainArtist extends ISinger {
	public abstract void perform();
}

// interface specifying essential methods for Backup Singers and Backup Dancers
interface IBackup {
	public void backup(MainArtist mainArtist);
	public abstract void perform(MainArtist mainArtist);
}

// interface specifying essential methods for BackupSinger class
interface IBackupSinger extends IBackup {
}

// interface specifying essential methods for BackupDancer class
interface IBackupDancer extends IBackup{
	public void dance();
}

// class to create music performance object
class MusicalPerformance implements IMusicalPerformance{
	private String venue;
	private int year;
	private String performanceName;
	private MainArtist mainArtist;
	// arraylist to store all MusicTrack objects of the performance
	private ArrayList<MusicTrack> trackList = new ArrayList<MusicTrack>();
	// arraylist to store all BackupSinger objects of the performance
	private ArrayList<BackupSinger> backupSingers = new ArrayList<BackupSinger>();
	// arraylist to store all BackupDancer objects of the performance
	private ArrayList<BackupDancer> backupDancers = new ArrayList<BackupDancer>();
	private String performanceType;
	
	// constructor
	public MusicalPerformance(String mainArtistName,String performanceName,int year,String venue,String[] trackList,String[] backupSingers,String[] backupDancers,String performanceType) {
		this.mainArtist = new MainArtist(mainArtistName, performanceType);
		this.performanceName = performanceName;
		this.year = year;
		this.venue = venue;
		// creates MusicTrack objects one by one and they are added to arraylist
		for (int i=0; i<trackList.length; i++) {
			MusicTrack trackObject = new MusicTrack(trackList[i]);
			this.trackList.add(trackObject);
		}
		// creates BackupSingers objects one by one and they are added to arraylist
		for (int i=0; i<backupSingers.length; i++) {
			BackupSinger singerObject = new BackupSinger(backupSingers[i]);
			this.backupSingers.add(singerObject);
		}
		// creates BackupDancers objects one by one and they are added to arraylist
		for (int i=0; i<backupDancers.length; i++) {
			BackupDancer dancerObject = new BackupDancer(backupDancers[i]);
			this.backupDancers.add(dancerObject);
		}
		this.performanceType = performanceType;
	}
	public void viewVenue() {
		System.out.println("Venue of the performance is " + this.venue + "...");
		System.out.println("---- This is the method viewVenue() in the class MusicalPerformance ----");
	}
	public void viewYear() {
		System.out.println("Venue of the performance is " + this.year + "...");
		System.out.println("---- This is the method viewYear() in the class MusicalPerformance ----");
	}
	public void viewPerformanceName() {
		System.out.println("Name of the performance is " + this.performanceName + "...");
		System.out.println("---- This is the method viewPerformanceName() in the class MusicalPerformance ----");
	}
	public void setPerformanceName(String performanceName) {
		this.performanceName = performanceName;
		System.out.println("---- This is the method setPerformanceName() in the class MusicalPerformance ----");
	}
	public void viewTrackList(Artist artist) {
		System.out.println(artist.name+" viewed the tracklist...");
		System.out.println("---- This is the method viewTrackList() in the class MusicalPerformance ----");
	}
	public void changeTrackList(String[] trackList) {
		this.trackList = new ArrayList<MusicTrack>();
		for (int i=0; i<trackList.length; i++) {
			MusicTrack trackObject = new MusicTrack(trackList[i]);
			this.trackList.add(trackObject);
		}
		System.out.println("---- This is the method changeTrackList() in the class MusicalPerformance ----");
	}
	private void audioProcessing() {
		System.out.println("Audio processing started...");
		System.out.println("---- This is the method audioProcessing() in the class MusicalPerformance ----");
	}
	private void recordPerformance() {
		System.out.println("Recording live performance started...");
		System.out.println("---- This is the method recordPerformance() in the class MusicalPerformance ----");
	}
	public void activateFeatures() {
		if (performanceType=="studio") {
			this.audioProcessing();
		} else {
			this.recordPerformance();
		}
		System.out.println("---- This is the method activateFeatures() in the class MusicalPerformance ----");
	}
	// method to start performings of artists
	public void startMusicalPerformance() {
		System.out.println("Welcome to the Performance "+this.performanceName+" by "+this.mainArtist.name);
		this.activateFeatures();
		this.mainArtist.perform();
		for (int i=0; i<this.backupSingers.size(); i++) {
			this.backupSingers.get(i).perform(this.mainArtist);
		}
		for (int i=0; i<this.backupDancers.size(); i++) {
			this.backupDancers.get(i).perform(this.mainArtist);
		}
		this.viewPerformanceName();
		this.viewVenue();
		this.viewYear();
		this.viewTrackList(this.mainArtist);
		System.out.println("---- This is the method startMusicalPerformance() in the class MusicalPerformance ----");
	}
}

// class to create MusicTrack objects
class MusicTrack {
	private String trackName;
	private double trackLength;
	//constructor
	public MusicTrack(String trackName) {
		this.trackName = trackName;
	}
	public void viewMusicTrack() {
		System.out.println(this.trackName);
		System.out.println("---- This is the method viewMusicTrack() in the class MusicTrack ----");
	}
}

// abstract class defining functionality common for all artists
abstract class Artist {
	protected String name;
	// constructor
	public Artist(String name) {
		this.name = name;
	}
}

// abstract class defining functionality common for both of Main and backup singers
abstract class Singer extends Artist implements ISinger {
	public Singer(String name) {
		super(name);
	}
	public void sing() {
		System.out.println(this.name + " is singing...");
		System.out.println("---- This is the method sing() in the class Singer ----");
	}
}

// class to create MainArtist object
class MainArtist extends Singer implements IMainArtist {
	private boolean isInteractingAvailable;
	// constructor
	public MainArtist(String name, String performanceType) {
		super(name);
		if (performanceType=="live") {
			this.isInteractingAvailable = true;
		} else {
			this.isInteractingAvailable = false;
		}
	}
	public void interactWithAudience() {
		System.out.println(this.name+" interacted with audience...");
		System.out.println("---- This is the method interactWithAudience() in the class MainArtist ----");

	}
	public void perform() {
		this.sing();
		if (this.isInteractingAvailable) {
			this.interactWithAudience();
		}
		System.out.println("---- This is the method perform() in the class MainArtist ----");
	}
}

// class to create backup singer objects
class BackupSinger extends Singer implements IBackupSinger {
	// constructor
	public BackupSinger(String name) {
		super(name);
	}
	public void backup(MainArtist mainArtist) {
		System.out.println(this.name+" is backing up "+mainArtist.name+"...");
		System.out.println("---- This is the method backup() in the class BackupSinger ----");
	}
	public void perform(MainArtist mainArtist) {
		this.sing();
		this.backup(mainArtist);
		System.out.println("---- This is the method perform() in the class BackupSinger ----");
	}
}

// class to create backup dancer objects
class BackupDancer extends Artist implements IBackupDancer {
	// constructor
	public BackupDancer(String name) {
		super(name);
	}
	public void dance() {
		System.out.println(this.name + " is dancing...");
		System.out.println("---- This is the method dance() in the class BackupDancer ----");
	}
	public void backup(MainArtist mainArtist) {
		System.out.println(this.name+" is backing up "+mainArtist.name+"...");
		System.out.println("---- This is the method backup() in the class BackupDancer ----");
	}
	public void perform(MainArtist mainArtist) {
		this.dance();
		this.backup(mainArtist);
		System.out.println("---- This is the method perform() in the class BackupDancer ----");
	}
}

// Main function of the program to create and run a musical performance
public class App {
		public static void main(String[] args) {
			String mainArtistName = "Taylor Swift";
			String performanceName = "Eras Tour";
			int year = 2023;
			String venue = "Glendale";
			String[] tracksArray = {"Lavender Haze", "All Too Well", "The lakes", "The Man", "Love Story"};
			String[] backupSingersArray = {"Jeslyn", "Melanie"};
			String[] backupDancersArray = {"Stephanie", "Jake"};
			String performanceType = "live";
			MusicalPerformance performance_1 = new MusicalPerformance(mainArtistName, performanceName, year, venue, tracksArray, backupSingersArray, backupDancersArray, performanceType);
			performance_1.startMusicalPerformance();
	}
}

