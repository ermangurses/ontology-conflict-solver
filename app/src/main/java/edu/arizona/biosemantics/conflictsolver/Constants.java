package edu.arizona.biosemantics.conflictsolver;


public class Constants {
    private static final String BASE_URL            = "http://shark.sbs.arizona.edu/ontology-conflict-solver-server/dev/";
    public  static final String URL_REGISTER        = BASE_URL + "Register/RegisterExpert.php";
    public  static final String URL_LOGIN           = BASE_URL + "Login/LoginExpert.php";
    public  static final String URL_GETTASKS        = BASE_URL + "DailyOperations/GetTasks.php";
    public  static final String URL_GETOPTIONS      = BASE_URL + "DailyOperations/GetOptions.php";
    public  static final String URL_PROCESSDECISION = BASE_URL + "DailyOperations/ProcessDecision.php";
    public  static final String URL_AUDIO           = BASE_URL + "DailyOperations/FileUploadApi/Audio/";
    public  static final String URL_REGISTERTOKEN   = BASE_URL + "DailyOperations/RegisterToken.php";
    public  static final String URL_ISREGISTERED    = BASE_URL + "DailyOperations/IsRegistered.php";
    public  static final String URL_GETCOUNT        = BASE_URL + "DailyOperations/GetCount.php";
}
