package socialtime;

public class SocialTime {
	String render(int seconds){
		if(seconds < 10){
			return "few seconds";
		}
		return null;
	}
}