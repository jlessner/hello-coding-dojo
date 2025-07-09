String minutesTrailer = (ageInSeconds / 60) > 1 ? "minutes" : "minute";
return ageInSeconds / 60 + " " + minutesTrailer + secondsPortion;
