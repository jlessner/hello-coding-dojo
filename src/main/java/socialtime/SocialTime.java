package socialtime;

public class SocialTime {

	String render(int age){
		int minutes = age/60;
		int seconds = age%60;

		if(age < 10){
			return "few seconds";
		} else if (minutes == 0) {
			return seconds + " seconds";
		} else {
			return minutes + " minute(s), " + seconds + " second(s)";
		}
	}

	//Task 2:
	// Periods of a minute or more as
	//„XX minute (s), YY seconds


}