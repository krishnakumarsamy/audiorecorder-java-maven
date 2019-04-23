This project is used to record voice and upload filed to google drive.

Used Core Java with multi threading. It will create separate audio files for each minute recording.

Also, after creating 5 audio files, it will encrypt and upload it into Google Drive.

How to Run:
mvn package
Right click -> App.java and run as java program

Or

Navigate to the project folder using command prompt.
mvn package
cd target
java -jar systemprogram-1.0-SNAPSHOT-shaded.jar


Or we can run this process in background using below command without displaying logs in command prompt.

start javaw -jar systemprogram-1.0-SNAPSHOT-shaded.jar

