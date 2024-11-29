package socialtime;

public class SocialTime {

	String render(int age){
		int minutes = age%60;
		int seconds = age-minutes*60;

		if(age < 10){
			return "few seconds";
		}
		else {
			return seconds + " seconds";
		}
	}

	//Task 2:
	// Periods of a minute or more as
	//„XX minute (s), YY seconds


}