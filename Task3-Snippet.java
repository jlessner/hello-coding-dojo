String secondsPortion = (ageInSeconds % 60 < 10) ? "" : ", " + ageInSeconds % 60 + " seconds";
return ageInSeconds / 60 + " minute(s)" + secondsPortion;
